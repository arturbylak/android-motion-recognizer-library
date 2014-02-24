package com.bylak.network.neural.teach;

import com.bylak.network.layer.Layer;
import com.bylak.network.neural.NeuralNetwork;
import com.bylak.network.neural.Neuron;
import com.bylak.network.util.PermutationGenerator;
import com.bylak.network.util.SSECalculator;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 11.09.13
 * Time: 15:35
 * To change this template use File | Settings | File Templates.
 */
public final class OneOutputBackPropagationAlgorithm implements NeutralNetworkTeachingAlgorithm {

    public static final int PERMUTATION_START_INDEX = 0;
    private final LeaningFactorCalculator learningFactorCalculator;
    private final SSECalculator sseCalculator;
    private final PermutationGenerator permutationGenerator;
    private BackPropagationWeightStorage oldWeightStorage;
    private BackPropagationWeightStorage tempOldWeightStorage;

    public OneOutputBackPropagationAlgorithm() {
        this.learningFactorCalculator = new DefaultLearningFactorCalculator();
        this.sseCalculator = new SSECalculator();
        this.permutationGenerator = new PermutationGenerator();
    }

    @Override
    public void teach(final NeuralNetwork neuralNetwork, final EpochData epochData, final TeachConfiguration teachConfiguration) {
        int teachDataCount = epochData.getSize();
        int epochCount = teachConfiguration.getEpochCount();
        double learningFactor = teachConfiguration.getLearningFactor();
        double maxErrorValue = teachConfiguration.getMaxErrorValue();
        double momentumFactor = teachConfiguration.getMomentumFactor();

        oldWeightStorage = createStorage(neuralNetwork);
        tempOldWeightStorage = createStorage(neuralNetwork);

        teach(neuralNetwork, epochData, teachDataCount, epochCount, learningFactor, maxErrorValue, momentumFactor);
    }

    private void teach(NeuralNetwork neuralNetwork, EpochData epochData, int teachDataCount, int epochCount, double learningFactor, double maxErrorValue, double momentumFactor) {
        double lastSSE;
        double sse = Double.MAX_VALUE;

        for (int i = 0; i < epochCount && sse > maxErrorValue; i++) {
            teach(neuralNetwork, epochData, teachDataCount, learningFactor, momentumFactor);
            lastSSE = sse;
            sse = getCurrentSSE(neuralNetwork, epochData);
            learningFactor = calculateFactor(sse, lastSSE, learningFactor);
        }
    }

    private BackPropagationWeightStorage createStorage(final NeuralNetwork neuralNetwork) {
        BackPropagationWeightStorage.Builder builder = new BackPropagationWeightStorage.Builder();
        builder.createFrom(neuralNetwork);

        return builder.build();
    }

    private void teach(final NeuralNetwork neuralNetwork, final EpochData epochData, int teachDataCount, double learningAspect, double momentumFactor) {
        List<Integer> permutation = generatePermutation(PERMUTATION_START_INDEX, teachDataCount);

        for (int i = 0; i < teachDataCount; i++) {
            TeachData singleTeachData = epochData.getElement(permutation.get(i));
            propagate(singleTeachData, neuralNetwork, learningAspect, momentumFactor);
        }
    }

    private List<Integer> generatePermutation(int startIndex, int stopIndex) {
        return permutationGenerator.generatePermutation(startIndex, stopIndex);
    }

    private double getCurrentSSE(final NeuralNetwork neuralNetwork, final EpochData epochData) {
       return sseCalculator.calculateCurrentSSE(neuralNetwork, epochData);
    }

    private double calculateFactor(double sse, double lastSSE, double learningAspect) {
        return learningFactorCalculator.calculate(sse, lastSSE, learningAspect);
    }

    private void propagate(final TeachData singleTeachData, final NeuralNetwork neuralNetwork, final double learningAspect, double momentumFactor) {

        int layersCount = neuralNetwork.getLayersCount();

        double[] inputs = singleTeachData.getInput();
        neuralNetwork.setInputs(inputs);

        neuralNetwork.simulate();
        double[] currentOutputs = neuralNetwork.getOutput();

        double[][] error = calculateError(singleTeachData, neuralNetwork, currentOutputs);

        tempOldWeightStorage = readWags(neuralNetwork);
        modifyWags(neuralNetwork, learningAspect, momentumFactor, layersCount, error);
        oldWeightStorage = tempOldWeightStorage.clone();
    }

    private BackPropagationWeightStorage readWags(final NeuralNetwork neuralNetwork) {
        final BackPropagationWeightStorage storage = createStorage(neuralNetwork);
        storage.read(neuralNetwork);

        return storage;
    }

    private double[][] calculateError(TeachData singleTeachData, NeuralNetwork neuralNetwork, double[] currentOutputs) {
        int layersCount = neuralNetwork.getLayersCount();
        double[][] errors = getErrorMatrix(neuralNetwork);
        double error = calculateLastLayerError(singleTeachData, neuralNetwork, currentOutputs);

        errors[layersCount - 1][0] = error;

        return calculateLayersErrorWithoutLastLayer(neuralNetwork, errors);

    }

    private double[][] getErrorMatrix(final NeuralNetwork neuralNetwork) {
        int layersCount = neuralNetwork.getLayersCount();
        double[][] errors = new double[layersCount][];
        for (int i = 0; i < layersCount; i++) {
            Layer layer = neuralNetwork.getLayer(i);
            int neuronsInLayer = layer.getNeuronsCount();
            errors[i] = new double[neuronsInLayer];
        }
        return errors;
    }

    private double calculateLastLayerError(final TeachData singleTeachData, final NeuralNetwork neuralNetwork, double[] currentOutputs) {
        int layersCount = neuralNetwork.getLayersCount();
        Layer lastLayer = neuralNetwork.getLayer(layersCount - 1);
        double error = 0;
        double[] expectedOutputs = singleTeachData.getExpectedOutput();

        for (int i = 0; i < lastLayer.getNeuronsCount(); i++) {
            double expected = expectedOutputs[i];
            double currentOutput = currentOutputs[i];
            error += expected - currentOutput;
        }
        return error;
    }

    private double[][] calculateLayersErrorWithoutLastLayer(NeuralNetwork neuralNetwork, double[][] errors) {
        int layersCount = neuralNetwork.getLayersCount();
        for (int i = layersCount - 2; i > 0; i--) {
            Layer layer = neuralNetwork.getLayer(i);
            Layer nextLayer = neuralNetwork.getLayer(i + 1);

            for (int j = 0; j < layer.getNeuronsCount(); j++) {
                double localError = 0;
                for (int k = 0; k < nextLayer.getNeuronsCount(); k++) {
                    Neuron neuron = nextLayer.getNeuron(k);
                    double neuronWag = neuron.getWag(j);
                    localError += errors[i + 1][k] * neuronWag;
                }

                errors[i][j] = localError;
            }
        }

        return errors;
    }

    private void modifyWags(NeuralNetwork neuralNetwork, double learningAspect, double momentumFactor, int layersCount, double[][] errors) {
        for (int i = layersCount - 1; i > 0; i--) {
            Layer previous = neuralNetwork.getLayer(i - 1);
            Layer layer = neuralNetwork.getLayer(i);
            Double[][] oldWags = oldWeightStorage.get(i);

            modifyWags(learningAspect, momentumFactor, errors[i], previous, layer, oldWags);
        }
    }

    private void modifyWags(double learningAspect, double momentumFactor, double[] errors, final Layer previous, final Layer current, Double[][] oldWags) {
        for (int i = 0; i < current.getNeuronsCount(); i++) {
            for (int j = 0; j < previous.getNeuronsCount(); j++) {
                Neuron previousNeuron = previous.getNeuron(j);
                double previousNeuronValue = previousNeuron.getValue();
                Neuron currentNeuron = current.getNeuron(i);
                double currentNeuronDerivativeValue = currentNeuron.getInputDerivativeValue();
                double newWagCalculated = learningAspect * errors[i] * previousNeuronValue * currentNeuronDerivativeValue;
                double oldWag = currentNeuron.getWag(j);
                double preOldWag = oldWags[i][j];
                double newWagWithMomentum = oldWag + newWagCalculated + (momentumFactor * (oldWag - preOldWag));

                current.getNeuron(i).setWag(j, newWagWithMomentum);
            }
        }
    }
}

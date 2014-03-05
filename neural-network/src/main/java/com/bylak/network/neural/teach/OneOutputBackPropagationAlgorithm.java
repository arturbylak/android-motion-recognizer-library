package com.bylak.network.neural.teach;

import java.util.List;

import com.bylak.network.layer.Layer;
import com.bylak.network.neural.NeuralNetwork;
import com.bylak.network.neural.Neuron;
import com.bylak.network.util.PermutationGenerator;
import com.bylak.network.util.SSECalculator;

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
    public void teach(final NeuralNetwork neuralNetwork, final EpochData epochData,
            final TeachConfiguration teachConfiguration) {
        final int teachDataCount = epochData.getSize();
        final int epochCount = teachConfiguration.getEpochCount();
        final double learningFactor = teachConfiguration.getLearningFactor();
        final double maxErrorValue = teachConfiguration.getMaxErrorValue();
        final double momentumFactor = teachConfiguration.getMomentumFactor();

        oldWeightStorage = createStorage(neuralNetwork);
        tempOldWeightStorage = createStorage(neuralNetwork);

        teach(neuralNetwork, epochData, teachDataCount, epochCount, learningFactor, maxErrorValue, momentumFactor);
    }

    private void teach(final NeuralNetwork neuralNetwork, final EpochData epochData, final int teachDataCount,
            final int epochCount,
            double learningFactor, final double maxErrorValue, final double momentumFactor) {
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
        final BackPropagationWeightStorage.Builder builder = new BackPropagationWeightStorage.Builder();
        builder.createFrom(neuralNetwork);

        return builder.build();
    }

    private void teach(final NeuralNetwork neuralNetwork, final EpochData epochData, final int teachDataCount,
            final double learningAspect, final double momentumFactor) {
        final List<Integer> permutation = generatePermutation(PERMUTATION_START_INDEX, teachDataCount);

        for (int i = 0; i < teachDataCount; i++) {
            final TeachData singleTeachData = epochData.getElement(permutation.get(i));
            propagate(singleTeachData, neuralNetwork, learningAspect, momentumFactor);
        }
    }

    private List<Integer> generatePermutation(final int startIndex, final int stopIndex) {
        return permutationGenerator.generatePermutation(startIndex, stopIndex);
    }

    private double getCurrentSSE(final NeuralNetwork neuralNetwork, final EpochData epochData) {
        return sseCalculator.calculateCurrentSSE(neuralNetwork, epochData);
    }

    private double calculateFactor(final double sse, final double lastSSE, final double learningAspect) {
        return learningFactorCalculator.calculate(sse, lastSSE, learningAspect);
    }

    private void propagate(final TeachData singleTeachData, final NeuralNetwork neuralNetwork,
            final double learningAspect, final double momentumFactor) {

        final int layersCount = neuralNetwork.getLayersCount();

        final double[] inputs = singleTeachData.getInput();
        neuralNetwork.setInputs(inputs);

        neuralNetwork.simulate();
        final double[] currentOutputs = neuralNetwork.getOutput();

        final double[][] error = calculateError(singleTeachData, neuralNetwork, currentOutputs);

        tempOldWeightStorage = readWags(neuralNetwork);
        modifyWags(neuralNetwork, learningAspect, momentumFactor, layersCount, error);
        oldWeightStorage = tempOldWeightStorage.clone();
    }

    private BackPropagationWeightStorage readWags(final NeuralNetwork neuralNetwork) {
        final BackPropagationWeightStorage storage = createStorage(neuralNetwork);
        storage.read(neuralNetwork);

        return storage;
    }

    private double[][] calculateError(final TeachData singleTeachData, final NeuralNetwork neuralNetwork,
            final double[] currentOutputs) {
        final int layersCount = neuralNetwork.getLayersCount();
        final double[][] errors = getErrorMatrix(neuralNetwork);
        final double error = calculateLastLayerError(singleTeachData, neuralNetwork, currentOutputs);

        errors[layersCount - 1][0] = error;

        return calculateLayersErrorWithoutLastLayer(neuralNetwork, errors);

    }

    private double[][] getErrorMatrix(final NeuralNetwork neuralNetwork) {
        final int layersCount = neuralNetwork.getLayersCount();
        final double[][] errors = new double[layersCount][];
        for (int i = 0; i < layersCount; i++) {
            final Layer layer = neuralNetwork.getLayer(i);
            final int neuronsInLayer = layer.getNeuronsCount();
            errors[i] = new double[neuronsInLayer];
        }
        return errors;
    }

    private double calculateLastLayerError(final TeachData singleTeachData, final NeuralNetwork neuralNetwork,
            final double[] currentOutputs) {
        final int layersCount = neuralNetwork.getLayersCount();
        final Layer lastLayer = neuralNetwork.getLayer(layersCount - 1);
        double error = 0;
        final double[] expectedOutputs = singleTeachData.getExpectedOutput();

        for (int i = 0; i < lastLayer.getNeuronsCount(); i++) {
            final double expected = expectedOutputs[i];
            final double currentOutput = currentOutputs[i];
            error += expected - currentOutput;
        }
        return error;
    }

    private double[][] calculateLayersErrorWithoutLastLayer(final NeuralNetwork neuralNetwork, final double[][] errors) {
        final int layersCount = neuralNetwork.getLayersCount();
        for (int i = layersCount - 2; i > 0; i--) {
            final Layer layer = neuralNetwork.getLayer(i);
            final Layer nextLayer = neuralNetwork.getLayer(i + 1);

            for (int j = 0; j < layer.getNeuronsCount(); j++) {
                double localError = 0;
                for (int k = 0; k < nextLayer.getNeuronsCount(); k++) {
                    final Neuron neuron = nextLayer.getNeuron(k);
                    final double neuronWag = neuron.getWag(j);
                    localError += errors[i + 1][k] * neuronWag;
                }

                errors[i][j] = localError;
            }
        }

        return errors;
    }

    private void modifyWags(final NeuralNetwork neuralNetwork, final double learningAspect,
            final double momentumFactor, final int layersCount,
            final double[][] errors) {
        for (int i = layersCount - 1; i > 0; i--) {
            final Layer previous = neuralNetwork.getLayer(i - 1);
            final Layer layer = neuralNetwork.getLayer(i);
            final Double[][] oldWags = oldWeightStorage.get(i);

            modifyWags(learningAspect, momentumFactor, errors[i], previous, layer, oldWags);
        }
    }

    private void modifyWags(final double learningAspect, final double momentumFactor, final double[] errors,
            final Layer previous,
            final Layer current, final Double[][] oldWags) {
        for (int i = 0; i < current.getNeuronsCount(); i++) {
            for (int j = 0; j < previous.getNeuronsCount(); j++) {
                final Neuron previousNeuron = previous.getNeuron(j);
                final double previousNeuronValue = previousNeuron.getValue();
                final Neuron currentNeuron = current.getNeuron(i);
                final double currentNeuronDerivativeValue = currentNeuron.getInputDerivativeValue();
                final double newWagCalculated = learningAspect * errors[i] * previousNeuronValue
                        * currentNeuronDerivativeValue;
                final double oldWag = currentNeuron.getWag(j);
                final double preOldWag = oldWags[i][j];
                final double newWagWithMomentum = oldWag + newWagCalculated + momentumFactor * (oldWag - preOldWag);

                current.getNeuron(i).setWag(j, newWagWithMomentum);
            }
        }
    }
}

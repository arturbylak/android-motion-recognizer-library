package com.bylak.network.neural.teach;

import com.bylak.network.layer.Layer;
import com.bylak.network.neural.NeuralNetwork;
import com.bylak.network.neural.Neuron;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 11.09.13
 * Time: 15:35
 * To change this template use File | Settings | File Templates.
 */
public final class BackPropagationAlgorithm implements NeutralNetworkTeachingAlgorithm {

    @Override
    public void teach(final NeuralNetwork neuralNetwork, final EpochData epochData, final TeachConfiguration teachConfiguration) {
        int teachDataCount = epochData.getSize();
        int epochCount = teachConfiguration.getEpochCount();
        double learningAspect = teachConfiguration.getLearningAspect();
        double maxErrorValue = teachConfiguration.getMaxErrorValue();
        double sse = Double.MAX_VALUE;

        for (int i = 0; i < epochCount && sse > maxErrorValue; i++) {
            teach(neuralNetwork, epochData, teachDataCount, learningAspect);
            sse = getCurrentSSE(neuralNetwork, epochData);
        }
    }

    private void teach(final NeuralNetwork neuralNetwork, final EpochData epochData, int teachDataCount, double learningAspect) {
        List<Integer> permutation = generatePermutation(0, teachDataCount);

        for (int i = 0; i < teachDataCount; i++) {
            propagate(neuralNetwork, epochData, learningAspect, permutation, i);
        }
    }

    private List<Integer> generatePermutation(int startIndex, int stopIndex) {
        List<Integer> permutation = new ArrayList<Integer>();

        for (int i = startIndex; i < stopIndex; i++) {
            permutation.add(i);
        }

        java.util.Collections.shuffle(permutation);

        return permutation;
    }

    private double getCurrentSSE(final NeuralNetwork neuralNetwork, final EpochData epochData) {
        double sse = 0;
        for (int i = 0; i < epochData.getSize(); i++) {
            TeachData teachData = epochData.getElement(i);
            neuralNetwork.setInputs(teachData.getInput());
            neuralNetwork.simulate();
            double[] simulationResult = neuralNetwork.getOutput();
            double[] expectedOutput = teachData.getExpectedOutput();

            double error = getOutputError(simulationResult, expectedOutput);
            sse += Math.pow(error, 2);
        }

        return Math.sqrt(sse);
    }

    private double getOutputError(double[] results, double[] expectedDatas) {
        double errorSum = 0;

        for(int i=0; i<results.length; i++){
            double diff = results[i] - expectedDatas[i];
            errorSum += diff;
        }

        return errorSum;
    }

    private void propagate(NeuralNetwork neuralNetwork, EpochData epochData, double learningAspect, List<Integer> permutation, int i) {
        Integer dataIndexToProcess = permutation.get(i);
        TeachData singleTeachData = epochData.getElement(dataIndexToProcess);

        propagate(singleTeachData, neuralNetwork, learningAspect);
    }

    private void propagate(final TeachData singleTeachData, final NeuralNetwork neuralNetwork, final double learningAspect) {

        int layersCount = neuralNetwork.getLayersCount();
        double[][] errors = prepareErrorTable(neuralNetwork);

        double[] inputs = singleTeachData.getInput();
        neuralNetwork.setInputs(inputs);

        neuralNetwork.simulate();
        double[] currentOutputs = neuralNetwork.getOutput();

        errors = calculateLastLayerError(singleTeachData, neuralNetwork, layersCount, errors, currentOutputs);
        errors = calculateOtherLayerError(neuralNetwork, layersCount, errors);
        modifyWags(neuralNetwork, learningAspect, layersCount, errors);
    }

    private double[][] prepareErrorTable(final NeuralNetwork neuralNetwork) {
        int layersCount = neuralNetwork.getLayersCount();
        double[][] errors = new double[layersCount][];

        for (int i = 0; i < layersCount; i++) {
            Layer layer = neuralNetwork.getLayer(i);
            errors[i] = new double[layer.getNeuronsCount()];
        }

        return errors;
    }

    private double[][] calculateLastLayerError(TeachData singleTeachData, NeuralNetwork neuralNetwork, int layersCount, double[][] errors, double[] currentOutputs) {
        Layer lastLayer = neuralNetwork.getLayer(layersCount - 1);
        double[] expectedOutputs = singleTeachData.getExpectedOutput();
        for (int i = 0; i < lastLayer.getNeuronsCount(); i++) {
            double expected = expectedOutputs[i];
            double currentOutput = currentOutputs[i];
            Neuron neuronToPropagate = lastLayer.getNeuron(i);
            double error = neuronToPropagate.getInputDerivativeValue() * (expected - currentOutput);
            errors[layersCount - 1][i] = error;
        }

        return errors;
    }

    private double[][] calculateOtherLayerError(NeuralNetwork neuralNetwork, int layersCount, double[][] errors) {

        for (int i = layersCount - 1; i > 0; i--) {
            Layer previousLayer = neuralNetwork.getLayer(i - 1);
            Layer currentLayer = neuralNetwork.getLayer(i);

            RealMatrix layerErrors = MatrixUtils.createColumnRealMatrix(errors[i]).transpose();

            int currentLayerNeuronsCount = currentLayer.getNeuronsCount();
            for (int j = 0; j < previousLayer.getNeuronsCount(); j++) {
                double[] wags = getWagsPerInputNumber(currentLayer, currentLayerNeuronsCount, j);

                RealMatrix wagsMatrix = MatrixUtils.createColumnRealMatrix(wags);
                RealMatrix multiplied = layerErrors.multiply(wagsMatrix);
                Neuron neuron = previousLayer.getNeuron(j);
                errors[i - 1][j] = neuron.getInputDerivativeValue() * multiplied.getEntry(0, 0);
            }
        }

        return errors;
    }

    private double[] getWagsPerInputNumber(final Layer layer, int neuronsCount, int inputNumber) {
        double[] wags = new double[neuronsCount];
        for (int i = 0; i < neuronsCount; i++) {
            wags[i] = layer.getNeuron(i).getWag(inputNumber);
        }
        return wags;
    }

    private void modifyWags(NeuralNetwork neuralNetwork, double learningAspect, int layersCount, double[][] errors) {
        for (int i = layersCount - 1; i > 0; i--) {
            Layer previous = neuralNetwork.getLayer(i - 1);
            Layer layer = neuralNetwork.getLayer(i);

            modifyWags(learningAspect, errors[i], previous, layer);
        }
    }

    private void modifyWags(double learningAspect, double[] error, final Layer previous, final Layer current) {
        for (int i = 0; i < current.getNeuronsCount(); i++) {
            for (int j = 0; j < previous.getNeuronsCount(); j++) {
                double oldWag = current.getNeuron(i).getWag(j);
                double newWag = oldWag + (learningAspect * error[i] * previous.getNeuron(j).getValue());
                current.getNeuron(i).setWag(j, newWag);
            }
        }
    }
}

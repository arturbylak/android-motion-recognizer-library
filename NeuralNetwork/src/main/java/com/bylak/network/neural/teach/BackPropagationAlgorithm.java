package com.bylak.network.neural.teach;

import com.bylak.network.layer.Layer;
import com.bylak.network.neural.NeuralNetwork;
import com.bylak.network.neural.Neuron;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;

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

        for (int i = 0; i < epochCount; i++) {
            for (int j = 0; j < teachDataCount; j++) {
                TeachData singleTeachData = epochData.getElement(0);

                propagate(singleTeachData, neuralNetwork, learningAspect);
            }
        }
    }

    //TODO refactoring
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

    private double[][] calculateOtherLayerError(NeuralNetwork neuralNetwork, int layersCount, double[][] errors) {
        //Other layers
        for (int i = layersCount - 1; i > 0; i--) {
            Layer previousLayer = neuralNetwork.getLayer(i - 1);
            Layer currentLayer = neuralNetwork.getLayer(i);

            RealMatrix layerErrors = MatrixUtils.createColumnRealMatrix(errors[i]).transpose();

            for (int k = 0; k < previousLayer.getNeuronsCount(); k++) {
                double[] wags = new double[currentLayer.getNeuronsCount()];
                for (int j = 0; j < currentLayer.getNeuronsCount(); j++) {
                    wags[j] = currentLayer.getNeuron(j).getWag(k);
                }

                RealMatrix wagsMatrix = MatrixUtils.createColumnRealMatrix(wags);
                RealMatrix multiplied = layerErrors.multiply(wagsMatrix);
                Neuron neuron = previousLayer.getNeuron(k);
                errors[i - 1][k] = neuron.getInputDerivativeValue() * multiplied.getEntry(0, 0);
            }
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

    private void modifyWags(NeuralNetwork neuralNetwork, double learningAspect, int layersCount, double[][] errors) {
        for (int i = layersCount - 1; i > 0; i--) {
            Layer previous = neuralNetwork.getLayer(i - 1);
            Layer layer = neuralNetwork.getLayer(i);

            for (int j = 0; j < layer.getNeuronsCount(); j++) {
                for (int k = 0; k < previous.getNeuronsCount(); k++) {
                    double oldWag = layer.getNeuron(j).getWag(k);
                    double newWag = oldWag + (learningAspect * errors[i][j] * previous.getNeuron(k).getValue());
                    layer.getNeuron(j).setWag(k, newWag);
                }
            }
        }
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
}

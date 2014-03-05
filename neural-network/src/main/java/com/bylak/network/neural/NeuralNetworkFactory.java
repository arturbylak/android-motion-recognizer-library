package com.bylak.network.neural;

import com.bylak.network.function.ActivationFunction;
import com.bylak.network.layer.Layer;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 04.10.13
 * Time: 16:23
 * To change this template use File | Settings | File Templates.
 */
public final class NeuralNetworkFactory {

    private NeuralNetworkFactory() {
    }

    public static NeuralNetwork createNetwork(final int inputCount, final int inputLayerCount,
            final int hiddenLayerCount,
            final int outputLayerCount, final ActivationFunction activationFunction) {
        final NeuralNetwork neuralNetwork = new NeuralNetwork();

        final Layer inputLayer = createLayer(inputCount, inputLayerCount, activationFunction, true);
        final Layer hiddenLayer = createLayer(inputLayerCount, hiddenLayerCount, activationFunction, true);
        final Layer outputLayer = createLayer(hiddenLayerCount, outputLayerCount, activationFunction, false);

        neuralNetwork.addLayer(inputLayer);
        neuralNetwork.addLayer(hiddenLayer);
        neuralNetwork.addLayer(outputLayer);

        return neuralNetwork;
    }

    private static Layer createLayer(final int inputCount, final int inputLayerCount,
            final ActivationFunction activationFunction,
            final boolean bias) {
        final Layer.Builder builder = new Layer.Builder();
        for (int i = 0; i < inputLayerCount; i++) {
            builder.addNeuron(NeuronImpl.createNeuron(inputCount, activationFunction));
        }

        if (bias) {
            builder.addNeuron(Bias.createBias(activationFunction));
        }

        return builder.build();
    }
}

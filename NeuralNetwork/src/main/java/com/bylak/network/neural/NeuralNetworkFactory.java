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

    private  NeuralNetworkFactory() {
    }

    public static final NeuralNetwork createNetwork(int inputCount, int inputLayerCount, int hiddenLayerCount, int outputLayerCount, final ActivationFunction activationFunction) {
        NeuralNetwork neuralNetwork = new NeuralNetwork();

        Layer inputLayer = createLayer(inputCount, inputLayerCount, activationFunction);
        Layer hiddenLayer = createLayer(inputLayerCount, hiddenLayerCount, activationFunction);
        Layer outputLayer = createLayer(hiddenLayerCount, outputLayerCount, activationFunction);

        neuralNetwork.addLayer(inputLayer);
        neuralNetwork.addLayer(hiddenLayer);
        neuralNetwork.addLayer(outputLayer);

        return neuralNetwork;
    }

    private static Layer createLayer(int inputCount, int inputLayerCount, ActivationFunction activationFunction) {
        Layer.Builder builder = new Layer.Builder();
        for (int i = 0; i < inputLayerCount; i++) {
            builder.addNeuron(Neuron.createNeuron(inputCount, activationFunction));
        }
        return builder.build();
    }
}

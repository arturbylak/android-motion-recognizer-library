package com.bylak.network.neural.teach;

import org.junit.Assert;
import org.junit.Test;

import com.bylak.network.layer.Layer;
import com.bylak.network.neural.Bias;
import com.bylak.network.neural.NeuralNetwork;
import com.bylak.network.neural.Neuron;
import com.bylak.network.neural.NeuronImpl;
import com.bylak.network.neural.TestActivationFunction;

/**
 * Created with IntelliJ IDEA.
 * User: bylak
 * Date: 19.09.13
 * Time: 21:13
 * To change this template use File | Settings | File Templates.
 */
public class BackPropagationAlgorithmTest {
    @Test
    public void testTeach() throws Exception {
        // given
        final NeuralNetwork neuralNetwork = new NeuralNetwork();
        final TestActivationFunction activationFunction = new TestActivationFunction();
        final double input[] = { 1d, 1d };

        final Layer inputLayer = new Layer.Builder()
                .addNeuron(new NeuronImpl(new double[] { 1 }, 1, activationFunction))
                .addNeuron(new NeuronImpl(new double[] { 2 }, 1, activationFunction))
                .addNeuron(Bias.createBias(activationFunction))
                .build();
        final Layer outputLayer = new Layer.Builder()
                .addNeuron(new NeuronImpl(new double[] { 3, 1, 1 }, 1, activationFunction))
                .build();

        neuralNetwork.addLayer(inputLayer);
        neuralNetwork.addLayer(outputLayer);

        final EpochData epochData = new EpochData.Builder()
                .add(new TeachData(new double[] { 1, 2 }, new double[] { 3 }))
                .build();

        // when
        neuralNetwork.teach(epochData, new TeachConfiguration(0.0001, 1, 1, 0));
        neuralNetwork.setInputs(input);
        neuralNetwork.simulate();

        final double expectedOutput = -7.0d;
        final double actualOutput = neuralNetwork.getOutput()[0];
        final double[] expectedWag = { 0.0d, -5.0d, -2.0d };
        final Neuron outputNeuron = outputLayer.getNeuron(0);

        // then
        Assert.assertEquals(expectedOutput, actualOutput, 0.01d);

        for (int i = 0; i < expectedWag.length; i++) {
            Assert.assertEquals(expectedWag[i], outputNeuron.getWag(i), 0.01d);
        }

    }
}

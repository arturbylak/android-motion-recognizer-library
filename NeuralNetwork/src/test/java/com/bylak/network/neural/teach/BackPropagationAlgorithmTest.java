package com.bylak.network.neural.teach;

import com.bylak.network.layer.Layer;
import com.bylak.network.neural.*;
import org.junit.Assert;
import org.junit.Test;

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
        //given
        NeuralNetwork neuralNetwork = new NeuralNetwork();
        final TestActivationFunction activationFunction = new TestActivationFunction();
        double input[] = {1d, 1d};

        Layer inputLayer = new Layer.Builder()
                .addNeuron(new NeuronImpl(new double[]{1}, 1, activationFunction))
                .addNeuron(new NeuronImpl(new double[]{2}, 1, activationFunction))
                .addNeuron(Bias.createBias(activationFunction))
                .build();
        Layer outputLayer = new Layer.Builder()
                .addNeuron(new NeuronImpl(new double[]{3, 1, 1}, 1, activationFunction))
                .build();

        neuralNetwork.addLayer(inputLayer);
        neuralNetwork.addLayer(outputLayer);

        EpochData epochData = new EpochData.Builder()
                .add(new TeachData(new double[]{1, 2}, new double[]{3}))
                .build();

        //when
        neuralNetwork.teach(epochData, new TeachConfiguration(0.0001, 1, 1,  0));
        neuralNetwork.setInputs(input);
        neuralNetwork.simulate();

        double expectedOutput = -7.0d;
        double actualOutput = neuralNetwork.getOutput()[0];
        double[] expectedWag = {0.0d, -5.0d, -2.0d};
        Neuron outputNeuron = outputLayer.getNeuron(0);

        //then
        Assert.assertEquals(expectedOutput, actualOutput, 0.01d);

        for (int i = 0; i < expectedWag.length; i++) {
            Assert.assertEquals(expectedWag[i], outputNeuron.getWag(i), 0.01d);
        }

    }
}

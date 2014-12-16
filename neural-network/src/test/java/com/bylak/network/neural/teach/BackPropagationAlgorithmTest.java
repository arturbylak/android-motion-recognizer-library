package com.bylak.network.neural.teach;

import com.bylak.network.layer.Layer;
import com.bylak.network.neural.Bias;
import com.bylak.network.neural.NeuralNetwork;
import com.bylak.network.neural.Neuron;
import com.bylak.network.neural.NeuronImpl;
import com.bylak.network.neural.TestActivationFunction;
import org.hamcrest.number.IsCloseTo;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Created with IntelliJ IDEA.
 * User: bylak
 * Date: 19.09.13
 * Time: 21:13
 * To change this template use File | Settings | File Templates.
 */
public class BackPropagationAlgorithmTest {

    private final EpochData epochData = new EpochData.Builder()
            .add(new TeachData(new double[] { 1, 2 }, new double[] { 3 }))
            .build();

    private final TestActivationFunction activationFunction = new TestActivationFunction();

    private final Layer inputLayer = new Layer.Builder()
            .addNeuron(new NeuronImpl(new double[] { 1 }, 1, activationFunction))
            .addNeuron(new NeuronImpl(new double[] { 2 }, 1, activationFunction))
            .addNeuron(Bias.createBias(activationFunction))
            .build();

    private final Layer outputLayer = new Layer.Builder()
            .addNeuron(new NeuronImpl(new double[] { 3, 1, 1 }, 1, activationFunction))
            .build();

    private final double input[] = { 1d, 1d };

    @Test
    public void shouldTeachNeuralNetwork() throws Exception {
        // given
        final NeuralNetwork neuralNetwork = buildNeuralNetwork();

        final double expectedOutput = -7.0d;
        final double[] expectedWag = { 0.0d, -5.0d, -2.0d };
        final Neuron outputNeuron = outputLayer.getNeuron(0);

        // when
        final double actualOutput = simulate(neuralNetwork, input, epochData);


        // then
        assertThat(expectedOutput, IsCloseTo.closeTo(actualOutput, 0.01d));

        for (int i = 0; i < expectedWag.length; i++) {
            assertThat(expectedWag[i], IsCloseTo.closeTo(outputNeuron.getWag(i), 0.01d));
        }
    }

    private NeuralNetwork buildNeuralNetwork() {
        final NeuralNetwork neuralNetwork = new NeuralNetwork();
        neuralNetwork.addLayer(inputLayer);
        neuralNetwork.addLayer(outputLayer);
        return neuralNetwork;
    }

    private double simulate(final NeuralNetwork neuralNetwork, final double[] input, final
        EpochData epochData) {
        neuralNetwork.teach(epochData, new TeachConfiguration(0.0001, 1, 1, 0));
        neuralNetwork.setInputs(input);
        neuralNetwork.simulate();

        return neuralNetwork.getOutput()[0];
    }
}
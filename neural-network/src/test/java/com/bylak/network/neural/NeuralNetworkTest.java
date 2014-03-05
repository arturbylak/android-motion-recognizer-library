package com.bylak.network.neural;

import org.junit.Assert;
import org.junit.Test;

import com.bylak.network.function.ActivationFunction;
import com.bylak.network.function.SigmoidActivationFunction;
import com.bylak.network.layer.Layer;
import com.bylak.network.neural.teach.EpochData;
import com.bylak.network.neural.teach.TeachConfiguration;
import com.bylak.network.neural.teach.TeachData;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 23.08.13
 * Time: 20:52
 * To change this template use File | Settings | File Templates.
 */
public class NeuralNetworkTest {

    private static final EpochData XOR_EPOCH_DATA;
    private static final EpochData OR_EPOCH_DATA;
    private static final EpochData zero;
    private static final EpochData zero2;
    private static final EpochData zero3;

    static {
        XOR_EPOCH_DATA = new EpochData.Builder()
                .add(new TeachData(new double[] { 0, 0 }, new double[] { 0 }))
                .add(new TeachData(new double[] { 0, 1 }, new double[] { 1 }))
                .add(new TeachData(new double[] { 1, 0 }, new double[] { 1 }))
                .add(new TeachData(new double[] { 1, 1 }, new double[] { 0 }))
                .build();

        OR_EPOCH_DATA = new EpochData.Builder()
                .add(new TeachData(new double[] { 0, 0 }, new double[] { 0 }))
                .add(new TeachData(new double[] { 0, 1 }, new double[] { 1 }))
                .add(new TeachData(new double[] { 1, 0 }, new double[] { 1 }))
                .add(new TeachData(new double[] { 1, 1 }, new double[] { 1 }))
                .build();

        zero = new EpochData.Builder()
                .add(new TeachData(new double[] { 0 }, new double[] { 0 }))
                .build();

        zero2 = new EpochData.Builder()
                .add(new TeachData(new double[] { 1, 1, 1, 1 }, new double[] { 1 }))
                .build();
        zero3 = new EpochData.Builder()
                .add(new TeachData(new double[] { 1, 0 }, new double[] { 0 }))

                .build();
    }

    @Test
    public void testSimulate() throws Exception {
        // given
        final NeuralNetwork neuralNetwork = new NeuralNetwork();
        final TestActivationFunction activationFunction = new TestActivationFunction();

        final Layer inputLayer = new Layer.Builder()
                .addNeuron(new NeuronImpl(new double[] { 1 }, 1, activationFunction))
                .addNeuron(new NeuronImpl(new double[] { 2 }, 1, activationFunction))
                .build();

        final Layer hiddenLayer = new Layer.Builder()
                .addNeuron(new NeuronImpl(new double[] { 1, 2 }, 1, activationFunction))
                .addNeuron(new NeuronImpl(new double[] { 3, 4 }, 1, activationFunction))
                .build();

        final Layer outputLayer = new Layer.Builder()
                .addNeuron(new NeuronImpl(new double[] { 3, 1 }, 1, activationFunction))
                .build();

        neuralNetwork.addLayer(inputLayer);
        neuralNetwork.addLayer(hiddenLayer);
        neuralNetwork.addLayer(outputLayer);

        final int expectedLength = 1;
        final int expectedValue = 16;

        // when
        neuralNetwork.simulate();
        final double[] simulateResult = neuralNetwork.getOutput();

        // then
        Assert.assertTrue(simulateResult.length == expectedLength);
        Assert.assertTrue(simulateResult[0] == expectedValue);
    }

    @Test
    public void testXor() {
        // given
        final NeuralNetwork neuralNetwork = getXORNeuralNetwork();

        final TeachConfiguration configuration = new TeachConfiguration(0.01d, 40000000, 1d, 0.7d);

        // when
        neuralNetwork.teach(XOR_EPOCH_DATA, configuration);

        // then
        double output = simulate(neuralNetwork, new double[] { 0, 0 });
        Assert.assertEquals(0, output, 0.1d);

        output = simulate(neuralNetwork, new double[] { 0, 1 });
        Assert.assertEquals(1, output, 0.1d);

        output = simulate(neuralNetwork, new double[] { 1, 0 });
        Assert.assertEquals(1, output, 0.1d);

        output = simulate(neuralNetwork, new double[] { 1, 1 });
        Assert.assertEquals(0, output, 0.1d);
    }

    @Test
    public void testOR() {
        // given
        final NeuralNetwork neuralNetwork = getXORNeuralNetwork();

        final TeachConfiguration configuration = new TeachConfiguration(0.01d, 40000000, 1d, 0.7d);

        // when
        neuralNetwork.teach(OR_EPOCH_DATA, configuration);

        // then
        double output = simulate(neuralNetwork, new double[] { 0, 0 });
        Assert.assertEquals(0, output, 0.1d);

        output = simulate(neuralNetwork, new double[] { 0, 1 });
        Assert.assertEquals(1, output, 0.1d);

        output = simulate(neuralNetwork, new double[] { 1, 0 });
        Assert.assertEquals(1, output, 0.1d);

        output = simulate(neuralNetwork, new double[] { 1, 1 });
        Assert.assertEquals(1, output, 0.1d);
    }

    @Test
    public void testSSE() {
        // given
        final NeuralNetwork neuralNetwork = getXORNeuralNetwork();
        final double maxErrorValue = 0.2d;
        final TeachConfiguration configuration = new TeachConfiguration(maxErrorValue, 4000000, 1, 0.7d);
        double errorSum = 0;

        // when
        neuralNetwork.teach(XOR_EPOCH_DATA, configuration);
        double output = simulate(neuralNetwork, new double[] { 0, 0 });
        errorSum = +output - 0;

        output = simulate(neuralNetwork, new double[] { 0, 1 });
        errorSum = +output - 1;

        output = simulate(neuralNetwork, new double[] { 1, 0 });
        errorSum = +output - 1;

        output = simulate(neuralNetwork, new double[] { 1, 1 });
        errorSum = +output - 0;

        // then
        Assert.assertTrue(errorSum < maxErrorValue);
    }

    private NeuralNetwork getXORNeuralNetwork() {

        final NeuralNetwork neuralNetwork = new NeuralNetwork();
        final ActivationFunction activationFunction = new SigmoidActivationFunction();

        final Layer inputLayer = new Layer.Builder()
                .addNeuron(NeuronImpl.createNeuron(1, activationFunction))
                .addNeuron(NeuronImpl.createNeuron(1, activationFunction))
                .addNeuron(Bias.createBias(activationFunction))
                .build();

        final Layer hiddenLayer = new Layer.Builder()
                .addNeuron(NeuronImpl.createNeuron(3, activationFunction))
                .addNeuron(NeuronImpl.createNeuron(3, activationFunction))
                .addNeuron(NeuronImpl.createNeuron(3, activationFunction))
                .addNeuron(NeuronImpl.createNeuron(3, activationFunction))
                .addNeuron(Bias.createBias(activationFunction))
                .build();

        final Layer outputLayer = new Layer.Builder()
                .addNeuron(NeuronImpl.createNeuron(5, activationFunction))
                .build();

        neuralNetwork.addLayer(inputLayer);
        neuralNetwork.addLayer(hiddenLayer);
        neuralNetwork.addLayer(outputLayer);

        return neuralNetwork;
    }

    private double simulate(final NeuralNetwork neuralNetwork, final double[] inputs) {
        neuralNetwork.setInputs(inputs);
        neuralNetwork.simulate();
        final double[] output = neuralNetwork.getOutput();

        return output[0];
    }

}

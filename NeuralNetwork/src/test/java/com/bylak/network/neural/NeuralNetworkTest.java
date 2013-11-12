package com.bylak.network.neural;

import com.bylak.network.function.ActivationFunction;
import com.bylak.network.function.SigmoidActivationFunction;
import com.bylak.network.layer.Layer;
import com.bylak.network.neural.teach.EpochData;
import com.bylak.network.neural.teach.TeachConfiguration;
import com.bylak.network.neural.teach.TeachData;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 23.08.13
 * Time: 20:52
 * To change this template use File | Settings | File Templates.
 */
public class NeuralNetworkTest {

    private static final EpochData xorEpochData;
    private static final EpochData orEpochData;
    private static final EpochData zero;
    private static final EpochData zero2;
    private static final EpochData zero3;

    static {
        xorEpochData = new EpochData.Builder()
                .add(new TeachData(new double[]{0, 0}, new double[]{0}))
                .add(new TeachData(new double[]{0, 1}, new double[]{1}))
                .add(new TeachData(new double[]{1, 0}, new double[]{1}))
                .add(new TeachData(new double[]{1, 1}, new double[]{0}))
                .build();

        orEpochData = new EpochData.Builder()
                .add(new TeachData(new double[]{0, 0}, new double[]{0}))
                .add(new TeachData(new double[]{0, 1}, new double[]{1}))
                .add(new TeachData(new double[]{1, 0}, new double[]{1}))
                .add(new TeachData(new double[]{1, 1}, new double[]{1}))
                .build();

        zero = new EpochData.Builder()
                .add(new TeachData(new double[]{0}, new double[]{0}))
                .build();

        zero2 = new EpochData.Builder()
                .add(new TeachData(new double[]{1, 1, 1, 1}, new double[]{1}))
                .build();
        zero3 = new EpochData.Builder()
                .add(new TeachData(new double[]{1, 0}, new double[]{0}))

                .build();
    }

    @Test
    public void testSimulate() throws Exception {
        //given
        NeuralNetwork neuralNetwork = new NeuralNetwork();
        final TestActivationFunction activationFunction = new TestActivationFunction();

        Layer inputLayer = new Layer.Builder()
                .addNeuron(new NeuronImpl(new double[]{1}, 1, activationFunction))
                .addNeuron(new NeuronImpl(new double[]{2}, 1, activationFunction))
                .build();

        Layer hiddenLayer = new Layer.Builder()
                .addNeuron(new NeuronImpl(new double[]{1, 2}, 1, activationFunction))
                .addNeuron(new NeuronImpl(new double[]{3, 4}, 1, activationFunction))
                .build();

        Layer outputLayer = new Layer.Builder()
                .addNeuron(new NeuronImpl(new double[]{3, 1}, 1, activationFunction))
                .build();

        neuralNetwork.addLayer(inputLayer);
        neuralNetwork.addLayer(hiddenLayer);
        neuralNetwork.addLayer(outputLayer);

        int expectedLength = 1;
        int expectedValue = 16;

        //when
        neuralNetwork.simulate();
        double[] simulateResult = neuralNetwork.getOutput();

        //then
        Assert.assertTrue(simulateResult.length == expectedLength);
        Assert.assertTrue(simulateResult[0] == expectedValue);
    }

    //Run manually - long
   // @Test
    public void testXor() {
        //given
        NeuralNetwork neuralNetwork = getXORNeuralNetwork();

        TeachConfiguration configuration = new TeachConfiguration(0.01d, 40000000, 1d);

        //when
        neuralNetwork.teach(xorEpochData, configuration);

        //then
        double output = simulate(neuralNetwork, new double[]{0, 0});
        Assert.assertEquals(0, output, 0.1d);

        output = simulate(neuralNetwork, new double[]{0, 1});
        Assert.assertEquals(1,output,  0.1d);

        output = simulate(neuralNetwork, new double[]{1, 0});
        Assert.assertEquals(1, output,  0.1d);

        output = simulate(neuralNetwork, new double[]{1, 1});
        Assert.assertEquals(0, output, 0.1d);
    }

    //Run manually - long
    //@Test
    public void testSSE() {
        //given
        NeuralNetwork neuralNetwork = getXORNeuralNetwork();
        double maxErrorValue = 0.2d;
        TeachConfiguration configuration = new TeachConfiguration(maxErrorValue, 4000000, 1);
        double errorSum = 0;

        //when
        neuralNetwork.teach(xorEpochData, configuration);
        double output = simulate(neuralNetwork, new double[]{0, 0});
        errorSum =+ output - 0;

        output = simulate(neuralNetwork, new double[]{0, 1});
        errorSum =+ output -1;

        output = simulate(neuralNetwork, new double[]{1, 0});
        errorSum =+ output -1;

        output = simulate(neuralNetwork, new double[]{1, 1});
        errorSum =+ output - 0;

        //then
        Assert.assertTrue(errorSum < maxErrorValue);
    }

    private NeuralNetwork getXORNeuralNetwork() {

        NeuralNetwork neuralNetwork = new NeuralNetwork();
        final ActivationFunction activationFunction = new SigmoidActivationFunction();

        Layer inputLayer = new Layer.Builder()
                .addNeuron(NeuronImpl.createNeuron(1, activationFunction))
                .addNeuron(NeuronImpl.createNeuron(1, activationFunction))
                .addNeuron(Bias.createBias(activationFunction))
                .build();

        Layer hiddenLayer = new Layer.Builder()
                .addNeuron(NeuronImpl.createNeuron(3, activationFunction))
                .addNeuron(NeuronImpl.createNeuron(3, activationFunction))
                .addNeuron(NeuronImpl.createNeuron(3, activationFunction))
                .addNeuron(NeuronImpl.createNeuron(3, activationFunction))
                .addNeuron(Bias.createBias(activationFunction))
                .build();

        Layer outputLayer = new Layer.Builder()
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
        double[] output = neuralNetwork.getOutput();

        return output[0];
    }

}

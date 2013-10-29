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
                .add(new TeachData(new double[]{1, 1}, new double[]{1}))
                .build();
    }

    @Test
    public void testSimulate() throws Exception {
        //given
        NeuralNetwork neuralNetwork = new NeuralNetwork();
        final TestActivationFunction activationFunction = new TestActivationFunction();

        Layer inputLayer = new Layer.Builder()
                .addNeuron(new Neuron(new double[]{1}, 1, activationFunction))
                .addNeuron(new Neuron(new double[]{2}, 1, activationFunction))
                .build();

        Layer hiddenLayer = new Layer.Builder()
                .addNeuron(new Neuron(new double[]{1, 2}, 1, activationFunction))
                .addNeuron(new Neuron(new double[]{3, 4}, 1, activationFunction))
                .build();

        Layer outputLayer = new Layer.Builder()
                .addNeuron(new Neuron(new double[]{3, 1}, 1, activationFunction))
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

        TeachConfiguration configuration = new TeachConfiguration(0.01d, 4000000, 0.01);

        //when
        neuralNetwork.teach(xorEpochData, configuration);

        //then
        double output = simulate(neuralNetwork, new double[]{0, 0});
        Assert.assertEquals(output, 0, 0.1d);

        output = simulate(neuralNetwork, new double[]{0, 1});
        Assert.assertEquals(output, 1, 0.1d);

        output = simulate(neuralNetwork, new double[]{1, 0});
        Assert.assertEquals(output, 1, 0.1d);

        output = simulate(neuralNetwork, new double[]{1, 1});
        Assert.assertEquals(output, 0, 0.1d);
    }

    //Run manually - long
    // @Test
    public void testSSE() {
        //given
        NeuralNetwork neuralNetwork = getXORNeuralNetwork();
        double maxErrorValue = 0.2d;
        TeachConfiguration configuration = new TeachConfiguration(maxErrorValue, 4000000, 0.01);
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
                .addNeuron(Neuron.createNeuron(1, activationFunction))
                .addNeuron(Neuron.createNeuron(1, activationFunction))
                .build();

        Layer hiddenLayer = new Layer.Builder()
                .addNeuron(Neuron.createNeuron(2, activationFunction))
                .addNeuron(Neuron.createNeuron(2, activationFunction))
                .build();

        Layer outputLayer = new Layer.Builder()
                .addNeuron(Neuron.createNeuron(2, activationFunction))
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

    @Test
    public void test(){
        NeuralNetwork neuralNetwork = NeuralNetworkFactory.createNetwork(100, 1, 1, 1, new SigmoidActivationFunction());

        double[] inputs = new double[100];

        for(int i=0; i<100; i++){
            inputs[i] = i * 0.001;
        }

        neuralNetwork.setInputs(inputs);
        neuralNetwork.simulate();

        System.out.println("wynik = " + neuralNetwork.getOutput()[0]);

        for(int i=0; i<100; i++){
            inputs[i] = (100-i) * 0.002;
        }

        neuralNetwork.setInputs(inputs);
        neuralNetwork.simulate();

        System.out.println("wynik2 = " + neuralNetwork.getOutput()[0]);
    }

    @Test
    public void sss(){

        System.out.println("************************");
        NeuralNetwork neuralNetwork = new NeuralNetwork();
        final ActivationFunction activationFunction = new SigmoidActivationFunction();

        Layer inputLayer = new Layer.Builder()
                .addNeuron(Neuron.createNeuron(1, activationFunction))
                .addNeuron(Neuron.createNeuron(1, activationFunction))
                .build();

        Layer hiddenLayer = new Layer.Builder()
                .addNeuron(Neuron.createNeuron(2, activationFunction))
                .addNeuron(Neuron.createNeuron(2, activationFunction))
                .addNeuron(Neuron.createNeuron(2, activationFunction))
                .build();

        Layer hiddenLayer2 = new Layer.Builder()
                .addNeuron(Neuron.createNeuron(3, activationFunction))
                .addNeuron(Neuron.createNeuron(3, activationFunction))
                .addNeuron(Neuron.createNeuron(3, activationFunction))
                .build();

        Layer outputLayer = new Layer.Builder()
                .addNeuron(Neuron.createNeuron(3, activationFunction))
                .build();

        neuralNetwork.addLayer(inputLayer);
        neuralNetwork.addLayer(hiddenLayer);
        neuralNetwork.addLayer(hiddenLayer2);
        neuralNetwork.addLayer(outputLayer);

        TeachConfiguration configuration = new TeachConfiguration(0.001d, 1000, 1);

        //when
        neuralNetwork.teach(orEpochData, configuration);

        //then
        double output = simulate(neuralNetwork, new double[]{0, 0});
        System.out.println(output);

        output = simulate(neuralNetwork, new double[]{0, 1});
        System.out.println(output);

        output = simulate(neuralNetwork, new double[]{1, 0});
        System.out.println(output);

        output = simulate(neuralNetwork, new double[]{1, 1});
        System.out.println(output);

        Layer layer = neuralNetwork.getLayer(1);

        System.out.println("Neurony wags");
        for(int i=0; i<layer.getNeuronsCount(); i++){
            Neuron n = layer.getNeuron(i);
            System.out.println("//");
            System.out.println(n.getWag(0));
            System.out.println(n.getWag(1));
            System.out.println("\\");
        }


    }

    @Test
    public void sss2(){

        System.out.println("********TTTT*****");
        NeuralNetwork neuralNetwork = new NeuralNetwork();
        final ActivationFunction activationFunction = new SigmoidActivationFunction();

        Layer inputLayer = new Layer.Builder()
                .addNeuron(Neuron.createNeuron(1, activationFunction))
                .build();

        Layer hiddenLayer = new Layer.Builder()
                .addNeuron(Neuron.createNeuron(1, activationFunction))
                .build();

        neuralNetwork.addLayer(inputLayer);
        neuralNetwork.addLayer(hiddenLayer);

        TeachConfiguration configuration = new TeachConfiguration(0.001d, 1000, 1);

        //when
        neuralNetwork.teach(zero, configuration);

        //then
        double output = simulate(neuralNetwork, new double[]{0});
        System.out.println(output);

        output = simulate(neuralNetwork, new double[]{1});
        System.out.println(output);

        Layer layer = neuralNetwork.getLayer(0);

        System.out.println("Neurony wags");
        for(int i=0; i<layer.getNeuronsCount(); i++){
            Neuron n = layer.getNeuron(i);
            System.out.println("//");
            System.out.println(n.getWag(0));

            System.out.println("\\");
        }


    }

    @Test
    public void sss5(){

        System.out.println("********FFFF*****");
        NeuralNetwork neuralNetwork = new NeuralNetwork();
        final ActivationFunction activationFunction = new SigmoidActivationFunction();

        Layer inputLayer = new Layer.Builder()
                .addNeuron(Neuron.createNeuron(1, activationFunction))
                .addNeuron(Neuron.createNeuron(1, activationFunction))
                .addNeuron(Neuron.createNeuron(1, activationFunction))
                .addNeuron(Neuron.createNeuron(1, activationFunction))
                .build();

        Layer hiddenLayer = new Layer.Builder()
                .addNeuron(Neuron.createNeuron(4, activationFunction))
                .addNeuron(Neuron.createNeuron(4, activationFunction))
                .addNeuron(Neuron.createNeuron(4, activationFunction))
                .build();

        Layer hiddenLayer2 = new Layer.Builder()
                .addNeuron(Neuron.createNeuron(3, activationFunction))
                .addNeuron(Neuron.createNeuron(3, activationFunction))
                .addNeuron(Neuron.createNeuron(3, activationFunction))
                .build();

        Layer outputLayer = new Layer.Builder()
                .addNeuron(Neuron.createNeuron(3, activationFunction))
                .build();

        neuralNetwork.addLayer(inputLayer);
        neuralNetwork.addLayer(hiddenLayer);
        neuralNetwork.addLayer(hiddenLayer2);
        neuralNetwork.addLayer(outputLayer);

        TeachConfiguration configuration = new TeachConfiguration(0.001d, 1, 1);

        //when
        neuralNetwork.teach(zero2, configuration);

        //then
        double output = simulate(neuralNetwork, new double[]{1, 1, 1, 1});
        System.out.println(output);

        Layer layer = neuralNetwork.getLayer(0);

        System.out.println("Neurony wags");
        for(int i=0; i<layer.getNeuronsCount(); i++){
            Neuron n = layer.getNeuron(i);
            System.out.println("//");
            System.out.println(n.getWag(0));

            System.out.println("\\");
        }
    }

    @Test
    public void sss6(){

        System.out.println("********GG*****");
        NeuralNetwork neuralNetwork = new NeuralNetwork();
        final ActivationFunction activationFunction = new SigmoidActivationFunction();

        Layer inputLayer = new Layer.Builder()
                .addNeuron(new Neuron(new double[] {1.0d}, 1, activationFunction ))
                .addNeuron(new Neuron(new double[] {1.0d}, 1, activationFunction ))
                .build();

        Layer hiddenLayer = new Layer.Builder()
                .addNeuron(new Neuron(new double[] {-0.17, 0.42}, 1, activationFunction ))
                .addNeuron(new Neuron(new double[] {0.55, 0.62}, 1, activationFunction ))
                .build();

        Layer outputLayer = new Layer.Builder()
                .addNeuron(new Neuron(new double[] {0.81, 0.35}, 1, activationFunction ))
                .build();

        neuralNetwork.addLayer(inputLayer);
        neuralNetwork.addLayer(hiddenLayer);
        neuralNetwork.addLayer(outputLayer);

        TeachConfiguration configuration = new TeachConfiguration(0.001d, 1, 0.25);

        //when
        neuralNetwork.teach(zero3, configuration);

        //then
        double output = simulate(neuralNetwork, new double[]{1, 0});
        System.out.println(output);

        Layer layer = neuralNetwork.getLayer(0);

        System.out.println("Neurony wags");
        for(int i=0; i<layer.getNeuronsCount(); i++){
            Neuron n = layer.getNeuron(i);
            System.out.println("//");
            System.out.println(n.getWag(0));

            System.out.println("\\");
        }


    }
}

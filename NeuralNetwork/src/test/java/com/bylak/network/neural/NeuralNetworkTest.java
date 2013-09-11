package com.bylak.network.neural;

import com.bylak.network.function.ActivationFunction;
import com.bylak.network.layer.Layer;
import com.bylak.network.util.ArrayListBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 23.08.13
 * Time: 20:52
 * To change this template use File | Settings | File Templates.
 */
public class NeuralNetworkTest {
    @Test
    public void testSimulate() throws Exception {
        //given
        NeuralNetwork neuralNetwork = new NeuralNetwork();
        final TestActivationFunction activationFunction = new TestActivationFunction();

        List<Neuron> inputNeurons = new ArrayListBuilder<Neuron>()
                .add(new Neuron(new double[]{1}, 1, activationFunction))
                .add(new Neuron(new double[]{2}, 1, activationFunction))
                .build();

        List<Neuron> hiddenNeurons = new ArrayListBuilder<Neuron>()
                .add(new Neuron(new double[]{1, 2}, 1, activationFunction))
                .add(new Neuron(new double[]{3, 4}, 1, activationFunction))
                .build();

        List<Neuron> outputNeurons = new ArrayListBuilder<Neuron>()
                .add(new Neuron(new double[]{3, 1}, 1, activationFunction))
                .build();

        Layer inputLayer = new Layer(inputNeurons);
        Layer hiddenLayer = new Layer(hiddenNeurons);
        Layer outputLayer = new Layer(outputNeurons);

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

    private static final class TestActivationFunction implements ActivationFunction {

        @Override
        public double calculate(double xValue) {
            return xValue;
        }
    }
}

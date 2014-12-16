package com.bylak.network.neural;

import com.bylak.network.function.ActivationFunction;
import com.bylak.network.function.SigmoidActivationFunction;
import com.bylak.network.layer.Layer;
import com.bylak.network.neural.teach.EpochData;
import com.bylak.network.neural.teach.TeachConfiguration;
import com.bylak.network.neural.teach.TeachData;
import org.hamcrest.number.IsCloseTo;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

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
    public void shouldReturnCorrectValue() {
        // given
        final NeuralNetwork neuralNetwork = buildStandardNeutralNetwork();

        final int expectedLength = 1;
        final double expectedValue = 16.0;

        // when
        neuralNetwork.simulate();

        // then
        final double[] simulateResult = neuralNetwork.getOutput();
        assertThat(simulateResult.length, equalTo(expectedLength));
        assertThat(simulateResult[0], IsCloseTo.closeTo(expectedValue, 0.1));
    }

    private NeuralNetwork buildStandardNeutralNetwork() {
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

        return neuralNetwork;
    }

    @Test
    public void shouldReturnCorrectXOROperationValues() {
        // given
        final NeuralNetwork neuralNetwork = getXORNeuralNetwork();
        final TeachConfiguration teachConfiguration = new TeachConfiguration(0.01d, 40000000, 1d, 0.7d);

        // when
        neuralNetwork.teach(XOR_EPOCH_DATA, teachConfiguration);

        // then
        assertEqualResponse(neuralNetwork, XOR_EPOCH_DATA);
    }

    private void assertEqualResponse(final NeuralNetwork neuralNetwork, final EpochData epochData){
        for(int i=0; i<epochData.getSize(); i++){
            final TeachData singleEpochData = epochData.getElement(i);
            final double[] input = singleEpochData.getInput();
            final double expectedOutput = singleEpochData.getExpectedOutput()[0];
            final double actualOutput = simulate(neuralNetwork, input);

            assertThat(actualOutput, IsCloseTo.closeTo(actualOutput, 0.1));
        }
    }

    @Test
    public void shouldReturnCorrectOROperationValues() {
        // given
        final NeuralNetwork neuralNetwork = getXORNeuralNetwork();
        final TeachConfiguration configuration = new TeachConfiguration(0.01d, 40000000, 1d, 0.7d);

        // when
        neuralNetwork.teach(OR_EPOCH_DATA, configuration);

        // then
        assertEqualResponse(neuralNetwork, OR_EPOCH_DATA);
    }

    @Test
    public void shouldReturnSSELessThen() {
        // given
        final NeuralNetwork neuralNetwork = getXORNeuralNetwork();
        final double maxErrorValue = 0.2d;
        final TeachConfiguration configuration = new TeachConfiguration(maxErrorValue, 4000000, 1, 0.7d);

        // when
        neuralNetwork.teach(XOR_EPOCH_DATA, configuration);

        //then
        final double errorSum = calculateError(neuralNetwork, XOR_EPOCH_DATA);

        // then
        assertThat(errorSum, lessThan(maxErrorValue));
    }

    private double calculateError(final NeuralNetwork neuralNetwork, final EpochData epochData){
        double errorSum = 0;
        for(int i=0; i<epochData.getSize(); i++){
            final TeachData singleEpochData = epochData.getElement(i);
            final double[] input = singleEpochData.getInput();
            final double expectedOutput = singleEpochData.getExpectedOutput()[0];
            final double actualOutput = simulate(neuralNetwork, input);

            errorSum += actualOutput - expectedOutput;
        }

        return errorSum;
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
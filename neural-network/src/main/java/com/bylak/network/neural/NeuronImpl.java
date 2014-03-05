package com.bylak.network.neural;

import java.util.Random;

import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;

import com.bylak.network.function.ActivationFunction;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 10.09.13
 * Time: 13:52
 * To change this template use File | Settings | File Templates.
 */
public final class NeuronImpl implements Neuron {
    private final RealMatrix wags;
    private double neuronValue;
    private double inputsValue;
    private final ActivationFunction activationFunction;

    public NeuronImpl(final double[] wags, final double neuronValue, final ActivationFunction activationFunction) {
        this.wags = MatrixUtils.createColumnRealMatrix(wags);
        this.neuronValue = neuronValue;
        this.activationFunction = activationFunction;
    }

    public static Neuron createNeuron(final int inputCount, final ActivationFunction activationFunction) {
        final Random random = new Random();
        final double wags[] = new double[inputCount];
        final double neuronValue = random.nextDouble();

        for (int i = 0; i < wags.length; i++) {
            final double newWag = random.nextDouble();
            wags[i] = newWag;
        }

        return new NeuronImpl(wags, neuronValue, activationFunction);
    }

    @Override
    public double getValue() {
        return this.neuronValue;
    }

    @Override
    public void simulate(final RealMatrix inputs) {
        final RealMatrix multiplied = inputs.multiply(wags);
        inputsValue = sumFirstRow(multiplied);
        neuronValue = activationFunction.value(inputsValue);
    }

    private double sumFirstRow(final RealMatrix matrixToSum) {
        double sum = 0;
        for (final double value : matrixToSum.getColumn(0)) {
            sum += value;
        }

        return sum;
    }

    @Override
    public void setValue(final double newValue) {
        this.neuronValue = newValue;
    }

    @Override
    public double getInputDerivativeValue() {
        return this.activationFunction.derivativeValue(inputsValue);
    }

    @Override
    public double getWag(final int index) {
        return this.wags.getEntry(index, 0);
    }

    @Override
    public void setWag(final int index, final double value) {
        this.wags.setEntry(index, 0, value);
    }
}

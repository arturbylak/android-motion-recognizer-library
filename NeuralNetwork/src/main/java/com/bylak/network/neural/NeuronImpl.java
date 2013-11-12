package com.bylak.network.neural;

import com.bylak.network.function.ActivationFunction;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 10.09.13
 * Time: 13:52
 * To change this template use File | Settings | File Templates.
 */
public final class NeuronImpl implements Neuron {
    private RealMatrix wags;
    private double neuronValue;
    private double inputsValue;
    private final ActivationFunction activationFunction;

    public NeuronImpl(double[] wags, double neuronValue, final ActivationFunction activationFunction) {
        this.wags = MatrixUtils.createColumnRealMatrix(wags);
        this.neuronValue = neuronValue;
        this.activationFunction = activationFunction;
    }

    public static Neuron createNeuron(int inputCount, final ActivationFunction activationFunction) {
        Random random = new Random();
        double wags[] = new double[inputCount];
        double neuronValue = random.nextDouble();

        for (int i = 0; i < wags.length; i++) {
            double newWag = random.nextDouble();
            wags[i] = newWag;
        }

        return new NeuronImpl(wags, neuronValue, activationFunction);
    }

    @Override
    public double getValue() {
        return this.neuronValue;
    }

    @Override
    public void simulate(RealMatrix inputs) {
        RealMatrix multiplied = inputs.multiply(wags);
        inputsValue = sumFirstRow(multiplied);
        neuronValue = activationFunction.value(inputsValue);
    }

    private double sumFirstRow(RealMatrix matrixToSum) {
        double sum = 0;
        for (double value : matrixToSum.getColumn(0)) {
            sum += value;
        }

        return sum;
    }

    @Override
    public void setValue(double newValue) {
        this.neuronValue = newValue;
    }

    @Override
    public double getInputDerivativeValue() {
        return this.activationFunction.derivativeValue(inputsValue);
    }

    @Override
    public double getWag(int index) {
        return this.wags.getEntry(index, 0);
    }

    @Override
    public void setWag(int index, double value) {
        this.wags.setEntry(index, 0, value);
    }
}
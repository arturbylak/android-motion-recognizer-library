package com.bylak.network.neural;

import com.bylak.network.function.ActivationFunction;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 10.09.13
 * Time: 13:52
 * To change this template use File | Settings | File Templates.
 */
public final class Neuron {
    private RealMatrix wags;
    private double neuronValue;
    private final ActivationFunction activationFunction;

    public Neuron(double[] wags, double neuronValue, final ActivationFunction activationFunction) {
        this.wags = MatrixUtils.createColumnRealMatrix(wags);
        this.neuronValue = neuronValue;
        this.activationFunction = activationFunction;
    }

    public double getValue() {
        return this.neuronValue;
    }

    public void calculateNewNeuronValue(RealMatrix inputs) {
        RealMatrix multiplied = inputs.multiply(wags);
        double newNeuronValue = sumFirstRow(multiplied);
        neuronValue = activateFunctionValue(newNeuronValue);
    }

    private double sumFirstRow(RealMatrix matrixToSum) {
        double sum = 0;
        for (double value : matrixToSum.getColumn(0)) {
            sum += value;
        }

        return sum;
    }

    private double activateFunctionValue(double value) {
        return activationFunction.value(value);
    }
}

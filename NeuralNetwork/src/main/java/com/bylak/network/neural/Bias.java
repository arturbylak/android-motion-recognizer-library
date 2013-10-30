package com.bylak.network.neural;

import com.bylak.network.function.ActivationFunction;
import org.apache.commons.math.linear.RealMatrix;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 30.10.13
 * Time: 12:26
 * To change this template use File | Settings | File Templates.
 */
public final class Bias implements Neuron{

    private final ActivationFunction activationFunction;
    private static final int BIAS_VALUE = 1;


    private Bias(final ActivationFunction activationFunction){
         this.activationFunction = activationFunction;
    }

    @Override
    public double getValue() {
        return BIAS_VALUE;
    }

    @Override
    public void simulate(RealMatrix inputs) {
    }

    @Override
    public void setValue(double newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getInputDerivativeValue() {
        return activationFunction.derivativeValue(BIAS_VALUE);
    }

    @Override
    public double getWag(int index) {
       return 0;
    }

    @Override
    public void setWag(int index, double value) {
    }

    public static Neuron createBias(final ActivationFunction activationFunction){
        return new Bias(activationFunction);
    }
}

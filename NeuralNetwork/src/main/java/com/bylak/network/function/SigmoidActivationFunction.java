package com.bylak.network.function;

/**
 * Created with IntelliJ IDEA.
 * User: bylak
 * Date: 23.08.13
 * Time: 20:39
 * To change this template use File | Settings | File Templates.
 */
public class SigmoidActivationFunction implements ActivationFunction {
    @Override
    public double calculate(double xValue) {
        return 1.0 / (1 + Math.exp(-1.0 * xValue));
    }
}

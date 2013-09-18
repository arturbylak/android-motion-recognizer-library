package com.bylak.network.function;

/**
 * Created with IntelliJ IDEA.
 * User: bylak
 * Date: 22.08.13
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
public interface ActivationFunction {
    double value(double xValue);
    double derivativeValue(double xValue);
}

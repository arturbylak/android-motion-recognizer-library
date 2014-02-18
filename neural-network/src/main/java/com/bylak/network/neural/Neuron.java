package com.bylak.network.neural;

import org.apache.commons.math.linear.RealMatrix;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 30.10.13
 * Time: 12:24
 * To change this template use File | Settings | File Templates.
 */
public interface Neuron {
    double getValue();

    void simulate(RealMatrix inputs);

    void setValue(double newValue);

    double getInputDerivativeValue();

    double getWag(int index);

    void setWag(int index, double value);
}

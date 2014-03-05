package com.bylak.network.neural;

import com.bylak.network.function.ActivationFunction;

public final class TestActivationFunction implements ActivationFunction {
    public TestActivationFunction() {
    }

    @Override
    public double value(final double xValue) {
        return xValue;
    }

    @Override
    public double derivativeValue(final double xValue) {
        return 1;
    }
}
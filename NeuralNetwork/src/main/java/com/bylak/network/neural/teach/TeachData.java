package com.bylak.network.neural.teach;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 11.09.13
 * Time: 15:05
 * To change this template use File | Settings | File Templates.
 */
public final class TeachData {
    private final double[] input;
    private final double[] expectedOutput;

    public TeachData(final double[] input, final double[] expectedOutput) {
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    public double[] getInput() {
        return input;
    }

    public double[] getExpectedOutput() {
        return expectedOutput;
    }
}

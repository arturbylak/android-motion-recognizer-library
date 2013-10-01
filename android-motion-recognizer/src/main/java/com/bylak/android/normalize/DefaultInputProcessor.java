package com.bylak.android.normalize;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 30.09.13
 * Time: 13:01
 * To change this template use File | Settings | File Templates.
 */
public final class DefaultInputProcessor implements InputProcessor {
    public double[] processInput(int expectedOutputCount, float[] dataToProcess) {
        double[] normalizedData = normalize(dataToProcess);

        return changeSize(normalizedData, expectedOutputCount);
    }

    public double[] normalize(float[] dataToProcess) {
        return new double[0];
    }

    public double[] changeSize(double[] dataToProcess, int expectedOutputCount) {
        return dataToProcess;
    }
}

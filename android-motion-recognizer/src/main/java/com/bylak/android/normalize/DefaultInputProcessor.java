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

        if (expectedOutputCount < 0) {
            throw new IllegalArgumentException("Expected count should be positive");
        }

        if (dataToProcess == null || dataToProcess.length < 1) {
            throw new IllegalArgumentException("Incorrect data to process");
        }

        double[] normalizedData = normalize(dataToProcess);

        return changeSize(normalizedData, expectedOutputCount);
    }

    private double[] normalize(float[] dataToProcess) {
        int length = dataToProcess.length;

        double min = dataToProcess[0];
        double max = dataToProcess[0];
        for (int i = 0; i < length; i++) {
            double currentValue = dataToProcess[i];

            if (currentValue > max) {
                max = currentValue;
            }
            if (currentValue < min) {
                min = currentValue;
            }
        }

        return normalize(max, min, dataToProcess);
    }

    private double[] normalize(double max, double min, float[] dataToNormalzie) {
        int length = dataToNormalzie.length;
        double[] normalziedData = new double[length];
        double diff = (max - min);

        for (int i = 0; i < length; i++) {
            double currentValue = dataToNormalzie[i];

            normalziedData[i] = (currentValue - min) / diff;
        }

        return normalziedData;
    }

    private double[] changeSize(double[] dataToProcess, int expectedOutputCount) {
        return dataToProcess;
    }
}

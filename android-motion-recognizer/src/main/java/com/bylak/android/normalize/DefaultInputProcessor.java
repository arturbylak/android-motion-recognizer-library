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
        int length = dataToProcess.length;

        if (expectedOutputCount == length) {
            return dataToProcess;
        } else if (expectedOutputCount == 2 && length > 2) {
            return new double[]{dataToProcess[0], dataToProcess[length - 1]};
        } else if (expectedOutputCount < length) {
            return reduceArray(dataToProcess, expectedOutputCount);
        } else {
            return resizeArray(dataToProcess, expectedOutputCount);
        }
    }

    private double[] reduceArray(double[] dataToProcess, int expectedOutputCount) {
        double[] reducedValues = new double[expectedOutputCount];
        int currentLength = dataToProcess.length;
        int step = currentLength / expectedOutputCount;

        for (int i = 0; i < expectedOutputCount; i++) {
            int nextIndex = i * step;
            reducedValues[i] = dataToProcess[nextIndex];
        }

        return reducedValues;
    }

    //TODO Refactoring
    private double[] resizeArray(double[] dataToProcess, int expectedOutputCount) {
        double[] resizedData = new double[expectedOutputCount];
        int currentDataLength = dataToProcess.length;
        int step = expectedOutputCount / currentDataLength;
        int lastCurrentArrayIndex = currentDataLength - 1;

        for (int i = 0; i < currentDataLength; i++) {
            int newIndex = i * step;

            if (newIndex < expectedOutputCount) {
                resizedData[newIndex] = dataToProcess[i];
            }
        }

        for (int i = 0; i < currentDataLength - 1; i++) {
            int startIndex = i * step;
            int stopIndex = (i + 1) * step;

            double avg = (resizedData[startIndex] + resizedData[stopIndex]) / 2;

            for (int j = startIndex + 1; j < stopIndex; j++) {
                resizedData[j] = avg;
            }
        }

        int previousIndex = (lastCurrentArrayIndex) * step;
        double lastValue = resizedData[previousIndex];
        for (int i = previousIndex + 1; i < expectedOutputCount; i++) {
            resizedData[i] = lastValue;
        }

        return resizedData;
    }
}

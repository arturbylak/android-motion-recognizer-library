package com.bylak.android.normalize;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 18.02.14
 * Time: 12:32
 * To change this template use File | Settings | File Templates.
 */
public final class DefaultDataSizeChanger implements DataSizeChanger{
    @Override
    public double[] changeSize(double[] dataToProcess, int expectedOutputCount) {
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

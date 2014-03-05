package com.bylak.android.normalize;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 18.02.14
 * Time: 12:32
 * To change this template use File | Settings | File Templates.
 */
public final class DefaultDataSizeChanger implements DataSizeChanger {
    @Override
    public double[] changeSize(final double[] dataToProcess, final int expectedOutputCount) {
        final int length = dataToProcess.length;

        if (expectedOutputCount == length) {
            return dataToProcess;
        } else if (expectedOutputCount == 2 && length > 2) {
            return new double[] { dataToProcess[0], dataToProcess[length - 1] };
        } else if (expectedOutputCount < length) {
            return reduceArray(dataToProcess, expectedOutputCount);
        } else {
            return resizeArray(dataToProcess, expectedOutputCount);
        }
    }

    private double[] reduceArray(final double[] dataToProcess, final int expectedOutputCount) {
        final double[] reducedValues = new double[expectedOutputCount];
        final int currentLength = dataToProcess.length;
        final int step = currentLength / expectedOutputCount;

        for (int i = 0; i < expectedOutputCount; i++) {
            final int nextIndex = i * step;
            reducedValues[i] = dataToProcess[nextIndex];
        }

        return reducedValues;
    }

    // TODO Refactoring
    private double[] resizeArray(final double[] dataToProcess, final int expectedOutputCount) {
        final double[] resizedData = new double[expectedOutputCount];
        final int currentDataLength = dataToProcess.length;
        final int step = expectedOutputCount / currentDataLength;
        final int lastCurrentArrayIndex = currentDataLength - 1;

        for (int i = 0; i < currentDataLength; i++) {
            final int newIndex = i * step;

            if (newIndex < expectedOutputCount) {
                resizedData[newIndex] = dataToProcess[i];
            }
        }

        for (int i = 0; i < currentDataLength - 1; i++) {
            final int startIndex = i * step;
            final int stopIndex = (i + 1) * step;

            final double avg = (resizedData[startIndex] + resizedData[stopIndex]) / 2;

            for (int j = startIndex + 1; j < stopIndex; j++) {
                resizedData[j] = avg;
            }
        }

        final int previousIndex = lastCurrentArrayIndex * step;
        final double lastValue = resizedData[previousIndex];
        for (int i = previousIndex + 1; i < expectedOutputCount; i++) {
            resizedData[i] = lastValue;
        }

        return resizedData;
    }
}

package com.bylak.android.normalize;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 30.09.13
 * Time: 13:01
 * To change this template use File | Settings | File Templates.
 */
public final class DefaultInputProcessor implements InputProcessor {

    private final DataNormalizer dataNormalizer;
    private final DataSizeChanger dataSizeChanger;

    public DefaultInputProcessor() {
        this.dataNormalizer = new DefaultDataNormalizer();
        this.dataSizeChanger = new DefaultDataSizeChanger();
    }

    @Override
    public double[] processInput(final int expectedOutputCount, final float[] dataToProcess) {

        if (expectedOutputCount < 0) {
            throw new IllegalArgumentException("Expected count should be positive");
        }

        if (dataToProcess == null || dataToProcess.length < 1) {
            throw new IllegalArgumentException("Incorrect data to process");
        }

        final double[] normalizedData = normalize(dataToProcess);

        return changeSize(normalizedData, expectedOutputCount);
    }

    private double[] normalize(final float[] dataToProcess) {
        return dataNormalizer.normalizeData(dataToProcess);
    }

    private double[] changeSize(final double[] dataToProcess, final int expectedOutputCount) {
        return dataSizeChanger.changeSize(dataToProcess, expectedOutputCount);
    }
}

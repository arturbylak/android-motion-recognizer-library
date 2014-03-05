package com.bylak.android.normalize;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 18.02.14
 * Time: 12:23
 * To change this template use File | Settings | File Templates.
 */
public final class DefaultDataNormalizer implements DataNormalizer {
    @Override
    public double[] normalizeData(final float[] dataToNormalize) {
        final int length = dataToNormalize.length;

        double min = dataToNormalize[0];
        double max = dataToNormalize[0];

        for (int i = 0; i < length; i++) {
            final double currentValue = dataToNormalize[i];

            if (currentValue > max) {
                max = currentValue;
            }
            if (currentValue < min) {
                min = currentValue;
            }
        }

        return normalize(max, min, dataToNormalize);
    }

    private double[] normalize(final double max, final double min, final float[] dataToNormalize) {
        final int length = dataToNormalize.length;
        final double[] normalizedData = new double[length];
        final double diff = max - min;

        for (int i = 0; i < length; i++) {
            final double currentValue = dataToNormalize[i];

            normalizedData[i] = (currentValue - min) / diff;
        }

        return normalizedData;
    }
}

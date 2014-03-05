package com.bylak.android.motion.recognize;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 01.10.13
 * Time: 16:20
 * To change this template use File | Settings | File Templates.
 */
public final class DefaultMotionResolver implements MotionResolver {
    private final double threshold;

    public DefaultMotionResolver(final double threshold) {
        this.threshold = threshold;
    }

    @Override
    public MotionType resolve(final Map<MotionType, Double[]> simulationOutput) {

        if (simulationOutput == null) {
            throw new IllegalArgumentException("Simulation output cant be null");
        }

        MotionType motionType = new MotionType(MotionType.UNKNOWN, "");
        double biggestValue = 0;

        for (final Map.Entry<MotionType, Double[]> entry : simulationOutput.entrySet()) {
            final MotionType key = entry.getKey();
            final Double[] networkOutputs = entry.getValue();

            final double output = sum(networkOutputs);

            if (biggestValue < output && output > threshold) {
                motionType = key;
                biggestValue = output;
            }
        }

        return motionType;
    }

    private double sum(final Double[] values) {
        double sum = 0;
        for (final Double value : values) {
            sum += value;
        }

        return sum;
    }
}

package com.bylak.network.neural.teach;

/**
 * Created with IntelliJ IDEA.
 * User: bylak
 * Date: 14.09.13
 * Time: 13:33
 * To change this template use File | Settings | File Templates.
 */
public final class TeachConfiguration {
    private final double maxErrorValue;
    private final int epochCount;
    private final double learningFactor;
    private final double momentumFactor;

    public TeachConfiguration(final double maxErrorValue, final int epochCount, final double learningFactor, final double momentumFactor) {
        this.maxErrorValue = maxErrorValue;
        this.epochCount = epochCount;
        this.learningFactor = learningFactor;
        this.momentumFactor = momentumFactor;
    }

    public double getMaxErrorValue() {
        return maxErrorValue;
    }

    public int getEpochCount() {
        return epochCount;
    }

    public double getLearningFactor() {
        return learningFactor;
    }

    public double getMomentumFactor() {
        return momentumFactor;
    }
}

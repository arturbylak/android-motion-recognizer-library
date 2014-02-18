package com.bylak.network.neural.teach;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 06.11.13
 * Time: 13:55
 * To change this template use File | Settings | File Templates.
 */
public interface LeaningFactorCalculator {
    double calculate(double currentSSE, double lastSSE, double lastLearningFactor);
}

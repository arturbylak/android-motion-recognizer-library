package com.bylak.network.neural.teach;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 06.11.13
 * Time: 13:56
 * To change this template use File | Settings | File Templates.
 */
public class DefaultLearningFactorCalculator implements LeaningFactorCalculator{
    private static final double KW = 1.04;
    private static final double PI = 1.05;
    private static final double PD = 0.7;

    @Override
    public double calculate(double currentSSE, double lastSSE, double lastLearningFactor) {
        double learningFactor = 0;
        if(currentSSE - (KW * lastSSE) > 0){
            learningFactor = lastLearningFactor * PD;
        }else{
            learningFactor = lastLearningFactor * PI;
        }

        return learningFactor;
    }
}

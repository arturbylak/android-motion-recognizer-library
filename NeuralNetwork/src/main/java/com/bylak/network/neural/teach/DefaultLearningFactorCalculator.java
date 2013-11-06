package com.bylak.network.neural.teach;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 06.11.13
 * Time: 13:56
 * To change this template use File | Settings | File Templates.
 */
public class DefaultLearningFactorCalculator implements LeaningFactorCalculator{
    private double kw = 1.04;
    private double pi = 1.05;
    private double pd = 0.7;

    @Override
    public double calculate(double currentSSE, double lastSSE, double lastLearningFactor) {
        if(currentSSE - (kw * lastSSE) > 0){
            lastLearningFactor = lastLearningFactor * pd;
        }else{
            lastLearningFactor = lastLearningFactor * pi;
        }

        return lastLearningFactor;
    }
}

package com.bylak.network.util;

import com.bylak.network.neural.NeuralNetwork;
import com.bylak.network.neural.teach.EpochData;
import com.bylak.network.neural.teach.TeachData;

/**
 * Created with IntelliJ IDEA.
 * User: bylak
 * Date: 08.02.14
 * Time: 16:23
 * To change this template use File | Settings | File Templates.
 */
public final class SSECalculator {
    public double calculateCurrentSSE(final NeuralNetwork neuralNetwork, final EpochData epochData) {
        double sse = 0;
        for (int i = 0; i < epochData.getSize(); i++) {
            final TeachData teachData = epochData.getElement(i);
            neuralNetwork.setInputs(teachData.getInput());

            neuralNetwork.simulate();

            final double[] simulationResult = neuralNetwork.getOutput();
            final double[] expectedOutput = teachData.getExpectedOutput();

            final double error = getOutputError(simulationResult, expectedOutput);
            sse += Math.pow(error, 2);
        }

        return Math.sqrt(sse);
    }

    private double getOutputError(final double[] results, final double[] expectedData) {
        double errorSum = 0;

        for (int i = 0; i < results.length; i++) {
            final double diff = results[i] - expectedData[i];
            errorSum += diff;
        }

        return errorSum;
    }
}

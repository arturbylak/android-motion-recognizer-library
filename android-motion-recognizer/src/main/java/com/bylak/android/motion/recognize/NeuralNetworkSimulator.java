package com.bylak.android.motion.recognize;

import com.bylak.android.normalize.DefaultInputProcessor;
import com.bylak.android.normalize.InputProcessor;
import com.bylak.network.neural.NeuralNetwork;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 01.10.13
 * Time: 15:42
 * To change this template use File | Settings | File Templates.
 */
public final class NeuralNetworkSimulator {
    private final Map<MotionType, NeuralNetwork> networks;
    private final InputProcessor inputProcessor;

    public NeuralNetworkSimulator(final Map<MotionType, NeuralNetwork> networks) {
        this.networks = networks;
        this.inputProcessor = new DefaultInputProcessor();
    }

    public Map<MotionType, Double[]> invokeAll(final float[] data) {
        Map<MotionType, Double[]> simulationResult = new HashMap<>();
        for (Map.Entry<MotionType, NeuralNetwork> entry : networks.entrySet()) {
            MotionType key = entry.getKey();
            NeuralNetwork network = entry.getValue();
            int networkInputCount = network.getInputCount();

            double[] processedData = processInputData(data, networkInputCount);
            double[] output = simulate(network, processedData);

            simulationResult.put(key, convert(output));
        }

        return simulationResult;
    }

    private double[] processInputData(float[] data, int networkInputCount) {
        return inputProcessor.processInput(networkInputCount, data);
    }

    private double[] simulate(NeuralNetwork network, double[] processedData) {
        network.setInputs(processedData);
        network.simulate();

        return network.getOutput();
    }

    private Double[] convert(double[] values) {
        Double[] newObject = new Double[values.length];

        for (int i = 0; i < values.length; i++) {
            newObject[i] = new Double(values[i]);
        }

        return newObject;
    }
}

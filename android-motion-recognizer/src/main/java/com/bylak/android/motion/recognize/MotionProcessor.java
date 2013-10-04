package com.bylak.android.motion.recognize;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import com.bylak.android.motion.recognize.listener.OnRecognizedListener;
import com.bylak.android.util.InputQueue;
import com.bylak.network.neural.NeuralNetwork;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 30.09.13
 * Time: 13:26
 * To change this template use File | Settings | File Templates.
 */
public final class MotionProcessor implements SensorEventListener {
    public static final int MOTION_UNKNOWN = -1;
    private static final int BUFFOR_SIZE = 50 * 3;
    private static final int PACKAGE_SIZE = 3;
    private final OnRecognizedListener onRecognizedListener;
    private final Sensor sensor;
    private final NeuralNetworkSimulator simulator;
    private final MotionResolver motionResolver;
    private final InputQueue inputQueue;

    public MotionProcessor(final Sensor sensor, final OnRecognizedListener onRecognizedListener, final Map<MotionType, NeuralNetwork> networks, double thresholdValue) {
        this.onRecognizedListener = onRecognizedListener;
        this.sensor = sensor;
        this.simulator = new NeuralNetworkSimulator(networks);
        this.motionResolver = new DefaultMotionResolver(thresholdValue);
        this.inputQueue = new InputQueue(PACKAGE_SIZE);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] values = sensorEvent.values;
        int queueSize = inputQueue.size();

        inputQueue.add(values);

        if (queueSize == BUFFOR_SIZE) {
            processInput();
        }

    }

    private void processInput() {
        float[] valuesToProcess = inputQueue.getAllData();

        Map<MotionType, Double[]> simulationOutput = simulator.invokeAll(valuesToProcess);
        MotionType motionType = motionResolver.resolve(simulationOutput);

        if (motionType.getId() != MOTION_UNKNOWN) {
            onRecognizedListener.onRecognize(motionType);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


}

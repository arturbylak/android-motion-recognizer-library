package com.bylak.android.motion.recognize;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import com.bylak.android.motion.recognize.listener.OnRecognizedListener;
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
    private final OnRecognizedListener onRecognizedListener;
    private final Sensor sensor;
    private final NeuralNetworkSimulator simulator;
    private final MotionResolver motionResolver;

    public static final int MOTION_UNKNOWN = -1;

    public MotionProcessor(final Sensor sensor, final OnRecognizedListener onRecognizedListener, final Map<MotionType, NeuralNetwork> networks, double thresholdValue) {
        this.onRecognizedListener = onRecognizedListener;
        this.sensor = sensor;
        this.simulator = new NeuralNetworkSimulator(networks);
        this.motionResolver = new DefaultMotionResolver(thresholdValue);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] values = sensorEvent.values;

        Map<MotionType, Double[]> simulationOutput = simulator.invokeAll(values);
        MotionType motionType = motionResolver.resolve(simulationOutput);

        if (motionType.getId() != MOTION_UNKNOWN) {
            onRecognizedListener.onRecognize(motionType);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


}

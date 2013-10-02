package com.bylak.android.motion.recognize;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import com.bylak.android.motion.recognize.listener.OnRecognizedListener;
import com.bylak.network.neural.NeuralNetwork;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: bylak
 * Date: 28.09.13
 * Time: 12:12
 * To change this template use File | Settings | File Templates.
 */
public final class MotionRecognizer {
    private final Map<MotionType, NeuralNetwork> networks;
    private final SensorManager sensorManager;
    private final MotionProcessor motionProcessor;
    private final Sensor selectedSensor;

    private MotionRecognizer(final Map<MotionType, NeuralNetwork> networks, final SensorManager sensorManager, final OnRecognizedListener onRecognizedListener, double thresholdValue) {
        this.networks = networks;
        this.sensorManager = sensorManager;

        selectedSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        motionProcessor = new MotionProcessor(selectedSensor, onRecognizedListener, networks, thresholdValue);

        init();
    }

    public void init() {
        registerSensorListeners();
    }

    private void registerSensorListeners() {
        this.sensorManager.registerListener(motionProcessor, selectedSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public MotionRecognizer addNetwork(final NeuralNetwork networkToAdd, final MotionType motionType) {
        this.networks.put(motionType, networkToAdd);

        return this;
    }

    public void removeNetwork(final MotionType motionType) {
        this.networks.remove(motionType);
    }

    public static class Builder {
        private final Map<MotionType, NeuralNetwork> networks;
        private SensorManager sensorManager;
        private OnRecognizedListener onRecognizedListener;
        private double thresholdValue;

        public Builder() {
            this.networks = new HashMap<>();
        }

        public Builder add(final NeuralNetwork networkToAdd, final MotionType motionType) {
            this.networks.put(motionType, networkToAdd);

            return this;
        }

        public Builder sensor(final SensorManager sensorManager) {
            this.sensorManager = sensorManager;

            return this;
        }

        public Builder threshold(final double thresholdValue) {
            this.thresholdValue = thresholdValue;

            return this;
        }

        public Builder recognizedListener(final OnRecognizedListener onRecognizedListener) {
            this.onRecognizedListener = onRecognizedListener;

            return this;
        }

        public MotionRecognizer build() {
            return new MotionRecognizer(this.networks, sensorManager, onRecognizedListener, thresholdValue);
        }
    }
}

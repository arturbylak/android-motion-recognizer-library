package com.bylak.android.motion.recognize.listener;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 30.09.13
 * Time: 13:26
 * To change this template use File | Settings | File Templates.
 */
public final class DefaultSensorEventListener implements SensorEventListener {
    private final OnRecognizedListener onRecognizedListener;

    public DefaultSensorEventListener(final OnRecognizedListener onRecognizedListener) {
        this.onRecognizedListener = onRecognizedListener;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

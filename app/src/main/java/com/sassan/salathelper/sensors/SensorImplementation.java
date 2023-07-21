package com.sassan.salathelper.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorImplementation implements SensorEventListener {

    private final SensorManager sensorManager;
    private final Sensor sensor;
    private SensorListener listener;

    public SensorImplementation(Context context, int sensorType) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensorType);
    }

    public void start() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    public void setListener(SensorListener listener) {
        this.listener = listener;
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (listener != null) {
            listener.onSensorValueChanged(sensorEvent.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public interface SensorListener {
        void onSensorValueChanged(float lightValue);
    }
}

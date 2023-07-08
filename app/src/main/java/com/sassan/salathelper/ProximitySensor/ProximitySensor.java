package com.sassan.salathelper.ProximitySensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ProximitySensor implements SensorEventListener {
    private final SensorManager sensorManager;
    private final Sensor proximitySensor;

    private float proximityValue;
    private ProximitySensorListener listener;

    public ProximitySensor(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    public void start() {
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    public void setListener(ProximitySensorListener listener) {
        this.listener = listener;
    }


    @Override
    public final void onSensorChanged(SensorEvent event) {
        proximityValue = event.values[0];
        if (listener != null) {
            listener.onProximitySensorChanged(proximityValue);
        }
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    public float getProximityValue() {
        return proximityValue;
    }

    public interface ProximitySensorListener {
        void onProximitySensorChanged(float lightValue);
    }
}
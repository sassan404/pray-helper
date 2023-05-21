package com.example.lightsensorreader.LightSensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class LightSensor implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private float lightValue;
    private LightSensorListener listener;

    public interface LightSensorListener {
        void onLightSensorChanged(float lightValue);
    }

    public LightSensor(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    public void start() {
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    public void setListener(LightSensorListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        lightValue = event.values[0];
        if (listener != null) {
            listener.onLightSensorChanged(lightValue);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used, but required to implement SensorEventListener
    }

    public float getLightValue() {
        return lightValue;
    }
}

package com.sassan.salathelper.sensors;

import android.content.Context;
import android.hardware.Sensor;

public class LightSensor extends SensorImplementation {

    public LightSensor(Context context) {
        super(context, Sensor.TYPE_LIGHT);
    }


}

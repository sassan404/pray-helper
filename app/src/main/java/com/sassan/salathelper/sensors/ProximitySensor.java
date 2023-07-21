package com.sassan.salathelper.sensors;

import android.content.Context;
import android.hardware.Sensor;

public class ProximitySensor extends SensorImplementation {

    public ProximitySensor(Context context) {
        super(context, Sensor.TYPE_PROXIMITY);
    }

}
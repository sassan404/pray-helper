package com.example.lightsensorreader;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lightsensorreader.LightSensor.LightSensor;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "mainActivity";

    private LightSensor lightSensor;

    private LightChangeDetector lightChangeDetector;

    private PrayerSpecificView prayerSpecificView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        prayerSpecificView = findViewById(R.id.prayer_specific_view);

        lightChangeDetector = findViewById(R.id.light_change_detector);


        lightSensor = new LightSensor(this);
        lightSensor.setListener(this::changeApplication);
    }

    void changeApplication(float lightValue) {
        if (lightChangeDetector.updateLightValue(lightValue)) {
            prayerSpecificView.updatePrayerCounter();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        lightSensor.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        lightSensor.stop();
    }

    public void startCounting(View view) {
        prayerSpecificView.startCounting();
        lightChangeDetector.startCount();
    }

    public void resetCount(View view) {
        prayerSpecificView.resetCount();
        lightChangeDetector.resetCount();
    }
}

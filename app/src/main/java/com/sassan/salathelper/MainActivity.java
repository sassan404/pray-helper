package com.sassan.salathelper;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.sassan.salathelper.LightSensor.LightSensor;
import com.sassan.salathelper.ProximitySensor.ProximitySensor;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "mainActivity";

    private LightSensor lightSensor;

    private ProximitySensor proximitySensor;

    private LightChangeDetector lightChangeDetector;

    private PrayerSpecificView prayerSpecificView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        prayerSpecificView = findViewById(R.id.prayer_specific_view);

        lightChangeDetector = findViewById(R.id.light_change_detector);


        lightSensor = new LightSensor(this);
        proximitySensor = new ProximitySensor(this);
        proximitySensor.setListener(this::changeApplication);
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
        proximitySensor.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        lightSensor.stop();
        proximitySensor.stop();
    }

    public void startCounting(View view) {
        showWarningDialogue();
    }

    public void resetCount(View view) {
        prayerSpecificView.resetCount();
        lightChangeDetector.resetCount();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void showWarningDialogue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.light_warning_message)
                .setTitle(R.string.light_warning_message_title);

        // Add the buttons
        builder.setPositiveButton(R.string.light_warning_message_start, (dialog, id) -> {
            prayerSpecificView.startCounting();
            lightChangeDetector.startCount();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        });
        builder.setNegativeButton(R.string.light_warning_message_cancel, (dialog, id) -> {
            // User cancelled the dialog
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

package com.sassan.salathelper;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.sassan.salathelper.LightSensor.LightSensor;
//import com.sassan.salathelper.ProximitySensor.ProximitySensor;

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
        showWarningDialogue();
    }

    public void resetCount(View view) {
        prayerSpecificView.resetCount();
        lightChangeDetector.resetCount();
    }

    private void showWarningDialogue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("This app works well on light values between 20 and 800; make sure that the light intensity in the room is between those two values")
                .setTitle("Limitations");

        // Add the buttons
        builder.setPositiveButton("Start", (dialog, id) -> {
            prayerSpecificView.startCounting();
            lightChangeDetector.startCount();
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> {
            // User cancelled the dialog
        });
//        Dialog dialog = new Dialog(this, R.style.DialogueStyle);
//        dialog.setContentView(R.layout.warning_dialog);
//
//        dialog.getWindow().setBackgroundDrawableResource(R.drawable.warning_dialog_background);
//
//
//        dialog.show();
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

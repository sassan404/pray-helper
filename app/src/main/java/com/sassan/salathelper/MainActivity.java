package com.sassan.salathelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.sassan.salathelper.sensors.LightSensor;
import com.sassan.salathelper.sensors.ProximitySensor;
import com.sassan.salathelper.sensors.SensorImplementation;

public class MainActivity extends AppCompatActivity {

    private SensorImplementation usedSensor;

    private ChangeDetector changeDetector;

    private PrayerSpecificView prayerSpecificView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        SensorSelectionActivity.Mode mode = (SensorSelectionActivity.Mode) intent.getSerializableExtra("mode");

        setContentView(R.layout.activity_main);

        prayerSpecificView = findViewById(R.id.prayer_specific_view);

        changeDetector = findViewById(R.id.light_change_detector);

        Button resetButton = findViewById(R.id.reset_button);

        resetButton.setOnClickListener(v -> resetCount());


        switch (mode) {
            case MANUAL:
                prayerSpecificView.setBackground(ContextCompat.getDrawable(this, R.drawable.border));
                prayerSpecificView.setOnClickListener(view -> prayerSpecificView.updatePrayerCounter());
            case LIGHT:
                usedSensor = new LightSensor(this);
                usedSensor.setListener(this::changeApplication);
                break;
            case PROXIMITY:
                usedSensor = new ProximitySensor(this);
                usedSensor.setListener(this::changeApplication);
                break;
        }

        changeDetector.setMode(mode);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    void changeApplication(float lightValue) {
        if (changeDetector.updateSensorValue(lightValue)) {
            prayerSpecificView.updatePrayerCounter();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        usedSensor.start();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onPause() {
        super.onPause();
        usedSensor.stop();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void resetCount() {
        prayerSpecificView.resetCount();
        changeDetector.resetCount();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.select_mode || item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }
}

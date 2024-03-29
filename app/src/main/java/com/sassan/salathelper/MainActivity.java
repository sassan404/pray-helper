package com.sassan.salathelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sassan.salathelper.sensors.LightSensor;
import com.sassan.salathelper.sensors.ProximitySensor;
import com.sassan.salathelper.sensors.SensorImplementation;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {

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

        BottomNavigationView bottomNavigationView = findViewById(R.id.main_activity_bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);

        switch (mode) {
            case MANUAL:
                prayerSpecificView.setBackground(ContextCompat.getDrawable(this, R.drawable.border));
                prayerSpecificView.setOnClickListener(view -> prayerSpecificView.updatePrayerCounter());
                break;
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

        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        // Configure the behavior of the hidden system bars.
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());

        BottomNavBarResize.resizeBottomNavBar(bottomNavigationView, getResources());

    }

    void changeApplication(float lightValue) {
        if (changeDetector.updateSensorValue(lightValue)) {
            prayerSpecificView.updatePrayerCounter();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (usedSensor != null) usedSensor.start();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (usedSensor != null) usedSensor.stop();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void resetCount() {
        prayerSpecificView.resetCount();
        changeDetector.resetCount();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.select_mode || item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.reset_button) {
            resetCount();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

}

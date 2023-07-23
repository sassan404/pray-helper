package com.sassan.salathelper;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;


public class SensorSelectionActivity extends AppCompatActivity {

    private RadioButton manualMode;
    private RadioButton lightSensorMode;
    private RadioButton proximitySensorMode;
    private Mode selectedMode;
    private TextView modeDescription;

    private Button proceedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_selection);

        PackageManager pm = this.getPackageManager();

        RadioGroup modeSelectionGroup = findViewById(R.id.radioGroupOptions);
        modeSelectionGroup.setOnCheckedChangeListener(this::onModeSelection);

        manualMode = findViewById(R.id.radioButtonManual);
        manualMode.setOnClickListener(view -> manualMode.setChecked(true));

        lightSensorMode = findViewById(R.id.radioButtonAutoLightSensor);
        if (!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_LIGHT)) {
            lightSensorMode.setEnabled(false);
            lightSensorMode.setText(R.string.light_mode_title_disabled);
        }
        lightSensorMode.setOnClickListener(view -> lightSensorMode.setChecked(true));

        proximitySensorMode = findViewById(R.id.radioButtonAutoProximitySensor);
        if (!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_PROXIMITY)) {
            proximitySensorMode.setEnabled(false);
            proximitySensorMode.setText(R.string.proximity_mode_title_disabled);
        }
        proximitySensorMode.setOnClickListener(view -> proximitySensorMode.setChecked(true));

        modeDescription = findViewById(R.id.textViewUserChoice);
        modeDescription.setMovementMethod(new ScrollingMovementMethod());

        proceedButton = findViewById(R.id.buttonProceed);
        proceedButton.setOnClickListener(view -> startMainActivity());

        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        // Configure the behavior of the hidden system bars.
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());

    }

    public void onModeSelection(RadioGroup radioGroup, int checkedId) {

        if (checkedId == R.id.radioButtonManual) {
            selectedMode = Mode.MANUAL;
            modeDescription.setText(R.string.manual_mode_description);
        } else if (checkedId == R.id.radioButtonAutoLightSensor) {
            selectedMode = Mode.LIGHT;
            modeDescription.setText(R.string.light_mode_description);
        } else if (checkedId == R.id.radioButtonAutoProximitySensor) {
            selectedMode = Mode.PROXIMITY;
            modeDescription.setText(R.string.proximity_mode_description);
        }
        proceedButton.setEnabled(true);
    }

    public void startMainActivity() {
        Intent myIntent = new Intent(SensorSelectionActivity.this, MainActivity.class);
        myIntent.putExtra("mode", selectedMode);
        SensorSelectionActivity.this.startActivity(myIntent);
    }

    public enum Mode {
        MANUAL,
        LIGHT,
        PROXIMITY
    }

}
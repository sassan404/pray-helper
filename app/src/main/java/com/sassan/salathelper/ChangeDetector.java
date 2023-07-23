package com.sassan.salathelper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

@SuppressWarnings("unused")
public class ChangeDetector extends ConstraintLayout {

    private TextView sensorValueTextView;

    private TextView modeTypeTextView;

    private LinearLayout SensorValueContainer;

    private boolean isUnderLowThreshold = false;
    private double maxSensorValue = 1;


    public ChangeDetector(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ChangeDetector(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChangeDetector(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setMode(SensorSelectionActivity.Mode mode) {
        switch (mode) {
            case MANUAL:
                modeTypeTextView.setText(R.string.manual_mode_active);
                SensorValueContainer.setVisibility(View.GONE);
                break;
            case LIGHT:
                modeTypeTextView.setText(R.string.light_sensor_mode_active);
                break;
            case PROXIMITY:
                modeTypeTextView.setText(R.string.proximity_sensor_mode_active);
                break;
        }
    }

    private double naiveLowerThreshold() {
        double for600 = 0.85;
        double for30 = 5.0 / 30.0;
        double b = (20 * for30 - for600) / 19;
        double a = (for600 - for30) / 570;
        return maxSensorValue * a + b;
    }

    private double naiveUpperThreshold() {
        double for600 = 0.90;
        double for30 = 20.0 / 30.0;
        double b = (20 * for30 - for600) / 19;
        double a = (for600 - for30) / 570;
        return maxSensorValue * a + b;
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.change_detector, this, true);
        SensorValueContainer = findViewById(R.id.sensor_number_container);
        sensorValueTextView = findViewById(R.id.sensor_value);
        modeTypeTextView = findViewById(R.id.mode_type);
        resetCount();
    }

    public boolean updateSensorValue(float lightValue) {
        sensorValueTextView.setText(String.valueOf(lightValue));

        return changeDetection(lightValue);

    }

    private boolean changeDetection(float newLightValue) {

        if (newLightValue > maxSensorValue) {
            maxSensorValue = newLightValue;
        }

        if (!isUnderLowThreshold && newLightValue < lowerThreshold()) {
            isUnderLowThreshold = true;
        }
        if (isUnderLowThreshold && newLightValue > upperThreshold()) {
            isUnderLowThreshold = false;
            return true;
        }
        return false;

    }

    private double lowerThreshold() {
        return getLowThresholdWithPolynomial();
    }

    private double upperThreshold() {
        return getHighThresholdWithPolynomial();
    }

    private double getLowThresholdWithPolynomial() {
        return -0.00000090018394541952 * maxSensorValue * maxSensorValue * maxSensorValue + 0.00181682053998599713 * maxSensorValue * maxSensorValue + 0.08244766903317213291 * maxSensorValue + 0.91573640820570290089;
    }

    private double getHighThresholdWithPolynomial() {
        return (0 - 0.00000018434209589011 * maxSensorValue * maxSensorValue * maxSensorValue + 0.00054554905775461293 * maxSensorValue * maxSensorValue + 0.63843201552572281798 * maxSensorValue + 0.36102261746418662369);
    }

    public void resetCount() {
        isUnderLowThreshold = false;
        maxSensorValue = 1;
    }


}

package com.sassan.lightsensorreader;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class LightChangeDetector extends ConstraintLayout {


    private TextView lightValueTextView;
    private TextView maxLightValueTextView;
    private TextView lowerThresholdLightValueTextView;
    private TextView higherThresholdLightValueTextView;
    private boolean startCount = false;

    private boolean isUnderLowThreshold = false;
    private double maxLightValue = 1;


    public LightChangeDetector(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public LightChangeDetector(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public LightChangeDetector(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public LightChangeDetector(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private double naiveLowerThreshold() {
        double for600 = 0.85;
        double for30 = 5.0 / 30.0;
        double b = (20 * for30 - for600) / 19;
        double a = (for600 - for30) / 570;
        return maxLightValue * a + b;
    }

    private double naiveUpperThreshold() {
        double for600 = 0.90;
        double for30 = 20.0 / 30.0;
        double b = (20 * for30 - for600) / 19;
        double a = (for600 - for30) / 570;
        return maxLightValue * a + b;
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.light_change_detector, this, true);
        lightValueTextView = findViewById(R.id.light_value);
        maxLightValueTextView = findViewById(R.id.max_light_value);
        lowerThresholdLightValueTextView = findViewById(R.id.lower_threshold_light_value);
        higherThresholdLightValueTextView = findViewById(R.id.high_threshold_light_value);
    }

    public boolean updateLightValue(float lightValue) {
        lightValueTextView.setText(String.valueOf(lightValue));
        maxLightValueTextView.setText(String.valueOf(maxLightValue));
        lowerThresholdLightValueTextView.setText(String.valueOf(lowerThreshold()));
        higherThresholdLightValueTextView.setText(String.valueOf(upperThreshold()));
        if (startCount) {
            return changeDetection(lightValue);
        }
        return false;
    }

    private boolean changeDetection(float newLightValue) {

        if (newLightValue > maxLightValue) {
            maxLightValue = newLightValue;
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
        return -0.00000090018394541952 * maxLightValue * maxLightValue * maxLightValue + 0.00181682053998599713 * maxLightValue * maxLightValue + 0.08244766903317213291 * maxLightValue + 0.91573640820570290089;
    }

    private double getHighThresholdWithPolynomial() {
        return (0 - 0.00000018434209589011 * maxLightValue * maxLightValue * maxLightValue + 0.00054554905775461293 * maxLightValue * maxLightValue + 0.63843201552572281798 * maxLightValue + 0.36102261746418662369);
    }


    public void startCount() {
        resetCount();
        startCount = true;
    }

    public void resetCount() {
        isUnderLowThreshold = false;
        maxLightValue = 1;
        startCount = false;
    }


}

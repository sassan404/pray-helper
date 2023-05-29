package com.example.lightsensorreader;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lightsensorreader.LightSensor.LightSensor;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "mainActivity";
    private TextView lightValueTextView;
    private LightSensor lightSensor;
    private TextView rukuuNumber;
    private ImageView firstArrow;
    private ImageView secondArrow;
    private Button start;
    private Button reset;
    private TextView maxLightValue;
    private TextView lowerThresholdLightValue;
    private TextView higherThresholdLightValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        rukuuNumber = findViewById(R.id.rukuu_number);

        firstArrow = findViewById(R.id.first_arrow);

        secondArrow = findViewById(R.id.second_arrow);

        lightValueTextView = findViewById(R.id.light_value);

        start = findViewById(R.id.start_button);

        reset = findViewById(R.id.reset_button);

        maxLightValue = findViewById(R.id.max_light_value);

        lowerThresholdLightValue = findViewById(R.id.lower_threshold_light_value);
        higherThresholdLightValue = findViewById(R.id.high_threshold_light_value);

        lightSensor = new LightSensor(this);
        lightSensor.setListener(this::updateLightValue);
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

    private void updateLightValue(float lightValue) {
        lightValueTextView.setText(String.valueOf(lightValue));
        if (LightChangeCount.startCount) {
            LightChangeCount.changeDetection(lightValue, rukuuNumber, firstArrow, secondArrow);
        }
        maxLightValue.setText(String.valueOf(LightChangeCount.maxLightValue));
        lowerThresholdLightValue.setText(String.valueOf(LightChangeCount.lowerThreshold()));
        higherThresholdLightValue.setText(String.valueOf(LightChangeCount.upperThreshold()));
    }

    public void startCounting(View view) {
        LightChangeCount.startCount();
        rukuuNumber.setText(String.valueOf(LightChangeCount.changeCount));
        firstArrow.setVisibility(View.INVISIBLE);
        secondArrow.setVisibility(View.INVISIBLE);
    }

    public void resetCount(View view) {
        LightChangeCount.resetCount();
        rukuuNumber.setText(String.valueOf(LightChangeCount.changeCount));
        firstArrow.setVisibility(View.INVISIBLE);
        secondArrow.setVisibility(View.INVISIBLE);
    }
}

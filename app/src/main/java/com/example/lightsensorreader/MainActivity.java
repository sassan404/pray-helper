package com.example.lightsensorreader;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lightsensorreader.LightSensor.LightSensor;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView lightValueTextView;
    private LightSensor lightSensor;

    private TextView rukuuNumber;

    private ImageView firstArrow;
    private ImageView secondArrow;

    private Button start;

    private Button reset;


    private TextView maxLightValue;

    private  TextView lowerThresholdLightValue;
    private  TextView higherThresholdLightValue;


    private static final String TAG = "mainActivity";

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
            LightChangeCount.updateChangeCount(lightValue, rukuuNumber, secondArrow);
        }
        maxLightValue.setText(String.valueOf(LightChangeCount.maxLightValue));
        lowerThresholdLightValue.setText(String.valueOf(LightChangeCount.maxLightValue*LightChangeCount.lowerThreshold()));
        higherThresholdLightValue.setText(String.valueOf(LightChangeCount.maxLightValue*LightChangeCount.upperThreshold()));
    }

    public void startCounting(View view){
        LightChangeCount.startCount();
        rukuuNumber.setText(String.valueOf(LightChangeCount.changeCount));
        secondArrow.setVisibility(View.INVISIBLE);
    }

    public void resetCount(View view){
        LightChangeCount.resetCount();
        rukuuNumber.setText(String.valueOf(LightChangeCount.changeCount));
        secondArrow.setVisibility(View.INVISIBLE);
    }
}

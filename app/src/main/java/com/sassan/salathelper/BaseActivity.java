package com.sassan.salathelper;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    public int activityCounter = 0;


    @Override
    protected void onPause() {
        super.onPause();
        activityCounter--;
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityCounter++;
    }

    protected boolean isAppInForeground() {
        return activityCounter > 0;
    }
}

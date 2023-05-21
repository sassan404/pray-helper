package com.example.lightsensorreader;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LightChangeCount {

    public static int changeCount = 1;

    public static boolean startCount = false;
    public static float maxLightValue = 1;

    public static boolean isUnderThird = false;

    public static boolean isSecondSajda = true;

    public static double lowerThreshold(){
        double for600 = 0.85;
        double for30 = 5.0/30.0;
        double b = (20*for30 - for600)/19;
        double a = (for600-for30)/570;
        return maxLightValue * a + b;
    };
    public static double upperThreshold(){
        double for600 = 0.90;
        double for30 = 20.0/30.0;
        double b = (20*for30 - for600)/19;
        double a = (for600-for30)/570;
        return maxLightValue * a + b;
    };

    public static void updateChangeCount(float newLightValue, TextView rukuuNumber, ImageView secondArrow){

        if (newLightValue>maxLightValue){maxLightValue = newLightValue;}

        if (!isUnderThird && newLightValue<maxLightValue*lowerThreshold()) {
            isUnderThird = true;
        }
        if (isUnderThird && newLightValue>maxLightValue*upperThreshold()){
            isUnderThird = false;
            updatePrayerCounter(rukuuNumber, secondArrow);
        }

    }

    public static void startCount(){
        resetCount();
        startCount = true;

    }
    static void updatePrayerCounter(TextView rukuuNumber, ImageView secondArrow) {
        if(isSecondSajda){
            isSecondSajda = false;
            changeCount++;
            rukuuNumber.setText(String.valueOf(changeCount));
            secondArrow.setVisibility(View.INVISIBLE);
        } else {
            isSecondSajda = true;
            secondArrow.setVisibility(View.VISIBLE);
        }
    }

    static void resetCount(){
        isSecondSajda = false;
        changeCount=1;
        isUnderThird = false;
        maxLightValue = 1;
        startCount = false;
    }
}

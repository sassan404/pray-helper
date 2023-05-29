package com.example.lightsensorreader;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LightChangeCount {

    private static final Handler handler = new Handler();
    public static int changeCount = 1;
    public static boolean startCount = false;
    public static double maxLightValue = 1;
    public static boolean isUnderLowThreshold = false;
    public static CountState sajdaCount = CountState.ZERO;

    private static double naiveLowerThreshold() {
        double for600 = 0.85;
        double for30 = 5.0 / 30.0;
        double b = (20 * for30 - for600) / 19;
        double a = (for600 - for30) / 570;
        return maxLightValue * a + b;
    }

    private static double naiveUpperThreshold() {
        double for600 = 0.90;
        double for30 = 20.0 / 30.0;
        double b = (20 * for30 - for600) / 19;
        double a = (for600 - for30) / 570;
        return maxLightValue * a + b;
    }

    private static double getLowThresholdWithPolynomial() {
        return -0.00000090018394541952 * maxLightValue * maxLightValue * maxLightValue + 0.00181682053998599713 * maxLightValue * maxLightValue + 0.08244766903317213291 * maxLightValue + 0.91573640820570290089;
    }

    private static double getHighThresholdWithPolynomial() {
        return (0 - 0.00000018434209589011 * maxLightValue * maxLightValue * maxLightValue + 0.00054554905775461293 * maxLightValue * maxLightValue + 0.63843201552572281798 * maxLightValue + 0.36102261746418662369);
    }

    public static double lowerThreshold() {
        return getLowThresholdWithPolynomial();
    }

    public static double upperThreshold() {
        return getHighThresholdWithPolynomial();
    }

    public static void changeDetection(float newLightValue, TextView rukuuNumber, ImageView firstArrow, ImageView secondArrow) {

        if (newLightValue > maxLightValue) {
            maxLightValue = newLightValue;
        }

        if (!isUnderLowThreshold && newLightValue < lowerThreshold()) {
            isUnderLowThreshold = true;
        }
        if (isUnderLowThreshold && newLightValue > upperThreshold()) {
            isUnderLowThreshold = false;
            updatePrayerCounter(rukuuNumber, firstArrow, secondArrow);
        }

    }

    public static void startCount() {
        resetCount();
        startCount = true;

    }

    static void updatePrayerCounter(TextView rukuuNumber, ImageView firstArrow, ImageView secondArrow) {
        Runnable updateCountRunnable = new Runnable() {
            @Override
            public void run() {
                changeCount++;
                rukuuNumber.setText(String.valueOf(changeCount));
                firstArrow.setVisibility(View.INVISIBLE);
                secondArrow.setVisibility(View.INVISIBLE);
            }
        };
        switch (sajdaCount) {
            case ZERO:
                sajdaCount = CountState.ONE;
                firstArrow.setVisibility(View.VISIBLE);
                secondArrow.setVisibility(View.INVISIBLE);
                break;
            case ONE:
                sajdaCount = CountState.TWO;
                firstArrow.setVisibility(View.VISIBLE);
                secondArrow.setVisibility(View.VISIBLE);
                sajdaCount = CountState.ZERO;
                handler.postDelayed(updateCountRunnable, 5000);
                break;
            default:
                break;
        }
    }

    static void resetCount() {
        sajdaCount = CountState.ZERO;
        changeCount = 1;
        isUnderLowThreshold = false;
        maxLightValue = 1;
        startCount = false;
    }


    private enum CountState {
        ZERO,
        ONE,
        TWO
    }
}
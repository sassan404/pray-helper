package com.example.lightsensorreader;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class PrayerSpecificView extends ConstraintLayout {

    private static final Handler handler = new Handler();
    private boolean isUpdateScheduled = false;
    private TextView rukuuNumber;
    private ImageView firstArrow;
    private ImageView secondArrow;
    private CountState sajdaCount = CountState.ZERO;
    private int changeCount = 1;

    public PrayerSpecificView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public PrayerSpecificView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public PrayerSpecificView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.prayer_specific_view, this, true);
        rukuuNumber = findViewById(R.id.rukuu_number);

        firstArrow = findViewById(R.id.first_arrow);

        secondArrow = findViewById(R.id.second_arrow);

    }

    public void startCounting() {
        resetCount();

    }

    public void resetCount() {
        changeCount = 1;
        rukuuNumber.setText(String.valueOf(changeCount));
        firstArrow.setVisibility(View.INVISIBLE);
        secondArrow.setVisibility(View.INVISIBLE);
        sajdaCount = CountState.ZERO;

    }

    public void updatePrayerCounter() {
        Runnable updateCountRunnable = () -> {
            changeCount++;
            isUpdateScheduled = false;
            sajdaCount = CountState.ZERO;
            rukuuNumber.setText(String.valueOf(changeCount));
            firstArrow.setVisibility(View.INVISIBLE);
            secondArrow.setVisibility(View.INVISIBLE);
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
                if (!isUpdateScheduled) {
                    isUpdateScheduled = true;
                    handler.postDelayed(updateCountRunnable, 20000);
                }
                break;
            default:
                break;
        }
    }

    private enum CountState {
        ZERO,
        ONE,
        TWO
    }
}
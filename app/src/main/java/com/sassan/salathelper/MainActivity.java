package com.sassan.salathelper;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.sassan.salathelper.sensors.LightSensor;
import com.sassan.salathelper.sensors.ProximitySensor;
import com.sassan.salathelper.sensors.SensorImplementation;

public class MainActivity extends BaseActivity {

    private static final int NOTIFICATION_REQUEST_CODE = 1;

    private SensorImplementation usedSensor;
    private ChangeDetector changeDetector;
    private PrayerSpecificView prayerSpecificView;
    // Connection to the NotificationListenerService
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // Service connected, you can now interact with the NotificationListenerService if needed
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            // Service disconnected
        }
    };
    private boolean isNotificationListenerBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        SensorSelectionActivity.Mode mode = (SensorSelectionActivity.Mode) intent.getSerializableExtra("mode");

        setContentView(R.layout.activity_main);

        prayerSpecificView = findViewById(R.id.prayer_specific_view);

        changeDetector = findViewById(R.id.light_change_detector);

        Button resetButton = findViewById(R.id.reset_button);

        resetButton.setOnClickListener(v -> resetCount());


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
        assert windowInsetsController != null;
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());


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
        // Unbind from the NotificationListenerService when the activity is paused
        if (isNotificationListenerBound) {
            unbindService(mConnection);
            isNotificationListenerBound = false;
        }
    }

    public void resetCount() {
        prayerSpecificView.resetCount();
        changeDetector.resetCount();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.select_mode || item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.cancel_notifications) {

            cancelAllNotifications();
        }
        return super.onOptionsItemSelected(item);
    }

    private void cancelAllNotifications() {
        // Check if the notification access is enabled for your app
        if (!isNotificationAccessEnabled()) {
            // If not enabled, open the notification access settings for the user to grant access
            openNotificationAccessSettings();
        }
        // call your NotificationListenerService to cancel all notifications
        // Send a broadcast to the NotificationListenerService to cancel all notifications
        Intent firstIntent = new Intent(NotificationListener.ACTION_CANCEL_NOTIFICATIONS);
        sendBroadcast(firstIntent);


        // Bind to the NotificationListenerService when the activity is in the foreground
        if (!isNotificationListenerBound) {
            Intent secondIntent = new Intent(this, NotificationListener.class);
            bindService(secondIntent, mConnection, Context.BIND_AUTO_CREATE);
            isNotificationListenerBound = true;
        }

    }

    private boolean isNotificationAccessEnabled() {
        return NotificationManagerCompat.getEnabledListenerPackages(getApplicationContext())
                .contains(getApplicationContext().getPackageName());
    }

    private void openNotificationAccessSettings() {
        Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        startActivityForResult(intent, NOTIFICATION_REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }
}

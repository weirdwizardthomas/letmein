package com.via.letmein;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import java.util.Objects;

public class App extends Application {

    public static final String CHANNEL_ID = "Door opening notification channel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Door opening notifications",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("This is a notification channel.");

            NotificationManager manager = getSystemService(NotificationManager.class);
            Objects.requireNonNull(manager).createNotificationChannel(notificationChannel);
        }
    }
}

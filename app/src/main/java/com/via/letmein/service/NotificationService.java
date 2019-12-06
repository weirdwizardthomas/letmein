package com.via.letmein.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;

import androidx.core.app.NotificationCompat;

import com.via.letmein.App;
import com.via.letmein.R;
import com.via.letmein.persistence.api.Api;
import com.via.letmein.persistence.api.ApiResponse;
import com.via.letmein.persistence.api.ServiceGenerator;
import com.via.letmein.persistence.api.Session;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Background service that listens to the server for updates and shows them as notifications.
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class NotificationService extends JobService {

    public static final String TAG = "NOTIFICATION THREAD";

    public static final boolean IS_RUNNING_IN_BACKGROUND = true;

    private NotificationManager notificationManager;

    Api api;

    @Override
    public boolean onStartJob(JobParameters params) {
        api = ServiceGenerator.getApi(Session.getInstance(this).getIpAddress());
        notificationManager = getSystemService(NotificationManager.class);

        doBackgroundWork(params);
        return IS_RUNNING_IN_BACKGROUND;
    }

    private void doBackgroundWork(JobParameters params) {
        Call<ApiResponse> call = api.getNotificationLog(Session.getInstance(this).getSessionId());
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse dummy = response.body();
                    if (!dummy.isError() && dummy.getContent() != null) {
                        List<String> messages = (List<String>) dummy.getContent();

                        for (int i = 0; i < messages.size(); ++i)
                            sendNotification(messages.get(i), i);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private void sendNotification(String message, int i) {
        Notification notification = buildNotification(message);
        notificationManager.notify(i, notification);

        //TODO enqueue a request to acknowledge showing the notification
    }

    private Notification buildNotification(String message) {
        return new NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setContentTitle("Somebody's at the door!")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_door_closed_lock_black_24dp)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
    }
}

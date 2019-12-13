package com.via.letmein.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.via.letmein.App;
import com.via.letmein.R;
import com.via.letmein.persistence.api.Api;
import com.via.letmein.persistence.api.ApiResponse;
import com.via.letmein.persistence.api.ServiceGenerator;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.persistence.model.LoggedAction;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.via.letmein.persistence.model.LoggedAction.DATE_FORMAT_FULL;

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
                    Gson gson = new GsonBuilder().create();

                    //Check for error & resolve
                    if (dummy.isError())
                        Log.d(TAG, "Notification request error.");
                    else {
                        TypeToken<List<LoggedAction>> responseTypeToken = new TypeToken<List<LoggedAction>>() {
                        };
                        List<LoggedAction> token = gson.fromJson(
                                gson.toJson(dummy.getContent()),
                                responseTypeToken.getType());
                        dummy.setContent(token);

                        sendNotifications((List<LoggedAction>) dummy.getContent());
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

    /**
     * Displays notifications in a bulk
     *
     * @param loggedActions Notifications to display
     */
    private void sendNotifications(List<LoggedAction> loggedActions) {
        for (LoggedAction loggedAction : loggedActions)
            sendNotification(loggedAction);
    }

    /**
     * Displays a single notification in a notification channel
     *
     * @param loggedAction Log to be shown as a notification
     */
    private void sendNotification(LoggedAction loggedAction) {
        Notification notification = buildNotification(loggedAction);
        notificationManager.notify(loggedAction.getLogID(), notification);
        markNotificationAsRead(loggedAction);
    }

    /**
     * Sends a query to the server to mark the log as read
     *
     * @param loggedAction Log to be marked as read by Admin
     */
    private void markNotificationAsRead(LoggedAction loggedAction) {
        Call<ApiResponse> call = api.markNotificationAsRead(Session.getInstance(this).getSessionId(), loggedAction.getLogID());
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse dummy = response.body();

                    //Check for error & resolve
                    if (dummy.isError())
                        Log.d(TAG, "Notification marking error");
                    else
                        Log.i(TAG, "Notification marking successful");

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    /**
     * Builds the notification based on retrieved log data
     *
     * @param loggedAction Data to be displayed
     * @return Notification based on the retrieved log
     */
    private Notification buildNotification(LoggedAction loggedAction) {
        return new NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setContentTitle(loggedAction.getTimestamp(DATE_FORMAT_FULL).toString() + "  " + loggedAction.getInfoPretty())
                .setContentText(loggedAction.getName())
                .setSmallIcon(R.drawable.ic_door_closed_lock_black_24dp)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
    }

}

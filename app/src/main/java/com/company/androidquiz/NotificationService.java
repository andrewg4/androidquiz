package com.company.androidquiz;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class NotificationService extends Service {

    public static final String RESTART_SERVICE_ACTION = "com.company.RestartNotificationService";
    public static final int COUNTDOWN_INTERVAL = 1000;
    // 5 minutes = 300000 milliseconds
    private static final int TOTAL_TIME = 300000;

    private NotificationCompat.Builder mBuilder;
    private CountDownTimer mCountDownTimer;
    private SharedPrefsManager mPrefsManager;

    public NotificationService() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        mBuilder = NotificationUtils.buildNotification(NotificationService.this);
        mPrefsManager = SharedPrefsManager.getInstance(NotificationService.this);

        long currentTimerValue = getCurrentTimerValue(TOTAL_TIME, mPrefsManager.getRemainingTime());

        mCountDownTimer = scheduleCountDownTimer(currentTimerValue);
        mCountDownTimer.start();
        return START_STICKY;
    }

    @NonNull
    private CountDownTimer scheduleCountDownTimer(final long currentTimerValue) {
        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        return new CountDownTimer(currentTimerValue, COUNTDOWN_INTERVAL) {

            public void onTick(long millisUntilFinished) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                long remainingSeconds = TimeUnit.MINUTES.toSeconds(minutes);
                String formattedTime = String.format(Locale.US, getString(R.string.format_min_sec),
                        minutes, seconds - remainingSeconds);
                mBuilder.setContentText(formattedTime);
                notificationManager.notify(NotificationUtils.NOTIFICATION_ID, mBuilder.build());
                mPrefsManager.putRemainingTime(millisUntilFinished);
            }

            public void onFinish() {
                mPrefsManager.setCompleted(true);
                mPrefsManager.putRemainingTime(0);
                notificationManager.cancel(NotificationUtils.NOTIFICATION_ID);
            }
        };
    }

    private long getCurrentTimerValue(int timerValueByDefault, long remainingTime) {
        return remainingTime != SharedPrefsManager.DEF_VALUE ? remainingTime : timerValueByDefault;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCountDownTimer.cancel();
        // we should recreate the NotificationService as many times as it requires to complete the task
        if (!mPrefsManager.isTaskCompleted()) {
            sendBroadcast(new Intent(RESTART_SERVICE_ACTION));
        } else {
            // once it completed, just clean up the SharedPreferences.
            // So, if we start the application again after completing the task,
            // notification should appear with another 5 minutes countdown
            mPrefsManager.reset();
            NotificationUtils.deleteNotificationChannel(this);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
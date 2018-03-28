package com.company.androidquiz;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextViewBuildVariant;

    private Intent mServiceLauncherIntent;
    private NotificationService mNotificationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewBuildVariant = findViewById(R.id.tv_build_variant);

        String buildVariant = BuildConfig.FLAVOR;
        mTextViewBuildVariant.setText(buildVariant);

        mNotificationService = new NotificationService();
        mServiceLauncherIntent = new Intent(this, mNotificationService.getClass());
        if (!isServiceRunning(mNotificationService.getClass())) {
            startService(mServiceLauncherIntent);
        }
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        stopService(mServiceLauncherIntent);
        super.onDestroy();
    }
}

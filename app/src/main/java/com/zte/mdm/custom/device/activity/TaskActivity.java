package com.zte.mdm.custom.device.activity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;

import com.zte.mdm.custom.device.util.AppConstants;
import com.zte.mdm.custom.device.util.StorageUtil;
import com.zte.mdm.custom.device.utils.LogUtils;

public class TaskActivity extends Activity {
    private static final String TAG = "TaskActivity";

    /* access modifiers changed from: protected */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addFlags(67108864);
        if (((Integer) StorageUtil.get(AppConstants.APP_WAKE_UP_COUNT, -1)).intValue() == 0) {
            LogUtils.info(TAG, "Cancel startApp AlarmManager");
            @SuppressLint("WrongConstant") AlarmManager alarmManager = (AlarmManager) getSystemService(NotificationCompat.CATEGORY_ALARM);
            PendingIntent activity = PendingIntent.getActivity(getApplication(), 123, new Intent(getApplication(), TaskActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            if (alarmManager != null) {
                alarmManager.cancel(activity);
            }
        }
        StorageUtil.put(AppConstants.APP_WAKE_UP_COUNT, 0);
        finish();
    }
}
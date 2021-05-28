package com.zte.mdm.custom.device.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.zte.mdm.custom.device.utils.LogUtils;

/**
 * 监听卸载
 * @author Z T
 * @data 20200924
 */
public class AppInstallReceiver extends BroadcastReceiver {
    private static final String TAG = "AppInstallReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = intent.getDataString();
            if (TextUtils.isEmpty(packageName)){
                return;
            }
            LogUtils.info(TAG, "app installed "+ packageName);
        } else if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
            LogUtils.info(TAG, "app uninstalled");
            // startAppAlarm();
        }
    }

}


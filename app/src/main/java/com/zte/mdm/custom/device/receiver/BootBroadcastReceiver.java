package com.zte.mdm.custom.device.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zte.mdm.custom.device.utils.LogUtils;

/**
 * 开机自启
 *
 * @author Z T
 * @date 20200924
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "BootBroadcastReceiver";
    static final String boot_completed = "android.intent.action.BOOT_COMPLETED";
    static final String locked_boot_completed = "android.intent.action.LOCKED_BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(boot_completed)) {
            LogUtils.info(TAG, boot_completed);
        } else if (intent.getAction().equals(locked_boot_completed)) {
            LogUtils.info(TAG, locked_boot_completed);
        }
    }
}

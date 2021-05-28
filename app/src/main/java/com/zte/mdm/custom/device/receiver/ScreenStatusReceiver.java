package com.zte.mdm.custom.device.receiver;

// UpdateReceiver

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.zte.mdm.custom.device.service.HwMdmUtil;
import com.zte.mdm.custom.device.util.StorageUtil;
import com.zte.mdm.custom.device.utils.LogUtils;

import static android.content.ContentValues.TAG;
import static com.zte.mdm.custom.device.util.AppConstants.ANDROID_INTENT_ACTION_SCREEN_OFF;
import static com.zte.mdm.custom.device.util.AppConstants.ANDROID_INTENT_ACTION_SCREEN_ON;
import static com.zte.mdm.custom.device.util.AppConstants.IS_LOCK;
import static com.zte.mdm.custom.device.util.AppConstants.LOCK;
import static com.zte.mdm.custom.device.util.AppConstants.UN_LOCK;
import static com.zte.mdm.custom.device.util.TaskUtil.startLockActivity;
import static com.zte.mdm.custom.device.util.TaskUtil.startPollAlarmReceiver;
import static com.zte.mdm.custom.device.utils.UpdateUtils.updateApp;

/**
 * 灭屏/亮屏广播
 * @author Z T
 */
public class ScreenStatusReceiver extends BroadcastReceiver {
    public static boolean mScreenPowerStatus = true;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ANDROID_INTENT_ACTION_SCREEN_ON.equals(intent.getAction())) {
            LogUtils.info(TAG, "Detect screen on and set mScreenPowerStatus false");
            mScreenPowerStatus = true;
            HwMdmUtil.replaceSim();
            startPollAlarmReceiver(true);
            //updateApp();
//            if (LOCK.equals(StorageUtil.get(IS_LOCK,UN_LOCK))){
//                startLockActivity();
//            }
        } else if (ANDROID_INTENT_ACTION_SCREEN_OFF.equals(intent.getAction())) {
            LogUtils.info(TAG, "Detect screen off and set mScreenPowerStatus ture");
            mScreenPowerStatus = false;
        }
    }
}

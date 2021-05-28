package com.zte.mdm.custom.device.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zte.mdm.custom.device.util.StorageUtil;
import com.zte.mdm.custom.device.utils.LogUtils;

import static com.zte.mdm.custom.device.util.AppConstants.IS_LOCK;
import static com.zte.mdm.custom.device.util.AppConstants.LOCK;
import static com.zte.mdm.custom.device.util.AppConstants.UN_LOCK;
import static com.zte.mdm.custom.device.util.TaskUtil.isTopActivity;
import static com.zte.mdm.custom.device.util.TaskUtil.startLockActivity;

/**
 * @author ZT
 * @date 20201110
 */
public class StartLockReceiver extends BroadcastReceiver {
    private static final String TAG = "StartLockReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        /*if (!isTopActivity()) {
            startLockReceiver();
        }*/
        LogUtils.info(TAG,(String) StorageUtil.get(IS_LOCK,UN_LOCK));
        if (!isTopActivity() && LOCK.equals(StorageUtil.get(IS_LOCK,UN_LOCK))){
            LogUtils.info(TAG,"startLockActivity");
            startLockActivity();
        }
    }
}

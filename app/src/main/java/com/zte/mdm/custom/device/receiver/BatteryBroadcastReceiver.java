package com.zte.mdm.custom.device.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static com.zte.mdm.custom.device.utils.UpdateUtils.updateApp;

/**
 * @author Z T
 * @date 20200925
 */
public class BatteryBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String acyion = intent.getAction();
            switch (acyion) {
                // 接通电源
                case Intent.ACTION_POWER_CONNECTED:
                    Log.e("zhang", "BatteryBroadcastReceiver --> onReceive--> ACTION_POWER_CONNECTED");
                    //updateApp();
                    break;
                //拔出电源
                case Intent.ACTION_POWER_DISCONNECTED:
                    Log.e("zhang", "BatteryBroadcastReceiver --> onReceive--> ACTION_POWER_DISCONNECTED");
                    //updateApp();
                    break;
                default:
            }
        }
    }
}
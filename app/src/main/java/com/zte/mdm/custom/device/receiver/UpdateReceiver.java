package com.zte.mdm.custom.device.receiver;

// UpdateReceiver

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zte.mdm.custom.device.utils.LogUtils;

/**
 * 监测是否升级、安装、卸载成功
 * @author Z T
 * @date 20200925
 */
public class UpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")) {
        }
        // 接收安装广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            String packageName = intent.getDataString();
            LogUtils.info(null, "安装了:" + packageName + "包名的程序");
        }
        //接收卸载广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            String packageName = intent.getDataString();
            LogUtils.info(null, "卸载了:" + packageName + "包名的程序");
        }
    }
}

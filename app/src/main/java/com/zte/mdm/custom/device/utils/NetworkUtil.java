package com.zte.mdm.custom.device.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static com.zte.mdm.custom.device.util.AppConstants.TYPE_MOBILE;
import static com.zte.mdm.custom.device.util.AppConstants.TYPE_NONE;
import static com.zte.mdm.custom.device.util.AppConstants.TYPE_WIFI;

public class NetworkUtil {

    /**
     * 获取网络状态
     *
     * @param context
     * @return one of TYPE_NONE, TYPE_MOBILE, TYPE_WIFI
     * @permission android.permission.ACCESS_NETWORK_STATE
     */
    public static final int getNetWorkStates(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            return TYPE_NONE;
        }

        int type = activeNetworkInfo.getType();
        switch (type) {
            //移动数据
            case ConnectivityManager.TYPE_MOBILE:
                return TYPE_MOBILE;
            //WIFI
            case ConnectivityManager.TYPE_WIFI:
                return TYPE_WIFI;
            default:
                break;
        }
        return TYPE_NONE;
    }
}
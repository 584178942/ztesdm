package com.zte.mdm.custom.device.util;

import android.content.SharedPreferences;

import static com.zte.mdm.custom.device.SGTApplication.contextApp;
import static com.zte.mdm.custom.device.util.AppConstants.COM_SGT_SECURITY;

/**
 * @author ZT
 * @data 2020/09/24
 */
public class StorageUtil {
    public static void put(String str2, Object obj) {
        SharedPreferences.Editor edit = getSharedPreferences().edit();
        if (obj instanceof String) {
            edit.putString(str2, (String) obj);
        } else if (obj instanceof Integer) {
            edit.putInt(str2, ((Integer) obj).intValue());
        } else if (obj instanceof Boolean) {
            edit.putBoolean(str2, ((Boolean) obj).booleanValue());
        } else if (obj instanceof Float) {
            edit.putFloat(str2, ((Float) obj).floatValue());
        } else if (obj instanceof Long) {
            edit.putLong(str2, ((Long) obj).longValue());
        } else if (obj != null) {
            edit.putString(str2, obj.toString());
        }
        edit.apply();
    }

    /**
     * 获取数据
     *
     * @param keyName
     * @param defaultValue 默认值
     * @return
     */
    public static Object get(String keyName, Object defaultValue) {
        SharedPreferences sp = getSharedPreferences();
        if (defaultValue instanceof String) {
            return sp.getString(keyName, (String) defaultValue);
        } else if (defaultValue instanceof Integer) {
            return sp.getInt(keyName, (Integer) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            return sp.getBoolean(keyName, (Boolean) defaultValue);
        } else if (defaultValue instanceof Float) {
            return sp.getFloat(keyName, (Float) defaultValue);
        } else if (defaultValue instanceof Long) {
            return sp.getLong(keyName, (Long) defaultValue);
        }
        return null;
    }

    public static SharedPreferences getSharedPreferences() {
        return contextApp.createDeviceProtectedStorageContext().getSharedPreferences(COM_SGT_SECURITY, 0);
    }

}

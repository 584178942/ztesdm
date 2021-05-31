package com.zte.mdm.custom.device.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.zte.mdm.custom.device.SGTApplication;
import com.zte.mdm.custom.device.activity.LockActivity;
import com.zte.mdm.custom.device.bean.InstallBean;
import com.zte.mdm.custom.device.receiver.PollAlarmReceiver;
import com.zte.mdm.custom.device.receiver.ScreenStatusReceiver;
import com.zte.mdm.custom.device.receiver.StartLockReceiver;
import com.zte.mdm.custom.device.utils.LogUtils;
import com.zte.mdm.custom.device.utils.UpdateUtil;
import com.zte.mdm.custom.device.utils.UpdateUtils;

import java.io.IOException;
import java.util.Random;

import ga.mdm.PolicyManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.Notification.CATEGORY_ALARM;
import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;
import static android.content.ContentValues.TAG;
import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.ALARM_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.zte.mdm.custom.device.SGTApplication.contextApp;
import static com.zte.mdm.custom.device.util.AppConstants.DEFAULT_INTERVAL;
import static com.zte.mdm.custom.device.util.AppConstants.FIFTH_SECOND;
import static com.zte.mdm.custom.device.util.AppConstants.FIVE_SECOND;
import static com.zte.mdm.custom.device.util.AppConstants.SPACE_SECOND;

/**
 * @author ZT
 * @data 20200924
 */
public class TaskUtil {
    private static final PolicyManager pm = PolicyManager.getInstance(contextApp);
    public static void startLockActivity() {
        try {
            Intent intent = new Intent(contextApp.getApplicationContext(), LockActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            contextApp.startActivity(intent);
        } catch (SecurityException e) {
            LogUtils.info(TAG, e.getMessage());
        } catch (ActivityNotFoundException unused) {
            LogUtils.info(TAG, "startActivity ActivityNotFoundException Error");
        }
    }



    /**
     * 安装apk
     * @param installStr 下载String
     */
    public static void installApp(String installStr){
        InstallBean installBean = new Gson().fromJson(installStr, InstallBean.class);
        if (!TextUtils.isEmpty(installBean.getApkUrl())|| !TextUtils.isEmpty(installBean.getPkgName())) {
            String pkg2 = installBean.getPkgName();
            String url2 = installBean.getApkUrl();
            LogUtils.info(TAG,"getApkUrl" + installBean.getApkUrl() + "getPkgName" + installBean.getPkgName());
            UpdateUtil.processInstall(url2,pkg2);
        }
    }
    /**
     * 下载图片
     * @param url
     * @return byte[]
     * @throws IOException
     */
    public static void changeDownloadImage(String url) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        byte[] bytes = response.body().bytes();
        // 更换桌面背景
        WallpaperManager.getInstance(SGTApplication.contextApp).setBitmap(Bytes2Bimap(bytes));

    }

    /**
     * byte[] 转 Bitmap
     * @param b
     * @return
     */
    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }
    /**
     * 启动LockActivity
     */
    public static void startLockReceiver(){
        Intent intent = new Intent();
        Context applicationContext = contextApp.getApplicationContext();
        intent.setComponent(new ComponentName(applicationContext.getPackageName(), StartLockReceiver.class.getName()));
        PendingIntent broadcast = PendingIntent.getBroadcast(applicationContext, 888, intent,  PendingIntent.FLAG_UPDATE_CURRENT);
        long countAlarmMillis = FIFTH_SECOND;
        LogUtils.info(TAG, "startAlarm intervalMillis = " + countAlarmMillis);
         @SuppressLint("WrongConstant") AlarmManager alarmManager = (AlarmManager) applicationContext.getSystemService(CATEGORY_ALARM);
        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + countAlarmMillis, broadcast);
        }
    }

    /***
     * 关闭闹钟
     */
    public static void cancelLockReceiver() {
        Intent intent = new Intent();
        Context applicationContext = contextApp.getApplicationContext();
        intent.setComponent(new ComponentName(applicationContext.getPackageName(), StartLockReceiver.class.getName()));

        @SuppressLint("WrongConstant") AlarmManager alarmManager = (AlarmManager) applicationContext.getSystemService(CATEGORY_ALARM);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(applicationContext, 888, intent,  PendingIntent.FLAG_UPDATE_CURRENT );
        alarmManager.cancel(pendingIntent);
        LogUtils.info(TAG,"cancelLockReceiver");
       /* Intent intent = new Intent();
        Context applicationContext = contextApp.getApplicationContext();
        intent.setComponent(new ComponentName(applicationContext.getPackageName(), StartLockReceiver.class.getName()));
        PendingIntent broadcast = PendingIntent.getBroadcast(applicationContext, 888, intent, 268435456);
        AlarmManager alarmManager = (AlarmManager) applicationContext.getSystemService(NotificationCompat.CATEGORY_ALARM);
        if (alarmManager != null) {
            alarmManager.cancel(broadcast);
        }*/
    }
    public static void startPollAlarmReceiver(boolean isNetWook){
        startPollAlarmReceiver(isNetWook,0,0,0,0);
    }

    /**
     *
     * @param isNetWook 是否断网
     * @param BRIGHT_SCREEN_MIN 灭屏最小时间s
     * @param BRIGHT_SCREEN_MAX 灭屏最大时间s
     * @param Dark_SCREEN_MAX 亮屏最大时间s
     * @param Dark_SCREEN_MIN 亮屏最小时间s
     */
    public static void startPollAlarmReceiver(boolean isNetWook, int BRIGHT_SCREEN_MIN, int BRIGHT_SCREEN_MAX, int Dark_SCREEN_MAX, int Dark_SCREEN_MIN) {
        Intent intent = new Intent();
        Context applicationContext = contextApp.getApplicationContext();
        intent.setComponent(new ComponentName(applicationContext.getPackageName(), PollAlarmReceiver.class.getName()));
        PendingIntent broadcast = PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) contextApp.getApplicationContext().getSystemService(ALARM_SERVICE);
        long intervalMillis;
        if (isNetWook) {
            intervalMillis = FIVE_SECOND;
        } else {
            if (ScreenStatusReceiver.mScreenPowerStatus) {
                DEFAULT_INTERVAL = new Random().nextInt(BRIGHT_SCREEN_MAX - BRIGHT_SCREEN_MIN) + BRIGHT_SCREEN_MIN;
            } else {
                if ((Dark_SCREEN_MAX - Dark_SCREEN_MIN) == 0) {
                    DEFAULT_INTERVAL = SPACE_SECOND;
                } else {
                    DEFAULT_INTERVAL = new Random().nextInt(Dark_SCREEN_MAX - Dark_SCREEN_MIN) + Dark_SCREEN_MIN;
                }
            }
            intervalMillis = DEFAULT_INTERVAL * 1000;
        }
        LogUtils.info(TAG,intervalMillis + "");

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(SystemClock.elapsedRealtime() + intervalMillis, broadcast), broadcast);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//
//            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + intervalMillis, broadcast);
//        } else {
//            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + intervalMillis, broadcast);
//
//        }  // pendingIntent 为发送广播

        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + intervalMillis, broadcast);
        }
    }
    /**
     * 当前页面是否在最上层
     * @return
     */
    public static boolean isTopActivity() {
        boolean isTop = false;
        ActivityManager am = (ActivityManager) SGTApplication.getContextApp().getApplicationContext().getSystemService(ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        LogUtils.info(TAG, "isTopActivity = " + cn.getClassName());
        if (cn.getClassName().contains(SGTApplication.getContextApp().getPackageName()+".activity.LockActivity")) {
            isTop = true;
        }
        LogUtils.info(TAG, "isTop = " + isTop);
        return isTop;
    }
    public static final String getImei() {
      String[] string =  pm.getDeviceInfo();
      return string[0];
    }
    public static String getImsiCode() {
        String[] string =  pm.getDeviceInfo();
        if (null == string[11]){
            return "";
        }
        return string[11];
    }
    public static String getImsiCode2() {
        String[] string =  pm.getDeviceInfo();
        if (null == string[12]){
            return "";
        }
        return string[12];
    }
}

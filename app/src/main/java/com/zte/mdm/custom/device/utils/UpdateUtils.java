package com.zte.mdm.custom.device.utils;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.zte.mdm.custom.device.R;
import com.zte.mdm.custom.device.SGTApplication;
import com.zte.mdm.custom.device.activity.TaskActivity;
import com.zte.mdm.custom.device.bean.UpdateBean;
import com.zte.mdm.custom.device.util.AppConstants;
import com.zte.mdm.custom.device.util.RsaUtils;
import com.zte.mdm.custom.device.util.StorageUtil;
import com.zte.mdm.custom.device.util.TaskUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

import static com.zte.mdm.custom.device.SGTApplication.contextApp;
import static com.zte.mdm.custom.device.SGTApplication.policyManager;

public class UpdateUtils {
    private final static String TAG = "UpdateUtils";
    public final static void updateApp() {
        Map<String, String> paramMap = new HashMap();
        paramMap.put("equipmentCode", TaskUtil.getImei());
        paramMap.put("oldVersion", getVerName());
        LogUtils.info(TAG, paramMap.toString());
        NetUtils netUtils = NetUtils.getInstance();
        netUtils.postDataAsynToNet(NetUtils.appUrl + "mdm/checkVersion", paramMap, new NetUtils.MyNetCall() {
            @Override
            public void success(Call call, Response response) throws IOException {
                String result = (String) response.body().string();
                try {
                    result = RsaUtils.decryptByPublicKey(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                LogUtils.info(TAG,"pu key dec" + result);
                if (!result.contains("<html")) {
                    Gson gson = new Gson();
                    UpdateBean updateBean = gson.fromJson(result, UpdateBean.class);
                    if (updateBean != null && updateBean.getData() != null) {
                        try {
                            if (shouldUpdate(updateBean.getData().getVersion())) {
                                LogUtils.info(UpdateUtils.TAG, "updateApp called, needs update");
                                processInstall(updateBean);
                            }
                        } catch (NumberFormatException unused2) {
                            LogUtils.info(UpdateUtils.TAG, "updateApp NumberFormatException");
                        }
                    }


                   /* if (updateBean.getData() != null) {
                        String code = getVerName(context);
                        LogUtils.info(TAG, code + ":" + updateBean.getData().getVersion());
                        // Log.d(TAG,Integer.parseInt(updateBean.getData().getVersion())+"");
                        LogUtils.info(TAG + ">1111", code.compareTo(updateBean.getData().getVersion()) + "");
                        if (code.compareTo(updateBean.getData().getVersion()) < 0) {
                            LogUtils.info(TAG + ">222", code.compareTo(updateBean.getData().getVersion()) + "");
                            File updateFile = downLoadFile(updateBean.getData().getUrl(), context);
                            if (fileIsExists(updateFile + "")) {
                                HwMdmUtil.installPackage(updateFile.getPath());
                            }
                        }

                    }*/

                }
            }

            @Override
            public void failed(Call call, IOException e) {

            }
        });
    }

    public static String getLocalFile() {
        File file;
        file = SGTApplication.getContextApp().getApplicationContext().getCacheDir();
        if (file == null || file.exists() || file.mkdirs()) {
            File file2 = new File(file, SGTApplication.getContextApp().getPackageName() + ".apk");
            try {
                String canonicalPath = file2.getCanonicalPath();
                if (file2.exists()) {
                    boolean delete = file2.delete();
                    LogUtils.info(TAG, "downLoadFile getLocalFile old file exists, delete it " + delete);
                }
                return canonicalPath;
            } catch (IOException unused) {
                LogUtils.info(TAG, "downLoadFile getCanonicalPath failed IOException");
                return "";
            }
        } else {
            // AppConstants.setBootInstallFlag(true);
            LogUtils.info(TAG, "downLoadFile mkdirs caught permission");
            return "";
        }
    }
    /**
     * 下载apk
     * updateBean 服务器更新数据
     * */
    public static void processInstall(final UpdateBean updateBean) {
        final String localFile = getLocalFile();
        if (TextUtils.isEmpty(localFile)) {
            LogUtils.info(TAG, "processInstall create updateFilePath failed");
        } else {
            NetUtils.getInstance().downloadFile(updateBean.getData().getUrl(), localFile, new NetUtils.MyNetCall() {
                @Override
                public void success(Call call, Response response) {
                    File file = new File(localFile);

                    InputStream inputStream = null;
                    FileOutputStream outputStream = null;
                    HttpURLConnection connection = null;
                    try {
                        URL url = new URL(updateBean.getData().getUrl());
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setConnectTimeout(10 * 1000);
                        connection.setReadTimeout(10 * 1000);
                        connection.connect();
                        inputStream = connection.getInputStream();
                        outputStream = new FileOutputStream(file);
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while ((len = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, len);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (inputStream != null) {
                                inputStream.close();
                            }
                            if (outputStream != null) {
                                outputStream.close();
                            }
                            if (connection != null) {
                                connection.disconnect();
                            }
                        } catch (IOException e) {
                            inputStream = null;
                            outputStream = null;
                        }
                    }
                    if (null != policyManager) {
                        policyManager.installPackage(file.getPath());
                        startAppAlarm();
                    }
                }

                @Override
                public void failed(Call call, IOException iOException) {
                    LogUtils.info(UpdateUtils.TAG, "downloadFile  IOException");
                }
            });
        }
    }
    private static void startAppAlarm() {
        Context applicationContext = SGTApplication.getContextApp().getApplicationContext();
        @SuppressLint("WrongConstant") AlarmManager alarmManager = (AlarmManager) applicationContext.getSystemService(NotificationCompat.CATEGORY_ALARM);
        PendingIntent activity = PendingIntent.getActivity(applicationContext, 123, new Intent(applicationContext, TaskActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 10000 + System.currentTimeMillis(), 60000, activity);
            StorageUtil.put(AppConstants.APP_WAKE_UP_COUNT, 2);
        }
    }

    /** access modifiers changed from: private */
    /*public static void callSilenceInstall(File file) {
        if (AppConstants.getEmuiVersion() < 21) {
            if (checkApkSignature(file.getPath(), SGTApplication.getContextApp().getString(R.string.sha256))) {
                LogUtils.info(TAG, "callSilenceInstall for under EMUI_10_0 ");
                // startAppAlarm();
                HwMdmUtil.installPackage(updateFile.getPath());
            }
        } else if (checkApkSignature(file.getPath(), getSha256())) {
            LogUtils.info(TAG, "callSilenceInstall for EMUI_10_0 ");
            Context applicationContext = SGTApplication.getContextApp().getApplicationContext();
            Uri uriForFile = FileProvider.getUriForFile(applicationContext, SGTApplication.getContextApp().getPackageName() + ".fileProvider", file);
            SGTApplication.getContextApp().getApplicationContext().grantUriPermission("android", uriForFile, 1);
            // startAppAlarm();
            HwMdmUtil.installPackage(updateFile.getPath());
        }
    }*/
    /*private static void startAppAlarm() {
        Context applicationContext = SGTApplication.getContextApp().getApplicationContext();
        AlarmManager alarmManager = (AlarmManager) applicationContext.getSystemService(NotificationCompat.CATEGORY_ALARM);
        PendingIntent activity = PendingIntent.getActivity(applicationContext, 123, new Intent(applicationContext, TaskActivity.class), 268435456);
        if (alarmManager != null) {
            alarmManager.setRepeating(0, 10000 + System.currentTimeMillis(), 60000, activity);
            SpUtil.put(AppConstants.FILE_NAME, AppConstants.APP_WAKE_UP_COUNT, 2);
        }
    }*/

    private static String getSha256() {
        /*if (AppConstants.getEmuiVersion() >= 25) {
            return SGTApplication.getContextApp().getString(R.string.sha256);
        }*/
        return SGTApplication.getContextApp().getString(R.string.sha256);
    }

   /* private static boolean checkApkSignature(String str, String str2) {
        Context applicationContext = SGTApplication.getContextApp().getApplicationContext();
        if (new File(str).exists()) {
            Signature[] signatureArr = applicationContext.getPackageManager().getPackageArchiveInfo(str, 64).signatures;
            int length = signatureArr.length;
            int i = 0;
            while (i < length) {
                Signature signature = signatureArr[i];
                try {
                    MessageDigest instance = MessageDigest.getInstance("SHA256");
                    instance.update(signature.toByteArray());
                    if (bytesToHexString(instance.digest()).equals(str2)) {
                        LogUtils.info(TAG, "checkApkSignature success");
                        return true;
                    }
                    i++;
                } catch (NoSuchAlgorithmException unused) {
                    LogUtils.info(TAG, "checkApkSignature caught permission");
                }
            }
        }
        return false;
    }*/

    private static String bytesToHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder(bArr.length);
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() < 2) {
                sb.append(0);
            }
            sb.append(hexString.toUpperCase(Locale.ENGLISH));
        }
        return sb.toString();
    }

    /**
     * 下载至 Environment.getExternalStorageDirectory().getPath() + "/update.apk"
     *
     * @param httpUrl
     * @return
     */
    private File downLoadFile(String httpUrl, Context context) {
        String filePath = Environment.getExternalStorageDirectory().getPath();
        if (TextUtils.isEmpty(httpUrl)) {
            throw new IllegalArgumentException();
        }
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(filePath + File.separator + "com.sgt.security.terminal_" + getVerName() + "_" + getVersionCode(context) + ".apk");

        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10 * 1000);
            connection.setReadTimeout(10 * 1000);
            connection.connect();
            inputStream = connection.getInputStream();
            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                inputStream = null;
                outputStream = null;
            }
        }
        return file;
    }


    //判断文件是否存在
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }
    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取版本号名称
     *
     * @return
     */
    public static String getVerName() {
        String verName = "";
        try {
            verName = contextApp.getPackageManager().
                    getPackageInfo(contextApp.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    public static boolean shouldUpdate(String str) {
        LogUtils.info(TAG, "version code from server = " + str);
        String[] split = str.split("\\.");
        String[] split2 = getVerName().split("\\.");
        LogUtils.info(TAG,new Gson().toJson(split) + "==" + new Gson().toJson(split2));
        if (str.equals(getVerName())){
            LogUtils.info(TAG, "version code equal ==, should not cross upgrade");
            return false;
        }
        if (split.length == 3 && split2.length == 3) {
            try {
                if (Integer.parseInt(split[0]) != Integer.parseInt(split2[0])) {
                    LogUtils.info(TAG, "version code first index not match, should not cross upgrade");
                    return false;
                } else if (Integer.parseInt(split[1]) != Integer.parseInt(split2[1])) {
                    LogUtils.info(TAG, "version code second index not match, should not cross upgrade");
                    return false;
                } else {
                    if (Integer.parseInt(split[2]) > Integer.parseInt(split2[2])) {
                        LogUtils.info(TAG, "server version is larger than local, should upgrade");
                        return true;
                    }
                    return false;
                }
            } catch (NumberFormatException unused) {
                LogUtils.info(TAG, "version code NumberFormatException");
            }
        } else {
            LogUtils.info(TAG, "version code length not right");
            return false;
        }
        return false;
    }

}





package com.zte.mdm.custom.device.util;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.format.DateUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.zte.mdm.custom.device.SGTApplication;
import com.zte.mdm.custom.device.utils.LogUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ga.mdm.PolicyManager;

import static com.zte.mdm.custom.device.SGTApplication.contextApp;
import static com.zte.mdm.custom.device.SGTApplication.policyManager;


/**
 * @author ZT
 * @date 20201027
 */
public class MdmUtil {
    private static String[] columns = {CallLog.Calls.CACHED_NAME// 通话记录的联系人
            , CallLog.Calls.NUMBER// 通话记录的电话号码
            , CallLog.Calls.DATE// 通话记录的日期
            , CallLog.Calls.DURATION// 通话时长
            , CallLog.Calls.TYPE};// 通话类型}
    private static final String TAG = "MdmUtil";
    private static final PolicyManager pm = PolicyManager.getInstance(contextApp);
   /* private static DriverImpl driverImpi = new DriverImpl();
    private static VivoOperationControl vivoOperationControl = driverImpi.getOperationManager();
    private static VivoDeviceInfoControl vivoDeviceInfoControl = driverImpi.getDeviceInfoManager();
    private static VivoApplicationControl vivoApplicationControl = driverImpi.getApplicationManager();
    private static VivoTelecomControl vivoTelecomControl = driverImpi.getTelecomManager();*/

    /**
     * 恢复出厂设置
     */
    public static void clearData() {
        try {
            Class vcs = Class.forName("com.vivo.services.cust.VivoCustomManager");
            Method clearData = vcs.getDeclaredMethod("clearData");
            clearData.invoke(vcs.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取通话时长
     */
    public static List getCallLog() {
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map;
        Cursor cursor = SGTApplication.getContextApp().getApplicationContext().getContentResolver().query(CallLog.Calls.CONTENT_URI,
                columns,  null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        while (cursor.moveToNext()) {
            map = new HashMap<>();
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));  //号码
            long dateLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)); //获取通话日期
            String callDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(dateLong));
            String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(dateLong));
            int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));//获取通话时长，值为多少秒
            int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)); //获取通话类型：1.呼入2.呼出3.未接
            LogUtils.info(TAG,"getCallLog" + startTime);
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, -1);//获取昨天时间
            Date yesterday = c.getTime();
            String newDate = new SimpleDateFormat("yyyy-MM-dd").format(yesterday);
            // LogUtils.info(TAG,callDate + ":" +  newDate);
            List<String> callDateList = Arrays.asList(callDate.split("-"));
            /*if (DateUtils.isDateOneBigger(callDate,newDate)) {
                map.put("mobile",number);
                map.put("duration",duration);
                map.put("startTime",startTime);
                map.put("type",type);
                map.put("year",callDateList.get(0));
                map.put("month",callDateList.get(1));
                map.put("day",callDateList.get(2));
                list.add(map);
                LogUtils.info(TAG,new Gson().toJson(callDateList));
            }*/
        }
        return list;
    }

    /**
     * 机卡绑定
     */
    public static void bindPhone(){
        try {
            int i = 0;
            /*vivoTelecomControl.setTelephonyPhoneState(i,i,i);
            vivoTelecomControl.setTelephonySmsState(i,i,i);
            vivoOperationControl.setBackKeyEventState(i);
            vivoOperationControl.setMenuKeyEventState(i);
            vivoOperationControl.setHomeKeyEventState(i);
            vivoOperationControl.setStatusBarState(i);
            TaskUtil.startBindActivity();*/
        } catch (Exception e) {
            LogUtils.info(TAG + "bindPhone",e.getLocalizedMessage());
        }

    }

    /**
     * 解除机卡绑定
     */
    public static void unBindPhone(){
        try {
            int i = 0;
            int k = 1;
           /* vivoTelecomControl.setTelephonyPhoneState(i,k,k);
            vivoTelecomControl.setTelephonySmsState(i,k,k);
            vivoOperationControl.setBackKeyEventState(k);
            vivoOperationControl.setMenuKeyEventState(k);
            vivoOperationControl.setHomeKeyEventState(k);
            vivoOperationControl.setStatusBarState(k);
            TaskUtil.closeBindActivity();*/
            // TaskUtil.startLockActivity();
        } catch (Exception e) {
            LogUtils.info(TAG + "unBindPhone",e.getLocalizedMessage());
        }
    }

    /**
     * 锁机
     */
    public static void lockPhone(){
        try {
            int i = 0;
            /*vivoTelecomControl.setTelephonyPhoneState(i,1,i);
            vivoTelecomControl.setTelephonySmsState(i,i,i);
            vivoOperationControl.setBackKeyEventState(i);
            vivoOperationControl.setMenuKeyEventState(i);
            vivoOperationControl.setHomeKeyEventState(i);
            vivoOperationControl.setStatusBarState(i);*/
            TaskUtil.startLockActivity();
        } catch (Exception e){
            LogUtils.info(TAG + "lockPhone",e.getLocalizedMessage());
        }
    }

    /**
     * 解锁
     */
    public static void unLockPhone(){
        try {
            int i = 1;
            /*vivoTelecomControl.setTelephonyPhoneState(0,i,i);
            vivoTelecomControl.setTelephonySmsState(0,i,i);
            vivoOperationControl.setBackKeyEventState(i);
            vivoOperationControl.setMenuKeyEventState(i);
            vivoOperationControl.setHomeKeyEventState(i);
            vivoOperationControl.setStatusBarState(i);
            TaskUtil.closeLockActivity();*/
        } catch (Exception e) {
            LogUtils.info(TAG + "unLockPhone",e.getLocalizedMessage());
        }
    }

    /**
     * 添加安装应用白名单
     * @param pkgList 包名List
     */
    public static void addInstallWhiteList(List<String> pkgList){
        try {
            //vivoApplicationControl.setInstallPattern(2);
            List<String> whiteList = new ArrayList<>();
            whiteList.add(SGTApplication.getContextApp().getPackageName());
            if (pkgList.size() >= 1) {
                for (String pkgName : pkgList) {
                    LogUtils.info("getPkgName",pkgName);
                    whiteList.add(pkgName);
                }
            } else {
                whiteList.add("com.baidu.searchbox.lite");
                /*whiteList.add("com.ss.android.article.lite");
                whiteList.add("com.ss.android.article.news");
                whiteList.add("com.ss.android.ugc.aweme");
                whiteList.add("us.zoom.videomeetings");*/
                whiteList.add("com.tencent.wework");
            }
            String[] pkgStr =  new String[whiteList.size()];
            for(int i = 0; i < whiteList.size();i++){
                pkgStr[i] = whiteList.get(i);
            }

            Boolean flag = pm.setAppInstallationPolicies(1,pkgStr);
            LogUtils.info("setAppInstallationPolicies  flag=" + flag ,"  pkgStr:" + new Gson().toJson(pkgStr));

            LogUtils.info("getAppInstallationPolicies:" ,new Gson().toJson(pm.getAppInstallationPolicies()));
        } catch (Exception e){
            LogUtils.info(TAG + "addInstallWhiteList",e.getLocalizedMessage());
        }
    }

    /**
     * 移除安装应用白名单
     */
    public static void clearInstallWhiteList(){
        try{
            /*vivoApplicationControl.setInstallPattern(0);
            vivoApplicationControl.clearInstallWhiteList();*/
            // LogUtils.info("com.siyu.mdm.custom.device",vivoApplicationControl.getInstallWhiteList().toString());
        } catch (Exception e){
            LogUtils.info(TAG + "clearInstallWhiteList",e.getLocalizedMessage());
        }
    }

    /**
     * 获取IccId
     */
    public static List<String> getPhoneIccids(){
        try{
            //return vivoDeviceInfoControl.getPhoneIccids();
        } catch (Exception e){
            LogUtils.info(TAG + "getPhoneIccids",e.getLocalizedMessage());
            return new ArrayList<>();
        }

        return null;
    }
    /**
     * 获取imei
     */
    public static String getPhoneImeis(){
        try{
            return null;
            //return vivoDeviceInfoControl.getPhoneImeis().get(0);
        } catch (Exception e){
            LogUtils.info(TAG + "getPhoneImeis",e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * 静默安装
     * @param path 安装路径
     * @param pkg 包名
     */
    public static void installPackage(final String path, String pkg){
        policyManager.installPackage(path);
        /*try{
            vivoApplicationControl.installPackageWithObserver(path,2,pkg,new CustPackageInstallObserver(){
                @Override
                public void onPackageInstalled(String basePackageName, int returnCode, String msg, Bundle extras) {
                    super.onPackageInstalled(basePackageName, returnCode, msg, extras);
                    LogUtils.info("",basePackageName + returnCode +msg);
                    TaskUtil.delete(path);
                }
            });
        } catch (Exception e){
            LogUtils.info(TAG + "installPackage",e.getLocalizedMessage());
        }*/
    }

    /**
     * 禁止卸载
     */
    public static void setUninstall(){
        try{
           /* if (vivoApplicationControl.getUninstallPattern() != UNINSTALL_PATTERN){
                vivoApplicationControl.setUninstallPattern(UNINSTALL_PATTERN);
            }
            //LogUtils.info("setUninstall",vivoApplicationControl.getUninstallPattern() + "");
            List<String> pakList = new ArrayList<>();
            pakList.add(SGTApplication.getContextApp().getPackageName());
            vivoApplicationControl.addUninstallBlackList(pakList);*/
            //LogUtils.info(TAG,vivoApplicationControl.getUninstallPattern() + "addUninstallBlackList" + vivoApplicationControl.getUninstallBlackList());
        } catch (Exception e){
            LogUtils.info(TAG + "installPackage",e.getLocalizedMessage());
        }
    }

    /**
     * 静默卸载
     * @param pkgName 包名
     */
    public static void uninstallPackage(String pkgName){
        policyManager.uninstallPackage(pkgName);
        try{
           /* LogUtils.info("deletePackage",pkgName);
            vivoApplicationControl.deletePackageWithObserver(pkgName,2,new CustPackageDeleteObserver(){
                @Override
                public void onPackageDeleted(String basePackageName, int returnCode, String msg) {
                    super.onPackageDeleted(basePackageName, returnCode, msg);
                    LogUtils.info("onPackageDeleted",basePackageName+returnCode+msg);
                }
            });*/
        } catch (Exception e){
            LogUtils.info(TAG + "installPackage",e.getLocalizedMessage());
        }
    }

    /**
     * 静默卸载 deletePackage
     * @param pkgName 包名
     */
    public static void deletePackage(String pkgName){
        try{
            LogUtils.info("deletePackage",pkgName);
          //  vivoApplicationControl.deletePackage(pkgName,1);
        } catch (Exception e){
            LogUtils.info(TAG + "installPackage",e.getLocalizedMessage());
        }
    }

}

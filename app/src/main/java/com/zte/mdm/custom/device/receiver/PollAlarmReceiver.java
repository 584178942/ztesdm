package com.zte.mdm.custom.device.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.zte.mdm.custom.device.SGTApplication;
import com.zte.mdm.custom.device.bean.DataBean;
import com.zte.mdm.custom.device.bean.RemoveBean;
import com.zte.mdm.custom.device.bean.SimBean;
import com.zte.mdm.custom.device.bean.SwichImg;
import com.zte.mdm.custom.device.service.HwMdmUtil;
import com.zte.mdm.custom.device.service.ReflexStringToMethod;
import com.zte.mdm.custom.device.util.MdmUtil;
import com.zte.mdm.custom.device.util.RsaUtils;
import com.zte.mdm.custom.device.util.StorageUtil;
import com.zte.mdm.custom.device.util.TaskUtil;
import com.zte.mdm.custom.device.utils.BdLocationUtil;
import com.zte.mdm.custom.device.utils.LogUtils;
import com.zte.mdm.custom.device.utils.NetUtils;
import com.zte.mdm.custom.device.utils.UpdateUtils;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

import static com.zte.mdm.custom.device.SGTApplication.policyManager;
import static com.zte.mdm.custom.device.util.AppConstants.BRIGHT_SCREEN_MAX;
import static com.zte.mdm.custom.device.util.AppConstants.BRIGHT_SCREEN_MIN;
import static com.zte.mdm.custom.device.util.AppConstants.Dark_SCREEN_MAX;
import static com.zte.mdm.custom.device.util.AppConstants.Dark_SCREEN_MIN;
import static com.zte.mdm.custom.device.util.AppConstants.EQUIPMENT_CODE;
import static com.zte.mdm.custom.device.util.AppConstants.INSTALL;
import static com.zte.mdm.custom.device.util.AppConstants.IS_LOCK;
import static com.zte.mdm.custom.device.util.AppConstants.LOCK;
import static com.zte.mdm.custom.device.util.AppConstants.LOCK_MSG;
import static com.zte.mdm.custom.device.util.AppConstants.PASSIVE_RECEIVE_BUS_INDEX;
import static com.zte.mdm.custom.device.util.AppConstants.REMOVE;
import static com.zte.mdm.custom.device.util.AppConstants.SIM_CODE1;
import static com.zte.mdm.custom.device.util.AppConstants.SIM_CODE2;
import static com.zte.mdm.custom.device.util.AppConstants.SWITCH_IMG;
import static com.zte.mdm.custom.device.util.AppConstants.UN_LOCK;
import static com.zte.mdm.custom.device.util.TaskUtil.startPollAlarmReceiver;
import static com.zte.mdm.custom.device.utils.NetUtils.appUrl;

/**
 * Created by 闹钟广播
 * @author Z T
 * @date 20200924
 */
public class PollAlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "PollAlarmReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        track();
        Map paramMap = new HashMap();
        paramMap.put(SIM_CODE1, TaskUtil.getImsiCode());
        paramMap.put(SIM_CODE2, TaskUtil.getImsiCode2());
        paramMap.put(EQUIPMENT_CODE, TaskUtil.getImei());
        paramMap.put("latitude", SGTApplication.getBdLocationUtil().getLatitude() + "");
        paramMap.put("lontitude", SGTApplication.getBdLocationUtil().getLontitude() + "");
        passiveReceiveBus(context, paramMap, PASSIVE_RECEIVE_BUS_INDEX);
    }

    /**
     * @param context
     * @param params
     * @param url
     */
    public void passiveReceiveBus(final Context context, Map<String,String> params, String url) {
        NetUtils netUtils = NetUtils.getInstance();
        netUtils.postDataAsynToNet(appUrl + url, params, new NetUtils.MyNetCall() {
            @Override
            public void success(okhttp3.Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    result = RsaUtils.decryptByPublicKey(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                LogUtils.info(TAG,result);
                if (result.contains("<html")) {
                    startPollAlarmReceiver(false);
                } else {
                    SimBean simBean = new Gson().fromJson(result, SimBean.class);
                    if (simBean == null) {
                        return;
                    }
                    BRIGHT_SCREEN_MIN = simBean.getMin();
                    BRIGHT_SCREEN_MAX = simBean.getMax();
                    Dark_SCREEN_MAX = simBean.getInterestScreenmax();
                    Dark_SCREEN_MIN = simBean.getInterestScreenmin();
                    startPollAlarmReceiver(false,BRIGHT_SCREEN_MIN,BRIGHT_SCREEN_MAX,Dark_SCREEN_MAX,Dark_SCREEN_MIN);
                    ReflexStringToMethod reflexStringToMethod = new ReflexStringToMethod();
                    boolean flag = true;
                    boolean islock = true;
                    String operationType  = (String) simBean.getOperationType();
                    if (simBean.getData() == null){
                        return;
                    }
                    if (operationType != null){
                        switch (operationType) {
                            case INSTALL:
                                TaskUtil.installApp(simBean.getData().toString());
                                break;
                            case REMOVE:
                                RemoveBean removeBean = new Gson().fromJson((String) simBean.getData(), RemoveBean.class);
                                if (!TextUtils.isEmpty(removeBean.getPkgName())) {
                                    String pkgName = removeBean.getPkgName();
                                    MdmUtil.uninstallPackage(pkgName);
                                }
                                break;
                            case SWITCH_IMG:
                                SwichImg swichImg = new Gson().fromJson(simBean.getData().toString(), SwichImg.class);
                                if (!TextUtils.isEmpty(swichImg.getImageUrl())){
                                    String url = swichImg.getImageUrl();
                                    TaskUtil.changeDownloadImage(url);
                                }
                                break;
                           /* case ADD_WHITE:
                                if (!"".equals(bean.getData())){
                                    White white = new Gson().fromJson(bean.getData(), White.class);
                                    List<String> whiteList = Arrays.asList(white.getPkgNames().split(","));
                                    MdmUtil.addInstallWhiteList(whiteList);
                                }
                                break;
                            case REMOVE_WHITE:
                                MdmUtil.clearInstallWhiteList();
                                break;*/
                            default:
                                LogUtils.info("default",result);
                                break;
                        }
                    } else {
                        LogUtils.info("data",new Gson().toJson(simBean.getData()));
                        String data = new Gson().toJson(simBean.getData());
                        List<SimBean.DataBean> dataBeanList = new Gson().fromJson(data, new TypeToken<List<SimBean.DataBean>>(){}.getType());
                        if (simBean.getData() != null) {
                            for (SimBean.DataBean datum : dataBeanList) {
                                if (datum.getMethodName().equals(LOCK)) {
                                    StorageUtil.put(LOCK_MSG,simBean.getMessage());
                                    StorageUtil.put(IS_LOCK,LOCK);
                                    TaskUtil.startLockActivity();
                                } else if (datum.getMethodName().equals(UN_LOCK)) {
                                    StorageUtil.put(IS_LOCK,UN_LOCK);
                                    HwMdmUtil.finishActivity();
                                } else {
                                    flag = (boolean) reflexStringToMethod.reflexToMethod(datum.getPkgAddress(), datum.getClassAddress(), datum.getMethodName(), datum, context);
                                }
                            }
                        }
                    }
                    callback(simBean.getDraw(), flag + "", islock);

                }
            }
            @Override
            public void failed(okhttp3.Call call, IOException e) {
                LogUtils.info(TAG,e.getLocalizedMessage());
                startPollAlarmReceiver(true);
            }
        });
    }
    /**
     * 轨迹
     */
    private void track(){
        Map paramMap = new HashMap();
        paramMap.put("imeiCode",TaskUtil.getImei());
        paramMap.put("iccId",TaskUtil.getImsiCode());
        paramMap.put("version", UpdateUtils.getVerName());
        paramMap.put("latitude", SGTApplication.getBdLocationUtil().getLatitude()+"");
        paramMap.put("longitude", SGTApplication.getBdLocationUtil().getLontitude()+"");
        if ("4.9E-324".equals(SGTApplication.getBdLocationUtil().getLatitude()+"")||"4.9E-324".equals(SGTApplication.getBdLocationUtil().getLontitude()+"")){
            return;
        }
        NetUtils netUtils = NetUtils.getInstance();
        // String appUrl = "http://192.168.2.144:9999/api/track";
        netUtils.postDataAsynToNet(appUrl+"track", paramMap, new NetUtils.MyNetCall() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void success(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtils.info("CALL_RECORDS",result);
            }
            @Override
            public void failed(Call call, IOException e) {
                LogUtils.info("failed", e.getLocalizedMessage());
            }
        });
    }

    //回调
    public void callback(String draw, String callback, final boolean islock) {
        if (callback.equals("true")) {
            callback = "sendsucc";
        } else {
            callback = "senderror";
        }
        Map<String, String> paramMap = new HashMap();
        paramMap.put("equipmentCode", TaskUtil.getImei());
        paramMap.put("commandId", draw);
        paramMap.put("callback", callback);
        NetUtils netUtils = NetUtils.getInstance();
        netUtils.postDataAsynToNet(appUrl + "callback/index", paramMap, new NetUtils.MyNetCall() {
            @Override
            public void success(okhttp3.Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    result = RsaUtils.decryptByPublicKey(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (result.contains("<html")) {
                    startPollAlarmReceiver(true);
                }
                if (islock) {
                    HwMdmUtil.replaceSim();
                }
            }

            @Override
            public void failed(okhttp3.Call call, IOException e) {
                startPollAlarmReceiver(true);
            }
        });
    }

}


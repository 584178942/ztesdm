package com.zte.mdm.custom.device.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.google.gson.Gson;
import com.zte.mdm.custom.device.bean.SimBean;
import com.zte.mdm.custom.device.service.HwMdmUtil;
import com.zte.mdm.custom.device.service.ReflexStringToMethod;
import com.zte.mdm.custom.device.util.RsaUtils;
import com.zte.mdm.custom.device.util.StorageUtil;
import com.zte.mdm.custom.device.util.TaskUtil;
import com.zte.mdm.custom.device.utils.LogUtils;
import com.zte.mdm.custom.device.utils.NetUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Response;

import static com.zte.mdm.custom.device.SGTApplication.policyManager;
import static com.zte.mdm.custom.device.util.AppConstants.BRIGHT_SCREEN_MAX;
import static com.zte.mdm.custom.device.util.AppConstants.BRIGHT_SCREEN_MIN;
import static com.zte.mdm.custom.device.util.AppConstants.Dark_SCREEN_MAX;
import static com.zte.mdm.custom.device.util.AppConstants.Dark_SCREEN_MIN;
import static com.zte.mdm.custom.device.util.AppConstants.EQUIPMENT_CODE;
import static com.zte.mdm.custom.device.util.AppConstants.IS_LOCK;
import static com.zte.mdm.custom.device.util.AppConstants.LOCK;
import static com.zte.mdm.custom.device.util.AppConstants.LOCK_MSG;
import static com.zte.mdm.custom.device.util.AppConstants.PASSIVE_RECEIVE_BUS_INDEX;
import static com.zte.mdm.custom.device.util.AppConstants.SIM_CODE1;
import static com.zte.mdm.custom.device.util.AppConstants.SIM_CODE2;
import static com.zte.mdm.custom.device.util.AppConstants.UN_LOCK;
import static com.zte.mdm.custom.device.util.TaskUtil.startPollAlarmReceiver;

/**
 * Created by 闹钟广播
 * @author Z T
 * @date 20200924
 */
public class PollAlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "PollAlarmReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Map paramMap = new HashMap();
        paramMap.put(SIM_CODE1, TaskUtil.getImsiCode());
        paramMap.put(SIM_CODE2, TaskUtil.getImsiCode2());
        paramMap.put(EQUIPMENT_CODE, TaskUtil.getImei());
        passiveReceiveBus(context, paramMap, PASSIVE_RECEIVE_BUS_INDEX);
    }

    /**
     * @param context
     * @param params
     * @param url
     */
    public void passiveReceiveBus(final Context context, Map<String,String> params, String url) {
        NetUtils netUtils = NetUtils.getInstance();
        netUtils.postDataAsynToNet(NetUtils.appUrl + url, params, new NetUtils.MyNetCall() {
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
                    BRIGHT_SCREEN_MIN = simBean.getMin();
                    BRIGHT_SCREEN_MAX = simBean.getMax();
                    Dark_SCREEN_MAX = simBean.getInterestScreenmax();
                    Dark_SCREEN_MIN = simBean.getInterestScreenmin();
                    startPollAlarmReceiver(false,BRIGHT_SCREEN_MIN,BRIGHT_SCREEN_MAX,Dark_SCREEN_MAX,Dark_SCREEN_MIN);
                    ReflexStringToMethod reflexStringToMethod = new ReflexStringToMethod();
                    boolean flag = true;
                    boolean islock = true;
                    if (simBean.getData() != null) {
                        callback(simBean.getDraw(), flag + "", islock);
                        for (SimBean.DataBean datum : simBean.getData()) {
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
            }
            @Override
            public void failed(okhttp3.Call call, IOException e) {
                LogUtils.info(TAG,e.getLocalizedMessage());
                startPollAlarmReceiver(true);
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
        netUtils.postDataAsynToNet(NetUtils.appUrl + "callback/index", paramMap, new NetUtils.MyNetCall() {
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


package com.zte.mdm.custom.device.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.zte.mdm.custom.device.service.HwMdmUtil;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * 监听sim状态改变的广播，返回sim卡的状态， 有效或者无效。
 * 双卡中只要有一张卡的状态有效即返回状态为有效，两张卡都无效则返回无效。
 *
 * @author ZT
 * @date 20200925
 */
public class SimStateReceiver extends BroadcastReceiver {
    private final static String ACTION_SIM_STATE_CHANGED = "android.intent.action.SIM_STATE_CHANGED";
    @Override
    public void onReceive(Context context, Intent intent) {

        final String stateExtra = intent.getStringExtra(IccCardConstants.INTENT_KEY_ICC_STATE);
        if (intent.getAction().equals(ACTION_SIM_STATE_CHANGED)) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            int state = tm.getSimState();
            switch (state) {
                case TelephonyManager.SIM_STATE_READY:
                    if (IccCardConstants.INTENT_VALUE_ICC_LOADED.equals(stateExtra)) {
                        try {
                            HwMdmUtil.replaceSim();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case TelephonyManager.SIM_STATE_UNKNOWN:
                case TelephonyManager.SIM_STATE_ABSENT:
                case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                default:
                    break;
            }
        }
    }
    /**
     * @author Z T
     * @date 20200925
     */
    public class IccCardConstants {
        /** The extra data for broacasting intent INTENT_ICC_STATE_CHANGE */
        public static final String INTENT_KEY_ICC_STATE = "ss";
        /** LOADED means all ICC records, including IMSI, are loaded */
        public static final String INTENT_VALUE_ICC_LOADED = "LOADED";
    }

}

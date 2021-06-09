package com.zte.mdm.custom.device.util;

/**
 * @author ZT
 * @data 2020/09/24
 */
public class AppConstants {

    public static final String APP_WAKE_UP_COUNT = "appWakeUpCount";

    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;


    /**
     * 安装
     */
    public static final String INSTALL = "install";

    /**
     * 卸载
     */
    public static final String REMOVE = "remove";

    /**
     * 锁机
     */
    public static final String SWITCH_IMG = "switchImg";

    /**
     * 添加白名单
     */
    public static final String ADD_WHITE = "addWhite";

    /**
     * 添加白名单
     */
    public static final String REMOVE_WHITE = "removeWhite";

    /**
     * 5秒
     */
    public static final int FIVE_SECOND = 5000;

    /**
     * 15秒
     */
    public static final int FIFTH_SECOND = 15000;

    /**
     * 20秒
     */
    public static final int TWO_SECOND = 20000;


    public static final String IS_LOCK = "is_lock";
    /**
     * 锁屏文字
     */
    public static final String LOCK_MSG = "LOCK_MSG";

    /**
     * 存储文件name
     */
    public static final String COM_SGT_SECURITY = "com_sgt_security";

    /**
     * 默认锁屏文字
     */
    public static final String DEFAULT_LOCK_MSG = "终端强制锁定，如需解锁请与管理员联系";

    /**
     * 灭屏5分钟
     */
    public static final int SPACE_SECOND = 18000;

    public static final String CONTRACT_COMPLETED_ACTION = "CONTRACT_COMPLETED_ACTION";

    public static final String IMSICODE = "imsiCode";

    public static final String IMSICODE2 = "imsiCode2";

    public static final int TYPE_NONE = -1;

    public static final int TYPE_MOBILE = 0;

    public static final int TYPE_WIFI = 1;

    public static final String ANDROID_INTENT_ACTION_SCREEN_ON = "android.intent.action.SCREEN_ON";

    public static final String ANDROID_INTENT_ACTION_SCREEN_OFF = "android.intent.action.SCREEN_OFF";

    /**
     * 亮屏默认最小时间间隔
     */
    public static int BRIGHT_SCREEN_MIN = 30;

    /**
     * 亮屏默认最大时间间隔
     */
    public static int BRIGHT_SCREEN_MAX = 60;

    /**
     * 暗屏默认最小时间间隔
     */
    public static int Dark_SCREEN_MIN = 5;

    /**
     * 暗屏默认最大时间间隔
     */
    public static int Dark_SCREEN_MAX = 10;

    /**
     * 默认间隔
     */
    public static int DEFAULT_INTERVAL = 60;

    /**
     * sim1Code
     */
    public static String SIM_CODE1 = "sim1Code";

    /**
     * sim2Code
     */
    public static String SIM_CODE2 = "sim2Code";

    /**
     * EQUIPMENT_CODE
     */
    public static String EQUIPMENT_CODE = "equipmentCode";

    /**
     * EQUIPMENT_CODE
     */
    public static String PASSIVE_RECEIVE_BUS_INDEX = "passiveReceiveBus/index";
    public static String ACTIVE_RECEIVE_BUS_INDEX = "activeReceiveBus/index";
    public static String CALLBACK_INDEX= "callback/index";
    public static int MODE_0 = 0;
    public static int MODE_1 = 1;
    public static int MODE_2 = 2;
    public static int MODE_3 = 3;
    public static int MODE_4 = 4 ;

    /**
     * 锁机
     */
    public static String LOCK = "lock";
    /**
     * 解锁
     */
    public static String UN_LOCK = "unlock";

    /**
     * 多参类型 int string
     */
    public final static String INT_STRING = "intString";
    /**
     * 多参类型 int int
     */
    public final static String INT_INT = "intInt";

    public final static String LOCK_MSG_STR = "终端强制锁定，如需解锁请与管理员联系";


}

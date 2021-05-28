package com.zte.mdm.custom.device.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.zte.mdm.custom.device.R;
import com.zte.mdm.custom.device.util.StorageUtil;
import com.zte.mdm.custom.device.utils.LogUtils;

import static com.zte.mdm.custom.device.SGTApplication.policyManager;
import static com.zte.mdm.custom.device.util.AppConstants.DEFAULT_LOCK_MSG;
import static com.zte.mdm.custom.device.util.AppConstants.FLAG_HOMEKEY_DISPATCHED;
import static com.zte.mdm.custom.device.util.AppConstants.IS_LOCK;
import static com.zte.mdm.custom.device.util.AppConstants.LOCK_MSG;
import static com.zte.mdm.custom.device.util.AppConstants.LOCK_MSG_STR;
import static com.zte.mdm.custom.device.util.AppConstants.MODE_1;
import static com.zte.mdm.custom.device.util.AppConstants.UN_LOCK;
import static com.zte.mdm.custom.device.util.TaskUtil.isTopActivity;
import static com.zte.mdm.custom.device.util.TaskUtil.startLockReceiver;


/**
 * @author Z T
 * @data 20201015
 */
public class LockActivity extends Activity {
    private static final String TAG = "LockActivity";
    public static LockActivity lockActivity;
    private String lockMsg = DEFAULT_LOCK_MSG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_lock);
        TextView lockTV = findViewById(R.id.lock_TV);
        lockMsg = (String) StorageUtil.get(LOCK_MSG,LOCK_MSG_STR);
        lockTV.setText(lockMsg);
        lockActivity = this;
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                policyManager.setVoicePolicies(MODE_1);
                policyManager.setSmsPolicies(MODE_1,"");
                policyManager.setCaptureScreenPolicies(MODE_1);
                policyManager.zsdkSetStatusBarStatus(MODE_1);
                policyManager.zdevpForbiddenHome(MODE_1);
                policyManager.zsdkSetNavigationBarStatus(MODE_1);
                StorageUtil.put(IS_LOCK,UN_LOCK);
            }
        });
    }
    public static LockActivity getLockActivity() {
        return lockActivity;
    }
    /**
     * 屏蔽返回键的代码:
     * */
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        switch(keyCode){
            case KeyEvent.KEYCODE_HOME:
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_CALL:
            case KeyEvent.KEYCODE_SYM:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_STAR:
                return true;
            default:
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onPause() {
        super.onPause();
        startLockReceiver();
//        if (!isTopActivity()) {
//            startLockReceiver();
//        }
    }
    @Override
    public void onAttachedToWindow() {
        this.getWindow().addFlags(FLAG_HOMEKEY_DISPATCHED);
        super.onAttachedToWindow();
    }

}
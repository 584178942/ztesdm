package com.zte.mdm.custom.device.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.zte.mdm.custom.device.R;
import com.zte.mdm.custom.device.util.MdmUtil;
import com.zte.mdm.custom.device.util.StorageUtil;
import com.zte.mdm.custom.device.util.TaskUtil;
import com.zte.mdm.custom.device.utils.LogUtils;
import com.zte.mdm.custom.device.utils.UpdateUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ga.mdm.PolicyManager;

import static com.zte.mdm.custom.device.SGTApplication.policyManager;
import static com.zte.mdm.custom.device.util.AppConstants.IS_LOCK;
import static com.zte.mdm.custom.device.util.AppConstants.MODE_0;
import static com.zte.mdm.custom.device.util.AppConstants.MODE_1;
import static com.zte.mdm.custom.device.util.AppConstants.MODE_2;
import static com.zte.mdm.custom.device.util.AppConstants.UN_LOCK;
import static com.zte.mdm.custom.device.utils.UpdateUtils.fileIsExists;

/**
 * @author Z T
 * @date 20200924
 */
public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    public static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != policyManager) {

                    boolean isLock = policyManager.lockDevice("你好，已锁屏！");
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            try {
                                TaskUtil.changeDownloadImage("https://gimg2.baidu.com/image_search/src=http%3A%2F%2F1812.img.pp.sohu.com.cn%2Fimages%2Fblog%2F2009%2F11%2F18%2F18%2F8%2F125b6560a6ag214.jpg&refer=http%3A%2F%2F1812.img.pp.sohu.com.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1624698251&t=085ca3d7dbf5ca30b724da7b9f1f633b");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                      LogUtils.info("ISLOCK",isLock + "");
//                    policyManager.setSmsPolicies(MODE_0,null); 
//                    policyManager.setCaptureScreenPolicies(MODE_0);
//                    policyManager.zsdkSetStatusBarStatus(MODE_0);
//                    policyManager.zdevpForbiddenHome(MODE_2);
//                    policyManager.zsdkSetNavigationBarStatus(MODE_1);

                }
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != policyManager) {
                    boolean isLock = policyManager.unlockDevice();
                    LogUtils.info("unlockDevice",isLock + "");
                    /*LogUtils.info( TAG,policyManager.setVoicePolicies(MODE_1)+"");
                    policyManager.setVoicePolicies(MODE_1);
                    policyManager.setSmsPolicies(MODE_1,"");
                    policyManager.setCaptureScreenPolicies(MODE_1);
                    policyManager.zsdkSetStatusBarStatus(MODE_1);
                    policyManager.zdevpForbiddenHome(MODE_1);
                    policyManager.zsdkSetNavigationBarStatus(MODE_1);
                    boolean is = policyManager.setDataConnectivityPolicies(3);
                    StorageUtil.put(IS_LOCK,UN_LOCK);
                    LogUtils.info(TAG,"" + is);*/
                }
            }
        });

        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != policyManager) {
                    LogUtils.info( TAG, "btn3");
                    String pkgName = "com.baidu.searchbox.lite";
                    MdmUtil.uninstallPackage(pkgName);
                    // policyManager.rebootDevice();
                    // policyManager.zsdkResetFactoryDataEx(0,0x02);
                }
            }
        });

        findViewById(R.id.btn4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != policyManager) {
                    String apkUrl = "http://gdown.baidu.com/data/wisegame/4fb53174d106c3a6/04d44fb53174d106c3a6af535c969fe2.apk";
                    String pkgName = "com.baidu.searchbox.lite";
                    UpdateUtil.processInstall(apkUrl,pkgName);
                    /*String path = "/sdcard/app-release.apk";
                    LogUtils.info(TAG, "btn3" + fileIsExists(path));
                    if (fileIsExists(path)){
                        policyManager.installPackage(path);
                    }*/
                }
            }
        });

        findViewById(R.id.btn5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != policyManager) {
                    MdmUtil.addInstallWhiteList(new ArrayList<>());
                }
            }
        });

    }
    public static MainActivity getMainActivity() {
        return mainActivity;
    }
}

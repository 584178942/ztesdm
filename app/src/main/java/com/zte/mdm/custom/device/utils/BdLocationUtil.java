package com.zte.mdm.custom.device.utils;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.zte.mdm.custom.device.SGTApplication;
import com.zte.mdm.custom.device.bean.BdLocationVo;


/**
 * Created by zhoutao on 15/1/13.
 */
public class BdLocationUtil {
    private  static Context thisContext;
    private static BdLocationUtil mInstance;
    private LocationClient mLocationClient;
    private MyLocationListener mMyLocationListener;
    private static BdLocationVo bdLocationVo = new BdLocationVo();
    private BdLocationUtil(Context context) {
        thisContext = context;
        initGps();
    }

    public static synchronized BdLocationUtil getInstance(Context context){
        if (mInstance == null) {
            mInstance = new BdLocationUtil(context);
        }
        return mInstance;

    }

    /**
     * 初始化Gps
     */
    private void initGps(){
        mLocationClient = new LocationClient(thisContext);
        mMyLocationListener = new MyLocationListener();
        mLocationClient.setLocOption(getOption());
        mLocationClient.registerLocationListener(mMyLocationListener);
        mLocationClient.start();
    }


    //定位参数配置
    private LocationClientOption getOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setScanSpan(10000);
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setNeedDeviceDirect(true);
        option.setIgnoreKillProcess(false);
        return option;
    }

    /**
     * 获得定位结果
     * @return
     */
    public BdLocationVo getLocation(){
       if (mLocationClient != null && mLocationClient.isStarted()){
           startLocation();
       }
        return  bdLocationVo;
    }



    /**
     * 循环请求定位，定位成功
     */
    private void startLocation(){
        mLocationClient = new LocationClient(SGTApplication.contextApp);
        //声明LocationClient类
        mLocationClient.registerLocationListener(mMyLocationListener);
        int flag= mLocationClient.requestLocation();
      //  while (flag!=0){
          //  flag=mLocationClient.requestLocation();
            switch (flag){
                case 0:
                    LogUtils.info("gps", "绑定服务成功");
                    break;
                case 1:
                    LogUtils.info("gps","service没有启动");
                    break;
                case 2:
                    LogUtils.info("gps","无监听函数");
                    break;
                case 6:
                    LogUtils.info("gps","两次请求时间太短");
                    break;
                default:
                    break;
            }

        //}
    };


    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null){
                return;
            }

           // LogUtils.info("gpsInfo",new Gson().toJson(bdLocation));
            bdLocationVo.setTime(bdLocation.getTime());
            bdLocationVo.setCity(bdLocation.getCity());
            bdLocationVo.setLatitude(bdLocation.getLatitude());
            bdLocationVo.setLontitude(bdLocation.getLongitude());
            bdLocationVo.setAddr(bdLocation.getAddrStr());
        }

    }

}

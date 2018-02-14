package com.tjstudy.wifidemo;

import android.Manifest;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View;

import com.tjstudy.wifidemo.base.BaseActivity;
import com.tjstudy.wifidemo.base.OnPermissionCallbackListener;
import com.tjstudy.wifidemo.utils.WifiUtil;

import java.util.List;

/**
 * 切换到指定wifi
 */
public class MainActivity extends BaseActivity {


    private void initPermission() {
        onRequestPermission(new String[]{
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,

        }, new OnPermissionCallbackListener() {
            @Override
            public void onGranted() {
            }

            @Override
            public void onDenied(List<String> deniedPermissions) {
            }
        });
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        initPermission();

        findViewById(R.id.btn_change_wifi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeWifi2();
            }
        });
    }


    private String aimWifiName = "your wifi name";
    private String aimWifiPwd = "wifi pwd";

    private void changeWifi2() {
        WifiUtil.getIns().init(getApplicationContext());
        WifiUtil.getIns().changeToWifi(aimWifiName,aimWifiPwd);

    }

    @Override
    public void installData() {

    }


    /**
     * 切换wifi
     */
    private void changeWifi() {
////        WifiConfiguration wifiNewConfiguration = createWifiInfo(aimWifiName, aimWifiPwd);//使用wpa2的wifi加密方式
////        int newNetworkId = wifiManager.addNetwork(wifiNewConfiguration);
////        Log.e("MainActivity", "newNetworkId:" + newNetworkId);
////        /**
////         * addNetwork打印结果:
////         * 1、未保存密码             ==32
////         * 2、在代码里面保存了密码     ==32--意思是在代码里面add了 没有remove的情况
////         * 3、手动连接了密码          ==-1
////         */
////        boolean enableNetwork = wifiManager.enableNetwork(newNetworkId, true);
////        Log.e("MainActivity", "enableNetwork:" + enableNetwork);
////        /**
////         * enableNetwork打印结果:
////         * 1、未保存密码             切换成功
////         * 2、在代码里面保存了密码     切换成功
////         * 3、手动连接了密码          app无响应了
////         */
//
//        //最终解决方案
//        WifiConfiguration wifiNewConfiguration = createWifiInfo(aimWifiName, aimWifiPwd);//使用wpa2的wifi加密方式
//        int newNetworkId = mWifiManager.addNetwork(wifiNewConfiguration);
//        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
//
//        List wifiList = mWifiManager.getConfiguredNetworks();
//        for (int i = 0; i < wifiList.size(); ++i) {
//            WifiConfiguration wifiInfo0 = (WifiConfiguration) wifiList.get(i);
//            Log.i("MainActivity", "------------------- i =  " + i);
////            Log.i("MainActivity", "wifiInfo = " + wifiInfo0.toString());
//            Log.i("MainActivity", "wifiInfo.getNetworkId() = " + wifiInfo0.networkId);
//            Log.i("MainActivity", "wifiInfo.ssid 1 = " + wifiInfo0.SSID);
//
////            Log.i("MainActivity", "wifiInfo0.providerFriendlyName = " + wifiInfo0.providerFriendlyName);
//
//
////
//            if ("\"CMB-OA\"".equals(wifiInfo0.SSID)) {
//                Log.i("MainActivity", "leeTest ------> set wifi 1 = " + wifiInfo0.SSID);
//                enableWifi(wifiInfo0.networkId);
//                Log.i("MainActivity", "leeTest ------> set wifi 2 = " + wifiInfo0.SSID);
//                return;
//            }
//            Log.i("MainActivity", "wifiInfo.ssid 2 = " + wifiInfo0.SSID);
//        }
//
//        wifiList = mWifiManager.getScanResults();
//        for (int i = 0; i < wifiList.size(); ++i) {
//            ScanResult wifiInfo0 = (ScanResult) wifiList.get(i);
////            Log.i("MainActivity", "------------------- j =  " + i);
////            Log.i("MainActivity", "wifiInfo.getSSID() = " + wifiInfo0.toString() );
//
//
////
////            if("CMD-OA".equals(wifiInfo0.BSSID)){
////                enableWifi(wifiInfo0.getNetworkId());
////                return;
////            }
//        }
//
//        wifiInfo.getNetworkId();
//        wifiInfo.getSSID();
//        Log.i("MainActivity", "wifiInfo.getNetworkId() = " + wifiInfo.getNetworkId());
//        Log.i("MainActivity", "wifiInfo.getSSID() = " + wifiInfo.getSSID());
//
//
//        if (newNetworkId == -1) {
//            Log.e("MainActivity", "操作失败,需要您到手机wifi列表中取消对设备连接的保存");
//        } else {
//            enableWifi(newNetworkId);
//        }
    }




}

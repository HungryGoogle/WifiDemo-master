package com.tjstudy.wifidemo.utils;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by li on 2018/2/14.
 */

public class WifiUtil {

    /**
     * 切换到指定wifi
     * @param wifiName  指定的wifi名字
     * @param wifiPwd   wifi密码，如果已经保存过密码，可以传入null
     * @return
     */
    public boolean changeToWifi(String wifiName, String wifiPwd){
        if(mWifiManager == null){
            Log.i(TAG, " ***** init first ***** ");
            return false;
        }

        String __wifiName__ = "\"" + wifiName + "\"";
        printCurWifiInfo();

        List wifiList = mWifiManager.getConfiguredNetworks();
        boolean bFindInList = false;
        for (int i = 0; i < wifiList.size(); ++i) {
            WifiConfiguration wifiInfo0 = (WifiConfiguration) wifiList.get(i);

            // 先找到对应的wifi
            if (__wifiName__.equals(wifiInfo0.SSID) || wifiName.equals(wifiInfo0.SSID)) {
                // 1、 先启动，可能已经输入过密码，可以直接启动
                Log.i(TAG, " set wifi 1 = " + wifiInfo0.SSID);
                return doChange2Wifi(wifiInfo0.networkId);
            }

        }

        // 2、如果wifi还没有输入过密码，尝试输入密码，启动wifi
        if(!bFindInList){
            WifiConfiguration wifiNewConfiguration = createWifiInfo(wifiName, wifiPwd);//使用wpa2的wifi加密方式
            int newNetworkId = mWifiManager.addNetwork(wifiNewConfiguration);
            if (newNetworkId == -1) {
                Log.e(TAG, "操作失败,需要您到手机wifi列表中取消对设备连接的保存");
            } else {
                return doChange2Wifi(newNetworkId);
            }
        }

        return false;
    }

    private boolean doChange2Wifi(int newNetworkId) {
        // 如果wifi权限没打开（1、先打开wifi，2，使用指定的wifi
        if(!mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(true);
        }

        boolean enableNetwork = mWifiManager.enableNetwork(newNetworkId, true);
        if (!enableNetwork) {
            Log.e(TAG, "切换到指定wifi失败");
            return false;
        } else {
            Log.e(TAG, "切换到指定wifi成功");
            return true;
        }
    }

    /**
     * 创建 WifiConfiguration，这里创建的是wpa2加密方式的wifi
     *
     * @param ssid     wifi账号
     * @param password wifi密码
     * @return
     */
    private WifiConfiguration createWifiInfo(String ssid, String password) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + ssid + "\"";
        config.preSharedKey = "\"" + password + "\"";
        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        config.status = WifiConfiguration.Status.ENABLED;
        return config;
    }

    public static final String TAG = "WifiUtil";
    public void printCurWifiInfo(){
        if(mWifiManager == null){
            return;
        }

        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        Log.i(TAG, "cur wifi = " + wifiInfo.getSSID());
        Log.i(TAG, "cur getNetworkId = " + wifiInfo.getNetworkId());
    }


    private WifiManager mWifiManager;

    // 单例
    private static final WifiUtil ourInstance = new WifiUtil();

    public static WifiUtil getIns() {
        return ourInstance;
    }

    public void init(Context context) {
        mWifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
    }
}

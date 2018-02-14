package com.tjstudy.wifidemo.utils;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

/**
 * WIFI的各种操作与处理工具类
 */
public class WifiOperator {

    private static Context context;
    private WifiManager wifiManager;

    private static WifiOperator wifiOperator;

    private WifiManager.WifiLock wifiLock;

    private List<WifiConfiguration> wifiConfigurationList;
    private List<ScanResult> scanResultList;

    private WifiOperator() {
        if (context != null) {
            wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            this.startScan();
        }
    }

    public static WifiOperator getInstance() {
        if (wifiOperator == null && context != null) {
            wifiOperator = new WifiOperator();
        }
        return wifiOperator;
    }

    public static void setContext(Context _context) {
        context = _context;
    }

    /**
     * 查看WIFI当前是否处于打开状态
     *
     * @return true 处于打开状态；false 处于非打开状态(包括UnKnow状态)。
     */
    public boolean isWifiClosed() {
        int wifiState = getWifiState();
        if (wifiState == WifiManager.WIFI_STATE_DISABLED || wifiState == WifiManager.WIFI_STATE_DISABLING) {
            return true;
        }
        return false;
    }

    /**
     * 查看WIFI当前是否处于关闭状态
     *
     * @return true 处于关闭状态；false 处于非关闭状态(包括UNKNOW状态)
     */
    public boolean isWifiOpened() {
        int wifiState = getWifiState();
        if (wifiState == WifiManager.WIFI_STATE_ENABLED || wifiState == WifiManager.WIFI_STATE_ENABLING) {
            return true;
        }
        return false;
    }

    /**
     * 如果WIFI当前处于关闭状态，则打开WIFI
     */
    public void openWifi() {
        if (wifiManager != null && isWifiClosed()) {
            wifiManager.setWifiEnabled(true);
        }
    }

    /**
     * 如果WIFI当前处于打开状态，则关闭WIFI
     */
    public void closeWifi() {
        if (wifiManager != null && isWifiOpened()) {
            wifiManager.setWifiEnabled(false);
        }
    }

    /**
     * 获取当前Wifi的状态编码
     *
     * @return WifiManager.WIFI_STATE_ENABLED，WifiManager.WIFI_STATE_ENABLING，
     * WifiManager.WIFI_STATE_DISABLED，WifiManager.WIFI_STATE_DISABLING，
     * WifiManager.WIFI_STATE_UnKnow 中间的一个
     */
    public int getWifiState() {
        if (wifiManager != null) {
            return wifiManager.getWifiState();
        }
        return 0;
    }

    /**
     * 获取已经配置好的Wifi网络
     *
     * @return
     */
    public List<WifiConfiguration> getSavedWifiConfiguration() {
        return wifiConfigurationList;
    }

    /**
     * 获取扫描到的网络的信息
     *
     * @return
     */
    public List<ScanResult> getWifiScanResult() {
        return scanResultList;
    }

    /**
     * 执行一次Wifi的扫描
     */
    public synchronized void startScan() {
        if (wifiManager != null) {
            wifiManager.startScan();
            scanResultList = wifiManager.getScanResults();
            wifiConfigurationList = wifiManager.getConfiguredNetworks();
        }
    }

    /**
     * 通过netWorkId来连接一个已经保存好的Wifi网络
     *
     * @param netWorkId
     */
    public void connetionConfiguration(int netWorkId) {
        if (configurationNetWorkIdCheck(netWorkId) && wifiManager != null) {
            Log.e("WifiOperator", "最后执行");
            wifiManager.enableNetwork(netWorkId, true);
        }
    }

    /**
     * 断开一个指定ID的网络
     */
    public void disconnectionConfiguration(int netWorkId) {
        wifiManager.disableNetwork(netWorkId);
        wifiManager.disconnect();
    }

    /**
     * 检测尝试连接某个网络时，查看该网络是否已经在保存的队列中间
     *
     * @param netWorkId
     * @return
     */
    private boolean configurationNetWorkIdCheck(int netWorkId) {
        for (WifiConfiguration temp : wifiConfigurationList) {
            if (temp.networkId == netWorkId) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取Wifi的数据
     *
     * @return
     */
    public WifiInfo getWifiConnectionInfo() {
        return wifiManager.getConnectionInfo();
    }

    /**
     * 锁定WIFI，使得在熄屏状态下，仍然可以使用WIFI
     */
    public void acquireWifiLock() {
        if (wifiLock != null) {
            wifiLock.acquire();
        }
    }

    /**
     * 解锁WIFI
     */
    public void releaseWifiLock() {
        if (wifiLock != null) {
            if (wifiLock.isHeld()) {
                wifiLock.acquire();
            }
        }
    }

    /**
     * 创建一个WifiLock
     */
    public void createWifiLock() {
        if (wifiManager != null) {
            wifiLock = wifiManager.createWifiLock("wifiLock");
        }
    }

    /**
     * 保存一个新的网络
     *
     * @param _wifiConfiguration
     */
    public int addNetWork(WifiConfiguration _wifiConfiguration) {
        int netWorkId = -255;
        if (_wifiConfiguration != null && wifiManager != null) {
            netWorkId = wifiManager.addNetwork(_wifiConfiguration);
            startScan();
        }
        return netWorkId;
    }

    /**
     * 保存并连接到一个新的网络
     *
     * @param _wifiConfiguration
     */
    public void addNetWorkAndConnect(WifiConfiguration _wifiConfiguration) {
        int netWorkId = addNetWork(_wifiConfiguration);
        if (netWorkId != -255) {
            connetionConfiguration(netWorkId);
        }
    }

    /**
     * 获取当前连接状态中的Wifi的信号强度
     *
     * @return
     */
    public int getConnectedWifiLevel() {
        WifiInfo wifiInfo = getWifiConnectionInfo();
        if (wifiInfo != null) {
            String connectedWifiSSID = wifiInfo.getSSID();
            if (scanResultList != null) {
                for (ScanResult temp : scanResultList) {
                    if (temp.SSID.replace("\"", "").equals(connectedWifiSSID.replace("\"", ""))) {
                        return temp.level;
                    }
                }
            }
        }
        return 1;
    }

    /**
     * 删除一个已经保存的网络
     *
     * @param netWorkId
     */
    public void remoteNetWork(int netWorkId) {
        if (wifiManager != null) {
            wifiManager.removeNetwork(netWorkId);
        }
    }

    /**
     * Created by wangzong on 14-9-24.
     * <p>
     * Wifi加密类型的描述类
     */
    public static enum WifiCipherType {
        NONE, IEEE8021XEAP, WEP, WPA, WPA2, WPAWPA2;
    }

    /**
     * 连接一个WIFI
     *
     * @param ssid
     * @param password
     * @param wifiCipherType
     */
    public void addNetWorkAndConnect(String ssid, String password, WifiCipherType wifiCipherType) {
        if (wifiManager != null && wifiCipherType != null) {
            WifiConfiguration wifiConfig = createWifiConfiguration(ssid, password, wifiCipherType);
            WifiConfiguration temp = isWifiConfigurationSaved(wifiConfig);
            if (temp != null) {
                wifiManager.removeNetwork(temp.networkId);
            }
            addNetWorkAndConnect(wifiConfig);
        }
    }

    private WifiConfiguration isWifiConfigurationSaved(WifiConfiguration wifiConfig) {
        if (wifiConfigurationList == null) {
            this.startScan();
        }
        for (WifiConfiguration temp : wifiConfigurationList) {
            if (temp.SSID.equals(wifiConfig.SSID)) {
                return temp;
            }
        }
        return null;
    }

    private WifiConfiguration createWifiConfiguration(String ssid, String password, WifiCipherType type) {
        WifiConfiguration newWifiConfiguration = new WifiConfiguration();
        newWifiConfiguration.allowedAuthAlgorithms.clear();
        newWifiConfiguration.allowedGroupCiphers.clear();
        newWifiConfiguration.allowedKeyManagement.clear();
        newWifiConfiguration.allowedPairwiseCiphers.clear();
        newWifiConfiguration.allowedProtocols.clear();
        newWifiConfiguration.SSID = "\"" + ssid + "\"";
        switch (type) {
            case NONE:
                newWifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                break;
            case IEEE8021XEAP:
                break;
            case WEP:
                break;
            case WPA:
                newWifiConfiguration.preSharedKey = "\"" + password + "\"";
                newWifiConfiguration.hiddenSSID = true;
                newWifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                newWifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                newWifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                newWifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                newWifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                newWifiConfiguration.status = WifiConfiguration.Status.ENABLED;
                break;
            case WPA2:
                newWifiConfiguration.preSharedKey = "\"" + password + "\"";
                newWifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                newWifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                newWifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                newWifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                newWifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                newWifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                newWifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                newWifiConfiguration.status = WifiConfiguration.Status.ENABLED;
                break;
            default:
                return null;
        }
        return newWifiConfiguration;
    }
}
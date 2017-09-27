package pt.ptinovacao.arqospocket.core.network.data.wifi;

import android.net.wifi.ScanResult;

import java.util.List;

import pt.ptinovacao.arqospocket.core.network.data.BaseNetworkInfo;
import pt.ptinovacao.arqospocket.core.utils.JsonHelper;

public class WifiInfoData extends BaseNetworkInfo {

    private Integer rssi;

    private Integer signal;

    private int wifiState;

    private String rxLevel = null;

    private String ssid = null;

    private String channel = null;

    private String linkSpeed = null;

    private String bssid;

    private boolean hiddenSsid;

    private String macAddress;

    private boolean realMacAddress;

    private String ipAddress;

    private String dns1;

    private String dns2;

    private String gateway;

    private Integer leaseDuration;

    private String netMask;

    private String serverAddress;

    private List<ScanResult> scanWifiList;

    public Integer getRssi() {
        return rssi;
    }

    public void setRssi(Integer rssi) {
        this.rssi = rssi;
    }

    public Integer getSignal() {
        return signal;
    }

    public void setSignal(Integer signal) {
        this.signal = signal;
    }

    public int getWifiState() {
        return wifiState;
    }

    public void setWifiState(int wifiState) {
        this.wifiState = wifiState;
    }

    public String getRxLevel() {
        return rxLevel;
    }

    public void setRxLevel(String rxLevel) {
        this.rxLevel = rxLevel;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getLinkSpeed() {
        return linkSpeed;
    }

    public void setLinkSpeed(String linkSpeed) {
        this.linkSpeed = linkSpeed;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public boolean isHiddenSsid() {
        return hiddenSsid;
    }

    public void setHiddenSsid(boolean hiddenSsid) {
        this.hiddenSsid = hiddenSsid;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public boolean isRealMacAddress() {
        return realMacAddress;
    }

    public void setRealMacAddress(boolean realMacAddress) {
        this.realMacAddress = realMacAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDns1() {
        return dns1;
    }

    public void setDns1(String dns1) {
        this.dns1 = dns1;
    }

    public String getDns2() {
        return dns2;
    }

    public void setDns2(String dns2) {
        this.dns2 = dns2;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public Integer getLeaseDuration() {
        return leaseDuration;
    }

    public void setLeaseDuration(Integer leaseDuration) {
        this.leaseDuration = leaseDuration;
    }

    public String getNetMask() {
        return netMask;
    }

    public void setNetMask(String netMask) {
        this.netMask = netMask;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public List<ScanResult> getScanWifiList() {
        return scanWifiList;
    }

    public void setScanWifiList(List<ScanResult> scanWifiList) {
        this.scanWifiList = scanWifiList;
    }

    @Override
    public String toString() {
        return JsonHelper.getGsonInstance(true).toJson(this);
    }
}

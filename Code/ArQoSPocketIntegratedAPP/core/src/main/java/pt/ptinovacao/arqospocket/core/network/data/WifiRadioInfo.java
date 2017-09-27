package pt.ptinovacao.arqospocket.core.network.data;

/**
 * Class that identifies an object that contains device related connectivity information.
 * <p>
 * Created by Tomás Rodrigues on 20-04-2017.
 */
public abstract class WifiRadioInfo extends RadioInfo {

    private String bssid;

    private Integer rssi;

    private String ssid;

    private String IpAddress;

    private String LinkSpeed;

    private String rxLevel;

    private Integer leaseDuration;

    private String wifiState;

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getIpAddress() {
        return IpAddress;
    }

    public void setIpAddress(String ipAddress) {
        IpAddress = ipAddress;
    }

    public String getLinkSpeed() {
        return LinkSpeed;
    }

    public void setLinkSpeed(String linkSpeed) {
        LinkSpeed = linkSpeed;
    }

    public String getRxLevel() {
        return rxLevel;
    }

    public void setRxLevel(String rxLevel) {
        this.rxLevel = rxLevel;
    }

    public Integer getLeaseDuration() {
        return leaseDuration;
    }

    public void setLeaseDuration(Integer leaseDuration) {
        this.leaseDuration = leaseDuration;
    }

    public String getWifiState() {
        return wifiState;
    }

    public void setWifiState(String wifiState) {
        this.wifiState = wifiState;
    }

    public Integer getRssi() {
        return rssi;
    }

    public void setRssi(Integer rssi) {
        this.rssi = rssi;
    }
}

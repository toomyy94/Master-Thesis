package pt.ptinovacao.arqospocket.core.network.data.wifi;

import android.support.annotation.NonNull;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

import pt.ptinovacao.arqospocket.core.network.data.WifiRadioInfo;

/**
 * Default {@link WifiRadioInfo} implementation.
 * Created by Tomás Rodrigues on 05-05-2017.
 */
public class DefaultWifiInfo extends WifiRadioInfo {

    private String bssid;

    private String ssid;

    private String rxLevel;

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

    public String getRxLevel() {
        return rxLevel;
    }

    public void setRxLevel(String rxLevel) {
        this.rxLevel = rxLevel;
    }

    public String getWifiState() {
        return wifiState;
    }

    public void setWifiState(String wifiState) {
        this.wifiState = wifiState;
    }

    @Override
    public String format() {
        List<String> values = getPositionalValues();
        return Joiner.on(',').useForNull("").join(values);
    }

    @NonNull
    @Override
    protected List<String> getPositionalValues() {
        List<String> values = new ArrayList<>();
        for (int i = 0; i < RADIO_INFO_FIELD_COUNT; i++) {
            values.add("");
        }

        values.set(22, getBssid());
        values.set(25, getRxLevel());
        values.set(26, getSsid());
        values.set(27, getWifiState());
        values.set(28, ""); //quality
        values.set(29, ""); //noiseLevel
        values.set(30, ""); //snr
        values.set(38, ""); //mcs
        values.set(39, ""); //channel bandwidth

        return values;
    }
}

package pt.ptinovacao.arqospocket.core.tests.data.results;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;

/**
 * Scan network task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class ScanWifiNetworkTaskResult extends TaskResult {

    public static final String WIFI_NETWORKS_LIST = "wifi_networks_list";

    @SerializedName(WIFI_NETWORKS_LIST)
    private String[] wifiNetworkList;

    public ScanWifiNetworkTaskResult(ResolverInfo resolverInfo) {
        super(resolverInfo);
    }

    public String[] getWifiNetworkList() {
        return wifiNetworkList;
    }

    public void setWifiNetworkList(String[] wifiNetworkList) {
        this.wifiNetworkList = wifiNetworkList;
    }
}


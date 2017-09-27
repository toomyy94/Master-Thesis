package pt.ptinovacao.arqospocket.core.serialization.entities.results;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pedro on 16/05/2017.
 */
public class ProbeNotificationIpUpdate extends ProbeNotification {

    private static final String OLD_IP_ADDRESS = "oldipaddress";

    @SerializedName(OLD_IP_ADDRESS)
    private String oldIpAddress;

    public static String getOldIpAddress() {
        return OLD_IP_ADDRESS;
    }

    public void setOldIpAddress(String oldIpAddress) {
        this.oldIpAddress = oldIpAddress;
    }
}


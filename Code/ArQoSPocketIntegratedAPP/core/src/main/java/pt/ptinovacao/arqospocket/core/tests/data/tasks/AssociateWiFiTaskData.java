package pt.ptinovacao.arqospocket.core.tests.data.tasks;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;

/**
 * Associate WiFi task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class AssociateWiFiTaskData extends TaskData {

    public static final String ESSID = "essid";

    public static final String BSSID = "bssid";

    public static final String PASSWORD = "password";

    @SerializedName(ESSID)
    private String essid;

    @SerializedName(BSSID)
    private String bssid;

    @SerializedName(PASSWORD)
    private String password;

    public AssociateWiFiTaskData(ResolverInfo resolverInfo) {
        super(resolverInfo);
    }

    public String getEssid() {
        return essid;
    }

    public void setEssid(String essid) {
        this.essid = essid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

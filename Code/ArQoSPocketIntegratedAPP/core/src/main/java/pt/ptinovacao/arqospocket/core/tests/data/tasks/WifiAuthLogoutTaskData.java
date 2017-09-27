package pt.ptinovacao.arqospocket.core.tests.data.tasks;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;

/**
 * Wifi Auth logout task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class WifiAuthLogoutTaskData extends TaskData {

    public static final String URL = "url";

    @SerializedName(URL)
    private String url;

    public WifiAuthLogoutTaskData(ResolverInfo resolverInfo) {
        super(resolverInfo);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

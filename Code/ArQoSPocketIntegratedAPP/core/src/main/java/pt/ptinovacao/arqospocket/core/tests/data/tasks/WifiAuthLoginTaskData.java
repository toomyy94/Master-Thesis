package pt.ptinovacao.arqospocket.core.tests.data.tasks;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;

/**
 * Wifi Auth login task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class WifiAuthLoginTaskData extends TaskData {

    public static final String URL = "url";

    public static final String USERNAME = "username";

    public static final String PASSWORD = "password";

    @SerializedName(URL)
    private String url;

    @SerializedName(USERNAME)
    private String username;

    @SerializedName(PASSWORD)
    private String password;

    public WifiAuthLoginTaskData(ResolverInfo resolverInfo) {
        super(resolverInfo);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

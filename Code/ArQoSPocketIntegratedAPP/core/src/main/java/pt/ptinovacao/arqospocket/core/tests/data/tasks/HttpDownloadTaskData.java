package pt.ptinovacao.arqospocket.core.tests.data.tasks;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;

/**
 * HTTP download task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class HttpDownloadTaskData extends TaskData {

    public static final String URL = "url";

    public static final String PROXY = "proxy";

    public static final String USER_AGENT = "user_agent";

    @SerializedName(URL)
    private String url;

    @SerializedName(PROXY)
    private String proxy;

    @SerializedName(USER_AGENT)
    private String userAgent;

    public HttpDownloadTaskData(ResolverInfo resolverInfo) {
        super(resolverInfo);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}


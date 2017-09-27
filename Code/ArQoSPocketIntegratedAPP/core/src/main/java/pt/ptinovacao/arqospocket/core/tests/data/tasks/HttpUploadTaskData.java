package pt.ptinovacao.arqospocket.core.tests.data.tasks;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;

/**
 * HTTP upload task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class HttpUploadTaskData extends TaskData {

    public static final String URL = "url";

    public static final String PROXY = "proxy";

    public static final String CONTENT = "content";

    @SerializedName(URL)
    private String url;

    @SerializedName(PROXY)
    private String proxy;

    @SerializedName(CONTENT)
    private String content;

    public HttpUploadTaskData(ResolverInfo resolverInfo) {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}


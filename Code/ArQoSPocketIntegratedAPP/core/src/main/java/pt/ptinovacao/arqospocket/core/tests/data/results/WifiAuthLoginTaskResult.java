package pt.ptinovacao.arqospocket.core.tests.data.results;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;

/**
 * Wifi Auth login task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class WifiAuthLoginTaskResult extends TaskResult {

    public static final String RESPONSE_CODE = "response_code";

    public static final String RESPONSE_TIME = "response_time";

    @SerializedName(RESPONSE_CODE)
    private String responseCode;

    @SerializedName(RESPONSE_TIME)
    private String responseTime;

    public WifiAuthLoginTaskResult(ResolverInfo resolverInfo) {
        super(resolverInfo);
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }
}

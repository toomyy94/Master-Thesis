package pt.ptinovacao.arqospocket.core.serialization.entities.results.type71;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.entities.results.ProbeNotification;

/**
 * Created by pedro on 16/05/2017.
 */
public class ProbeNotificationLoadedTestStatusRequestResponse extends ProbeNotification {

    private static final String ASSOCIATED_RESPONSE = "associatedResponse";

    @SerializedName(ASSOCIATED_RESPONSE)
    private ProbeLoadedTestStatusRequest response;

    public ProbeLoadedTestStatusRequest getResponse() {
        return response;
    }

    public void setResponse(ProbeLoadedTestStatusRequest response) {
        this.response = response;
    }
}


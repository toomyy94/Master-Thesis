package pt.ptinovacao.arqospocket.core.serialization.entities.results.type66;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.entities.results.ProbeNotification;

/**
 * Created by pedro on 16/05/2017.
 */
public class ProbeNotificationAssociatedStatusRequestResponse extends ProbeNotification {

    private static final String ASSOCIATED_RESPONSE = "associatedResponse";

    @SerializedName(ASSOCIATED_RESPONSE)
    private ProbeResponseStatusRequest response;

    public ProbeResponseStatusRequest getResponse() {
        return response;
    }

    public void setResponse(ProbeResponseStatusRequest response) {
        this.response = response;
    }
}


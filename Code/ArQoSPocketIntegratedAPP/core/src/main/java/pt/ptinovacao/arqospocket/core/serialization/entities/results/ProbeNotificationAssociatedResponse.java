package pt.ptinovacao.arqospocket.core.serialization.entities.results;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.entities.results.proberesponse.ProbeResponse;

/**
 * Created by pedro on 16/05/2017.
 */
public class ProbeNotificationAssociatedResponse extends ProbeNotification {

    private static final String ASSOCIATED_RESPONSE = "associatedResponse";

    @SerializedName(ASSOCIATED_RESPONSE)
    private ProbeResponse response;

    public ProbeResponse getResponse() {
        return response;
    }

    public void setResponse(ProbeResponse response) {
        this.response = response;
    }
}


package pt.ptinovacao.arqospocket.core.serialization.entities.results.type2;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.entities.results.ProbeNotification;

/**
 * Created by pedro on 16/05/2017.
 */
public class ProbeNotificationAssociatedModuleStatusRequestResponse extends ProbeNotification {

    private static final String ASSOCIATED_RESPONSE = "associatedResponse";

    @SerializedName(ASSOCIATED_RESPONSE)
    private ModuleStatusRequest response;

    public ModuleStatusRequest getResponse() {
        return response;
    }

    public void setResponse(ModuleStatusRequest response) {
        this.response = response;
    }
}


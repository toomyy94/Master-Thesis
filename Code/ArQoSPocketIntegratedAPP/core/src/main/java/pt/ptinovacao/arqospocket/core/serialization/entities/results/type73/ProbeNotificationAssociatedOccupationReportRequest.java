package pt.ptinovacao.arqospocket.core.serialization.entities.results.type73;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.entities.results.ProbeNotification;

/**
 * Created by pedro on 16/05/2017.
 */
public class ProbeNotificationAssociatedOccupationReportRequest extends ProbeNotification {

    private static final String ASSOCIATED_RESPONSE = "associatedResponse";

    @SerializedName(ASSOCIATED_RESPONSE)
    private OccupationReportRequest response;

    public OccupationReportRequest getResponse() {
        return response;
    }

    public void setResponse(OccupationReportRequest response) {
        this.response = response;
    }
}


package pt.ptinovacao.arqospocket.core.serialization.entities.results;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by pedro on 16/05/2017.
 */
public class ProbeNotificationAttachmentsProcess extends ProbeNotification {

    private static final String ASSOCIATED_FILENAME = "associatedfilename";

    @SerializedName(ASSOCIATED_FILENAME)
    private ArrayList<String> associatedfilename;

    public ArrayList<String> getAssociatedfilename() {
        return associatedfilename;
    }

    public void setAssociatedfilename(ArrayList<String> associatedfilename) {
        this.associatedfilename = associatedfilename;
    }
}
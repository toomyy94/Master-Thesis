package pt.ptinovacao.arqospocket.core.serialization.entities.results;

import java.util.ArrayList;

/**
 * Created by pedro on 16/05/2017.
 */
public class ResultFileAttachmentsProcess extends ResultFileData {

    public static class Builder extends ResultFileData.Builder {

        private ArrayList<String> associatedfilename;

        public ArrayList<String> getAssociatedfilename() {
            return associatedfilename;
        }

        public Builder associatedfilename(ArrayList<String> associatedfilename) {
            this.associatedfilename = associatedfilename;
            return this;
        }

        public ResultFileAttachmentsProcess build() {

            ResultFileAttachmentsProcess data = new ResultFileAttachmentsProcess();

            ProbeNotificationAttachmentsProcess probeNotification = new ProbeNotificationAttachmentsProcess();
            data.setNotification(probeNotification);

            setValues(probeNotification);

            probeNotification.setAssociatedfilename(getAssociatedfilename());

            return data;
        }
    }
}
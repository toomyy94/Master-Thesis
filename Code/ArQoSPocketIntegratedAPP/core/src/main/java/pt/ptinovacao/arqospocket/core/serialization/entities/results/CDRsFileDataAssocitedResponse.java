package pt.ptinovacao.arqospocket.core.serialization.entities.results;

import com.google.gson.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.core.serialization.entities.results.proberesponse.ProbeResponse;

/**
 * Created by Tomás on 07/09/2017.
 */
public class CDRsFileDataAssocitedResponse extends ResultFileData {

    private static final Logger LOGGER = LoggerFactory.getLogger(CDRsFileDataAssocitedResponse.class);

    public static class Builder extends ResultFileData.Builder {

        protected JsonObject pippedString;

        public Builder() {
            super();
            this.pippedString = new JsonObject();
        }



        public ResultFileData.Builder appendPippedCDRs(JsonObject pippedString) {
            this.pippedString = pippedString;
            return this;
        }

        public CDRsFileDataAssocitedResponse build() {

            CDRsFileDataAssocitedResponse data = new CDRsFileDataAssocitedResponse();

            ProbeNotificationAssociatedResponse probeNotification = new ProbeNotificationAssociatedResponse();
            data.setNotification(probeNotification);

            setValues(probeNotification);

            ProbeResponse probeResponse = new ProbeResponse();
            probeNotification.setResponse(probeResponse);

            probeResponse.setCDRs(pippedString);

            return data;
        }
    }
}

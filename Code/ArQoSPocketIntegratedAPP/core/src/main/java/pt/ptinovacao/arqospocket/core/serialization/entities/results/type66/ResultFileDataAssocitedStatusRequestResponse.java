package pt.ptinovacao.arqospocket.core.serialization.entities.results.type66;

import pt.ptinovacao.arqospocket.core.serialization.entities.results.ResultFileData;

/**
 * Created by pedro on 16/05/2017.
 */
public class ResultFileDataAssocitedStatusRequestResponse extends ResultFileData {

    public static class Builder extends ResultFileData.Builder {

        public Builder() {
            super();
            this.probeConfiguration = new ProbeConfiguration();
            this.probeHardware = new ProbeHardware();
        }

        private ProbeConfiguration probeConfiguration;

        private ProbeHardware probeHardware;

        public ResultFileDataAssocitedStatusRequestResponse.Builder appendProbeConfiguration(
                ProbeConfiguration probeConfiguration) {
            if (probeConfiguration != null) {
                this.probeConfiguration = probeConfiguration;
            }
            return this;
        }

        public ResultFileDataAssocitedStatusRequestResponse.Builder appendProbeHardware(ProbeHardware probeHardware) {
            if (probeHardware != null) {
                this.probeHardware = probeHardware;
            }
            return this;
        }

        @Override
        public ResultFileDataAssocitedStatusRequestResponse build() {

            ResultFileDataAssocitedStatusRequestResponse data = new ResultFileDataAssocitedStatusRequestResponse();

            ProbeNotificationAssociatedStatusRequestResponse probeNotification =
                    new ProbeNotificationAssociatedStatusRequestResponse();
            data.setNotification(probeNotification);

            setValues(probeNotification);

            ProbeResponseStatusRequest probeResponse = new ProbeResponseStatusRequest();
            probeNotification.setResponse(probeResponse);

            probeResponse.setProbeConfiguration(probeConfiguration);
            probeResponse.setProbeHardware(probeHardware);
            return data;
        }
    }
}

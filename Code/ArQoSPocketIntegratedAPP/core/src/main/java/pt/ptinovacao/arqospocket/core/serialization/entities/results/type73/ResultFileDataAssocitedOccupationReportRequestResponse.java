package pt.ptinovacao.arqospocket.core.serialization.entities.results.type73;

import pt.ptinovacao.arqospocket.core.serialization.entities.results.ResultFileData;

/**
 * Created by pedro on 16/05/2017.
 */
public class ResultFileDataAssocitedOccupationReportRequestResponse extends ResultFileData {

    public static class Builder extends ResultFileData.Builder {

        public Builder() {
            super();
            this.occupationReportRequest = new OccupationReportRequest();
        }

        private OccupationReportRequest occupationReportRequest;

        public ResultFileDataAssocitedOccupationReportRequestResponse.Builder appendOccupationReportRequest(
                OccupationReportRequest moduleStatusRequest) {

            if (moduleStatusRequest != null) {
                this.occupationReportRequest = moduleStatusRequest;
            }
            return this;
        }

        @Override
        public ResultFileDataAssocitedOccupationReportRequestResponse build() {

            ResultFileDataAssocitedOccupationReportRequestResponse data =
                    new ResultFileDataAssocitedOccupationReportRequestResponse();

            ProbeNotificationAssociatedOccupationReportRequest probeNotification =
                    new ProbeNotificationAssociatedOccupationReportRequest();
            data.setNotification(probeNotification);

            probeNotification.setResponse(occupationReportRequest);

            setValues(probeNotification);
            return data;
        }
    }
}

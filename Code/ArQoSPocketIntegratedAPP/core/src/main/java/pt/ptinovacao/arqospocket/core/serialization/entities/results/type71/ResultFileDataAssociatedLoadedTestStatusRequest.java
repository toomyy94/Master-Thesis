package pt.ptinovacao.arqospocket.core.serialization.entities.results.type71;

import java.util.ArrayList;

import pt.ptinovacao.arqospocket.core.serialization.entities.results.ResultFileData;

/**
 * Created by pedro on 16/05/2017.
 */
public class ResultFileDataAssociatedLoadedTestStatusRequest extends ResultFileData {

    public static class Builder extends ResultFileData.Builder {

        public Builder() {
            super();
            this.testInfos = new ArrayList<>();
        }

        private ArrayList<TestInfo> testInfos;

        public ResultFileDataAssociatedLoadedTestStatusRequest.Builder appendTestInfo(ArrayList<TestInfo> testInfos) {
            if (testInfos != null) {
                this.testInfos = testInfos;
            }
            return this;
        }

        @Override
        public ResultFileDataAssociatedLoadedTestStatusRequest build() {

            ResultFileDataAssociatedLoadedTestStatusRequest data =
                    new ResultFileDataAssociatedLoadedTestStatusRequest();

            ProbeNotificationLoadedTestStatusRequestResponse probeNotification =
                    new ProbeNotificationLoadedTestStatusRequestResponse();
            data.setNotification(probeNotification);

            setValues(probeNotification);

            ProbeLoadedTestStatusRequest probeResponse = new ProbeLoadedTestStatusRequest();
            probeNotification.setResponse(probeResponse);

            probeResponse.setData(testInfos);
            return data;
        }
    }
}

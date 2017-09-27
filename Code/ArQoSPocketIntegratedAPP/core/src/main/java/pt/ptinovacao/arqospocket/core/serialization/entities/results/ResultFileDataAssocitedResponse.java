package pt.ptinovacao.arqospocket.core.serialization.entities.results;

import java.util.ArrayList;
import java.util.List;

import pt.ptinovacao.arqospocket.core.serialization.entities.results.proberesponse.ProbeResponse;
import pt.ptinovacao.arqospocket.core.tests.data.TestResult;

/**
 * Created by pedro on 16/05/2017.
 */
public class ResultFileDataAssocitedResponse extends ResultFileData {

    public static class Builder extends ResultFileData.Builder {

        public Builder() {
            super();
            this.results = new ArrayList<>();
        }

        protected List<TestResult> results;

        public ResultFileData.Builder appendResults(List<TestResult> results) {
            if (results != null) {
                this.results.addAll(results);
            }
            return this;
        }

        public ResultFileData.Builder appendResult(TestResult result) {
            this.results.add(result);
            return this;
        }

        public ResultFileDataAssocitedResponse build() {

            ResultFileDataAssocitedResponse data = new ResultFileDataAssocitedResponse();

            ProbeNotificationAssociatedResponse probeNotification = new ProbeNotificationAssociatedResponse();
            data.setNotification(probeNotification);

            setValues(probeNotification);

            ProbeResponse probeResponse = new ProbeResponse();
            probeNotification.setResponse(probeResponse);

            probeResponse.setResults(results);

            return data;
        }
    }
}

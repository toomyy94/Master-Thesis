package pt.ptinovacao.arqospocket.core.serialization.entities.results.type2;

import pt.ptinovacao.arqospocket.core.serialization.entities.results.ResultFileData;

/**
 * Created by pedro on 16/05/2017.
 */
public class ResultFileDataAssocitedModuleStatusRequestResponse extends ResultFileData {

    public static class Builder extends ResultFileData.Builder {

        public Builder() {
            super();
            this.moduleStatusRequest = new ModuleStatusRequest();
        }

        private ModuleStatusRequest moduleStatusRequest;

        public ResultFileDataAssocitedModuleStatusRequestResponse.Builder appendModuleStatusRequest(
                ModuleStatusRequest moduleStatusRequest) {

            if (moduleStatusRequest != null) {
                this.moduleStatusRequest = moduleStatusRequest;
            }
            return this;
        }

        @Override
        public ResultFileDataAssocitedModuleStatusRequestResponse build() {

            ResultFileDataAssocitedModuleStatusRequestResponse data =
                    new ResultFileDataAssocitedModuleStatusRequestResponse();

            ProbeNotificationAssociatedModuleStatusRequestResponse probeNotification =
                    new ProbeNotificationAssociatedModuleStatusRequestResponse();
            data.setNotification(probeNotification);

            probeNotification.setResponse(moduleStatusRequest);

            setValues(probeNotification);
            return data;
        }

    }
}

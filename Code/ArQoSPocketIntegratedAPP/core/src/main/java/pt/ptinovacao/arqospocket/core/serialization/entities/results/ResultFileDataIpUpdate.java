package pt.ptinovacao.arqospocket.core.serialization.entities.results;

/**
 * Created by pedro on 16/05/2017.
 */
public class ResultFileDataIpUpdate extends ResultFileData {

    public static class Builder extends ResultFileData.Builder {

        private String oldIpAddress;

        public String getOldIpAddress() {
            return oldIpAddress;
        }

        public Builder oldIpAddress(String oldIpAddress) {
            this.oldIpAddress = oldIpAddress;
            return this;
        }

        public ResultFileDataIpUpdate build() {

            ResultFileDataIpUpdate data = new ResultFileDataIpUpdate();

            ProbeNotificationIpUpdate probeNotification = new ProbeNotificationIpUpdate();
            data.setNotification(probeNotification);

            setValues(probeNotification);

            probeNotification.setOldIpAddress(oldIpAddress);

            return data;
        }
    }
}
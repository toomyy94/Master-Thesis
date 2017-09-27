package pt.ptinovacao.arqospocket.core.serialization.entities.results;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Wrapper file for the test results.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class ResultFileData {

    private static final String PROBE_NOTIFICATION = "probenotification";

    @SerializedName(PROBE_NOTIFICATION)
    private ProbeNotification notification;

    public ProbeNotification getNotification() {
        return notification;
    }

    public void setNotification(ProbeNotification notification) {
        this.notification = notification;
    }

    public static class Builder {

        private int errorCode;

        private String serialNumber;

        private String macAddress;

        private String ipAddress;

        private String equipmentType;

        private Date timestamp;

        private String error;

        private int messageType;

        public Builder() {
            errorCode = -1;
        }

        public Builder serialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
            return this;
        }

        public Builder macAddress(String macAddress) {
            this.macAddress = macAddress;
            return this;
        }

        public Builder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public Builder equipmentType(String equipmentType) {
            this.equipmentType = equipmentType;
            return this;
        }

        public Builder timestamp(Date timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder error(String error) {
            this.error = error;
            return this;
        }

        public Builder success() {
            this.messageType = 254;
            return this;
        }

        public Builder fail(int errorCode) {
            this.errorCode = errorCode;
            this.messageType = 255;
            return this;
        }

        public ResultFileData build() {
            ResultFileData data = new ResultFileData();

            ProbeNotification probeNotification = new ProbeNotification();
            data.setNotification(probeNotification);

            setValues(probeNotification);

            return data;
        }

        protected void setValues(ProbeNotification probeNotification) {
            probeNotification.setEquipmentType(equipmentType);
            probeNotification.setIpAddress(ipAddress);
            probeNotification.setMacAddress(macAddress);
            probeNotification.setSerialNumber(serialNumber);
            probeNotification.setTimestamp(timestamp);
            probeNotification.setError(error);
            probeNotification.setErrorCode(errorCode >= 0 ? errorCode : null);
            probeNotification.setMessageType(messageType);
        }
    }
}

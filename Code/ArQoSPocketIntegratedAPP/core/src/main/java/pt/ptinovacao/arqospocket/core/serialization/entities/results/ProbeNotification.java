package pt.ptinovacao.arqospocket.core.serialization.entities.results;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Wrapper file for the test results.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class ProbeNotification {

    private static final String SERIAL_NUMBER = "serialnumber";

    private static final String MAC_ADDRESS = "macaddress";

    private static final String IP_ADDRESS = "ipaddress";

    private static final String EQUIPMENT_TYPE = "equipmenttype";

    private static final String TIMESTAMP = "timestamp";

    private static final String ERROR = "error";

    private static final String ERROR_CODE = "error_id";

    private static final String MESSAGE_TYPE = "msg_type";

    @SerializedName(SERIAL_NUMBER)
    private String serialNumber;

    @SerializedName(MAC_ADDRESS)
    private String macAddress;

    @SerializedName(IP_ADDRESS)
    private String ipAddress;

    @SerializedName(EQUIPMENT_TYPE)
    private String equipmentType;

    @SerializedName(TIMESTAMP)
    private Date timestamp;

    @SerializedName(ERROR)
    private String error;

    @SerializedName(ERROR_CODE)
    private Integer errorCode;

    @SerializedName(MESSAGE_TYPE)
    private int messageType;

    public ProbeNotification() {
        messageType = 254;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
}


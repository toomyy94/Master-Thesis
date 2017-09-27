package pt.ptinovacao.arqospocket.core.serialization.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import pt.ptinovacao.arqospocket.core.serialization.entities.results.type66.ProbeConfiguration;
import pt.ptinovacao.arqospocket.core.tests.data.TestData;

/**
 * Represents a JSON file that contains a set of tests.
 * <p>
 * Created by Emílio Simões on 07-04-2017.
 */
public class ManagementMessage {

    @SerializedName("msg_type")
    private int messageType;

    @SerializedName("modulo")
    private String modulo;

    @SerializedName("macaddress")
    private String macAddress;

    @SerializedName("probetest")
    private List<TestData> tests;

    @SerializedName("dataini")
    private String startDate;

    @SerializedName("datafim")
    private String endDate;

    @SerializedName("ProbeConfiguration")
    private ProbeConfiguration probeConfiguration;

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public List<TestData> getTests() {
        return tests;
    }

    public void setTests(List<TestData> tests) {
        this.tests = tests;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ProbeConfiguration getProbeConfiguration() {
        return probeConfiguration;
    }

    public void setProbeConfiguration(ProbeConfiguration probeConfiguration) {
        this.probeConfiguration = probeConfiguration;
    }
}

package pt.ptinovacao.arqospocket.core.serialization.entities.results.type66;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.entities.results.proberesponse.BaseProbeResponse;

/**
 * Created by Pedro Simões on 17-04-2017.
 */
public class ProbeResponseStatusRequest extends BaseProbeResponse {

    private static final String PROBE_CONFIGURATION = "ProbeConfiguration";

    private static final String PROBE_HARDWARE = "ProbeHardware";

    @SerializedName(PROBE_CONFIGURATION)
    private ProbeConfiguration probeConfiguration;

    @SerializedName(PROBE_HARDWARE)
    private ProbeHardware probeHardware;

    public String getProbeConfiguration() {
        return PROBE_CONFIGURATION;
    }

    public void setProbeConfiguration(ProbeConfiguration probeConfiguration) {
        this.probeConfiguration = probeConfiguration;
    }

    public String getProbeHardware() {
        return PROBE_HARDWARE;
    }

    public void setProbeHardware(ProbeHardware probeHardware) {
        this.probeHardware = probeHardware;
    }
}





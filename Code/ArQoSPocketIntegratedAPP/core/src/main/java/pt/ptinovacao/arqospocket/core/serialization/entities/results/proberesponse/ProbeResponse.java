package pt.ptinovacao.arqospocket.core.serialization.entities.results.proberesponse;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import pt.ptinovacao.arqospocket.core.tests.data.TestResult;

/**
 * Wrapper file for the test results.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class ProbeResponse extends BaseProbeResponse  {

    @SerializedName(PROBE_RESULTS)
    private List<TestResult> results;

    @SerializedName(PROBE_ALARMS)
    private JsonObject alarms;

    @SerializedName(PROBE_CDRS)
    private JsonObject cdrs;

    /**
     *
     * @return testResults
     */
    public List<TestResult> getResults() {
        return results;
    }

    public void setResults(List<TestResult> results) {
        this.results = results;
    }

    /**
     *
     * @return alarms
     */
    public JsonObject getAlarms() {
        return alarms;
    }

    public void setAlarms(JsonObject alarms) {
        this.alarms = alarms;
    }

    /**
     *
     * @return cdrs
     */
    public JsonObject getCDRs() {
        return cdrs;
    }

    public void setCDRs(JsonObject cdrs) {
        this.cdrs = cdrs;
    }
}

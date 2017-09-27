package pt.ptinovacao.arqospocket.core.tests.data.results;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;

/**
 * Hangup voice call task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class HangUpVoiceCallTaskResult extends TaskResult {

    public static final String CALL_DURATION = "call_duration";

    public static final String FIXED_VALUE = "fixed_value";

    @SerializedName(CALL_DURATION)
    private String callDuration;

    @SerializedName(FIXED_VALUE)
    private int fixedValue;

    public HangUpVoiceCallTaskResult(ResolverInfo resolverInfo, String name) {
        super(resolverInfo, name);
    }

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public int getFixedValue() {
        return fixedValue;
    }

    public void setFixedValue(int fixedValue) {
        this.fixedValue = fixedValue;
    }
}

package pt.ptinovacao.arqospocket.core.tests.data.tasks;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;

/**
 * Hangup voice call task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class HangUpVoiceCallTaskData extends TaskData {

    public static final String CALLS_TO_BE_TERMINATED = "calls_to_be_terminated";

    @SerializedName(CALLS_TO_BE_TERMINATED)
    private int callsToBeTerminated;

    public HangUpVoiceCallTaskData(ResolverInfo resolverInfo, String name) {
        super(resolverInfo, name);
    }

    public int getCallsToBeTerminated() {
        return callsToBeTerminated;
    }

    public void setCallsToBeTerminated(int callsToBeTerminated) {
        this.callsToBeTerminated = callsToBeTerminated;
    }
}

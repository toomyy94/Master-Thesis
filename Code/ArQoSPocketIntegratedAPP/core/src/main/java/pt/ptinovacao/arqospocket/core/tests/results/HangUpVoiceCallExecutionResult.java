package pt.ptinovacao.arqospocket.core.tests.results;

import pt.ptinovacao.arqospocket.core.network.ConnectionTechnology;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.HangUpVoiceCallTaskResult;

/**
 * Hang up voice call execution result.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class HangUpVoiceCallExecutionResult extends BaseTaskExecutionResult {

    private final HangUpVoiceCallTaskResult taskResult;

    public HangUpVoiceCallExecutionResult(TaskResult result) {
        super(result);
        taskResult = (HangUpVoiceCallTaskResult) result;
    }

    public void finalizeResult(String status, ConnectionTechnology technology) {
        setConnectionTechnology(technology);
        taskResult.setStatus(status);
    }

    public void setCallDuration(String callDuration) {
        taskResult.setCallDuration(callDuration);
    }

    public void setFixedValue(int fixedValue) {
        taskResult.setFixedValue(fixedValue);
    }
}


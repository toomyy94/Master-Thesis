package pt.ptinovacao.arqospocket.core.tests.results;

import pt.ptinovacao.arqospocket.core.network.ConnectionTechnology;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.AnswerVoiceCallTaskResult;

/**
 * Answer voice call execution result.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class AnswerVoiceCallExecutionResult extends BaseTaskExecutionResult {

    public AnswerVoiceCallExecutionResult(TaskResult result) {
        super(result);
        taskResult = (AnswerVoiceCallTaskResult) result;
    }

    private final AnswerVoiceCallTaskResult taskResult;

    public void finalizeResult(String status, ConnectionTechnology technology) {
        setConnectionTechnology(technology);
        taskResult.setStatus(status);
    }

    public void setTimeWaitingForRinging(int timeWaitingForRinging) {
        taskResult.setTimeWaitingForRinging(timeWaitingForRinging);
    }

    public void updateFixedValue() {
        taskResult.setFixedValue(1);
    }
}


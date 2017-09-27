package pt.ptinovacao.arqospocket.core.tests.results;

import pt.ptinovacao.arqospocket.core.network.ConnectionTechnology;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.MakeVoiceCallTaskResult;

/**
 * Make voice call execution result.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class MakeVoiceCallExecutionResult extends BaseTaskExecutionResult {

    private final MakeVoiceCallTaskResult taskResult;

    public MakeVoiceCallExecutionResult(TaskResult result) {
        super(result);
        taskResult = (MakeVoiceCallTaskResult) result;
    }

    public void finalizeResult(String status, ConnectionTechnology technology) {
        setConnectionTechnology(technology);
        taskResult.setStatus(status);
    }

    public void updateDestinationNumber(String destinationNumber) {
        taskResult.setDestinationNumber(destinationNumber);
    }

    public void updateSetupTime(long callSetupTime) {
        taskResult.setCallSetupTime(callSetupTime);
    }

    public void updateAudioRecordTime(String audioRecordTime) {
        taskResult.setAudioRecordFileName(audioRecordTime);
    }

    public void updateFixedValue() {
        taskResult.setFixedValue(1);
    }
}

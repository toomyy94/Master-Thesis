package pt.ptinovacao.arqospocket.core.tests.results;

import pt.ptinovacao.arqospocket.core.network.ConnectionTechnology;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.RecordAudioTaskResult;

/**
 * Record Audio call execution result.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class RecordAudioExecutionResult extends BaseTaskExecutionResult {

    private final RecordAudioTaskResult taskResult;

    public RecordAudioExecutionResult(TaskResult result) {
        super(result);
        taskResult = (RecordAudioTaskResult) result;
    }

    public void finalizeResult(String status, ConnectionTechnology technology) {
        setConnectionTechnology(technology);
        taskResult.setStatus(status);
    }

    public void setAudioRecordingFileName(String audioRecordingFileName) {
        taskResult.setAudioRecordingFileName(audioRecordingFileName);
    }
}

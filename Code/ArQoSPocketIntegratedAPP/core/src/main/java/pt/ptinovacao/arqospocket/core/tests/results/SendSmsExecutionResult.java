package pt.ptinovacao.arqospocket.core.tests.results;

import pt.ptinovacao.arqospocket.core.network.ConnectionTechnology;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.SendSmsTaskResult;

/**
 * Send SMS call execution result.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class SendSmsExecutionResult extends BaseTaskExecutionResult {

    private final SendSmsTaskResult taskResult;

    public SendSmsExecutionResult(TaskResult result) {
        super(result);
        taskResult = (SendSmsTaskResult) result;
    }

    public void updateExecutionTime( long executionTime) {
        taskResult.setSendingTime(String.valueOf(executionTime));
    }

    public void finalizeResult(String status, ConnectionTechnology technology) {
        setConnectionTechnology(technology);
        taskResult.setStatus(status);
    }

    public void updateMessageData(String destinationNumber, String messageText) {
        taskResult.setDestinationNumber(destinationNumber);
        taskResult.setMessageText(messageText);
    }
}

package pt.ptinovacao.arqospocket.core.tests.results;

import com.google.common.primitives.Ints;

import pt.ptinovacao.arqospocket.core.network.ConnectionTechnology;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.ReceiveSmsTaskResult;

/**
 * Receive SMS call execution result.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class ReceiveSmsExecutionResult extends BaseTaskExecutionResult {

    private final ReceiveSmsTaskResult taskResult;

    public ReceiveSmsExecutionResult(TaskResult result) {
        super(result);
        taskResult = (ReceiveSmsTaskResult) result;
    }

    public void updateMessageMetadata(String messageText, String number, String encoding) {
        taskResult.setMessageText(messageText);
        taskResult.setSourceNumber(number);
        taskResult.setEncoding(Ints.tryParse(encoding));
    }

    public void finalizeResult(String status, ConnectionTechnology technology) {
        setConnectionTechnology(technology);
        taskResult.setStatus(status);
    }

    public void updateMessageTimes(long waitingTime, long endToEndTime) {
        taskResult.setWaitingTime(String.valueOf(waitingTime));
        taskResult.setMessageDeliveryTime(String.valueOf(endToEndTime));
    }

    public void updateMessageTimes(long waitingTime) {
        taskResult.setWaitingTime(String.valueOf(waitingTime));
    }
}

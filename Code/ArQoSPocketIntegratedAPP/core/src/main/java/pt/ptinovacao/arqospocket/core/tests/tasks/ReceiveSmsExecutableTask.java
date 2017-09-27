package pt.ptinovacao.arqospocket.core.tests.tasks;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pt.ptinovacao.arqospocket.core.cdrs.CDRUtils;
import pt.ptinovacao.arqospocket.core.cdrs.CDRsManager;
import pt.ptinovacao.arqospocket.core.error.ErrorMapper;
import pt.ptinovacao.arqospocket.core.messaging.ReceiveSmsListener;
import pt.ptinovacao.arqospocket.core.messaging.ReceivedMessage;
import pt.ptinovacao.arqospocket.core.messaging.SmsMessageManager;
import pt.ptinovacao.arqospocket.core.network.ConnectionTechnology;
import pt.ptinovacao.arqospocket.core.serialization.TaskResolver;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.ExecutableTest;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.ReceiveSmsTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.ReceiveSmsTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.ReceiveSmsExecutionResult;
import pt.ptinovacao.arqospocket.core.utils.ParseNumberUtil;

/**
 * Receive SMS executable task.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class ReceiveSmsExecutableTask extends BaseExecutableTask implements ReceiveSmsListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiveSmsExecutableTask.class);

    private long startTime, receivedTime = 0L;

    private ExecutableTest ownerTest;

    private int index;

    private ReceiveSmsExecutionResult executionResult;

    private ReceiveSmsTaskData taskData;

    private Disposable subscribe;

    public ReceiveSmsExecutableTask(TaskData taskData) {
        super(taskData);
    }

    @Override
    public Observable<BaseTaskExecutionResult> execute(ExecutableTest ownerTest, int index) {
        this.ownerTest = ownerTest;
        this.index = index;
        createExecutionResult(index);

        Single.just(getData().getTaskName()).subscribeOn(Schedulers.newThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull String name) throws Exception {
                startTaskExecution();
            }
        });

        return Observable.empty();
    }

    @Override
    public void onSmsReceived(ReceivedMessage message) {
        LOGGER.debug("An SMS was received [{}], processing...", message.getMessage());

        receivedTime = Calendar.getInstance().getTimeInMillis();
        String[] messageParts = getMessageParts(message);
        executionResult.updateMessageMetadata(messageParts[0], message.getNumber(), message.getEncoding());

        if (messageParts.length == 1) {
            validateMessageText(messageParts, receivedTime);
        } else {
            validateTrailerText(messageParts, receivedTime);
        }
    }

    @Override
    public BaseTaskExecutionResult fail(String errorCode) {
        createExecutionResult(index);
        executionResult.finalizeResult(errorCode, ConnectionTechnology.MIXED);
        return executionResult;
    }

    private String[] getMessageParts(ReceivedMessage message) {
        if (!message.getMessage().contains("id=")) {
            return new String[] { message.getMessage() };
        }

        String[] parts = Strings.nullToEmpty(message.getMessage()).split("id=");
        if (parts.length != 2) {
            if (parts.length > 0 && parts[0].contains("id=")) {
                parts = new String[] { "", parts[0] };
            } else {
                return new String[] { "", "", "", "" };
            }
        }
      
        String text = Strings.nullToEmpty(parts[0]).trim();
        Iterable<String> strings = Splitter.on(' ').trimResults().split(Strings.nullToEmpty(parts[1]).trim());

        List<String> result = new ArrayList<>();
        result.add(text);
        boolean added = Iterables.addAll(result, strings);
        LOGGER.debug("Items added = {}", added);
        return Iterables.toArray(result, String.class);
    }

    private void startTaskExecution() {
        LOGGER.debug("Starting receive SMS task");

        //Fill Init CDR values
        CDRUtils.getInstance(getApplication()).fillTempInitSmsCdr();

        Calendar calendar = Calendar.getInstance();
        executionResult.updateStartDate(calendar.getTime());

        startTime = calendar.getTimeInMillis();

        taskData = (ReceiveSmsTaskData) getData();
        SmsMessageManager.getInstance(getApplication()).registerSmsReceiver(startTime, this);

        int timeout = ParseNumberUtil.parseNumber(taskData.getTimeout());
        LOGGER.debug("Timeout: " + timeout);
        if (timeout > 0) {
            subscribe = Single.just(RESULT_TASK_SUCCESS).subscribeOn(Schedulers.newThread())
                    .delay(timeout, TimeUnit.SECONDS).subscribe(new Consumer<String>() {
                        @Override
                        public void accept(@NonNull String result) throws Exception {
                            finishTask(ErrorMapper.SMS_RECEIVER_TIMEOUT.toString());
                        }
                    });
        }
    }

    private void createExecutionResult(int index) {
        TaskResult result = TaskResolver.taskResultForData(getData());
        executionResult = (ReceiveSmsExecutionResult) TaskResolver.executionResultForTaskResult(result);
        executionResult.setExecutionId(index);
    }

    private void validateTrailerText(String[] messageParts, long receivedTime) {
        taskData = (ReceiveSmsTaskData) getData();
        long testId = ((ownerTest.getData().getStartDate().getTime() / 1000) +
                ParseNumberUtil.parseNumber(taskData.getExecutionDelay())) % 10000;

        if (messageParts[1].equals(taskData.getExpectedTrailer() + Strings.padStart(String.valueOf(testId), 4, '0'))) {
            calculateMessageTimes(messageParts, receivedTime);
            finishTask(RESULT_TASK_SUCCESS);
        }
    }

    private void validateMessageText(String[] messageParts, long receivedTime) {
        if (messageParts[0].contains(taskData.getMessageToVerify())) {

            calculateMessageTimes(messageParts, receivedTime);
            finishTask(RESULT_TASK_SUCCESS);
        }
    }

    private void calculateMessageTimes(String[] messageParts, long receivedTime) {
        long waitingTime = receivedTime - startTime;

        if (messageParts.length > 2) {
            long sendTime = parseNumber(messageParts[2], messageParts[3]);
            long endToEndTime = receivedTime - sendTime;
            executionResult.updateMessageTimes(waitingTime, endToEndTime);
        } else {
            executionResult.updateMessageTimes(waitingTime);
        }
    }

    private long parseNumber(String secondsString, String millisecondsString) {
        long seconds = Long.parseLong(secondsString);
        long milliseconds = Long.parseLong(millisecondsString);
        return seconds * 1000 + milliseconds;
    }

    private synchronized void finishTimeout() {
        if (subscribe != null && !subscribe.isDisposed()) {
            subscribe.dispose();
            subscribe = null;
            LOGGER.debug("SMS receiver timeout, finishing task");
        }
    }

    private void finishTask(String executionStatus) {
        finishTimeout();
        executionResult.finalizeResult(executionStatus, ConnectionTechnology.MOBILE);

        //CDRs
        ReceiveSmsTaskResult taskResult = (ReceiveSmsTaskResult) executionResult.getResult();
        CDRsManager.getInstance(getApplication()).generateCDRReceivedSMS(receivedTime, executionResult, taskResult);

        SmsMessageManager.getInstance(getApplication()).unregisterSmsReceiver(startTime);
        ownerTest.onTaskExecutionFinished(index, executionResult);
    }
}

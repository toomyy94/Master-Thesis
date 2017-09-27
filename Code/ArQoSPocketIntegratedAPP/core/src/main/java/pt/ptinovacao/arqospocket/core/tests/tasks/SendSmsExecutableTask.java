package pt.ptinovacao.arqospocket.core.tests.tasks;

import android.support.annotation.NonNull;
import android.telephony.SmsManager;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pt.ptinovacao.arqospocket.core.cdrs.CDRsManager;
import pt.ptinovacao.arqospocket.core.error.ErrorMapper;
import pt.ptinovacao.arqospocket.core.messaging.SendSmsListener;
import pt.ptinovacao.arqospocket.core.messaging.SmsMessageManager;
import pt.ptinovacao.arqospocket.core.network.ConnectionTechnology;
import pt.ptinovacao.arqospocket.core.serialization.TaskResolver;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.ExecutableTest;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.SendSmsTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.SendSmsTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.SendSmsExecutionResult;
import pt.ptinovacao.arqospocket.core.utils.NumberUtils;
import pt.ptinovacao.arqospocket.core.utils.ParseNumberUtil;

/**
 * Send SMS executable task.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class SendSmsExecutableTask extends BaseExecutableTask implements SendSmsListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendSmsExecutableTask.class);

    private long startTime, deliverTime;

    private ExecutableTest ownerTest;

    private int index;

    private SendSmsExecutionResult executionResult;

    private Disposable deliverySubscribe;

    private Disposable sendSubscribe;

    public SendSmsExecutableTask(TaskData taskData) {
        super(taskData);
    }

    @Override
    public Observable<BaseTaskExecutionResult> execute(ExecutableTest ownerTest, int index) {
        this.ownerTest = ownerTest;
        this.index = index;
        createExecutionResult(index);

        Single.just(getData().getTaskName()).subscribeOn(Schedulers.newThread()).delay(3, TimeUnit.SECONDS)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String name) throws Exception {
                        startTaskExecution();
                    }
                });

        return Observable.empty();
    }

    @Override
    public BaseTaskExecutionResult fail(String errorCode) {
        createExecutionResult(index);
        executionResult.finalizeResult(errorCode, ConnectionTechnology.MIXED);
        return executionResult;
    }

    private void startTaskExecution() {
        startTime = Calendar.getInstance().getTimeInMillis();

        SendSmsTaskData data = (SendSmsTaskData) getData();
        String messageText = buildMessageText(data);

        if (!Strings.isNullOrEmpty(data.getDestinationNumber()) &&
                NumberUtils.isNumberToSendSms(data.getDestinationNumber())) {
            SmsMessageManager.getInstance(getApplication())
                    .sendSms(data.getDestinationNumber(), data.getSmsCenterNumber(), messageText, this);
        } else {
            finishTask(ErrorMapper.SMS_SEND_INVALID_NUMBER_FORMAT.toString());
            return;
        }

        int timeout = ParseNumberUtil.parseNumber(getData().getTimeout());
        if (timeout > 0) {
            sendSubscribe = Single.just(RESULT_TASK_SUCCESS).subscribeOn(Schedulers.newThread())
                    .delay(timeout, TimeUnit.SECONDS).subscribe(new Consumer<String>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull String result) throws Exception {
                            LOGGER.debug("SMS send timeout, finishing task");
                            finishTask(ErrorMapper.SMS_SEND_TIMEOUT.toString());
                        }
                    });
        }
    }

    @NonNull
    private String buildMessageText(SendSmsTaskData data) {
        String messageId = data.getTrailerText();
        long testId = ((ownerTest.getData().getStartDate().getTime() / 1000) +
                ParseNumberUtil.parseNumber(data.getExecutionDelay())) % 10000;
        long timestampSeconds = startTime / 1000;
        long timestampMillis = startTime % 1000;

        String textMessage = data.getTextMessage();
        textMessage = textMessage.replace(Strings.nullToEmpty(data.getTrailerText()), "");

        if (data.getTrailerMetadata() != null && data.getTrailerMetadata() == 1) {
            return textMessage;
        }

        String[] parts = {
                textMessage + "id=" + messageId + Strings.padStart(String.valueOf(testId), 4, '0'),
                String.valueOf(timestampSeconds),
                Strings.padStart(String.valueOf(timestampMillis), 3, '0')
        };
        String messageText = Joiner.on(' ').join(parts);
        executionResult.updateMessageData(data.getDestinationNumber(), messageText);
        return messageText;
    }

    private void createExecutionResult(int index) {
        TaskResult result = TaskResolver.taskResultForData(getData());
        executionResult = (SendSmsExecutionResult) TaskResolver.executionResultForTaskResult(result);
        executionResult.setExecutionId(index);
        executionResult.updateStartDate(Calendar.getInstance().getTime());
    }

    @Override
    public void onSmsSent(int resultCode) {
        if (resultCode != SmsMessageManager.RESULT_OK) {

            String error = StringUtils.EMPTY;
            switch (resultCode) {
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    error = ErrorMapper.SMS_GENERIC_FAILURE.toString();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    error = ErrorMapper.SMS_NO_SERVICE.toString();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    error = ErrorMapper.SMS_NULL_PDU.toString();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    error = ErrorMapper.SMS_NO_RADIO.toString();
                    break;
            }

            finishTask(error);
        } else {
            deliverTime = Calendar.getInstance().getTimeInMillis();

            long executionTime = Calendar.getInstance().getTimeInMillis() - startTime;
            executionResult.updateExecutionTime(executionTime);

            int timeout = ParseNumberUtil.parseNumber(getData().getTimeout());
            if (timeout > 0) {
                LOGGER.debug("SMS sent, setting timeout for delivery");
                deliverySubscribe = Single.just(RESULT_TASK_SUCCESS).subscribeOn(Schedulers.newThread())
                        .delay(timeout, TimeUnit.SECONDS).subscribe(new Consumer<String>() {
                            @Override
                            public void accept(@io.reactivex.annotations.NonNull String result) throws Exception {
                                LOGGER.debug("SMS send timeout, finishing task");
                                finishTask(ErrorMapper.SMS_SEND_TIMEOUT.toString());
                            }
                        });
            }
        }
    }

    @Override
    public void onSmsDelivered(int resultCode) {
        if (resultCode == SmsMessageManager.RESULT_OK) {
            finishTask(RESULT_TASK_SUCCESS);
        } else {
            finishTask(ErrorMapper.SMS_POSTPONED.toString());
        }
    }

    private synchronized void finishTask(String executionStatus) {

        if (deliverySubscribe != null && !deliverySubscribe.isDisposed()) {
            deliverySubscribe.dispose();
            deliverySubscribe = null;
        }

        if (sendSubscribe != null && !sendSubscribe.isDisposed()) {
            sendSubscribe.dispose();
            sendSubscribe = null;
        }

        executionResult.finalizeResult(executionStatus, ConnectionTechnology.MOBILE);

        //CDRs
        SendSmsTaskResult taskResult = (SendSmsTaskResult) executionResult.getResult();
        CDRsManager.getInstance(getApplication()).generateCDRSendSMS(deliverTime, executionResult, taskResult);

        ownerTest.onTaskExecutionFinished(index, executionResult);
    }
}


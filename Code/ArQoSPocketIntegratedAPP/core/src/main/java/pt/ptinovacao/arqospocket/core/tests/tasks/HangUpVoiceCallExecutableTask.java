package pt.ptinovacao.arqospocket.core.tests.tasks;

import android.provider.CallLog;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pt.ptinovacao.arqospocket.core.error.ErrorMapper;
import pt.ptinovacao.arqospocket.core.network.ConnectionTechnology;
import pt.ptinovacao.arqospocket.core.serialization.TaskResolver;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.type2.TelephonyStateManager;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.ExecutableTest;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.HangUpVoiceCallTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.HangUpVoiceCallExecutionResult;
import pt.ptinovacao.arqospocket.core.utils.ParseNumberUtil;
import pt.ptinovacao.arqospocket.core.voicecall.VoiceCallManager;

/**
 * Hangup voice call executable task.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class HangUpVoiceCallExecutableTask extends BaseExecutableTask implements TelephonyStateManager.UpdateState {

    private static final Logger LOGGER = LoggerFactory.getLogger(HangUpVoiceCallExecutableTask.class);

    public HangUpVoiceCallExecutableTask(TaskData taskData) {
        super(taskData);
    }

    private HangUpVoiceCallExecutionResult hangUpVoiceCallExecutionResult;

    private ExecutableTest ownerTest;

    private int index;

    private long startTime;

    private boolean stateWaiting = false;

    private Disposable hangUpVoiceCallSubscribe;

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

    private void startTaskExecution() {
        startTime = Calendar.getInstance().getTimeInMillis();

        HangUpVoiceCallTaskData data = (HangUpVoiceCallTaskData) getData();

        TelephonyStateManager telephonyStateManager = TelephonyStateManager.getInstance(getApplication());
        telephonyStateManager.registerStateCall(this);

        if (data.getCallsToBeTerminated() != 0 && data.getCallsToBeTerminated() != 1 &&
                data.getCallsToBeTerminated() != 3) {
            finishTask(ErrorMapper.INCORRECT_USAGE.toString());
            return;
        }

        int timeout = ParseNumberUtil.parseNumber(getData().getTimeout());
        LOGGER.debug("Timeout: " + timeout);
        if (timeout > 0) {
            hangUpVoiceCallSubscribe = Single.just(RESULT_TASK_SUCCESS).subscribeOn(Schedulers.newThread())
                    .delay(timeout, TimeUnit.SECONDS).subscribe(new Consumer<String>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull String result) throws Exception {
                            LOGGER.debug("Hang up call, timeout");
                            finishTask(ErrorMapper.CALL_TASK_TIMEOUT.toString());
                        }
                    });
        }

        if (telephonyStateManager.getCallStatus() != TelephonyStateManager.CallStatus.ACTIVE) {
            stateWaiting = true;
            return;
        }

        tryHangUpVoiceCall();
    }

    private synchronized void tryHangUpVoiceCall() {

        if (VoiceCallManager.getInstance(getApplication()).sendKeyToCall() &&
                VoiceCallManager.getInstance(getApplication()).hangUpVoiceCall()) {
            finishTask(RESULT_TASK_SUCCESS);
        } else {
            finishTask(ErrorMapper.PROBE_ERROR_ANSWER_THE_CALL.toString());
        }
    }

    private void createExecutionResult(int index) {
        TaskResult result = TaskResolver.taskResultForData(getData());
        hangUpVoiceCallExecutionResult =
                (HangUpVoiceCallExecutionResult) TaskResolver.executionResultForTaskResult(result);
        hangUpVoiceCallExecutionResult.setExecutionId(index);
        hangUpVoiceCallExecutionResult.updateStartDate(Calendar.getInstance().getTime());
        hangUpVoiceCallExecutionResult.setFixedValue(1);
    }

    @Override
    public BaseTaskExecutionResult fail(String errorCode) {
        TaskResult result = TaskResolver.taskResultForData(getData());
        BaseTaskExecutionResult executionResult = TaskResolver.executionResultForTaskResult(result);
        executionResult.updateStatus(errorCode);
        return executionResult;
    }

    private synchronized void finishTask(String executionStatus) {
        if (hangUpVoiceCallSubscribe != null && !hangUpVoiceCallSubscribe.isDisposed()) {
            hangUpVoiceCallSubscribe.dispose();
            hangUpVoiceCallSubscribe = null;
        }

        TelephonyStateManager telephonyStateManager = TelephonyStateManager.getInstance(getApplication());

        String callNumber = telephonyStateManager.getIncomingNumber();
        if (!Strings.isNullOrEmpty(callNumber)) {
            hangUpVoiceCallExecutionResult.setCallDuration(VoiceCallManager.getInstance(getApplication())
                    .getCallDurationFromLog(callNumber, getcType(telephonyStateManager)));
        }
        telephonyStateManager.unRegisterStateCall();
        hangUpVoiceCallExecutionResult.finalizeResult(executionStatus, ConnectionTechnology.MOBILE);
        ownerTest.onTaskExecutionFinished(index, hangUpVoiceCallExecutionResult);
    }

    private int getcType(TelephonyStateManager voiceCallReceiver) {
        return (voiceCallReceiver.getCallStatus() != null ?
                voiceCallReceiver.getCallInOrOut() == TelephonyStateManager.CallInOrOut.Callee ?
                        CallLog.Calls.INCOMING_TYPE : CallLog.Calls.OUTGOING_TYPE : 0);
    }

    @Override
    public void updateCallStatus() {
        TelephonyStateManager telephonyStateManager = TelephonyStateManager.getInstance(getApplication());
        telephonyStateManager.registerStateCall(this);

        if (stateWaiting && telephonyStateManager.getCallStatus() == TelephonyStateManager.CallStatus.ACTIVE) {
            tryHangUpVoiceCall();
        }
    }
}
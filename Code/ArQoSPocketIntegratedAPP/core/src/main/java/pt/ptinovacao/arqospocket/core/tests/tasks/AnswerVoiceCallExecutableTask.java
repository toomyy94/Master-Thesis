package pt.ptinovacao.arqospocket.core.tests.tasks;

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
import pt.ptinovacao.arqospocket.core.network.ConnectionTechnology;
import pt.ptinovacao.arqospocket.core.serialization.TaskResolver;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.type2.TelephonyStateManager;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.ExecutableTest;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.AnswerVoiceCallTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.AnswerVoiceCallTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.AnswerVoiceCallExecutionResult;
import pt.ptinovacao.arqospocket.core.utils.ParseNumberUtil;
import pt.ptinovacao.arqospocket.core.voicecall.VoiceCallManager;

/**
 * Answer voice call executable task.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class AnswerVoiceCallExecutableTask extends BaseExecutableTask implements TelephonyStateManager.UpdateState {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnswerVoiceCallExecutableTask.class);

    private AnswerVoiceCallExecutionResult answerVoiceCallExecutionResult;

    private ExecutableTest ownerTest;

    private int index;

    private Disposable answerVoiceCallSubscribe;

    private long startTime;

    private boolean stateWaiting = false;

    public AnswerVoiceCallExecutableTask(TaskData taskData) {
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

    private void startTaskExecution() {
        startTime = Calendar.getInstance().getTimeInMillis();

        AnswerVoiceCallTaskData data = (AnswerVoiceCallTaskData) getData();

        TelephonyStateManager telephonyStateManager = TelephonyStateManager.getInstance(getApplication());
        telephonyStateManager.registerStateCall(this);

        if (telephonyStateManager.getCallStatus() != TelephonyStateManager.CallStatus.WAITING) {

            stateWaiting = true;

            int timeout = ParseNumberUtil.parseNumber(getData().getTimeout());
            LOGGER.debug("Timeout: " + timeout);
            if (timeout > 0) {
                answerVoiceCallSubscribe = Single.just(RESULT_TASK_SUCCESS).subscribeOn(Schedulers.newThread())
                        .delay(timeout, TimeUnit.SECONDS).subscribe(new Consumer<String>() {
                            @Override
                            public void accept(@io.reactivex.annotations.NonNull String result) throws Exception {
                                LOGGER.debug("Answer voice call, timeout");
                                finishTask(ErrorMapper.PROBE_ERROR_ANSWER_THE_CALL.toString());
                            }
                        });
            }
        } else {
            tryAnswerVoiceCall();
        }
    }

    private synchronized void tryAnswerVoiceCall() {

        if (VoiceCallManager.getInstance(getApplication()).sendKeyToCall() &&
                VoiceCallManager.getInstance(getApplication()).acceptVoiceCall()) {
            stateWaiting = false;
            finishTask(RESULT_TASK_SUCCESS);
            LOGGER.debug("Success");
        } else {
            LOGGER.debug("!Success");
            finishTask(ErrorMapper.PROBE_ERROR_ANSWER_THE_CALL.toString());
        }
    }

    private void createExecutionResult(int index) {
        TaskResult result = TaskResolver.taskResultForData(getData());
        answerVoiceCallExecutionResult =
                (AnswerVoiceCallExecutionResult) TaskResolver.executionResultForTaskResult(result);
        answerVoiceCallExecutionResult.setExecutionId(index);
        answerVoiceCallExecutionResult.updateStartDate(Calendar.getInstance().getTime());
        answerVoiceCallExecutionResult.updateFixedValue();
    }

    @Override
    public BaseTaskExecutionResult fail(String errorCode) {
        TaskResult result = TaskResolver.taskResultForData(getData());
        BaseTaskExecutionResult executionResult = TaskResolver.executionResultForTaskResult(result);
        executionResult.updateStatus(errorCode);
        return executionResult;
    }

    private synchronized void finishTask(String executionStatus) {
        if (answerVoiceCallSubscribe != null && !answerVoiceCallSubscribe.isDisposed()) {
            answerVoiceCallSubscribe.dispose();
            answerVoiceCallSubscribe = null;
        }

        TelephonyStateManager telephonyStateManager = TelephonyStateManager.getInstance(getApplication());
        telephonyStateManager.unRegisterStateCall();

        answerVoiceCallExecutionResult.setTimeWaitingForRinging(
                Long.valueOf(Calendar.getInstance().getTimeInMillis() - startTime).intValue());
        answerVoiceCallExecutionResult.finalizeResult(executionStatus, ConnectionTechnology.MOBILE);

        //CDRs
        AnswerVoiceCallTaskResult taskResult = (AnswerVoiceCallTaskResult) answerVoiceCallExecutionResult.getResult();
        CDRsManager.getInstance(getApplication()).fillAnswerVoiceTaskResults(answerVoiceCallExecutionResult, taskResult);

        ownerTest.onTaskExecutionFinished(index, answerVoiceCallExecutionResult);
    }

    @Override
    public void updateCallStatus() {
        TelephonyStateManager telephonyStateManager = TelephonyStateManager.getInstance(getApplication());
        if (stateWaiting && telephonyStateManager.getCallStatus() == TelephonyStateManager.CallStatus.WAITING) {
            tryAnswerVoiceCall();
        }
    }
}
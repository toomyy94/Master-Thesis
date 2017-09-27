package pt.ptinovacao.arqospocket.core.tests.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

import io.reactivex.Observable;
import io.reactivex.Single;
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
import pt.ptinovacao.arqospocket.core.tests.data.results.MakeVoiceCallTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.MakeVoiceCallTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.MakeVoiceCallExecutionResult;
import pt.ptinovacao.arqospocket.core.voicecall.VoiceCallManager;

/**
 * Make voice call executable task.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class MakeVoiceCallExecutableTask extends BaseExecutableTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(MakeVoiceCallExecutableTask.class);

    public MakeVoiceCallExecutableTask(TaskData taskData) {
        super(taskData);
    }

    private MakeVoiceCallExecutionResult makeVoiceCallExecutionResult;

    private ExecutableTest ownerTest;

    private int index;

    private long startTime;

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

        MakeVoiceCallTaskData data = (MakeVoiceCallTaskData) getData();

        TelephonyStateManager telephonyStateManager = TelephonyStateManager.getInstance(getApplication());

        if (telephonyStateManager.getCallStatus() == TelephonyStateManager.CallStatus.ACTIVE) {
            finishTask(ErrorMapper.ERROR_PLACING_A_CALL.toString());
            return;
        }

        if (VoiceCallManager.getInstance(getApplication()).makeCallIntent(data.getDestinationNumber())) {
            makeVoiceCallExecutionResult.updateDestinationNumber(data.getDestinationNumber());
            finishTask(RESULT_TASK_SUCCESS);
        } else {
            finishTask(ErrorMapper.SMS_SEND_INVALID_NUMBER_FORMAT.toString());
        }
    }

    private void createExecutionResult(int index) {
        TaskResult result = TaskResolver.taskResultForData(getData());
        makeVoiceCallExecutionResult = (MakeVoiceCallExecutionResult) TaskResolver.executionResultForTaskResult(result);
        makeVoiceCallExecutionResult.setExecutionId(index);
        makeVoiceCallExecutionResult.updateStartDate(Calendar.getInstance().getTime());
        makeVoiceCallExecutionResult.updateFixedValue();
    }

    @Override
    public BaseTaskExecutionResult fail(String errorCode) {
        TaskResult result = TaskResolver.taskResultForData(getData());
        BaseTaskExecutionResult executionResult = TaskResolver.executionResultForTaskResult(result);
        executionResult.updateStatus(errorCode);
        return executionResult;
    }

    private synchronized void finishTask(String executionStatus) {
        makeVoiceCallExecutionResult.updateSetupTime(Calendar.getInstance().getTimeInMillis() - startTime);
        makeVoiceCallExecutionResult.finalizeResult(executionStatus, ConnectionTechnology.MOBILE);

        //CDRs
        MakeVoiceCallTaskResult taskResult = (MakeVoiceCallTaskResult) makeVoiceCallExecutionResult.getResult();
        CDRsManager.getInstance(getApplication()).fillMakeVoiceTaskResults(makeVoiceCallExecutionResult, taskResult);

        ownerTest.onTaskExecutionFinished(index, makeVoiceCallExecutionResult);
    }
}
package pt.ptinovacao.arqospocket.core.tests.tasks;

import java.util.Calendar;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import pt.ptinovacao.arqospocket.core.network.PingManager;
import pt.ptinovacao.arqospocket.core.network.PingResult;
import pt.ptinovacao.arqospocket.core.serialization.TaskResolver;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.ExecutableTest;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.PingTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.PingExecutionResult;

/**
 * Ping executable task.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class PingExecutableTask extends BaseExecutableTask {

    private PingExecutionResult executionResult;

    private final PingTaskData taskData;

    public PingExecutableTask(TaskData taskData) {
        super(taskData);
        this.taskData = (PingTaskData) taskData;
    }

    @Override
    public Observable<BaseTaskExecutionResult> execute(ExecutableTest ownerTest, int index) {
        createExecutionResult();
        executionResult.setExecutionId(index);

        return Observable.fromCallable(new Callable<BaseTaskExecutionResult>() {
            @Override
            public BaseTaskExecutionResult call() throws Exception {
                executionResult.updateStartDate(Calendar.getInstance().getTime());

                int packetSize = parse(taskData.getPacketSize());
                int packetNumber = parse(taskData.getPacketNumber());
                int interval = parse(taskData.getInterval());
                int timeout = parse(taskData.getPingTimeout());
                PingResult pingResult =
                        PingManager.ping(taskData.getIpAddress(), packetSize, packetNumber, interval, timeout);

                String status = pingResult.getResult() == 0 ? RESULT_TASK_SUCCESS : RESULT_TASK_FAILED;
                executionResult.updatePingData(pingResult, status);

                return executionResult;
            }
        });
    }

    @Override
    public BaseTaskExecutionResult fail(String errorCode) {
        createExecutionResult();
        executionResult.updatePingData(null, errorCode);
        return executionResult;
    }

    private void createExecutionResult() {
        TaskResult result = TaskResolver.taskResultForData(getData());
        executionResult = (PingExecutionResult) TaskResolver.executionResultForTaskResult(result);
    }

    private int parse(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }
}


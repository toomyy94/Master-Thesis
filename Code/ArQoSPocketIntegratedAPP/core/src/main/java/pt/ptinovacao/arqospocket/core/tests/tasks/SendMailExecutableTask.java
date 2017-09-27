package pt.ptinovacao.arqospocket.core.tests.tasks;

import io.reactivex.Observable;
import pt.ptinovacao.arqospocket.core.serialization.TaskResolver;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.ExecutableTest;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;

/**
 * Send mail executable task.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class SendMailExecutableTask extends BaseExecutableTask {

    public SendMailExecutableTask(TaskData taskData) {
        super(taskData);
    }

    @Override
    public Observable<BaseTaskExecutionResult> execute(ExecutableTest ownerTest, int index) {
        // TODO : TEST CODE ONLY - change when implementing the tasks properly
        TaskResult result = TaskResolver.taskResultForData(getData());
        BaseTaskExecutionResult executionResult = TaskResolver.executionResultForTaskResult(result);
        executionResult.setExecutionId(index);
        return Observable.just(executionResult);
    }

    @Override
    public BaseTaskExecutionResult fail(String errorCode) {
        TaskResult result = TaskResolver.taskResultForData(getData());
        BaseTaskExecutionResult executionResult = TaskResolver.executionResultForTaskResult(result);
        executionResult.updateStatus(errorCode);
        return executionResult;
    }
}

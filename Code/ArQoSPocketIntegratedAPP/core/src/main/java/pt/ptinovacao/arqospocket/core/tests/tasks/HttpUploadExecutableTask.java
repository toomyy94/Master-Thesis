package pt.ptinovacao.arqospocket.core.tests.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pt.ptinovacao.arqospocket.core.http.TestHttpClient;
import pt.ptinovacao.arqospocket.core.http.TestHttpResponse;
import pt.ptinovacao.arqospocket.core.http.TestHttpResponseStatus;
import pt.ptinovacao.arqospocket.core.serialization.TaskResolver;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.ExecutableTest;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.HttpUploadTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.HttpUploadExecutionResult;

/**
 * HTTP upload executable task.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class HttpUploadExecutableTask extends BaseExecutableTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUploadExecutableTask.class);

    private ExecutableTest ownerTest;

    private int index;

    private HttpUploadExecutionResult executionResult;

    public HttpUploadExecutableTask(TaskData taskData) {
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
    public BaseTaskExecutionResult fail(String errorCode) {
        createExecutionResult(index);
        executionResult.updateUploadData(null, errorCode);
        return executionResult;
    }

    private void startTaskExecution() {
        LOGGER.debug("Starting HTTP Upload execution");

        Calendar calendar = Calendar.getInstance();
        executionResult.updateStartDate(calendar.getTime());

        HttpUploadTaskData data = (HttpUploadTaskData) getData();

        TestHttpClient client = new TestHttpClient();
        TestHttpResponse response = client.uploadData(data.getUrl(), data.getProxy(), data.getContent());

        String status = response.getStatus() == TestHttpResponseStatus.OK ? RESULT_TASK_SUCCESS : RESULT_TASK_FAILED;
        executionResult.updateUploadData(response, status);
        ownerTest.onTaskExecutionFinished(index, executionResult);
    }

    private void createExecutionResult(int index) {
        TaskResult result = TaskResolver.taskResultForData(getData());
        executionResult = (HttpUploadExecutionResult) TaskResolver.executionResultForTaskResult(result);
        executionResult.setExecutionId(index);
    }
}


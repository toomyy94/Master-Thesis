package pt.ptinovacao.arqospocket.core.tests;

import io.reactivex.Observable;
import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;

/**
 * Represents each one of the tasks from a test. A test is free to manage the task execution and how they coordinate.
 * <p>
 * Created by Emílio Simões on 06-04-2017.
 */
public abstract class BaseExecutableTask {

    public static final String RESULT_TASK_SUCCESS = "OK";

    public static final String RESULT_TASK_FAILED = "NOK";

    private final TaskData taskData;

    private CoreApplication application;

    protected BaseExecutableTask(TaskData taskData) {
        this.taskData = taskData;
    }

    /**
     * Gets the task data.
     *
     * @return the task data.
     */
    public TaskData getData() {
        return taskData;
    }

    protected CoreApplication getApplication() {
        return application;
    }

    protected void setApplication(CoreApplication application) {
        this.application = application;
    }

    /**
     * Starts the task execution and returns an {@link Observable<BaseTaskExecutionResult>} that will deliver the
     * execution result.
     *
     * @param ownerTest the test that owns and is executing the task. This test can be used to deliver the response in
     * case the observable is not a viable approach.
     * @param index the index
     * @return an {@link Observable<BaseTaskExecutionResult>} that will deliver the execution result.
     */
    public abstract Observable<BaseTaskExecutionResult> execute(ExecutableTest ownerTest, int index);

    public abstract BaseTaskExecutionResult fail(String errorCode);
}

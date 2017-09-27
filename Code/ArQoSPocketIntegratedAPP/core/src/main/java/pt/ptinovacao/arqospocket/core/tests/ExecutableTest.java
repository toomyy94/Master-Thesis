package pt.ptinovacao.arqospocket.core.tests;

import android.util.SparseArray;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.TestExecutor;
import pt.ptinovacao.arqospocket.core.error.ErrorMapper;
import pt.ptinovacao.arqospocket.core.location.GeoLocationManager;
import pt.ptinovacao.arqospocket.core.network.MobileNetworkManager;
import pt.ptinovacao.arqospocket.core.network.WifiNetworkManager;
import pt.ptinovacao.arqospocket.core.network.data.RadioInfo;
import pt.ptinovacao.arqospocket.core.serialization.TaskResolver;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TestData;

/**
 * Base executable test. An executable test is a test that can be run in the test executor or scheduled in the test
 * scheduler. The data for the test is provided by a test data entity that is supplied by a test provider.
 * <p>
 * Created by Emílio Simões on 06-04-2017.
 */
public class ExecutableTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutableTest.class);

    private TestData data;

    private TestExecutionType testType;

    private Date executionDate;

    private List<BaseExecutableTask> tasks;

    private TestExecutor executor;

    private long testId;

    private TestExecutionResult testResult;

    private SparseArray<Holder> pendingTasks = new SparseArray<>();

    private CoreApplication application;

    private long executionStart;

    private boolean cancelled = false;

    private ExecutableTest(CoreApplication application) {
        this.application = application;
    }

    public TestData getData() {
        return data;
    }

    private void setData(TestData data) {
        this.data = data;
    }

    public TestExecutionType getTestType() {
        return testType;
    }

    private void setTestType(TestExecutionType testType) {
        this.testType = testType;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    private void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public List<BaseExecutableTask> getTasks() {
        return tasks;
    }

    private void setTasks(List<BaseExecutableTask> tasks) {
        this.tasks = tasks;
    }

    /**
     * Starts the test execution and returns an {@link Observable<TestExecutionResult>} that will deliver the execution
     * result.
     *
     * @param executor the executor executing the test. This executor can be used to deliver the result in case the
     * observable is not a viable approach.
     * @param testId the test ID in the executor. The test must return this ID upon completion.
     * @return an {@link Observable<TestExecutionResult>} that will deliver the execution result.
     */
    public Observable<TestExecutionResult> execute(TestExecutor executor, long testId) {
        this.executor = executor;
        this.testId = testId;
        testResult = new TestExecutionResult(testId);

        LOGGER.debug("Executing test with ID = {}", testId);

        Observable<TestExecutionResult> result;
        if (tasks == null || tasks.size() == 0) {
            result = Observable.just(testResult);
        } else {
            result = Observable.create(new ObservableOnSubscribe<TestExecutionResult>() {
                @Override
                public void subscribe(ObservableEmitter<TestExecutionResult> e) throws Exception {
                    executionStart = Calendar.getInstance().getTimeInMillis();
                    processTask(0);
                }
            });
        }

        return result.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public TestExecutionResult fail(ErrorMapper errorMapper) {

        testResult = new TestExecutionResult(0);
        failAllTasks(0, errorMapper);
        return testResult;
    }

    /**
     * Creates an instance of the executable test from an {@link TestData}.
     *
     * @param data the test data.
     * @param type the execution type.
     * @param application he application context.
     * @return the created instance.
     */
    public static ExecutableTest createFromData(TestData data, TestExecutionType type, CoreApplication application) {
        ExecutableTest test = new ExecutableTest(application);
        test.setData(data);
        test.setTestType(type);

        Date executionDate = data.getStartDate();
        TaskData[] tasks = data.getTasksData();
        if (tasks != null && tasks.length > 0) {
            if (!tasks[0].executeImmediately() && tasks[0].delay() > 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(executionDate.getTime() + tasks[0].delay());
                executionDate = calendar.getTime();
            }
            setTasks(test, data.getTasksData(), application);
        }
        test.setExecutionDate(executionDate);
        return test;
    }

    public void onTaskExecutionFinished(final int index, BaseTaskExecutionResult result) {
        LOGGER.debug("Task with ID = {} has executed", index);

        if (index < 0 || pendingTasks.indexOfKey(index) < 0) {
            throw new IllegalArgumentException(index + " is not an active task");
        }

        LOGGER.debug("Task execution cleanup finished");

        result.updateLocation(GeoLocationManager.getInstance(application).getLocationInfo());
        RadioInfo mobileInfo = MobileNetworkManager.getInstance(application).getNetworkInfo();
        RadioInfo wifiInfo = WifiNetworkManager.getInstance(application).getWifiInfo();
        result.updateCellId(mobileInfo.mergeAndFormat(wifiInfo));
        result.updateTaskData(tasks.get(index));
        testResult.addTaskResult(result);

        pendingTasks.get(index).subscription.dispose();
        pendingTasks.remove(index);

        executor.onTaskExecutionFinished(this.testId, this.data.getTestId(), tasks.get(index).getData().getTaskId(),
                testResult, result).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean result) throws Exception {
                processTask(index + 1);
            }
        });
    }

    public synchronized void cancelExecution() {
        this.cancelled = true;
    }

    private synchronized boolean isCancelled() {
        return cancelled;
    }

    private void processTask(int index) {

        if (tasks.size() <= index) {
            LOGGER.debug("No more tasks or test was cancelled, exiting");
            finishTestExecution();
        } else if (isCancelled()) {

            failAllTasks(index, ErrorMapper.TASK_EXECUTION_CANCELED);
        } else {
            LOGGER.debug("Executing task with ID = {}", index);

            BaseExecutableTask executableTask = tasks.get(index);
            TaskData data = executableTask.getData();
            if (Strings.nullToEmpty(data.getImmediate()).equals("1")) {
                LOGGER.debug("Executing immediate task");
                runTask(index, executableTask);
            } else {
                int delay = calculateDelay(index, executableTask);
                if (delay > 0) {
                    LOGGER.debug("Executing delayed task with {} seconds delay remaining", delay);
                    Single.just(new DelayedTask(index, executableTask)).delay(delay, TimeUnit.SECONDS)
                            .subscribe(new Consumer<DelayedTask>() {
                                @Override
                                public void accept(@NonNull DelayedTask value) throws Exception {
                                    runTask(value.index, value.executableTask);
                                }
                            });
                } else {
                    LOGGER.debug("Executing delayed task with no delay");
                    runTask(index, executableTask);
                }
            }
        }
    }

    private void failAllTasks(int index, ErrorMapper status) {
        for (int i = index; i < tasks.size(); i++) {
            BaseExecutableTask executableTask = tasks.get(index);
            BaseTaskExecutionResult executionResult = executableTask.fail(status.toString());
            executionResult.updateTaskData(executableTask);
            testResult.addTaskResult(executionResult);
        }
        finishTestExecution();
    }

    private int calculateDelay(int index, BaseExecutableTask executableTask) {
        TaskData data = executableTask.getData();
        int taskDelay = parseDelayFromString(data.getExecutionDelay());
        if (index == 0 || taskDelay <= 0) {
            return taskDelay;
        }

        long taskDelayInMillis = taskDelay * 1000;
        long targetTime = executionStart + taskDelayInMillis;
        long now = Calendar.getInstance().getTimeInMillis();
        long delay = (targetTime - now) / 1000;
        if (delay > 0) {
            return (int) delay;
        }

        return 0;
    }

    private int parseDelayFromString(String executionDelay) {
        if (Strings.nullToEmpty(executionDelay).length() == 0) {
            return 0;
        }

        try {
            return Integer.parseInt(executionDelay);
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid execution delay: {}", executionDelay);
        }
        return 0;
    }

    private void runTask(int index, BaseExecutableTask executableTask) {
        Observable<BaseTaskExecutionResult> observable =
                executableTask.execute(this, index).subscribeOn(Schedulers.newThread());
        Disposable subscription = observable.subscribe(new Consumer<BaseTaskExecutionResult>() {
            @Override
            public void accept(@NonNull BaseTaskExecutionResult result) throws Exception {
                onTaskExecutionFinished(result.getExecutionId(), result);
            }
        });
        pendingTasks.append(index, new Holder(observable, subscription));
        executor.onTaskExecutionStarted(this.testId, this.data.getTestId(), executableTask.getData().getTaskId());
    }

    private void finishTestExecution() {
        testResult.finishExecution(data);
        try {
            if (executor != null) {
                executor.onTestExecutionFinished(testId, testResult);
            }
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Test is not running anymore, possible timeout not cleared");
        }
    }

    private static void setTasks(ExecutableTest test, TaskData[] tasksData, CoreApplication application) {
        List<BaseExecutableTask> tasks = new ArrayList<>();
        for (TaskData data : tasksData) {
            BaseExecutableTask task = TaskResolver.executableTaskForTaskData(data);
            if (task != null) {
                task.setApplication(application);
                tasks.add(task);
            }
        }
        test.setTasks(ImmutableList.copyOf(tasks));
    }

    private class Holder {

        final Observable<BaseTaskExecutionResult> observable;

        final Disposable subscription;

        Holder(Observable<BaseTaskExecutionResult> observable, Disposable subscription) {
            this.observable = observable;
            this.subscription = subscription;
        }
    }

    private class DelayedTask {

        final int index;

        final BaseExecutableTask executableTask;

        DelayedTask(int index, BaseExecutableTask executableTask) {
            this.index = index;
            this.executableTask = executableTask;
        }
    }
}

package pt.ptinovacao.arqospocket.core;

import android.support.v4.util.LongSparseArray;

import com.google.common.base.Strings;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import pt.ptinovacao.arqospocket.core.error.ErrorMapper;
import pt.ptinovacao.arqospocket.core.location.LocationInfo;
import pt.ptinovacao.arqospocket.core.network.ConnectionTechnology;
import pt.ptinovacao.arqospocket.core.notify.TestNotificationManager;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.type73.ConfigureOccupationReportRequest;
import pt.ptinovacao.arqospocket.core.settings.SharedPreferencesManager;
import pt.ptinovacao.arqospocket.core.ssh.AttachmentsProcessManager;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.ExecutableTest;
import pt.ptinovacao.arqospocket.core.tests.TestExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.TestType;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.TestData;
import pt.ptinovacao.arqospocket.core.utils.IntegerUtils;
import pt.ptinovacao.arqospocket.core.utils.WeekEventUtil;
import pt.ptinovacao.arqospocket.persistence.ExecutingEventDao;
import pt.ptinovacao.arqospocket.persistence.models.ExecutingEvent;
import pt.ptinovacao.arqospocket.persistence.models.TaskEvent;

/**
 * Class responsible to execute the tests. This class has a single responsibility, to start the test execution and
 * receive the results. The tests themselves are independent and self contained entities that define their own lifecycle
 * and return a result asynchronously at the end of the execution.
 * <p>
 * Created by Emílio Simões on 06-04-2017.
 */
public class TestExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestExecutor.class);

    private static TestExecutor instance;

    private final CoreApplication application;

    private LongSparseArray<Holder> pendingTests = new LongSparseArray<>();

    private TestExecutor(CoreApplication application) {
        this.application = application;
    }

    public synchronized static TestExecutor getInstance(CoreApplication application) {
        if (instance == null) {
            instance = new TestExecutor(application);
        }
        return instance;
    }

    public void onTestExecutionFinished(long testId, TestExecutionResult result) {
        if (testId <= 0 || pendingTests.indexOfKey(testId) < 0) {
            throw new IllegalArgumentException(testId + " is not an active test");
        }

        Holder holder = pendingTests.get(testId);
        TestData data = holder.testData;
        holder.subscription.dispose();
        pendingTests.remove(testId);
        LOGGER.debug("Test execution cleanup finished");

        flagTestHasExecuted(testId, result).subscribe(new Consumer<ExecutingEvent>() {
            @Override
            public void accept(@NonNull ExecutingEvent executingEvent) throws Exception {
                LOGGER.debug("Test flagged has executed, cleanup done!");
                if (SharedPreferencesManager.getInstance(application).getConnectionWithMSManual()) {
                    ResultsManager.getInstance(application).deliverSingleResult(executingEvent);
                }
            }
        });
        application.getOnDemandTestManager().finishOnDemandTestExecution(result.getResult().getTestId());
        TestNotificationManager.getInstance(application).notifyTestFinished(testId, result.getResult().getTestId());
        if (!holder.isCancelled()) {
            LOGGER.debug("Refreshing test {} reschedule", testId);

            TestConsumer.getInstance(application).validateAfterExecution(data);

        } else {
            LOGGER.debug("Test {} reschedule was not executed", testId);
        }
    }

    public void execute(final ExecutableTest test) {

        if (!validateTimeUtilUntilEndTest(test)) {
            checkTestsInHistoricAndCreate(test);
            return;
        }

        if (!validateIfItsOnTheTime(test)) {
            createFailedExecutingTest(test, ErrorMapper.TASK_EXECUTION_EXCEEDED).subscribeOn(Schedulers.newThread())
                    .subscribe(new Consumer<ExecutingEvent>() {
                        @Override
                        public void accept(@NonNull ExecutingEvent executingEvent) throws Exception {
                            LOGGER.debug("Failed test create");
                            createFailedTasks(executingEvent, test);
                            if (checkTestsInHistoricAndCreate(test)) {
                                TestConsumer.getInstance(application).validateAfterExecution(test.getData());
                            }
                        }
                    });
            return;
        }

        LOGGER.debug("Executing test validate");

        createExecutingTest(test).subscribe(new Consumer<ExecutingEvent>() {
            @Override
            public void accept(@NonNull ExecutingEvent executingEvent) throws Exception {
                LOGGER.debug("Test persisted, starting test => {}", executingEvent.getId());
                startTestExecution(test, executingEvent.getId());
                checkTestsInHistoricAndCreate(test);
            }
        });
    }

    private synchronized boolean checkTestsInHistoricAndCreate(final ExecutableTest test) {

        LOGGER.debug("checkTestsInHistoric");

        ExecutingEventDao executingEventDao = application.getDatabaseHelper().createExecutingEventDao();

        int numberTestCalculated = createTimeProvidedSchedule(test);

        int numberTestSavedWithFalse = executingEventDao.countTestsWithFalse(test.getData().getTestId());
        if (numberTestSavedWithFalse == 0) {
            LOGGER.debug("checkTestsInHistoric::It already exists in the database: " + numberTestSavedWithFalse);
            return false;
        }
        List<ExecutingEvent> executingEvents = executingEventDao.readAllExecutedEventsById(test.getData().getTestId());

        if (test.getData().getEndDate().getTime() < Calendar.getInstance().getTimeInMillis()) {
            if (numberTestCalculated == numberTestSavedWithFalse) {
                executingEventDao.setAllTestExecuted(test.getData().getTestId());
                LOGGER.debug("checkTestsInHistoric::It already exists in the database, but was set");
            } else {
                int numberTestInLack = numberTestCalculated - executingEvents.size();
                LOGGER.debug("checkTestsInHistoric::Added {} elements.", numberTestInLack);

                for (Integer i = 0; i < numberTestInLack; i++) {
                    ExecutableTest executableTest =
                            ExecutableTest.createFromData(test.getData(), test.getTestType(), application);

                    LOGGER.debug("checkTestsInHistoric::" + executableTest.getData().toString());

                    Calendar instance = Calendar.getInstance();
                    instance.add(i * 1000,Calendar.MILLISECOND);

                    Date executionDate = instance.getTime();

                    executableTest.getData().setStartDate(executionDate);
                    LOGGER.debug("checkTestsInHistoric::" + i);
                    executableTest.getData().setEndDate(executionDate);
                    LOGGER.debug("checkTestsInHistoric::" + executableTest.getData().toString());
                    ExecutingEvent executingEvent =
                            createFailedExecutingTest(executableTest, ErrorMapper.TASK_NOT_EXECUTION_SNOOZING)
                                    .blockingGet();
                    createFailedTasks(executingEvent, executableTest);
                }
                executingEventDao.setAllTestExecuted(test.getData().getTestId());
            }
        }
        return true;
    }

    private int createTimeProvidedSchedule(ExecutableTest test) {
        ExecutingEventDao executingEventDao = application.getDatabaseHelper().createExecutingEventDao();
        List<ExecutingEvent> executingEvents = executingEventDao.readAllExecutedEventsById(test.getData().getTestId());

        long dateStart;
        if (executingEvents.size() > 0) {
            dateStart = executingEvents.get(0).getStartDate();
        } else {
            dateStart = test.getData().getStartDate().getTime();
        }

        int numberIteration = 0;
        int countTimeTasks = ConfigureOccupationReportRequest.getCountTimeTasks(test.getData()) * 1000;
        long dateEndProvided = 0L;

        if (test.getData().getRecursion() != null && test.getData().getRecursion().getParameters() != null) {

            long dateActual = dateStart;
            TestType testType = TestType.fromInt(test.getData().getRecursion().getEvent(), TestType.NONE);
            while ((test.getData().getEndDate().getTime() / 1000) > (dateEndProvided / 1000)) {
                switch (testType) {
                    case ITERATIONS: {

                        dateEndProvided = dateActual + countTimeTasks;
                        break;
                    }
                    case TIME_INTERVAL: {
                        long timeMoreInterval = test.getData().getRecursion().getParameters().getInterval() * 1000;
                        dateEndProvided = dateActual + timeMoreInterval;
                        break;
                    }
                    case WEEK:
                        dateActual = WeekEventUtil
                                .getNextExecutionDate(dateActual, test.getData().getRecursion().getParameters())
                                .getTime();
                        dateEndProvided = dateActual + countTimeTasks;
                        break;
                    default:
                        return numberIteration;
                }
                dateActual = dateEndProvided;
                numberIteration++;
            }
        } else {
            numberIteration++;
        }
        return numberIteration;
    }

    private void createFailedTasks(@NonNull ExecutingEvent executingEvent, ExecutableTest test) {
        ExecutingEventDao executingEventDao = application.getDatabaseHelper().createExecutingEventDao();

        LocationInfo locationInfo = new LocationInfo();

        int i = 0;
        while (i++ < test.getData().getTasksData().length) {

            TaskEvent taskEvent = new TaskEvent();
            taskEvent.setStartDate(Calendar.getInstance().getTimeInMillis());
            taskEvent.setEndDate(Calendar.getInstance().getTimeInMillis());
            taskEvent.setLongitude(locationInfo.getLongitude());
            taskEvent.setLatitude(locationInfo.getLatitude());
            taskEvent.setConnectionTechnology(ConnectionTechnology.NA.getValue());

            executingEventDao.createExecutionStatus(executingEvent.getId(), taskEvent)
                    .subscribe(new Consumer<ExecutingEvent>() {
                        @Override
                        public void accept(@NonNull ExecutingEvent executingEvent) throws Exception {
                            LOGGER.debug("Created test");
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            LOGGER.debug("Created test");
                        }
                    });
        }
    }

    private boolean validateTimeUtilUntilEndTest(ExecutableTest test) {
        int sumTimeout = 0;

        for (BaseExecutableTask baseExecutableTask : test.getTasks()) {
            sumTimeout += IntegerUtils.parseInt(baseExecutableTask.getData().getTimeout());
        }

        long dateFinalTest = test.getData().getEndDate().getTime();
        long timeMin = dateFinalTest - (sumTimeout * 1000);

        return (timeMin / 1000) >= (Calendar.getInstance().getTimeInMillis() / 1000);
    }

    private boolean validateIfItsOnTheTime(ExecutableTest test) {
        return DateUtils.truncate(test.getExecutionDate(), Calendar.SECOND)
                .equals(DateUtils.truncate(Calendar.getInstance().getTime(), Calendar.SECOND));
    }

    public void onTaskExecutionStarted(long testId, String testDataId, String taskDataId) {
        TestNotificationManager.getInstance(application).notifyTaskStarted(testId, testDataId, taskDataId);

        ExecutingEventDao executingEventDao = application.getDatabaseHelper().createExecutingEventDao();

        TaskEvent taskEvent = new TaskEvent();
        taskEvent.setStartDate(Calendar.getInstance().getTimeInMillis());
        taskEvent.setTaskDataId(taskDataId);

        executingEventDao.createExecutionStatus(testId, taskEvent).subscribe(new Consumer<ExecutingEvent>() {
            @Override
            public void accept(@NonNull ExecutingEvent executingEvent) throws Exception {
                LOGGER.debug("Created test");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                LOGGER.debug("Created test");
            }
        });
    }

    public Single<Boolean> onTaskExecutionFinished(final long testId, final String testDataId, final String taskDataId,
            final TestExecutionResult result, final BaseTaskExecutionResult taskResult) {

        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(final SingleEmitter<Boolean> e) throws Exception {
                updateExecutionStatus(testId, result, taskResult, taskDataId, taskResult.getNameFileAttachments())
                        .subscribe(new Consumer<ExecutingEvent>() {
                            @Override
                            public void accept(@NonNull ExecutingEvent executingEvent) throws Exception {
                                TestNotificationManager.getInstance(application)
                                        .notifyTaskFinished(testId, testDataId, taskDataId);
                                LOGGER.debug("Updated test result");
                                e.onSuccess(true);

                                if (!Strings.isNullOrEmpty(taskResult.getNameFileAttachments())) {
                                    AttachmentsProcessManager.startSendAttachment(application);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                e.onError(throwable);
                            }
                        });
            }
        });
    }

    public boolean isTestExecuting(long id) {
        return pendingTests.indexOfKey(id) >= 0;
    }

    void disposeIfExecuting(String testId) {
        for (int i = 0; i < pendingTests.size(); i++) {
            Holder holder = pendingTests.valueAt(i);
            if (holder.isSameTest(testId)) {
                LOGGER.debug("Test {} is executing, cancelling test reschedule", testId);
                holder.cancelReValidation();
            }
        }
    }

    private void startTestExecution(final ExecutableTest test, final long testId) {
        Observable<TestExecutionResult> observable = test.execute(this, testId);
        Disposable subscription = observable.subscribe(new Consumer<TestExecutionResult>() {
            @Override
            public void accept(@NonNull final TestExecutionResult result) throws Exception {
                LOGGER.debug("Test execution finished, cleaning up");
                try {
                    onTestExecutionFinished(testId, result);
                } catch (IllegalArgumentException e) {
                    LOGGER.warn("Test is not running anymore, possible timeout not cleared");
                }
            }
        });
        pendingTests.append(testId, new Holder(observable, subscription, test.getData(), test));
        TestNotificationManager.getInstance(application).notifyTestStarted(testId, test.getData().getTestId());
    }

    private Single<ExecutingEvent> createExecutingTest(ExecutableTest test) {
        TestParser parser = new TestParser();
        String testData = parser.stringify(test.getData(), false);

        ExecutingEvent executingEvent = new ExecutingEvent();
        executingEvent.setTestData(testData);
        executingEvent.setTestId(test.getData().getTestId());

        ExecutingEventDao executingEventDao = application.getDatabaseHelper().createExecutingEventDao();
        return executingEventDao.saveNewExecutingTest(executingEvent);
    }

    private Single<ExecutingEvent> createFailedExecutingTest(ExecutableTest test, ErrorMapper errorMapper) {
        TestParser parser = new TestParser();
        String testData = parser.stringify(test.getData(), false);

        ExecutingEvent executingEvent = new ExecutingEvent();
        executingEvent.setTestData(testData);
        executingEvent.setTestId(test.getData().getTestId());

        TestExecutionResult fail = test.fail(errorMapper);

        String resultData = parser.stringify(fail.getResult());

        ExecutingEventDao executingEventDao = application.getDatabaseHelper().createExecutingEventDao();
        return executingEventDao.saveNewFailedExecutingTest(executingEvent, resultData);
    }

    private Single<ExecutingEvent> flagTestHasExecuted(long testId, TestExecutionResult result) {
        ExecutingEventDao executingEventDao = application.getDatabaseHelper().createExecutingEventDao();

        TestParser parser = new TestParser();
        String testResultJson = parser.stringify(result.getResult(), false);

        boolean failed = Flowable.fromArray(result.getResult().getTaskResults()).any(new Predicate<TaskResult>() {
            @Override
            public boolean test(@NonNull TaskResult result) throws Exception {
                return BaseExecutableTask.RESULT_TASK_FAILED.equals(result.getStatus());
            }
        }).blockingGet();

        return executingEventDao.flagTestAsExecuted(testId, testResultJson, failed);
    }

    private Single<ExecutingEvent> updateExecutionStatus(long testId, TestExecutionResult result,
            BaseTaskExecutionResult taskResult, String taskDataId, String nameFile) {
        ExecutingEventDao executingEventDao = application.getDatabaseHelper().createExecutingEventDao();

        TestParser parser = new TestParser();
        String testResult = parser.stringify(result.getResult(), false);

        TaskEvent taskEvent = new TaskEvent();
        taskEvent.setConnectionTechnology(getConnectionTechnology(taskResult).getValue());
        taskEvent.setLatitude(taskResult.getLocationInfo().getLatitude());
        taskEvent.setLongitude(taskResult.getLocationInfo().getLongitude());

        return executingEventDao.updateExecutionStatus(testId, testResult, taskEvent, taskDataId, nameFile);
    }

    private ConnectionTechnology getConnectionTechnology(BaseTaskExecutionResult taskResult) {
        if (taskResult.getConnectionTechnology() == null) {
            return ConnectionTechnology.NA;
        }
        return taskResult.getConnectionTechnology();
    }

    private class Holder {

        final Observable<TestExecutionResult> observable;

        final Disposable subscription;

        final TestData testData;

        private final ExecutableTest test;

        private boolean cancelled;

        private final Object locker = new Object();

        Holder(Observable<TestExecutionResult> observable, Disposable subscription, TestData testData,
                ExecutableTest test) {
            this.observable = observable;
            this.subscription = subscription;
            this.testData = testData;
            this.test = test;
            cancelled = false;
        }

        boolean isSameTest(String testId) {
            return testData != null &&
                    Strings.nullToEmpty(testId).trim().equals(Strings.nullToEmpty(testData.getTestId()).trim());
        }

        boolean isCancelled() {
            synchronized (locker) {
                return cancelled;
            }
        }

        void cancelReValidation() {
            synchronized (locker) {
                cancelled = true;
                test.cancelExecution();
            }
        }
    }

    public boolean isAnyTestExecuting() {
        return pendingTests.size() > 0;
    }
}

package pt.ptinovacao.arqospocket.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

import pt.ptinovacao.arqospocket.core.alarms.AlarmType;
import pt.ptinovacao.arqospocket.core.alarms.AlarmUtils;
import pt.ptinovacao.arqospocket.core.alarms.AlarmsManager;
import pt.ptinovacao.arqospocket.core.notify.TestNotificationManager;
import pt.ptinovacao.arqospocket.core.tests.ExecutableTest;
import pt.ptinovacao.arqospocket.core.tests.TestExecutionType;
import pt.ptinovacao.arqospocket.core.tests.TestType;
import pt.ptinovacao.arqospocket.core.tests.data.ParameterData;
import pt.ptinovacao.arqospocket.core.tests.data.RecursionData;
import pt.ptinovacao.arqospocket.core.tests.data.TestData;
import pt.ptinovacao.arqospocket.core.utils.WeekEventUtil;

/**
 * A test scheduler is responsible to evaluate if a test execution is to be delayed or can be executed immediately. All
 * the tests should be delivered by this service that will evaluate the test status and redirect it to the proper
 * channel.
 * <p>
 * Created by Emílio Simões on 06-04-2017.
 */

class TestScheduler extends TestConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestScheduler.class);

    private final TestAlarmManager alarmManager;

    TestScheduler(CoreApplication application) {
        super(application);
        alarmManager = new TestAlarmManager(application);
    }

    @Override
    public void consume(TestData data, TestExecutionType executionType) {

        if (executionType == TestExecutionType.USER_REQUEST || executionType == TestExecutionType.ALARM_REQUEST) {
            executeOnDemandTest(data, executionType);
        } else {
            validateInputTest(data, executionType);
        }
        TestNotificationManager.getInstance(getApplication()).notifyTestChanged(0L, data.getTestId());
    }

    @Override
    public void cancel(TestData data) {
        clearScheduledTest(data);
        clearOnDemandTest(data);
        AlarmsManager.getInstance(getApplication()).generateAlarm(AlarmUtils.INICIO, AlarmType.A068.name(), AlarmType.A068.getAlarmContent(),
                "Name: " + data.getTestName() + " ID: " + data.getTestId() + " Agent: " + AlarmUtils.SG);
        TestNotificationManager.getInstance(getApplication()).notifyTestChanged(0L, data.getTestId());
    }

    @Override
    public boolean validateAfterExecution(TestData data) {
        LOGGER.debug("Validating next execution");
        TestType endEvent = TestType.fromInt(data.getEndEvent(), TestType.NONE);
        switch (endEvent) {
            case DATE:
            case USER_REQUEST:
            case NONE: {
                LOGGER.debug("Recursive test with DATE, USER_REQUEST or NONE type, validating end date ");
                if (data.getEndDate().getTime() > Calendar.getInstance().getTimeInMillis()) {
                    LOGGER.debug("Recursive test with DATE, USER_REQUEST or NONE type, validating recursion");
                    return validateRecursion(data);
                }
                break;
            }
            case ITERATIONS: {
                LOGGER.debug("Recursive test with ITERATIONS type, validating next iteration");
                if (hasNextIteration(data)) {
                    LOGGER.debug("Recursive test with ITERATIONS type, validating next recursion");
                    return validateRecursion(data);
                }
                break;
            }
            case BOOT:
            case TIME_INTERVAL:
            case WEEK:
            default: {
                LOGGER.warn("Unhandled test, test is not recursive or iterative: [{}] :: {}", endEvent,
                        data.getTestName());
            }
        }
        return false;
    }

    private boolean hasNextIteration(TestData data) {
        ParameterData parameter = data.getEndParameter();
        if (parameter != null && parameter.getCount() != null) {
            parameter.setCount(parameter.getCount() - 1);

            if (parameter.getCount() > 0) {
                return true;
            }
        }
        LOGGER.debug("No more iterations, test reschedule cancelled");
        return false;
    }

    private boolean validateRecursion(TestData data) {
        LOGGER.debug("Validating recursion");
        RecursionData recursion = data.getRecursion();
        if (recursion == null || recursion.getParameters() == null) {
            return false;
        }
        TestType recursionType = TestType.fromInt(recursion.getEvent(), TestType.NONE);
        switch (recursionType) {
            case TIME_INTERVAL: {
                LOGGER.debug("Validating time interval");
                return validateTimeIntervalRecursion(data);
            }
            case WEEK: {
                LOGGER.debug("Validating week interval");
                return validateWeekIntervalRecursion(data);
            }
            case DATE:
            case BOOT:
            case ITERATIONS:
            case USER_REQUEST:
            case NONE:
            default: {
                LOGGER.warn("Unhandled test, test is not recursive or does not have recursion data: [{}] :: {}",
                        recursionType, data.getTestName());
            }
        }
        return false;
    }

    private boolean validateTimeIntervalRecursion(TestData data) {
        LOGGER.debug("Validating time interval recursion");
        ParameterData parameter = data.getRecursion().getParameters();
        if (parameter == null || parameter.getInterval() == null) {
            LOGGER.debug("Validating time interval recursion :: missing parameters");
            return false;
        }

        int interval = parameter.getInterval() * 1000;
        Calendar nextDate = Calendar.getInstance();
        nextDate.setTimeInMillis(data.getStartDate().getTime() + interval);
        data.setStartDate(nextDate.getTime());

        LOGGER.debug("Validating time interval recursion :: rescheduling test");
        TestType testType = TestType.fromInt(data.getTestType(), TestType.NONE);
        if (testType == TestType.USER_REQUEST) {
            testType = TestType.DATE;
            data.setTestType(testType.getType());
        }
        createTestAndExecuteOrSchedule(data, testType, TestExecutionType.SCHEDULED);
        return true;
    }

    private boolean validateWeekIntervalRecursion(TestData data) {
        LOGGER.debug("Validating week interval recursion");
        ParameterData parameter = data.getRecursion().getParameters();
        if (parameter == null || parameter.getHour() == null || parameter.getWeekDays() == null ||
                parameter.getInterval() == null) {
            LOGGER.debug("Validating week interval recursion :: missing parameters");
            return false;
        }

        data.setStartDate(WeekEventUtil.getNextExecutionDate(parameter));

        LOGGER.debug("Validating week interval recursion :: rescheduling test {}", data.getStartDate());
        TestType testType = TestType.fromInt(data.getTestType(), TestType.NONE);
        if (testType == TestType.USER_REQUEST) {
            testType = TestType.WEEK;
            data.setTestType(testType.getType());
        }
        createTestAndExecuteOrSchedule(data, testType, TestExecutionType.SCHEDULED);
        return true;
    }

    private void validateInputTest(TestData data, TestExecutionType executionType) {
        TestType testType = TestType.fromInt(data.getTestType(), TestType.NONE);

        switch (testType) {
            case NONE:
            case DATE: {
                scheduleDateExecutionTest(data, executionType);
                break;
            }
            case WEEK: {
                scheduleWeekExecutionTest(data, executionType);
                break;
            }
            case USER_REQUEST: {
                scheduleUserRequestTest(data, executionType);
                break;
            }
            case BOOT:
            case ITERATIONS:
            case TIME_INTERVAL:
            default: {
                LOGGER.warn("Unhandled test, can't execute, schedule or run on demand: [{}] :: {}", testType,
                        data.getTestName());
            }
        }
    }

    private void scheduleUserRequestTest(TestData data, TestExecutionType executionType) {
        if (data.getEndDate().getTime() <= Calendar.getInstance().getTime().getTime()) {
//            AlarmsManager.getInstance(getApplication()).generateAlarm(AlarmUtils.INICIO, AlarmType.A067.name(), AlarmType.A067.getAlarmContent(), "Name: " + data.getTestName() + " ID: " + data.getTestId());
            LOGGER.debug("User request test has expired: {}", data.getTestName());
            clearOnDemandTest(data);
            return;
        }
        createTestAndExecuteOrSchedule(data, TestType.USER_REQUEST, executionType);
    }

    private void scheduleWeekExecutionTest(TestData data, TestExecutionType executionType) {
        if (data.getStartDate().getTime() <= Calendar.getInstance().getTimeInMillis()) {
            ParameterData startParameter = data.getStartParameter();
            Calendar now = Calendar.getInstance();

            if (data.getStartDate().getTime() < now.getTimeInMillis()) {
                data.setStartDate(WeekEventUtil.getNextExecutionDate(startParameter));
            }
        }
        if (data.getStartDate().getTime() <= Calendar.getInstance().getTimeInMillis() ||
                data.getEndDate().getTime() <= Calendar.getInstance().getTimeInMillis()) {
            LOGGER.debug("Weekly execution test has expired: {}", data.getTestName());
            clearScheduledTest(data);
            return;
        }

        createTestAndExecuteOrSchedule(data, TestType.WEEK, executionType);
    }

    private void scheduleDateExecutionTest(TestData data, TestExecutionType executionType) {
        if (data.getStartDate().getTime() <= Calendar.getInstance().getTime().getTime() ||
                data.getEndDate().getTime() <= Calendar.getInstance().getTimeInMillis()) {

            if (!validateAfterExecution(data)) {
                LOGGER.debug("Date execution test has expired: {}", data.getTestName());
                clearScheduledTest(data);
            }
            return;
        }
        createTestAndExecuteOrSchedule(data, TestType.DATE, executionType);
    }

    private void createTestAndExecuteOrSchedule(TestData data, TestType testType, TestExecutionType executionType) {
        ExecutableTest executableTest = ExecutableTest.createFromData(data, executionType, getApplication());
        if (shouldExecuteTestImmediately(executableTest, testType)) {
            sendTestToExecutor(executableTest);
        } else if (testType == TestType.USER_REQUEST) {
            LOGGER.debug("Persisting test for UI: {}", data.getTestName());
            saveOnDemandTest(executableTest);
        } else if (executionType == TestExecutionType.RESCHEDULE) {
            LOGGER.debug("Rescheduling test: {}", data.getTestName());
            resetAlarmForTest(executableTest);
        } else if (data.getStartDate().getTime() < data.getEndDate().getTime()) {
            LOGGER.debug("Scheduling test: {}", data.getTestName());
            createAlarmForTest(executableTest);
        }
    }

    private void executeOnDemandTest(TestData data, TestExecutionType executionType) {
        ExecutableTest executableTest = ExecutableTest.createFromData(data, executionType, getApplication());
        sendTestToExecutor(executableTest);
    }

    private void sendTestToExecutor(ExecutableTest executableTest) {
        TestExecutor.getInstance(getApplication()).execute(executableTest);
    }

    private boolean shouldExecuteTestImmediately(ExecutableTest executableTest, TestType testType) {
        long now = Calendar.getInstance().getTimeInMillis();
        long testTime = executableTest.getExecutionDate().getTime();
        return (testTime - now) <= 3000 && testType != TestType.USER_REQUEST;
    }

    private void createAlarmForTest(ExecutableTest executableTest) {
        alarmManager.createAlarmForTest(executableTest);
    }

    private void resetAlarmForTest(ExecutableTest executableTest) {
        alarmManager.resetAlarmForTest(executableTest);
    }

    private void saveOnDemandTest(ExecutableTest executableTest) {
        OnDemandTestManager.getInstance(getApplication()).saveUserExecutableTest(executableTest);
    }

    private void clearScheduledTest(TestData data) {
        alarmManager.clearScheduledTest(data);
    }

    private void clearOnDemandTest(TestData data) {
        TestExecutor.getInstance(getApplication()).disposeIfExecuting(data.getTestId());
        boolean onDemandTestDeleted = OnDemandTestManager.getInstance(getApplication()).clearOnDemandTest(data);
        LOGGER.debug("On Demand test {} deleted ==> {}", data.getTestId(), onDemandTestDeleted);
    }
}

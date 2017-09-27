package pt.ptinovacao.arqospocket.core.serialization.entities.results.type71;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.TestParser;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.type73.ConfigureOccupationReportRequest;
import pt.ptinovacao.arqospocket.core.tests.TestType;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TestData;
import pt.ptinovacao.arqospocket.core.utils.DateUtils;
import pt.ptinovacao.arqospocket.core.utils.WeekEventUtil;
import pt.ptinovacao.arqospocket.persistence.ExecutingEventDao;
import pt.ptinovacao.arqospocket.persistence.OnDemandEventDao;
import pt.ptinovacao.arqospocket.persistence.ScheduledEventDao;
import pt.ptinovacao.arqospocket.persistence.models.ExecutingEvent;
import pt.ptinovacao.arqospocket.persistence.models.OnDemandEvent;
import pt.ptinovacao.arqospocket.persistence.models.ScheduledEvent;

/**
 * Created by pedro on 19/05/2017.
 */
public class ConfigureTestInfo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigureTestInfo.class);

    private ArrayList<TestInfo> testInfos;

    private CoreApplication coreApplication;

    private final TestParser testParser = new TestParser();

    public ConfigureTestInfo(CoreApplication coreApplication) {
        this.coreApplication = coreApplication;
        testInfos = new ArrayList<>();

        ScheduledEventDao scheduledEventDao = coreApplication.getDatabaseHelper().createScheduledEventDao();
        ExecutingEventDao executingEventDao = coreApplication.getDatabaseHelper().createExecutingEventDao();
        OnDemandEventDao onDemandEventDao = coreApplication.getDatabaseHelper().createOnDemandEventDao();

        for (ScheduledEvent scheduledEvent : scheduledEventDao.readAllScheduledEventList()) {

            TestInfo testInfo = new TestInfo();

            final TestData testData = testParser.parseSingleTest(scheduledEvent.getTestData());

            addValues(testData, testInfo);
            testInfo.setTestId(scheduledEvent.getTestId());
            testInfo.setInternalState(StateExecutionTest.TEST_READY.convertToIndex());

            testInfos.add(testInfo);
        }

        for (ExecutingEvent executingEvent : executingEventDao.listAllEvents()) {

            TestInfo testInfo = new TestInfo();

            final TestData testData = testParser.parseSingleTest(executingEvent.getTestData());

            addValues(testData, testInfo);
            testInfo.setTestId(executingEvent.getTestId());
            testInfo.setInternalState(
                    executingEvent.isExecuting() ? StateExecutionTest.TEST_IN_EXECUTION_BUT_EXCEEDING_TIME_MAX.convertToIndex() :
                            StateExecutionTest.TEST_TERMINATED.convertToIndex());

            testInfos.add(testInfo);
        }

        for (OnDemandEvent onDemandEvent : onDemandEventDao.listAllOnDemandEvents()) {
            try {
                TestInfo testInfo = new TestInfo();

                final TestData testData = testParser.parseSingleTest(onDemandEvent.getTestData());

                addValues(testData, testInfo);
                testInfo.setTestId(onDemandEvent.getTestId());
                testInfo.setInternalState(StateExecutionTest.TEST_CREATED.convertToIndex());

                testInfos.add(testInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addValues(TestData testData, TestInfo testInfo) {
        testInfo.setModulo(0);
        testInfo.setTestName(testData.getTestName());
        testInfo.setDataIni(DateUtils.convertDateToStringArqos(testData.getStartDate()));
        testInfo.setDataFim(DateUtils.convertDateToStringArqos(testData.getEndDate()));
        testInfo.setStartEvent(testData.getTestType());
        testInfo.setStartParam(testData.getStartParameter());
        testInfo.setEndEvent(testData.getEndEvent());
        testInfo.setEndParam(testData.getEndParameter());
        testInfo.setRecursion(testData.getRecursion());
        testInfo.setIterationMaxDuration(ConfigureOccupationReportRequest.getCountTimeTasks(testData));
        testInfo.setMacroPerIteration(countMacros(testData.getTasksData()));
        testInfo.setTasksPerIteration(testData.getTasksData().length);
        testInfo.setState(1);

        ExecutingEventDao executingEventDao = coreApplication.getDatabaseHelper().createExecutingEventDao();

        testInfo.setExecutedIterations(executingEventDao.countTestExecutingFalse(testData.getTestId()));
        testInfo.setBrokenIterations(executingEventDao.countTestExecutingTrue(testData.getTestId()));
        testInfo.setSkippedIterations(createTimeProvidedSchedule(testData));
    }

    private int countMacros(TaskData[] taskDatas) {
        HashMap<String, String> stringHashMap = new HashMap<>();
        for (TaskData taskData : taskDatas) {
            if (!Strings.isNullOrEmpty(taskData.getMacroId())) {
                stringHashMap.put(taskData.getMacroId(), taskData.getMacroId());
            }
        }
        return stringHashMap.size();
    }

    public ArrayList<TestInfo> getTestInfo() {
        return testInfos;
    }

    private int createTimeProvidedSchedule(TestData testData) {

        int numberIteration = 0;
        int countTimeTasks = ConfigureOccupationReportRequest.getCountTimeTasks(testData) * 1000;
        long dateEndProvided = 0L;

        if (testData.getRecursion() != null && testData.getRecursion().getParameters() != null) {
            long dateActual = Calendar.getInstance().getTimeInMillis();

            TestType testType = TestType.fromInt(testData.getRecursion().getEvent(), TestType.NONE);
            while ((testData.getEndDate().getTime() / 1000) > (dateEndProvided / 1000)) {
                long nextExecutionDate;

                switch (testType) {
                    case ITERATIONS: {
                        nextExecutionDate = ConfigureOccupationReportRequest
                                .calculateNextExecution(dateActual, testData.getStartDate().getTime(),
                                        testData.getRecursion().getParameters().getCount());
                        dateEndProvided = nextExecutionDate + countTimeTasks;
                        break;
                    }
                    case TIME_INTERVAL: {
                        nextExecutionDate = ConfigureOccupationReportRequest
                                .calculateNextExecution(dateActual, testData.getStartDate().getTime(),
                                        testData.getRecursion().getParameters().getInterval());

                        long timeMoreInterval = testData.getRecursion().getParameters().getInterval() * 1000;
                        dateEndProvided = nextExecutionDate + timeMoreInterval;
                        break;
                    }
                    case WEEK:
                        nextExecutionDate =
                                WeekEventUtil.getNextExecutionDate(dateActual, testData.getRecursion().getParameters())
                                        .getTime();
                        dateEndProvided = nextExecutionDate + countTimeTasks;
                        break;
                    default:
                        return numberIteration;
                }

                dateActual = nextExecutionDate;
                numberIteration++;
            }
        }
        return numberIteration;
    }

    private enum StateExecutionTest {
        TEST_CREATED,
        PERSIST_FILE,
        TEST_SUBMITTED,
        TEST_READY,
        TEST_CONFLICTED_WITH_OTHER_TEST_ACTIVE,
        TEST_CONFLICTED_WAITING_BY_RESPONSE,
        TEST_EXPIRED,
        TEST_IN_EXECUTION,
        TEST_IN_EXECUTION_BUT_EXCEEDING_TIME_MAX,
        TEST_IN_DISCARDED,
        POORLY_DEFINED_TEST,
        TEST_SELECTED_TO_BE_DOWNLOAD_BY_PROBE,
        TEST_SELECTED_TO_BE_DOWNLOAD_BY_SG,
        TEST_TERMINATED,
        TEST_CANCELED;

        public int convertToIndex() {
            return ordinal();
        }
    }
}
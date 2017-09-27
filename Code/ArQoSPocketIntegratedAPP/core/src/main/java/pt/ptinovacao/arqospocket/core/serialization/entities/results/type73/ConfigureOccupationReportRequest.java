package pt.ptinovacao.arqospocket.core.serialization.entities.results.type73;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.TestParser;
import pt.ptinovacao.arqospocket.core.serialization.entities.ManagementMessage;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.TestType;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.TestData;
import pt.ptinovacao.arqospocket.core.tests.data.TestResult;
import pt.ptinovacao.arqospocket.core.utils.DateUtils;
import pt.ptinovacao.arqospocket.core.utils.IntegerUtils;
import pt.ptinovacao.arqospocket.core.utils.WeekEventUtil;
import pt.ptinovacao.arqospocket.persistence.ExecutingEventDao;
import pt.ptinovacao.arqospocket.persistence.ScheduledEventDao;
import pt.ptinovacao.arqospocket.persistence.models.BaseTaskEvent;
import pt.ptinovacao.arqospocket.persistence.models.ExecutingEvent;
import pt.ptinovacao.arqospocket.persistence.models.ScheduledEvent;

/**
 * Created by pedro on 22/05/2017.
 */
public class ConfigureOccupationReportRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigureOccupationReportRequest.class);

    private OccupationReportRequest occupationReportRequest;

    private CoreApplication coreApplication;

    private HashMap<String, String> numberTests = new HashMap<>();

    private List<IterationInfo> iterationInfos;

    public ConfigureOccupationReportRequest(CoreApplication coreApplication, ManagementMessage tests) {
        this.coreApplication = coreApplication;

        occupationReportRequest = new OccupationReportRequest();
        iterationInfos = new ArrayList<>();

        ExecutingEventDao executingEventDao = coreApplication.getDatabaseHelper().createExecutingEventDao();
        ScheduledEventDao ScheduledEventDao = coreApplication.getDatabaseHelper().createScheduledEventDao();

        long startDate = DateUtils.convertDateStringToLong(tests.getStartDate());
        long endDate = DateUtils.convertDateStringToLong(tests.getEndDate());

        List<ExecutingEvent> executingEvents = executingEventDao.readAllExecutedEventsBetweenDate(startDate, endDate);
        List<ScheduledEvent> scheduledEvents = ScheduledEventDao.readAllScheduledEventsBetweenDates(startDate, endDate);

        occupationReportRequest.setDataFim(tests.getEndDate());
        occupationReportRequest.setDataini(tests.getStartDate());

        for (ExecutingEvent executingEvent : executingEvents) {
            countTimeEndEndIterationTests(executingEventDao, executingEvent);
        }

        for (ScheduledEvent scheduledEvent : scheduledEvents) {
            createTimeProvidedSchedule(scheduledEvent);
        }

        occupationReportRequest.setTestCount(numberTests.size());
        occupationReportRequest.setIterationCount(iterationInfos.size());

        occupationReportRequest.setIteration(iterationInfos);
    }

    private void countTimeEndEndIterationTests(ExecutingEventDao executingEventDao, ExecutingEvent executingEvent) {

        StateExecutionTask stateExecutionTask;

        IterationInfo iterationInfo = new IterationInfo();

        final TestParser testParser = new TestParser();

        TestResult testResult = testParser.parseSingleResult(executingEvent.getResultData());
        TestData testData = testParser.parseSingleTest(executingEvent.getTestData());

        iterationInfo.setTestName(testData.getTestName());
        iterationInfo.setTesteid(testData.getTestId());
        numberTests.put(testData.getTestId(), testData.getTestId());
        iterationInfo.setExecutionType("0");

        iterationInfo.setDataIni(DateUtils.convertDateToStringArqos(testData.getStartDate()));
        iterationInfo.setDataFim(DateUtils.convertDateToStringArqos(testData.getEndDate()));

        if (testResult != null) {
            iterationInfo.setRealDataIni(DateUtils.convertDateToStringArqos(testResult.getStartDate()));
            iterationInfo.setRealDataFim(DateUtils.convertDateToStringArqos(testResult.getEndDate()));

            iterationInfo.setModulo(String.valueOf(testResult.getModulo()));
            TaskResult[] taskResults = testResult.getTaskResults();

            if (taskResults != null) {

                ArrayList<TaskInfo> taskInfos = new ArrayList<>();
                int id = 0;
                for (TaskResult taskResult : taskResults) {
                    TaskInfo taskInfo = new TaskInfo();

                    taskInfo.setTestName(testData.getTestName());
                    taskInfo.setModulo(String.valueOf(testData.getModulo()));
                    taskInfo.setTesteid(testData.getTestId());
                    taskInfo.setMacroid(String.valueOf(testData.getMacroNumber()));
                    taskInfo.setTaskNumber(String.valueOf(taskResult.getTaskNumber()));
                    taskInfo.setTesteid(taskResult.getTaskId());
                    taskInfo.setState(taskResult.getStatus());

                    if (executingEvent.isExecuting()) {
                        stateExecutionTask = StateExecutionTask.RUNNING;
                    } else {
                        if (BaseExecutableTask.RESULT_TASK_FAILED.equals(taskResult.getStatus())) {
                            stateExecutionTask = StateExecutionTask.DID_NOT_RUN;
                        } else {
                            stateExecutionTask = StateExecutionTask.DONE;
                        }
                    }

                    taskInfo.setState(String.valueOf(stateExecutionTask.convertToIndex()));

                    taskInfo.setDataIni(DateUtils.convertDateToStringArqos(taskResult.getStartDate()));
                    taskInfo.setDataFim(DateUtils.convertDateToStringArqos(taskResult.getEndDate()));

                    List<BaseTaskEvent> baseTaskEvents = executingEventDao.readEventList(executingEvent.getId());

                    if (baseTaskEvents.get(id) != null) {
                        taskInfo.setRealDataFim(
                                DateUtils.convertDateToStringArqos(new Date(baseTaskEvents.get(id).getEndDate())));
                        taskInfo.setRealDataIni(
                                DateUtils.convertDateToStringArqos(new Date(baseTaskEvents.get(id).getStartDate())));
                    }

                    taskInfos.add(taskInfo);
                }
                iterationInfo.setTaskInfo(taskInfos);
            }
        }

        iterationInfos.add(iterationInfo);
    }

    private void createIterationInfoSchedule(ScheduledEvent scheduledEvent, Date dateStart, Date dateEnd) {

        final TestParser testParser = new TestParser();

        StateExecutionTask stateExecutionTask = StateExecutionTask.ON_GOING;

        final TestData testData = testParser.parseSingleTest(scheduledEvent.getTestData());

        IterationInfo iterationInfo = new IterationInfo();
        iterationInfo.setModulo(String.valueOf(testData.getModulo()));
        iterationInfo.setTestName(testData.getTestName());
        iterationInfo.setTesteid(testData.getTestId());
        numberTests.put(testData.getTestId(), testData.getTestId());
        iterationInfo.setExecutionType("0");

        iterationInfo.setDataIni(DateUtils.convertDateToStringArqos(dateStart));
        iterationInfo.setDataFim(DateUtils.convertDateToStringArqos(dateEnd));

        TaskData[] tasksData = testData.getTasksData();

        if (tasksData != null) {

            ArrayList<TaskInfo> taskInfos = new ArrayList<>();
            for (TaskData tasksDat : testData.getTasksData()) {
                TaskInfo taskInfo = new TaskInfo();
                taskInfo.setTestName(testData.getTestName());
                taskInfo.setModulo(String.valueOf(testData.getModulo()));
                taskInfo.setTesteid(testData.getTestId());
                taskInfo.setMacroid(String.valueOf(testData.getMacroNumber()));
                taskInfo.setTesteid(tasksDat.getTaskId());
                taskInfo.setTaskNumber(String.valueOf(tasksDat.getTaskNumber()));

                taskInfo.setState(String.valueOf(stateExecutionTask.convertToIndex()));
                taskInfo.setDataIni(DateUtils.convertDateToStringArqos(dateStart));

                Long timeoutValue = dateStart.getTime() + (IntegerUtils.parseInt(tasksDat.getTimeout()) * 1000);

                taskInfo.setDataFim(DateUtils.convertDateToStringArqos(new Date(timeoutValue)));
                taskInfos.add(taskInfo);
            }
            iterationInfo.setTaskInfo(taskInfos);
        }
        iterationInfos.add(iterationInfo);
    }

    private int createTimeProvidedSchedule(ScheduledEvent scheduledEvent) {

        int numberIteration = 0;

        TestParser testParser = new TestParser();
        TestData testData = testParser.parseSingleTest(scheduledEvent.getTestData());
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

                createIterationInfoSchedule(scheduledEvent, new Date(nextExecutionDate), new Date(dateEndProvided));

                dateActual = nextExecutionDate;
                numberIteration++;
            }

        } else {

            long startDate = testData.getStartDate().getTime();
            createIterationInfoSchedule(scheduledEvent, new Date(startDate), new Date(startDate + countTimeTasks));
        }

        return numberIteration;
    }

    public static long calculateNextExecution(long inc, long testData, int value) {

        if (inc < testData) {
            return testData;
        } else {
            return inc + (value * 1000);
        }
    }

    public static int getCountTimeTasks(TestData testData) {
        int value = 0;
        if (testData != null && testData.getTasksData() != null) {
            for (TaskData taskData : testData.getTasksData()) {
                if (taskData != null) {
                    value += IntegerUtils.parseInt(taskData.getTimeout());
                }
            }
        }
        return value;
    }

    public OccupationReportRequest getOccupationReportRequest() {
        return occupationReportRequest;
    }

    private enum StateExecutionTask {
        READY_TO_RUN,
        RUNNING,
        DONE,
        ON_GOING,
        WAITING,
        LOADING_ERROR,
        CANCELED,
        DID_NOT_RUN,
        DOES_NOT_EXIST;

        public int convertToIndex() {
            return (ordinal() + 1);
        }
    }
}
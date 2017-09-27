package pt.ptinovacao.arqospocket.persistence;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import pt.ptinovacao.arqospocket.persistence.models.Alarm;
import pt.ptinovacao.arqospocket.persistence.models.BaseTaskEvent;
import pt.ptinovacao.arqospocket.persistence.models.CDR;
import pt.ptinovacao.arqospocket.persistence.models.ExecutingEvent;
import pt.ptinovacao.arqospocket.persistence.models.Radiolog;
import pt.ptinovacao.arqospocket.persistence.models.TaskEvent;
import pt.ptinovacao.arqospocket.persistence.utils.Hashing;

/**
 * Data Access Object for the {@link ExecutingEvent}.
 * <p>
 * Created by Emílio Simões on 12-04-2017.
 */
public class ExecutingEventDao extends BaseDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutingEventDao.class);

    private static final String RECORD_AUDIO_TASK = "42";
    private static final String RECEIVE_SMS_TASK = "36";

    private static final String SCANLOG = "Scanlog";

    ExecutingEventDao(DatabaseHelper helper) {
        super(helper);
    }

    /**
     * Saves a new executing test to the database. It will validate if the test already exists in the database assuming
     * that test of the same type will be executed on different times causing a different signature. The duplicates test
     * will only prevent the same execution from being persisted twice. Duplicates will be ignored.
     *
     * @param executingEvent the executing test to persist.
     * @return a {@link Single<ExecutingEvent>} that will resolve the inserted test. If the test is ignored the original
     * test is returned.
     */
    public Single<ExecutingEvent> saveNewExecutingTest(ExecutingEvent executingEvent) {
        String signature = Hashing.md5(executingEvent.getTestData());

        ExecutingEvent existing =
                getEntityStore().select(ExecutingEvent.class).where(ExecutingEvent.HASH.eq(signature)).get()
                        .firstOrNull();
        if (existing != null) {
            LOGGER.debug("On demand test already persisted, ignoring");
            return Single.just(executingEvent);
        }

        executingEvent.setHash(signature);
        executingEvent.setExecuting(true);
        executingEvent.setReported(false);
        executingEvent.setResultData(null);
        executingEvent.setStartDate(Calendar.getInstance().getTimeInMillis());

        return getEntityStore().insert(executingEvent);
    }

    public Single<ExecutingEvent> saveNewFailedExecutingTest(ExecutingEvent executingEvent, String testResult) {
        String signature = Hashing.md5(executingEvent.getTestData());

        ExecutingEvent existing =
                getEntityStore().select(ExecutingEvent.class).where(ExecutingEvent.HASH.eq(signature)).get()
                        .firstOrNull();
        if (existing != null) {
            LOGGER.debug("On demand test already persisted, ignoring");
            return Single.just(executingEvent);
        }

        executingEvent.setHash(signature);
        executingEvent.setExecuting(false);
        executingEvent.setReported(false);
        executingEvent.setResultData(testResult);
        executingEvent.setTestFailed(true);

        if (executingEvent == null) {
            long now = Calendar.getInstance().getTimeInMillis();
            executingEvent.setStartDate(now);
            executingEvent.setEndDate(now);
        }

        return getEntityStore().insert(executingEvent);
    }

    /**
     * Gets the executing tests as an observable. This method will only return the events that are currently executing.
     *
     * @return the executing tests as an observable.
     */
    public Observable<ExecutingEvent> readAllExecutingEvents() {
        return getEntityStore().select(ExecutingEvent.class).where(ExecutingEvent.EXECUTING.eq(true)).get()
                .observable();
    }

    /**
     * Lists all the executing tests. This method will only return the events that are currently executing.
     *
     * @return the executing tests as an observable.
     */
    public List<ExecutingEvent> listAllExecutingEvents() {
        return getEntityStore().select(ExecutingEvent.class).where(ExecutingEvent.EXECUTING.eq(false)).get().toList();
    }

    public List<ExecutingEvent> listAllEvents() {
        return getEntityStore().select(ExecutingEvent.class).get().toList();
    }

    /**
     * Gets an executing test by it's ID.
     *
     * @param testId the ID of the test to read.
     * @return the read test, if any.
     */
    public Maybe<ExecutingEvent> readExecutingEvent(long testId) {
        return getEntityStore().findByKey(ExecutingEvent.class, testId);
    }

    public List<BaseTaskEvent> readEventList(long testId) {
        return getEntityStore().findByKey(ExecutingEvent.class, testId).blockingGet().getTaskEvents().toList();
    }

    public boolean isExistTest(String testId) {
        return getEntityStore().count(ExecutingEvent.class).where(ExecutingEvent.TEST_ID.eq(testId))
                .and(ExecutingEvent.EXECUTING.eq(true)).get().value() > 0;
    }

    public int countTestExecutingTrue(String testId) {
        return getEntityStore().count(ExecutingEvent.class).where(ExecutingEvent.TEST_ID.eq(String.valueOf(testId)))
                .and(ExecutingEvent.EXECUTING.eq(true)).get().value();
    }

    public int countTestExecutingFalse(String testId) {
        return getEntityStore().count(ExecutingEvent.class).where(ExecutingEvent.TEST_ID.eq(String.valueOf(testId)))
                .and(ExecutingEvent.EXECUTING.eq(false)).get().value();
    }

    public BaseTaskEvent readExecutingEventFirstOrNull(long testId) {
        return getEntityStore().findByKey(ExecutingEvent.class, testId).blockingGet().getTaskEvents().firstOrNull();
    }

    /**
     * Reads all the executing events filtered by connection technology.
     *
     * @param connectionTechnology the connection technology.
     * @return the events observable.
     */
    public Observable<ExecutingEvent> readAllExecutedEventsByConnectionTechnology(int connectionTechnology) {
        if (connectionTechnology == 0) {
            return getEntityStore().select(ExecutingEvent.class).where(ExecutingEvent.EXECUTING.eq(false))
                    .orderBy(ExecutingEvent.ID.desc()).get().observable();
        } else {
            return getEntityStore().select(ExecutingEvent.class).distinct().join(TaskEvent.class)
                    .on(TaskEvent.EVENT_ID.eq(ExecutingEvent.ID))
                    .and(TaskEvent.CONNECTION_TECHNOLOGY.eq(connectionTechnology))
                    .and(ExecutingEvent.EXECUTING.eq(false)).orderBy(ExecutingEvent.ID.desc()).get().observable();
        }
    }

    /**
     * Lists the executing events filtered by connection technology.
     *
     * @param connectionTechnology the connection technology.
     * @return the events list.
     */
    public List<ExecutingEvent> listAllExecutedEventsByConnectionTechnology(int connectionTechnology) {
        if (connectionTechnology == 0) {
            return getEntityStore().select(ExecutingEvent.class).where(ExecutingEvent.EXECUTING.eq(false)).get()
                    .toList();
        } else {
            return getEntityStore().select(ExecutingEvent.class).distinct().join(TaskEvent.class)
                    .on(TaskEvent.EVENT_ID.eq(ExecutingEvent.ID))
                    .and(TaskEvent.CONNECTION_TECHNOLOGY.eq(connectionTechnology))
                    .and(ExecutingEvent.EXECUTING.eq(false)).get().toList();
        }
    }

    /**
     * Gets the executed test as an observable. This method will only return the event that is currently executing.
     *
     * @return the executing test as an observable.
     */
    public Observable<ExecutingEvent> readExecutedEvent(long testId) {
        return getEntityStore().select(ExecutingEvent.class).where(ExecutingEvent.EXECUTING.eq(false))
                .and(ExecutingEvent.ID.eq(testId)).get().observable();
    }

    public ExecutingEvent readExecutingEvent() {
        return getEntityStore().select(ExecutingEvent.class).where(ExecutingEvent.EXECUTING.eq(true)).get()
                .firstOrNull();
    }

    /**
     * Gets a count of the executing tests.
     *
     * @return the executing events count.
     */
    public int countAllExecutingEvents() {
        return getEntityStore().count(ExecutingEvent.class).where(ExecutingEvent.EXECUTING.eq(true)).get().value();
    }

    public List<ExecutingEvent> readAllExecutedEventsBetweenDate(long startDate, long endDate) {
        return getEntityStore().select(ExecutingEvent.class)
                .where(ExecutingEvent.START_DATE.between(startDate, endDate))
                .or(ExecutingEvent.END_DATE.between(startDate, endDate)).get().toList();
    }

    public List<ExecutingEvent> readAllExecutedEventsById(String idTest) {
        return getEntityStore().select(ExecutingEvent.class).where(ExecutingEvent.TEST_ID.eq(idTest))
                .and(ExecutingEvent.EXECUTING_TEST.eq(false)).get().toList();
    }

    public int setAllTestExecuted(String idTest) {
        return getEntityStore().update(ExecutingEvent.class).set(ExecutingEvent.EXECUTING_TEST, true)
                .where(ExecutingEvent.TEST_ID.eq(idTest)).get().value();
    }

    public int countTestsWithFalse(String idTest) {
        return getEntityStore().count(ExecutingEvent.class).where(ExecutingEvent.TEST_ID.eq(idTest))
                .and(ExecutingEvent.EXECUTING_TEST.eq(false)).get().value();
    }

    /**
     * Gets a count of the executing tests of type x
     *
     * @param connectionTechnology value of technologies to filter
     * @return the executing events count of type x
     */
    public int countExecutedEventsByConnectionTechnology(int connectionTechnology) {
        return getEntityStore().count(ExecutingEvent.class).distinct().join(TaskEvent.class)
                .on(TaskEvent.EVENT_ID.eq(ExecutingEvent.ID))
                .and(TaskEvent.CONNECTION_TECHNOLOGY.eq(connectionTechnology)).and(ExecutingEvent.EXECUTING.eq(false))
                .get().value();
    }

    /**
     * Flags an existing test as executed.
     *
     * @param testId the ID of the test to set executed.
     * @param testResult the produced test result.
     * @param failed if any of the test tasks as failed.
     * @return a {@link Single<ExecutingEvent>} with the updated entity. If the ID does not match an existing event an
     * empty event is returned.
     */
    public Single<ExecutingEvent> flagTestAsExecuted(long testId, String testResult, boolean failed) {
        ExecutingEvent executingEvent = getEntityStore().findByKey(ExecutingEvent.class, testId).blockingGet();

        if (executingEvent != null) {
            executingEvent.setExecuting(false);
            executingEvent.setResultData(testResult);
            executingEvent.setTestFailed(failed);
            executingEvent.setEndDate(Calendar.getInstance().getTimeInMillis());
            return getEntityStore().update(executingEvent);
        }
        return Single.just(new ExecutingEvent());
    }

    public List<TaskEvent> readAllTestToSendAttachments() {
        return getEntityStore().select(TaskEvent.class).where(TaskEvent.NAME_FILE.notNull())
                .and(TaskEvent.TASK_DATA_ID.eq(RECORD_AUDIO_TASK)).and(TaskEvent.UPLOAD.eq(false)).get().toList();
    }

    public List<TaskEvent> readAllTestToSendNotification() {
        return getEntityStore().select(TaskEvent.class).where(TaskEvent.NAME_FILE.notNull())
                .and(TaskEvent.TASK_DATA_ID.eq(RECORD_AUDIO_TASK)).and(TaskEvent.UPLOAD.eq(true))
                .and(TaskEvent.NOTIFICATION_SENT.eq(false)).get().toList();
    }

    public List<Radiolog> readAllRadiologsToSendAttachments() {
        return getEntityStore().select(Radiolog.class).where(Radiolog.NAME_FILE.notNull())
                .and(Radiolog.REPORT_TYPE.notEqual(SCANLOG)).and(Radiolog.UPLOAD.eq(false)).get().toList();
    }

    public List<Radiolog> readAllScanlogsToSendAttachments() {
        return getEntityStore().select(Radiolog.class).where(Radiolog.NAME_FILE.notNull())
                .and(Radiolog.REPORT_TYPE.eq(SCANLOG)).and(Radiolog.UPLOAD.eq(false)).get().toList();
    }

    public List<Radiolog> readAllRadiologsToSendNotification() {
        return getEntityStore().select(Radiolog.class).where(Radiolog.NAME_FILE.notNull())
                .and(Radiolog.REPORT_TYPE.notEqual(SCANLOG)).and(Radiolog.UPLOAD.eq(true))
                .and(Radiolog.NOTIFICATION_SENT.eq(false)).get().toList();
    }

    public List<Radiolog> readAllScanlogsToSendNotification() {
        return getEntityStore().select(Radiolog.class).where(Radiolog.NAME_FILE.notNull())
                .and(Radiolog.REPORT_TYPE.eq(SCANLOG)).and(Radiolog.UPLOAD.eq(true))
                .and(Radiolog.NOTIFICATION_SENT.eq(false)).get().toList();
    }

    public List<Alarm> readAllAlarmsToSendNotification() {
        return getEntityStore().select(Alarm.class).where(Alarm.NAME_FILE.notNull())
                .and(Alarm.NOTIFICATION_SENT.eq(false)).get().toList();
    }

    public List<CDR> readAllCDRsToSendNotification() {
        return getEntityStore().select(CDR.class).where(CDR.NOTIFICATION_SENT.eq(false)).get().toList();
    }

    public Single<TaskEvent> updateAllTestThatUploadFile(Long testId) {
        TaskEvent taskEvent = getEntityStore().findByKey(TaskEvent.class, testId).blockingGet();

        if (taskEvent != null) {
            taskEvent.setUpload(true);
            LOGGER.debug("UploadFile::Updated task with id " + testId);
            return getEntityStore().update(taskEvent);
        }
        return Single.just(new TaskEvent());
    }

    public Single<Radiolog> updateAllRadiologsThatUploadFile(Long testId) {
        Radiolog radiolog = getEntityStore().findByKey(Radiolog.class, testId).blockingGet();

        if (radiolog != null && !radiolog.getReportType().equals(SCANLOG)) {
            radiolog.setUpload(true);
            LOGGER.debug("UploadRadiolog::Updated radiolog with id " + testId);
            return getEntityStore().update(radiolog);
        }
        return Single.just(new Radiolog());
    }

    public Single<Radiolog> updateAllScanlogsThatUploadFile(Long testId) {
        Radiolog radiolog = getEntityStore().findByKey(Radiolog.class, testId).blockingGet();

        if (radiolog != null && radiolog.getReportType().equals(SCANLOG)) {
            radiolog.setUpload(true);
            LOGGER.debug("UploadScanlog::Updated scanlog with id " + testId);
            return getEntityStore().update(radiolog);
        }
        return Single.just(new Radiolog());
    }

    public Single<TaskEvent> updateAllTestThatPostFileNotification(ArrayList<Long> uploadFilesId) {
        for (Long testId : uploadFilesId) {
            TaskEvent taskEvent = getEntityStore().findByKey(TaskEvent.class, testId).blockingGet();

            if (taskEvent != null) {
                taskEvent.setNotificationSent(true);
                getEntityStore().update(taskEvent).blockingGet();
                LOGGER.debug("Notification::Updated task with id " + testId);
            }
        }
        return Single.just(new TaskEvent());
    }

    public Single<Radiolog> updateAllRadiologsThatPostFileNotification(ArrayList<Long> uploadFilesId) {
        for (Long radiologId : uploadFilesId) {
            Radiolog radiolog = getEntityStore().findByKey(Radiolog.class, radiologId).blockingGet();

            if (radiolog != null && !radiolog.getReportType().equals(SCANLOG)) {
                radiolog.setNotificationSent(true);
                getEntityStore().update(radiolog).blockingGet();
                LOGGER.debug("Notification::Updated task with id " + radiologId);
            }
        }
        return Single.just(new Radiolog());
    }

    public Single<Radiolog> updateAllScanlogsThatPostFileNotification(ArrayList<Long> uploadFilesId) {
        for (Long radiologId : uploadFilesId) {
            Radiolog radiolog = getEntityStore().findByKey(Radiolog.class, radiologId).blockingGet();

            if (radiolog != null && radiolog.getReportType().equals(SCANLOG)) {
                radiolog.setNotificationSent(true);
                getEntityStore().update(radiolog).blockingGet();
                LOGGER.debug("Notification::Updated radiolog with id " + radiologId);
            }
        }
        return Single.just(new Radiolog());
    }

    public Single<Alarm> updateAllAlarmsThatPostNotification(ArrayList<Long> uploadId) {
        for (Long alarmId : uploadId) {
            Alarm alarm = getEntityStore().findByKey(Alarm.class, alarmId).blockingGet();

            alarm.setNotificationSent(true);
            getEntityStore().update(alarm).blockingGet();
            LOGGER.debug("Notification::Updated alarm with id " + alarmId);

        }
        return Single.just(new Alarm());
    }

    public Single<CDR> updateAllCDRsThatPostNotification(ArrayList<Long> uploadId) {
        for (Long cdrId : uploadId) {
            CDR cdr = getEntityStore().findByKey(CDR.class, cdrId).blockingGet();

            cdr.setNotificationSent(true);
            getEntityStore().update(cdr).blockingGet();
            LOGGER.debug("Notification::Updated cdr with id " + cdrId);

        }
        return Single.just(new CDR());
    }

    /**
     * Updates a test executing result. This will enable tracking of the test progress during the test execution.
     *
     * @param testId the ID of the test to update.
     * @param testResult the result to set in the event.
     * @param taskEvent the task event to associate with the test event.
     * @param taskDataId the task data ID.
     * @return the updated event, or an empty event if the ID does not match a valid test.
     */
    public Single<ExecutingEvent> updateExecutionStatus(long testId, String testResult, TaskEvent taskEvent,
            String taskDataId, String nameFile) {
        ExecutingEvent executingEvent = getEntityStore().findByKey(ExecutingEvent.class, testId).blockingGet();

        if (executingEvent != null) {

            TaskEvent existing = null;
            for (BaseTaskEvent current : executingEvent.getTaskEvents().toList()) {
                if (Strings.nullToEmpty(taskDataId).equals(current.getTaskDataId())) {
                    existing = (TaskEvent) current;
                    break;
                }
            }

            if (existing != null) {
                existing.setEvent(executingEvent);
                existing.setConnectionTechnology(taskEvent.getConnectionTechnology());
                existing.setLongitude(taskEvent.getLongitude());
                existing.setLatitude(taskEvent.getLatitude());
                existing.setEndDate(Calendar.getInstance().getTimeInMillis());
                existing.setNameFile(nameFile);
                getEntityStore().update(existing).blockingGet();
            }

            executingEvent.setResultData(testResult);

            return getEntityStore().update(executingEvent);
        }
        return Single.just(new ExecutingEvent());
    }

    public Single<ExecutingEvent> createExecutionStatus(long testId, TaskEvent taskEvent) {
        ExecutingEvent executingEvent = getEntityStore().findByKey(ExecutingEvent.class, testId).blockingGet();

        if (executingEvent != null) {
            taskEvent.setEvent(executingEvent);
            getEntityStore().insert(taskEvent).blockingGet();
        }
        return Single.just(new ExecutingEvent());
    }

    /**
     * Gets a list of events that haven't been reported to the management service.
     *
     * @return an observable with the found events.
     */
    public Flowable<ExecutingEvent> readPendingExecutedEvents() {
        return getEntityStore().select(ExecutingEvent.class).where(ExecutingEvent.EXECUTING.eq(false))
                .and(ExecutingEvent.REPORTED.eq(false)).get().flowable();
    }

    /**
     * Gets a list of events that haven't been finished their execution yet.
     *
     * @return an observable with the found events.
     */
    public int countPendingExecutedEvents() {
        return getEntityStore().count(ExecutingEvent.class).where(ExecutingEvent.EXECUTING.eq(false))
                .and(ExecutingEvent.REPORTED.eq(false)).get().value();    }

    public int countPendingExecutingEvents() {
        return getEntityStore().count(ExecutingEvent.class).where(ExecutingEvent.EXECUTING.eq(true)).get().value();
    }

    /**
     * Flags a list of tests has being sent to the management server.
     *
     * @param events the evens to flag.
     */
    public void flagTestsAsReported(List<ExecutingEvent> events) {
        for (ExecutingEvent event : events) {
            if (event.getId() > 0) {
                ExecutingEvent existingEvent =
                        getEntityStore().findByKey(ExecutingEvent.class, event.getId()).blockingGet();
                if (existingEvent != null) {
                    existingEvent.setReported(true);
                    getEntityStore().update(existingEvent).blockingGet();
                }
            }
        }
    }

    /**
     * Deletes the dates older than a certain date.
     *
     * @param date the date from which the entries are considered old.
     */
    public Single<Integer> deleteEntriesOlderThan(long date) {
        return getEntityStore().delete(ExecutingEvent.class).where(ExecutingEvent.START_DATE.lessThan(date))
                .and(ExecutingEvent.REPORTED.equal(true)).get().single();
    }

    public Maybe<TaskEvent> listEntriesOlderThan(long date) {
        return getEntityStore().select(TaskEvent.class).where(TaskEvent.START_DATE.lessThan(date))
                .and(TaskEvent.NOTIFICATION_SENT.equal(true)).get().maybe();
    }
}
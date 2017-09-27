package pt.ptinovacao.arqospocket.core;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import pt.ptinovacao.arqospocket.core.alarms.AlarmType;
import pt.ptinovacao.arqospocket.core.alarms.AlarmUtils;
import pt.ptinovacao.arqospocket.core.alarms.AlarmsManager;
import pt.ptinovacao.arqospocket.core.settings.SharedPreferencesManager;
import pt.ptinovacao.arqospocket.core.tests.ExecutableTest;
import pt.ptinovacao.arqospocket.core.tests.data.TestData;
import pt.ptinovacao.arqospocket.core.utils.AlarmManagerUtils;
import pt.ptinovacao.arqospocket.persistence.ScheduledEventDao;
import pt.ptinovacao.arqospocket.persistence.models.ScheduledEvent;
import pt.ptinovacao.arqospocket.persistence.utils.ExecutedOperation;
import pt.ptinovacao.arqospocket.persistence.utils.ScheduledEventResult;

/**
 * Creates alarms for the test execution.
 * <p>
 * Created by Emílio Simões on 12-04-2017.
 */
public class TestAlarmManager {

    public static final String ACTION_EXECUTE_TEST = "pt.ptinovacao.arqospocket.service.alarm.action.EXECUTE_TEST";

    public static final String EXTRA_SCHEDULED_EVENT = "pt.ptinovacao.arqospocket.service.alarm.extra.SCHEDULED_EVENT";

    private static final Logger LOGGER = LoggerFactory.getLogger(TestAlarmManager.class);

    private final CoreApplication application;

    public TestAlarmManager(CoreApplication application) {
        this.application = application;
    }

    void createAlarmForTest(final ExecutableTest executableTest) {
        persistEvent(executableTest).subscribe(new Consumer<ScheduledEventResult>() {
            @Override
            public void accept(@NonNull ScheduledEventResult result) throws Exception {
                ScheduledEvent scheduledEvent = result.getEntity();

                if (result.getExecutedOperation() == ExecutedOperation.INSERT) {
                    createAlarm(executableTest, scheduledEvent);
                } else if (result.getExecutedOperation() == ExecutedOperation.UPDATE) {
                    cancelAlarm(scheduledEvent.getId());
                    createAlarm(executableTest, scheduledEvent);
                } else {
                    LOGGER.debug("Delayed test alarm already exists in the database or is invalid");
                }
            }
        });
    }

    void resetAlarmForTest(ExecutableTest executableTest) {
        ScheduledEvent scheduledEvent = loadEvent(executableTest);
        if (scheduledEvent != null) {
            cancelAlarm(scheduledEvent.getId());
            createAlarm(executableTest, scheduledEvent);
        } else {
            LOGGER.debug("No scheduled event exists for the provided test, event not rescheduled");
        }
    }

    void clearScheduledTest(TestData data) {
        ScheduledEventDao scheduledEventDao = application.getDatabaseHelper().createScheduledEventDao();
        scheduledEventDao.deleteTestByTestId(data.getTestId()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(@NonNull Long id) throws Exception {
                cancelAlarm(id);
            }
        });
      TestExecutor.getInstance(application).disposeIfExecuting(data.getTestId());
    }

    private void createAlarm(ExecutableTest executableTest, ScheduledEvent scheduledEvent) {

        if (!SharedPreferencesManager.getInstance(application).getAutomaticallyRunTests()) {
            LOGGER.debug("AutomaticallyRunTests off");

            return;
        }

        AlarmsManager.getInstance(application).generateAlarm(AlarmUtils.INICIO, AlarmType.A003.name(), AlarmType.A003.getAlarmContent(), executableTest.getData().getTestName().toString());

        LOGGER.debug("Creating delayed test alarm");
        PendingIntent pendingIntent = getPendingIntent(scheduledEvent.getId());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(executableTest.getExecutionDate());
        long alarmTime = calendar.getTimeInMillis();

        AlarmManager manager = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
        AlarmManagerUtils.setAlarm(pendingIntent, alarmTime, manager);
    }

    public void cancelAlarm(long id) {
        LOGGER.debug("Cancelling delayed test alarm");

        PendingIntent pendingIntent = getPendingIntent(id);
        AlarmManager manager = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
    }

    private PendingIntent getPendingIntent(long id) {
        Intent intent = new Intent(ACTION_EXECUTE_TEST);
        intent.putExtra(EXTRA_SCHEDULED_EVENT, id);

        int requestCode = (int) id;
        return PendingIntent.getBroadcast(application, requestCode, intent, 0);
    }

    private Single<ScheduledEventResult> persistEvent(ExecutableTest executableTest) {
        LOGGER.debug("Persisting delayed test into database");

        ScheduledEventDao scheduledEventDao = application.getDatabaseHelper().createScheduledEventDao();

        String testData = getTestDataAsString(executableTest.getData());

        ScheduledEvent scheduledEvent = new ScheduledEvent();
        scheduledEvent.setTestData(testData);
        scheduledEvent.setStartDate(executableTest.getExecutionDate().getTime());
        scheduledEvent.setTestId(executableTest.getData().getTestId());
        return scheduledEventDao.saveScheduledEvent(scheduledEvent);
    }

    private ScheduledEvent loadEvent(ExecutableTest executableTest) {
        LOGGER.debug("Loading delayed test from database");

        ScheduledEventDao scheduledEventDao = application.getDatabaseHelper().createScheduledEventDao();
        String data = getTestDataAsString(executableTest.getData());

        return scheduledEventDao.findScheduledEventFromData(data);
    }

    private String getTestDataAsString(TestData data) {
        TestParser parser = new TestParser();
        return parser.stringify(data, false);
    }
}

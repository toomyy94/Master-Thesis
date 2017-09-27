package pt.ptinovacao.arqospocket.core.maintenance;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.settings.SharedPreferencesManager;
import pt.ptinovacao.arqospocket.core.utils.AlarmManagerUtils;
import pt.ptinovacao.arqospocket.persistence.ExecutingEventDao;

/**
 * Manager class to clean and maintain the database.
 * <p>
 * Created by Emílio Simões on 01-06-2017.
 */
public class DatabaseMaintenanceManager {

    public static final String ACTION_EXECUTE_CLEANUP =
            "pt.ptinovacao.arqospocket.core.maintenance.action.EXECUTE_CLEANUP";

    private static final int ID_EXECUTE_CLEANUP = 0x420042;

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseMaintenanceManager.class);

    private static DatabaseMaintenanceManager instance;

    private final CoreApplication application;

    private DatabaseMaintenanceManager(CoreApplication application) {
        this.application = application;
    }

    /**
     * Gets the manager instance.
     *
     * @param application the application instance.
     * @return the manager instance.
     */
    public static DatabaseMaintenanceManager getInstance(CoreApplication application) {
        if (instance == null) {
            instance = new DatabaseMaintenanceManager(application);
        }
        return instance;
    }

    /**
     * Clear executing event entries from the database that are older than the provided number of days and have been
     * reported to the management service.
     */
    public void cleanOldExecutingEntries() {
        SharedPreferencesManager manager = SharedPreferencesManager.getInstance(application);
        int interval = manager.getDatabaseCleanupInterval();

        ExecutingEventDao executingEventDao = application.getDatabaseHelper().createExecutingEventDao();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, -interval);

        executingEventDao.deleteEntriesOlderThan(calendar.getTimeInMillis()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer count) throws Exception {
                LOGGER.debug("Deleted {} old logs from the database", count);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                LOGGER.error("Error deleting old entries from the database", throwable);
            }
        });

        scheduleNextDatabaseCleanup(manager);
    }

    private void scheduleNextDatabaseCleanup(SharedPreferencesManager preferencesManager) {
        Intent intent = new Intent(ACTION_EXECUTE_CLEANUP);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(application, ID_EXECUTE_CLEANUP, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        long alarmTime = calendar.getTimeInMillis();

        AlarmManager manager = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
        AlarmManagerUtils.setAlarm(pendingIntent, alarmTime, manager);

        preferencesManager.setNextDatabaseCleanupDate(calendar.getTime());
        LOGGER.debug("Next database cleanup schedule to {}", calendar.getTime());
    }
}

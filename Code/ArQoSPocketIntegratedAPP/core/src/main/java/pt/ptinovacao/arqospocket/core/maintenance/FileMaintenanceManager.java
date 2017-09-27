package pt.ptinovacao.arqospocket.core.maintenance;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Calendar;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.settings.SharedPreferencesManager;
import pt.ptinovacao.arqospocket.core.utils.AlarmManagerUtils;
import pt.ptinovacao.arqospocket.core.voicecall.audio.AudioRecorderManager;
import pt.ptinovacao.arqospocket.persistence.ExecutingEventDao;
import pt.ptinovacao.arqospocket.persistence.models.TaskEvent;

/**
 * Manager class to clean and maintain the files.
 * <p>
 * Created by Pedro Simões on 05-07-2017.
 */
public class FileMaintenanceManager {

    public static final String ACTION_EXECUTE_CLEANUP =
            "pt.ptinovacao.arqospocket.core.maintenance.action.EXECUTE_CLEANUP_FIILE";

    private static final int ID_EXECUTE_CLEANUP = 0x420041;

    private static final Logger LOGGER = LoggerFactory.getLogger(FileMaintenanceManager.class);

    private static FileMaintenanceManager instance;

    private final CoreApplication application;

    private FileMaintenanceManager(CoreApplication application) {
        this.application = application;
    }

    /**
     * Gets the manager instance.
     *
     * @param application the application instance.
     * @return the manager instance.
     */
    public static FileMaintenanceManager getInstance(CoreApplication application) {
        if (instance == null) {
            instance = new FileMaintenanceManager(application);
        }
        return instance;
    }

    /**
     * Clear executing event entries from the database that are older than the provided number of days and have been
     * reported to the management service.
     */
    public void cleanOldExecutingEntries() {
        SharedPreferencesManager manager = SharedPreferencesManager.getInstance(application);
        int interval = manager.getFileCleanupInterval();

        ExecutingEventDao executingEventDao = application.getDatabaseHelper().createExecutingEventDao();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, -interval);

        executingEventDao.listEntriesOlderThan(calendar.getTimeInMillis()).subscribe(new Consumer<TaskEvent>() {
            @Override
            public void accept(@NonNull final TaskEvent taskEvent) throws Exception {

                if (!Strings.isNullOrEmpty(taskEvent.getNameFile())) {
                    deleteAllFilesIgnoreExtensions(taskEvent.getNameFile());
                }
            }
        });
        scheduleNextFileCleanup(manager);
    }

    private void deleteAllFilesIgnoreExtensions(final String nameFile) {
        LOGGER.debug("Is it remove " + nameFile);

        final File folder = new File(AudioRecorderManager.checkDirectory());
        final File[] files = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(final File dir, final String name) {
                String[] parts = nameFile.split("\\.");
                return name.matches(parts[0] + "\\..*");
            }
        });
        for (final File file : files) {
            if (!file.delete()) {
                LOGGER.debug("Can't remove " + file.getAbsolutePath());
            } else {
                LOGGER.debug("Removed " + file.getAbsolutePath());
            }
        }
    }

    private void scheduleNextFileCleanup(SharedPreferencesManager preferencesManager) {
        Intent intent = new Intent(ACTION_EXECUTE_CLEANUP);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(application, ID_EXECUTE_CLEANUP, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        long alarmTime = calendar.getTimeInMillis();

        AlarmManager manager = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
        AlarmManagerUtils.setAlarm(pendingIntent, alarmTime, manager);

        preferencesManager.setNextFileCleanupDate(calendar.getTime());
        LOGGER.debug("Next file cleanup schedule to {}", calendar.getTime());
    }
}

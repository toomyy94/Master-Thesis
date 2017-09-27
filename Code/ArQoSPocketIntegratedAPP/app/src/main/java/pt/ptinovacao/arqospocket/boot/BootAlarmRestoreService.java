package pt.ptinovacao.arqospocket.boot;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.BaseIntentService;
import pt.ptinovacao.arqospocket.core.maintenance.DatabaseMaintenanceManager;
import pt.ptinovacao.arqospocket.core.maintenance.FileMaintenanceManager;
import pt.ptinovacao.arqospocket.core.producers.TestProducer;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in a service on a separate handler thread.
 */
public class BootAlarmRestoreService extends BaseIntentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BootAlarmRestoreService.class);

    private static final String ACTION_RESTORE_ALARMS = "pt.ptinovacao.arqospocket.boot.action.RESTORE_ALARMS";

    public BootAlarmRestoreService() {
        super("BootAlarmRestoreService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If the service is already performing a task
     * this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionRestoreAlarms(Context context) {
        Intent intent = new Intent(context, BootAlarmRestoreService.class);
        intent.setAction(ACTION_RESTORE_ALARMS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_RESTORE_ALARMS.equals(action)) {
                handleActionRestoreAlarms();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionRestoreAlarms() {
        LOGGER.debug("Executing BOOT alarm service");
        TestProducer.getBootInstance().executeOneShot();
        DatabaseMaintenanceManager.getInstance(getArqosApplication()).cleanOldExecutingEntries();
        FileMaintenanceManager.getInstance(getArqosApplication()).cleanOldExecutingEntries();
    }
}

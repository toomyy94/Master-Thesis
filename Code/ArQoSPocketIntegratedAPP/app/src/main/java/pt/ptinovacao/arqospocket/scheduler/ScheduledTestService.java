package pt.ptinovacao.arqospocket.scheduler;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.BaseIntentService;
import pt.ptinovacao.arqospocket.core.maintenance.DatabaseMaintenanceManager;
import pt.ptinovacao.arqospocket.core.maintenance.FileMaintenanceManager;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in a service on a separate handler thread.
 * <p>
 * Created by Emílio Simões on 12-04-2017.
 */
public class ScheduledTestService extends BaseIntentService {

    private static final String ACTION_EXECUTE_TEST = "pt.ptinovacao.arqospocket.scheduler.action.EXECUTE_TEST";

    private static final String ACTION_EXECUTE_CLEANUP = "pt.ptinovacao.arqospocket.scheduler.action.EXECUTE_CLEANUP";

    private static final String EXTRA_SCHEDULED_EVENT = "pt.ptinovacao.arqospocket.scheduler.extra.SCHEDULED_EVENT";

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTestService.class);

    public ScheduledTestService() {
        super("ScheduledTestService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If the service is already performing a task
     * this action will be queued.
     *
     * @param context the current context.
     * @param scheduledEventId the scheduled event ID.
     * @see IntentService
     */
    public static void startActionExecuteTest(Context context, long scheduledEventId) {
        Intent intent = new Intent(context, ScheduledTestService.class);
        intent.setAction(ACTION_EXECUTE_TEST);
        intent.putExtra(EXTRA_SCHEDULED_EVENT, scheduledEventId);
        context.startService(intent);
    }

    public static void startActionExecuteCleanup(Context context) {
        Intent intent = new Intent(context, ScheduledTestService.class);
        intent.setAction(ACTION_EXECUTE_CLEANUP);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_EXECUTE_TEST.equals(action)) {
                long scheduledEventId = intent.getLongExtra(EXTRA_SCHEDULED_EVENT, -1L);
                if (scheduledEventId > 0) {
                    handleActionScheduleEvent(scheduledEventId);
                } else {
                    LOGGER.warn("Received invalid scheduled event ID: {}", scheduledEventId);
                }
            } else if (ACTION_EXECUTE_CLEANUP.equals(action)) {
                handleActionDatabaseCleanup();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided parameters.
     *
     * @param scheduledEventId the scheduled event ID.
     */
    private void handleActionScheduleEvent(long scheduledEventId) {
        LOGGER.debug("Executing scheduled test: {}", scheduledEventId);
        getArqosApplication().getOnDemandTestManager().executeOnDemand(scheduledEventId, true);
    }

    private void handleActionDatabaseCleanup() {
        LOGGER.debug("Executing database cleanup for tests older than {} days", 3);
        DatabaseMaintenanceManager.getInstance(getArqosApplication()).cleanOldExecutingEntries();
        FileMaintenanceManager.getInstance(getArqosApplication()).cleanOldExecutingEntries();
    }
}

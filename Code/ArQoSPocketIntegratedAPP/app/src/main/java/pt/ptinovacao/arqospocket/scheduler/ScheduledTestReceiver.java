package pt.ptinovacao.arqospocket.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.core.TestAlarmManager;
import pt.ptinovacao.arqospocket.core.maintenance.DatabaseMaintenanceManager;

/**
 * A {@link BroadcastReceiver} subclass which listens for test alarms.
 * <p>
 * Created by Emílio Simões on 12-04-2017.
 */
public class ScheduledTestReceiver extends BroadcastReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTestReceiver.class);

    @Override
    public void onReceive(Context context, Intent intent) {
        if (TestAlarmManager.ACTION_EXECUTE_TEST.equals(intent.getAction())) {
            long scheduledEventId = intent.getLongExtra(TestAlarmManager.EXTRA_SCHEDULED_EVENT, -1L);
            if (scheduledEventId > 0) {
                ScheduledTestService.startActionExecuteTest(context.getApplicationContext(), scheduledEventId);
            } else {
                LOGGER.warn("Received invalid scheduled event ID: {}", scheduledEventId);
            }
        } else if (DatabaseMaintenanceManager.ACTION_EXECUTE_CLEANUP.equals(intent.getAction())) {
            LOGGER.debug("Executing database cleanup");
            ScheduledTestService.startActionExecuteCleanup(context.getApplicationContext());
        }
    }
}

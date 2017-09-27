package pt.ptinovacao.arqospocket.boot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.settings.SharedPreferencesManager;

public class BootInitializationReceiver extends BroadcastReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(BootInitializationReceiver.class);

    @Override
    public void onReceive(Context context, Intent intent) {
        LOGGER.debug("Starting boot service to reload the alarms");

        final CoreApplication applicationContext = ((CoreApplication) context.getApplicationContext());

        if (SharedPreferencesManager.getInstance(applicationContext).getAutomaticallyRunTests()) {
            BootAlarmRestoreService.startActionRestoreAlarms(context.getApplicationContext());
        }
    }
}

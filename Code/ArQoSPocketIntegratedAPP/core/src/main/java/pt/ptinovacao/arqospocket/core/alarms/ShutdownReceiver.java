package pt.ptinovacao.arqospocket.core.alarms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.utils.BatteryUtils;

public class ShutdownReceiver extends BroadcastReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownReceiver.class);

    @Override
    public void onReceive(Context context, Intent intent) {

        final CoreApplication applicationContext = ((CoreApplication) context.getApplicationContext());
        if(intent.getAction().equals("android.intent.action.ACTION_SHUTDOWN")
                || intent.getAction().equals("android.intent.action.QUICKBOOT_POWEROFF")) {
            //Check the shutdown cause
            if (BatteryUtils.getPercentage(applicationContext) < 2) AlarmsManager.getInstance(applicationContext).generateAlarm(AlarmUtils.INICIO, AlarmType.A047.name(), AlarmType.A047.getAlarmContent());
            else AlarmsManager.getInstance(applicationContext).generateAlarm(AlarmUtils.INICIO, AlarmType.A046.name(), AlarmType.A046.getAlarmContent());

        }
    }
}

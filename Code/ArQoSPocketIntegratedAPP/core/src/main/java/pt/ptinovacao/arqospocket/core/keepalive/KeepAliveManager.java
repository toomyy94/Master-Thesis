package pt.ptinovacao.arqospocket.core.keepalive;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

import pt.ptinovacao.arqospocket.core.BuildConfig;
import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.backoff.BackOffManager;
import pt.ptinovacao.arqospocket.core.settings.SharedPreferencesManager;
import pt.ptinovacao.arqospocket.core.utils.AlarmManagerUtils;

/**
 * Created by pedro on 16/05/2017.
 */
public class KeepAliveManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeepAliveManager.class);

    private final CoreApplication application;

    private static KeepAliveManager instance;

    public static String BROADCAST_RECEIVER_KEEP_ALIVE_RECEIVER = "pt.ptinovacao.arqospocket.core.notify.keepalives";

    public static final String ACTION_EXECUTE_AUTO_DISCOVERY =
            BuildConfig.APPLICATION_ID + ".ACTION_EXECUTE_AUTO_DISCOVERY";

    public static final String ACTION_EXECUTE_KEEP_ALIVE = BuildConfig.APPLICATION_ID + ".ACTION_EXECUTE_KEEP_ALIVE";

    private static final int ACTION_EXECUTE_KEEP_ALIVE_ID = 0;

    public static final int TIME_INTERVAL_KEEP_ALIVE = 60000;

    private static final int TIME_INTERVAL_AUTO_DISCOVERY = 10000;

    private boolean connectedWithSG = false;

    private boolean isRunning = false;

    private boolean stateConnection = false;

    private KeepAliveManager(CoreApplication application) {
        this.application = application;
    }

    public synchronized static KeepAliveManager getInstance(CoreApplication application) {
        if (instance == null) {
            instance = new KeepAliveManager(application);
        }
        return instance;
    }

    public void start() {

        if (!SharedPreferencesManager.getInstance(application).getConnectionWithMSManual()) {
            LOGGER.debug("Not started keepalive, it's blocked by user on the setting.");
            return;
        }

        if (isRunning()) {
            return;
        }

        if (isConnectedWithSG()) {
            createAlarmKeepAlive();
        } else {
            createAlarmAutoDiscovery();
        }

        sendBroadcastReceiver(application);
    }

    public void reset() {
        stop();
        setConnectedWithSG(false);
        start();
    }

    public void stop() {
        LOGGER.debug("Cancelling keepAlive");

        PendingIntent pendingIntent = getPendingIntent();
        AlarmManager manager = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);

        BackOffManager.getInstance(application).stop();

        setRunning(false);
        setStateConnection(false);

        sendBroadcastReceiver(application);
    }

    public static void sendBroadcastReceiver(Context context) {
        Intent intent = new Intent();
        intent.setAction(BROADCAST_RECEIVER_KEEP_ALIVE_RECEIVER);
        context.sendBroadcast(intent);
    }

    private void createAlarmAutoDiscovery() {
        LOGGER.debug("Starting Alarm Manager auto discovery");

        createAlarm(TIME_INTERVAL_AUTO_DISCOVERY);
    }

    private void createAlarmKeepAlive() {
        LOGGER.debug("Starting Alarm Manager keep Alive");

        createAlarm(TIME_INTERVAL_KEEP_ALIVE);
    }

    public synchronized boolean isRunning() {
        return isRunning;
    }

    public synchronized void setRunning(boolean running) {
        isRunning = running;
    }

    private void createAlarm(int timeDelay) {
        LOGGER.debug("Creating keepAlive");

        PendingIntent pendingIntent = getPendingIntent();

        Calendar calendar = Calendar.getInstance();
        long alarmTime = calendar.getTimeInMillis() + timeDelay;

        AlarmManager manager = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
        AlarmManagerUtils.setAlarm(pendingIntent, alarmTime, manager);
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(connectedWithSG == true ? ACTION_EXECUTE_KEEP_ALIVE : ACTION_EXECUTE_AUTO_DISCOVERY);

        return PendingIntent.getBroadcast(application, ACTION_EXECUTE_KEEP_ALIVE_ID, intent, 0);
    }

    public synchronized boolean isConnectedWithSG() {
        return connectedWithSG;
    }

    public synchronized void setConnectedWithSG(boolean connectedWithSG) {
        this.connectedWithSG = connectedWithSG;
    }

    public synchronized boolean isStateConnection() {
        return stateConnection;
    }

    public synchronized void setStateConnection(boolean stateConnection) {
        this.stateConnection = stateConnection;
    }
}

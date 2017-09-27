package pt.ptinovacao.arqospocket.core.backoff;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

import pt.ptinovacao.arqospocket.core.BuildConfig;
import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.keepalive.KeepAliveManager;
import pt.ptinovacao.arqospocket.core.network.NetworkUtils;
import pt.ptinovacao.arqospocket.core.settings.SharedPreferencesManager;
import pt.ptinovacao.arqospocket.core.utils.AlarmManagerUtils;
import pt.ptinovacao.arqospocket.core.utils.RandomNumberUtils;

/**
 * Created by pedro on 17/05/2017.
 */
public class BackOffManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackOffManager.class);

    public static final String ACTION_EXECUTE_BACK_OFF_INTENT = BuildConfig.APPLICATION_ID + ".ACTION_EXECUTE_BACK_OFF";

    public static final int MIN_BACK_OFF = 5;

    public static final int MAX_BACK_OFF = 160;

    private static final int ACTION_EXECUTE_BACK_OFF = 7234;

    private static BackOffManager instance;

    private final CoreApplication application;

    private int timeDelay = BackOffManager.MIN_BACK_OFF;

    private boolean isRunning = false;

    private SharedPreferencesManager sharedPreferencesManager;

    private BackOffManager(CoreApplication application) {
        this.application = application;
        sharedPreferencesManager = SharedPreferencesManager.getInstance(application);
    }

    public synchronized static BackOffManager getInstance(CoreApplication application) {
        if (instance == null) {
            instance = new BackOffManager(application);
        }
        return instance;
    }

    public void start() {
        if (KeepAliveManager.getInstance(application).isConnectedWithSG() && !isRunning() &&
                sharedPreferencesManager.getLastIp().size() > 1) {
            setRunning(true);
            createAlarm();
        }
    }

    public void stop() {
        LOGGER.debug("Cancelling backoff ip update");

        PendingIntent pendingIntent = getPendingIntent();
        AlarmManager manager = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);

        timeDelay = MIN_BACK_OFF;
        setRunning(false);
    }

    public synchronized boolean isRunning() {
        return isRunning;
    }

    public synchronized void setRunning(boolean running) {
        isRunning = running;
    }

    public static void setIpAddress(CoreApplication coreApplication) {

        String currentIpAddress = NetworkUtils.getIPAddress(true);

        SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(coreApplication);

        if (!Strings.isNullOrEmpty(currentIpAddress)) {
            sharedPreferencesManager.addLastIp(currentIpAddress);

            if (sharedPreferencesManager.getLastIp().size() > 1) {
                BackOffManager.getInstance(coreApplication).start();
            }
        }
    }

    public void createAlarm() {

        PendingIntent pendingIntent = getPendingIntent();

        Calendar calendar = Calendar.getInstance();
        long alarmTime = calendar.getTimeInMillis() + (timeDelay * 1000);

        AlarmManager manager = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
        AlarmManagerUtils.setAlarm(pendingIntent, alarmTime, manager);

        addTimeOfSchedule();
    }

    public void setTimeDelay(int timeDelay) {
        this.timeDelay = timeDelay;
    }

    public void resetTimeDelay() {
        this.timeDelay = MIN_BACK_OFF;
    }

    private void addTimeOfSchedule() {
        if (timeDelay < MAX_BACK_OFF) {
            LOGGER.debug(timeDelay + "");
            timeDelay += RandomNumberUtils.randInt0Until10();
            LOGGER.debug(timeDelay + "");
        }
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(ACTION_EXECUTE_BACK_OFF_INTENT);
        return PendingIntent.getBroadcast(application, ACTION_EXECUTE_BACK_OFF, intent, 0);
    }
}

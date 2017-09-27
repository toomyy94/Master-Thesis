package pt.ptinovacao.arqospocket.core.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.Build;

/**
 * Utility methods to be used with the {@link AlarmManager}.
 * <p>
 * Created by pedro on 17/05/2017.
 */
public class AlarmManagerUtils {

    /**
     * Sets and alarm checking for SDK version and using always the more applicable setAlarm method.
     *
     * @param pendingIntent the pending intent to use in the alarm.
     * @param alarmTime the time the alarm will occur.
     * @param manager the {@link AlarmManager}.
     */
    public static void setAlarm(PendingIntent pendingIntent, long alarmTime, AlarmManager manager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        } else {
            manager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        }
    }
}

package PTInov.IEX.ArQoSPocket.ApplicationSettings;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsStore {
	
	private static Context myContext = null;
	
	private final static String SharedPreferencesID = "SettingsStore";
	
	private final static String myAlarmDir = "SettingsStore.alarmDir";
	private final static String activeAlarm = "SettingsStore.alarmActive";
	private final static String historySize = "SettingsStore.historySize";
	private final static String keepalive = "SettingsStore.keepalive";
	
	
	private final static String myAlarmDirDefaultValue = "/sdcard/alarmsound.wav"; // falta meter istoooo................................
	private final static int defaultHistorySize = 20;

	public SettingsStore(Context c) {
		
		SharedPreferences prefs = c.getSharedPreferences(SharedPreferencesID, Context.MODE_PRIVATE);
		
		if (!prefs.contains(myAlarmDir)) {
			// add default value
			// falta meter isto..............................................................
			prefs.edit().putString(myAlarmDir, myAlarmDirDefaultValue).commit();
		}
		
		if (!prefs.contains(activeAlarm)) {
			// add default value
			prefs.edit().putBoolean(activeAlarm, true).commit();
		}
		
		if (!prefs.contains(historySize)) {
			// add default value
			prefs.edit().putInt(historySize, defaultHistorySize).commit();
		}
		
		if (!prefs.contains(keepalive)) {
			// add default value
			prefs.edit().putBoolean(keepalive, true).commit();
		}
		
		myContext = c;
	}
	
	
	public static void setAlarmDir(String s) {
		SharedPreferences prefs = myContext.getSharedPreferences(SharedPreferencesID, Context.MODE_PRIVATE);
		
		prefs.edit().putString(myAlarmDir, s).commit();
	}
	
	public static void setActive(boolean b) {
		SharedPreferences prefs = myContext.getSharedPreferences(SharedPreferencesID, Context.MODE_PRIVATE);
		
		prefs.edit().putBoolean(activeAlarm, b).commit();
	}
	
	public static void setHistorySize(int newSize) {
		SharedPreferences prefs = myContext.getSharedPreferences(SharedPreferencesID, Context.MODE_PRIVATE);
		
		prefs.edit().putInt(historySize, newSize).commit();
	}
	
	public static void setkeepaliveState(boolean newKeepaliveState) {
		SharedPreferences prefs = myContext.getSharedPreferences(SharedPreferencesID, Context.MODE_PRIVATE);
		
		prefs.edit().putBoolean(keepalive, newKeepaliveState).commit();
	}
	
	
	
	public static boolean alarmActive() {
		SharedPreferences prefs = myContext.getSharedPreferences(SharedPreferencesID, Context.MODE_PRIVATE);

		return prefs.getBoolean(activeAlarm, true);
	}
	
	
	public static String getAlarmDir() {
		SharedPreferences prefs = myContext.getSharedPreferences(SharedPreferencesID, Context.MODE_PRIVATE);
		
		return prefs.getString(myAlarmDir, myAlarmDirDefaultValue);
	}
	
	public static int getHistorySize() {
		SharedPreferences prefs = myContext.getSharedPreferences(SharedPreferencesID, Context.MODE_PRIVATE);
		
		return prefs.getInt(historySize, defaultHistorySize);
	}
	
	public static boolean getkeepaliveState() {
		SharedPreferences prefs = myContext.getSharedPreferences(SharedPreferencesID, Context.MODE_PRIVATE);
		
		return prefs.getBoolean(keepalive, true);
	}
}

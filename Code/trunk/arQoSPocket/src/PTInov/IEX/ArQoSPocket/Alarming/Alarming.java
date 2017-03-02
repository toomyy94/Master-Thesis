package PTInov.IEX.ArQoSPocket.Alarming;

import PTInov.IEX.ArQoSPocket.ApplicationSettings.SettingsStore;
import PTInov.IEX.ArQoSPocket.UserInterface.HistoryInterface;
import PTInov.IEX.ArQoSPocket.UserInterface.MainInterface;
import PTInov.IEX.ArQoSPocket.UserInterface.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import android.app.Notification;
import android.app.NotificationManager;

public class Alarming {
	
	private static String tag = "Alarming";
	
	private static int NOTIFICATION = 12569844;
	
	private static String from = "ArQoS Pocket";
	private static String text = "Test ERROR";
	
	
	public static void showNotificationMessage(Context myContext, NotificationManager mNM) {
		// In this sample, we'll use the same text for the ticker and the
		// expanded notification
		
		Log.d(tag, "showNotificationMessage :: In");
		
		SettingsStore ss = new SettingsStore(myContext);
		
		if (SettingsStore.alarmActive()) {

		Log.d(tag, "Show Notification 1");
		
		// Set the icon, scrolling text and timestamp
		Notification notification = new Notification(R.drawable.arqosicon,
				from, System.currentTimeMillis());
		// notification.defaults |= Notification.DEFAULT_SOUND;
		
		Log.d(tag, "Show Notification 2");

		notification.sound = Uri.parse("file://" + SettingsStore.getAlarmDir());
		//notification.sound = Uri.parse("file:///sdcard/alarmsound.wav");
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		long[] vibrate = { 0, 100, 200, 300, 200, 500, 100, 1000 };
		notification.vibrate = vibrate;
		notification.flags |= Notification.FLAG_INSISTENT;
		
		Log.d(tag, "Show Notification 3");

		// The PendingIntent to launch our activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(
				myContext.getApplicationContext(), 0, new Intent(myContext.getApplicationContext(),
						HistoryInterface.class), 0);
		
		Log.d(tag, "Show Notification 4");

		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(myContext, from, text, contentIntent);
		
		Log.d(tag, "Show Notification 5");

		// Send the notification.
		// Log.d(TAG, "Cancel previous notifications");
		// mNM.cancel(NOTIFICATION);
		Log.d(tag, "Show new notification");
		mNM.notify(NOTIFICATION, notification);
		
		Log.d(tag, "showNotificationMessage :: Out with notification");
		
		
		} else {
			
			Log.d(tag, "showNotificationMessage :: Out without notification");
			return;
		}
	}
}

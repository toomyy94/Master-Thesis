package pt.ptinovacao.arqospocket.service.alarm;

import java.util.Calendar;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyAlarmManager {

	private final static Logger logger = LoggerFactory.getLogger(MyAlarmManager.class);
	
	private BroadcastReceiver br;
	private AlarmManager alarmManager;
	private PendingIntent pendingIntent;
	
	private static AlarmIDMapping alarm_ID_Mapping = null;
	private static TreeMap<Integer, IAlarm> CallBackRefMap = null;
	
	private Context context = null;
	
	
	public MyAlarmManager(Context context) {
		final String method = "MyAlarmManager";
		
		try {
			
			MyLogger.trace(logger, method, "In");
		
			this.context = context;
			
			alarm_ID_Mapping = new AlarmIDMapping();
			CallBackRefMap = new TreeMap<Integer, IAlarm>();
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		MyLogger.trace(logger, method, "Out");
	}
	
	
	private boolean mapping_ID(int int_id, String string_id) {
		final String method = "mapping_ID";
		
		try {
			
			MyLogger.trace(logger, method, "In");
		
			if (alarm_ID_Mapping == null)
				return false;
			
			boolean mapping_result = alarm_ID_Mapping.mapping_ID(int_id, string_id);
			MyLogger.debug(logger, method, "mapping_result :"+mapping_result);
			
			return mapping_result;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		MyLogger.trace(logger, method, "Out");
		return false;
	}
	
	private static String get_mapped_string_id(int int_id) {
		final String method = "get_mapped_string_id";
		
		String resultValue = null;
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			if (alarm_ID_Mapping == null)
				return null;
			
			resultValue = alarm_ID_Mapping.get_and_remove_string_id(int_id);
			MyLogger.debug(logger, method, "resultValue :"+resultValue);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		MyLogger.trace(logger, method, "Out");
		return resultValue;
	}
	
	public static void new_alarm(int alarm_id) {
		final String method = "new_alarm";
		
		try {
		
			String idString = get_mapped_string_id(alarm_id);
			MyLogger.debug(logger, method, "idString :"+idString);
			
			CallBackRefMap.get(alarm_id).new_alarm(idString);
			MyLogger.trace(logger, method, "Callback done!");
		
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		MyLogger.trace(logger, method, "Out");
	}
	
	
	
	
	public void setAlarm(Calendar calendar, String id, IAlarm callbackRef) {
		final String method = "MyAlarmManager";
		
		try {
			MyLogger.trace(logger, method, "In");
			
			Intent startIntent = new Intent();
			startIntent.setAction("pt.ptinovacao.arqospocket.service.alarm.AlarmReceiver");
			
			MyLogger.debug(logger, method, "id: " + id);
			
			int intent_id = (int) (System.currentTimeMillis()*System.currentTimeMillis());
			MyLogger.debug(logger, method, "intent_id: " + intent_id);
			
			mapping_ID(intent_id, id);
			CallBackRefMap.put(intent_id, callbackRef);
			
			startIntent.putExtra("id", intent_id);
			pendingIntent = PendingIntent.getBroadcast(context, intent_id, startIntent, 0);
			
			if (pendingIntent == null)
				MyLogger.debug(logger, method, "(pendingIntent == null)"); 
	     
			alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);			
			
			MyLogger.debug(logger, method, "set alarm at :"+calendar.getTime());
			alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
			
			MyLogger.trace(logger, method, "Done!");
		
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		MyLogger.trace(logger, method, "Out");
	}
	
	/*
	public void setAlarmDaily(Calendar calendar, Calendar endCalendar, String id) {
		final String method = "setAlarmDaily";
		
		
		try {
			MyLogger.trace(logger, method, "In");
			//String id = id;
	     
			
			//Intent myIntent = new Intent(context, AlarmReceiver.class);
			//pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent,0);
			Intent startIntent = new Intent();
			startIntent.setAction("AlarmReceiver");
			
			MyLogger.debug(logger, method, "ID: " + id);
			id = id.replaceAll("[^\\d]", "");

			if (id.length()>9)
				id = id.substring(0, 9);
			
			MyLogger.debug(logger, method, "Convertido ID: " + id);
			
			
			int intent_id = Integer.parseInt(id);
			MyLogger.debug(logger, method, "Convertido ID int: " + intent_id);
			//int intent_id = (int) (System.currentTimeMillis()*System.currentTimeMillis());

			pendingIntent = PendingIntent.getBroadcast(context, intent_id, startIntent, 0);
			
			if (pendingIntent == null)
				MyLogger.debug(logger, method, "(pendingIntent == null)"); 
	     
			alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);			
			
			MyLogger.debug(logger, method, "set alarm at :"+calendar.getTime() + "with id: " + intent_id);
			
			// RTC WAKEUP gasta mais bateria mas acorda os dispositivos
			
			alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),24*60*60*1000, pendingIntent);
			
			MyLogger.trace(logger, method, "Done!");
		
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		MyLogger.trace(logger, method, "Out");
	}
	*/

}

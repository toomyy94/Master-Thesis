package pt.ptinovacao.arqospocket.service.alarm;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver{
	
	private final static Logger logger = LoggerFactory.getLogger(AlarmReceiver.class);
	
    @Override
    public void onReceive(Context context, Intent intent){
    	final String method = "AlarmReceiver";
    	
    	try {
    		MyLogger.trace(logger, method, "In");
    	
    		int id = intent.getIntExtra("id", -1);
    		MyLogger.trace(logger, method, "id :"+id);
    		
    		 
    		MyAlarmManager.new_alarm(id);
    		
    		
    		/*
    		IAndsfConnector iAndsfConnector = ANDSFConnector.getInstance();
    	
    		if (iAndsfConnector == null)
    			Logger.v(className, method, LogType.Debug, "iAndsfConnector == null");
    		else {
    			Logger.v(className, method, LogType.Debug, "iAndsfConnector != null");
    			
    			iAndsfConnector.internalGetConnectionType();
    		}
    		
    		ArrayList<String> alarmsToDelete = databaseHelper.getAllPastAlarms();
    		cancelAlarms(context, alarmsToDelete);
    		*/

    	} catch(Exception ex) {
    		MyLogger.error(logger, method, ex);
    	}
    	
    	MyLogger.trace(logger, method, "Out");
    }
    
    public static void cancelAlarms(Context context, ArrayList<String> alarmsToDelete) {
    	final String method = "cancelAlarms";
    	
    	try {
    		
    		MyLogger.trace(logger, method, "In");
    		
    		PendingIntent pendingIntent;
    		AlarmManager alarmManager ;
    		for(String id: alarmsToDelete) {
    			Intent startIntent = new Intent();
    			startIntent.setAction("pt.ptinovacao.arqospocket.service.alarm.AlarmReceiver");
    			pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(id), startIntent, 0);
    			alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);			
    			alarmManager.cancel(pendingIntent);

    		}
    		
    	} catch(Exception ex) {
    		MyLogger.error(logger, method, ex);
    	}
    	
    	MyLogger.trace(logger, method, "Out");
    }
}

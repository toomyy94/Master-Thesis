package pt.ptinovacao.arqospocket.service.store;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.service.AnomaliesHistory;
import pt.ptinovacao.arqospocket.service.service.RadiologsHistory;
import pt.ptinovacao.arqospocket.service.service.TestHistory;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;


public class PrivateStore {

	private final static Logger logger = LoggerFactory.getLogger(PrivateStore.class);
	
	private final static String SharedPreferencesID = "PrivateAPPArQoSPocket";
	private final static String Current_Configuration = "PrivateAPPArQoSPocket.CurrentConfiguration";
	private final static String Test_History = "PrivateAPPArQoSPocket.TestHistory";
	private final static String Anomalies_History = "PrivateAPPArQoSPocket.AnomaliesHistory";
	private final static String Radiologs_History = "PrivateAPPArQoSPocket.RadiologsHistory";


	private static Context myContext = null;
	
	public PrivateStore(Context c) {
		final String method = "PrivateStore";
		
		try {
			
			myContext = c;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	
	public static boolean save_test_history(TestHistory object) {
		final String method = "save_test_history";
		
		try {
			
			MyLogger.debug(logger, method, "In - object:"+object.toString());
			
			SharedPreferences prefs = myContext.getSharedPreferences(SharedPreferencesID, Context.MODE_PRIVATE);
			boolean result = prefs.edit().putString(Test_History, toString(object)).commit();
			
			MyLogger.debug(logger, method, "Out - result:"+result);
			
			return result;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);;
		}
		return false;
	}
	
	public static TestHistory load_test_history() {
		String method = "load_test_history";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			SharedPreferences prefs = myContext.getSharedPreferences(SharedPreferencesID, Context.MODE_PRIVATE);
			String result = prefs.getString(Test_History, null);
			
			MyLogger.debug(logger, method, "result:"+result);
			
			if (result != null)  {
				
				TestHistory testHistory = ((TestHistory) fromString(result));
			
				MyLogger.debug(logger, method, "testHistory:"+testHistory.toString());
				
				return testHistory;
			}
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);;
		}
		
		return null;
	}
	
	public static boolean save_anomalies_history(AnomaliesHistory object) {
		final String method = "save_anomalies_history";
		
		try {
			
			MyLogger.debug(logger, method, "In - object:"+object.toString());
			
			SharedPreferences prefs = myContext.getSharedPreferences(SharedPreferencesID, Context.MODE_PRIVATE);
			boolean result = prefs.edit().putString(Anomalies_History, toString(object)).commit();
			
			MyLogger.debug(logger, method, "Out - result:"+result);
			
			return result;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);;
		}
		return false;
	}
	
	public static AnomaliesHistory load_anomalies_history() {
		String method = "load_anomalies_history";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			SharedPreferences prefs = myContext.getSharedPreferences(SharedPreferencesID, Context.MODE_PRIVATE);
			String result = prefs.getString(Anomalies_History, null);
			
			MyLogger.debug(logger, method, "result:"+result);
			
			if (result != null)  {
			
			    AnomaliesHistory anomaliesHistory = (AnomaliesHistory) fromString(result);
				
				MyLogger.debug(logger, method, "anomaliesHistory:"+anomaliesHistory.toString());
				
				return anomaliesHistory;
			}
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);;
		}
		
		return null;
	}

	public static boolean save_radiologs_history(RadiologsHistory object) {
		final String method = "save_radiologs_history";

		try {

			MyLogger.debug(logger, method, "In - object:"+object.toString());

			SharedPreferences prefs = myContext.getSharedPreferences(SharedPreferencesID, Context.MODE_PRIVATE);
			boolean result = prefs.edit().putString(Radiologs_History, toString(object)).commit();

			MyLogger.debug(logger, method, "Out - result:"+result);

			return result;

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);;
		}
		return false;
	}

	public static RadiologsHistory load_radiologs_history() {
		String method = "load_anomalies_history";

		try {

			MyLogger.trace(logger, method, "In");

			SharedPreferences prefs = myContext.getSharedPreferences(SharedPreferencesID, Context.MODE_PRIVATE);
			String result = prefs.getString(Radiologs_History, null);

			MyLogger.debug(logger, method, "result:"+result);

			if (result != null)  {

				RadiologsHistory radiologsHistory = (RadiologsHistory) fromString(result);

				MyLogger.debug(logger, method, "radiologsHistory:"+radiologsHistory.toString());

				return radiologsHistory;
			}

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);;
		}

		return null;
	}
	
	public static boolean setCurrentConfiguration(CurrentConfiguration object) {
		final String method = "setCurrentConfiguration";
		
		try {
			
			MyLogger.debug(logger, method, "In - object:"+object.toString());
			
			SharedPreferences prefs = myContext.getSharedPreferences(SharedPreferencesID, Context.MODE_PRIVATE);
			boolean result = prefs.edit().putString(Current_Configuration, toString(object)).commit();
			
			MyLogger.debug(logger, method, "Out - result:"+result);
			
			return result;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);;
		}
		return false;
	}
	
	public static CurrentConfiguration getCurrentConfiguration() {
		String method = "getCurrentConfiguration";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			SharedPreferences prefs = myContext.getSharedPreferences(SharedPreferencesID, Context.MODE_PRIVATE);
			String result = prefs.getString(Current_Configuration, null);
			
			MyLogger.debug(logger, method, "result:"+result);
			
			if (result != null)  {
				
				CurrentConfiguration currentConfiguration = (CurrentConfiguration) fromString(result);
				
				MyLogger.debug(logger, method, "currentConfiguration:"+currentConfiguration.toString());
				
				return currentConfiguration;
			} else {
				return new CurrentConfiguration();
			}
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
	
	private static Object fromString( String s ) {
		final String method = "fromString";
		
		Object o = null;
		
		try {
		
			byte [] data = Base64.decode(s, Base64.NO_PADDING);
		
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(  data ) );
			o  = ois.readObject();
			ois.close();
		
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return o;
	}


	private static String toString( Serializable o ) {
		final String method = "toString";
		
		String returnString = null;
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			MyLogger.trace(logger, method, "1");
			ObjectOutputStream oos = new ObjectOutputStream( baos );
			MyLogger.trace(logger, method, "2");
			oos.writeObject( o );
			MyLogger.trace(logger, method, "3");
			oos.close();
			MyLogger.trace(logger, method, "4");

			byte[] objectByte = Base64.encode(baos.toByteArray(), Base64.NO_PADDING);
			
			MyLogger.trace(logger, method, "5");
			
			returnString = new String( objectByte );
		
			MyLogger.trace(logger, method, "6");
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return returnString;
	}
}

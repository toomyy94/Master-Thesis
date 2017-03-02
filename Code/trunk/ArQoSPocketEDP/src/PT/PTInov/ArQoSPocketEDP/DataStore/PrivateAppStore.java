package PT.PTInov.ArQoSPocketEDP.DataStore;

import PT.PTInov.ArQoSPocketEDP.Utils.LogType;
import PT.PTInov.ArQoSPocketEDP.Utils.Logger;
import android.content.Context;
import android.content.SharedPreferences;

public class PrivateAppStore {
	
	private final static String tag = "PrivateAppStore";
	
	private final static String SharedPreferencesID = "PrivateAPPStoreEMon";
	private final static String Report_Mail = "PrivateAPPStoreEMon.REPORTMAIL";
	
	private static SharedPreferences prefs;
	private static Context myContext = null;
	
	public PrivateAppStore(Context c) {
		
		prefs = c.getSharedPreferences(SharedPreferencesID, Context.MODE_PRIVATE);
		
		myContext = c;
	}
	
	public static boolean setReportMail(String mail) {
		String methodName = "setReportMail";
		
		try {
			SharedPreferences prefs = myContext.getSharedPreferences(SharedPreferencesID, Context.MODE_PRIVATE);
			prefs.edit().putString(Report_Mail, mail).commit();
			return true;
			
		} catch(Exception ex) {
			Logger.v(tag, methodName, LogType.Error, ex.toString());
		}
		return false;
	}
	
	public static String getReportMail() {
		String methodName = "getSnipeTime";
		
		try {
			
			SharedPreferences prefs = myContext.getSharedPreferences(SharedPreferencesID, Context.MODE_PRIVATE);
			return prefs.getString(Report_Mail, null);
			
		} catch(Exception ex) {
			Logger.v(tag, methodName, LogType.Error, ex.toString());
		}
		
		return null;
	}
}

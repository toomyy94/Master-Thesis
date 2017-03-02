package PT.PTInov.ArQoSPocketEDP.Utils;

import java.sql.SQLException;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class APNManager {
	
	private final static String tag = "APNManager";
	
	// Application Context
	private Context appContext = null;

	 /* 
     * Information of all APNs
     * Details can be found in com.android.providers.telephony.TelephonyProvider
     */
	public static final Uri APN_TABLE_URI = Uri.parse("content://telephony/carriers");
	
	 /* 
     * Information of the preferred APN
     * 
     */
	public static final Uri APN_PREFER_URI = Uri.parse("content://telephony/carriers/preferapn");
	
	
	
	
	public APNManager(Context appContext) {
		this.appContext = appContext;
	}
	
	 /*
     * Enumerate all APN data
     */
    public void EnumerateAPNs() {
    	String method = "EnumerateAPNs";
    	
    	try{
    		
    		Logger.v(tag, method, LogType.Debug, "In");
    		
    		Cursor   cur = appContext.getContentResolver().query(APN_TABLE_URI, null, null, null, null);
    	
    	    if (cur != null) {

    	        if (cur.moveToFirst()) {
    	        	
    	        	do {
    	        		
    	        		Logger.v(tag, method, LogType.Debug, "----------------------------------");
    	        		for (String columnName :cur.getColumnNames()) {
    	        			Logger.v(tag, method, LogType.Debug, "columnName :"+columnName+" value :"+cur.getString(cur.getColumnIndex(columnName)));
    	        		}
    	        		Logger.v(tag, method, LogType.Debug, "----------------------------------");
    	        		
    	        	} while (cur.moveToNext());
    	        }
    	        cur.close();
    	    }
 
    	} catch(Exception ex) {
    		Logger.v(tag, method, LogType.Error, ex.toString());
    	}
    }
    
    /*
     * Add APN date
     * 
     * Insert a new APN entry into the system APN table
     * Require an apn name, and the apn address. More can be added.
     * Return an id (_id) that is automatically generated for the new apn entry.
     */
	public int addNewAPN(String name, String apn_addr, String numeric, String mcc, String mnc) {
		String method = "addNewAPN1";
		
		Logger.v(tag, method, LogType.Debug, "In");
		
		int id = -1;
		
		ContentResolver resolver = appContext.getContentResolver();
		ContentValues values = new ContentValues();
		
		values.put("name", name);
		values.put("apn", apn_addr);
		values.put("mcc", mcc);
		values.put("mnc", mnc);
		values.put("numeric", numeric);
	        
		Cursor c = null;
		try {
			Uri newRow = resolver.insert(APN_TABLE_URI, values);
			if (newRow != null) {
				
				Logger.v(tag, method, LogType.Debug, "APN inserted");
				
				c = resolver.query(newRow, null, null, null, null);
				c.moveToFirst();
				
				Logger.v(tag, method, LogType.Debug, "Inserted Information:");
				for (String columnName :c.getColumnNames()) {
        			Logger.v(tag, method, LogType.Debug, "columnName :"+columnName+" value :"+c.getString(c.getColumnIndex(columnName)));
        		}

				// Obtain the apn id
				int idindex = c.getColumnIndex("_id");
				Logger.v(tag, method, LogType.Debug, "idindex :"+idindex);
				
				id = c.getShort(idindex);
				Logger.v(tag, method, LogType.Debug, "New ID: " + id + ": Inserting new APN succeeded!");

			}
		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}

		if (c != null)
			c.close();
		
		Logger.v(tag, method, LogType.Debug, "return :"+id);
		
		return id;
	}
	
	public int addNewAPN(String _id,String name, String numeric, String mcc, String	mnc, String apn,
			String user, String server, String password, String proxy, String port, String mmsproxy,
			String mmsport, String mmsc, String authtype, String type, String current, String protocol,
			String roaming_protocol, String carrier_enabled, String bearer) {
		String method = "addNewAPN2";
		
		return 0;
	}
	
	 /*
     * Set an apn to be the default apn for web traffic
     * Require an input of the apn id to be set
     */
    public boolean SetDefaultAPN(int id) {
    	String method = "SetDefaultAPN";
    	
    	Logger.v(tag, method, LogType.Debug, "In - id:"+id);
    	
        boolean res = false;
        ContentResolver resolver = appContext.getContentResolver();
        ContentValues values = new ContentValues();
        
        //See /etc/apns-conf.xml. The TelephonyProvider uses this file to provide 
        //content://telephony/carriers/preferapn URI mapping
        
        values.put("apn_id", id); 
        try
        {
            if (resolver.update(APN_PREFER_URI, values, null, null) == 1) {
            	Cursor c = resolver.query(APN_PREFER_URI, new String[]{"name","apn"}, "_id="+id, null, null);
            	if(c != null) {
            		res = true;
            		c.close();
            	}
            }
        }
        catch (Exception ex) {
        	Logger.v(tag, method, LogType.Error, ex.toString());
        }
        
        Logger.v(tag, method, LogType.Debug, "return :"+res);
        
         return res;
    }
    
}

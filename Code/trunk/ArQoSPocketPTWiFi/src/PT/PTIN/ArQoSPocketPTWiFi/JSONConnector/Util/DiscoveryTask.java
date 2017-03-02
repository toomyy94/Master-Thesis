package PT.PTIN.ArQoSPocketPTWiFi.JSONConnector.Util;

import java.net.URL;

import PT.PTIN.ArQoSPocketPTWiFi.JSONConnector.ArQoSConnector;
import PT.PTIN.ArQoSPocketPTWiFi.Logs.LogType;
import PT.PTIN.ArQoSPocketPTWiFi.Logs.Logger;
import android.os.AsyncTask;

public class DiscoveryTask extends AsyncTask<ArQoSConnector, Void, Void> {
	
	private final static String tag ="DiscoveryTask";

	@Override
	protected Void doInBackground(ArQoSConnector... params) {
		
		try {
			
			Logger.v(tag, LogType.Trace, "EStou akiiiiiiiiiiiiiiiiiiiiiiii!!!!!!!");
		
		int count = params.length;
        long totalSize = 0;
        for (int i = 0; i < count; i++) {
            params[i].discoveryDevice();
        }
        
		return null;
		
		} catch(Exception ex) {
		}
		
		return null;
	}


}

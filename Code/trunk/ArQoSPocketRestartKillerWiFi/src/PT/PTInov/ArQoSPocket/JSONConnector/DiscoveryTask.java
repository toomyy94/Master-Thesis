package PT.PTInov.ArQoSPocket.JSONConnector;

import java.net.URL;

import PT.PTInov.ArQoSPocket.JSONConnector.ArQoSConnector;
import android.os.AsyncTask;

public class DiscoveryTask extends AsyncTask<ArQoSConnector, Void, Void> {

	@Override
	protected Void doInBackground(ArQoSConnector... params) {
		
		try {
			
		
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

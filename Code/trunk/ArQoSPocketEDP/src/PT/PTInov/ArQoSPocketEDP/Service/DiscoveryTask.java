package PT.PTInov.ArQoSPocketEDP.Service;

import java.net.URL;

import PT.PTInov.ArQoSPocketEDP.DataStructs.EnumTestE2EState;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.ArQoSConnector;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.DiscoveryEnum;
import PT.PTInov.ArQoSPocketEDP.Utils.LogType;
import PT.PTInov.ArQoSPocketEDP.Utils.Logger;
import android.annotation.SuppressLint;
import android.os.AsyncTask;

@SuppressLint({ "ParserError", "ParserError" })
public class DiscoveryTask extends AsyncTask<ArQoSConnector, Void, DiscoveryEnum> {
	
	private final static String tag = "DiscoveryTask";
	private ArQoSConnector serverConnector = null;
	private EngineServiceInterface calbackInterface = null;

	public DiscoveryTask(ArQoSConnector serverConnector, EngineServiceInterface calbackInterface) {
		this.serverConnector = serverConnector;
		this.calbackInterface = calbackInterface;
	}

	@Override
	protected DiscoveryEnum doInBackground(ArQoSConnector... params) {
		String methodName = "doInBackground";

		try {

			return serverConnector.discoveryDevice();

		} catch (Exception ex) {
			Logger.v(tag, methodName, LogType.Error, "Error"+ex.toString());
		}

		Logger.v(tag, methodName, LogType.Trace, "Out with false");
		
		return DiscoveryEnum.NOK;
	}

	protected void onPostExecute(DiscoveryEnum result){
		String methodName = "onPostExecute";
		
		try {
			
			Logger.v(tag, methodName, LogType.Trace, "In");
		
			if (calbackInterface != null){
				
				Logger.v(tag, methodName, LogType.Trace, "doCallBack");
				
				calbackInterface.setDescoveryStatus(result);
			}
		
		} catch(Exception ex) {
			Logger.v(tag, methodName, LogType.Error, "Error"+ex.toString());
		}
		
		Logger.v(tag, methodName, LogType.Trace, "Out");
	}
}

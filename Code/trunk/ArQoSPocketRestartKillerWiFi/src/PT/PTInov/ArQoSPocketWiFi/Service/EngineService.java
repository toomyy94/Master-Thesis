package PT.PTInov.ArQoSPocketWiFi.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import PT.PTInov.ArQoSPocket.JSONConnector.ArQoSConnector;
import PT.PTInov.ArQoSPocket.JSONConnector.ConnectorCallBackInterface;
import PT.PTInov.ArQoSPocket.JSONConnector.DiscoveryTask;
import PT.PTInov.ArQoSPocketWiFi.UI.ArQoSPocketRestartKillerWiFiActivity;
import PT.PTInov.ArQoSPocketWiFi.Utils.CircularStore;
import PT.PTInov.ArQoSPocketWiFi.Utils.LogType;
import PT.PTInov.ArQoSPocketWiFi.Utils.Logger;
import PT.PTInov.ArQoSPocketWiFi.Utils.StoreInformation;
import PT.PTInov.ArQoSPocketWiFi.Utils.WifiInformation;
import android.app.Service;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.IBinder;

public class EngineService extends Service implements EngineServiceInterface, ConnectorCallBackInterface {
	
	private final static String tag = "EngineService";
	private final static int MAX_ARRAY_STORE_SIZE = 20;
	
	//private static String serverHost = "http://10.112.80.72:9180";  //desenvolvimento - blade 7
	//private static String serverHost = "http://10.112.29.43:9180";   // projecto lte tmn qsr
	//private static String serverHost = "http://10.112.85.102:9180";  // teste de integração APP2
	private static String serverHost = "http://172.20.2.21:9180";     // PTC QA
	
	private static String ip = "172.20.2.21";
	private static String port = "9180";
	
	// false - stopped, true - running
	private boolean runState = false; 
	
	// Information
	private GPSInformation GPSInfo = null;
	private WiFiInformation WiFiInfo = null;
	
	private CircularStore myStore = null;
	
	// ArQoS server connector
	private ArQoSConnector serverConnector = null;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Logger.v(tag, "onStartCommand", LogType.Trace, "In");

		// warning the UI that already started
		try {
			ArQoSPocketRestartKillerWiFiActivity.serviceAlreadyStart(this);
		} catch(Exception ex) {
			Logger.v(tag, "onStartCommand", LogType.Error, ex.toString());
		}
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		String method = "onCreate";
		
		try {
			Logger.v(tag, method, LogType.Trace, "In");
		
			// Start the GPS Location
			// Load the gsp module, and try get the location updated
			GPSInfo = new GPSInformation(this);
        
			// Start load the wifi information
			WiFiInfo = new WiFiInformation(this);
        
			// Create the circular store
			myStore = new CircularStore(MAX_ARRAY_STORE_SIZE);
        
			// init the connector
			serverConnector = new ArQoSConnector(this, serverHost,this);
			
			// send discovery
        	//serverConnector.discoveryDevice();
        	new DiscoveryTask().execute(serverConnector);
        	
        	setServerHost(ip, port);
			
		} catch(Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
	}
	
	
	public List<StoreInformation> getMyHistory() {
		return myStore.getAllElems();
	}
	
	public void refreshInformation() {
	
		try {
		
			// refresh the WiFi List
			WiFiInfo.doScan();

		} catch(Exception ex) {
			
		}
	}
	
	public boolean saveActualInformation(String userListLocation, Date testExecDate) {
		
		try {
			
			Date dScan = new Date();
			
			// refresh the WiFi List
			WiFiInfo.doScan();
			
			try {
				Thread.sleep(2000);
			} catch(Exception ex) {
				
			}
			
			// get Wifi information
			WifiInformation wi = WiFiInfo.doScan();
			
			StoreInformation newStoreInformation = new StoreInformation(GPSInfo.getLatitude(), GPSInfo.getLongitude(), testExecDate, userListLocation, wi.getScanWifiNetWorks());
			
			// send information to management server
			if (serverConnector.sendInformation(newStoreInformation, dScan)) {
				newStoreInformation.setSended();
			}
			
			// if sended with success
			if (wi.getScanWifiNetWorks() != null) {
				
				newStoreInformation.setSuccess();
			}
			
			// Save the information on service store
			myStore.addElem(newStoreInformation);
			
			Logger.v(tag, "saveActualInformation", LogType.Debug, "-- Log Information ----------------------------------------------------");
			
			if (GPSInfo != null) {
				Logger.v(tag, "saveActualInformation", LogType.Debug, "Latitude :" + GPSInfo.getLatitude());
				Logger.v(tag, "saveActualInformation", LogType.Debug, "Longitude :" + GPSInfo.getLongitude());
			}
			
			if (WiFiInfo != null) {
				Logger.v(tag, "saveActualInformation", LogType.Debug, "scanDate :" + wi.getDate());
				Logger.v(tag, "saveActualInformation", LogType.Debug, "ScanWifiNetWorks :" + wi.getScanWifiNetWorks().toString());
			}
			
			Logger.v(tag, "saveActualInformation", LogType.Debug, "-----------------------------------------------------------------------");
			
			// only for debug in test app
			Logger.v(tag, "saveActualInformation", LogType.Debug, myStore.toString());
			
			
			return true;
			
		} catch(Exception ex) {
			Logger.v(tag, "saveActualInformation", LogType.Error, ex.toString());
		}
		
		
		return false;
	}
	
	
	public List<StoreInformation> getAllResult() {
		return myStore.getAllElems();
	}
	
	public void setServerHost(String ip, String port) {
		this.ip = ip;
		this.port = port;
		serverHost = "http://"+ip+":"+port;
	}
	
	public String getPort() {
		return port;
	}
	
	public String getIP() {
		return ip;
	}
}

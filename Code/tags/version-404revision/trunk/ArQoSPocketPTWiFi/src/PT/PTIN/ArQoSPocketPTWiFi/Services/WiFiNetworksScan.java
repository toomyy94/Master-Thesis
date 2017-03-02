package PT.PTIN.ArQoSPocketPTWiFi.Services;

import java.util.List;

import PT.PTIN.ArQoSPocketPTWiFi.Logs.LogType;
import PT.PTIN.ArQoSPocketPTWiFi.Logs.Logger;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class WiFiNetworksScan {
	
	private final static String tag = "WiFiNetworksScan";
	
	private WifiManager wifi = null;
	private Context serviceContext = null;
	
	public WiFiNetworksScan(Context c) {
		Logger.v(tag, LogType.Trace,"WiFiNetworksScan :: Creat a instance of TurnWiFiONorOFF");
		
		try {
			
			serviceContext = c;
			
		} catch(Exception ex) {
			Logger.v(tag, LogType.Error,"WiFiNetworksScan :: Error getting wifiManager :"+ex.toString());
		}
	}
	
	public List<ScanResult> doWiFiScan() {
		
		try {
			
			wifi = (WifiManager) serviceContext.getSystemService(Context.WIFI_SERVICE);
			
			wifi.startScan();
			
			return wifi.getScanResults();
			
		} catch(Exception ex) {
			Logger.v(tag, LogType.Error,"WiFiNetworksScan :: Error do a scan :"+ex.toString());
		}
		
		return null;
	}
	
}

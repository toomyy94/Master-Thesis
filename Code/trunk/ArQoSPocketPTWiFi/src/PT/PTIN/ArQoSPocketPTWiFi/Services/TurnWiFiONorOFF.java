package PT.PTIN.ArQoSPocketPTWiFi.Services;

import PT.PTIN.ArQoSPocketPTWiFi.Enums.WiFiModuleState;
import PT.PTIN.ArQoSPocketPTWiFi.Logs.LogType;
import PT.PTIN.ArQoSPocketPTWiFi.Logs.Logger;
import android.content.Context;
import android.net.wifi.WifiManager;


public class TurnWiFiONorOFF {
	
	private final static String tag = "TurnWiFiONorOFF";
	
	private WifiManager wifi = null;
	private Context serviceContext = null;
	
	public TurnWiFiONorOFF(Context c) {
		
		Logger.v(tag, LogType.Trace,"TurnWiFiONorOFF :: Creat a instance of TurnWiFiONorOFF");
		
		try {
			
			serviceContext = c;
			
		} catch(Exception ex) {
			Logger.v(tag, LogType.Error,"TurnWiFiONorOFF :: Error :"+ex.toString());
		}
	}
	
	public WiFiModuleState turnWiFiON() {
		
		try {
			
			wifi = (WifiManager) serviceContext.getSystemService(Context.WIFI_SERVICE);
			
			if (!wifi.isWifiEnabled()) {
				if (wifi.setWifiEnabled(true)) {
					return WiFiModuleState.ON;
				} else {
					return WiFiModuleState.OFF;
				}
			}
			
			return WiFiModuleState.ALREADYON;
		
		} catch(Exception ex) {
			Logger.v(tag, LogType.Error,"turnWiFiON :: Error :"+ex.toString());
		}
		
		return WiFiModuleState.ERROR;
	}
	
	public WiFiModuleState trunWiFiOFF() {
		
		try {
			
			wifi = (WifiManager) serviceContext.getSystemService(Context.WIFI_SERVICE);
			
			if (wifi.isWifiEnabled()) {
				if (wifi.setWifiEnabled(false)) {
					return WiFiModuleState.OFF;
				} else {
					return WiFiModuleState.ON;
				}
			}
			
			return WiFiModuleState.ALREADYOFF;
			
		} catch(Exception ex) {
			Logger.v(tag, LogType.Error,"trunWiFiOFF :: Error :"+ex.toString());
		}
		
		return WiFiModuleState.ERROR;
	}

}

package PT.PTInov.ArQoSPocket.Service;

import java.util.Date;
import java.util.List;

import PT.PTInov.ArQoSPocket.Enums.ActionState;
import PT.PTInov.ArQoSPocket.Enums.WiFiModuleState;
import PT.PTInov.ArQoSPocket.Utils.LogType;
import PT.PTInov.ArQoSPocket.Utils.Logger;
import PT.PTInov.ArQoSPocket.structs.AssociationResultState;
import android.content.Context;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

public class WiFiService {

private final static String tag = "WiFiService";
	
	private WifiManager wifi = null;
	private Context serviceContext = null;
	
	public WiFiService(Context c) {
		
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
	
	public AssociationResultState Association(String SSID) {
		
		try {
			
			Logger.v(tag, LogType.Trace,"Association :: In");
			
			wifi = (WifiManager) serviceContext.getSystemService(Context.WIFI_SERVICE);
			
			Logger.v(tag, LogType.Debug,"Association :: getSystemService WIFI");
			
			// get all configured wifi network available
			List<WifiConfiguration> confWiFiList = wifi.getConfiguredNetworks();
			
			Logger.v(tag, LogType.Debug,"Association :: getConfiguredNetworks");
			
			for (WifiConfiguration wc :confWiFiList) {
				
				Logger.v(tag, LogType.Debug,"Association :: wc.SSID :"+wc.SSID.replace("\"", ""));
				Logger.v(tag, LogType.Debug,"Association :: SSID :"+SSID);
				
				if (wc.SSID.replace("\"", "").equals(SSID)) {
					
					Logger.v(tag, LogType.Debug,"Association :: enableNetwork");
					
					long waitTime = 60000;
					long beginTime = (new Date()).getTime();
					
					if (wifi.enableNetwork(wc.networkId, true)) {
						
						// espera que a ligação esteja disponivel
						// o tempo de espera é limitado a x segundos
						
						while (!wifi.getConnectionInfo().getSupplicantState().equals(SupplicantState.COMPLETED) && waitTime>0) {
							
							try {
								Thread.sleep(2000);
								waitTime = waitTime - 1000;
							} catch(Exception ex) {
								
							}
							
						}
						
						long endTime = (new Date()).getTime();
						
						if (wifi.getConnectionInfo().getSupplicantState().equals(SupplicantState.COMPLETED)) {
							
							boolean pingSupplicant = wifi.pingSupplicant();
							
							Logger.v(tag, LogType.Debug,"Association :: pingSupplicant :"+pingSupplicant +" in :"+((20000 - waitTime)/1000)+" secounds");
							
							
							return  (new AssociationResultState( ActionState.OK, endTime - beginTime));
						} else {
							
							Logger.v(tag, LogType.Debug,"Association :: enableNetwork return false!");
							return  (new AssociationResultState( ActionState.NOTOK, endTime - beginTime));
						}
					}
					
					Logger.v(tag, LogType.Debug,"Association :: wifi.getConnectionInfo().getSupplicantState() !=SupplicantState.COMPLETED");
					return (new AssociationResultState( ActionState.NOTOK, 0));
					
				}
			}
			
			Logger.v(tag, LogType.Debug,"Association :: no SSID :"+SSID+" found!");
			return  (new AssociationResultState( ActionState.NOTOK, 0));
			
		} catch(Exception ex) {
			Logger.v(tag, LogType.Error,"Association :: Error :"+ex.toString());
			return (new AssociationResultState( ActionState.NOTOK, 0));
		}
	}

	public boolean Reassociation() {
		
		try {
			
			wifi = (WifiManager) serviceContext.getSystemService(Context.WIFI_SERVICE);
			return wifi.reassociate();
			
		} catch(Exception ex) {
			Logger.v(tag, LogType.Error,"Reassociation :: Error :"+ex.toString());
			return false;
		}
	}
	
	public boolean Disconnect() {
		// TODO:
		
		try {
			
			wifi = (WifiManager) serviceContext.getSystemService(Context.WIFI_SERVICE);
			return wifi.disconnect();
			
		} catch(Exception ex) {
			Logger.v(tag, LogType.Error,"Disconnect :: Error :"+ex.toString());
			return false;
		}
	}
}

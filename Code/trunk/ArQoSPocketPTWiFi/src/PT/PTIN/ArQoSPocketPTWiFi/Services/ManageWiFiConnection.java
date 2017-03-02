package PT.PTIN.ArQoSPocketPTWiFi.Services;

import java.util.Date;
import java.util.List;

import PT.PTIN.ArQoSPocketPTWiFi.Enums.ActionState;
import PT.PTIN.ArQoSPocketPTWiFi.Logs.LogType;
import PT.PTIN.ArQoSPocketPTWiFi.Logs.Logger;
import PT.PTIN.ArQoSPocketPTWiFi.Utils.AssociationResultState;
import android.content.Context;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

public class ManageWiFiConnection {
	
	private final static String tag = "ManageWiFiConnection";
	
	private WifiManager wifi = null;
	private Context serviceContext = null;
	
	public ManageWiFiConnection(Context c) {
		
		Logger.v(tag, LogType.Trace,"ManageWiFiConnection :: Creat a instance of ManageWiFiConnection");
		
		try {
			
			serviceContext = c;
			
		} catch(Exception ex) {
			Logger.v(tag, LogType.Error,"ManageWiFiConnection :: Error :"+ex.toString());
		}
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

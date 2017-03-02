package pt.ptinovacao.arqospocket.service.tasks;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.tasks.enums.WiFiModuleState;
import pt.ptinovacao.arqospocket.service.tasks.structs.AssociationResultState;

import android.content.Context;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

public class WiFiService {

	private final static Logger logger = LoggerFactory.getLogger(WiFiService.class);
	
	private WifiManager wifi = null;
	private Context serviceContext = null;
	
	public WiFiService(Context c) {
		final String method = "WiFiService";
		
		MyLogger.trace(logger, method, "IN");
		
		try {
			
			serviceContext = c;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	
	public WiFiModuleState turnWiFiON() {
		final String method = "turnWiFiON";
		
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
			MyLogger.error(logger, method, ex);
		}
		
		return WiFiModuleState.ERROR;
	}
	
	public WiFiModuleState trunWiFiOFF() {
		final String method = "trunWiFiOFF";
		
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
			MyLogger.error(logger, method, ex);
		}
		
		return WiFiModuleState.ERROR;
	}
	
	public AssociationResultState Association(String SSID) {
		final String method = "Association";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			wifi = (WifiManager) serviceContext.getSystemService(Context.WIFI_SERVICE);
			
			MyLogger.trace(logger, method, "getSystemService WIFI");
			
			// get all configured wifi network available
			List<WifiConfiguration> confWiFiList = wifi.getConfiguredNetworks();
			
			MyLogger.trace(logger, method, "getConfiguredNetworks");
			
			for (WifiConfiguration wc :confWiFiList) {
				
				MyLogger.debug(logger, method, "wc.SSID :"+wc.SSID.replace("\"", ""));
				MyLogger.debug(logger, method, "SSID :"+SSID);
				
				if (wc.SSID.replace("\"", "").equals(SSID)) {
					
					MyLogger.trace(logger, method, "enableNetwork");
					
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
							
							MyLogger.debug(logger, method, "pingSupplicant :"+pingSupplicant +" in :"+((20000 - waitTime)/1000)+" secounds");
							
							return  (new AssociationResultState( AssociationResultState.ActionState.OK, endTime - beginTime));
						} else {
							
							MyLogger.trace(logger, method, "enableNetwork return false!");
							return  (new AssociationResultState( AssociationResultState.ActionState.NOTOK, endTime - beginTime));
						}
					}
					
					MyLogger.trace(logger, method, "wifi.getConnectionInfo().getSupplicantState() != SupplicantState.COMPLETED");
					return (new AssociationResultState( AssociationResultState.ActionState.NOTOK, 0));
					
				}
			}
			
			MyLogger.debug(logger, method, "no SSID :"+SSID+" found!");
			return  (new AssociationResultState( AssociationResultState.ActionState.NOTOK, 0));
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
			return (new AssociationResultState( AssociationResultState.ActionState.NOTOK, 0));
		}
	}

	public boolean Reassociation() {
		final String method = "Reassociation";
		
		try {
			
			wifi = (WifiManager) serviceContext.getSystemService(Context.WIFI_SERVICE);
			return wifi.reassociate();
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
			return false;
		}
	}
	
	public boolean Disconnect() {
		final String method = "Disconnect";
		
		try {
			
			wifi = (WifiManager) serviceContext.getSystemService(Context.WIFI_SERVICE);
			return wifi.disconnect();
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
			return false;
		}
	}
}

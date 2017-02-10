package pt.ptinovacao.arqospocket.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.enums.EConnectionTechnology;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

public class ConnectionInfo {

	private final static Logger logger = LoggerFactory.getLogger(ConnectionInfo.class);
	
	public static EConnectionTechnology get_active_connection(Context context) {
		final String method = "get_active_connection";
		
		try {
			
			// verifica se esta pela rede movel
			try {
				
				TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
				
				if (telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED)
					return EConnectionTechnology.MOBILE;
				
			} catch (Exception ex) {
				MyLogger.error(logger, method, ex);
			}
			
			// verifica se esta pela rede wifi
			try {
				
				WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				
				if (wifiManager.getConnectionInfo() != null)
					return EConnectionTechnology.WIFI;
				
			} catch (Exception ex) {
				MyLogger.error(logger, method, ex);
			}
			
			return EConnectionTechnology.NOT_CONNECTED;
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return EConnectionTechnology.NA;
	}
}

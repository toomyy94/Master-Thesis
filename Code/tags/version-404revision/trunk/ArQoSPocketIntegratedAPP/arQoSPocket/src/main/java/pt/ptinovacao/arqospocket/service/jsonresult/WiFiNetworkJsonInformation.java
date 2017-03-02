package pt.ptinovacao.arqospocket.service.jsonresult;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.net.wifi.ScanResult;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public class WiFiNetworkJsonInformation implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(WiFiNetworkJsonInformation.class);
	
	private String ssid;
	private int signal_level;
	private String security;
	
	public WiFiNetworkJsonInformation(String ssid, int signal_level, String security) {
		final String method = "WiFiNetworkJsonInformation";
		
		try {
			
			this.ssid = ssid;
			this.signal_level = signal_level;
			this.security = security;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public WiFiNetworkJsonInformation(ScanResult ScanResult) {
		final String method = "WiFiNetworkJsonInformation2";
		
		try {
			
			this.ssid = ScanResult.SSID;
			
			// TODO: tem que se avaliar se é preciso fazer qualquer na força de sinal
			this.signal_level = ScanResult.level;
			this.security = ScanResult.capabilities;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public String get_ssid() {
		return ssid;
	}
	
	public int get_signal_level() {
		return signal_level;
	}
	
	public String get_security() {
		return security;
	}
	

	public String buildTaskJsonResult() {
		final String method = "buildTaskJsonResult";
		
		StringBuilder sb = new StringBuilder();
		
		return sb.toString();
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\nssid :"+ssid);
			sb.append("\nsignal_level :"+signal_level);
			sb.append("\nsecurity :"+security);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
	
	public Object clone() {
		final String method = "clone";
		
		try {
			
			return new WiFiNetworkJsonInformation(ssid, signal_level, security);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
}

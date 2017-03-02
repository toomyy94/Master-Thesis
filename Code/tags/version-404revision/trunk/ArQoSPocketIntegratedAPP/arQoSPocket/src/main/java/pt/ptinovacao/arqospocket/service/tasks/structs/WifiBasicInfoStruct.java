package pt.ptinovacao.arqospocket.service.tasks.structs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.enums.EWifiState;

public class WifiBasicInfoStruct {

	private final static Logger logger = LoggerFactory.getLogger(WifiBasicInfoStruct.class);
	
	private EWifiState wireless_state;
	
	private String rx_level = null;
	private String ssid = null;
	private String channel = null;
	private String link_speed = null;
	
	public WifiBasicInfoStruct(EWifiState wireless_state, String rx_level, String ssid, String channel, String link_speed) {
		final String method = "WifiBasicInfoStruct";
		
		try {
			
			this.wireless_state = wireless_state;
			this.rx_level = rx_level;
			this.ssid = ssid;
			this.channel = channel;
			this.link_speed = link_speed;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public EWifiState get_wireless_state() {
		return wireless_state;
	}
	
	public String get_rx_level() {
		return rx_level;
	}
	
	public String get_ssid() {
		return ssid;
	}
	
	public String get_channel() {
		return channel;
	}
	
	public String get_link_speed() {
		return link_speed;
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\nwireless_state :"+wireless_state);
			sb.append("\nrx_level :"+rx_level);
			sb.append("\nssid :"+ssid);
			sb.append("\nchannel :"+channel);
			sb.append("\nlink_speed :"+link_speed);	
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
}

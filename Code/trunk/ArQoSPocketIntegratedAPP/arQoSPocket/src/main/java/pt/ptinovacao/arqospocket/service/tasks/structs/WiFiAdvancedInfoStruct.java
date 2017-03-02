package pt.ptinovacao.arqospocket.service.tasks.structs;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.net.wifi.ScanResult;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.enums.EWifiState;

public class WiFiAdvancedInfoStruct extends WifiBasicInfoStruct {
	
	private final static Logger logger = LoggerFactory.getLogger(WiFiAdvancedInfoStruct.class);
	
	private String bssid;
	private boolean hidden_ssid;
	private String mac_address;
	
	private String ip_address;
	private String dns1;
	private String dns2;
	private String gateway;
	private String lease_duration;
	private String netmask;
	private String server_address;
	
	private List<ScanResult> scan_Wifi_list;
	
	
	public WiFiAdvancedInfoStruct(EWifiState wireless_state, String rx_level, String ssid, String channel, String link_speed,
			String bssid, boolean hidden_ssid, String mac_address, String ip_address, String dns1, String dns2, String gateway, 
			String lease_duration, String netmask, String server_address, List<ScanResult> scan_Wifi_list) {
		
		super(wireless_state, rx_level, ssid, channel, link_speed);
		
			this.bssid = bssid;
			this.hidden_ssid = hidden_ssid;
			this.mac_address = mac_address;
			
			this.ip_address = ip_address;
			this.dns1 = dns1;
			this.dns2 = dns2;
			this.gateway = gateway;
			this.lease_duration = lease_duration;
			this.netmask = netmask;
			this.server_address = server_address;
			
			this.scan_Wifi_list = scan_Wifi_list;
	}
	
	public String get_bssid() {
		return bssid;
	}
	
	public boolean get_hidden_ssid() {
		return hidden_ssid;
	}
	
	public String get_mac_address() {
		return mac_address;
	}
	
	public String get_ip_address() {
		return ip_address;
	}
	
	public String get_dns1() {
		return dns1;
	}
	
	public String get_dns2() {
		return dns2;
	}
	
	public String get_gateway() {
		return gateway;
	}
	
	public String get_lease_duration() {
		return lease_duration;
	}
	
	public String get_netmask() {
		return netmask;
	}
	
	public String get_server_address() {
		return server_address;
	}
	
	public List<ScanResult> get_scan_Wifi_list() {
		return scan_Wifi_list;
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\nbssid :"+bssid);
			sb.append("\nhidden_ssid :"+hidden_ssid);
			sb.append("\nmac_address :"+mac_address);
			sb.append("\nip_address :"+ip_address);
			sb.append("\ndns1 :"+dns1);
			sb.append("\ndns2 :"+dns2);
			sb.append("\ngateway :"+gateway);
			sb.append("\nlease_duration :"+lease_duration);
			sb.append("\nnetmask :"+netmask);
			sb.append("\nserver_address :"+server_address);
			sb.append("\nscan_Wifi_list :"+scan_Wifi_list.toString());
			
			sb.append("\nWifiBasicInfoStruct :"+super.toString());

			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
}

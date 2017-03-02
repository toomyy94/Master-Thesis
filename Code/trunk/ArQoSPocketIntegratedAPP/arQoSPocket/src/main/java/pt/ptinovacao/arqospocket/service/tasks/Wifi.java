package pt.ptinovacao.arqospocket.service.tasks;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.service.enums.PskType;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.tasks.structs.WiFiAdvancedInfoStruct;
import pt.ptinovacao.arqospocket.service.tasks.structs.WifiBasicInfoStruct;
import pt.ptinovacao.arqospocket.service.tasks.utils.WiFiConfiguration;
import pt.ptinovacao.arqospocket.service.tasks.utils.WiFiTools;
import pt.ptinovacao.arqospocket.service.tasks.utils.WifiWatcher;
import pt.ptinovacao.arqospocket.service.enums.EWifiState;
import pt.ptinovacao.arqospocket.service.tasks.interfaces.IWifiCallback;
import pt.ptinovacao.arqospocket.service.tasks.utils.WiFiConnect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;



public class Wifi {

	private final static Logger logger = LoggerFactory.getLogger(Wifi.class);
	
	static final int SECURITY_NONE = 0;
    static final int SECURITY_WEP = 1;
    static final int SECURITY_PSK = 2;
    static final int SECURITY_EAP = 3;

	private EWifiState wirelessState;
	//private WifiInfoStruct2 wirelessNetwork;

	private Context context = null;
	private IWifiCallback iWifiCallback = null;
	
	private String ssid;
	private String bssid;
	private int rssi;
	private int signal;
	List<ScanResult> scan_Wifi_list = null;
	
	private WifiWatcher watcher;
	
	private static WifiManager wifiManager;
	
	
	public Wifi(Context context, IWifiCallback iWifiCallback) {
		final String method = "Wifi";
		
		try {
			
			MyLogger.trace(logger, method, "In");
	
			this.context = context;
			this.iWifiCallback = iWifiCallback;

			// wirelessNetwork = null;
			// this.ssid = null;
			// this.signal = -1;

			wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			
			this.watcher = new WifiWatcher(context, this);
			
			if (wifiManager.isWifiEnabled()) {
				enablingWifi();
				scan_Wifi_list = wifiManager.getScanResults();
			} else
				disablingWifi();
		
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
	}
	
	private int FrequencyToChannel(int frequency) {
		final String method = "FrequencyToChannel";

		int channel = -1;
		try {
			
			MyLogger.trace(logger, method, "In - frequency :"+frequency);

			switch (frequency) {
			case 2412:
				channel = 1;
				break;
			case 2417:
				channel = 2;
				break;
			case 2422:
				channel = 3;
				break;
			case 2427:
				channel = 4;
				break;
			case 2432:
				channel = 5;
				break;
			case 2437:
				channel = 6;
				break;
			case 2442:
				channel = 7;
				break;
			case 2447:
				channel = 8;
				break;
			case 2452:
				channel = 9;
				break;
			case 2457:
				channel = 10;
				break;
			case 2462:
				channel = 11;
				break;
			case 2467:
				channel = 12;
				break;
			case 2472:
				channel = 13;
				break;
			case 2484:
				channel = 14;
				break;

			}

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}

		return channel;
	}
	
	
	public WifiBasicInfoStruct get_WifiBasicInfoStruct() {
		final String method = "get_WifiBasicInfoStruct";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			String channel = context.getString(R.string.na);
			
			//MyLogger.trace(logger, method, "scan_Wifi_list :"+scan_Wifi_list.toString());
			
			// Procura o atual SSIS na lista do scan e obtem de lá a informação do canal
			for (ScanResult sr :scan_Wifi_list)
				if ((ssid != null) && (bssid != null)) {
					
					//MyLogger.trace(logger, method, "ssid :"+ssid);
					//MyLogger.trace(logger, method, "bssid :"+bssid);
					
					//MyLogger.trace(logger, method, "sr.SSID :"+sr.SSID);
					//MyLogger.trace(logger, method, "sr.BSSID :"+sr.BSSID);
					
					if (sr.SSID.replace("\"", "").equals(ssid.replace("\"", "")) && sr.BSSID.replace("\"", "").equals(bssid.replace("\"", "")))
						channel = FrequencyToChannel(sr.frequency)+"";
				}
			
			String link_speed = wifiManager.getConnectionInfo().getLinkSpeed()+"";
			
			WifiBasicInfoStruct wifiBasicInfoStruct = new WifiBasicInfoStruct(wirelessState, rssi+"", ssid, channel, link_speed);
			//MyLogger.trace(logger, method, "wifiBasicInfoStruct :"+wifiBasicInfoStruct.toString());
			
			return wifiBasicInfoStruct;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
	
	public WiFiAdvancedInfoStruct get_WiFiAdvancedInfoStruct() {
		final String method = "get_WiFiAdvancedInfoStruct";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			scan_Wifi_list = wifiManager.getScanResults();
			String channel = context.getString(R.string.na);;
			
			//MyLogger.trace(logger, method, "scan_Wifi_list :"+scan_Wifi_list.toString());
			
			// Procura o atual SSIS na lista do scan e obtem de lá a informação do canal
			for (ScanResult sr :scan_Wifi_list)
				if ((ssid != null) && (bssid != null)) {
					
					//MyLogger.trace(logger, method, "ssid :"+ssid);
					//MyLogger.trace(logger, method, "bssid :"+bssid);
					
					//MyLogger.trace(logger, method, "sr.SSID :"+sr.SSID);
					//MyLogger.trace(logger, method, "sr.BSSID :"+sr.BSSID);
					
					if (sr.SSID.replace("\"", "").equals(ssid.replace("\"", "")) && sr.BSSID.replace("\"", "").equals(bssid.replace("\"", "")))
						channel = FrequencyToChannel(sr.frequency)+"";
				}
			
			String link_speed = wifiManager.getConnectionInfo().getLinkSpeed()+"";
			String bssid = wifiManager.getConnectionInfo().getBSSID();
			boolean hidden_ssid = wifiManager.getConnectionInfo().getHiddenSSID();
			String mac_address = wifiManager.getConnectionInfo().getMacAddress();
			String ip_address = WiFiTools.convertIntToStringIP(wifiManager.getConnectionInfo().getIpAddress());
			String dns1 = WiFiTools.convertIntToStringIP(wifiManager.getDhcpInfo().dns1);
			String dns2 = WiFiTools.convertIntToStringIP(wifiManager.getDhcpInfo().dns2);
			String gateway = WiFiTools.convertIntToStringIP(wifiManager.getDhcpInfo().gateway);
			String lease_duration = wifiManager.getDhcpInfo().leaseDuration+"";
			String netmask = WiFiTools.convertIntToStringIP(wifiManager.getDhcpInfo().netmask);
			String server_address = WiFiTools.convertIntToStringIP(wifiManager.getDhcpInfo().serverAddress);
			
			WiFiAdvancedInfoStruct wiFiAdvancedInfoStruct = new WiFiAdvancedInfoStruct(wirelessState, rssi+"", ssid, channel, link_speed, bssid, hidden_ssid, 
					mac_address, ip_address, dns1, dns2, gateway, lease_duration, netmask, server_address, scan_Wifi_list);
			//MyLogger.trace(logger, method, "wiFiAdvancedInfoStruct :"+wiFiAdvancedInfoStruct.toString());
			
			return wiFiAdvancedInfoStruct;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
	public boolean isWiFiAvailable() {
		final String method = "isWiFiAvailable";
		
		try {

			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}
	
	public void connectingWifi(String ssid) {
		final String method = "connectingWifi";

		try {
			
			MyLogger.trace(logger, method, "In");

			wirelessState = EWifiState.CONNECTING;
			this.ssid = ssid;

			if (iWifiCallback != null)
				iWifiCallback.wifi_information_change();

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	
	public void disconnectingWifi() {
		final String method = "disconnectingWifi";

		try {
			
			MyLogger.trace(logger, method, "In");

			wirelessState = EWifiState.DISCONNECTING;

			// wirelessNetwork = null;

			if (iWifiCallback != null)
				iWifiCallback.wifi_information_change();

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public void disconnectedWifi() {
		final String method = "disconnectedWifi";

		try {
			
			MyLogger.trace(logger, method, "In");

			wirelessState = EWifiState.DISCONNECTED;

			this.ssid = null;
			this.signal = -1;
			this.bssid = null;

			// wirelessNetwork = null;

			if (iWifiCallback != null)
				iWifiCallback.wifi_information_change();

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public void connectedWifi(String ssid, int signal, String bssid) {
		final String method = "connectedWifi";

		try {
			
			MyLogger.trace(logger, method, "In");

			wirelessState = EWifiState.CONNECTED;

			this.ssid = ssid;

			this.signal = signal;
			this.bssid = bssid;

			// wirelessNetwork = new WifiInfoStruct2();
			// wirelessNetwork.setSSID(ssid);
			// wirelessNetwork.setBSSID(bssid);

			if (iWifiCallback != null)
				iWifiCallback.wifi_information_change();

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	public void disablingWifi() {
		final String method = "disablingWifi";

		try {
			
			MyLogger.trace(logger, method, "In");

			wirelessState = EWifiState.DISABLING;

			this.ssid = null;
			this.signal = -1;

			// wirelessNetwork = null;

			if (iWifiCallback != null)
				iWifiCallback.wifi_information_change();

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public void disabledWifi() {
		final String method = "disabledWifi";

		try {
			
			MyLogger.trace(logger, method, "In");

			wirelessState = EWifiState.DISABLED;

			this.ssid = null;
			this.signal = -1;

			// wirelessNetwork = null;

			if (iWifiCallback != null)
				iWifiCallback.wifi_information_change();

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	public void enablingWifi() {
		final String method = "enablingWifi";

		try {
			
			MyLogger.trace(logger, method, "In");

			wirelessState = EWifiState.ENABLING;

			if (iWifiCallback != null)
				iWifiCallback.wifi_information_change();

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	
	public void enabledWifi() {
		final String method = "enabledWifi";

		try {
			
			MyLogger.trace(logger, method, "In");

			wirelessState = EWifiState.ENABLED;

			if (iWifiCallback != null)
				iWifiCallback.wifi_information_change();

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public void credentialsChanged() {
		
		//if(wirelessState.equals(EWifiState.CONNECTED)) {
		//	AsyncTask_Authenticate task = new AsyncTask_Authenticate(ssid, context);
		//	task.execute();
		//}
		
		// if(service != null)
		//	service.updateState();
	}
	
	
	public EWifiState getWirelessState() {
		return wirelessState;
	}


	public void setWirelessState(EWifiState wirelessState) {
		this.wirelessState = wirelessState;
	}


	//public WifiInfoStruct2 getWirelessNetwork() {
	//	return wirelessNetwork;
	//}


	//public void setWirelessNetwork(WifiInfoStruct2 wirelessNetwork) {
	//	this.wirelessNetwork = wirelessNetwork;
	//}


	public String getSsid() {
		return ssid;
	}


	public void setSsid(String ssid) {
		final String method = "setSsid";

		try {
			
			MyLogger.trace(logger, method, "In");

			this.ssid = ssid;

			if (iWifiCallback != null)
				iWifiCallback.wifi_information_change();

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	
	public String getBssid() {
		return bssid;
	}


	public void setBssid(String bssid) {
		final String method = "setBssid";

		try {
			
			MyLogger.trace(logger, method, "In");

			this.bssid = bssid;

			if (iWifiCallback != null)
				iWifiCallback.wifi_params_change();

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}


	public int getSignal() {
		return signal;
	}


	public void setSignal(int newSignal) {
		final String method = "setSignal";

		try {
			
			MyLogger.trace(logger, method, "In");

			this.signal = newSignal;

			if (iWifiCallback != null)
				iWifiCallback.wifi_information_change();

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	
	public List<ScanResult> ScanResults(){
		final String method = "ScanResults";
		
		List<ScanResult> scanResults = null;
		
		try {
			
			MyLogger.trace(logger, method, "In");
		
			scanResults = wifiManager.getScanResults();
		
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return scanResults;
	}
	
	public List<WifiConfiguration> getConfiguredNetworks() {
		final String method = "getConfiguredNetworks";
		
		List<WifiConfiguration> resultList = null;
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			resultList = wifiManager.getConfiguredNetworks();
		
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return resultList;
	}


	public void scanFinished() {
		final String method = "scanFinished";

		try {
			
			MyLogger.trace(logger, method, "In");

			// service.forceNetworksUpdate();

			if (iWifiCallback != null)
				iWifiCallback.wifi_params_change();

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	public int getRssi() {
		return rssi;
	}


	public void setRssi(int rssi) {
		final String method = "setRssi";

		try {
			
			MyLogger.trace(logger, method, "In");

			this.rssi = rssi;

			if (iWifiCallback != null)
				iWifiCallback.wifi_information_change();

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	private int getSecurity(ScanResult result) {
		final String method = "setRssi";

		try {
			
			MyLogger.trace(logger, method, "In");

			if (result.capabilities.contains("WEP")) {
				return SECURITY_WEP;
			} else if (result.capabilities.contains("PSK")) {
				return SECURITY_PSK;
			} else if (result.capabilities.contains("EAP")) {
				return SECURITY_EAP;
			}

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
        return SECURITY_NONE;
    }
	
	public String getScanResultSecurity(ScanResult scanResult) {
		final String method = "getScanResultSecurity";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			return String.valueOf(getSecurity(scanResult));
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
	public static boolean isOpenNetwork(String security) {
		final String method = "isOpenNetwork";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			return String.valueOf(SECURITY_NONE).equals(security);
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}

	public boolean isOpenNetwork(ScanResult scanResult) {
		final String method = "isOpenNetwork";
		
		try {
			
			MyLogger.trace(logger, method, "In");
		
			String scanResultSecurity = getScanResultSecurity(scanResult);
			return  String.valueOf(SECURITY_NONE).equals(scanResultSecurity);
		
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}
	
	
	private PskType getPskType(ScanResult result) {
		final String method = "getPskType";
	
		try {
			
			MyLogger.trace(logger, method, "In");
			
			boolean wpa = result.capabilities.contains("WPA-PSK");
			boolean wpa2 = result.capabilities.contains("WPA2-PSK");
			if (wpa2 && wpa) {
				return PskType.WPA_WPA2;
			} else if (wpa2) {
				return PskType.WPA2;
			} else if (wpa) {
				return PskType.WPA;
			} else {
				MyLogger.debug(logger, method,
						"Received abnormal flag string: " + result.capabilities);
				return PskType.UNKNOWN;
			}

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return PskType.UNKNOWN;
	}
	
	public String getDisplaySecurityString(final ScanResult scanResult) {
		final String method = "getDisplaySecurityString";
		
		try {
			
			MyLogger.trace(logger, method, "In");

			final int security = getSecurity(scanResult);
			if (security == SECURITY_PSK) {
				switch (getPskType(scanResult)) {
				case WPA:
					return "WPA";
				case WPA_WPA2:
				case WPA2:
					return "WPA2";
				default:
					return "?";
				}
			} else {
				switch (security) {
				case SECURITY_NONE:
					return "OPEN";
				case SECURITY_WEP:
					return "WEP";
				case SECURITY_EAP:
					return "EAP";
				}
			}

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}

		return "?";
	}


	public String getReadableSecurity(ScanResult scanResult) {
		final String method = "getReadableSecurity";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			final String rawSecurity = getDisplaySecurityString(scanResult);
			return isOpenNetwork(rawSecurity) ? "" : rawSecurity;

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}


	public WifiInfo getWifiInfo() {
		final String method = "getWifiInfo";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			return wifiManager.getConnectionInfo();
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}

		return null;
	}


	/*
	public void removeNetwork(ScanResult scanResult) {
		String scanResultSecurity = getScanResultSecurity(scanResult);
		final WifiConfiguration config = getWifiConfiguration(wifiManager, scanResult, scanResultSecurity);
		
		if(config != null) {
			wifiManager.removeNetwork(config.networkId);
			wifiManager.saveConfiguration();
		}

	}
	*/
	
	
	public void connectToNewNetwork(ScanResult scanResult, String password, int numOpenNetworksKept) {
		final String method = "connectToNewNetwork";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			if(isOpenNetwork(getScanResultSecurity(scanResult))) {
				WiFiConnect.connectToNewNetwork(context, wifiManager, scanResult, null, numOpenNetworksKept);
			} else {
				WiFiConnect.connectToNewNetwork(context, wifiManager, scanResult, password, numOpenNetworksKept);
			}
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	
	public void connectToConfiguredNetwork(ScanResult scanResult) {
		final String method = "connectToConfiguredNetwork";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			final WifiConfiguration config = WiFiConfiguration
					.getWifiConfiguration(wifiManager, scanResult,
							WiFiConfiguration.getScanResultSecurity(scanResult));

			if (config != null) {
				WiFiConnect.connectToConfiguredNetwork(context, wifiManager,config);
			}

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}


	public void changePasswordAndConnect(ScanResult scanResult, String password,
			int numOpenNetworksKept) {
		final String method = "changePasswordAndConnect";

		try {
			
			MyLogger.trace(logger, method, "In");

			final WifiConfiguration config = WiFiConfiguration
					.getWifiConfiguration(wifiManager, scanResult,
							WiFiConfiguration.getScanResultSecurity(scanResult));

			if (config != null) {
				WiFiConnect.changePasswordAndConnect(context, wifiManager,
						config, password, numOpenNetworksKept);

			}

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	
	public boolean disableWifi() {
		final String method = "disableWifi";

		try {
			
			MyLogger.trace(logger, method, "In");
			
			boolean result = wifiManager.setWifiEnabled(false);
			MyLogger.trace(logger, method, "result :"+result);
			
			return result;
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		MyLogger.trace(logger, method, "Out");
		return false;
	}

	public boolean enableWifi() {
		final String method = "enableWifi";

		try {
			
			MyLogger.trace(logger, method, "In");
			
			boolean result = wifiManager.setWifiEnabled(true);
			MyLogger.trace(logger, method, "result :"+result);
			
			return result;
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		MyLogger.trace(logger, method, "Out");
		return false;
	}

	
	private boolean alreadyAuthenticated() {
		final String method = "alreadyAuthenticated";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			InputStreamReader is = new InputStreamReader(
					new DefaultHttpClient()
							.execute(new HttpGet("http://www.apple.com/library/test/success.html"))
							.getEntity().getContent());
			BufferedReader r = new BufferedReader(is);
			
			if(r.readLine().contains("Success")) {
				return true;
			}
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}


	public boolean startScan() {
		final String method = "startScan";

		try {
			
			MyLogger.trace(logger, method, "In");
			return wifiManager.startScan();
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}

		return false;
	}

	public List<ScanResult> getScanResults() {
		final String method = "getScanResults";

		try {
			
			MyLogger.trace(logger, method, "In");
			return wifiManager.getScanResults();
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		return null;
	}
	
	
	@SuppressWarnings("deprecation")
	@SuppressLint("InlinedApi")
	public boolean ConnectNetwork(ScanResult result, Context context) {
		final String method = "ConnectNetwork";

		try {
			
			MyLogger.trace(logger, method, "In");
			
			final String security = WiFiConfiguration.getScanResultSecurity(result);
			WifiConfiguration config = WiFiConfiguration.getWifiConfiguration(wifiManager, result, security);
		
		
			if(config == null) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
					return WiFiConnect.connectToNewNetwork(context, wifiManager, result, null,Settings.Secure.getInt(context.getContentResolver(),
							Settings.Global.WIFI_NUM_OPEN_NETWORKS_KEPT, 10));
				} else {
					return WiFiConnect.connectToNewNetwork(context, wifiManager, result, null,Settings.Secure.getInt(context.getContentResolver(),
							Settings.Secure.WIFI_NUM_OPEN_NETWORKS_KEPT, 10));
				}
			}
			else
				return WiFiConnect.connectToConfiguredNetwork(context, wifiManager, config);
		
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
 	}


	public WifiConfiguration getWifiConfiguration(ScanResult result) {
		final String method = "getWifiConfiguration";

		try {
		
			MyLogger.trace(logger, method, "In");
			
			final String security = WiFiConfiguration.getScanResultSecurity(result);
			return WiFiConfiguration.getWifiConfiguration(wifiManager, result, security);
		
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}

}

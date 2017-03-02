package pt.ptinovacao.arqospocket.service.tasks.utils;

import pt.ptinovacao.arqospocket.service.tasks.Wifi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WifiWatcher {

	private Context context;
	private WifiManager wifiManager;
	private Wifi wifi;
	
	public WifiWatcher(Context context, Wifi ref) {
		this.context = context;
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		this.context.registerReceiver(this.wifiStateChangedReceiver, intentFilter);
		
		IntentFilter rssiIntentFilter = new IntentFilter();
		rssiIntentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		this.context.registerReceiver(this.wifiSignalReceiver, rssiIntentFilter);
		
		IntentFilter scanIntentFilter = new IntentFilter();
		scanIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		this.context.registerReceiver(this.wifiScanReceiver, scanIntentFilter);
		
		this.wifi = (Wifi) ref;
	}
	
	
	private BroadcastReceiver wifiSignalReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			WifiInfo info = wifiManager.getConnectionInfo();
			
			int signal = WifiManager.calculateSignalLevel(info.getRssi(), 6);
			wifi.setSignal(signal);
			wifi.setRssi(info.getRssi());
			
		}

	};
	
	private BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			wifi.scanFinished();
			
		}

	};
	
	private BroadcastReceiver wifiStateChangedReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) { 

			if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
				
				NetworkInfo nwInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			    
				if(NetworkInfo.State.CONNECTED.equals(nwInfo.getState())){
			    	WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			        String ssid = wifiInfo.getSSID();
			    	
			    	int signal = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 6);
			    	wifi.connectedWifi(ssid, signal, wifiInfo.getBSSID());
			    }
			    else if(NetworkInfo.State.DISCONNECTED.equals(nwInfo.getState())){    	
			    	wifi.disconnectedWifi();
			    }
			    
			    else if(NetworkInfo.State.DISCONNECTING.equals(nwInfo.getState())){
			    	wifi.disconnectingWifi();
			    }
			    else if(NetworkInfo.State.CONNECTING.equals(nwInfo.getState())){
			    	WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			        String ssid = wifiInfo.getSSID();
			    	
			    	wifi.connectingWifi(ssid);
			    }

				return;
			}
			
			
			int extraWifiState = intent.getIntExtra(
					WifiManager.EXTRA_WIFI_STATE,
					WifiManager.WIFI_STATE_UNKNOWN);

			switch (extraWifiState) {
				case WifiManager.WIFI_STATE_DISABLED:
					
					wifi.disabledWifi();
					break;
	
				case WifiManager.WIFI_STATE_DISABLING:
					
					wifi.disablingWifi();
					break;
	
				case WifiManager.WIFI_STATE_ENABLED:
					
					wifi.enabledWifi();
					break;
	
				case WifiManager.WIFI_STATE_ENABLING:
					
					wifi.enablingWifi();
					break;
	
				case WifiManager.WIFI_STATE_UNKNOWN:
	
					// TODO 
					break;
				}

		}

	};
	
	
	
}


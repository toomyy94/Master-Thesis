package PT.PTInov.ArQoSPocketWiFi.Service;

import java.util.Date;
import java.util.List;

import PT.PTInov.ArQoSPocketWiFi.Utils.LogType;
import PT.PTInov.ArQoSPocketWiFi.Utils.Logger;
import PT.PTInov.ArQoSPocketWiFi.Utils.MyWatchDog;
import PT.PTInov.ArQoSPocketWiFi.Utils.StoreInformation;
import PT.PTInov.ArQoSPocketWiFi.Utils.WacthDogInterface;
import PT.PTInov.ArQoSPocketWiFi.Utils.WifiInformation;
import android.content.Context;
import android.location.LocationListener;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class WiFiInformation implements WacthDogInterface {

	private final static String tag = "WiFiInformation";

	// Do update in 30.... 30 secs
	private final static long updateTimeInMillisec = 30000;

	// Application Context
	private Context myContext = null;

	private WifiManager wifiManager = null;

	// watchdog update
	MyWatchDog watchDogUpdate = null;

	WifiInformation myLastScanInformation = null;

	public WiFiInformation(Context c) {

		try {

			myContext = c;

			wifiManager = (WifiManager) myContext
					.getSystemService(Context.WIFI_SERVICE);
			
			if (!wifiManager.isWifiEnabled()) {
				wifiManager.setWifiEnabled(true);
			}

			doScan();
			
			// Maybe we need to stop this options, depends the battery life
			//activeLocationAutoUpdate();

		} catch (Exception ex) {
			Logger.v(tag, "WiFiInformation", LogType.Error, ex.toString());
		}
	}
	
	public WifiInformation doScan() {

		try {

			if (wifiManager != null) {
				
				if (!wifiManager.isWifiEnabled()) {
					wifiManager.setWifiEnabled(true);
				}

				myLastScanInformation = new WifiInformation(new Date(), wifiManager.getScanResults());
			}
			
			return myLastScanInformation;

		} catch (Exception ex) {
			Logger.v(tag, "doScan", LogType.Error, ex.toString());
		}
		
		return null;

	}
	
	public void activeLocationAutoUpdate() {
		try {
			watchDogUpdate = new MyWatchDog(updateTimeInMillisec, this);
			watchDogUpdate.start();
		} catch (Exception ex) {
			Logger.v(tag, "activeLocationAutoUpdate", LogType.Error,
					ex.toString());
		}
	}

	public void stopLocationAutoUpdate() {
		try {
			if (watchDogUpdate != null)
				watchDogUpdate.cancelWatchDog();
		} catch (Exception ex) {
			Logger.v(tag, "stopLocationAutoUpdate", LogType.Error,
					ex.toString());
		}
	}
	
	public void WacthDog() {
		doScan();
	}
}

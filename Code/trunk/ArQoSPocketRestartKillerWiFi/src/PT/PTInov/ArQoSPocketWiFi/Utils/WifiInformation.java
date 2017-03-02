package PT.PTInov.ArQoSPocketWiFi.Utils;

import java.util.Date;
import java.util.List;

import android.net.wifi.ScanResult;

public class WifiInformation {
		
		private final static String tag = "WifiInformation";

		private Date scanDate = null;
		
		// Save the scan information
		private List<ScanResult> scanWifiNetWorks = null;

		public WifiInformation(Date pdate, List<ScanResult> pScanWifiNetWorks) {
			
			scanWifiNetWorks = pScanWifiNetWorks;
			scanDate = pdate;
		}

		public Date getDate() {
			return scanDate;
		}
		
		public List<ScanResult> getScanWifiNetWorks() {
			return scanWifiNetWorks;
		}
		
		public String getRegistryDateFormated() {
			return Utils.buildDateOfNextTest(scanDate);
		}

}

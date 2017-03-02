package PT.PTInov.ArQoSPocketWiFi.Utils;

import java.util.Date;
import java.util.List;

import android.net.wifi.ScanResult;

public class StoreInformation {
	
	private final static String tag = "StoreInformation";

	private Date scanDate = null;

	private String userLocationInfo = null;

	private boolean sended = false;
	private boolean success = false;

	// Save the last Location update
	private String Latitude = "NA";
	private String Longitude = "NA";
	
	// Save the scan information
	private List<ScanResult> scanWifiNetWorks = null;

	public StoreInformation(String pLatitude, String pLongitude, Date pdate, String puserLocationInfo, List<ScanResult> pScanWifiNetWorks) {
		
		Latitude = pLatitude;
		Longitude = pLongitude;
		scanWifiNetWorks = pScanWifiNetWorks;
		scanDate = pdate;
		
		userLocationInfo = puserLocationInfo;
	}

	public Date getDate() {
		return scanDate;
	}
	
	public String getuserLocationInfo() {
		return userLocationInfo;
	}
	
	public void setSended() {
		sended = true;
	}
	
	public boolean getSended() {
		return sended;
	}
	
	public void setSuccess() {
		success = true;
	}
	
	public boolean getSuccess() {
		return success;
	}
	
	public String getState() {
		return (sended)?"True":"False";
	}
	
	public String getLatitude() {
		return Latitude;
	}
	
	public String getLongitude() {
		return Longitude;
	}
	
	public List<ScanResult> getScanWifiNetWorks() {
		return scanWifiNetWorks;
	}
	
	public String getRegistryDateFormated() {
		return Utils.buildDateOfNextTest(scanDate);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\nLatitude :"+Latitude);
		sb.append("\nLongitude :"+Longitude);
		sb.append("\nscanDate :"+scanDate);
		sb.append("\nuserLocationInfo :"+userLocationInfo);
		sb.append("\nsended :"+sended);
		sb.append("\nsuccess :"+success);
		sb.append("\nList<ScanResult> :"+scanWifiNetWorks);
		
		return sb.toString();
	}
}

package PT.PTIN.ArQoSPocketPTWiFi.Utils;

import java.util.List;

import PT.PTIN.ArQoSPocketPTWiFi.Enums.ActionState;
import android.net.wifi.ScanResult;

public class ConnectWiFiState {

	private final static String tag = "ConnectWiFiState";
	
	
	private ActionState ConnectionWifiState;
	private List<ScanResult> wifiScanResult = null;
	
	private AssociationResultState associationResult = null;
	
	
	public ConnectWiFiState() {
		ConnectionWifiState = ActionState.NOTOK;
		associationResult = new AssociationResultState();
	}
	
	public void setConnectionWifiStateDONE() {
		ConnectionWifiState = ActionState.OK;
	}
	
	public ActionState getConnectionWifiState() {
		return ConnectionWifiState;
	}
	
	public void setAssociationResult(AssociationResultState pAssociationResult) {
		associationResult = pAssociationResult;
	}
	
	public  AssociationResultState getAssociationResult() {
		return associationResult;
	}
	
	public List<ScanResult> getWifiScanResult() {
		return wifiScanResult;
	}
	
	public void setWifiScanResult(List<ScanResult> pWifiScanResult) {
		wifiScanResult = pWifiScanResult;
	}
}

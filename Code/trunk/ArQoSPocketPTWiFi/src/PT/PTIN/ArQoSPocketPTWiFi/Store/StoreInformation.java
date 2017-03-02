package PT.PTIN.ArQoSPocketPTWiFi.Store;

import java.util.Date;
import java.util.List;

import PT.PTIN.ArQoSPocketPTWiFi.Enums.ActionState;
import PT.PTIN.ArQoSPocketPTWiFi.Utils.ActionResult;
import PT.PTIN.ArQoSPocketPTWiFi.Utils.Utils;
import android.net.wifi.ScanResult;

public class StoreInformation {
	
	private final static String tag = "StoreInformation";

	private ActionResult actionResult = null;

	public StoreInformation(ActionResult pActionResult) {
		actionResult = pActionResult;
	}
	
	public ActionResult getActionResult() {
		return actionResult;
	}

	public Date getDate() {
		return actionResult.getTestDate();
	}
	
	public boolean getSended() {
		return (actionResult.getSentResultsSTATE() == ActionState.OK);
	}
	
	public boolean getSuccess() {
		return (actionResult.getGeralActionState() == ActionState.OK);
	}
	
	public String getuserLocationInfo() {
		
		String newLatitude = actionResult.getLatitude();
		if (newLatitude.length()>7) {
			newLatitude = newLatitude.substring(0, 7)+newLatitude.substring(newLatitude.length()-1, newLatitude.length());
		}

		String newLongitude = actionResult.getLongitude();
		if (newLongitude.length()>7) {
			newLongitude = newLongitude.substring(0, 7)+newLongitude.substring(newLongitude.length()-1, newLongitude.length());
		}
		
		
		return newLatitude+", "+newLongitude;
	}
	
	public String getRegistryDateFormated() {
		return Utils.buildDateOfNextTest(actionResult.getTestDate());
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\ntestDate :"+actionResult.getTestDate());
		sb.append("\nActionResult :"+actionResult.toString());
		
		return sb.toString();
	}
}

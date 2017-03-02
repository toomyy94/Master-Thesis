package PT.PTInov.ArQoSPocket.JSONConnector;

import java.util.Date;

import android.telephony.TelephonyManager;

import PT.PTInov.ArQoSPocket.Utils.LogType;
import PT.PTInov.ArQoSPocket.Utils.Logger;

public class SendInformationJSON extends JSONConnector {

	private final String tag = "SendInformationJSON";

	private String getTestServerMethodName = null;
	private String connectHost = null;
	
	//todo: Usado para teste, no futuro tem de ser adicionado na wiki..........................................
	private final static int eventType = 2;

	public SendInformationJSON(String phost, String pGetTestServerMethodName) {

		super(phost + pGetTestServerMethodName);

		getTestServerMethodName = pGetTestServerMethodName;
		connectHost = phost;
	}

	public boolean sendInformationJSONToServer(Date testExecDate,
			String DeviceID, String Latitude, String Longitude,
			String GsmSignalStrength, String GsmBitErrorRate, String GsmCellID,
			String GsmLac, String GsmPsc, String MCC, String IMEI,
			String MCC_MNC, String OperatorName, String NetworkType,
			String ISOCountry, String SIMSerialNumber, String IMSI,
			String Roaming, String locationDescription) {

		StringBuilder message = new StringBuilder();
		
		String mode = "7";
		
		if (NetworkType.contains("GPRS")) {
			mode = "0";
		} else if (NetworkType.contains("EDGE")) {
			mode = "1";
		} else if (NetworkType.contains("HSDPA")) {
			mode = "3";
		} else if (NetworkType.contains("HSPA")) {
			mode = "5";
		} else if (NetworkType.contains("HSUPA")) {
			mode = "4";
		} else if (NetworkType.contains("UMTS")) {
			mode = "2";
		}

		message.append("{");
		message.append("\"deviceId\":\"" + DeviceID + "\",");
		message.append("\"radiolog\":[");
		message.append("{");
		message.append("\"module\": \"1\",");
		message.append("\"timestamp\": \"" + convertDate(testExecDate) + "\",");
		message.append("\"mac\": \"00:00:00:00\",");
		message.append("\"network\": {");
		message.append("\"mode\": \""+mode+"\",");
		message.append("\"roaming\": \""
				+ (Roaming.contains("im") ? "1" : "0") + "\",");
		message.append("\"rssi\": \"" + GsmSignalStrength + "\",");
		message.append("\"cellid\":\"" + GsmCellID + "\",");
		message.append("\"plmn\":\"" + OperatorName + "\",");
		message.append("\"mcc\":\"" + MCC_MNC + "\",");
		message.append("\"psc\":\"" + GsmPsc + "\",");
		message.append("\"ber\":\"" + GsmBitErrorRate + "\",");
		message.append("\"lac\":\"" + GsmLac + "\"");
		message.append("},");
		message.append("\"event\":{");
		message.append("\"type\":"+eventType+",");
		message.append("\"origin\":\""+locationDescription+"\"");
		message.append("},");
		message.append("\"gps\":\"" + Latitude + " " + Longitude +" 0.0 0.0\"");
		message.append("}],");
		message.append("\"scanlog\": []");
		message.append("}");
		
		
		Logger.v(tag, LogType.Trace, "message :" + message.toString());

		// Send the Post
		String responseServer = SendPost(message.toString());

		Logger.v(tag, LogType.Trace, "response :" + responseServer);

		return responseServer.contains("Ok");
	}

	private String convertDate(Date d) {
		// 20120110113329

		return (d.getYear() + 1900) + convertNumberNormalDataFormat(d.getMonth() + 1) + convertNumberNormalDataFormat(d.getDate()) +
				convertNumberNormalDataFormat(d.getHours()) + convertNumberNormalDataFormat(d.getMinutes()) + convertNumberNormalDataFormat(d.getSeconds());
		
	}
	
	public static String convertNumberNormalDataFormat(int number) {

		if (number < 10 && number > 0) {
			return "0" + number;
		} else {
			return number + "";
		}
	}
}

package PT.PTInov.ArQoSPocket.JSONConnector;

import java.util.Date;
import java.util.List;

import PT.PTInov.ArQoSPocket.JSONConnector.Util.AndroidInformation;
import PT.PTInov.ArQoSPocketWiFi.Utils.FrequencyToChannel;
import PT.PTInov.ArQoSPocketWiFi.Utils.LogType;
import PT.PTInov.ArQoSPocketWiFi.Utils.Logger;
import PT.PTInov.ArQoSPocketWiFi.Utils.StoreInformation;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.telephony.TelephonyManager;


@SuppressLint({ "ParserError", "ParserError", "ParserError", "ParserError", "ParserError" })
public class SendInformationJSON extends JSONConnector {

	private final String tag = "SendInformationJSON";

	private String getTestServerMethodName = null;
	private String connectHost = null;
	
	//todo: Usado para teste, no futuro tem de ser adicionado na wiki..........................................
	private final static int eventType = 7;

	public SendInformationJSON(String phost, String pGetTestServerMethodName) {

		super(phost + pGetTestServerMethodName);

		getTestServerMethodName = pGetTestServerMethodName;
		connectHost = phost;
	}

	public boolean sendInformationJSONToServer(String deviceID, String MacAddress, Date testExecDate, StoreInformation scanInfo, Context contextRef) {

		StringBuilder message = new StringBuilder();
		
		message.append("{");
			message.append("\"deviceId\":\""+deviceID+"\",");
			message.append("\"radiolog\": [],");
			message.append("\"scanlog\":[");
				List<ScanResult> scanResults = scanInfo.getScanWifiNetWorks();
				for (ScanResult sr :scanResults) {
					
					message.append("{");
						message.append("\"module\":1,");
						message.append("\"timestamp\":\"" + convertDate(testExecDate) + "\",");
						message.append("\"mac\":\"" + MacAddress + "\",");
						message.append("\"network\":{");
							message.append("\"bssid\":\""+sr.BSSID+"\",");
							message.append("\"channel\":"+FrequencyToChannel.convertChannel(sr.frequency)+",");
							message.append("\"frequency\":"+sr.frequency+",");
							message.append("\"encryption\":\""+sr.capabilities+"\",");
							message.append("\"mode\":\"Managed\",");
							message.append("\"protocol\":\"802.11b/g\",");
							message.append("\"quality\":{");
								message.append("\"noise_level\":0,");
								message.append("\"quality\":0,");
								message.append("\"signal_level\":"+sr.level+",");
								message.append("\"snr\":0");
							message.append("},");
							message.append("\"essid\":\""+sr.SSID+"\",");
							message.append("\"bitrate\":0");
						message.append("},");
						message.append("\"event\":{");
							message.append("\"type\":"+eventType);
						message.append("}");
					message.append("},");
						
				}
				// apaga a ultima virgula
				message.deleteCharAt(message.length()-1);
			message.append("]");
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

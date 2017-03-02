package PT.PTIN.ArQoSPocketPTWiFi.JSONConnector;

import java.util.Date;
import java.util.List;

import PT.PTIN.ArQoSPocketPTWiFi.JSONConnector.Util.AndroidInformation;
import PT.PTIN.ArQoSPocketPTWiFi.Logs.LogType;
import PT.PTIN.ArQoSPocketPTWiFi.Logs.Logger;
import PT.PTIN.ArQoSPocketPTWiFi.Utils.ActionResult;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.telephony.TelephonyManager;


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

	public boolean sendInformationJSONToServer(ActionResult actionResult, Context contextRef) {

		StringBuilder message = new StringBuilder();
		
		message.append("{");
			message.append("\"probenotification\": {");
				message.append("\"serialNumber\": \""+AndroidInformation.getDeviceIMEI(contextRef)+"\",");
				message.append("\"macaddress\": \""+AndroidInformation.getMacAddress(contextRef)+"\",");
				message.append("\"ipddress\": \""+AndroidInformation.getIpAddress()+"\",");
				message.append("\"oldipddress\": \"192.168.2.2\",");
				message.append("\"equipmenttype\": \"10\",");
				message.append("\"firmwareversion\": \"arqosev-2.20.2-20110913FPGA0757\",");
				message.append("\"timestamp\": \""+actionResult.getTestDate().getTime()+"\",");
				message.append("\"associatedfilename\":[");
				message.append("],");
				message.append("\"associatedResponse\": {");
					message.append("\"connectedRadioAnalysis\":[");
						List<ScanResult> scanResults = actionResult.getConnectWiFiState().getWifiScanResult();
						for (ScanResult sr :scanResults) {
							message.append("{");
								message.append("\"module\": \"1\",");
								message.append("\"timestamp\": \""+actionResult.getTestDate().getTime()+"\",");
								message.append("\"mac\": \""+AndroidInformation.getMacAddress(contextRef)+"\",");
								message.append("\"gps\": \""+actionResult.getLatitude()+","+actionResult.getLongitude()+"\",");
								message.append("\"network\": {");
									message.append("\"mode\": \"\",");
									message.append("\"roaming\": \"\",");
									message.append("\"rssi\": \""+sr.level+"\",");
									message.append("\"cellid\": \"\",");
									message.append("\"plmn\": \"\",");
									message.append("\"mcc\": \"\",");
									message.append("\"psc\": \"\",");
									message.append("\"ber\": \"\",");
									message.append("\"lac\": \"\",");
									message.append("\"sid\": \"\",");
									message.append("\"bssid\": \""+sr.BSSID+"\",");
									message.append("\"hiddenssid\": \"\",");
									message.append("\"linkSpeedMbits\": \"\",");
									message.append("\"macAddress\": \"\",");
									message.append("\"ssid\": \""+sr.SSID+"\",");
									message.append("\"connectionState\": \"\",");
									message.append("\"dns1\": \"\",");
									message.append("\"dns2\": \"\",");
									message.append("\"gateway\": \"\",");
									message.append("\"leaseDuration\": \"\",");
									message.append("\"netMask\": \"\",");
									message.append("\"serverAddress\": \"\"");
								message.append("}");
							message.append("},");
						}
						// apaga a ultima virgula
						message.deleteCharAt(message.length()-1);	
					message.append("],");
					message.append("\"throughputMeasureAnalysis\":[");
						message.append("{");
							message.append("\"module\": \"\",");
							message.append("\"timestamp\": \"\",");
							message.append("\"mac\": \"\",");
							message.append("\"task\":[");
								message.append("\"id\", {");
									message.append("\"minnimumTransmissionTime\": \""+actionResult.getPingResponse().getMin()+"\",");
									message.append("\"maximumTransmissionTime\": \""+actionResult.getPingResponse().getMax()+"\",");
									message.append("\"averageTransmissionTime\": \""+actionResult.getPingResponse().getMedia()+"\",");
									message.append("\"sentPacketNumber\": \""+actionResult.getPingResponse().getPacketTransmitted()+"\",");
									message.append("\"receivedPacketNumber\": \""+actionResult.getPingResponse().getPacketReceived()+"\",");
									message.append("\"lostPacketNumber\": \"\",");
									message.append("\"receivedBytesNumber\": \"\",");
									message.append("\"totaltransmissionTime\": \"\",");
									message.append("\"accessTime\": \"\",");
									message.append("\"throughput\": \"\"");
								message.append("}");
							message.append("]");
							message.append("}");
						message.append("]");
					message.append("}");
			message.append("}");
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

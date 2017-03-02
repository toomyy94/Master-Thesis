package PT.PTInov.ArQoSPocket.JSONConnector;

import PT.PTInov.ArQoSPocket.JSONConnector.Util.ResponseDiscoveryObject;
import PT.PTInov.ArQoSPocketWiFi.Utils.LogType;
import PT.PTInov.ArQoSPocketWiFi.Utils.Logger;




public class DiscoveryJSONConnector extends JSONConnector {
	
	private final String tag = "DiscoveryJSONConnector";

	private String discoveryServerMethodName = null;
	private String connectHost = null;
	
	public DiscoveryJSONConnector(String phost, String pdiscoveryServerMethodName) {
		
		super(phost+pdiscoveryServerMethodName);
		
		discoveryServerMethodName = pdiscoveryServerMethodName;
		connectHost = phost;
	}
	
	public ResponseDiscoveryObject sendDiscoveryJSONInformation(String deviceID, String deviceIP,
			String devicePort, String deviceFirmwareVersion, String deviceHardwareVersion, String dataHora,
			String deviceSerialNuber, String deviceVendedor, String deviceModel, String deviceName, String connectionType) {
		
		// Convert the information to JSON format
		StringBuilder sb = new StringBuilder();
		
		sb.append("{");
		sb.append("\"constitution\": {");
		sb.append("\"physicalDevice\": {");
		sb.append("\"type\": \""+deviceID+"\",");
		sb.append("\"deviceFirmwareVersion\": \""+deviceFirmwareVersion+"\",");
		sb.append("\"deviceHardwareVersion\": \""+limitString(deviceHardwareVersion,50)+"\",");
		sb.append("\"model\": \""+deviceModel+"\",");
		sb.append("\"resourceBusinessName\": \""+deviceName+"\",");
		sb.append("\"vendor\": \""+deviceVendedor+"\",");
		sb.append("\"serialNumber\": \""+deviceSerialNuber+"\",");
		sb.append("\"installDate\": \""+dataHora+"\"");
		sb.append("},");
		sb.append("\"logicalDevice\": {");
		sb.append("\"managementInfo\": {");
		sb.append("\"context\": \""+deviceIP+":"+devicePort+"\",");
		sb.append("\"method\": \""+connectionType+"\"");
		sb.append("}}}}");
		
		Logger.v(tag, LogType.Trace, "JSON :"+sb.toString());
		
		// Send the Post
		String response = SendPost(sb.toString());
		
		Logger.v(tag, LogType.Trace, "response :"+response);
		
		return ResponseDiscoveryObject.parseDiscoveryJSONResponse(response);
	}
	
	public ResponseDiscoveryObject sendDiscoveryJSONInformationDummy(String deviceID, String deviceIP,
			String devicePort, String deviceFirmwareVersion, String deviceHardwareVersion, String dataHora,
			String deviceSerialNuber, String deviceVendedor, String deviceModel, String deviceName, String connectionType) {
		
		Logger.v(tag, LogType.Trace, "-----------------------------------");
		Logger.v(tag, LogType.Trace, "deviceID :"+deviceID);
		Logger.v(tag, LogType.Trace, "deviceIP :"+deviceIP);
		Logger.v(tag, LogType.Trace, "devicePort :"+devicePort);
		Logger.v(tag, LogType.Trace, "deviceFirmwareVersion :"+deviceFirmwareVersion);
		Logger.v(tag, LogType.Trace, "deviceHardwareVersion :"+deviceHardwareVersion);
		Logger.v(tag, LogType.Trace, "dataHora :"+dataHora);
		Logger.v(tag, LogType.Trace, "deviceSerialNuber :"+deviceSerialNuber);
		Logger.v(tag, LogType.Trace, "deviceVendedor :"+deviceVendedor);
		Logger.v(tag, LogType.Trace, "deviceModel :"+deviceModel);
		Logger.v(tag, LogType.Trace, "deviceName :"+deviceName);
		Logger.v(tag, LogType.Trace, "connectionType :"+connectionType);
		Logger.v(tag, LogType.Trace, "-----------------------------------");
		
		// Convert the information to JSON format
		StringBuilder sb = new StringBuilder();
		
		sb.append("{");
		sb.append("\"result\": {");
		sb.append("\"code\":\"1\",");
		sb.append("\"deviceId\":\"12f15\"");
		sb.append("}");
		sb.append("}");
		
		Logger.v(tag, LogType.Trace, "response :"+sb.toString());
		
		return ResponseDiscoveryObject.parseDiscoveryJSONResponse(sb.toString());
	}
	
	private String limitString(String s, int limit) {
		try {
			return s.substring(0, limit);
		} catch(Exception ex) {
			Logger.v(tag, "limitString", LogType.Error, "Error :"+ex.toString());
			return s;
		}
	}
}

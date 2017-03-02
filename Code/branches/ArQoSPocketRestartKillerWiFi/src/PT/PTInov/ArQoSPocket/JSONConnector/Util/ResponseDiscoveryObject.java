package PT.PTInov.ArQoSPocket.JSONConnector.Util;

import org.json.JSONObject;

import PT.PTInov.ArQoSPocketWiFi.Utils.LogType;
import PT.PTInov.ArQoSPocketWiFi.Utils.Logger;



public class ResponseDiscoveryObject {
	
	private final static String tag = "ResponseDiscoveryObject";

	private String code = null;
	private String deviceId = null;
	
	public ResponseDiscoveryObject(String code, String deviceId) {
		this.code = code;
		this.deviceId = deviceId;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public String getDeviceId() {
		return this.deviceId;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("code: "+code+"\n");
		sb.append("deviceId: "+deviceId+"\n");
		
		return sb.toString();
	}
	
	public static ResponseDiscoveryObject parseDiscoveryJSONResponse(String jsonResponse) {
		
		String localCode = null;
		String localDeviceId = null;
		
		try {
			
			JSONObject jsonObject = new JSONObject(jsonResponse);
			
			JSONObject result = (JSONObject) jsonObject.get("result");
			Logger.v(tag, LogType.Debug, "result :" + result.toString());
			
			localCode = result.getString("code");
			Logger.v(tag, LogType.Debug, "code :" + localCode);
			
			localDeviceId = result.getString("deviceId");
			Logger.v(tag, LogType.Debug, "deviceId :" + localDeviceId);			
			
			
		} catch(Exception ex) {
			Logger.v(tag, LogType.Error, "parseDiscoveryJSONResponse :: Error :"+ex.toString());
			return null;
		}
		
		return new ResponseDiscoveryObject(localCode, localDeviceId);
	}
}

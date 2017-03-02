package PT.PTIN.ArQoSPocketPTWiFi.Utils;

import PT.PTIN.ArQoSPocketPTWiFi.Enums.ActionState;

public class PTWiFiAuthResult {
	
	private final static String tag = "ConnectWiFiState";
	
	private ActionState AuthPTWifiState;
	
	private ActionState PostResponseState;
	private String postServerResponse = null;
	private long postResponseTime;
	// Tenho de meter aki o tempo de resposta ao post
	
	private ActionState GetResponseState;
	private String getServerResponse = null;
	private long getResponseTime;
	// tenho de meter aki o tempo de resposta ao get

	public PTWiFiAuthResult() {
		AuthPTWifiState = ActionState.NOTOK;
		PostResponseState = ActionState.NOTOK;
		GetResponseState = ActionState.NOTOK;
		
		postResponseTime = -1;
		getResponseTime = -1;
	}
	
	public long getPostResponseTime() {
		return postResponseTime;
	}
	
	public void setPostResponseTime(long responseTime) {
		postResponseTime = responseTime;
	}
	
	public long getGetResponseTime() {
		return getResponseTime;
	}
	
	public void setGetResponseTime(long responseTime) {
		getResponseTime = responseTime;
	}
	
	public ActionState getAuthPTWifiState() {
		return AuthPTWifiState;
	}
	
	public void setAuthPTWifiStateDONE() {
		AuthPTWifiState = ActionState.OK;
	}
	
	public ActionState getPostResponseState() {
		return PostResponseState;
	}
	
	public void setPostResponseStateDONE() {
		PostResponseState = ActionState.OK;
	}
	
	public ActionState getGetResponseState() {
		return GetResponseState;
	}
	
	public void setGetResponseStateDONE() {
		GetResponseState =  ActionState.OK;
	}
	
	public String getPostServerResponse() {
		return postServerResponse;
	}
	
	public void setPostServerResponse(String postResponse) {
		postServerResponse = postResponse;
	}
	
	public String getGetServerResponse() {
		return getServerResponse;
	}
	
	public void setGetServerResponse(String getResponse) {
		getServerResponse = getResponse;
	}
}

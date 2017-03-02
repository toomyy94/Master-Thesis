package PT.PTInov.ArQoSPocketEDP.JSONConnector.Util;

public class WebResponse {
	
	private final static String tag = "WebResponse";
	
	private int responseCode = -1;
	private String responseMessage = null;
	private String responseInput = null;
	private String cookie = null;
	
	public WebResponse(int pResponseCode, String pResponseMessage, String pResponseInput) {
		responseCode = pResponseCode;
		responseMessage = pResponseMessage;
		responseInput = pResponseInput;
	}
	
	public WebResponse(int pResponseCode, String pResponseMessage, String pResponseInput, String pcookie) {
		responseCode = pResponseCode;
		responseMessage = pResponseMessage;
		responseInput = pResponseInput;
		cookie = pcookie;
	}
	
	public String getCookie() {
		return cookie;
	}
	
	public int getResponseCode() {
		return responseCode;
	}
	
	public String getResponseMessage() {
		return responseMessage;
	}
	
	public String getResponseInput() {
		return responseInput;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\nresponseCode :"+responseCode);
		sb.append("\nresponseMessage :"+responseMessage);
		sb.append("\nresponseInput :"+responseInput);
		sb.append("\ncookie :"+cookie);
		
		return sb.toString();
	}
}
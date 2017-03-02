package PT.PTInov.ArQoSPocketEDP.JSONConnector.Util;

import java.io.ByteArrayOutputStream;


// objecto: no caso da string em java ter limite, utilizar o bytearray - neste momento não está a ser utilizado
public class WebResponseLongInputStream {

private final static String tag = "WebResponse";
	
	private int responseCode = -1;
	private String responseMessage = null;
	private ByteArrayOutputStream responseInput = null;
	private String cookie = null;
	
	public WebResponseLongInputStream(int pResponseCode, String pResponseMessage, ByteArrayOutputStream pResponseInput) {
		responseCode = pResponseCode;
		responseMessage = pResponseMessage;
		responseInput = pResponseInput;
	}
	
	public WebResponseLongInputStream(int pResponseCode, String pResponseMessage, ByteArrayOutputStream pResponseInput, String pcookie) {
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
	
	public ByteArrayOutputStream getResponseInput() {
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

package pt.ptinovacao.arqospocket.service.tasks.structs;

public class WebResponse {
	
	private final static String TAG = "WebResponse";
	
	private int responseCode = -1;
	private String responseMessage = null;
	private String responseInput = null;
	
	public WebResponse(int pResponseCode, String pResponseMessage, String pResponseInput) {
		responseCode = pResponseCode;
		responseMessage = pResponseMessage;
		responseInput = pResponseInput;
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
		
		return sb.toString();
	}
}

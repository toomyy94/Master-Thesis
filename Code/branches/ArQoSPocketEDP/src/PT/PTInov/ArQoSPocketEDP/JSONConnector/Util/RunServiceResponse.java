package PT.PTInov.ArQoSPocketEDP.JSONConnector.Util;

import android.annotation.SuppressLint;

public class RunServiceResponse {

	private final String tag = "RunServiceResponse";
	
	private boolean testIsOK;
	private String testID;
	private String errorCode;
	private String errorMsg;
	
	public RunServiceResponse(boolean testIsOK, String testID, String errorCode, String errorMsg) {
		this.testIsOK = testIsOK;
		this.testID = testID;
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
	
	@SuppressLint("ParserError")
	public boolean getIfTestIsOK() {
		return testIsOK;
	}
	
	public String getTestID() {
		return testID;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("testIsOK :"+testIsOK+"\n");
		sb.append("testID :"+testID+"\n");
		sb.append("errorCode :"+errorCode+"\n");
		sb.append("errorMsg :"+errorMsg+"\n");
		
		return sb.toString();
	}
}

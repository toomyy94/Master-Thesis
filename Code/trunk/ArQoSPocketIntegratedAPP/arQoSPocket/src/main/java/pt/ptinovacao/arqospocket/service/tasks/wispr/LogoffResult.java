package pt.ptinovacao.arqospocket.service.tasks.wispr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public class LogoffResult {
	
	private final static Logger logger = LoggerFactory.getLogger(LogoffResult.class);

	private String MessageType = null;
	private String ResponseCode = null;
	
	private boolean LoginSuccess = false;
	
	public LogoffResult(String responseXML) {
		final String method = "LogoffResult";
		
		try {
			
			WisprParser wisprParser = new WisprParser();
			
			this.MessageType = wisprParser.ParseField(responseXML, "MessageType");
			this.ResponseCode = wisprParser.ParseField(responseXML, "ResponseCode");
			
			LoginSuccess = ResponseCode.equals("150")?true:false;
					
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public String get_MessageType() {
		return MessageType;
	}
	
	public String get_ResponseCode() {
		return ResponseCode;
	}
	
	public boolean get_LoginSuccess() {
		return LoginSuccess;
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\nMessageType :"+MessageType);
			sb.append("\nResponseCode :"+ResponseCode);
			sb.append("\nLoginSuccess :"+LoginSuccess);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
}

package pt.ptinovacao.arqospocket.service.tasks.wispr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public class LoginResult {
	
	private final static Logger logger = LoggerFactory.getLogger(LoginResult.class);

	private String MessageType = null;
	private String ResponseCode = null;
	private String ReplyMessage = null;
	
	private String LoginResultsURL = null;
	private String LogoffURL = null;
	
	private boolean LoginSuccess = false;
	
	public LoginResult(String responseXML) {
		final String method = "LoginResult";
		
		try {
			
			WisprParser wisprParser = new WisprParser();
			
			this.MessageType = wisprParser.ParseField(responseXML, "MessageType");
			this.ResponseCode = wisprParser.ParseField(responseXML, "ResponseCode");
			this.ReplyMessage = wisprParser.ParseField(responseXML, "ReplyMessage");
			
			this.LoginResultsURL = wisprParser.ParseField(responseXML, "LoginResultsURL");
			String logoffXML = wisprParser.ParseField(responseXML, "LogoffURL");
			this.LogoffURL = wisprParser.ParseRefField(logoffXML);
			
			LoginSuccess = ResponseCode.equals("50")?true:false;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public LoginResult(String MessageType, String ResponseCode, 
			String ReplyMessage, String LoginResultsURL, String LogoffURL) {
		final String method = "LoginResult";
		
		try {
			
			this.MessageType = MessageType;
			this.ResponseCode = ResponseCode;
			this.ReplyMessage = ReplyMessage;
			
			this.LoginResultsURL = LoginResultsURL;
			this.LogoffURL = LogoffURL;
			
			LoginSuccess = ResponseCode.equals("50")?true:false;
			
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
	
	public String get_ReplyMessage() {
		return ReplyMessage;
	}
	
	public String get_LoginResultsURL() {
		return LoginResultsURL;
	}
	
	public String get_LogoffURL() {
		return LogoffURL;
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
			sb.append("\nReplyMessage :"+ReplyMessage);
			sb.append("\nLoginResultsURL :"+LoginResultsURL);
			sb.append("\nLogoffURL :"+LogoffURL);
			sb.append("\nLoginSuccess :"+LoginSuccess);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
}

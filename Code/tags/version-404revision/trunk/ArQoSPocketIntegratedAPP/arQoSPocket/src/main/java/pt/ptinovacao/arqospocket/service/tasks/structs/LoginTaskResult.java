package pt.ptinovacao.arqospocket.service.tasks.structs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.tasks.wispr.LoginResult;


public class LoginTaskResult {
	
	private final static Logger logger = LoggerFactory.getLogger(LoginTaskResult.class);
	
	private LoginResult loginResult;
	private long login_time; // millisec

	public LoginTaskResult(LoginResult loginResult, long time) {
		final String method = "LoginTaskResult";
		
		try {
			
			this.loginResult = loginResult;
			this.login_time = time;
			
		} catch(Exception ex) {
        	MyLogger.error(logger, method, ex);
        }
	}
	
	public LoginResult get_loginResult() {
		return loginResult;
	}
	
	public long get_time() {
		return login_time;
	}
	
	public String toJSON() {
		final String method = "toJSON";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\"response_code\":\""+loginResult.get_ResponseCode()+"\",");
			sb.append("\"response_time\":\""+login_time+"\"");
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\nlogin_time :"+login_time);
			sb.append("\nloginResult :"+loginResult.toString());
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
}

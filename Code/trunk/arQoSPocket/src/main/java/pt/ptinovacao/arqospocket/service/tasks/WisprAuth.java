package pt.ptinovacao.arqospocket.service.tasks;

import android.content.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.tasks.structs.LogoffTaskResult;
import pt.ptinovacao.arqospocket.service.tasks.wispr.LoginResult;
import pt.ptinovacao.arqospocket.service.tasks.structs.LoginTaskResult;
import pt.ptinovacao.arqospocket.service.tasks.wispr.LogoffResult;
import pt.ptinovacao.arqospocket.service.tasks.wispr.WisprClient;

public class WisprAuth {
	private static final String DEFAULT_REQUEST_URL = "https://wifi.meo.pt/pt/Pages/Homepage.aspx";
	private final static Logger logger = LoggerFactory.getLogger(WisprAuth.class);
	
	private String request_url_auth = null;
	private String logoff_link = null;
	private Context context;

	
	public WisprAuth() {
		final String method = "WisprAuth";
		
		try {
			
			// MEO-WiFi web site
			this.request_url_auth = DEFAULT_REQUEST_URL;
			
		} catch(Exception ex) {
        	MyLogger.error(logger, method, ex);
        }
	}
	
	public WisprAuth(String request_url_auth) {
		final String method = "WisprAuth";
		
		try {
			
			this.request_url_auth = request_url_auth;
			
		} catch(Exception ex) {
        	MyLogger.error(logger, method, ex);
        }
	}
	
	public LoginTaskResult doLogin(String ssid, String username, String password) {
		final String method = "doLogin";
		
		try {
			
			long startTime = System.currentTimeMillis();
			
			WisprClient wisprClient = new WisprClient();
			LoginResult loginResult = wisprClient.Login(ssid, username, password, request_url_auth);
			
			long endTime = System.currentTimeMillis();
			
			return new LoginTaskResult(loginResult, endTime - startTime);
			
		} catch(Exception ex) {
        	MyLogger.error(logger, method, ex);
        }
		
		return new LoginTaskResult(null, 0);
	}
	
	public LogoffTaskResult doLogoff() {
		final String method = "doLogoff";
		
		try {
			
			MyLogger.debug(logger, method, "request_url_auth :"+request_url_auth);
			
			long startTime = System.currentTimeMillis();
			
			WisprClient wisprClient = new WisprClient();
			//LogoffResult logoffResult = wisprClient.Logoff(logoff_link);
			LogoffResult logoffResult = wisprClient.Logoff(request_url_auth);
			
			if (logoffResult != null)
				MyLogger.debug(logger, method, "logoffResult :"+logoffResult.toString());
			else
				MyLogger.debug(logger, method, "logoffResult == null");
			
			long endTime = System.currentTimeMillis();
			
			return new LogoffTaskResult(logoffResult, endTime - startTime);
			
		} catch(Exception ex) {
        	MyLogger.error(logger, method, ex);
        }
		
		return new LogoffTaskResult(null, 0);
	}
}

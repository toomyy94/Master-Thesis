package pt.ptinovacao.arqospocket.service.tasks.wispr;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.graphics.Bitmap;

import pt.ptinovacao.arqospocket.service.http.IWebRequestCallBack;
import pt.ptinovacao.arqospocket.service.http.WebRequests;
import pt.ptinovacao.arqospocket.service.http.WebResponse;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public class WisprClient implements IWebRequestCallBack {
	
	private final static Logger logger = LoggerFactory.getLogger(WisprClient.class);
	
	private final String FIELD_LOGIN_URL = "LoginURL";
	
	private final String LOGIN_GET_REQUEST_CODE = "0011";
	private final String LOGIN_POST_REQUEST_CODE = "0022";
	
	private WisprParser parser = null;
    private Map<String, Credential> credentials;
    
    private String responseContent = null;
    private String loginResponse = null;
    private static final int MAX_AVAILABLE = 1;
    private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);

    public WisprClient() {
    	final String method = "WisprClient";
    	
    	try {
    		
    		credentials = new TreeMap<String, Credential>();
    		parser = new WisprParser();
    		//httpClient = new HttpClient();
    		
    	} catch(Exception ex) {
    		MyLogger.error(logger, method, ex);
    	}
    }
    
    public boolean IsWifiSupported(String essid)
    {
        return credentials.containsKey(essid);
    }
    
    public void AddCredentials(String essid, String username, String password, String authenticationTriggerUri)
    {
        credentials.put(essid, new Credential(username, password, authenticationTriggerUri));
    }
    
    public LoginResult Login(String essid)
    {
        final String method = "Login";
        MyLogger.trace(logger, method, "In");
        
        try {
        	
        	if (!credentials.containsKey(essid))
        		return null;

        	Credential credential = credentials.get(essid);

        	return Login(essid, credential.username, credential.password, credential.authenticationTriggerUri); 
        	
        } catch(Exception ex) {
        	MyLogger.error(logger, method, ex);
        }
        
        return null;
    }
    
    public LogoffResult Logoff(String link) {
    	final String method = "Logoff";
    	
    	try {
    		
    		MyLogger.trace(logger, method, "In");
    		
    		WebResponse webResponse =  WebRequests.doGET(link);
        	//MyLogger.debug(logger, method, "webResponse: " + webResponse.toString());
        	responseContent = webResponse.getResponseInput();
        	
        	MyLogger.debug(logger, method, "responseContent :" + responseContent);
        	
        	
        	String wisprResponse = parser.FilterWisprXml(responseContent);
            MyLogger.debug(logger, method, "authentication response WISPr XML :" + wisprResponse);
        	
            WisprParser wisprParser = new WisprParser();
            String logoffXML = wisprParser.ParseField(wisprResponse, "LogoffURL");
			String LogoffURL = wisprParser.ParseRefField(logoffXML);
            
			MyLogger.debug(logger, method, "LogoffURL :" + LogoffURL);
			
			if (LogoffURL == null)
				LogoffURL = logoffXML;
			
			MyLogger.debug(logger, method, "LogoffURL :" + LogoffURL);
			
    		
    		webResponse =  WebRequests.doGET(LogoffURL);
        	responseContent = webResponse.getResponseInput();
        	
        	MyLogger.debug(logger, method, "responseContent: " + responseContent);
        	
        	String wisprXml = parser.FilterWisprXml(responseContent);

        	MyLogger.debug(logger, method, "first WISPr XML: " + wisprXml);
        	
        	LogoffResult logoffResult = new LogoffResult(wisprXml);
        	
        	MyLogger.debug(logger, method, "logoffResult :" + logoffResult.toString());
        	
        	return logoffResult;
    		
    	} catch(Exception ex) {
        	MyLogger.error(logger, method, ex);
        }
    	
    	return null;
    }
    
    public LoginResult Login(String essid, String username, String password, String authenticationTriggerUri)
    {
    	final String method = "Login2";
    	
    	LoginResult loginResult = null;

        try {
        	
        	MyLogger.trace(logger, method, "In");
        	
        	WebResponse webResponse =  WebRequests.doGET(authenticationTriggerUri);
        	responseContent = webResponse.getResponseInput();
        	
        	MyLogger.debug(logger, method, "responseContent: " + responseContent);
        	
        	String wisprXml = parser.FilterWisprXml(responseContent);

        	MyLogger.debug(logger, method, "first WISPr XML: " + wisprXml);

            String loginUrl = parser.ParseField(wisprXml, FIELD_LOGIN_URL);
            MyLogger.debug(logger, method, "loginUrl: " + loginUrl);

            Map<String, String> parameters = new TreeMap<String, String>();
            parameters.put("button", "Login");
            parameters.put("UserName", username);
            parameters.put("Password", password);
            parameters.put("FNAME", "0");
            parameters.put("OriginatingServer", authenticationTriggerUri);
            
            loginResponse = WebRequests.doPost(loginUrl, parameters, "").getResponseInput();
            MyLogger.debug(logger, method, "loginResponse: " + loginResponse);
        	
            String wisprResponse = parser.FilterWisprXml(loginResponse);
            MyLogger.debug(logger, method, "authentication response WISPr XML: " + wisprResponse);
            
            loginResult = new LoginResult(wisprResponse);
            MyLogger.debug(logger, method, "loginResult :" + loginResult.toString());
            
            return loginResult;
            
        } catch (Exception ex)
        {
        	MyLogger.error(logger, method, ex);
        }
        
        return loginResult;
    }

    /***
     * 
     * IWebRequestCallBack
     * 
     */

	@Override
	public void WebRequestComplete(WebResponse response, String requestCode) {
		final String method = "WebRequestComplete";
		
		try {
			
			if (requestCode.equals(LOGIN_GET_REQUEST_CODE)) {

				responseContent = response.getResponseInput();
				
			} else if (requestCode.equals(LOGIN_POST_REQUEST_CODE)) {
				
				loginResponse = response.getResponseInput();
			}
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		try {
			available.release();
			available.release();
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	} 


	@Override
	public void PhotoDownloadComplete(Bitmap photo, String requestCode) {

	}
}

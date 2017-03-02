package pt.ptinovacao.arqospocket.service.tasks.wispr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public class Credential {
	
	private final static Logger logger = LoggerFactory.getLogger(Credential.class);
	
    public String username;
    public String password;
    public String authenticationTriggerUri;

    public Credential(String username, String password, String authenticationTriggerUri)
    {
        this.username = username;
        this.password = password;
        this.authenticationTriggerUri = authenticationTriggerUri;
    }
    
    public String get_username() {
    	return username;
    }
    
    public String get_password() {
    	return password;
    }

    public String get_authenticationTriggerUri() {
    	return authenticationTriggerUri;
    }
    
    public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\nusername :"+username);
			sb.append("\npassword :"+password);
			sb.append("\nauthenticationTriggerUri :"+authenticationTriggerUri);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
}

package pt.ptinovacao.arqospocket.service.jsonparser.taskparser;

import java.io.Serializable;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.interfaces.ITask;
import pt.ptinovacao.arqospocket.service.jsonparser.TaskStruct;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public class WiFiAuthLoginTaskStruct extends TaskStruct implements ITask, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(WiFiAuthLoginTaskStruct.class);
	
	private String url;
	private String username;
	private String password;

	public WiFiAuthLoginTaskStruct(JSONObject jObject) {
		super(jObject);
		
		final String method = "WiFiAuthLoginTaskStruct";
		
		try {
			
			this.jObjectSerialized = jObject.toString();
			
			this.url = jObject.getString("url");
			this.username = jObject.getString("username");
			this.password = jObject.getString("password");
			
			MyLogger.debug(logger, method, "url :"+url);
			MyLogger.debug(logger, method, "username :"+username);
			MyLogger.debug(logger, method, "password :"+password);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	public String get_url() {
		return url;
	}
	
	public String get_username() {
		return username;
	}
	
	public String get_password() {
		return password;
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\nurl :"+url);
			sb.append("\nusername :"+username);
			sb.append("\npassword :"+password);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
	
	public Object clone() {
		final String method = "clone";
		
		try {
			
			return new WiFiAuthLoginTaskStruct(new JSONObject(jObjectSerialized));
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
}

package pt.ptinovacao.arqospocket.service.jsonparser.taskparser;

import java.io.Serializable;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.interfaces.ITask;
import pt.ptinovacao.arqospocket.service.jsonparser.TaskStruct;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public class AssociateWiFiTaskStruct extends TaskStruct implements ITask, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(AssociateWiFiTaskStruct.class);
	
	private String essid;
	private String password;
	private String bssid;

	public AssociateWiFiTaskStruct(JSONObject jObject) {
		super(jObject);
		
		final String method = "AssociateWiFiTaskStruct";
		
		try {
			
			this.jObjectSerialized = jObject.toString();
			
			this.essid = jObject.getString("essid");
			this.password = jObject.getString("password");
			this.bssid = jObject.getString("bssid");
			
			MyLogger.debug(logger, method, "essid :"+essid);
			MyLogger.debug(logger, method, "password :"+password);
			MyLogger.debug(logger, method, "bssid :"+bssid);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	public String get_essid() {
		return essid;
	}
	
	public String get_password() {
		return password;
	}
	
	public String get_bssid() {
		return bssid;
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\nessid :"+essid);
			sb.append("\npassword :"+password);
			sb.append("\nbssid :"+bssid);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
	
	public Object clone() {
		final String method = "clone";
		
		try {
			
			return new AssociateWiFiTaskStruct(new JSONObject(jObjectSerialized));
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
}
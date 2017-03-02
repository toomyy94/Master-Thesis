package pt.ptinovacao.arqospocket.service.jsonparser.taskparser;

import java.io.Serializable;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.interfaces.ITask;
import pt.ptinovacao.arqospocket.service.jsonparser.TaskStruct;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public class HTTPDownloadTaskStruct extends TaskStruct implements ITask, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(HTTPDownloadTaskStruct.class);
	
	private String url;
	private String proxy;
	private String user_agent;

	public HTTPDownloadTaskStruct(JSONObject jObject) {
		super(jObject);
		
		final String method = "HTTPDownloadTaskStruct";
		
		try {
			
			this.jObjectSerialized = jObject.toString();
			
			this.url = jObject.getString("url");
			this.proxy = jObject.getString("proxy");
			this.user_agent = jObject.getString("user_agent");
			
			MyLogger.debug(logger, method, "url :"+url);
			MyLogger.debug(logger, method, "proxy :"+proxy);
			MyLogger.debug(logger, method, "user_agent :"+user_agent);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	public String get_url() {
		return url;
	}
	
	public String get_proxy() {
		return proxy;
	}
	
	public String get_user_agent() {
		return user_agent;
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\nurl :"+url);
			sb.append("\nproxy :"+proxy);
			sb.append("\nuser_agent :"+user_agent);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
	
	public Object clone() {
		final String method = "clone";
		
		try {
			
			return new HTTPDownloadTaskStruct(new JSONObject(jObjectSerialized));
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
}
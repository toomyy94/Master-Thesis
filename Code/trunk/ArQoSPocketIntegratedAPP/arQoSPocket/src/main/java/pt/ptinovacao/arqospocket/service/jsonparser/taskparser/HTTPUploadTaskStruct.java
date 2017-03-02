package pt.ptinovacao.arqospocket.service.jsonparser.taskparser;

import java.io.Serializable;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.interfaces.ITask;
import pt.ptinovacao.arqospocket.service.jsonparser.TaskStruct;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public class HTTPUploadTaskStruct extends TaskStruct implements ITask, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(HTTPUploadTaskStruct.class);
	
	private String url;
	private String proxy;
	private String content;

	public HTTPUploadTaskStruct(JSONObject jObject) {
		super(jObject);
		
		final String method = "HTTPUploadTaskStruct";
		
		try {
			
			this.jObjectSerialized = jObject.toString();
			
			this.url = jObject.getString("url");
			this.proxy = jObject.getString("proxy");
			this.content = jObject.getString("content");
			
			MyLogger.debug(logger, method, "url :"+url);
			MyLogger.debug(logger, method, "proxy :"+proxy);
			MyLogger.debug(logger, method, "content :"+content);
			
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
	
	public String get_content() {
		return content;
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\nurl :"+url);
			sb.append("\nproxy :"+proxy);
			sb.append("\ncontent :"+content);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
	
	public Object clone() {
		final String method = "clone";
		
		try {
			
			return new HTTPUploadTaskStruct(new JSONObject(jObjectSerialized));
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
}
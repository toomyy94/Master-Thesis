package pt.ptinovacao.arqospocket.service.jsonparser.structs;

import java.io.Serializable;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public class IterationsTestEvent extends TestEventObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger logger = LoggerFactory.getLogger(IterationsTestEvent.class);

	private long count = -1;
	
	public IterationsTestEvent(JSONObject jObject) {
		super();
		
		final String method = "IterationsTestEvent";
		
		try {
			
			this.count = Long.parseLong(jObject.getString("count"));
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public IterationsTestEvent(long count) {
		super();
		
		final String method = "IterationsTestEvent";
		
		try {
			
			this.count = count;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public long get_count() {
		return count;
	}
	
	public long decrement_count() {
		count -= 1;
		return count;
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\ncount :"+count);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
	
	public Object clone() {
		final String method = "IterationsTestEvent";
		
		try {
			
			return new IterationsTestEvent(count);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
}

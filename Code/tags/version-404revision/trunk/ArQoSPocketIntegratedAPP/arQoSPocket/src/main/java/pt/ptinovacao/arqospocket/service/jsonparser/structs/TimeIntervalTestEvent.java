package pt.ptinovacao.arqospocket.service.jsonparser.structs;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public class TimeIntervalTestEvent extends TestEventObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger logger = LoggerFactory.getLogger(TimeIntervalTestEvent.class);
	
	private long interval = -1;
	
	public TimeIntervalTestEvent(JSONObject jObject) {
		super();
		
		final String method = "TimeIntervalTestEvent";
		
		try {
			
			this.interval = Long.parseLong(jObject.getString("interval"));
			
			// no formato do servidor esta a enviar em sec, temos de passar para millsec
			this.interval *= 1000;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public TimeIntervalTestEvent(long interval_millsec) {
		super();
		
		final String method = "TimeIntervalTestEvent";
		
		try {
			
			this.interval = interval;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public long get_interval() {
		return interval;
	}
	
	public Date get_next_execution_date(Date dateInitTest) {
		final String method = "get_next_execution_date";
		
		try {
			
			MyLogger.debug(logger, method, "dateInitTest.getTime() :"+dateInitTest.getTime());
			MyLogger.debug(logger, method, "this.get_interval() :"+this.get_interval());
			
			return new Date(dateInitTest.getTime()+this.get_interval());
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\ninterval :"+interval);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	} 
	
	public Object clone() {
		final String method = "IterationsTestEvent";
		
		try {
			
			return new TimeIntervalTestEvent(interval);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
}

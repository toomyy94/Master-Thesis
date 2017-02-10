package pt.ptinovacao.arqospocket.service.tasks.structs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.tasks.wispr.LogoffResult;

public class LogoffTaskResult {
	
	private final static Logger logger = LoggerFactory.getLogger(LogoffTaskResult.class);

	private long logoff_time;
	private LogoffResult logoffResult;
	
	public LogoffTaskResult(LogoffResult logoffResult, long time) {
		final String method = "LoginTaskResult";
		
		try {
			
			this.logoffResult = logoffResult;
			this.logoff_time = time;
			
		} catch(Exception ex) {
        	MyLogger.error(logger, method, ex);
        }
	}
	
	public LogoffResult get_logoffResult() {
		return logoffResult;
	}
	
	public long get_time() {
		return logoff_time;
	}
	
	public String toJSON() {
		final String method = "toJSON";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\"response_code\":\""+logoffResult.get_ResponseCode()+"\",");
			sb.append("\"response_time\":\""+logoff_time+"\"");
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\nlogoff_time :"+logoff_time);
			sb.append("\nlogoffResult :"+logoffResult.toString());
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
}

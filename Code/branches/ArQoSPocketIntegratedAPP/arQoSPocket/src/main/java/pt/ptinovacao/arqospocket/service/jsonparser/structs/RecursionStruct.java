package pt.ptinovacao.arqospocket.service.jsonparser.structs;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.jsonparser.enums.ETestEvent;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public class RecursionStruct  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger logger = LoggerFactory.getLogger(RecursionStruct.class);
	
	private ETestEvent event_type;
	private TestEventObject test_event_params;
	
	public RecursionStruct(ETestEvent event_type, TestEventObject test_event_params) {
		final String method = "RecursionStruct";
		
		try {
			
			this.event_type = event_type;
			this.test_event_params = test_event_params;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public ETestEvent get_event_type() {
		return event_type;
	}
	
	public TestEventObject get_test_event_params() {
		return test_event_params;
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\nrecursion_event_type :"+event_type.toString());
			sb.append("\nrecursion_event_params :"+test_event_params.toString());
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	} 
	
	public Object clone() {
		final String method = "clone";
		
		try {
			
			return new RecursionStruct(event_type, (TestEventObject) test_event_params.clone());
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
}

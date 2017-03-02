package pt.ptinovacao.arqospocket.service.alarm;

import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public class AlarmIDMapping {

	private final static Logger logger = LoggerFactory.getLogger(AlarmIDMapping.class);
	
	private TreeMap<Integer,String> id_mapping;
	
	public AlarmIDMapping() {
		final String method = "AlarmIDMapping";
		
		try {
			MyLogger.trace(logger, method, "In");
			
			
			id_mapping = new TreeMap<Integer,String>();
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		MyLogger.trace(logger, method, "Out");
	}
	
	public boolean mapping_ID(int int_id, String string_id) {
		final String method = "mapping_ID";
		
		try {
			MyLogger.trace(logger, method, "In");
			
			if (id_mapping.put(int_id, string_id) == null)
				return false;
			else
				return true;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		MyLogger.trace(logger, method, "Out");
		
		return false;
	}
	
	public String get_and_remove_string_id(int int_id) {
		final String method = "get_and_remove_string_id";
		
		String returnValue = null;
		
		try {
			MyLogger.trace(logger, method, "In");
			
			returnValue = id_mapping.get(int_id);
			
			if (returnValue != null)
				id_mapping.remove(int_id);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		MyLogger.trace(logger, method, "Out");
		
		return returnValue;
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("id_mapping :"+id_mapping.toString());
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
}

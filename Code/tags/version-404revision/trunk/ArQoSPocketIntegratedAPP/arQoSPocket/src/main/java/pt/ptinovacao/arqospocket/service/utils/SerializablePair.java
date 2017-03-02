package pt.ptinovacao.arqospocket.service.utils;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.structs.Anomalies;

public class SerializablePair<X,Y> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger logger = LoggerFactory.getLogger(Anomalies.class);

	public X first;
	public Y second;
	
	public SerializablePair(X first, Y second) {
		final String method = "SerializablePair";
		
		try {
			
			this.first = first;
			this.second = second;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\nfirst :"+first.toString());
			sb.append("\nsecond :"+second.toString());
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
}

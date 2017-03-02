package pt.ptinovacao.arqospocket.service.jsonparser.utils;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public class WeekSelectionDaysObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger logger = LoggerFactory.getLogger(WeekSelectionDaysObject.class);
	
	private boolean _2feira = false;
	private boolean _3feira = false;
	private boolean _4feira = false;
	private boolean _5feira = false;
	private boolean _6feira = false;
	private boolean Sabado = false;
	private boolean Domingo = false;
	
	public WeekSelectionDaysObject(int bitArrayValue) {
		final String method = "WeekSelectionDaysObject";
		
		try {
			
			MyLogger.debug(logger, method, "bitArrayValue :"+bitArrayValue);
			
			String bitArray = convert_bitArrayValue_to_bitArray(bitArrayValue);
			
			MyLogger.debug(logger, method, "bitArray :"+bitArray);
			

			Domingo = "1".equals(bitArray.substring(6, 7));
			_2feira = "1".equals(bitArray.substring(5, 6));
			_3feira = "1".equals(bitArray.substring(4, 5));
			_4feira = "1".equals(bitArray.substring(3, 4));
			_5feira = "1".equals(bitArray.substring(2, 3));
			_6feira = "1".equals(bitArray.substring(1, 2));
			Sabado  = "1".equals(bitArray.substring(0, 1));

				
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	private String convert_bitArrayValue_to_bitArray(int value) {
		final String method = "convert_bitArrayValue_to_bitArray";
		
		String result = null;
		
		try {
			
			result = String.format("%7s", Integer.toBinaryString(value)).replace(' ', '0');
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return result;
	}
	
	public boolean get_2feira() {
		return _2feira;
	}
	
	public boolean get_3feira() {
		return _3feira;
	}
	
	public boolean get_4feira() {
		return _4feira;
	}
	
	public boolean get_5feira() {
		return _5feira;
	}
	
	public boolean get_6feira() {
		return _6feira;
	}
	
	public boolean get_Sabado() {
		return Sabado;
	}
	
	public boolean get_Domingo() {
		return Domingo;
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\n_2feira :"+_2feira);
			sb.append("\n_3feira :"+_3feira);
			sb.append("\n_4feira :"+_4feira);
			sb.append("\n_5feira :"+_5feira);
			sb.append("\n_6feira :"+_6feira);
			sb.append("\nSabado :"+Sabado);
			sb.append("\nDomingo :"+Domingo);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	} 
}

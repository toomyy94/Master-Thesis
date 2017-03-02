package pt.ptinovacao.arqospocket.service.logs;

import org.slf4j.Logger;

public class MyLogger {

	public static void trace(Logger logger, String method, String description) {
		
		try {
			logger.trace(method + " :: " + description);
		} catch(Exception ex) {}
	}
	
	public static void debug(Logger logger, String method, String description) {
		
		try {
			logger.debug(method + " :: " + description);
		} catch(Exception ex) {}
	}
	
	public static void error(Logger logger, String method, Exception error_description) {
		
		try {
			logger.error(method + " :: " + error_description.toString());
		} catch(Exception ex) {}
	}
}

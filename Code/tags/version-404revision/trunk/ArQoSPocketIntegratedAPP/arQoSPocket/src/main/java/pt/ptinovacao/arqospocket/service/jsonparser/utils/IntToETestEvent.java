package pt.ptinovacao.arqospocket.service.jsonparser.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.jsonparser.enums.ETestEvent;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public class IntToETestEvent {
	
	private final static Logger logger = LoggerFactory.getLogger(IntToETestEvent.class);

	public static ETestEvent getETestEvent(int eTestEvent) {
		final String method = "getETestEvent";
		
		try {
			
			switch(eTestEvent) {
				case 0: return ETestEvent.DATE;
				case 1: return ETestEvent.BOOT;
				case 2: return ETestEvent.ITERATIONS;
				case 3: return ETestEvent.TIME_INTERVAL;
				case 4: return ETestEvent.WEEK;
				case 5: return ETestEvent.USER_REQUEST;
				case 255: return ETestEvent.NONE;
			}
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return ETestEvent.NONE;
	}
}

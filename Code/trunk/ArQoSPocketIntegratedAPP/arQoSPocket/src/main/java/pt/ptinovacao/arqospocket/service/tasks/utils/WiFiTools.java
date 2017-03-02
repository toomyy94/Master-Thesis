package pt.ptinovacao.arqospocket.service.tasks.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;


public class WiFiTools {
	
	private final static Logger logger = LoggerFactory.getLogger(WiFiTools.class);
	
	public static String convertIntToStringIP(int intIP) {
		final String method = "convertIntToStringIP";
        
        try {

        	return String.format("%d.%d.%d.%d", (intIP & 0xff),
                        (intIP >> 8 & 0xff), (intIP >> 16 & 0xff),
                        (intIP >> 24 & 0xff));
        
        } catch(Exception ex) {
        	MyLogger.error(logger, method, ex);
        }
        
        return null;
}

	public static int convertChannel(int frequency) {
		int channel = 0;
		switch(frequency) {
			case 2412:
				channel = 1;
				break;
			case 2417:
				channel = 2;
				break;
			case 2422:
				channel = 3;
				break;
			case 2427:
				channel = 4;
				break;
			case 2432:
				channel = 5;
				break;
			case 2437:
				channel = 6;
				break;
			case 2442:
				channel = 7;
				break;
			case 2447:
				channel = 8;
				break;
			case 2452:
				channel = 9;
				break;
			case 2457:
				channel = 10;
				break;
			case 2462:
				channel = 11;
				break;
			case 2467:
				channel = 12;
				break;
			case 2472:
				channel = 13;
				break;
			case 2484:
				channel = 14;
				break;
				
		}
		return channel;
	}
}

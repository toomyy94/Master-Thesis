package pt.ptinovacao.arqospocket.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;



public class MyWatchDog extends Thread {
	
	private final static Logger logger = LoggerFactory.getLogger(MyWatchDog.class);

	private long sleepTimeSec = 0;
	
	private boolean cancelWatchDog = false;
	private WacthDogInterface ref = null;
	
	public MyWatchDog(long pSleepTime, WacthDogInterface wdi) {
		sleepTimeSec = pSleepTime;
		ref = wdi;
	}
	
	public void cancelWatchDog() {
		cancelWatchDog = true;
	}
	
	public void run() {
		final String method = "run";
		
		try {
			
			Thread.sleep(sleepTimeSec);
			
			if (cancelWatchDog) return;
			
			ref.WacthDog();
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
}

package pt.ptinovacao.arqospocket.service.service;

import pt.ptinovacao.secsipapp.slf4j.impl.GoogleAnalyticsLogger;
import pt.ptinovacao.secsipapp.slf4j.impl.GoogleAnalyticsLogger.LogLevel;
import android.app.Application;

public class MyApplication extends Application {
	
	
	/**
	 * 
	 * 
	 * Apenas para teste da lib do serviço
	 * 
	 * 
	 */
	
	
    public void onCreate() {
        super.onCreate();
 
        GoogleAnalyticsLogger.setContext(this); // Set the context to be used to get the Google Analytics Tracker
         
        //if(BuildConfig.DEBUG) {
            GoogleAnalyticsLogger.setCurrentLogLevel(LogLevel.TRACE); // Set the log level to print all logs
            GoogleAnalyticsLogger.setGoogleAnalyticsEnabled(false); // Disable reporting logs to Google Analytics, print only locally
        //}
    }
}

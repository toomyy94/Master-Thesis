package pt.ptinovacao.arqospocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.interfaces.IService;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.service.EngineService;
import pt.ptinovacao.arqospocket.interfaces.IServiceBound;
import pt.ptinovacao.arqospocket.util.LocaleHelper;
import pt.ptinovacao.secsipapp.slf4j.impl.GoogleAnalyticsLogger;
import pt.ptinovacao.secsipapp.slf4j.impl.GoogleAnalyticsLogger.LogLevel;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class MyApplication extends Application {
	
	private final static Logger logger = LoggerFactory.getLogger(MyApplication.class);
	
	private IService mEngineService = null;
	private boolean mIsBound = false;
	
	private IServiceBound isb;

	public IService getEngineServiceRef() {
		return mEngineService;
	}
	
	private ServiceConnection mConnection = new ServiceConnection() { 
		
	    public void onServiceConnected(ComponentName className, IBinder service) {
	    	final String method = "onServiceConnected";
	    	
	    	MyLogger.trace(logger, method, "In");
	    	
	        mEngineService = ((EngineService.LocalBinder)service).getService();
	        mIsBound = true;	        
		    
	        if(isb != null) {
	        	isb.onServiceBound();
	        	MyLogger.trace(logger, method, "Service bound");
	        }
	    }

	    public void onServiceDisconnected(ComponentName className) {
	    	final String method = "onServiceDisconnected";
	    	
	    	MyLogger.trace(logger, method, "In");
	    	
	        mEngineService = null;
	    }
	};
	
	void doBindService() {
		final String method = "doBindService";
		
		MyLogger.trace(logger, method, "In");
        
		Intent startServiceIntent = new Intent(this, EngineService.class);
        startService(startServiceIntent); 
		
	    bindService(new Intent(this, EngineService.class), mConnection, Context.BIND_AUTO_CREATE);
	    
	}
	
	void doUnbindService() {
		final String method = "doUnbindService";
		
		MyLogger.trace(logger, method, "In"); 
		
	    if (mIsBound) {
	        unbindService(mConnection);
	        mIsBound = false;
	    }
	}
	
	
	@Override
    public void onCreate() {
        super.onCreate();

        LocaleHelper.onCreate(this);

		GoogleAnalyticsLogger.setContext(this); // Set the context to be used to get the Google Analytics Tracker
         
        //if(BuildConfig.DEBUG) {
            GoogleAnalyticsLogger.setCurrentLogLevel(LogLevel.TRACE); // Set the log level to print all logs
            GoogleAnalyticsLogger.setGoogleAnalyticsEnabled(false); // Disable reporting logs to Google Analytics, print only locally
        //}
            
        doBindService();
    }
    
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		
		doUnbindService();
	}
	
	public void registerOnServiceBound(IServiceBound isb) {
		final String method = "registerOnServiceBound";
		this.isb = isb;
		MyLogger.trace(logger, method, "IServiceBound registered");
	}
}

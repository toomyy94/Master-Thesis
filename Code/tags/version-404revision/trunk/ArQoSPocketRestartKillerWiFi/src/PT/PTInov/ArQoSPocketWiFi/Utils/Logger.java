package PT.PTInov.ArQoSPocketWiFi.Utils;

import android.util.Log;

public class Logger {
	
	private static final String tag = "Logger";
	
	private static final LogType Debug = LogType.Debug;
	
	public static void v(String tag, LogType logType, String errorLog) {
		//System.out.println(tag+" :: "+errorLog);
		
		switch(Debug) {
			case Debug:
				// print all logs
				Log.v(tag, errorLog);
				break;
			case Trace:
				// print error and trace logs
				if (logType == LogType.Error || logType == LogType.Trace) {
					Log.v(tag, errorLog);
				}
				break;
			case Error:
				// print only error logs
				if (logType == LogType.Error) {
					Log.v(tag, errorLog);
				}
				break;
		}
	}
	
	public static void v(String tag, String tagMethod, LogType logType, String errorLog) {
		//System.out.println(tag+" :: "+errorLog);
		
		switch(Debug) {
			case Debug:
				// print all logs
				Log.v(tag, tagMethod+" :: "+errorLog);
				break;
			case Trace:
				// print error and trace logs
				if (logType == LogType.Error || logType == LogType.Trace) {
					Log.v(tag, tagMethod+" :: "+errorLog);
				}
				break;
			case Error:
				// print only error logs
				if (logType == LogType.Error) {
					Log.v(tag, tagMethod+" :: "+errorLog);
				}
				break;
		}
	}
}

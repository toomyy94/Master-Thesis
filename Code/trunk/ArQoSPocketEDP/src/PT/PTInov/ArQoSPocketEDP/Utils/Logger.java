package PT.PTInov.ArQoSPocketEDP.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;

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
	
	public static void savaToFile(String logText) {
		
		try {
			
			File myFile = new File("/sdcard/logeMon/"+System.currentTimeMillis()+".txt");
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(myFile, true));
			bw.append(logText);
			bw.append('\n');

			bw.close();
			
		} catch(Exception ex) {
			v("Logger", "savaToFile", LogType.Error, ex.toString());
		}
	}
}

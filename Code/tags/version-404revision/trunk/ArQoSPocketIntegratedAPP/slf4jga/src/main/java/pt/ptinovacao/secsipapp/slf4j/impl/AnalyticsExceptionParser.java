package pt.ptinovacao.secsipapp.slf4j.impl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import com.google.analytics.tracking.android.ExceptionParser;
 
/**
 * Google analytics exception parser. 
 * Formats the description to send to Analytics from the exceptions. 
 *
 */
public class AnalyticsExceptionParser implements ExceptionParser {
  
  @Override
  public String getDescription(String p_thread, Throwable p_throwable) {
	 try{
		 
	     return "Thread: " + p_thread + ", Exception: " + ExceptionUtils.getStackTrace(p_throwable);

	 }catch(Exception e)
	 {
		return ""; 
	 }
  }
}
package pt.ptinovacao.secsipapp.slf4j.impl;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

import android.content.Context;
import android.util.Log;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

/**
 * SLF4J implementation that sends logs to the Android logging framework and to the Google Analytics.
 * The method {@link #setContext(Context)} must be called before logs are reported to Google Analytics, 
 * otherwise they are just logged locally to the Android logging framework.
 *
 */
public class GoogleAnalyticsLogger extends MarkerIgnoringBase {
	private static final long serialVersionUID = -1227274521521287937L;
	
	private static Context context = null;
	private static LogLevel currentLogLevel = LogLevel.INFO;
	private static boolean googleAnalyticsEnabled = true;
	
	
	GoogleAnalyticsLogger(final String name)
	{
		this.name = name;
	}

	/* @see org.slf4j.Logger#isTraceEnabled() */
	public boolean isTraceEnabled()
	{
		return isLevelEnabled(LogLevel.TRACE);
	}

	/* @see org.slf4j.Logger#trace(java.lang.String) */
	public void trace(final String msg)
	{
		log(LogLevel.TRACE, msg, null);
	}

	/* @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object) */
	public void trace(final String format, final Object param1)
	{
		formatAndLog(LogLevel.TRACE, format, param1, null);
	}

	/* @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object, java.lang.Object) */
	public void trace(final String format, final Object param1, final Object param2)
	{
		formatAndLog(LogLevel.TRACE, format, param1, param2);
	}

	/* @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object[]) */
	public void trace(final String format, final Object... argArray)
	{
		formatAndLog(LogLevel.TRACE, format, argArray);
	}

	/* @see org.slf4j.Logger#trace(java.lang.String, java.lang.Throwable) */
	public void trace(final String msg, final Throwable t)
	{
		log(LogLevel.TRACE, msg, t);
	}

	/* @see org.slf4j.Logger#isDebugEnabled() */
	public boolean isDebugEnabled()
	{
		return isLevelEnabled(LogLevel.DEBUG);
	}

	/* @see org.slf4j.Logger#debug(java.lang.String) */
	public void debug(final String msg)
	{
		log(LogLevel.DEBUG, msg, null);
	}

	/* @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object) */
	public void debug(final String format, final Object arg1)
	{
		formatAndLog(LogLevel.DEBUG, format, arg1, null);
	}

	/* @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object, java.lang.Object) */
	public void debug(final String format, final Object param1, final Object param2)
	{
		formatAndLog(LogLevel.DEBUG, format, param1, param2);
	}

	/* @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object[]) */
	public void debug(final String format, final Object... argArray)
	{
		formatAndLog(LogLevel.DEBUG, format, argArray);
	}

	/* @see org.slf4j.Logger#debug(java.lang.String, java.lang.Throwable) */
	public void debug(final String msg, final Throwable t)
	{
		log(LogLevel.DEBUG, msg, t);
	}

	/* @see org.slf4j.Logger#isInfoEnabled() */
	public boolean isInfoEnabled()
	{
		return isLevelEnabled(LogLevel.INFO);
	}

	/* @see org.slf4j.Logger#info(java.lang.String) */
	public void info(final String msg)
	{
		log(LogLevel.INFO, msg, null);
	}

	/* @see org.slf4j.Logger#info(java.lang.String, java.lang.Object) */
	public void info(final String format, final Object arg)
	{
		formatAndLog(LogLevel.INFO, format, arg, null);
	}

	/* @see org.slf4j.Logger#info(java.lang.String, java.lang.Object, java.lang.Object) */
	public void info(final String format, final Object arg1, final Object arg2)
	{
		formatAndLog(LogLevel.INFO, format, arg1, arg2);
	}

	/* @see org.slf4j.Logger#info(java.lang.String, java.lang.Object[]) */
	public void info(final String format, final Object... argArray)
	{
		formatAndLog(LogLevel.INFO, format, argArray);
	}

	/* @see org.slf4j.Logger#info(java.lang.String, java.lang.Throwable) */
	public void info(final String msg, final Throwable t)
	{
		log(LogLevel.INFO, msg, t);
	}

	/* @see org.slf4j.Logger#isWarnEnabled() */
	public boolean isWarnEnabled()
	{
		return isLevelEnabled(LogLevel.WARN);
	}

	/* @see org.slf4j.Logger#warn(java.lang.String) */
	public void warn(final String msg)
	{
		log(LogLevel.WARN, msg, null);
	}

	/* @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object) */
	public void warn(final String format, final Object arg)
	{
		formatAndLog(LogLevel.WARN, format, arg, null);
	}

	/* @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object, java.lang.Object) */
	public void warn(final String format, final Object arg1, final Object arg2)
	{
		formatAndLog(LogLevel.WARN, format, arg1, arg2);
	}

	/* @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object[]) */
	public void warn(final String format, final Object... argArray)
	{
		formatAndLog(LogLevel.WARN, format, argArray);
	}

	/* @see org.slf4j.Logger#warn(java.lang.String, java.lang.Throwable) */
	public void warn(final String msg, final Throwable t)
	{
		log(LogLevel.WARN, msg, t);
	}

	/* @see org.slf4j.Logger#isErrorEnabled() */
	public boolean isErrorEnabled()
	{
		return isLevelEnabled(LogLevel.ERROR);
	}

	/* @see org.slf4j.Logger#error(java.lang.String) */
	public void error(final String msg)
	{
		log(LogLevel.ERROR, msg, null);
	}

	/* @see org.slf4j.Logger#error(java.lang.String, java.lang.Object) */
	public void error(final String format, final Object arg)
	{
		formatAndLog(LogLevel.ERROR, format, arg, null);
	}

	/* @see org.slf4j.Logger#error(java.lang.String, java.lang.Object, java.lang.Object) */
	public void error(final String format, final Object arg1, final Object arg2)
	{
		formatAndLog(LogLevel.ERROR, format, arg1, arg2);
	}

	/* @see org.slf4j.Logger#error(java.lang.String, java.lang.Object[]) */
	public void error(final String format, final Object... argArray)
	{
		formatAndLog(LogLevel.ERROR, format, argArray);
	}

	/* @see org.slf4j.Logger#error(java.lang.String, java.lang.Throwable) */
	public void error(final String msg, final Throwable t)
	{
		log(LogLevel.ERROR, msg, t);
	}
	
	/**
	 * Checks if the specified log level is enabled. 
	 * 
	 * @param level log level to check
	 * @return {@code true} if enabled, {@code false} otherwise
	 */
	public boolean isLevelEnabled(LogLevel level) {		
		return level.ordinal() >= currentLogLevel.ordinal();
	}

	private void formatAndLog(LogLevel level, String format, Object arg1, Object arg2) {
		if (!isLevelEnabled(level)) {
			return;
		}
		FormattingTuple tp = MessageFormatter.format(format, arg1, arg2);
		log(level, tp.getMessage(), tp.getThrowable());
	}

	private void formatAndLog(LogLevel level, String format, Object... arguments) {
		if (!isLevelEnabled(level)) {
			return;
		}
		FormattingTuple tp = MessageFormatter.arrayFormat(format, arguments);
		log(level, tp.getMessage(), tp.getThrowable());
	}
	
	private void log(LogLevel level, String message, Throwable t) {
		if (!isLevelEnabled(level)) {
			return;
		}
		
		switch(level) {
			case TRACE:
				Log.v(name, message, t);
				break;
			case DEBUG:
				Log.d(name, message, t);
				break;
			case INFO:
				Log.i(name, message, t);
				break;
			case WARN:
				Log.w(name, message, t);
				break;
			case ERROR:
				Log.e(name, message, t);
				break;				
		}

		sendToGoogleAnalytics(level, message, t);

	}
	
	private void sendToGoogleAnalytics(LogLevel level, String message, final Throwable t) {
		if(context != null && googleAnalyticsEnabled) {
			EasyTracker.getInstance(context).send(MapBuilder
					.createException(formatExceptionDescription(level, message, t),                                  
							false)                                               
							.build());
		}
	}
	
	private String formatExceptionDescription(LogLevel level, String message, Throwable t) {
		return String.format("%s:%s, %s", level, message, new AnalyticsExceptionParser().getDescription(Thread.currentThread().getName(), t));
	}
	
	/**
	 * Log levels.
	 */
	public static enum LogLevel { TRACE, DEBUG, INFO, WARN, ERROR};
	
	/**
	 * Returns the current log level.
	 * The default log level is INFO.
	 * 
	 * @return log level
	 */
	public static LogLevel getCurrentLogLevel() {
		return currentLogLevel;
	}
	
	/**
	 * Checks if the logs are reported to Google Analytics.
	 * This is true by default.
	 * 
	 * @return {@code true} if the logs are sent to Google Analytics, {@code false} otherwise.
	 */
	public static boolean isGoogleAnalyticsEnabled() {
		return googleAnalyticsEnabled;
	}
	
	/**
	 * Sets the Android context to be used by the Google Analytics tracker.
	 * Until this method is called no logs are reported to the Google Analytics.
	 * 
	 * @param context Android context
	 */
	public static void setContext(Context context) {
		GoogleAnalyticsLogger.context = context;
	}
	
	/**
	 * Sets the current log level.
	 * 
	 * @param level new log level to set
	 */
	public static void setCurrentLogLevel(LogLevel level) {
		currentLogLevel = level;
	}
	
	/**
	 * Sets if the logs are reported to Google Analytics or not.
	 * 
	 * @param enabled if {@code true} logs are sent; if {@code false} logs are just logged locally on the Android logging framework
	 */
	public static void setGoogleAnalyticsEnabled(boolean enabled) {
		googleAnalyticsEnabled = enabled;
	}
		
}


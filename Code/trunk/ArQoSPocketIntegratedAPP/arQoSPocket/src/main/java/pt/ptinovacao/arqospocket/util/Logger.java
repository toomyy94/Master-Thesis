package pt.ptinovacao.arqospocket.util;

import android.util.Log;

public class Logger
{
	static boolean LOG_MODE = true;
	static String TAG = "Logger";

	public static void logE(String msg)
	{
		if (LOG_MODE)
		{
			if (msg != null)
			{
				Log.e(TAG, getLogClass() + msg);
			}

		}
	}

	public static void logE(Throwable th)
	{
		if (LOG_MODE)
		{
			if (th != null)
			{
				Log.e(TAG, getLogClass() + th.getCause() + " " + th.getMessage());
				Log.w(TAG, th);
			}

		}
	}

	public static void logW(String msg)
	{
		if (LOG_MODE)
		{
			Log.d(TAG, getLogClass() + msg);
		}
	}

	public static void logD(String msg)
	{
		if (LOG_MODE)
		{
			if (msg != null)
			{
				Log.d(TAG, getLogClass() + msg);
			}
		}
	}

	public static void logE(Exception e)
	{
		if (LOG_MODE)
		{
			Log.e(TAG, getLogClass() + e.getCause() + " " + e.getMessage());
			e.printStackTrace();
		}
	}

	static String getLogClass()
	{
		try
		{
			StackTraceElement[] str = Thread.currentThread().getStackTrace();

			StackTraceElement callingstr = str[4];

			String cls = callingstr.getClassName();
			int num = callingstr.getLineNumber();

			return cls + ":" + num + " ";
		}
		catch (Exception e)
		{

		}

		return "";

	}
}

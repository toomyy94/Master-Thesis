package PT.PTInov.ArQoSPocketEDP.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.net.ParseException;

public class Utils {
	
	private final static String tag = "Utils";

	public static String buildDateOfNextTest(Date d) {

		return convertNumberNormalDataFormat(d.getDate()) + "-"
				+ convertNumberNormalDataFormat(d.getMonth() + 1) + "-"
				+ (d.getYear() + 1900) + "   "
				+ convertNumberNormalDataFormat(d.getHours()) + ":"
				+ convertNumberNormalDataFormat(d.getMinutes()) + ":"
				+ convertNumberNormalDataFormat(d.getSeconds());
	}

	public static String convertNumberNormalDataFormat(int number) {

		Logger.v(tag, "convertNumberNormalDataFormat", LogType.Trace,
				"number :" + number);

		if (number < 10 && number >= 0) {
			return "0" + number;
		} else {
			return number + "";
		}
	}
	
	static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

	public static Date convertTimeToUTCDate(Date originalDate) {
		
		Date UTCDate = null;
		
		try {

			Logger.v(tag, "convertTimezone", LogType.Trace, "originalDate :"
					+ originalDate.toString());
			
			UTCDate = new Date( originalDate.getTime() + (originalDate.getTimezoneOffset() * 60000));


			Logger.v(tag, "convertTimezone", LogType.Trace,
					"utcCal.getTime() :" + UTCDate.toString());
			
		} catch (Exception ex) {
			Logger.v(tag, "convertTimezone", LogType.Error,
					ex.toString());
		}
		
		return UTCDate;
		
	}
	
	public static String convertDate(Date d) {
		// 20120110113329

		// fazer a conversão da data para enviar para o servidor como pediu o fernando
		d = Utils.convertTimeToUTCDate(d);
		
		return (d.getYear() + 1900) + convertNumberNormalDataFormat(d.getMonth() + 1) + convertNumberNormalDataFormat(d.getDate()) +
				convertNumberNormalDataFormat(d.getHours()) + convertNumberNormalDataFormat(d.getMinutes()) + convertNumberNormalDataFormat(d.getSeconds());
		
	}
	
	public static Date GetUTCdatetimeAsDate() {
		//note: doesn't check for null
		return StringDateToDate(GetUTCdatetimeAsString());
	}

	public static String GetUTCdatetimeAsString() {
		final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		final String utcTime = sdf.format(new Date());

		return utcTime;
	}
	
	public static Date StringDateToDate(String StrDate)
	{
	    Date dateToReturn = null;
	    SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);

	    try
	    {
	        dateToReturn = (Date)dateFormat.parse(StrDate);
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }

	    return dateToReturn;
	}
}

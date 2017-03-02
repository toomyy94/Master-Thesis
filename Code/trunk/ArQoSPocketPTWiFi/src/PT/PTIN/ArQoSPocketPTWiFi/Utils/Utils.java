package PT.PTIN.ArQoSPocketPTWiFi.Utils;

import java.util.Date;

import PT.PTIN.ArQoSPocketPTWiFi.Logs.LogType;
import PT.PTIN.ArQoSPocketPTWiFi.Logs.Logger;

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

	private static String convertNumberNormalDataFormat(int number) {

		Logger.v(tag, "convertNumberNormalDataFormat", LogType.Trace,
				"number :" + number);

		if (number < 10 && number > 0) {
			return "0" + number;
		} else {
			return number + "";
		}
	}
}

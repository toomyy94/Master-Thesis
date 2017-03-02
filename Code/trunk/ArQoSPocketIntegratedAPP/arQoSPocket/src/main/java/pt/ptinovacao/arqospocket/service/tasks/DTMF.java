package pt.ptinovacao.arqospocket.service.tasks;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public class DTMF {

	private final static Logger logger = LoggerFactory.getLogger(DTMF.class);

	private WifiManager wifi = null;
	private Context serviceContext = null;

	public DTMF(Context c) {
		final String method = "DTMF";
		
		MyLogger.trace(logger, method, "IN");
		
		try {
			
			serviceContext = c;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	/**
	 * Dials a number with DTMF chars
	 * Note: When the number is dialed, only the initial number is displayed on the device dialer
	 * For example: dial("*6900,,1") will display only *6900 on the device dialer (but the rest will also be processed)
	 * @param number
	 */
	public void dialDTMF(String number) {
		try {
			number = new String(number.trim().replace(" ", "%20").replace("&", "%26")
					.replace(",", "%2c").replace("(", "%28").replace(")", "%29")
					.replace("!", "%21").replace("=", "%3D").replace("<", "%3C")
					.replace(">", "%3E").replace("#", "%23").replace("$", "%24")
					.replace("'", "%27").replace("*", "%2A").replace("-", "%2D")
					.replace(".", "%2E").replace("/", "%2F").replace(":", "%3A")
					.replace(";", "%3B").replace("?", "%3F").replace("@", "%40")
					.replace("[", "%5B").replace("\\", "%5C").replace("]", "%5D")
					.replace("_", "%5F").replace("`", "%60").replace("{", "%7B")
					.replace("|", "%7C").replace("}", "%7D"));

//			Error here - Pass this code to respective Activity when implement this on AvtivityTestes (Code tested and working)

//			Uri uri = Uri.parse("tel:"+ number);
//			Intent intent = new Intent(Intent.ACTION_CALL, uri);
//			startActivity(intent);

		} catch (Exception e) {
			//getAlertDialog().setMessage("Invalid number");
			e.printStackTrace();
		}
	}

}

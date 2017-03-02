package PT.PTInov.ArQoSPocket.JSONConnector.Util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import PT.PTInov.ArQoSPocket.Utils.LogType;
import PT.PTInov.ArQoSPocket.Utils.Logger;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

public class AndroidInformation {

	private final static String tag = "AndroidInformation";
	
	public static String convertIntToStringIP(int intIP) {
		
		try {

		return String.format("%d.%d.%d.%d", (intIP & 0xff),
				(intIP >> 8 & 0xff), (intIP >> 16 & 0xff),
				(intIP >> 24 & 0xff));
		
		} catch(Exception ex) {
			Logger.v(tag, LogType.Error,
					"convertIntToStringIP :: Error :"+ex.toString());
			return null;
		}
	}
	
	public static String getMacAddress(Context contextRef) {
		try {

			WifiManager wifiManager = (WifiManager) contextRef
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();

			return wifiInfo.getMacAddress();

		} catch (Exception ex) {
			Logger.v(tag, LogType.Error,
					"getMacAddress :: WifiManager unavailable!");
			
			return "NA";
		}
	}
	
	public static String getMyWiFiIP(Context contextRef) {

		try {

			WifiManager wifiManager = (WifiManager) contextRef
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();

			int intIPFormat = wifiInfo.getIpAddress();

			return String.format("%d.%d.%d.%d", (intIPFormat & 0xff),
					(intIPFormat >> 8 & 0xff), (intIPFormat >> 16 & 0xff),
					(intIPFormat >> 24 & 0xff));

		} catch (Exception ex) {
			Logger.v(tag, LogType.Error,
					"getMyWiFiIP :: WifiManager unavailable!");
			return "NA";
		}
	}

	public static String getIpAddress() {
		try {
			String ipv4;
			List<NetworkInterface> nilist = Collections.list(NetworkInterface
					.getNetworkInterfaces());
			for (NetworkInterface ni : nilist) {
				List<InetAddress> ialist = Collections.list(ni
						.getInetAddresses());
				for (InetAddress address : ialist) {
					if (!address.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(ipv4 = address
									.getHostAddress())) {
						return ipv4;
					}
				}

			}

		} catch (SocketException ex) {
			Logger.v(tag, LogType.Error,
					"getIpAddress :: Connection unavailable!");
		}
		return "NA";
	}

	public static String getDeviceIMEI(Context contextRef) {
		
		try {
			
			TelephonyManager m_telephonyManager = (TelephonyManager) contextRef
					.getSystemService(Context.TELEPHONY_SERVICE);

			String IMEI = m_telephonyManager.getDeviceId();
			return (IMEI == null) ? "NA" : IMEI;
			
		} catch (Exception ex) {
			Logger.v(tag, LogType.Error,
					"getIpAddress :: Cannot get the device IMEI!");
		}
		return "NA";
	}
	
	public static String getDeviceModel() {
		try {
			
			 return Build.DEVICE;
			
		}catch (Exception ex) {
			Logger.v(tag, LogType.Error,
					"getIpAddress :: Cannot get the device Model!");
		}
		return "NA";
	}
	
	public static String getDeviceVendedor() {
		try {
			
			 return Build.MANUFACTURER;
			
		}catch (Exception ex) {
			Logger.v(tag, LogType.Error,
					"getIpAddress :: Cannot get the device Vendedor!");
		}
		return "NA";
	}
	
	public static String getDeviceName() {
		try {
			
			 return Build.PRODUCT;
			
		}catch (Exception ex) {
			Logger.v(tag, LogType.Error,
					"getIpAddress :: Cannot get the device name!");
		}
		return "NA";
	}
	
	public static String getHardwareVersion() {
		try {
			
			 return Build.FINGERPRINT;
			
		}catch (Exception ex) {
			Logger.v(tag, LogType.Error,
					"getIpAddress :: Cannot get the device Hardware Version!");
		}
		return "NA";
	}
	
	public static String getActualDate() {
		Date d = new Date();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d); 
		
		return ""+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND)+" "+calendar.get(Calendar.DAY_OF_MONTH)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.YEAR);
	}
}

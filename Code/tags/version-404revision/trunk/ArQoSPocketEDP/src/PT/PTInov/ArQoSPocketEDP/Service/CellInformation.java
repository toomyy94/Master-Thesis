package PT.PTInov.ArQoSPocketEDP.Service;

import java.util.List;

import PT.PTInov.ArQoSPocketEDP.Utils.LogType;
import PT.PTInov.ArQoSPocketEDP.Utils.Logger;
import PT.PTInov.ArQoSPocketEDP.Utils.MyWatchDog;
import PT.PTInov.ArQoSPocketEDP.Utils.WacthDogInterface;
import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
public class CellInformation implements WacthDogInterface {

	private final static String tag = "CellInformation";

	private Context myContext = null;

	private static TelephonyManager telephonyManager = null;
	
	// Do update in 30.... 30 secs
	private final static long updateTimeInMillisec = 10000;
	
	// watchdog update
	MyWatchDog watchDogUpdate = null;

	private static int multDBM = 1;
	private static int subDBM = 116;

	// signal information for GSM
	private int gsmSignalStrength = 0; // GSM Signal Strength, valid values are
										// (0-31, 99) as defined in TS 27.007
										// 8.5 - rssi
	private int gsmBitErrorRate = 0; // GSM bit error rate (0-7, 99) as defined
										// in TS 27.007 8.5 - rxqual

	// cell information for GSM
	private String gsmCellID = "-1";
	private String gsmLac = "-1";
	private int gsmPsc = -1; // only available on Android 2.3
	private int cellPadding = 4;

	// SIM card information
	private String MCC = "NA";
	private String IMEI = "NA";
	private String MCC_MNC = "NA";
	private String OperatorName = "NA";
	private String NetworkType = "NA";
	private String ISOCountry = "NA";
	private String SIMSerialNumber = "NA";
	private String IMSI = "NA";
	private Boolean Roaming = false;
	
	private int canUpdate = 0;
	
	public static final int NETWORK_TYPE_LTE = 13; 

	private List<NeighboringCellInfo> neighboringCellList = null;

	private boolean activeTelephonyManager = false;

	public CellInformation(Context c) {
		try {

			myContext = c;
			
			// Maybe we need to stop this options, depends the battery life
			//activeLocationAutoUpdate();

			updateAndRegestryTelephonyManager();

			if (telephonyManager != null) {
				MCC = telephonyManager.getNetworkCountryIso();
				IMEI = telephonyManager.getDeviceId();
				MCC_MNC = telephonyManager.getNetworkOperator();
				OperatorName = telephonyManager.getNetworkOperatorName();

				int networkType = telephonyManager.getNetworkType();
				switch (networkType) {
				case (TelephonyManager.NETWORK_TYPE_GPRS): // 2G
					NetworkType = "GPRS";
					multDBM = 2;
					subDBM = 113;
					cellPadding = 4;
					break;
				case (TelephonyManager.NETWORK_TYPE_EDGE): // 2G
					multDBM = 2;
					subDBM = 113;
					cellPadding = 4;
					NetworkType = "EDGE";
					break;
				case (TelephonyManager.NETWORK_TYPE_HSDPA): // 3G
					multDBM = 1;
					subDBM = 116;
					cellPadding = 8;
					NetworkType = "HSDPA";
					break;
				case (TelephonyManager.NETWORK_TYPE_HSPA): // 3G
					multDBM = 1;
					subDBM = 116;
					cellPadding = 8;
					NetworkType = "HSPA";
					break;
				case (TelephonyManager.NETWORK_TYPE_HSUPA): // 3G
					multDBM = 1;
					subDBM = 116;
					cellPadding = 8;
					NetworkType = "HSUPA";
					break;
				case (TelephonyManager.NETWORK_TYPE_UMTS): // 3G
					multDBM = 1;
					subDBM = 116;
					cellPadding = 8;
					NetworkType = "UMTS";
					break;
				case (NETWORK_TYPE_LTE): // LTE
					multDBM = 1;
					subDBM = 116;
					cellPadding = 8;
					NetworkType = "LTE";
					break;
				case (TelephonyManager.NETWORK_TYPE_UNKNOWN): // 3G
					NetworkType = "UNKNOWN";
					break;
				default:
					break;
				}

				ISOCountry = telephonyManager.getSimCountryIso();
				SIMSerialNumber = telephonyManager.getSimSerialNumber();
				IMSI = telephonyManager.getSubscriberId();
				Roaming = telephonyManager.isNetworkRoaming();

				neighboringCellList = telephonyManager.getNeighboringCellInfo();
			}

			// create the listeners
		} catch (Exception ex) {
			Logger.v(tag, "CellInformation", LogType.Error, ex.toString());
		}

	}
	
	public void activeLocationAutoUpdate() {
		try {
			watchDogUpdate = new MyWatchDog(updateTimeInMillisec, this);
			watchDogUpdate.start();
		} catch (Exception ex) {
			Logger.v(tag, "activeLocationAutoUpdate", LogType.Error,
					ex.toString());
		}
	}
	
	public void updateAndRegestryTelephonyManager() {
		
		try {
			
			/*
			try {
				
				telephonyManager.hasIccCard();
				
			} catch(Exception ex) {*/
				
				Logger.v(tag, "updateAndRegestryTelephonyManager", LogType.Error, "Loading new telephonyManager!");
				
				telephonyManager = (TelephonyManager) myContext
						.getSystemService(Context.TELEPHONY_SERVICE);
				

				if (telephonyManager != null) {
					activeTelephonyManager = true;
				}

				telephonyManager.listen(cellLocationListener,
						PhoneStateListener.LISTEN_CELL_LOCATION);
				telephonyManager.listen(cellStrengthListener,
						PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
				telephonyManager.listen(DataConnectionChangeListener,
						PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
				
				
			//}
		
		} catch(Exception ex) {
			Logger.v(tag, "updateAndRegestryTelephonyManager", LogType.Error, ex.toString());
		}
		
	}

	public List<NeighboringCellInfo> getNeighboringCellList() {
		return neighboringCellList;
	}

	public boolean getActiveTelephonyManager() {
		return activeTelephonyManager;
	}

	public int getGsmSignalStrength() {
		updateAndRegestryTelephonyManager();
		
		return gsmSignalStrength;
	}

	public int getGsmBitErrorRate() {
		return gsmBitErrorRate;
	}

	public String getGsmCellID() {
		return gsmCellID;
	}

	public String getGsmLac() {
		return gsmLac;
	}

	public int getGsmPsc() {
		return gsmPsc;
	}

	public String getMCC() {
		return MCC;
	}

	public String getIMEI() {
		return IMEI;
	}

	public String getMCC_MNC() {
		return MCC_MNC;
	}

	public String getOperatorName() {
		return OperatorName;
	}

	public String getNetworkType() {
		return NetworkType;
	}

	public String getISOCountry() {
		return ISOCountry;
	}

	public String getSIMSerialNumber() {
		return SIMSerialNumber;
	}

	public String getIMSI() {
		return IMSI;
	}

	public String getRoaming() {
		return (Roaming) ? "True" : "False";
	}

	// //
	// listeners
	// //

	private PhoneStateListener cellLocationListener = new PhoneStateListener() {
		public void onCellLocationChanged(CellLocation callLocation) {

			Logger.v(tag, "cellLocationListener", LogType.Trace,
					"onCellLocationChanged");

			try {
				
				//updateAndRegestryTelephonyManager();

				GsmCellLocation gsmLocation = (GsmCellLocation) callLocation;
				gsmCellID = getPaddedHex(gsmLocation.getCid(), cellPadding);
				//gsmLac = getPaddedHex(gsmLocation.getLac(), cellPadding);
				//gsmCellID = gsmLocation.getCid()+"";
				gsmLac = gsmLocation.getLac()+"";

				gsmPsc = gsmLocation.getPsc();

				neighboringCellList = telephonyManager.getNeighboringCellInfo();

			} catch (Exception ex) {
				Logger.v(tag, "cellLocationListener", LogType.Error,
						ex.toString());
			}
		}
	};

	private PhoneStateListener DataConnectionChangeListener = new PhoneStateListener() {
		public void onDataConnectionStateChanged(int state, int networkType) {

			Logger.v(tag, "onDataConnectionStateChanged", LogType.Trace, "..................................................In");

			try {
				
				//updateAndRegestryTelephonyManager();

				switch (networkType) {
				case (TelephonyManager.NETWORK_TYPE_GPRS): // 2G
					
					NetworkType = "GPRS";
					multDBM = 2;
					subDBM = 113;
					cellPadding = 4;
					break;
				case (TelephonyManager.NETWORK_TYPE_EDGE): // 2G
					
					multDBM = 2;
					subDBM = 113;
					cellPadding = 4;
					NetworkType = "EDGE";
					break;
				case (TelephonyManager.NETWORK_TYPE_HSDPA): // 3G
					
					multDBM = 1;
					subDBM = 116;
					cellPadding = 8;
					NetworkType = "HSDPA";
					break;
				case (TelephonyManager.NETWORK_TYPE_HSPA): // 3G
					
					multDBM = 1;
					subDBM = 116;
					cellPadding = 8;
					NetworkType = "HSPA";
					break;
				case (TelephonyManager.NETWORK_TYPE_HSUPA): // 3G
					
					multDBM = 1;
					subDBM = 116;
					cellPadding = 8;
					NetworkType = "HSUPA";
					break;
				case (TelephonyManager.NETWORK_TYPE_UMTS): // 3G
				
					multDBM = 1;
					subDBM = 116;
					cellPadding = 8;
					NetworkType = "UMTS";
					break;
				case (NETWORK_TYPE_LTE): // LTE
					multDBM = 1;
					subDBM = 116;
					cellPadding = 8;
					NetworkType = "LTE";
					break;
				case (TelephonyManager.NETWORK_TYPE_UNKNOWN): // 3G
					
					if (NetworkType.contains("GPRS") || NetworkType.contains("EDGE")) {
						updateAndRegestryTelephonyManager();
					}
					
					NetworkType = "UNKNOWN";
					break;
				default:
					break;
				}
				
				canUpdate++;

				neighboringCellList = telephonyManager.getNeighboringCellInfo();

				Logger.v(tag, "onDataConnectionStateChanged", LogType.Trace,
						"NetworkType :" + NetworkType);

			} catch (Exception ex) {
				Logger.v(tag, "onDataConnectionStateChanged", LogType.Error,
						ex.toString());
			}
		}
	};

	@SuppressLint("NewApi")
	private PhoneStateListener cellStrengthListener = new PhoneStateListener() {
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {

			Logger.v(tag, "onSignalStrengthsChanged", LogType.Trace,
					"onSignalStrengthsChanged");

			try {
				
				//updateAndRegestryTelephonyManager();

				if (signalStrength.isGsm()) {
					gsmSignalStrength = convertToDBM(signalStrength.getGsmSignalStrength());
					gsmBitErrorRate = signalStrength.getGsmBitErrorRate();
				}

				neighboringCellList = telephonyManager.getNeighboringCellInfo();

			} catch (Exception ex) {
				Logger.v(tag, "onSignalStrengthsChanged", LogType.Error,
						ex.toString());
			}
		}
	};

	public static int convertToDBM(int signalStrengthSystem) {
		return (-subDBM + (multDBM * signalStrengthSystem));
	}

	/**
	 * Convert an int to an hex String and pad with 0's up to minLen.
	 */
	/*
	private String getPaddedHex(int nr, int minLen) {
		String str = Integer.toHexString(nr);
		
		//if (str != null) {
		//	while (str.length() < minLen) {
		//		str = "0" + str;
		//	}
		//}
		return str;
	}*/
	
	/*
	private String getPaddedHex(int nr, int minLen) {
		
		Logger.v(tag, "getPaddedHex", LogType.Debug, "..........................................nr :"+nr+" minLen :"+minLen);
		
		if (minLen == 4) {
			
			return nr+"";
			
		} else {
			String str = Integer.toHexString(nr);
			str = str.substring(2, str.length());
			
			return Integer.parseInt(str, 16)+"";
		}
	}*/
	
	private String getPaddedHex(int nr, int minLen) {
		
		Logger.v(tag, "getPaddedHex", LogType.Debug, "..........................................nr :"+nr+" minLen :"+minLen+" NetworkType :"+NetworkType);
		
		try {
		
			if (NetworkType.contains("GPRS") ||  NetworkType.contains("EDGE") ||  NetworkType.contains("LTE")) {
			
				Logger.v(tag, "getPaddedHex", LogType.Debug, "........tou aki 2g");
				return nr+"";
			
			} else {
				
				Logger.v(tag, "getPaddedHex", LogType.Debug, "........tou aki 3g");
				
				String str = Integer.toHexString(nr);
				str = str.substring(2, str.length());
			
				return Integer.parseInt(str, 16)+"";
			}
			
		} catch(Exception ex) {
			Logger.v(tag, "getPaddedHex", LogType.Error,
					ex.toString());
			
			return "NA";
		}
	}


	public void WacthDog() {
		// TODO Auto-generated method stub
		//updateAndRegestryTelephonyManager();
	}
}

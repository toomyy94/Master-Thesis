package PT.PTInov.ArQoSPocket.Service;

import java.util.List;

import PT.PTInov.ArQoSPocket.Enums.DataStateEnum;
import PT.PTInov.ArQoSPocket.Utils.LogType;
import PT.PTInov.ArQoSPocket.Utils.Logger;
import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

@SuppressLint("NewApi")
public class RadioInformation {

	private final static String tag = "RadioInformation";

	private Context myContext = null;
	private static TelephonyManager telephonyManager = null;
	
	private int currentNetworkType = -1;
	private int phoneType = -1;
	
	private List<CellInfo> observedCells = null;
	private List<NeighboringCellInfo> neighboringCellList = null;
	
	private int CDMA_DBM = 0;
	private int CDMA_ECIO = 0;
	private int EVDO_DBM = 0;
	private int EVDO_ECIO = 0;
	private int EVDO_SNR = 0;
	private int BER = 0;
	private int RXL = 0;
	
	private int CID = 0;
	private int LCID = 0;
	private int LAC = 0;
	private int PSC = 0;
	
	private int RSRP = 0; // tenho de fazer os calculos para isto em Asu
	private int RSRQ = 0; // tenho de ver como se obtem isto
	
	private int baseStationId = 0;
	private int networkId = 0;
	private int softwareId = 0;
	
	private boolean NETWORK_MANUAL_SELECTION_MODE = false;
	private String REGISTED_OPERATOR_NAME = "NA";
	private String SHORT_REGISTED_OPERATOR_NAME = "NA";
	private String OPERATOR_NUMERIC_ID = "NA";
	private boolean IS_ROAMING = false;
	
	
	private static int multDBM = 1;
	private static int subDBM = 116;
	private int cellPadding = 4;
	
	
	public RadioInformation(Context c) {
		final String method = "RadioInformation";
		
		try {

			myContext = c;
			
			telephonyManager = (TelephonyManager) myContext.getSystemService(Context.TELEPHONY_SERVICE);
			
			// registry the listeners
			telephonyManager.listen(cellStrengthListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
			telephonyManager.listen(DataConnectionChangeListener, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
			telephonyManager.listen(cellLocationListener,PhoneStateListener.LISTEN_CELL_LOCATION);
			telephonyManager.listen(cellInfoListener,PhoneStateListener.LISTEN_CELL_INFO);
			telephonyManager.listen(serviceStateListener,PhoneStateListener.LISTEN_SERVICE_STATE);
			
			currentNetworkType = telephonyManager.getNetworkType();
			phoneType = telephonyManager.getPhoneType();
			
			Logger.v(tag, method, LogType.Trace, "currentNetworkType :"+currentNetworkType);
			Logger.v(tag, method, LogType.Trace, "phoneType :"+phoneType);
			
		} catch(Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
	}
	
	public String getCurrentNetworkType() {
		
		String NetworkType = "UNKNOWN";
		
		switch (currentNetworkType) {
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
		case (TelephonyManager.NETWORK_TYPE_LTE): // LTE
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
		
		return NetworkType;
	}
	
	public List<NeighboringCellInfo> getNeighboringCellList() {
		return neighboringCellList;
	}
	
	public int get_RSRP () {
		return RSRP ;
	}
	
	public int get_RSRQ () {
		return RSRQ ;
	}
	
	public int get_baseStationId () {
		return baseStationId ;
	}
	
	public int get_networkId () {
		return networkId ;
	}
	
	public int get_softwareId () {
		return softwareId ;
	}
	
	
	public boolean get_NETWORK_MANUAL_SELECTION_MODE () {
		return NETWORK_MANUAL_SELECTION_MODE ;
	}
	
	public boolean get_IS_ROAMING () {
		return IS_ROAMING ;
	}
	
	
	public String get_REGISTED_OPERATOR_NAME () {
		return REGISTED_OPERATOR_NAME ;
	}
	
	public String get_SHORT_REGISTED_OPERATOR_NAME () {
		return SHORT_REGISTED_OPERATOR_NAME ;
	}
	
	public String get_OPERATOR_NUMERIC_ID () {
		return OPERATOR_NUMERIC_ID ;
	}
	
	public int get_CID () {
		return CID ;
	}
	
	public int get_LAC () {
		return LAC ;
	}
	
	public int get_PSC () {
		return PSC ;
	}
	
	
	public int get_CDMA_DBM () {
		return CDMA_DBM ;
	}
	
	public int get_CDMA_ECIO () {
		return CDMA_ECIO ;
	}
	
	public int get_EVDO_DBM () {
		return EVDO_DBM ;
	}
	
	public int get_EVDO_ECIO () {
		return EVDO_ECIO ;
	}
	
	public int get_EVDO_SNR () {
		return EVDO_SNR ;
	}
	
	public int get_BER () {
		return BER ;
	}
	
	public int get_RXL () {
		return RXL ;
	}
	
	public int get_LCID() {
		return LCID;
	}
	
	public List<NeighboringCellInfo> getNeighboringCellInfoList() {
		final String method = "getNeighboringCellInfoList";
		
		try {
			return telephonyManager.getNeighboringCellInfo();
		} catch(Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return null;
	}
	
	public String getDeviceId() {
		final String method = "getDeviceId";
		
		try {
			return telephonyManager.getDeviceId();
		} catch(Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return null;
	}
	
	public String getLine1Number() {
		final String method = "getLine1Number";
		
		try {
			return telephonyManager.getLine1Number();
		} catch(Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return null;
	}
	
	public DataStateEnum getDataState() {
		final String method = "getDeviceId";
		
		try {
			switch(telephonyManager.getDataState()) {
			case 0:
				return DataStateEnum.DATA_DISCONNECTED;
			case 1:
				return DataStateEnum.DATA_CONNECTING;
			case 2:
				return DataStateEnum.DATA_CONNECTED;
			case 3:
				return DataStateEnum.DATA_SUSPENDED;
			}
		} catch(Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return DataStateEnum.NA;
	}
	
	public String getNetworkCountryIso() {
		final String method = "getNetworkCountryIso";
		
		try {
			return telephonyManager.getNetworkCountryIso();
		} catch(Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return null;
	}
	
	public String getNetworkOperator() {
		final String method = "getNetworkOperator";
		
		try {
			return telephonyManager.getNetworkOperator();
		} catch(Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return null;
	}
	
	public String getNetworkOperatorName() {
		final String method = "getNetworkOperatorName";
		
		try {
			return telephonyManager.getNetworkOperatorName();
		} catch(Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return null;
	}
	
	public String getSimCountryIso() {
		final String method = "getSimCountryIso";
		
		try {
			return telephonyManager.getSimCountryIso();
		} catch(Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return null;
	}
	
	public String getSimOperator() {
		final String method = "getSimOperator";
		
		try {
			return telephonyManager.getSimOperator();
		} catch(Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return null;
	}
	
	public String getSimOperatorName() {
		final String method = "getSimOperatorName";
		
		try {
			return telephonyManager.getSimOperatorName();
		} catch(Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return null;
	}
	
	public String getSimSerialNumber() {
		final String method = "getSimSerialNumber";
		
		try {
			return telephonyManager.getSimSerialNumber();
		} catch(Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return null;
	}
	
	public String getSubscriberId() {
		final String method = "getSubscriberId_IMSI";
		
		try {
			return telephonyManager.getSubscriberId();
		} catch(Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return null;
	}
	
	public boolean isNetworkRoaming() {
		final String method = "isNetworkRoaming";
		
		try {
			return telephonyManager.isNetworkRoaming();
		} catch(Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return false;
	}
	
	public static int convertToDBM(int signalStrengthSystem) {
		return (-subDBM + (multDBM * signalStrengthSystem));
	}
	
	private String getPaddedHex(int nr, int minLen) {
		
		try {
		
			if (currentNetworkType ==  TelephonyManager.NETWORK_TYPE_GPRS ||  currentNetworkType ==  TelephonyManager.NETWORK_TYPE_EDGE ||  currentNetworkType ==  TelephonyManager.NETWORK_TYPE_LTE) {
			
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
	
	
	////
	// listeners
	////
	
	private PhoneStateListener cellStrengthListener = new PhoneStateListener() {
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			final String method = "onSignalStrengthsChanged";
			
			Logger.v(tag, method, LogType.Trace, "onSignalStrengthsChanged");

			try {
				
				Logger.v(tag, method, LogType.Trace, "getCdmaDbm :" + signalStrength.getCdmaDbm());
				Logger.v(tag, method, LogType.Trace, "getCdmaEcio :" + signalStrength.getCdmaEcio());
				Logger.v(tag, method, LogType.Trace, "getEvdoDbm :" + signalStrength.getEvdoDbm());
				Logger.v(tag, method, LogType.Trace, "getEvdoEcio :" + signalStrength.getEvdoEcio());
				Logger.v(tag, method, LogType.Trace, "getEvdoSnr :" + signalStrength.getEvdoSnr());
				Logger.v(tag, method, LogType.Trace, "getGsmBitErrorRate :" + signalStrength.getGsmBitErrorRate());
				Logger.v(tag, method, LogType.Trace, "getGsmSignalStrength :" + signalStrength.getGsmSignalStrength());
				Logger.v(tag, method, LogType.Trace, "isGsm :" + signalStrength.isGsm());
				
				CDMA_DBM = signalStrength.getCdmaDbm();
				CDMA_ECIO = signalStrength.getCdmaEcio();
				EVDO_DBM = signalStrength.getEvdoDbm();
				EVDO_ECIO = signalStrength.getEvdoEcio();
				EVDO_SNR = signalStrength.getEvdoSnr();
				BER = signalStrength.getGsmBitErrorRate();
				RSRP = convertToDBM(signalStrength.getGsmSignalStrength());
				
				RXL = RSRP + 20;
				
				Logger.v(tag, method, LogType.Trace, "RXL :" + RXL);
				Logger.v(tag, method, LogType.Trace, "RSRP :" + RSRP);
				
				neighboringCellList = telephonyManager.getNeighboringCellInfo();

			} catch (Exception ex) {
				Logger.v(tag, method, LogType.Error, ex.toString());
			}
		}
	};
	
	@SuppressLint("NewApi")
	private PhoneStateListener cellLocationListener = new PhoneStateListener() {
		@SuppressLint("NewApi")
		public void onCellLocationChanged(CellLocation cellLocation) {
			final String method = "onCellLocationChanged";

			Logger.v(tag, method, LogType.Trace, "In");

			try {
				
				if (cellLocation instanceof CdmaCellLocation) {
					
					Logger.v(tag, method, LogType.Trace, "CdmaCellLocation!");
					
					CdmaCellLocation cdmaLocation = (CdmaCellLocation) cellLocation;
					
					Logger.v(tag, method, LogType.Trace, "BaseStationId :"+cdmaLocation.getBaseStationId());
					Logger.v(tag, method, LogType.Trace, "BaseStationLatitude :"+cdmaLocation.getBaseStationLatitude());
					Logger.v(tag, method, LogType.Trace, "Longitude :"+cdmaLocation.getBaseStationLongitude());
					Logger.v(tag, method, LogType.Trace, "NetworkId :"+cdmaLocation.getNetworkId());
					Logger.v(tag, method, LogType.Trace, "SystemId :"+cdmaLocation.getSystemId());
					
					baseStationId = cdmaLocation.getBaseStationId();
					networkId = cdmaLocation.getNetworkId();
					softwareId = cdmaLocation.getSystemId();
				}
				
				if (cellLocation instanceof GsmCellLocation) {
					
					Logger.v(tag, method, LogType.Trace, "GsmCellLocation!");
				
					GsmCellLocation gsmLocation = (GsmCellLocation) cellLocation;
					Logger.v(tag, method, LogType.Trace, "LCID :"+gsmLocation.getCid());
					Logger.v(tag, method, LogType.Trace, "LAC :"+gsmLocation.getLac());
					Logger.v(tag, method, LogType.Trace, "PSC :"+gsmLocation.getPsc());
					
					LCID = gsmLocation.getCid();
					CID = Integer.parseInt(getPaddedHex(gsmLocation.getCid(), cellPadding));
					LAC = gsmLocation.getLac();
					PSC = gsmLocation.getPsc();
					
					Logger.v(tag, method, LogType.Trace, "CID :"+CID);
					Logger.v(tag, method, LogType.Trace, "LCID :"+LCID);
				}
				
				neighboringCellList = telephonyManager.getNeighboringCellInfo();

			} catch (Exception ex) {
				Logger.v(tag, method, LogType.Error,ex.toString());
			}
		}
	};
	
	private PhoneStateListener DataConnectionChangeListener = new PhoneStateListener() {
		public void onDataConnectionStateChanged(int state, int networkType) {
			final String method = "onDataConnectionStateChanged";

			Logger.v(tag, method, LogType.Trace, "In");

			try {
				
				currentNetworkType = networkType;
				
			} catch (Exception ex) {
				Logger.v(tag, method, LogType.Error, ex.toString());
			}
		}
	};
	
	private PhoneStateListener cellInfoListener = new PhoneStateListener() {
		public void onCellInfoChanged(List<CellInfo> cellInfo) {
			final String method = "onCellInfoChanged";
			
			Logger.v(tag, method, LogType.Trace, "In.............................................................................................");

			try {
				
				observedCells = cellInfo;
				
			} catch (Exception ex) {
				Logger.v(tag, method, LogType.Error,ex.toString());
			}
		}
	};
	
	private PhoneStateListener serviceStateListener = new PhoneStateListener() {
		public void onServiceStateChanged(ServiceState serviceState) {
			final String method = "onServiceStateChanged";

			try {
				
				Logger.v(tag, method, LogType.Trace, "getIsManualSelection :"+serviceState.getIsManualSelection());
				Logger.v(tag, method, LogType.Trace, "getOperatorAlphaLong :"+serviceState.getOperatorAlphaLong());
				Logger.v(tag, method, LogType.Trace, "getOperatorAlphaShort :"+serviceState.getOperatorAlphaShort());
				Logger.v(tag, method, LogType.Trace, "getOperatorNumeric :"+serviceState.getOperatorNumeric());
				Logger.v(tag, method, LogType.Trace, "getRoaming :"+serviceState.getRoaming());
				
				NETWORK_MANUAL_SELECTION_MODE = serviceState.getIsManualSelection();
				REGISTED_OPERATOR_NAME = serviceState.getOperatorAlphaLong();
				SHORT_REGISTED_OPERATOR_NAME = serviceState.getOperatorAlphaShort();
				OPERATOR_NUMERIC_ID = serviceState.getOperatorNumeric();
				IS_ROAMING = serviceState.getRoaming();
				
			} catch (Exception ex) {
				Logger.v(tag, method, LogType.Error,ex.toString());
			}
		}
	};
}

package PT.PTInov.ArQoSPocketEDP.DataStructs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import PT.PTInov.ArQoSPocketEDP.Utils.Utils;
import android.telephony.NeighboringCellInfo;

public class NetworkInformationDataStruct {

	private final static String tag = "NetworkInformationDataStruct";

	// Save the last Location update
	private String Latitude = "";
	private String Longitude = "";

	// signal information for GSM
	private int gsmSignalStrength = 0;
	private int gsmBitErrorRate = 0;

	// cell information for GSM
	private String gsmCellID = "0";
	private String gsmLac = "0";
	private int gsmPsc = 0; // only available on Android 2.3

	// SIM card information
	private String MCC = "NA";
	private String IMEI = "NA";
	private String MCC_MNC = "NA";
	private String OperatorName = "NA";
	private String NetworkType = "NA";
	private String ISOCountry = "NA";
	private String SIMSerialNumber = "NA";
	private String IMSI = "NA";
	private String Roaming = "NA";

	//private List<NeighboringCellInfo> neighboringCellList = null;
	private List<MyNeighboringCellInfo> neighboringCellList = null;
	private Date registryDate = null;

	// localização obtida com sucesso
	private boolean hasLocation = false;

	public NetworkInformationDataStruct(String pLatitude, String pLongitude,
			int pgsmSignalStrength, int pgsmBitErrorRate, String pgsmCellID,
			String pgsmLac, int pgsmPsc, String pMCC, String pIMEI,
			String pMCC_MNC, String pOperatorName, String pNetworkType,
			String pISOCountry, String pSIMSerialNumber, String pIMSI,
			String pRoaming, List<NeighboringCellInfo> pNeighboringCellList,
			Date ptestExecDate) {

		Latitude = pLatitude;
		Longitude = pLongitude;
		gsmSignalStrength = pgsmSignalStrength;
		gsmBitErrorRate = pgsmBitErrorRate;
		gsmCellID = pgsmCellID;
		gsmLac = pgsmLac;
		gsmPsc = pgsmPsc;
		MCC = pMCC;
		IMEI = pIMEI;
		MCC_MNC = pMCC_MNC;
		OperatorName = pOperatorName;
		NetworkType = pNetworkType;
		ISOCountry = pISOCountry;
		SIMSerialNumber = pSIMSerialNumber;
		IMSI = pIMSI;
		Roaming = pRoaming;

		// transform List<NeighboringCellInfo> into private List<MyNeighboringCellInfo>
		neighboringCellList = new ArrayList<MyNeighboringCellInfo>(); 
		for (NeighboringCellInfo nci :pNeighboringCellList) {
			neighboringCellList.add(new MyNeighboringCellInfo(nci.getCid(), nci.getLac(), nci.getNetworkType(), nci.getPsc(), nci.getRssi()));
		}
		
		// set if we get the location
		hasLocation = (Latitude.equals("NA"))?false:true;

	}

	public String getLatitude() {
		return Latitude;
	}

	public String getLongitude() {
		return Longitude;
	}

	public int getGsmSignalStrength() {
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
		return Roaming;
	}

	public Date getRegistryDate() {
		return registryDate;
	}

	public String getRegistryDateFormated() {
		return Utils.buildDateOfNextTest(registryDate);
	}

	public boolean hasLocation() {
		return hasLocation;
	}
	
	public List<MyNeighboringCellInfo> getNeighboringCellInfo() {
		return neighboringCellList;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\nLatitude :"+Latitude);
		sb.append("\nLongitude :"+Longitude);
		sb.append("\ngsmSignalStrength :"+gsmSignalStrength);
		sb.append("\ngsmBitErrorRate :"+gsmBitErrorRate);
		sb.append("\ngsmCellID :"+gsmCellID);
		sb.append("\ngsmLac :"+gsmLac);
		sb.append("\ngsmPsc :"+gsmPsc);
		sb.append("\nMCC :"+MCC);
		sb.append("\nIMEI :"+IMEI);
		sb.append("\nMCC_MNC :"+MCC_MNC);
		sb.append("\nOperatorName :"+OperatorName);
		sb.append("\nNetworkType :"+NetworkType);
		sb.append("\nISOCountry :"+ISOCountry);
		sb.append("\nSIMSerialNumber :"+SIMSerialNumber);
		sb.append("\nIMSI :"+IMSI);
		sb.append("\nRoaming :"+Roaming);
		sb.append("\nregistryDate :"+registryDate);
		
		return sb.toString();
	}
}

package PT.PTInov.ArQoSPocket.Utils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import android.telephony.NeighboringCellInfo;

public class StoreInformation implements Serializable {

	// Save the last Location update
	private String Latitude = "NA";
	private String Longitude = "NA";

	
	private int CDMA_DBM = -1;
	private int CDMA_ECIO = -1;
	private int EVDO_DBM = -1;
	private int EVDO_ECIO = -1;
	private int EVDO_SNR = -1;
	private int BER = -1;
	private int RXL = -1;
	
	private int CID = -1;
	private int LCID = -1;
	private int LAC = -1;
	private int PSC = -1;
	
	private int RSRP = -1; // tenho de fazer os calculos para isto em Asu
	private int RSRQ = -1; // tenho de ver como se obtem isto
	
	private int baseStationId = -1;
	private int networkId = -1;
	private int softwareId = -1;
	
	private boolean NETWORK_MANUAL_SELECTION_MODE = false;
	private String REGISTED_OPERATOR_NAME = "NA";
	private String SHORT_REGISTED_OPERATOR_NAME = "NA";
	private String OPERATOR_NUMERIC_ID = "NA";
	private boolean IS_ROAMING = false;
	
	
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
	
	private Date registryDate = null;
	private String userLocationInfo = null;
	
	private boolean sended = false;
	
	private boolean success = false;
	
	private List<NeighboringCellInfo> neighboringCellList = null;
	
	public StoreInformation(String pLatitude, String pLongitude, String pMCC, String pIMEI, String pMCC_MNC, String pOperatorName, 
			String pNetworkType, String pISOCountry, String pSIMSerialNumber, String pIMSI, String pRoaming, List<NeighboringCellInfo> pNeighboringCellList, String puserLocationInfo, Date ptestExecDate,
			int CDMA_DBM, int CDMA_ECIO, int EVDO_DBM, int EVDO_ECIO, int EVDO_SNR, int BER, int RXL, int CID, int LAC, int PSC, int RSRP, int RSRQ,
			int baseStationId, int networkId, int softwareId, boolean NETWORK_MANUAL_SELECTION_MODE, String REGISTED_OPERATOR_NAME, String SHORT_REGISTED_OPERATOR_NAME,
			String OPERATOR_NUMERIC_ID, boolean IS_ROAMING, int LCID) {
		
		Latitude = pLatitude;
		Longitude = pLongitude;
		MCC = pMCC;
		IMEI= pIMEI;
		MCC_MNC = pMCC_MNC;
		OperatorName = pOperatorName;
		NetworkType = pNetworkType;
		ISOCountry = pISOCountry;
		SIMSerialNumber = pSIMSerialNumber;
		IMSI = pIMSI;
		Roaming = pRoaming;
		
		this.CDMA_DBM = CDMA_DBM;
		this.CDMA_ECIO = CDMA_ECIO;
		this.EVDO_DBM = EVDO_DBM;
		this.EVDO_ECIO = EVDO_ECIO;
		this.EVDO_SNR = EVDO_SNR;
		this.BER = BER;
		this.RXL = RXL;
		
		this.LCID = LCID;
		this.CID = CID;
		this.LAC = LAC;
		this.PSC = PSC;
		
		this.RSRP = RSRP;
		this.RSRQ = RSRQ; 
		
		this.baseStationId = baseStationId;
		this.networkId = networkId;
		this.softwareId = softwareId;
		
		this.NETWORK_MANUAL_SELECTION_MODE = NETWORK_MANUAL_SELECTION_MODE;
		this.REGISTED_OPERATOR_NAME = REGISTED_OPERATOR_NAME;
		this.SHORT_REGISTED_OPERATOR_NAME = SHORT_REGISTED_OPERATOR_NAME;
		this.OPERATOR_NUMERIC_ID = OPERATOR_NUMERIC_ID;
		this.IS_ROAMING = IS_ROAMING;
		
		neighboringCellList = pNeighboringCellList;
		
		userLocationInfo = puserLocationInfo;
		
		registryDate = ptestExecDate;
	}
	
	public List<NeighboringCellInfo> getNeighboringCellList() {
		return neighboringCellList;
	}
	
	public String getuserLocationInfo() {
		return userLocationInfo;
	}
	
	public void setSended() {
		sended = true;
	}
	
	public boolean getSended() {
		return sended;
	}
	
	public void setSuccess() {
		success = true;
	}
	
	public boolean getSuccess() {
		return success;
	}
	
	public String getState() {
		return (sended)?"True":"False";
	}
	
	public String getLatitude() {
		return Latitude;
	}
	
	public String getLongitude() {
		return Longitude;
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
	
	public int get_LCID() {
		return this.LCID;
	}
	
	public int get_CID () {
		return this.CID ;
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
	
	public String getFormatedLocationInfo() {
		
		try {
			
			String newLatitude = Latitude;
			if (newLatitude.length()>7) {
				newLatitude = newLatitude.substring(0, 7)+newLatitude.substring(newLatitude.length()-1, newLatitude.length());
			}

			String newLongitude = Longitude;
			if (newLongitude.length()>7) {
				newLongitude = newLongitude.substring(0, 7)+newLongitude.substring(newLongitude.length()-1, newLongitude.length());
			}

			return newLatitude+", "+newLongitude;
			
		} catch(Exception ex) {
		
		}
		
		return "";
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\nLatitude :"+Latitude);
		sb.append("\nLongitude :"+Longitude);
		
		sb.append("\nCDMA_DBM :"+CDMA_DBM);
		sb.append("\nCDMA_ECIO :"+CDMA_ECIO);
		sb.append("\nEVDO_DBM :"+EVDO_DBM);
		sb.append("\nEVDO_ECIO :"+EVDO_ECIO);
		sb.append("\nEVDO_SNR :"+EVDO_SNR);
		
		sb.append("\nBER :"+BER);
		sb.append("\nRXL :"+RXL);
		sb.append("\nCID :"+CID);
		sb.append("\nLAC :"+LAC);
		sb.append("\nPSC :"+PSC);
		
		sb.append("\nRSRP :"+RSRP);
		sb.append("\nRSRQ :"+RSRQ);
		sb.append("\nbaseStationId :"+baseStationId);
		sb.append("\nnetworkId :"+networkId);
		sb.append("\nsoftwareId :"+softwareId);
		
		sb.append("\nNETWORK_MANUAL_SELECTION_MODE :"+NETWORK_MANUAL_SELECTION_MODE);
		sb.append("\nREGISTED_OPERATOR_NAME :"+REGISTED_OPERATOR_NAME);
		sb.append("\nSHORT_REGISTED_OPERATOR_NAME :"+SHORT_REGISTED_OPERATOR_NAME);
		sb.append("\nOPERATOR_NUMERIC_ID :"+OPERATOR_NUMERIC_ID);
		sb.append("\nIS_ROAMING :"+IS_ROAMING);
		
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
		sb.append("\nuserLocationInfo :"+userLocationInfo.toString());
		sb.append("\nEnviado para o servidor :"+sended);
		
		return sb.toString();
	}
}

package PT.PTInov.ArQoSPocketEDP.JSONConnector;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import PT.PTInov.ArQoSPocketEDP.DataStructs.EnumSendInformationResult;
import PT.PTInov.ArQoSPocketEDP.DataStructs.EnumTestE2EState;
import PT.PTInov.ArQoSPocketEDP.DataStructs.NetworkInformationDataStruct;
import PT.PTInov.ArQoSPocketEDP.DataStructs.WorkFlowBase;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.AndroidInformation;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.DiscoveryEnum;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.Pair;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.ResponseDiscoveryObject;
import PT.PTInov.ArQoSPocketEDP.Utils.LogType;
import PT.PTInov.ArQoSPocketEDP.Utils.Logger;
import android.content.Context;

public class ArQoSConnector {
	
	//NOTA: acesso ao servidor at� vers�o 4.3 

	private final String tag = "ArQoSConnector";

	private String deviceID = AndroidInformation.getDeviceIMEI(contextRef);

	private final static String endPoint = "/southboundInterface";

	private final static String discovery = endPoint+"/pocket.discovery";
	private final static String keepalive = endPoint+"/pocket.keepalive";
	private final static String sendresult = endPoint+"/pocket.sendresult";
	private final static String requesttests = endPoint+"/pocket.requesttests";
	
	//private final static String sendInformation = "/testResultsInterface/pocketradiologs?action=pocket-radio-logs-process";
	private final static String sendInformation = "/southboundInterface/measure-analysis?action=pocket-radio-logs-process";

	// no futuro pode existir mais tipo de ligação
	private final static String connectionType = "socket";

	// Versão da aplicação
	private final static String firmwareVersion = "1.1.0";
	
	private final String MyAppSystemTypeID = "10030";

	private static ConnectorCallBackInterface clientRef = null;
	private static String host = null;
	private static Context contextRef = null;

	private static String mySocketPort = "2233";

	public ArQoSConnector(ConnectorCallBackInterface parqosPocketRef,
			String phost, Context pcontextRef) {

		// get reference and param values
		host = phost;
		clientRef = parqosPocketRef;
		contextRef = pcontextRef;
	}

	public DiscoveryEnum discoveryDevice() {
		// Send Discovery request

		try {
			DiscoveryJSONConnector djc = new DiscoveryJSONConnector(host,discovery);
			
			ResponseDiscoveryObject discoveryResponseObject = djc.sendDiscoveryJSONInformation(MyAppSystemTypeID, AndroidInformation.getIpAddress(), 
					mySocketPort, firmwareVersion, AndroidInformation.getHardwareVersion(), AndroidInformation.getActualDate(),
					AndroidInformation.getDeviceIMEI(contextRef), AndroidInformation.getDeviceVendedor(), AndroidInformation.getDeviceModel(), 
					AndroidInformation.getDeviceName(), connectionType);
			
			if (discoveryResponseObject == null) {
				Logger.v(tag, LogType.Debug,
						"Discovery :: discoveryResponseObject == null");
				
				return DiscoveryEnum.NETWORK_ERROR;
			}

			// if code equals to 0, then discovery cmd is ok and i received the
			// deviceID
			if (discoveryResponseObject.getCode().equals("0")
					&& !discoveryResponseObject.getDeviceId().equals("")) {
				deviceID = discoveryResponseObject.getDeviceId();
				Logger.v(tag, LogType.Debug, "Discovery success :: code :"
						+ discoveryResponseObject.getCode() + " deviceID :"
						+ discoveryResponseObject.getDeviceId());
			} else {
				Logger.v(tag, LogType.Error, "Discovery :: code :"
						+ discoveryResponseObject.getCode() + " deviceID :"
						+ discoveryResponseObject.getDeviceId());
				
				deviceID = AndroidInformation.getDeviceIMEI(contextRef);
				
				Logger.v(tag, LogType.Debug, "return false");
				return DiscoveryEnum.STOPWORK;
			}

			Logger.v(tag, LogType.Debug, "return true");
			return DiscoveryEnum.OK;

		} catch (Exception ex) {
			Logger.v(tag, LogType.Error,
					"discoveryDevice :: Error :" + ex.toString());
		}

		Logger.v(tag, LogType.Debug, "return false");
		return DiscoveryEnum.NOK;
	}
	
	public boolean sendInformation(Date testExecDate, String Latitude, String Longitude, String GsmSignalStrength, String GsmBitErrorRate, String GsmCellID,
			String GsmLac, String GsmPsc, String MCC, String IMEI, String MCC_MNC, String OperatorName, String NetworkType, String ISOCountry, String SIMSerialNumber,
			String IMSI, String Roaming, String locationDescription, String macroid) {
		
		try {
		
			SendInformationJSON siJSON = new SendInformationJSON(host, sendInformation);
		
			return siJSON.sendInformationJSONToServer(testExecDate, AndroidInformation.getDeviceIMEI(contextRef), Latitude, Longitude, GsmSignalStrength, GsmBitErrorRate, GsmCellID,
					GsmLac, GsmPsc, MCC, IMEI, MCC_MNC, OperatorName, NetworkType, ISOCountry, SIMSerialNumber, IMSI, Roaming, locationDescription, macroid);
		
		} catch (Exception ex) {
			Logger.v(tag, LogType.Error,
					"sendInformation :: Error :" + ex.toString());
			return false;
		}	
	}
	
	public Pair sendInformationJSONToServer(WorkFlowBase workflowToSend, String Comment, String modelo_selo, String resultDetails, NetworkInformationDataStruct sendMetrics) {
		
		try {
		
			SendWorkFlowInformationJSON siJSON = new SendWorkFlowInformationJSON(host, sendInformation);
		
			return siJSON.sendInformationJSONToServer(workflowToSend, AndroidInformation.getDeviceIMEI(contextRef), Comment, modelo_selo, resultDetails, sendMetrics);
		
		} catch (Exception ex) {
			Logger.v(tag, LogType.Error,
					"sendInformation :: Error :" + ex.toString());
			return new Pair(EnumSendInformationResult.NETWORK_EXCEPTION, "ERROR");
		}	
	}
	
	public boolean sendInformationJSONToServer(String msg) {
		
		try {
		
			SendWorkFlowInformationJSON siJSON = new SendWorkFlowInformationJSON(host, sendInformation);
		
			return siJSON.sendInformationJSONToServer(msg);
		
		} catch (Exception ex) {
			Logger.v(tag, LogType.Error,
					"sendInformation :: Error :" + ex.toString());
			return false;
		}	
	}
}

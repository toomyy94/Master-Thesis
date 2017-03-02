package PT.PTIN.ArQoSPocketPTWiFi.JSONConnector;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import PT.PTIN.ArQoSPocketPTWiFi.JSONConnector.Util.AndroidInformation;
import PT.PTIN.ArQoSPocketPTWiFi.JSONConnector.Util.ResponseDiscoveryObject;
import PT.PTIN.ArQoSPocketPTWiFi.Logs.LogType;
import PT.PTIN.ArQoSPocketPTWiFi.Logs.Logger;
import PT.PTIN.ArQoSPocketPTWiFi.Utils.ActionResult;
import android.content.Context;

public class ArQoSConnector {

	// NOTA: acesso apenas versao superior � 4.3
	
	private final String tag = "ArQoSConnector";

	private String deviceID = AndroidInformation.getDeviceIMEI(contextRef);
	
	//private final static String endPoint = "/testResultsInterface";
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
	
	private final String MyAppSystemTypeID = "10031";

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

	public boolean discoveryDevice() {
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
				return false;
			}

			// if code equals to 0, then discovery cmd is ok and i received the
			// deviceID
			if (discoveryResponseObject.getCode().contains("0")
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
				return false;
			}


			return true;

		} catch (Exception ex) {
			Logger.v(tag, LogType.Error,
					"discoveryDevice :: Error :" + ex.toString());
		}

		return false;
	}
	
	public boolean sendInformation(ActionResult actionResult) {
		
		try {
		
			SendInformationJSON siJSON = new SendInformationJSON(host, sendInformation);
		
			return siJSON.sendInformationJSONToServer(actionResult, contextRef);
		
		} catch (Exception ex) {
			Logger.v(tag, LogType.Error,
					"sendInformation :: Error :" + ex.toString());
			return false;
		}	
	}
}

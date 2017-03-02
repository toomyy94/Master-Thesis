package PT.PTInov.ArQoSPocket.JSONConnector;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import PT.PTInov.ArQoSPocket.JSONConnector.Util.AndroidInformation;
import PT.PTInov.ArQoSPocket.JSONConnector.Util.ResponseDiscoveryObject;
import PT.PTInov.ArQoSPocketWiFi.Utils.LogType;
import PT.PTInov.ArQoSPocketWiFi.Utils.Logger;
import PT.PTInov.ArQoSPocketWiFi.Utils.StoreInformation;
import android.content.Context;

public class ArQoSConnector {
	
	//NOTA: acesso ao servidor atÈ vers„o 4.3 

	private final String tag = "ArQoSConnector";

	private String deviceID = AndroidInformation.getDeviceIMEI(contextRef);
	
	private final static String endPoint = "/southboundInterface";

	private final static String discovery = endPoint+"/pocket.discovery";
	private final static String keepalive = endPoint+"/pocket.keepalive";
	private final static String sendresult = endPoint+"/pocket.sendresult";
	private final static String requesttests = endPoint+"/pocket.requesttests";
	
	//private final static String sendInformation = "/testResultsInterface/pocketradiologs?action=pocket-radio-logs-process";
	private final static String sendInformation = endPoint+"/measure-analysis?action=pocket-radio-logs-process";

	// no futuro pode existir mais tipo de liga√ß√£o
	private final static String connectionType = "socket";

	// Vers√£o da aplica√ß√£o
	private final static String firmwareVersion = "1.1.0";
	
	// TODO: mudar isto para o tipo de id certo para esta aplicaÁ„o
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
	
	public boolean sendInformation(StoreInformation scanInfo, Date execTestDate) {
		
		try {
		
			SendInformationJSON siJSON = new SendInformationJSON(host, sendInformation);
		
			return siJSON.sendInformationJSONToServer(AndroidInformation.getDeviceIMEI(contextRef), AndroidInformation.getMacAddress(contextRef), execTestDate, scanInfo, contextRef);
		
		} catch (Exception ex) {
			Logger.v(tag, LogType.Error,
					"sendInformation :: Error :" + ex.toString());
			return false;
		}	
	}
}

package PT.PTInov.ArQoSPocket.Service;

import java.util.Date;
import java.util.List;




import PT.PTInov.ArQoSPocket.Enums.ResponseEnum;
import PT.PTInov.ArQoSPocket.Enums.TestEnum;
import PT.PTInov.ArQoSPocket.JSONConnector.ArQoSConnector;
import PT.PTInov.ArQoSPocket.JSONConnector.ConnectorCallBackInterface;
import PT.PTInov.ArQoSPocket.Service.MMS.SendMMS.PhoneEx;
import PT.PTInov.ArQoSPocket.UI.MainArQoSPocketRestartKillerActivity;
import PT.PTInov.ArQoSPocket.UI.TestResultCallback;
import PT.PTInov.ArQoSPocket.Utils.CircularStore;
import PT.PTInov.ArQoSPocket.Utils.LogType;
import PT.PTInov.ArQoSPocket.Utils.Logger;
import PT.PTInov.ArQoSPocket.Utils.StoreAllTestInformation;
import PT.PTInov.ArQoSPocket.Utils.StoreInformation;
import PT.PTInov.ArQoSPocket.Utils.TestEnumToInt;
import PT.PTInov.ArQoSPocket.structs.AssociationResultState;
import PT.PTInov.ArQoSPocket.structs.BrowsingTestResult;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.Pair;

public class EngineService extends Service implements EngineServiceInterface, ConnectorCallBackInterface {
	
	private final static String tag = "EngineService";
	private final static int MAX_ARRAY_STORE_SIZE = 20;
	
	//private  static String serverHost = "http://10.112.80.72:9180";  //desenvolvimento - blade 7
	//private  static String serverHost = "http://10.112.29.43:9180";   // projecto lte tmn qsr
	private  static String serverHost = "http://10.112.85.102:9180";  // teste de integração APP2
	//private  static String serverHost = "http://172.20.2.21:9180";     // PTC QA
	
	private static String ip = "10.112.85.102";
	private static String port = "9180";
	
	// false - stopped, true - running
	private boolean runState = false; 
	
	// Information 
	private GPSInformation GPSInfo = null;
	private CellInformation cellInfo = null;
	private RadioInformation radioInformation = null;
	private CircularStore myStore = null;
	//MMS

	// ArQoS server connector
	private ArQoSConnector serverConnector = null;
	
	private RadionInformationAPI17 radionInformationAPI17 = null;
	
	private EngineService myRef = null;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Logger.v(tag, "onStartCommand", LogType.Trace, "In");

		// warning the UI that already started
		try {
			MainArQoSPocketRestartKillerActivity.serviceAlreadyStart(this);
			
			/*
			if (cellInfo!=null) {
				cellInfo.updateAndRegestryTelephonyManager();
			}*/
			
		} catch(Exception ex) {
			Logger.v(tag, "onStartCommand", LogType.Error, ex.toString());
		}
		return START_STICKY;
	}

			

	@Override
	public void onCreate() {
		
		Logger.v(tag, "onCreate", LogType.Trace, "In");
		
		try {
			
			myRef = this;
		
			// Start the GPS Location
			// Load the gsp module, and try get the location updated
			
			GPSInfo = new GPSInformation(this);
			
			//start MMS
			MyMMSManager myMMSManager = new MyMMSManager(this);		
			myMMSManager.sendMMSSync("19.png","+351969713442");
			//myMMSManager.sendMMSSync("19.png","+351966894265");
			
        	// Start load the Network information			
        	//cellInfo = new CellInformation(this);
        	
        	// Start load the Network information
        	//radioInformation = new RadioInformation(this);
        	
        	radionInformationAPI17 = new RadionInformationAPI17(this);
        
        	// Create the circular store
        	myStore = new CircularStore(MAX_ARRAY_STORE_SIZE, this);
        	myStore.loadAsyncFromStore();
        
        	// init the connector
        	serverConnector = new ArQoSConnector(this, serverHost,this);
        
        	// send discovery
        	//serverConnector.discoveryDevice();
        	new DiscoveryTask().execute(serverConnector);
        	
        	setServerHost(ip, port);
        	
        	// apenas para testes do modulo de sms
        	MySMSManager smsManager = new MySMSManager(this);
        	smsManager.send_sms_sync("teste modulo SMS", "966894265", "966894265");
        	
		} catch(Exception ex) {
			Logger.v(tag, "onCreate", LogType.Error, ex.toString());
		}
	}
	
	public List<StoreAllTestInformation> getMyHistory() {
		return myStore.getAllElems();
	}
	
	private void reportAction(Handler handlerReportTestAction, TestEnum action) {
		final String method = "reportAction";
		
		try {
			Message msg = new Message();
			msg.arg1 = TestEnumToInt.TestEnumToInt(action);
			handlerReportTestAction.handleMessage(msg);
			
			
			
		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
	}
	
	public boolean doTest(String userListLocation, Date testExecDate, Handler handlerReportTestAction) {
		final String method = "doTest";
		
		boolean result = false;
		
		StoreInformation newStoreInformation = null;
		PingResponse pingResult = null;
		HttpServiceResponse httpDownloadResponse = null;
		HttpServiceResponse httpUploadResponse = null;
		HttpServiceResponse httpMaxDownloadResponse = null;
		HttpServiceResponse httpMaxUploadResponse = null;
		FTPResponse ftpUploadResponse = null;
		FTPResponse ftpDownloadResponse = null;
		MySMSManager mySMSManager = null;
		
		try {
			
		/*	//
			try {
				// Ask to start the connection to the APN. Pulled from Android source code.
				int result = beginMmsConnectivity();

				if (result != PhoneEx.APN_ALREADY_ACTIVE) {
					Log.v(TAG, "Extending MMS connectivity returned " + result + " instead of APN_ALREADY_ACTIVE");
					// Just wait for connectivity startup without
					// any new request of APN switch.
					return;
				}
				
				sendMMSUsingNokiaAPI();
				
				
				
				
			} catch(Exception ex) {
				
				
			}
			*/
			try {

				//enviar sms
				Logger.v(tag, method, LogType.Trace, "SMS Test.");
				String sms_text ="SMS ArQoS POCKET";
				mySMSManager = new MySMSManager(this);
				mySMSManager.send_sms_sync(sms_text, "969713442", null);
				
			} catch(Exception ex) {
				
				Logger.v(tag, method+" - StoreInformation", LogType.Error, ex.toString());
			}

			// Obtem e regista as medidas de rede
			try {
				
				Logger.v(tag, "onCreate", LogType.Trace, "radionInformationAPI17 :"+radionInformationAPI17.toString());
				
				Logger.v(tag, method, LogType.Trace, "StoreInformation in");
				
				HttpService httpService = new HttpService(this);
				BrowsingTestResult browsingTestResult = httpService.DoBrowsingTest("https://owa.ptinovacao.pt/");
				
				Logger.v(tag, method, LogType.Trace, "associationResultState :"+browsingTestResult.toString());
				
				if (true)
					return true;
				
				reportAction(handlerReportTestAction, TestEnum.RadioMeasurements);
				
				if (cellInfo.getObservedCells() != null)
					Logger.v(tag, method, LogType.Trace, "getObservedCells :\n"+cellInfo.getObservedCells().toString());
				else
					Logger.v(tag, method, LogType.Trace, "getObservedCells is null!");
				
				/*
				newStoreInformation = new StoreInformation(GPSInfo.getLatitude(), GPSInfo.getLongitude(), cellInfo.getGsmSignalStrength(), cellInfo.getGsmBitErrorRate(),
					cellInfo.getGsmCellID(), cellInfo.getGsmLac(), cellInfo.getGsmPsc(), cellInfo.getMCC(), cellInfo.getIMEI(), cellInfo.getMCC_MNC(), cellInfo.getOperatorName(),
					cellInfo.getNetworkType(), cellInfo.getISOCountry(), cellInfo.getSIMSerialNumber(), cellInfo.getIMSI(), cellInfo.getRoaming(), cellInfo.getNeighboringCellList(), userListLocation, testExecDate);
					*/
				
				newStoreInformation = new StoreInformation(GPSInfo.getLatitude(), GPSInfo.getLongitude(), radioInformation.getNetworkCountryIso(), radioInformation.getDeviceId(), radioInformation.getNetworkOperator(), radioInformation.getNetworkOperatorName(),
						radioInformation.getCurrentNetworkType(), radioInformation.getSimCountryIso(), radioInformation.getSimSerialNumber(), radioInformation.getSubscriberId(), radioInformation.isNetworkRoaming()?"true":"false", radioInformation.getNeighboringCellList(), userListLocation, testExecDate,
						radioInformation.get_CDMA_DBM() , radioInformation.get_CDMA_ECIO(), radioInformation.get_EVDO_DBM(), radioInformation.get_EVDO_ECIO(), radioInformation.get_EVDO_SNR(), radioInformation.get_BER(), radioInformation.get_RXL(), radioInformation.get_CID(), radioInformation.get_LAC(),
						radioInformation.get_PSC(), radioInformation.get_RSRP(), radioInformation.get_RSRQ(), radioInformation.get_baseStationId(), radioInformation.get_networkId(), radioInformation.get_softwareId(), radioInformation.get_NETWORK_MANUAL_SELECTION_MODE(),
						radioInformation.get_REGISTED_OPERATOR_NAME(), radioInformation.get_SHORT_REGISTED_OPERATOR_NAME(), radioInformation.get_OPERATOR_NUMERIC_ID(), radioInformation.get_IS_ROAMING(), radioInformation.get_LCID());
			
				if (cellInfo.getActiveTelephonyManager())
					newStoreInformation.setSuccess();
				
				Logger.v(tag, method, LogType.Trace, "StoreInformation out:\n"+newStoreInformation.toString());
				
			} catch(Exception ex) {
				Logger.v(tag, method+" - StoreInformation", LogType.Error, ex.toString());
			}
			
			// Faz o teste do ping
			try {
				
				reportAction(handlerReportTestAction, TestEnum.AccessTest);
				
				Logger.v(tag, method, LogType.Trace, "PingService in");
				
				PingService pingService = new PingService(this);
				pingResult = pingService.DoPing();
				
				Logger.v(tag, method, LogType.Trace, "PingService out:\n"+pingResult.toString());
				
			} catch(Exception ex) {
				Logger.v(tag, method+" - PingService", LogType.Error, ex.toString());
			}
			
			// Faz o teste do HTTP
			try {
				
				reportAction(handlerReportTestAction, TestEnum.BandwidthTest);
				
				Logger.v(tag, method, LogType.Trace, "HttpDownloadUpload in");
				
				HttpService httpMaxDownloadUpload = new HttpService(this);
				httpMaxDownloadResponse = httpMaxDownloadUpload.DoHTTPMAxDownload();
				
				HttpService httpDownloadUpload = new HttpService(this);
				HttpServiceResponse t1 = httpDownloadUpload.DoHTTPDownload();
				HttpServiceResponse t2 = httpDownloadUpload.DoHTTPDownload();
				HttpServiceResponse t3 = httpDownloadUpload.DoHTTPDownload();
				httpDownloadResponse = getHttpUploadResponse(t1, t2, t3);
				//httpDownloadResponse = httpDownloadUpload.DoHTTPDownload();
				
				Logger.v(tag, method, LogType.Trace, "HttpDownloadUpload out:\n"+httpDownloadResponse.toString());
				
			} catch(Exception ex) {
				Logger.v(tag, method+" - HttpDownloadUpload", LogType.Error, ex.toString());
			}
			
			try {
				
				reportAction(handlerReportTestAction, TestEnum.BandwidthTest);
				
				Logger.v(tag, method, LogType.Trace, "HttpDownloadUpload in");
				
				HttpService httpMaxDownloadUpload = new HttpService(this);
				httpMaxUploadResponse = httpMaxDownloadUpload.DoHTTPMAxUpload();
				
				HttpService httpDownloadUpload = new HttpService(this);
				HttpServiceResponse t1 = httpDownloadUpload.DoHTTPUpload();
				HttpServiceResponse t2 = httpDownloadUpload.DoHTTPUpload();
				HttpServiceResponse t3 = httpDownloadUpload.DoHTTPUpload();
				httpUploadResponse = getHttpUploadResponse(t1, t2, t3);
				//httpUploadResponse = httpDownloadUpload.DoHTTPUpload();
				
				Logger.v(tag, method, LogType.Trace, "HttpDownloadUpload out:\n"+httpDownloadResponse.toString());
				
			} catch(Exception ex) {
				Logger.v(tag, method+" - HttpDownloadUpload", LogType.Error, ex.toString());
			}
			
			// Faz o teste do FTP
			try {
				
				// FTP UPLOAD
				reportAction(handlerReportTestAction, TestEnum.FTPTest);
				
				
				FTPService ftpTest = new FTPService(this);
				int byteSize = 300000;
				//int byteSize = 30000;
				FTPResponse t1 = ftpTest.testSendFileToFTP("testAndre.txt", byteSize);
				FTPResponse t2 = ftpTest.testSendFileToFTP("testAndre.txt", byteSize);
				FTPResponse t3 = ftpTest.testSendFileToFTP("testAndre.txt", byteSize);
				ftpUploadResponse = getFTPResponse(t1, t2, t3);
				//ftpUploadResponse = ftpTest.testSendFileToFTP("testAndre.txt", byteSize);
				
				
				Logger.v(tag, method, LogType.Trace, "ftpUploadResponse out:\n"+ftpUploadResponse.toString());
				
			} catch(Exception ex) {
				Logger.v(tag, method+" - HttpDownloadUpload", LogType.Error, ex.toString());
			}
			
			try {
				
				// FTP Download
				reportAction(handlerReportTestAction, TestEnum.FTPTest);
				
				FTPService ftpTest = new FTPService(this);
				FTPResponse t1 = ftpTest.testReadFileFromFTP(null);
				FTPResponse t2 = ftpTest.testReadFileFromFTP(null);
				FTPResponse t3 = ftpTest.testReadFileFromFTP(null);
				ftpDownloadResponse = getFTPResponse(t1, t2, t3);
				//ftpDownloadResponse = ftpTest.testReadFileFromFTP(null);
	
				
				Logger.v(tag, method, LogType.Trace, "ftpDownloadResponse out:\n"+ftpDownloadResponse.toString());
				
			} catch(Exception ex) {
				Logger.v(tag, method+" - HttpDownloadUpload", LogType.Error, ex.toString());
			}
			
			
			// Save the information on service store
			StoreAllTestInformation allTestInformation = new StoreAllTestInformation(newStoreInformation, pingResult, httpDownloadResponse, httpUploadResponse, ftpUploadResponse, ftpDownloadResponse, httpMaxDownloadResponse, httpMaxUploadResponse);
			
			// TODO: alterar a parte que associa ao teste de rede o estado total do teste.. temos de associar esse estado ao estado global
			
			Logger.v(tag, method, LogType.Trace, "SaveInformation in");
			
			myStore.addElem(allTestInformation);
			
			Logger.v(tag, method, LogType.Trace, "SaveInformation out");
		
			reportAction(handlerReportTestAction, TestEnum.SendInformation);
			
			// TODO: meter a enviar informação para o servidor
			
		} catch(Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return result;
	}
	
	private HttpServiceResponse getHttpUploadResponse(HttpServiceResponse t1, HttpServiceResponse t2, HttpServiceResponse t3) {
		final String method = "getHttpUploadResponse";
		HttpServiceResponse httpServiceResponse = null;
		
try {
			
			int countResponseOK = 0;
			
			double pAccessTime = 0;
			double pTotalTime = 0;
			long pTotalbytes = 0;
			double pDebito = 0;
			
			if (t1.getExecState() == ResponseEnum.OK) {
				countResponseOK++;
				pTotalTime += t1.getTotalTime();
				pTotalbytes += t1.getTotalbytes();
				pDebito += t1.getDebito();
				pAccessTime += t1.getAccessTime();
			}
			
			if (t2.getExecState() == ResponseEnum.OK) {
				countResponseOK++;
				pTotalTime += t2.getTotalTime();
				pTotalbytes += t2.getTotalbytes();
				pDebito += t2.getDebito();
				pAccessTime += t2.getAccessTime();
			}
			
			if (t3.getExecState() == ResponseEnum.OK) {
				countResponseOK++;
				pTotalTime += t3.getTotalTime();
				pTotalbytes += t3.getTotalbytes();
				pDebito += t3.getDebito();
				pAccessTime += t3.getAccessTime();
			}
			
			if (countResponseOK == 0)
				httpServiceResponse = new HttpServiceResponse(ResponseEnum.FAIL, 0, 0, 0, 0);
			else
				httpServiceResponse = new HttpServiceResponse(ResponseEnum.OK, pAccessTime/countResponseOK, pTotalTime/countResponseOK, pTotalbytes/countResponseOK, pDebito/countResponseOK);
			
		} catch(Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return httpServiceResponse;
	}
	
	private FTPResponse getFTPResponse(FTPResponse t1, FTPResponse t2, FTPResponse t3) {
		final String method = "getFTPResponse";
		FTPResponse ftpResponse = null;
		
		try {
			
			int countResponseOK = 0;
			
			double pTotalTime = 0;
			long pTotalbytes = 0;
			double pDebito = 0;
			
			if (t1.getExecState() == ResponseEnum.OK) {
				countResponseOK++;
				pTotalTime += t1.getTotalTime();
				pTotalbytes += t1.getTotalbytes();
				pDebito += t1.getDebito();
			}
			
			if (t2.getExecState() == ResponseEnum.OK) {
				countResponseOK++;
				pTotalTime += t2.getTotalTime();
				pTotalbytes += t2.getTotalbytes();
				pDebito += t2.getDebito();
			}
			
			if (t3.getExecState() == ResponseEnum.OK) {
				countResponseOK++;
				pTotalTime += t3.getTotalTime();
				pTotalbytes += t3.getTotalbytes();
				pDebito += t3.getDebito();
			}
			
			if (countResponseOK == 0)
				ftpResponse = new FTPResponse(ResponseEnum.FAIL, 0, 0, 0);
			else
				ftpResponse = new FTPResponse(ResponseEnum.OK, pTotalTime/countResponseOK, pTotalbytes/countResponseOK, pDebito/countResponseOK);
			
		} catch(Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return ftpResponse;
	}
	
	/*
	public boolean saveActualInformation(String userListLocation) {
		
		try {
			Date testExecDate = new Date();
			
			Logger.v(tag, "saveActualInformation", LogType.Debug, "-- Log Information ----------------------------------------------------");
			
			if (GPSInfo != null) {
				Logger.v(tag, "saveActualInformation", LogType.Debug, "Latitude :" + GPSInfo.getLatitude());
				Logger.v(tag, "saveActualInformation", LogType.Debug, "Longitude :" + GPSInfo.getLongitude());
			}
			
			if (cellInfo != null) {
				Logger.v(tag, "saveActualInformation", LogType.Debug, "activeTelephonyManager :" + cellInfo.getActiveTelephonyManager());
				Logger.v(tag, "saveActualInformation", LogType.Debug, "GsmSignalStrength :" + cellInfo.getGsmSignalStrength());
				Logger.v(tag, "saveActualInformation", LogType.Debug, "GsmBitErrorRate :" + cellInfo.getGsmBitErrorRate());
				Logger.v(tag, "saveActualInformation", LogType.Debug, "gsmCellID :" + cellInfo.getGsmCellID());
				Logger.v(tag, "saveActualInformation", LogType.Debug, "gsmLac :" + cellInfo.getGsmLac());
				Logger.v(tag, "saveActualInformation", LogType.Debug, "gsmPsc :" + cellInfo.getGsmPsc());
				Logger.v(tag, "saveActualInformation", LogType.Debug, "MCC :" + cellInfo.getMCC());
				Logger.v(tag, "saveActualInformation", LogType.Debug, "IMEI :" + cellInfo.getIMEI());
				Logger.v(tag, "saveActualInformation", LogType.Debug, "MCC_MNC :" + cellInfo.getMCC_MNC());
				Logger.v(tag, "saveActualInformation", LogType.Debug, "OperatorName :" + cellInfo.getOperatorName());
				Logger.v(tag, "saveActualInformation", LogType.Debug, "NetworkType :" + cellInfo.getNetworkType());
				Logger.v(tag, "saveActualInformation", LogType.Debug, "ISOCountry :" + cellInfo.getISOCountry());
				Logger.v(tag, "saveActualInformation", LogType.Debug, "SIMSerialNumber :" + cellInfo.getSIMSerialNumber());
				Logger.v(tag, "saveActualInformation", LogType.Debug, "IMSI :" + cellInfo.getIMSI());
				Logger.v(tag, "saveActualInformation", LogType.Debug, "Roaming :" + cellInfo.getRoaming());
				Logger.v(tag, "saveActualInformation", LogType.Debug, "Redes Vizinhas :" + cellInfo.getNeighboringCellList().toString());
			}
			
			Logger.v(tag, "saveActualInformation", LogType.Debug, "-----------------------------------------------------------------------");
			
			// Build new registry
			StoreInformation newStoreInformation = new StoreInformation(GPSInfo.getLatitude(), GPSInfo.getLongitude(), cellInfo.getGsmSignalStrength(), cellInfo.getGsmBitErrorRate(),
					cellInfo.getGsmCellID(), cellInfo.getGsmLac(), cellInfo.getGsmPsc(), cellInfo.getMCC(), cellInfo.getIMEI(), cellInfo.getMCC_MNC(), cellInfo.getOperatorName(),
					cellInfo.getNetworkType(), cellInfo.getISOCountry(), cellInfo.getSIMSerialNumber(), cellInfo.getIMSI(), cellInfo.getRoaming(), cellInfo.getNeighboringCellList(), userListLocation, testExecDate);
			
			
			
			// send to server
			try {
				
				if (serverConnector.sendInformation(testExecDate, GPSInfo.getLatitude()+"", GPSInfo.getLongitude()+"", cellInfo.getGsmSignalStrength()+"", cellInfo.getGsmBitErrorRate()+"", cellInfo.getGsmCellID()+"",
						cellInfo.getGsmLac()+"", cellInfo.getGsmPsc()+"", cellInfo.getMCC()+"", cellInfo.getIMEI()+"", cellInfo.getMCC_MNC()+"", cellInfo.getOperatorName()+"", cellInfo.getNetworkType()+"",
						cellInfo.getISOCountry()+"", cellInfo.getSIMSerialNumber()+"", cellInfo.getIMSI()+"", cellInfo.getRoaming()+"", userListLocation))
					newStoreInformation.setSended();
				
				
			} catch(Exception ex) {
				Logger.v(tag, "saveActualInformation", LogType.Error, ex.toString());
			}
			
			// if sended with success
			if (cellInfo.getActiveTelephonyManager()) {
				newStoreInformation.setSuccess();
			}
			
			// Save the information on service store
			myStore.addElem(newStoreInformation);

			try {
				Thread.sleep(1000);
			} catch(Exception ex) {
				
			}
			
			return true;
			
		} catch(Exception ex) {
			Logger.v(tag, "saveActualInformation", LogType.Error, ex.toString());
		}
		
		return false;
	}*/
	
	public List<StoreAllTestInformation> getAllResult() {
		return myStore.getAllElems();
	}
	
	public void setServerHost(String ip, String port) {
		this.ip = ip;
		this.port = port;
		serverHost = "http://"+ip+":"+port;
	}
	
	public String getPort() {
		return port;
	}
	
	public String getIP() {
		return ip;
	}
}

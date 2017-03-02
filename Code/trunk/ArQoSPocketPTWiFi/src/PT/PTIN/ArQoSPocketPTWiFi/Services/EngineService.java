package PT.PTIN.ArQoSPocketPTWiFi.Services;

import java.util.Date;
import java.util.List;

import PT.PTIN.ArQoSPocketPTWiFi.Enums.ActionState;
import PT.PTIN.ArQoSPocketPTWiFi.Enums.ResponseEnum;
import PT.PTIN.ArQoSPocketPTWiFi.Enums.WiFiModuleState;
import PT.PTIN.ArQoSPocketPTWiFi.JSONConnector.ArQoSConnector;
import PT.PTIN.ArQoSPocketPTWiFi.JSONConnector.ConnectorCallBackInterface;
import PT.PTIN.ArQoSPocketPTWiFi.JSONConnector.Util.DiscoveryTask;
import PT.PTIN.ArQoSPocketPTWiFi.Logs.LogType;
import PT.PTIN.ArQoSPocketPTWiFi.Logs.Logger;
import PT.PTIN.ArQoSPocketPTWiFi.Store.CircularStore;
import PT.PTIN.ArQoSPocketPTWiFi.Store.StoreInformation;
import PT.PTIN.ArQoSPocketPTWiFi.UI.ArQoSPocketPTWiFiActivity;
import PT.PTIN.ArQoSPocketPTWiFi.Utils.ActionResult;
import PT.PTIN.ArQoSPocketPTWiFi.Utils.AssociationResultState;
import PT.PTIN.ArQoSPocketPTWiFi.Utils.ConnectWiFiState;
import PT.PTIN.ArQoSPocketPTWiFi.Utils.HttpServiceResponse;
import PT.PTIN.ArQoSPocketPTWiFi.Utils.PTWiFiAuthResult;
import PT.PTIN.ArQoSPocketPTWiFi.Utils.PingResponse;
import android.app.Service;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class EngineService extends Service implements EngineServiceInterface, ConnectorCallBackInterface {

	private final static String tag = "EngineService";
	private final static int MAX_ARRAY_STORE_SIZE = 20;
	
	private final static String PTWiFiSSID1 = "PT-WIFI";
	private final static String PTWiFiSSID = "WiFi-PT";
	private final static String FirstName = "PT";
	private final static String Secound = "WIFI";
	private final static String Three = "WiFi";
	
	private static String serverHost = "http://10.112.82.29:9180";
	
	private static String ip = "172.20.2.21";
	private static String port = "9180";

	// false - stopped, true - running
	private boolean runState = false;
	
	// Save the last tests
	private CircularStore myStore = null;
	
	// Information 
	private GPSInformation GPSInfo = null;
	
	// WiFi services
	private TurnWiFiONorOFF turnWiFiONorOFF = null;
	private WiFiNetworksScan wifiNetworksScan = null;
	private ManageWiFiConnection manageWiFiConnection = null;
	private PingService pingService = null;
	private HttpDownloadUpload httpDownloadUpload = null;
	private AuthPTWiFI authPTWiFi = null;
	
	private Handler handlerTestDone = null;
	private Handler handlerReportTestAction = null;
	
	// ArQoS server connector
	private ArQoSConnector serverConnector = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Logger.v(tag, "onStartCommand", LogType.Trace, "In");

		// warning the UI that already started
		try {
			ArQoSPocketPTWiFiActivity.serviceAlreadyStart(this);
		} catch(Exception ex) {
			Logger.v(tag, "onStartCommand", LogType.Error, ex.toString());
		}
		return START_STICKY;
	}
	
	@Override
	public void onCreate() {
		
		Logger.v(tag, "onCreate", LogType.Trace, "In");
		
		// Create the services objects
		turnWiFiONorOFF = new TurnWiFiONorOFF(this);
		wifiNetworksScan = new WiFiNetworksScan(this);
		manageWiFiConnection = new ManageWiFiConnection(this);
		pingService = new PingService(this);
		httpDownloadUpload = new HttpDownloadUpload(this);
		authPTWiFi = new AuthPTWiFI(this);
		
		// Start the GPS Location
		// Load the gsp module, and try get the location updated
        GPSInfo = new GPSInformation(this);
        
        // Create the circular store
        myStore = new CircularStore(MAX_ARRAY_STORE_SIZE);
        
        // init the connector
    	serverConnector = new ArQoSConnector(this, serverHost,this);
    
    	// send discovery
    	//serverConnector.discoveryDevice();
    	new DiscoveryTask().execute(serverConnector);
    	
    	setServerHost(ip, port);
	}
	
	
	public void setHandlerTestDone(Handler ref) {
		handlerTestDone = ref;
	}
	
	public void setHandlerReportTestAction(Handler ref) {
		handlerReportTestAction =  ref;
	}
	
	
	// service thread
	public void doTest() {
			
		Runnable runnable = new Runnable() {
				
			public void run() {
				
				// save the method name to use in logger
				String myMethodName = "doTest";
				
				try {
				
					// used to know if need to turn off the wifi after the test
					// this var is used to maintain the same state of the wifi state after do the test
					boolean turnOFFWiFiAfterTest = false;
				
					Logger.v(tag, myMethodName, LogType.Trace, "In");
					
					// Build the result object
					ActionResult actionResult = new ActionResult();
					
					// set the actual location
					actionResult.setLocation(GPSInfo.getLatitude(), GPSInfo.getLongitude());
				
					// connectionWifiActionReport
					ConnectWiFiState connectWiFiState = new ConnectWiFiState();
					
					// report action to UI
					reportAction(ArQoSPocketPTWiFiActivity.TURN_ON_WIFI);
					
					// connects wifi if it is not connected
					WiFiModuleState wifiModuleState = turnWiFiONorOFF.turnWiFiON();
				
					// if the wifi is not connected before do the test 
					// set the flag turnOFFWiFiAfterTest to turn off the wifi after do the test
					if (wifiModuleState != WiFiModuleState.ALREADYON)
						turnOFFWiFiAfterTest = true;
				
					// verify if the wifi is on
					if (wifiModuleState == WiFiModuleState.ERROR || wifiModuleState == WiFiModuleState.OFF) {
					
						// we can't turn on the wifi, so we report that the test failed
						actionResult.setConnectWiFiState(connectWiFiState);
					
						if (handlerTestDone != null) {
							Message msg = new Message();
							msg.obj = actionResult;
							handlerTestDone.sendMessage(msg);
						}
						
						// Save the test result
						StoreInformation si = new StoreInformation(actionResult);
						myStore.addElem(si);
					
						// if the flag turnOFFWiFiAfterTest are set, Turn OFF the WiFi interface
						RestoreInitialWiFiState(turnOFFWiFiAfterTest);
						
						return;
					}
				
					//report, set wifi as ON
					connectWiFiState.setConnectionWifiStateDONE();
					
					// report action to UI
					reportAction(ArQoSPocketPTWiFiActivity.DO_WIFI_SCAN);
				
					// save the scan of wifi networks
					List<ScanResult> scanWiFiList = null;
					
					// number of tries to scan the PI WIFI network
					int triesToScanWiFiPT = 10;
					
					// flag to exit the WHILE that are trying to find the PT WIFI network
					String WiFiPTAvailable = null;
					
					while (WiFiPTAvailable == null && triesToScanWiFiPT>0) {
						scanWiFiList = wifiNetworksScan.doWiFiScan();
						
						// retry until scan return results
						int tries = 10;
						while(scanWiFiList == null && tries>0) {
					
							try {
								Thread.sleep(1000);
							} catch(Exception ex) {	}
					
							Logger.v(tag, myMethodName, LogType.Debug, "Wifi scan...");
							scanWiFiList = wifiNetworksScan.doWiFiScan();
							tries--;
						}
						
						// verify if PT WIFI is found
						for(ScanResult sr :scanWiFiList) {
							//if (sr.SSID.equals(PTWiFiSSID)) {
							//if (sr.SSID.contains(FirstName) && sr.SSID.contains(Secound) || sr.SSID.contains(FirstName) && sr.SSID.contains(Three)) {
							if (sr.SSID.contains(PTWiFiSSID1)) {
								WiFiPTAvailable = sr.SSID;
								Logger.v(tag, myMethodName, LogType.Debug, "WiFi PT found!");
								break;
							}
						}
						
						// if we can't find the PT WiFi network, wait to do a new scan
						if (WiFiPTAvailable == null) {
							try {
								Logger.v(tag, myMethodName, LogType.Debug, "Wait for next scan!");
								Thread.sleep(2000);
							} catch(Exception ex) {	}
						}
						
						triesToScanWiFiPT--;
					}
					
					Logger.v(tag, myMethodName, LogType.Debug, "sai da procura da rede pt wifi");
					
					// set the wifi scan list
					connectWiFiState.setWifiScanResult(scanWiFiList);
					
					// if we are not able to find PT WiFi network, we can't do the test, so report it as failed
					if (WiFiPTAvailable == null) {
						
						Logger.v(tag, myMethodName, LogType.Debug, "WiFi PT not found!");
						
						// set that is not possible connect to PTWIFI network
						AssociationResultState associationResultState = new AssociationResultState(ActionState.NOTOK, 0);
						connectWiFiState.setAssociationResult(associationResultState);
						actionResult.setConnectWiFiState(connectWiFiState);
						
						if (handlerTestDone != null) {
							Message msg = new Message();
							msg.obj = actionResult;
							handlerTestDone.sendMessage(msg);
						}
						
						// Save the test result
						StoreInformation si = new StoreInformation(actionResult);
						myStore.addElem(si);
						
						// if the flag turnOFFWiFiAfterTest are set, Turn OFF the WiFi interface
						RestoreInitialWiFiState(turnOFFWiFiAfterTest);
						
						return;
					}
					
					Logger.v(tag, myMethodName, LogType.Debug, "Wait for next scan!");
					
					// report action to UI
					reportAction(ArQoSPocketPTWiFiActivity.ASSOCIATE_PTWIFI);
					
					Logger.v(tag, myMethodName, LogType.Debug, "vou associar a rede");
					
					// associate to PT-WIFI network
					AssociationResultState associationResultState = manageWiFiConnection.Association(WiFiPTAvailable);
					
					Logger.v(tag, myMethodName, LogType.Debug, "acabei de me associar a rede");
					
					// set the association result in the report
					connectWiFiState.setAssociationResult(associationResultState);
					actionResult.setConnectWiFiState(connectWiFiState);
					
					// check if we are associated to PT WIFI
					if (associationResultState.getAssociatePTWiFiState() != ActionState.OK) {
						
						// if we can't associated to PT WIFI, report that we can't continue the test
						Logger.v(tag, myMethodName, LogType.Debug, "Não me consegui associar a rede");
						
						if (handlerTestDone != null) {
							Message msg = new Message();
							msg.obj = actionResult;
							handlerTestDone.sendMessage(msg);
						}
						
						// Save the test result
						StoreInformation si = new StoreInformation(actionResult);
						myStore.addElem(si);
						
						// if the flag turnOFFWiFiAfterTest are set, Turn OFF the WiFi interface
						RestoreInitialWiFiState(turnOFFWiFiAfterTest);
						
						return;
					}
					
					// set as associated
					actionResult.setConnectToPTWiFiDONE();
					
					// report action to UI
					reportAction(ArQoSPocketPTWiFiActivity.AUTH_PTWIFI);
					
					// do the authentication in PT WiFi open garden
					PTWiFiAuthResult authPTWiFiResult = authPTWiFi.DoAuthPTWIFI();
					
					// set the authentication result in the report
					actionResult.setPTWiFiAuthResult(authPTWiFiResult);
					
					if (authPTWiFiResult.getAuthPTWifiState() != ActionState.OK) {
						
						// if we can't do the authentication on PT WIFI, report that we can't continue the test
						Logger.v(tag, myMethodName, LogType.Debug, "não consegui fazer a autenticação");
						
						if (handlerTestDone != null) {
							Message msg = new Message();
							msg.obj = actionResult;
							handlerTestDone.sendMessage(msg);
						}
					
						// Save the test result
						StoreInformation si = new StoreInformation(actionResult);
						myStore.addElem(si);
						
						// if the flag turnOFFWiFiAfterTest are set, Turn OFF the WiFi interface
						RestoreInitialWiFiState(turnOFFWiFiAfterTest);
						
						return;
					}
					
					Logger.v(tag, "runTest", LogType.Trace, "authentication in PT WiFi!");
					actionResult.setAuthPTWiFiDONE();
					
					// report action to UI
					reportAction(ArQoSPocketPTWiFiActivity.PING);
					
					// do the ping test
					PingResponse pingResult = pingService.DoPing();
					actionResult.setPingResponse(pingResult);
					
					// verify if the ping test was done correctly
					if (pingResult.getResponseEnum() == ResponseEnum.OK)
						actionResult.setDoPingDONE();
					
					// report action to UI
					reportAction(ArQoSPocketPTWiFiActivity.DOWNLOAD);

					// do the download test
					HttpServiceResponse httpDownloadResponse = httpDownloadUpload.DoHTTPDownload();
					actionResult.setHttpDownloadResponse(httpDownloadResponse);
					
					// verify if the download test was done correctly
					if (httpDownloadResponse.getExecState() == ResponseEnum.OK)
						actionResult.setDoDownloadTestDONE();
					
					// report action to UI
					reportAction(ArQoSPocketPTWiFiActivity.UPLOAD);
					
					// do the upload test
					HttpServiceResponse httpUploadResponse = httpDownloadUpload.DoHTTPUpload();
					actionResult.setHttpUploadResponse(httpUploadResponse);
					
					// verify if the upload test was done correctly
					if (httpUploadResponse.getExecState() == ResponseEnum.OK)
						actionResult.setDoUploadTestDONE();
					
					// report action to UI
					reportAction(ArQoSPocketPTWiFiActivity.SEND_TEST);
					
					// TODO: # Tenta enviar para o servidor

					// Envia o teste feito agora

					// Caso consiga enviar o teste verificar se existem mais tentas na lista para enviar
					serverConnector.sendInformation(actionResult);
					
					// check all states sucess and set the general state
					actionResult.verifyGeneralState();
					
					// Save the test result
					StoreInformation si = new StoreInformation(actionResult);
					myStore.addElem(si);
					
					// sen the information to the UI
					if (handlerTestDone != null) {
						Message msg = new Message();
						msg.obj = actionResult;
						handlerTestDone.sendMessage(msg);
					}
				
					// if the flag turnOFFWiFiAfterTest are set, Turn OFF the WiFi interface
					RestoreInitialWiFiState(turnOFFWiFiAfterTest);
					
				} catch(Exception ex) {
					Logger.v(tag, myMethodName, LogType.Trace, "ERROR :"+ex.toString());
				}
			}
			
			private void RestoreInitialWiFiState(boolean turnOFFWiFiAfterTest) {
				
				// save the method name to use in logger
				String myMethodName = "RestoreInitialWiFiState";
				
				if (turnOFFWiFiAfterTest) {
					Logger.v(tag, myMethodName, LogType.Trace, "Turn off the WiFi!");
					turnWiFiONorOFF.trunWiFiOFF();
				}
				
			}
			
			private void reportAction(int action) {
				Message msg = new Message();
				msg.arg1 = action;
				handlerReportTestAction.sendMessage(msg);
			}
		};

		new Thread(runnable).start();
	}

	
	/**
	 * 
	 * UI - get service state
	 * 
	 */
	
	public boolean isRunningTest() {
		return runState;
	}
	
	public List<StoreInformation> getAllStoreInformation() {
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

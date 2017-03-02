package PT.PTInov.ArQoSPocketEDP.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import PT.PTInov.ArQoSPocketEDP.R;
import PT.PTInov.ArQoSPocketEDP.DataStore.BDStore;
import PT.PTInov.ArQoSPocketEDP.DataStore.PrivateAppStore;
import PT.PTInov.ArQoSPocketEDP.DataStructs.DoNetworkTestResultEnum;
import PT.PTInov.ArQoSPocketEDP.DataStructs.E2EInformationDataStruct;
import PT.PTInov.ArQoSPocketEDP.DataStructs.EnumSendInformationResult;
import PT.PTInov.ArQoSPocketEDP.DataStructs.EnumTestE2EState;
import PT.PTInov.ArQoSPocketEDP.DataStructs.NetworkInformationDataStruct;
import PT.PTInov.ArQoSPocketEDP.DataStructs.StoreInformation;
import PT.PTInov.ArQoSPocketEDP.DataStructs.WorkFlowBase;
import PT.PTInov.ArQoSPocketEDP.DataStructs.WorkFlowChangeSIMCard;
import PT.PTInov.ArQoSPocketEDP.DataStructs.WorkFlowInsertSIMCard;
import PT.PTInov.ArQoSPocketEDP.DataStructs.WorkFlowTestConnectivity;
import PT.PTInov.ArQoSPocketEDP.DataStructs.WorkFlowType;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.ArQoSConnector;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.AskForTestModule;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.ConnectorCallBackInterface;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.M2MIPService;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.M2M_CSDService;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.RunService;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.DiscoveryEnum;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.Pair;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.RunServiceResponse;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.ServiceResponse;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.TestModuleResponse;
import PT.PTInov.ArQoSPocketEDP.MailConnector.GMailSender;
import PT.PTInov.ArQoSPocketEDP.MailConnector.Mail;
import PT.PTInov.ArQoSPocketEDP.UI.MainActivity;
import PT.PTInov.ArQoSPocketEDP.Utils.CircularStore;
import PT.PTInov.ArQoSPocketEDP.Utils.EnumOperator;
import PT.PTInov.ArQoSPocketEDP.Utils.LogType;
import PT.PTInov.ArQoSPocketEDP.Utils.Logger;
import PT.PTInov.ArQoSPocketEDP.Utils.RowDataTwoLines;
import PT.PTInov.ArQoSPocketEDP.Utils.Utils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.telephony.SmsManager;
import android.widget.Toast;

@SuppressLint({ "NewApi", "NewApi", "NewApi" })
public class EngineService extends Service implements EngineServiceInterface,
		ConnectorCallBackInterface {

	private final static String tag = "EngineService";
	private final static int MAX_ARRAY_STORE_SIZE = 100;

	// private static String serverHost = "http://10.112.80.72:9180";
	// //desenvolvimento - blade 7
	// private static String serverHost = "http://10.112.29.43:9180"; //
	// projecto lte tmn qsr
	//private static String serverHost = "http://10.112.85.102:9180"; // teste de
																	// integração
																	// APP2
	 
	private static String serverHost = "http://172.20.2.21:9180"; // PTC QA
	//private static String serverHost = "http://10.162.120.52:9180"; // PTC Producao
	
	// PDR
	//private static String ip = "10.162.120.52";
	//private static String ipTest = "10.162.120.52";
	
	// QA
	private static String ip = "172.20.2.21";
	private static String ipTest = "172.20.2.21";
	
	/*
	private static String serverHost = "http://10.112.83.44:9180";
	private static String ip = "10.112.83.44";
	private static String ipTest = "10.112.83.44";
	*/
	
	//private static String ip = "10.162.120.52";
	private static String port = "9180";
	private static String portTest = "8930";
	//private static String ipTest = "10.162.120.52";

	// false - stopped, true - running
	private boolean runState = false;
	
	private static Boolean blockAllActions = false;
	private static Boolean needNetwrok = false;

	// Information
	private GPSInformation GPSInfo = null;
	private CellInformation cellInfo = null;

	private CircularStore myStore = null;
	private BDStore myFisicalStore = null;

	// ArQoS server connector
	private ArQoSConnector serverConnector = null;
	
	// BroadCast to receive the connection events
	private BroadcastReceiver broadcastReceiver = null;

	// workflow control
	private WorkFlowType activeWorkFlow = WorkFlowType.NA;
	private WorkFlowChangeSIMCard workFlowChangeSIMCard = null;
	private WorkFlowInsertSIMCard workFlowInsertSIMCard = null;
	private WorkFlowTestConnectivity workFlowTestConnectivity = null;

	// testes E2E credentials
	private final static String E2EUsernameAll = "e-MON-all";
	private final static String E2EpasswordAll = "e.MON#2012";
	
	private final static String E2EUsernameTMN = "e-MON-tmn";
	private final static String E2EpasswordTMN = "e.MON#2012";
	
	private final static String E2EUsernameVDF = "e-MON-vdf";
	private final static String E2EpasswordVDF = "e.MON#2012";
	
	private final static String E2EUsernameOPT = "e-MON-opt";
	private final static String E2EpasswordOPT = "e.MON#2012";
	
	private final static String DestinationTest = "";
	
	private static EngineService myRef = null;
	
	private String emailAddressToSend = "edp-inovgrid@epmpwa.telecom.pt";
	private String emailCCAddressToSend = "arqos.sm@gmail.com";

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Logger.v(tag, "onStartCommand", LogType.Trace, "In");

		// warning the UI that already started
		try {
			
			doDiscovery();
			
			MainActivity.serviceAlreadyStart(this);
			
			MainActivity.blockAllActions = blockAllActions;
			MainActivity.needNetwrok = needNetwrok;

			/*
			 * if (cellInfo!=null) {
			 * cellInfo.updateAndRegestryTelephonyManager(); }
			 */

		} catch (Exception ex) {
			Logger.v(tag, "onStartCommand", LogType.Error, ex.toString());
		}
		return START_STICKY;
	}

	@Override
	public void onCreate() {

		Logger.v(tag, "onCreate", LogType.Trace, "In");

		try {
			
			myRef = this;
			
			// start the private store
			new PrivateAppStore(this);
			
			// load the report mail
			String storeReportMail = PrivateAppStore.getReportMail();
			if (storeReportMail != null)
				emailAddressToSend = storeReportMail;

			// Start the GPS Location
			// Load the gsp module, and try get the location updated
			GPSInfo = new GPSInformation(this);

			// Start load the Network information
			cellInfo = new CellInformation(this);

			// Create the circular store
			myStore = new CircularStore(MAX_ARRAY_STORE_SIZE);
			
			// Create the db store
			myFisicalStore = new BDStore(this);
			
			setServerHost(ip, port);
			
			registryBroadCastEventNetwork();

		} catch (Exception ex) {
			Logger.v(tag, "onCreate", LogType.Error, ex.toString());
		}
	}
	
	private void doDiscovery() {
		(new DiscoveryTask(serverConnector, this)).execute(null,null,null);
	}

	public void restartWorkFlow() {
		activeWorkFlow = WorkFlowType.NA;
		workFlowChangeSIMCard = null;
		workFlowInsertSIMCard = null;
	}

	public void setWorkFlow(WorkFlowType newWorkFlowType) {
		
		Logger.v(tag, "setWorkFlow", LogType.Debug, "In :: activeWorkFlow :"+newWorkFlowType);

		activeWorkFlow = newWorkFlowType;

		switch (newWorkFlowType) {
		case WORKFLOW_CHANGE_SIM_CARD:
			workFlowChangeSIMCard = new WorkFlowChangeSIMCard(this.GPSInfo.getLocation());
			break;
		case WORKFLOW_INSERT_SIM_CARD:
			workFlowInsertSIMCard = new WorkFlowInsertSIMCard(this.GPSInfo.getLocation());
			break;
		case WORKFLOW_INSERT_TEST_CONNECTIVITY:
			workFlowTestConnectivity = new WorkFlowTestConnectivity(this.GPSInfo.getLocation());
			break;
		}
		
		Logger.v(tag, "setWorkFlow", LogType.Debug, "workFlowChangeSIMCard :"+workFlowChangeSIMCard);
		Logger.v(tag, "setWorkFlow", LogType.Debug, "workFlowInsertSIMCard :"+workFlowInsertSIMCard);
		Logger.v(tag, "setWorkFlow", LogType.Debug, "workFlowTestConnectivity :"+workFlowTestConnectivity);
		
		Logger.v(tag, "setWorkFlow", LogType.Debug, "Out");
	}

	public WorkFlowType getActualWorkFlow() {
		return activeWorkFlow;
	}

	public List<WorkFlowBase> getMyHistory() {
		return myStore.getAllElems();
	}
	
	public boolean secoundTestDone() {
		
		switch (activeWorkFlow) {
			case WORKFLOW_CHANGE_SIM_CARD:
				return (workFlowChangeSIMCard.getSecoundE3ETestState() != EnumTestE2EState.NA)?true:false;
		}
	
		return false;
		
	}
	
	public boolean firstTestDone() {
		
		switch (activeWorkFlow) {
			case WORKFLOW_CHANGE_SIM_CARD:
			
				return (workFlowChangeSIMCard.getFirstE3ETestState() != EnumTestE2EState.NA)?true:false;
			
			case WORKFLOW_INSERT_SIM_CARD:
			
				return (workFlowInsertSIMCard.getE3ETestState() != EnumTestE2EState.NA)?true:false;
			
			case WORKFLOW_INSERT_TEST_CONNECTIVITY:
			
				return (workFlowTestConnectivity.getE2ETestState() != EnumTestE2EState.NA)?true:false;
			
		}
		
		return false;
	}
	
	public void insertModemSerialNumber(String modemSerialNumber) {
		
		// make a generic workflow pointer
		WorkFlowBase genericWorkflow = null;
					
		switch (activeWorkFlow) {
			case WORKFLOW_CHANGE_SIM_CARD:
				genericWorkflow = workFlowChangeSIMCard;
				break;
			case WORKFLOW_INSERT_SIM_CARD:
				genericWorkflow = workFlowInsertSIMCard;
				break;
			case WORKFLOW_INSERT_TEST_CONNECTIVITY:
				genericWorkflow = workFlowTestConnectivity;
				break;
		}
		
		genericWorkflow.setModemSerialNumber(modemSerialNumber);
	}
	
	public void clearLastTest() {
		
		Logger.v(tag, "clearLastTest", LogType.Debug, "In :: activeWorkFlow :"+activeWorkFlow);
		
		switch (activeWorkFlow) {
			case WORKFLOW_CHANGE_SIM_CARD:
		
				workFlowChangeSIMCard.clearLastTest();
				break;
		
			case WORKFLOW_INSERT_SIM_CARD:
		
				workFlowInsertSIMCard.clearLastTest();
				break;
		
			case WORKFLOW_INSERT_TEST_CONNECTIVITY:
		
				workFlowTestConnectivity.clearLastTest();
				break;
		}
		
		Logger.v(tag, "clearLastTest", LogType.Debug, "Out");
	
	}
	
	public EnumTestE2EState getStateOfLastTest() {
		
		switch (activeWorkFlow) {
			case WORKFLOW_CHANGE_SIM_CARD:
				
				if (workFlowChangeSIMCard.getSecoundE3ETestState() != EnumTestE2EState.NA)
					return workFlowChangeSIMCard.getSecoundE3ETestState();
				else
					return workFlowChangeSIMCard.getFirstE3ETestState();
				
			case WORKFLOW_INSERT_SIM_CARD:
				
				return workFlowInsertSIMCard.getE3ETestState();
				
			case WORKFLOW_INSERT_TEST_CONNECTIVITY:
				
				 return workFlowTestConnectivity.getE2ETestState();
				
		}
		
		return EnumTestE2EState.NA;
	}
	
	private String getUsernameToOperator(EnumOperator operator) {
		
		switch (operator) {
			case TMN:
				return E2EUsernameTMN;
			case VODAFONE:
				return E2EUsernameVDF;
			case OPTIMUS:
				return E2EUsernameOPT;
		}

		return E2EUsernameAll;
	}
	
	private String getPasswordToOperator(EnumOperator operator) {
		
		switch (operator) {
			case TMN:
				return E2EpasswordTMN;
			case VODAFONE:
				return E2EpasswordVDF;
			case OPTIMUS:
				return E2EpasswordOPT;
		}

		return E2EpasswordAll;
	}
	
	public EnumOperator getOperatorFromMSISDN(String MSISDN) {
		String method = "getOperatorFromMSISDN";
		
		EnumOperator actualOperator = EnumOperator.NA;
		
		try {
			
			String first2Chars = MSISDN.substring(0, 2);
			
			if (first2Chars.equals("91")) {
				return EnumOperator.VODAFONE;
			} else if (first2Chars.equals("93")) {
				return EnumOperator.OPTIMUS;
			} else {
				return EnumOperator.TMN;
			}
			
		} catch(Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return actualOperator;
	}
	
	public EnumTestE2EState doE2ETestIP(String MSISDN, String IPAddr,
			Date testExecDate, int flagTest) {

		E2EInformationDataStruct E2EinfoData = null;
		Logger.v(tag, "doE2ETestIP", LogType.Debug, "In - flagTest :"+flagTest);

		try {
			
			// set a default value for error
			switch (activeWorkFlow) {
			case WORKFLOW_CHANGE_SIM_CARD:
				workFlowChangeSIMCard.setTestResult(E2EinfoData, false,flagTest);
				break;
			}

			Logger.v(tag, "doE2ETestCSD", LogType.Debug, "get operator!");
			
			EnumOperator actualOperator = getOperatorFromMSISDN(MSISDN);
			
			Logger.v(tag, "doE2ETestIP", LogType.Debug," actualOperator :"+actualOperator);
			
			// get module from server...............
			AskForTestModule aftm = new AskForTestModule(getUsernameToOperator(actualOperator),getPasswordToOperator(actualOperator), ipTest, portTest);
			TestModuleResponse tmr = aftm.getAvailableModule();
			
			// Verify if the module available
			if (tmr == null) return EnumTestE2EState.MODULE_UNAVAILABLE;

			Logger.v(tag, "doE2ETestIP", LogType.Debug,"already have the module :: moduleID :"+tmr.getId());
					

			// set test with module and get test id.............
			M2MIPService rs = new M2MIPService(getUsernameToOperator(actualOperator),getPasswordToOperator(actualOperator), ipTest, portTest);
			RunServiceResponse rsr = rs.runService(null, MSISDN, IPAddr, tmr.getId());
			
			// verify if the probe give the response
			if (rsr == null) return EnumTestE2EState.NETWORK_ERROR;

			Logger.v(tag, "doE2ETestIP", LogType.Debug,"requet test done :: rsr :"+rsr.toString());

			ArrayList<ServiceResponse> resulsWaitForResponse = new ArrayList<ServiceResponse>();

			long waitTimeForResultsInMillisec = 0;
			if (rsr.getIfTestIsOK()) {

				try {
					waitTimeForResultsInMillisec += 10000;
					Thread.sleep(10000);
				} catch (Exception exx) {
				}
				
				Logger.v(tag, "doE2ETestIP", LogType.Debug, "Antes de fazr o pedido do teste");

				// try get the results...............
				ServiceResponse sr = rs.getServiceTestResponse(rsr.getTestID());
				
				// if rs == null, try three times
				int tryTimes = 3;
				while(sr == null && tryTimes>0) {
					Thread.sleep(10000);
					sr = rs.getServiceTestResponse(rsr.getTestID());
					tryTimes--;
				}
				
				resulsWaitForResponse.add(sr);

				long timeToSleep = 1;
				long timeToStopInMillsec = -60 * 1000;
				while (sr.getExecutionstatus().equals("0")  && timeToSleep>=timeToStopInMillsec) {

					try {

						timeToSleep = (Integer.parseInt(sr.getTimeoend()) * 1000) / 4;
						waitTimeForResultsInMillisec += Math.abs(timeToSleep);
						Logger.v(tag, "doE2ETestIP", LogType.Debug,"timeToSleep :"+timeToSleep);
						Thread.sleep(Math.abs(timeToSleep));

					} catch (Exception ex) {
					}

					Logger.v(tag, "doE2ETestIP", LogType.Debug, "need to get the state again");
					sr = rs.getServiceTestResponse(rsr.getTestID());
					
					// if rs == null, try three times
					tryTimes = 3;
					while(sr == null && tryTimes>0) {
						Thread.sleep(10000);
						sr = rs.getServiceTestResponse(rsr.getTestID());
						tryTimes--;
					}
					
					resulsWaitForResponse.add(sr);
				}
			} else {
				return EnumTestE2EState.PROBE_ERROR;
			}
			
			Logger.v(tag, "doE2ETestIP", LogType.Debug, "already have the result :: resulsWaitForResponse :"+resulsWaitForResponse.toString());
			
			// build the results object
			E2EinfoData = new E2EInformationDataStruct(
					tmr, waitTimeForResultsInMillisec, DestinationTest,
					M2MIPService.testName, MSISDN,  IPAddr, resulsWaitForResponse);
			
			Logger.v(tag, "doE2ETestIP", LogType.Debug, "E2EinfoData :"+E2EinfoData.toString());

			switch (activeWorkFlow) {
			case WORKFLOW_CHANGE_SIM_CARD:
				workFlowChangeSIMCard.setTestResult(E2EinfoData, E2EinfoData.testOK(), flagTest);
				break;
			case WORKFLOW_INSERT_SIM_CARD:
				workFlowInsertSIMCard.setTestResult(E2EinfoData, E2EinfoData.testOK());
				break;
			case WORKFLOW_INSERT_TEST_CONNECTIVITY:
				workFlowTestConnectivity.setTestResult(E2EinfoData, E2EinfoData.testOK());
				break;

			}
			
			// Verifica se existiu um erro na proble. Caso tenha sido retornado alguem erro, da erro interno
			if (E2EinfoData.test_probe_error()) {
				return EnumTestE2EState.PROBE_ERROR; 
			}
			
			
			Logger.v(tag, "doE2ETestIP", LogType.Debug, "testState :"+E2EinfoData.testOK());

			if (E2EinfoData.testOK())
				return EnumTestE2EState.DONE_WITH_SUCCESS;
			else
				return EnumTestE2EState.DONE_WITHOUT_SUCCESS;

		} catch (Exception ex) {
			Logger.v(tag, "doE2ETestIP", LogType.Error, ex.toString());
		}
		
		return EnumTestE2EState.NETWORK_ERROR;
		
	}

	public EnumTestE2EState doE2ETestCSD(String MSISDN, String IPAddr,
			Date testExecDate, int flagTest) {
		
		E2EInformationDataStruct E2EinfoData = null;
		Logger.v(tag, "doE2ETestCSD", LogType.Debug, "In - flagTest :"+flagTest);

		try {

			
			// set a default value for error
			switch (activeWorkFlow) {
			case WORKFLOW_CHANGE_SIM_CARD:
				workFlowChangeSIMCard.setTestResult(E2EinfoData, false, flagTest);
				break;
			}
			
			Logger.v(tag, "doE2ETestCSD", LogType.Debug,"Ask for module!");
			
			// get module from server...............
			AskForTestModule aftm = new AskForTestModule(E2EUsernameAll,E2EpasswordAll, ipTest, portTest);
			TestModuleResponse tmr = aftm.getAvailableModule();
			
			// Verify if the module available
			if (tmr == null) {
				Logger.v(tag, "doE2ETestCSD", LogType.Debug, "return EnumTestE2EState.MODULE_UNAVAILABLE;");
				return EnumTestE2EState.MODULE_UNAVAILABLE;
			}

			Logger.v(tag, "doE2ETestCSD", LogType.Debug,"already have the module :: moduleID :"+tmr.getId());
					

			// set test with module and get test id.............
			M2M_CSDService rs = new M2M_CSDService(E2EUsernameAll, E2EpasswordAll, ipTest, portTest);
			RunServiceResponse rsr = rs.runService(null, MSISDN, IPAddr, tmr.getId());
			
			// verify if the probe give the response
			if (rsr == null) {
				Logger.v(tag, "doE2ETestCSD", LogType.Debug, "return EnumTestE2EState.NETWORK_ERROR; - 1");
				return EnumTestE2EState.NETWORK_ERROR;
			}

			Logger.v(tag, "doE2ETestCSD", LogType.Debug,"requet test done :: rsr :"+rsr.toString());

			ArrayList<ServiceResponse> resulsWaitForResponse = new ArrayList<ServiceResponse>();

			long waitTimeForResultsInMillisec = 0;
			if (rsr.getIfTestIsOK()) {

				try {
					waitTimeForResultsInMillisec += 10000;
					Thread.sleep(10000);
				} catch (Exception exx) {
				}

				// try get the results...............
				ServiceResponse sr = rs.getServiceTestResponse(rsr.getTestID());
				
				// if rs == null, try three times
				int tryTimes = 3;
				while(sr == null && tryTimes>0) {
					Thread.sleep(10000);
					sr = rs.getServiceTestResponse(rsr.getTestID());
					tryTimes--;
				}
				
				resulsWaitForResponse.add(sr);

				long timeToSleep = 1;
				long timeToStopInMillsec = -60 * 1000;
				while (sr.getExecutionstatus().equals("0") && timeToSleep>=timeToStopInMillsec) {

					try {

						timeToSleep = (Integer.parseInt(sr.getTimeoend()) * 1000) / 4;
						//waitTimeForResultsInMillisec += timeToSleep;
						waitTimeForResultsInMillisec += Math.abs(timeToSleep);
						Logger.v(tag, "doE2ETestCSD", LogType.Debug,"timeToSleep :"+timeToSleep);
						//Thread.sleep(timeToSleep);
						Thread.sleep(Math.abs(timeToSleep));

					} catch (Exception ex) {
					}

					Logger.v(tag, "doE2ETestCSD", LogType.Debug, "need to get the state again");
					sr = rs.getServiceTestResponse(rsr.getTestID());
					
					tryTimes = 3;
					while(sr == null && tryTimes>0) {
						Thread.sleep(10000);
						sr = rs.getServiceTestResponse(rsr.getTestID());
						tryTimes--;
					}
					
					resulsWaitForResponse.add(sr);
				}
			} else {
				Logger.v(tag, "doE2ETestCSD", LogType.Debug, "return EnumTestE2EState.PROBE_ERROR; - 1 ");
				return EnumTestE2EState.PROBE_ERROR;
			}
			
			Logger.v(tag, "doE2ETestCSD", LogType.Debug, "already have the result :: resulsWaitForResponse :"+resulsWaitForResponse.toString());
			
			// build the results object
			E2EinfoData = new E2EInformationDataStruct(
					tmr, waitTimeForResultsInMillisec, DestinationTest,
					M2M_CSDService.testName, MSISDN,  IPAddr, resulsWaitForResponse);
			
			Logger.v(tag, "doE2ETestCSD", LogType.Debug, "E2EinfoData :"+E2EinfoData.toString());
			
			switch (activeWorkFlow) {
				case WORKFLOW_CHANGE_SIM_CARD:
					workFlowChangeSIMCard.setTestResult(E2EinfoData, E2EinfoData.testOK(), flagTest);
					break;
				case WORKFLOW_INSERT_SIM_CARD:
					workFlowInsertSIMCard.setTestResult(E2EinfoData, E2EinfoData.testOK());
					break;
				case WORKFLOW_INSERT_TEST_CONNECTIVITY:
					workFlowTestConnectivity.setTestResult(E2EinfoData, E2EinfoData.testOK());
					break;
			}
			
			Logger.v(tag, "doE2ETestCSD", LogType.Debug, "testState :"+E2EinfoData.testOK());

			
			// Verifica se existiu um erro na proble. Caso tenha sido retornado alguem erro, da erro interno
			if (E2EinfoData.test_probe_error()) {
				Logger.v(tag, "doE2ETestCSD", LogType.Debug, "return EnumTestE2EState.PROBE_ERROR; - 2 ");
				return EnumTestE2EState.PROBE_ERROR; 
			}
			
			
			if (E2EinfoData.testOK()) {
				Logger.v(tag, "doE2ETestCSD", LogType.Debug, "return EnumTestE2EState.DONE_WITH_SUCCESS;");
				return EnumTestE2EState.DONE_WITH_SUCCESS;
			} else {
				Logger.v(tag, "doE2ETestCSD", LogType.Debug, "return EnumTestE2EState.DONE_WITHOUT_SUCCESS;");
				return EnumTestE2EState.DONE_WITHOUT_SUCCESS;
			}

		} catch (Exception ex) {
			Logger.v(tag, "doE2ETestCSD", LogType.Error, ex.toString());
		}
		
		Logger.v(tag, "doE2ETestCSD", LogType.Debug, "return EnumTestE2EState.NETWORK_ERROR; - 2");
		return EnumTestE2EState.NETWORK_ERROR;
	}
	
	public boolean saveAndSendWorkFlowResult(String comment, String modelo_selo, String resultDetails) {
		String method = "saveAndSendWorkFlowResult";
		
		try {
			
			Logger.v(tag, method, LogType.Debug, "In");
			
			Logger.v(tag, method, LogType.Debug, "Allmsg :"+myFisicalStore.getAllMsg().toString());
			
			NetworkInformationDataStruct networkMetricsInformation = new NetworkInformationDataStruct(GPSInfo.getLatitude(), GPSInfo.getLongitude(),
				cellInfo.getGsmSignalStrength(),
				cellInfo.getGsmBitErrorRate(), cellInfo.getGsmCellID(),
				cellInfo.getGsmLac(), cellInfo.getGsmPsc(),
				cellInfo.getMCC(), cellInfo.getIMEI(),
				cellInfo.getMCC_MNC(), cellInfo.getOperatorName(),
				cellInfo.getNetworkType(), cellInfo.getISOCountry(),
				cellInfo.getSIMSerialNumber(), cellInfo.getIMSI(),
				cellInfo.getRoaming(), cellInfo.getNeighboringCellList(),
				new Date());
			
			// make a generic workflow pointer
			WorkFlowBase genericWorkflow = null;
						
			switch (activeWorkFlow) {
				case WORKFLOW_CHANGE_SIM_CARD:
					genericWorkflow = workFlowChangeSIMCard;
					break;
				case WORKFLOW_INSERT_SIM_CARD:
					genericWorkflow = workFlowInsertSIMCard;
					break;
				case WORKFLOW_INSERT_TEST_CONNECTIVITY:
					genericWorkflow = workFlowTestConnectivity;
					break;
			}

			//Logger.v(tag, method, LogType.Debug, "comment :"+comment);
			comment = comment.replace("\n", " ");
			//Logger.v(tag, method, LogType.Debug, "comment :"+comment);
			Pair testeResult = serverConnector.sendInformationJSONToServer(genericWorkflow, comment, modelo_selo, resultDetails, networkMetricsInformation);
			
			// Grava a informação na store
			myStore.addElem(genericWorkflow);
			
			Logger.v(tag, method, LogType.Debug, " serverConnector.sendInformationJSONToServer state :"+testeResult.getKey());
			Logger.v(tag, method, LogType.Debug, " serverConnector.sendInformationJSONToServer msg :"+testeResult.getValue());
			
			//String mailToSendDebug = genericWorkflow.buildMailReport(comment, resultDetails, networkMetricsInformation);
			//Logger.v(tag, method, LogType.Debug, " mailToSendDebug :"+mailToSendDebug);
			
			//String mailToSend2 = genericWorkflow.buildMailReport(comment, modelo_selo, resultDetails, networkMetricsInformation);
			//sendEmail(emailAddressToSend, emailCCAddressToSend, genericWorkflow.getModemSerialNumber()+resultDetails, mailToSend2, Utils.convertDate(genericWorkflow.getCreateTestDate()));
			
			// caso não consiga enviar a informação, coloca a msg na store para ser enviado automaticamente mais tarde, logo que exista ligação à internet
			if (testeResult.getKey() != EnumSendInformationResult.OK) {
				
				Logger.savaToFile(testeResult.getValue());
				myFisicalStore.queueNewMsg(testeResult.getValue());
				Logger.v(tag, method, LogType.Debug, "Allmsg :"+myFisicalStore.getAllMsg().toString());
			} else {
				
				Logger.v(tag, method, LogType.Debug, "Send mail");
				Logger.savaToFile(testeResult.getValue());
				String mailToSend = genericWorkflow.buildMailReport(comment, modelo_selo, resultDetails, networkMetricsInformation);
				//Logger.v(tag, method, LogType.Debug, " mailToSend :"+mailToSend);
				//sendEmail("afilipebarbosa@gmail.com", "["+genericWorkflow.getModemSerialNumber()+"] ArQoS e-Mon Status", mailToSend, Utils.convertDate(genericWorkflow.getCreateTestDate()));
				sendEmail(emailAddressToSend, emailCCAddressToSend, genericWorkflow.getModemSerialNumber()+resultDetails.replace(", tente novamente..."," "), mailToSend, Utils.convertDate(genericWorkflow.getCreateTestDate()));
				
				processAllMsgToSent();
			}
			
			Logger.v(tag,method, LogType.Debug, "Out");
		
		} catch(Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return true;
	}
	
	public void sendEmail(String emailDest, String emailCC, String subject, String body, String attachName) {
		final String method = "sendEmail";
		
		try {
			
				Logger.v(tag,method, LogType.Debug, "In");
				
				Logger.v(tag,method, LogType.Debug, "emailDest : "+emailDest);
				Logger.v(tag,method, LogType.Debug, "emailCC : "+emailCC);
				Logger.v(tag,method, LogType.Debug, "subject : "+subject);
				Logger.v(tag,method, LogType.Debug, "body : "+body);
				Logger.v(tag,method, LogType.Debug, "subject : "+attachName);
				
				// Req ARQOSPOCKETFW-41 : adicionar a tag "- FIM" no final do assunto
				subject += "- FIM";
				
				// test send sms
				//sendSMS("927802671", body);
				
				/*
				GMailSender sender = new GMailSender("treinad@gmail.com", "nov1986");
				if (sender.sendMail(subject, body, "afilipebarbosa@gmail.com", emailDest)) {
					Logger.v(tag,method, LogType.Debug, "falhou o envio do e-mail!");
				}*/
			
				//Mail mail = new Mail("treinad@gmail.com", "nov1986");
				//mail.send(emailDest, subject, body);
		

			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("message/rfc822");
			intent.putExtra(Intent.EXTRA_EMAIL,
					new String[] { emailDest });
			intent.putExtra(Intent.EXTRA_CC,
					new String[] { emailCC });
			intent.putExtra(Intent.EXTRA_SUBJECT, subject);
			intent.putExtra(Intent.EXTRA_TEXT, body);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			// build attach file
			try {
				
				
				/*
				// File outputDir = this.getCacheDir(); // context being the
				// Activity pointer
				
				//File outputDir = this.getDir("e-MONTemp", Context.MODE_WORLD_READABLE); //Creating an internal dir;
				
				//File outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
				File outputDir = this.getExternalCacheDir();
				Logger.v(tag, method, LogType.Debug, "outputDir :" + outputDir.getAbsolutePath());
				
				//File outputDir = new File("/sdcard/e-MONTemp/");
				
				//if (!outputDir.exists()) {
				//	Logger.v(tag, method, LogType.Debug, "Cria directorio :"
				//			+ outputDir);
				//	outputDir.mkdir();
				//}
				
				//FileOutputStream fOut = null;
				//fOut = openFileOutput("anexo.txt", Context.MODE_WORLD_READABLE );
				File outputFile = File.createTempFile(attachName, ".txt", outputDir);
				Logger.v(tag, method, LogType.Debug, "outputFile :" + outputFile.getAbsolutePath());
				
				// File outputFile = File.createTempFile(attachName, ".txt");
				FileOutputStream os = new FileOutputStream(outputFile, false)	;
				
				*/
				
				//File cacheDir = getCacheDir();
		        //File outFile = new File(cacheDir, attachName+".txt");
		        //openFileOutput()
				
				Uri dir = null;
				
				// verifica se o tlm tem cartao de memoria
				Logger.v(tag, method, LogType.Debug, "tem cartao? :"+ hasStorage(true));
				if (hasStorage(true)) {
				
					// se tiver cartão de memoria utiliza o cartao para fazer cache do anexos
					File outputDir = new File("/sdcard/e-MONTemp/");
					
					if (!outputDir.exists()) {
						Logger.v(tag, method, LogType.Debug, "Cria directorio :"+ outputDir);
						outputDir.mkdir();
					}
					
					File outputFile = File.createTempFile(attachName, ".txt", outputDir);
					FileOutputStream os = new FileOutputStream(outputFile, false)	;
					OutputStreamWriter out = new OutputStreamWriter(os);
					out.write(body);
					out.close();
					
					dir = Uri.parse("file://" + outputFile.getAbsolutePath());
					
				} else {
				
					// se não tiver cartão utiliza o sistema "mal feito"
				
					FileOutputStream fOut = null;
					fOut = openFileOutput(attachName+".txt", Context.MODE_WORLD_READABLE );
				
					OutputStreamWriter out = new OutputStreamWriter(fOut);
					out.write(body);
					out.close();
					fOut.close();

					dir = Uri.parse("file://" + this.getFileStreamPath(attachName+".txt"));
				
				}
				
				if (dir != null) {
					Logger.v(tag, method, LogType.Debug, "path :" + dir);
					intent.putExtra(Intent.EXTRA_STREAM, dir);
				}

			} catch (Exception localEX) {
				Logger.v(tag, method, LogType.Error, "Erro ao adicionar o anexo ao email :"+localEX.toString());
			}
			
			try {
				startActivity(intent);
			} catch (android.content.ActivityNotFoundException ex) {
				Toast.makeText(myRef,"Não existem contas de email configuradas.",Toast.LENGTH_LONG).show();
			}

		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
			
			// TODO: falhou o envio do mail, colocar na lista de e-mail a enviar mais tarde
		}
		
		Logger.v(tag,method, LogType.Debug, "Out");
	}
	
	private void sendSMS(String phoneNumber, String message)
    {
		final String method = "sendSMS";

		try {
			String SENT = "SMS_SENT";
			String DELIVERED = "SMS_DELIVERED";

			PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
					new Intent(SENT), 0);

			PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
					new Intent(DELIVERED), 0);

			// ---when the SMS has been sent---
			registerReceiver(new BroadcastReceiver() {
				@Override
				public void onReceive(Context arg0, Intent arg1) {
					switch (getResultCode()) {
					case Activity.RESULT_OK:
						Toast.makeText(getBaseContext(), "SMS sent",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
						Toast.makeText(getBaseContext(), "Generic failure",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_NO_SERVICE:
						Toast.makeText(getBaseContext(), "No service",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_NULL_PDU:
						Toast.makeText(getBaseContext(), "Null PDU",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_RADIO_OFF:
						Toast.makeText(getBaseContext(), "Radio off",
								Toast.LENGTH_SHORT).show();
						break;
					}
				}
			}, new IntentFilter(SENT));

			// ---when the SMS has been delivered---
			registerReceiver(new BroadcastReceiver() {
				@Override
				public void onReceive(Context arg0, Intent arg1) {
					switch (getResultCode()) {
					case Activity.RESULT_OK:
						Toast.makeText(getBaseContext(), "SMS delivered",
								Toast.LENGTH_SHORT).show();
						break;
					case Activity.RESULT_CANCELED:
						Toast.makeText(getBaseContext(), "SMS not delivered",
								Toast.LENGTH_SHORT).show();
						break;
					}
				}
			}, new IntentFilter(DELIVERED));

			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
			
		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
    }
	
	private void processAllMsgToSent() {
		final String method = "processAllMsgToSent";
		
		Logger.v(tag,method, LogType.Debug, "In");
		
		try {

			TreeMap<String, String> msgToSent = myFisicalStore.getAllMsg();

			for (String key : msgToSent.keySet()) {
				if (serverConnector.sendInformationJSONToServer(msgToSent
						.get(key))) {
					myFisicalStore.removeMsgFromQueue(key);
					Logger.v(tag, method, LogType.Debug, "msg sent :"
							+ msgToSent.get(key));
				}
			}
			
		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		Logger.v(tag, method, LogType.Debug, "Allmsg :"+myFisicalStore.getAllMsg().toString());
	}
	
	private void registryBroadCastEventNetwork() {
		final String method = "registryBroadCastEventNetwork";
		
		Logger.v(tag,method, LogType.Debug, "In");
		
		broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
            	
            	try {

                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                if (cm != null) {
                
                	if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
                		Logger.v(tag,method, LogType.Debug, "connectivity enabled");
                		processAllMsgToSent();

                	} else {
                		Logger.v(tag,method, LogType.Debug, "connectivity disabled");
                	}

                }
                
            	} catch(Exception ex) {
            		Logger.v(tag, method, LogType.Error, ex.toString());
            	}
            }

        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
	}
	
	
	
	public EnumOperator getActualOperatorOnPhone() {
		
		try {
			
			Logger.v(tag, "getActualOperatorOnPhone", LogType.Debug, "In");
		
			String localOperatorMCC_MNC = cellInfo.getMCC_MNC();
			
			Logger.v(tag, "getActualOperatorOnPhone", LogType.Debug, "localOperatorMCC_MNC :"+localOperatorMCC_MNC);
			
			if (localOperatorMCC_MNC.contains("26806")) {
				Logger.v(tag, "getActualOperatorOnPhone", LogType.Debug, "TMN");
				return EnumOperator.TMN;
			} else if (localOperatorMCC_MNC.contains("26801")) {
				Logger.v(tag, "getActualOperatorOnPhone", LogType.Debug, "VODAFONE");
				return EnumOperator.VODAFONE;
			} else if (localOperatorMCC_MNC.contains("26803")) {
				Logger.v(tag, "getActualOperatorOnPhone", LogType.Debug, "OPTIMUS");
				return EnumOperator.OPTIMUS;
			}
			
		} catch(Exception ex) {}
		
		return EnumOperator.NA;
	}

	public NetworkInformationDataStruct getNetworkInfoFromTest() {

		NetworkInformationDataStruct valueToReturn = null;

		// make a generic workflow pointer
		WorkFlowBase genericWorkflow = null;

		switch (activeWorkFlow) {
		case WORKFLOW_CHANGE_SIM_CARD:
			genericWorkflow = workFlowChangeSIMCard;
			break;

		case WORKFLOW_INSERT_SIM_CARD:
			genericWorkflow = workFlowInsertSIMCard;
			break;

		case WORKFLOW_INSERT_TEST_CONNECTIVITY:
			genericWorkflow = workFlowTestConnectivity;
			break;

		}
		
		valueToReturn = genericWorkflow.getNetworkMetricsInformation();

		return valueToReturn;
	}

	public DoNetworkTestResultEnum doNetworkTest() {

		try {

			Logger.v(tag, "doNetworkTest", LogType.Debug, "In");

			// verify if had tlm information
			if (!cellInfo.getActiveTelephonyManager()) {

				Logger.v(tag, "doNetworkTest", LogType.Debug,
						"return DoNetworkTestResultEnum.NO_MOBILE_NETWORK");
				return DoNetworkTestResultEnum.NO_MOBILE_NETWORK;
			}

			// make a generic workflow pointer
			WorkFlowBase genericWorkflow = null;

			switch (activeWorkFlow) {
			case WORKFLOW_CHANGE_SIM_CARD:
				genericWorkflow = workFlowChangeSIMCard;
				break;

			case WORKFLOW_INSERT_SIM_CARD:
				genericWorkflow = workFlowInsertSIMCard;
				break;

			case WORKFLOW_INSERT_TEST_CONNECTIVITY:
				genericWorkflow = workFlowTestConnectivity;
				break;
			}

			genericWorkflow.setNetworkInformationDataStruct(
					GPSInfo.getLatitude(), GPSInfo.getLongitude(),
					cellInfo.getGsmSignalStrength(),
					cellInfo.getGsmBitErrorRate(), cellInfo.getGsmCellID(),
					cellInfo.getGsmLac(), cellInfo.getGsmPsc(),
					cellInfo.getMCC(), cellInfo.getIMEI(),
					cellInfo.getMCC_MNC(), cellInfo.getOperatorName(),
					cellInfo.getNetworkType(), cellInfo.getISOCountry(),
					cellInfo.getSIMSerialNumber(), cellInfo.getIMSI(),
					cellInfo.getRoaming(), cellInfo.getNeighboringCellList(),
					new Date());

			switch (genericWorkflow.getNetworkMetricsState()) {
				case DONE_WITH_RANGE:
					return DoNetworkTestResultEnum.DONE_WITH_GOOD_RANGE;
				case DONE_WITHOUT_RANGE:
					return DoNetworkTestResultEnum.DONE_WITH_GOOD_RANGE;
				default:
					return DoNetworkTestResultEnum.ERROR;
			}

		} catch (Exception ex) {
			Logger.v(tag, "doNetworkTest", LogType.Error, ex.toString());
		}

		return DoNetworkTestResultEnum.ERROR;
	}

	public List<WorkFlowBase> getAllResult() {
		return myStore.getAllElems();
	}

	public void setServerHost(String ip, String port) {
		
		this.ip = ip;
		this.port = port;
		serverHost = "http://" + ip + ":" + port;
		
		// init the connector
		serverConnector = new ArQoSConnector(this, serverHost, this);
		
		// TODO: para estar versão, os dois ips são iguais.....
		this.ip = ip;
		this.ipTest = ip;
		
		//send discovery
		doDiscovery();	
	}

	public String getPort() {
		return port;
	}

	public String getIP() {
		return ip;
	}

	public void setDescoveryStatus(DiscoveryEnum status) {
		String method = "setDescoveryStatus";
		
		try {
			
			

			if (DiscoveryEnum.STOPWORK == status) {

				blockAllActions = true;

				// bloqueia todo o tipo de testes possiveis
				MainActivity.blockAllActions = true;
			} else if (DiscoveryEnum.NETWORK_ERROR == status) {
				
				//NOTA: pra já vai ficar a false..........
				
				needNetwrok = false;
				
				// bloqueia todo o tipo de testes possiveis
				MainActivity.needNetwrok = false;
				
			} else {
				blockAllActions = false;
				needNetwrok = false;
				
				MainActivity.blockAllActions = false;
				MainActivity.needNetwrok = false;
			}

		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
	}
	
	public static boolean hasStorage(boolean requireWriteAccess) {  
	    String state = Environment.getExternalStorageState();  
	  
	    if (Environment.MEDIA_MOUNTED.equals(state)) {  
	        return true;  
	    } else if (!requireWriteAccess  
	            && Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {  
	        return true;  
	    }  
	    return false;  
	}
	
	public String getEmailToSend() {
		return emailAddressToSend;
	}
	
	public void setEmailToSend(String email) {
		String method = "setEmailToSend";
		
		try {
			
			emailAddressToSend = email;
		
			// Grava na store o novo email
			PrivateAppStore.setReportMail(email);
			
		} catch(Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
	}

}

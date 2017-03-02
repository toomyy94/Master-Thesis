package PT.PTInov.ArQoSPocketEDP.DataStore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import PT.PTInov.ArQoSPocketEDP.DataStructs.E2EInformationDataStruct;
import PT.PTInov.ArQoSPocketEDP.DataStructs.MyNeighboringCellInfo;
import PT.PTInov.ArQoSPocketEDP.DataStructs.NetworkInformationDataStruct;
import PT.PTInov.ArQoSPocketEDP.DataStructs.WorkFlowBase;
import PT.PTInov.ArQoSPocketEDP.DataStructs.WorkFlowChangeSIMCard;
import PT.PTInov.ArQoSPocketEDP.DataStructs.WorkFlowInsertSIMCard;
import PT.PTInov.ArQoSPocketEDP.DataStructs.WorkFlowTestConnectivity;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.ExecutionResults;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.ServiceResponse;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.TestModuleResponse;
import PT.PTInov.ArQoSPocketEDP.Utils.LogType;
import PT.PTInov.ArQoSPocketEDP.Utils.Logger;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDStore extends SQLiteOpenHelper {
	
	private final static String tag = "BDStore";
	
	private static final int DATABASE_VERSION = 1;
	private static final String DB_NAME = "MyPrivateBD.db";
	
	private static final String TABLE_WORKFLOW = "Workflow";
	private static final String TABLE_NETWORK_INFORMATION = "NetworkInfo";
	private static final String TABLE_NETWORK_INFORMATION_NEIGHBORINGCELL = "NeighboringCell";
	private static final String TABLE_TEST = "Test";
	private static final String TABLE_MODULE_INFO = "Module";
	private static final String TABLE_SERVICE_RESPONSE = "ServiceResponse";
	private static final String TABLE_EXECUTION_RESULTS = "ExecutionResults";
	
	private static final String TABLE_SEND_INFORMATION = "SendInfoTable";
	
	
	private SQLiteDatabase db = null;
	private Context appContext = null;
	
	public BDStore(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);

		String methodName = "AbstractDataStore";
		appContext = context;

		db = this.getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String methodName = "onCreate";
		
		Logger.v(tag, methodName, LogType.Debug, "In");
		
		try {
			
			db.execSQL("create table "+ TABLE_SEND_INFORMATION + " (msgID text primary key, msg text)");
			
			/*
			
			db.execSQL("create table "+ TABLE_WORKFLOW + " (createTestDate text primary key, doneTestDate text, sendTestDate text, workflowType integer, modemSerialNumber text, testSucessState integer, workflowdone integer)");
			db.execSQL("create table "+ TABLE_NETWORK_INFORMATION + " (networkInfoID text primary key, workflowID text, latitude text, longitude text, signalStrength integer, bitErrorRate integer, cellID text, lac text, psc integer, MCC text, IMEI text, MCC_MNC text, OperatorName text, NetworkType text, ISOCountry text, SIMSerialNumber text, IMSI text, Roaming text, registryDate text, hasLocation integer)");
			db.execSQL("create table "+ TABLE_NETWORK_INFORMATION_NEIGHBORINGCELL + " (neighboringCelID text primary key, workflowID text, networkInfoID text, cid integer, lac integer, networktype integer, psc integer, rssi integer)");
			db.execSQL("create table "+ TABLE_TEST + " (testID text primary key, workflowID text, moduleID text, waitTimeForResultsInMillisec text, destination text, testType text, msisdn text, IPaddress text)");
			db.execSQL("create table "+ TABLE_MODULE_INFO + " (moduleID text primary key, workflowID text, centralarea text, technology text, phonenumber text, id text, name text)");
			db.execSQL("create table "+ TABLE_SERVICE_RESPONSE + " (serviceResponseID text primary key, workflowID text, testID text, timeoend text, macroresultid text, executionstatus text, servicecode text)");
			db.execSQL("create table "+ TABLE_EXECUTION_RESULTS + " (executionResultsID text primary key, workflowID text, serviceResponseID text, internacionalcode text, value text, taskID text, taskModule text, taskOrderNumber text, idModule text, name text)");
			
			*/
			
		} catch(Exception ex) {
			Logger.v(tag, methodName, LogType.Error, ex.toString());
		}
		
		Logger.v(tag, methodName, LogType.Debug, "Out");
	}
	
	public boolean queueNewMsg(String msg) {
		String methodName = "queueNewMsg";
		
		Logger.v(tag, methodName, LogType.Debug, "In");
		
		try {
			
			String msgID = (new Date()).getTime()+"";
			db.execSQL("INSERT INTO " + TABLE_SEND_INFORMATION + "('msgID', 'msg') values ('" + msgID+ "','"+msg+"')");
			
			Logger.v(tag, methodName, LogType.Debug, "Out with true");
			
			return true;
			
		} catch(Exception ex) {
			Logger.v(tag, methodName, LogType.Error, ex.toString());
		}
		
		Logger.v(tag, methodName, LogType.Debug, "Out");
		
		return false;
	}
	
	
	public boolean removeMsgFromQueue(String id) {
		String methodName = "queueNewMsg";
		
		Logger.v(tag, methodName, LogType.Debug, "In");
		
		try {
			
			db.execSQL("Delete from " + TABLE_SEND_INFORMATION + " where msgID='"+ id +"'");
			
			return true;
			
		} catch(Exception ex) {
			Logger.v(tag, methodName, LogType.Error, ex.toString());
		}
		
		Logger.v(tag, methodName, LogType.Debug, "Out");
		
		return false;
	}
	
	public TreeMap<String, String> getAllMsg() {
		String methodName = "getAllMsg";
		
		TreeMap<String, String> allList = new TreeMap<String, String>();
		
		Logger.v(tag, methodName, LogType.Debug, "In");
		
		try {
			
			Cursor cursor = db.query(TABLE_SEND_INFORMATION, new String[] { "msgID","msg",}, null, null, null, null, "msgID");
			
			if (cursor.moveToFirst()) {
				
				do {
				
				Logger.v(tag, methodName, LogType.Trace, "msgID :"+cursor.getString(0));
				Logger.v(tag, methodName, LogType.Trace, "msg :"+cursor.getString(1));
				
				allList.put(cursor.getString(0), cursor.getString(1));
				
				} while (cursor.moveToNext());
				
			}
			
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
			
		} catch(Exception ex) {
			Logger.v(tag, methodName, LogType.Error, ex.toString());
		}
		
		Logger.v(tag, methodName, LogType.Debug, "Out");
		
		return allList;
	}
	
	
	public void insert(WorkFlowBase workflowObject) {
		String methodName = "insert";
		
		Logger.v(tag, methodName, LogType.Debug, "In");
		
		try {
			
			String workflowID = workflowObject.getCreateTestDate().getTime()+"";
			
			String moduleID = null;
			String executionResultsID = null;
			String serviceResponseID = null;
			String testID = null;
			
			E2EInformationDataStruct test = null;
			E2EInformationDataStruct test2 = null;
			
			// verificar o tipo de test
			switch(workflowObject.getMyWorkFlowType()) {
				case WORKFLOW_CHANGE_SIM_CARD:
					
					Logger.v(tag, methodName, LogType.Debug, "case WORKFLOW_CHANGE_SIM_CARD");
					
					WorkFlowChangeSIMCard workFlowChangeSIMCard = (WorkFlowChangeSIMCard) workflowObject;
					
					// insert base tables (TABLE_MODULE_INFO, TABLE_SERVICE_RESPONSE, TABLE_EXECUTION_RESULTS)
					
					// 1º test
					test = workFlowChangeSIMCard.getFirstE3ETestInformation();
					
					if (test != null) {
						
						testID = (new Date()).getTime()+"";
						
						// TABLE_MODULE_INFO
						Logger.v(tag, methodName, LogType.Debug, "Insert TABLE_MODULE_INFO");
						
						moduleID = (new Date()).getTime()+"";
						TestModuleResponse moduleInformation = test.getModuleInformation();
						db.execSQL("INSERT INTO " + TABLE_MODULE_INFO + "('moduleID', 'workflowID','centralarea','technology', 'phonenumber', 'id', 'name') values ('" + moduleID+ "','" + workflowID+ "','" + moduleInformation.getCentralarea() + "','"+moduleInformation.getTechnology()+"','" + moduleInformation.getPhonenumber() + "','"+moduleInformation.getId()+"','"+moduleInformation.getName()+"')");
						
						// TABLE_SERVICE_RESPONSE
						Logger.v(tag, methodName, LogType.Debug, "Insert TABLE_SERVICE_RESPONSE");
						
						for (ServiceResponse sr : test.getResponseFromServer()) {
							serviceResponseID = (new Date()).getTime()+"";
							
							// TABLE_EXECUTION_RESULTS
							Logger.v(tag, methodName, LogType.Debug, "Insert TABLE_EXECUTION_RESULTS");
							
							for (ExecutionResults es : sr.getExecutionResults()) {
								executionResultsID = (new Date()).getTime()+"";
								db.execSQL("INSERT INTO " + TABLE_EXECUTION_RESULTS + "('executionResultsID', 'workflowID','serviceResponseID','internacionalcode', 'value', 'taskID', 'taskModule', 'taskOrderNumber', 'idModule', 'name') values ('" + executionResultsID+ "','" + workflowID+ "','" + serviceResponseID + "','"+es.getinternacionalcode()+"','" + es.getvalue() + "','"+es.gettaskID()+"','"+es.gettaskModule()+"','"+es.gettaskOrderNumber()+"','"+es.getidModule()+"','"+es.getname()+"')");
							}
							
							db.execSQL("INSERT INTO " + TABLE_SERVICE_RESPONSE + "('serviceResponseID', 'workflowID','testID','timeoend', 'macroresultid', 'executionstatus', 'servicecode') values ('" + serviceResponseID+ "','" + workflowID+ "','" + testID + "','"+sr.getTimeoend()+"','" + sr.getMacroresultid() + "','"+sr.getExecutionstatus()+"','"+sr.getServicecode()+"')");
						}
						
						// insert test table (TABLE_TEST)
						db.execSQL("INSERT INTO " + TABLE_TEST + "('testID', 'workflowID','moduleID','waitTimeForResultsInMillisec', 'destination', 'testType', 'msisdn', 'IPaddress') values ('" + testID+ "','"+workflowID+"','" + moduleID + "','"+test.getWaitTimeForResultsInMillisec()+"','" + test.getDestination() + "','"+test.getTestType()+"','"+test.getMsisdn()+"','"+test.getIPaddress()+"')");
					}
					
					// 2º test
					test2 = workFlowChangeSIMCard.getSecoundE3ETestInformation();
					
					if (test2 != null) {
						
						testID = (new Date()).getTime()+"";
						
						// TABLE_MODULE_INFO
						Logger.v(tag, methodName, LogType.Debug, "Insert TABLE_MODULE_INFO");
						
						moduleID = (new Date()).getTime()+"";
						TestModuleResponse moduleInformation = test2.getModuleInformation();
						db.execSQL("INSERT INTO " + TABLE_MODULE_INFO + "('moduleID', 'workflowID','centralarea','technology', 'phonenumber', 'id', 'name') values ('" + moduleID+ "','"+workflowID+"','" + moduleInformation.getCentralarea() + "','"+moduleInformation.getTechnology()+"','" + moduleInformation.getPhonenumber() + "','"+moduleInformation.getId()+"','"+moduleInformation.getName()+"')");
						
						// TABLE_SERVICE_RESPONSE
						Logger.v(tag, methodName, LogType.Debug, "Insert TABLE_SERVICE_RESPONSE");
						
						for (ServiceResponse sr : test2.getResponseFromServer()) {
							serviceResponseID = (new Date()).getTime()+"";
							
							// TABLE_EXECUTION_RESULTS
							Logger.v(tag, methodName, LogType.Debug, "Insert TABLE_EXECUTION_RESULTS");
							
							for (ExecutionResults es : sr.getExecutionResults()) {
								executionResultsID = (new Date()).getTime()+"";
								db.execSQL("INSERT INTO " + TABLE_EXECUTION_RESULTS + "('executionResultsID', 'workflowID','serviceResponseID','internacionalcode', 'value', 'taskID', 'taskModule', 'taskOrderNumber', 'idModule', 'name') values ('" + executionResultsID+ "','"+workflowID+"','" + serviceResponseID + "','"+es.getinternacionalcode()+"','" + es.getvalue() + "','"+es.gettaskID()+"','"+es.gettaskModule()+"','"+es.gettaskOrderNumber()+"','"+es.getidModule()+"','"+es.getname()+"')");
							}
							
							db.execSQL("INSERT INTO " + TABLE_SERVICE_RESPONSE + "('serviceResponseID', 'workflowID','testID','timeoend', 'macroresultid', 'executionstatus', 'servicecode') values ('" + serviceResponseID+ "','"+workflowID+"','" + testID + "','"+sr.getTimeoend()+"','" + sr.getMacroresultid() + "','"+sr.getExecutionstatus()+"','"+sr.getServicecode()+"')");
						}
						
						// insert test table (TABLE_TEST)
						db.execSQL("INSERT INTO " + TABLE_TEST + "('testID', 'workflowID','moduleID','waitTimeForResultsInMillisec', 'destination', 'testType', 'msisdn', 'IPaddress') values ('" + testID+ "','"+workflowID+"','" + moduleID + "','"+test2.getWaitTimeForResultsInMillisec()+"','" + test2.getDestination() + "','"+test2.getTestType()+"','"+test2.getMsisdn()+"','"+test2.getIPaddress()+"')");
					}
					
					break;
				case WORKFLOW_INSERT_SIM_CARD:
					// apena tem um test
					
					Logger.v(tag, methodName, LogType.Debug, "case WORKFLOW_INSERT_SIM_CARD");
					
					WorkFlowInsertSIMCard workFlowInsertSIMCard = (WorkFlowInsertSIMCard) workflowObject;
					
					// 1º test
					test = workFlowInsertSIMCard.getE2ETestInformation();
					
					if (test != null) {
						
						testID = (new Date()).getTime()+"";
						
						// TABLE_MODULE_INFO
						Logger.v(tag, methodName, LogType.Debug, "Insert TABLE_MODULE_INFO");
						
						moduleID = (new Date()).getTime()+"";
						TestModuleResponse moduleInformation = test.getModuleInformation();
						db.execSQL("INSERT INTO " + TABLE_MODULE_INFO + "('moduleID', 'workflowID','centralarea','technology', 'phonenumber', 'id', 'name') values ('" + moduleID+ "','"+workflowID+"','" + moduleInformation.getCentralarea() + "','"+moduleInformation.getTechnology()+"','" + moduleInformation.getPhonenumber() + "','"+moduleInformation.getId()+"','"+moduleInformation.getName()+"')");
						
						// TABLE_SERVICE_RESPONSE
						Logger.v(tag, methodName, LogType.Debug, "Insert TABLE_SERVICE_RESPONSE");
						
						for (ServiceResponse sr : test.getResponseFromServer()) {
							serviceResponseID = (new Date()).getTime()+"";
							
							// TABLE_EXECUTION_RESULTS
							Logger.v(tag, methodName, LogType.Debug, "Insert TABLE_EXECUTION_RESULTS");
							
							for (ExecutionResults es : sr.getExecutionResults()) {
								executionResultsID = (new Date()).getTime()+"";
								db.execSQL("INSERT INTO " + TABLE_EXECUTION_RESULTS + "('executionResultsID', 'workflowID','serviceResponseID','internacionalcode', 'value', 'taskID', 'taskModule', 'taskOrderNumber', 'idModule', 'name') values ('" + executionResultsID+ "','"+workflowID+"','" + serviceResponseID + "','"+es.getinternacionalcode()+"','" + es.getvalue() + "','"+es.gettaskID()+"','"+es.gettaskModule()+"','"+es.gettaskOrderNumber()+"','"+es.getidModule()+"','"+es.getname()+"')");
							}
							
							db.execSQL("INSERT INTO " + TABLE_SERVICE_RESPONSE + "('serviceResponseID', 'workflowID','testID','timeoend', 'macroresultid', 'executionstatus', 'servicecode') values ('" + serviceResponseID+ "','"+workflowID+"','" + testID + "','"+sr.getTimeoend()+"','" + sr.getMacroresultid() + "','"+sr.getExecutionstatus()+"','"+sr.getServicecode()+"')");
						}
						
						// insert test table (TABLE_TEST)
						db.execSQL("INSERT INTO " + TABLE_TEST + "('testID', 'workflowID','moduleID','waitTimeForResultsInMillisec', 'destination', 'testType', 'msisdn', 'IPaddress') values ('" + testID+ "','" +workflowID+ "','" + moduleID + "','"+test.getWaitTimeForResultsInMillisec()+"','" + test.getDestination() + "','"+test.getTestType()+"','"+test.getMsisdn()+"','"+test.getIPaddress()+"')");
					}
					
					break;
				case WORKFLOW_INSERT_TEST_CONNECTIVITY:
					// apena tem um test
					
					Logger.v(tag, methodName, LogType.Debug, "case WORKFLOW_INSERT_TEST_CONNECTIVITY");
					
					WorkFlowTestConnectivity workFlowTestConnectivity = (WorkFlowTestConnectivity) workflowObject;
					
					// 1º test
					test = workFlowTestConnectivity.getE2ETestInformation();
					
					if (test != null) {
						
						testID = (new Date()).getTime()+"";
						
						// TABLE_MODULE_INFO
						Logger.v(tag, methodName, LogType.Debug, "Insert TABLE_MODULE_INFO");
						
						moduleID = (new Date()).getTime()+"";
						TestModuleResponse moduleInformation = test.getModuleInformation();
						db.execSQL("INSERT INTO " + TABLE_MODULE_INFO + "('moduleID', 'workflowID','centralarea','technology', 'phonenumber', 'id', 'name') values ('" + moduleID+ "','" +workflowID+ "','" + moduleInformation.getCentralarea() + "','"+moduleInformation.getTechnology()+"','" + moduleInformation.getPhonenumber() + "','"+moduleInformation.getId()+"','"+moduleInformation.getName()+"')");
						
						// TABLE_SERVICE_RESPONSE
						Logger.v(tag, methodName, LogType.Debug, "Insert TABLE_SERVICE_RESPONSE");
						
						for (ServiceResponse sr : test.getResponseFromServer()) {
							serviceResponseID = (new Date()).getTime()+"";
							
							// TABLE_EXECUTION_RESULTS
							Logger.v(tag, methodName, LogType.Debug, "Insert TABLE_EXECUTION_RESULTS");
							
							for (ExecutionResults es : sr.getExecutionResults()) {
								executionResultsID = (new Date()).getTime()+"";
								db.execSQL("INSERT INTO " + TABLE_EXECUTION_RESULTS + "('executionResultsID', 'workflowID','serviceResponseID','internacionalcode', 'value', 'taskID', 'taskModule', 'taskOrderNumber', 'idModule', 'name') values ('" + executionResultsID+ "','" +workflowID+ "','" + serviceResponseID + "','"+es.getinternacionalcode()+"','" + es.getvalue() + "','"+es.gettaskID()+"','"+es.gettaskModule()+"','"+es.gettaskOrderNumber()+"','"+es.getidModule()+"','"+es.getname()+"')");
							}
							
							db.execSQL("INSERT INTO " + TABLE_SERVICE_RESPONSE + "('serviceResponseID', 'workflowID','testID','timeoend', 'macroresultid', 'executionstatus', 'servicecode') values ('" + serviceResponseID+ "','" +workflowID+ "','" + testID + "','"+sr.getTimeoend()+"','" + sr.getMacroresultid() + "','"+sr.getExecutionstatus()+"','"+sr.getServicecode()+"')");
						}
						
						// insert test table (TABLE_TEST)
						db.execSQL("INSERT INTO " + TABLE_TEST + "('testID', 'workflowID','moduleID','waitTimeForResultsInMillisec', 'destination', 'testType', 'msisdn', 'IPaddress') values ('" + testID+ "','" +workflowID+ "','" + moduleID + "','"+test.getWaitTimeForResultsInMillisec()+"','" + test.getDestination() + "','"+test.getTestType()+"','"+test.getMsisdn()+"','"+test.getIPaddress()+"')");
					}
					
					break;
			}
		
			// verifica se exite informação das celulas vizinha no objecto do workflow
			NetworkInformationDataStruct networkMetricsInformation = workflowObject.getNetworkMetricsInformation();
			
			String networkInfoID = "";
			if (networkMetricsInformation != null) {
				String neighboringCelID = null;
				networkInfoID = (new Date()).getTime()+"";
				
				// insert TABLE_NETWORK_INFORMATION_NEIGHBORINGCELL
				Logger.v(tag, methodName, LogType.Debug, " insert TABLE_NETWORK_INFORMATION_NEIGHBORINGCELL");
				for (MyNeighboringCellInfo nci : networkMetricsInformation.getNeighboringCellInfo()) {
					neighboringCelID = (new Date()).getTime()+"";
					db.execSQL("INSERT INTO " + TABLE_NETWORK_INFORMATION_NEIGHBORINGCELL + "('neighboringCelID', 'workflowID','networkInfoID','cid', 'lac', 'networktype', 'psc', 'rssi') values ('" + neighboringCelID+ "','" +workflowID+ "','" + networkInfoID + "','"+nci.getCid()+"','" + nci.getLac() + "','"+nci.getNetworkType()+"','"+nci.getPsc()+"','"+nci.getRssi()+"')");
				}
				
				// insert TABLE_NETWORK_INFORMATION
				Logger.v(tag, methodName, LogType.Debug, " insert TABLE_NETWORK_INFORMATION");
				db.execSQL("INSERT INTO " + TABLE_NETWORK_INFORMATION + "('networkInfoID','workflowID','latitude', 'longitude', 'signalStrength', 'bitErrorRate', 'cellID', 'lac', 'psc', 'MCC', 'IMEI'," + " 'MCC_MNC', 'OperatorName', 'NetworkType', 'ISOCountry', 'SIMSerialNumber', 'IMSI', 'Roaming', 'registryDate', 'hasLocation') values ('" + networkInfoID+ "','" + workflowID + "','"+networkMetricsInformation.getLatitude()+"','" + networkMetricsInformation.getLongitude() + "','"+networkMetricsInformation.getGsmSignalStrength()+"','"+networkMetricsInformation.getGsmBitErrorRate()+"','"+networkMetricsInformation.getGsmCellID()+"','"+networkMetricsInformation.getGsmLac()+"','"+networkMetricsInformation.getGsmPsc()+"','"+networkMetricsInformation.getMCC()+"','"+networkMetricsInformation.getIMEI()+"','"+networkMetricsInformation.getMCC_MNC()+"','"+networkMetricsInformation.getOperatorName()+"','"+networkMetricsInformation.getNetworkType()+"','"+networkMetricsInformation.getISOCountry()+"','"+networkMetricsInformation.getSIMSerialNumber()+"','"+networkMetricsInformation.getIMSI()+"','" +networkMetricsInformation.getRoaming()+"','"+networkMetricsInformation.getRegistryDate()+"','"+(networkMetricsInformation.hasLocation()?1:0)+"')");
			}
		
			// insert TABLE_WORKFLOW
			db.execSQL("INSERT INTO " + TABLE_WORKFLOW + "('createTestDate','doneTestDate','sendTestDate', 'workflowType', 'modemSerialNumber', 'testSucessState', 'workflowdone') values ('" + workflowID+ "','" + workflowObject.getTestDoneDate().getTime() + "','"+workflowObject.getSendTestDate().getTime()+"','" + Util.ConvertWorkflowTypeToInt(workflowObject.getMyWorkFlowType()) + "','"+workflowObject.getModemSerialNumber()+"','"+Util.ConvertBooleanToInt(workflowObject.getWorkflowSucessState())+"','"+Util.ConvertBooleanToInt(workflowObject.getWorkflowDoneState())+"')");
			
		} catch(Exception ex) {
			Logger.v(tag, methodName, LogType.Error, ex.toString());
		}
		
		Logger.v(tag, methodName, LogType.Debug, "Out");
	}
	
	public boolean remove(String uniqueWorkFlowId) {
		String methodName = "remove";
		
		Logger.v(tag, methodName, LogType.Debug, "In");
		
		try {
			
			
			return true;
			
		} catch(Exception ex) {
			Logger.v(tag, methodName, LogType.Error, ex.toString());
		}
		
		Logger.v(tag, methodName, LogType.Debug, "Out");
		
		return false;
	}
	
	private boolean removeFromTable(String table, String uniqueId) {
		String methodName = "removeFromTable";
		
		Logger.v(tag, methodName, LogType.Debug, "In");
		
		try {
			
			db.execSQL("Delete from " + table + " where workflowID='"+ uniqueId +"'");
			
			return true;
			
		} catch(Exception ex) {
			Logger.v(tag, methodName, LogType.Error, ex.toString());
		}
		
		Logger.v(tag, methodName, LogType.Debug, "Out");
		
		return false;
	}
	
	private String ConvertTableToField(String table) {
		
		if (table.equals(TABLE_WORKFLOW)) {
			return "createTestDate";
		} else if (table.equals(TABLE_NETWORK_INFORMATION)) {
			return "workflowID";
		} else if (table.equals(TABLE_NETWORK_INFORMATION_NEIGHBORINGCELL)) {
			return "networkInfoID";
		} else if (table.equals(TABLE_TEST)) {
			return "workflowID";
		} else if (table.equals(TABLE_MODULE_INFO)) {
			return "moduleID";
		} else if (table.equals(TABLE_SERVICE_RESPONSE)) {
			return "testID";
		} else if (table.equals(TABLE_EXECUTION_RESULTS)) {
			return "serviceResponseID";
		}
		
		return "NA";
	}
	
	public List<WorkFlowBase> getAllItems() {
		String methodName = "getAllItems";
		
		ArrayList<WorkFlowBase> result = new  ArrayList<WorkFlowBase>();
		
		Logger.v(tag, methodName, LogType.Debug, "In");
		
		try {
			
			
			
			
		} catch(Exception ex) {
			Logger.v(tag, methodName, LogType.Error, ex.toString());
		}
		
		Logger.v(tag, methodName, LogType.Debug, "Out");
		
		
		return result;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String methodName = "onUpgrade";
		
		Logger.v(tag, methodName, LogType.Debug, "In");
	}
	
}

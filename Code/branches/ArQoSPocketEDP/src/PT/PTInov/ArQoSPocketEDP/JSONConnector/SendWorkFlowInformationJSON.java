package PT.PTInov.ArQoSPocketEDP.JSONConnector;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.Date;

import PT.PTInov.ArQoSPocketEDP.DataStructs.EnumEndPoint;
import PT.PTInov.ArQoSPocketEDP.DataStructs.EnumNetworkMetrics;
import PT.PTInov.ArQoSPocketEDP.DataStructs.EnumSendInformationResult;
import PT.PTInov.ArQoSPocketEDP.DataStructs.EnumTestE2EState;
import PT.PTInov.ArQoSPocketEDP.DataStructs.NetworkInformationDataStruct;
import PT.PTInov.ArQoSPocketEDP.DataStructs.WorkFlowBase;
import PT.PTInov.ArQoSPocketEDP.DataStructs.WorkFlowChangeSIMCard;
import PT.PTInov.ArQoSPocketEDP.DataStructs.WorkFlowInsertSIMCard;
import PT.PTInov.ArQoSPocketEDP.DataStructs.WorkFlowTestConnectivity;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.EnumParamTest;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.ExecutionResults;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.Pair;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.Tuplo;
import PT.PTInov.ArQoSPocketEDP.Utils.EnumTestType;
import PT.PTInov.ArQoSPocketEDP.Utils.LogType;
import PT.PTInov.ArQoSPocketEDP.Utils.Logger;
import PT.PTInov.ArQoSPocketEDP.Utils.Utils;

public class SendWorkFlowInformationJSON extends JSONConnector {

	private final static String tag = "SendWorkFlowInformationJSON";

	private String getTestServerMethodName = null;
	private String connectHost = null;
	
	private final static int eventType = 10;

	public SendWorkFlowInformationJSON(String phost, String pGetTestServerMethodName) {

		super(phost + pGetTestServerMethodName);

		getTestServerMethodName = pGetTestServerMethodName;
		connectHost = phost;
	}
	
	private boolean InternalErroChecker(String testInfoState) {
		
		try {
			
			if (testInfoState.contains("NOK: M100000 ERRO_PROGRAMACAO_TAREFA")
					|| testInfoState.contains("Erro de acesso")
					|| testInfoState.contains("GENERIC_ERROR")
					|| testInfoState.contains("ERRO_GENERICO"))
				return true;
				
			
		} catch(Exception ex) {
			Logger.v(tag, LogType.Trace, "Error :" + ex.toString());
		}
		
		return false;
	}

	@SuppressLint("ParserError")
	public Pair sendInformationJSONToServer(WorkFlowBase workflowToSend, String DeviceID, String Comment, String modelo_selo, String resultDetails, NetworkInformationDataStruct sendMetrics) {
		
		//boolean has1test = false;
		boolean has2test = false; 
		
		Logger.v(tag, LogType.Trace, "1");
		
		EnumEndPoint endpointEnum = EnumEndPoint.NA;
		boolean hasNetworkMetrics = false;
		
		String NetworkType = "NA";
		String module = "1";
		String timestamp = Utils.convertDate(workflowToSend.getCreateTestDate())+"";
		String mac = "00:00:00:00";
		
		Logger.v(tag, LogType.Trace, "2");
		
		// medidas no acto de envio do teste para o servidor
		String mode = "0";
		
		if (sendMetrics.getNetworkType().contains("GPRS")) {
			mode = "0";
		} else if (sendMetrics.getNetworkType().contains("EDGE")) {
			mode = "1";
		} else if (sendMetrics.getNetworkType().contains("HSDPA")) {
			mode = "3";
		} else if (sendMetrics.getNetworkType().contains("HSPA")) {
			mode = "5";
		} else if (sendMetrics.getNetworkType().contains("HSUPA")) {
			mode = "4";
		} else if (sendMetrics.getNetworkType().contains("UMTS")) {
			mode = "2";
		}
		
		String roaming =  (sendMetrics.getRoaming().contains("im") ? "1" : "0");
		String rssi = sendMetrics.getGsmSignalStrength()+"";
		String GsmCellID = sendMetrics.getGsmCellID();
		String OperatorName = sendMetrics.getOperatorName();
		String MCC_MNC = sendMetrics.getMCC_MNC();
		String GsmPsc = sendMetrics.getGsmPsc()+"";
		String GsmBitErrorRate = sendMetrics.getGsmBitErrorRate()+"";
		String GsmLac = sendMetrics.getGsmLac();
		String Latitude = sendMetrics.getLatitude();
		String Longitude = sendMetrics.getLongitude();
		
		Logger.v(tag, LogType.Trace, "3");
		
		// informação do workflow
		String activity_action = null;
		switch(workflowToSend.getMyWorkFlowType()) {
			case WORKFLOW_CHANGE_SIM_CARD:
				activity_action = "Trocar SIM";
				break;
			case WORKFLOW_INSERT_SIM_CARD:
				activity_action = "Novo SIM";
				break;
			case WORKFLOW_INSERT_TEST_CONNECTIVITY:
				activity_action = "Testar Conectividade";
				break;
		}
		
		Logger.v(tag, LogType.Trace, "4");
		
		String METTER_SERIAL = workflowToSend.getModemSerialNumber();
		
		// gps quando foi feito o teste
		String FINAL_GPS_INFO = "0.0 0.0 0.0 0.0";
		if (workflowToSend.getLocation()!=null)
			FINAL_GPS_INFO = workflowToSend.getLocation().getLatitude() + " " + workflowToSend.getLocation().getLongitude() +" 0.0 0.0";
		
		String COMMENT = Comment;
		
		Logger.v(tag, LogType.Trace, "5");
		
		String localWorkFlowLatitude = "";
		String localWorkFlowLongitude = "";
		String NET_MEASURES_1_GPS = "NA";
		String NET_MEASURES_1_RSSI = "NA";
		String NET_MEASURES_1_LAC_TAC = "NA";
		String NET_MEASURES_1_CELLID = "NA";
		String NET_MEASURES_1_OPERATORNAME = "NA";
		String NET_MEASURES_1_LINK = "NA";
		String NET_MEASURES_1_IMEI = "NA";
		String NET_MEASURES_1_IMSI = "NA";
		
		if (workflowToSend.getNetworkMetricsState() != EnumNetworkMetrics.NA) {
			
			hasNetworkMetrics = true;

			localWorkFlowLatitude = workflowToSend
					.getNetworkMetricsInformation().getLatitude();
			localWorkFlowLongitude = workflowToSend
					.getNetworkMetricsInformation().getLongitude();

			Logger.v(tag, LogType.Trace, "6");

			NET_MEASURES_1_GPS = localWorkFlowLatitude + " "
					+ localWorkFlowLongitude + " 0.0 0.0";
			NET_MEASURES_1_RSSI = workflowToSend
					.getNetworkMetricsInformation().getGsmSignalStrength()
					+ " dBm";
			NET_MEASURES_1_LAC_TAC = workflowToSend
					.getNetworkMetricsInformation().getGsmLac() + "";
			NET_MEASURES_1_CELLID = workflowToSend
					.getNetworkMetricsInformation().getGsmCellID();
			NET_MEASURES_1_OPERATORNAME = workflowToSend
					.getNetworkMetricsInformation().getOperatorName();
			NET_MEASURES_1_LINK = workflowToSend
					.getNetworkMetricsInformation().getNetworkType();
			NET_MEASURES_1_IMEI = workflowToSend
					.getNetworkMetricsInformation().getIMEI();
			NET_MEASURES_1_IMSI = workflowToSend
					.getNetworkMetricsInformation().getIMSI();
		}
		
		Logger.v(tag, LogType.Trace, "7");
		
		String E2E_1_RESULT = "NA";
		String E2E_1_CAUSE = "NA";
		String E2E_1_NETWORK = "NA";
		String E2E_1_TYPE = "NA";  //<- nome da macro que vou chamar
		String E2E_1_MSISDN = "NA";
		String E2E_1_IP = "NA";
		String E2E_1_IP_PKT_TX = "NA";
		String E2E_1_IP_PKT_RX = "NA";
		String E2E_1_IP_RTD_MAX = "NA";
		String E2E_1_IP_RTD_AVG = "NA";
		String E2E_1_IP_LOSS = "NA";
		String E2E_1_SMS_MODEM_RESPONSE = "NA";
		
		Logger.v(tag, LogType.Trace, "8");
		
		String E2E_2_RESULT = "NA";
		String E2E_2_CAUSE = "NA";
		String E2E_2_NETWORK = "NA";
		String E2E_2_TYPE = "NA";
		String E2E_2_MSISDN = "NA";
		String E2E_2_IP = "NA";
		String E2E_2_IP_PKT_TX = "NA";
		String E2E_2_IP_PKT_RX = "NA";
		String E2E_2_IP_RTD_MAX = "NA";
		String E2E_2_IP_RTD_AVG = "NA";
		String E2E_2_IP_LOSS = "NA";
		String E2E_2_SMS_MODEM_RESPONSE = "NA";
		
		Logger.v(tag, LogType.Trace, "9");
		
		String SIM_INIT = "NA";
		String SIM_FINAL = "NA";
		
		Logger.v(tag, LogType.Trace, "10");
		
		switch(workflowToSend.getMyWorkFlowType()) {
		case WORKFLOW_CHANGE_SIM_CARD:
			// Tem os dois testes
			
			WorkFlowChangeSIMCard workflowSpecific = (WorkFlowChangeSIMCard) workflowToSend;
			
			Logger.v(tag, LogType.Trace, "11");

			if (workflowSpecific.getFirstE3ETestState() != EnumTestE2EState.NA) {

				String moduleFirstTest = workflowSpecific
						.getFirstE3ETestInformation().getModuleInformation()
						.getId();
				ArrayList<ExecutionResults> resultFirstTest = workflowSpecific
						.getFirstE3ETestInformation().getAllExecutionResults();

				Logger.v(tag, LogType.Trace, "12");
				
				EnumTestType E2E_1_testType = convertTestTypeStringToTestTypeEnum(workflowSpecific.getFirstE3ETestInformation().getTestType());

				E2E_1_RESULT = getValueFromList(EnumParamTest.E2E_RESULT,
						moduleFirstTest, resultFirstTest, E2E_1_testType);
				E2E_1_CAUSE = getValueFromList(EnumParamTest.E2E_CAUSE,
						moduleFirstTest, resultFirstTest, E2E_1_testType);
				E2E_1_NETWORK = getValueFromList(EnumParamTest.E2E_NETWORK,
						moduleFirstTest, resultFirstTest, E2E_1_testType);
				E2E_1_TYPE = workflowSpecific.getFirstE3ETestInformation()
						.getTestType();
				E2E_1_MSISDN = workflowSpecific.getFirstE3ETestInformation()
						.getMsisdn();
				E2E_1_IP = workflowSpecific.getFirstE3ETestInformation()
						.getIPaddress();
				E2E_1_IP_PKT_TX = getValueFromList(EnumParamTest.E2E_IP_PKT_TX,
						moduleFirstTest, resultFirstTest, E2E_1_testType);
				E2E_1_IP_PKT_RX = getValueFromList(EnumParamTest.E2E_IP_PKT_RX,
						moduleFirstTest, resultFirstTest, E2E_1_testType);
				
				String LOCAL_E2E_1_IP_RTD_MAX = getValueFromList(EnumParamTest.E2E_IP_RTD_MAX, moduleFirstTest,resultFirstTest, E2E_1_testType);
				E2E_1_IP_RTD_MAX = (LOCAL_E2E_1_IP_RTD_MAX.equals("NA"))?LOCAL_E2E_1_IP_RTD_MAX:LOCAL_E2E_1_IP_RTD_MAX+" ms";
				
				String LOCAL_E2E_1_IP_RTD_AVG = getValueFromList(EnumParamTest.E2E_IP_RTD_AVG, moduleFirstTest,resultFirstTest, E2E_1_testType);
				E2E_1_IP_RTD_AVG = (LOCAL_E2E_1_IP_RTD_AVG.equals("NA"))?LOCAL_E2E_1_IP_RTD_AVG:LOCAL_E2E_1_IP_RTD_AVG+ " ms";
				
				String LOCAL_E2E_1_IP_LOSS = getValueFromList(EnumParamTest.E2E_IP_LOSS,moduleFirstTest, resultFirstTest, E2E_1_testType);
				E2E_1_IP_LOSS = (LOCAL_E2E_1_IP_LOSS.equals("NA"))?(LOCAL_E2E_1_IP_LOSS):(LOCAL_E2E_1_IP_LOSS + " %");
				
				E2E_1_SMS_MODEM_RESPONSE = getValueFromList(
						EnumParamTest.E2E_SMS_MODEM_RESPONSE, moduleFirstTest,
						resultFirstTest, E2E_1_testType);
				
				SIM_INIT = workflowSpecific.getFirstE3ETestInformation()
						.getMsisdn()
						+ "/"
						+ workflowSpecific.getFirstE3ETestInformation()
								.getIPaddress();
				if (InternalErroChecker(E2E_2_RESULT))
					endpointEnum = EnumEndPoint.END_POINT_5;
				else if (!E2E_2_RESULT.contains("NOK"))
					endpointEnum = EnumEndPoint.END_POINT_2;
				else
					endpointEnum = EnumEndPoint.END_POINT_1;

			} else {
				
					endpointEnum = EnumEndPoint.END_POINT_1;
			}
			
			Logger.v(tag, LogType.Trace, "13");
			
			if (workflowSpecific.getSecoundE3ETestState() != EnumTestE2EState.NA && workflowSpecific.getSecoundE3ETestInformation() != null) {
				
				has2test = true;

				String moduleSecondTest = workflowSpecific
						.getSecoundE3ETestInformation().getModuleInformation()
						.getId();
				ArrayList<ExecutionResults> resultSecondTest = workflowSpecific
						.getSecoundE3ETestInformation()
						.getAllExecutionResults();

				Logger.v(tag, LogType.Trace, "14");
				
				EnumTestType E2E_2_testType = convertTestTypeStringToTestTypeEnum(workflowSpecific.getSecoundE3ETestInformation().getTestType());

				E2E_2_RESULT = getValueFromList(EnumParamTest.E2E_RESULT,
						moduleSecondTest, resultSecondTest, E2E_2_testType);
				E2E_2_CAUSE = getValueFromList(EnumParamTest.E2E_CAUSE,
						moduleSecondTest, resultSecondTest, E2E_2_testType);
				E2E_2_NETWORK = getValueFromList(EnumParamTest.E2E_NETWORK,
						moduleSecondTest, resultSecondTest, E2E_2_testType);
				E2E_2_TYPE = workflowSpecific.getSecoundE3ETestInformation()
						.getTestType();
				E2E_2_MSISDN = workflowSpecific.getSecoundE3ETestInformation()
						.getMsisdn();
				E2E_2_IP = workflowSpecific.getSecoundE3ETestInformation()
						.getIPaddress();
				E2E_2_IP_PKT_TX = getValueFromList(EnumParamTest.E2E_IP_PKT_TX,
						moduleSecondTest, resultSecondTest, E2E_2_testType);
				E2E_2_IP_PKT_RX = getValueFromList(EnumParamTest.E2E_IP_PKT_RX,
						moduleSecondTest, resultSecondTest, E2E_2_testType);
				
				String LOCAL_E2E_2_IP_RTD_MAX = getValueFromList(EnumParamTest.E2E_IP_RTD_MAX, moduleSecondTest, resultSecondTest, E2E_2_testType);
				E2E_2_IP_RTD_MAX = (LOCAL_E2E_2_IP_RTD_MAX.equals("NA"))?LOCAL_E2E_2_IP_RTD_MAX:LOCAL_E2E_2_IP_RTD_MAX+" ms";
				
				String LOCAL_E2E_2_IP_RTD_AVG = getValueFromList(EnumParamTest.E2E_IP_RTD_AVG, moduleSecondTest, resultSecondTest, E2E_2_testType);
				E2E_2_IP_RTD_AVG = (LOCAL_E2E_2_IP_RTD_AVG.equals("NA"))?LOCAL_E2E_2_IP_RTD_AVG:LOCAL_E2E_2_IP_RTD_AVG+ " ms";
				
				String LOCAL_E2E_2_IP_LOSS = getValueFromList(EnumParamTest.E2E_IP_LOSS,moduleSecondTest, resultSecondTest, E2E_2_testType);
				E2E_2_IP_LOSS = (LOCAL_E2E_2_IP_LOSS.equals("NA"))?(LOCAL_E2E_2_IP_LOSS):(LOCAL_E2E_2_IP_LOSS + " %");
				
				E2E_2_SMS_MODEM_RESPONSE = getValueFromList(
						EnumParamTest.E2E_SMS_MODEM_RESPONSE, moduleSecondTest,
						resultSecondTest, E2E_2_testType);

				Logger.v(tag, LogType.Trace, "15");

				
				SIM_FINAL = workflowSpecific.getSecoundE3ETestInformation()
						.getMsisdn()
						+ "/"
						+ workflowSpecific.getSecoundE3ETestInformation()
								.getIPaddress();
				
				Logger.v(tag, LogType.Trace, "20.1 - E2E_2_RESULT :"+E2E_2_RESULT);
				
				if (InternalErroChecker(E2E_2_RESULT))
					endpointEnum = EnumEndPoint.END_POINT_5;
				else if (!E2E_2_RESULT.contains("NOK"))
					endpointEnum = EnumEndPoint.END_POINT_3;
				else
					endpointEnum = EnumEndPoint.END_POINT_4;

			} else {
				
				if (hasNetworkMetrics == true)
					endpointEnum = EnumEndPoint.END_POINT_2;
			}
			
			Logger.v(tag, LogType.Trace, "16");
			
			break;
		case WORKFLOW_INSERT_SIM_CARD:
			
			WorkFlowInsertSIMCard workflowSpecificInsert = (WorkFlowInsertSIMCard) workflowToSend;

			Logger.v(tag, LogType.Trace, "17");

			if (workflowSpecificInsert.getE3ETestState() != EnumTestE2EState.NA) {
				
				String moduleFirstTest = workflowSpecificInsert
						.getE2ETestInformation().getModuleInformation().getId();
				ArrayList<ExecutionResults> resultFirstTest = workflowSpecificInsert
						.getE2ETestInformation().getAllExecutionResults();

				Logger.v(tag, LogType.Trace, "18");
				
				EnumTestType E2E_1_testType = convertTestTypeStringToTestTypeEnum(workflowSpecificInsert.getE2ETestInformation().getTestType());

				E2E_1_RESULT = getValueFromList(EnumParamTest.E2E_RESULT,
						moduleFirstTest, resultFirstTest, E2E_1_testType);
				E2E_1_CAUSE = getValueFromList(EnumParamTest.E2E_CAUSE,
						moduleFirstTest, resultFirstTest, E2E_1_testType);
				E2E_1_NETWORK = getValueFromList(EnumParamTest.E2E_NETWORK,
						moduleFirstTest, resultFirstTest, E2E_1_testType);
				E2E_1_TYPE = workflowSpecificInsert.getE2ETestInformation()
						.getTestType();
				E2E_1_MSISDN = workflowSpecificInsert.getE2ETestInformation()
						.getMsisdn();
				E2E_1_IP = workflowSpecificInsert.getE2ETestInformation()
						.getIPaddress();
				E2E_1_IP_PKT_TX = getValueFromList(EnumParamTest.E2E_IP_PKT_TX,
						moduleFirstTest, resultFirstTest, E2E_1_testType);
				E2E_1_IP_PKT_RX = getValueFromList(EnumParamTest.E2E_IP_PKT_RX,
						moduleFirstTest, resultFirstTest, E2E_1_testType);
				
				String LOCAL_E2E_1_IP_RTD_MAX = getValueFromList(EnumParamTest.E2E_IP_RTD_MAX, moduleFirstTest,resultFirstTest, E2E_1_testType);
				E2E_1_IP_RTD_MAX = (LOCAL_E2E_1_IP_RTD_MAX.equals("NA"))?LOCAL_E2E_1_IP_RTD_MAX:LOCAL_E2E_1_IP_RTD_MAX+" ms";
				
				String LOCAL_E2E_1_IP_RTD_AVG = getValueFromList(EnumParamTest.E2E_IP_RTD_AVG, moduleFirstTest,resultFirstTest, E2E_1_testType);
				E2E_1_IP_RTD_AVG = (LOCAL_E2E_1_IP_RTD_AVG.equals("NA"))?LOCAL_E2E_1_IP_RTD_AVG:LOCAL_E2E_1_IP_RTD_AVG+ " ms";
				
				String LOCAL_E2E_1_IP_LOSS = getValueFromList(EnumParamTest.E2E_IP_LOSS,moduleFirstTest, resultFirstTest, E2E_1_testType);
				E2E_1_IP_LOSS = (LOCAL_E2E_1_IP_LOSS.equals("NA"))?(LOCAL_E2E_1_IP_LOSS):(LOCAL_E2E_1_IP_LOSS + " %");
				
				E2E_1_SMS_MODEM_RESPONSE = getValueFromList(
						EnumParamTest.E2E_SMS_MODEM_RESPONSE, moduleFirstTest,
						resultFirstTest, E2E_1_testType);
				
				SIM_FINAL = workflowSpecificInsert.getE2ETestInformation()
						.getMsisdn()
						+ "/"
						+ workflowSpecificInsert.getE2ETestInformation()
								.getIPaddress();
				
				Logger.v(tag, LogType.Trace, "20.1 - E2E_1_RESULT :"+E2E_1_RESULT);
				
				if (InternalErroChecker(E2E_1_RESULT))
					endpointEnum = EnumEndPoint.END_POINT_5;
				else if (!E2E_1_RESULT.contains("NOK"))
					endpointEnum = EnumEndPoint.END_POINT_3;
				else
					endpointEnum = EnumEndPoint.END_POINT_4;
				
			} else {
				if (hasNetworkMetrics == true)
					endpointEnum = EnumEndPoint.END_POINT_2;
			}

			break;
		case WORKFLOW_INSERT_TEST_CONNECTIVITY:
			
			WorkFlowTestConnectivity workflowSpecificConnectivity = (WorkFlowTestConnectivity) workflowToSend;

			Logger.v(tag, LogType.Trace, "19");

			if (workflowSpecificConnectivity.getE2ETestState() != EnumTestE2EState.NA) {
				
				String moduleFirstTest = workflowSpecificConnectivity
						.getE2ETestInformation().getModuleInformation().getId();
				//ArrayList<ExecutionResults> resultFirstTest = workflowSpecificConnectivity.getE2ETestInformation().getAllExecutionResults();
				ArrayList<ExecutionResults> resultFirstTest = workflowSpecificConnectivity.getE2ETestInformation().getFinalOKServeResponse().getExecutionResults();
				
				Logger.v(tag, LogType.Trace, "resultFirstTest :"+resultFirstTest.toString());
				
				Logger.v(tag, LogType.Trace, "20");
				
				EnumTestType E2E_1_testType = convertTestTypeStringToTestTypeEnum(workflowSpecificConnectivity.getE2ETestInformation().getTestType());

				E2E_1_RESULT = getValueFromList(EnumParamTest.E2E_RESULT,
						moduleFirstTest, resultFirstTest, E2E_1_testType);
				E2E_1_CAUSE = getValueFromList(EnumParamTest.E2E_CAUSE,
						moduleFirstTest, resultFirstTest, E2E_1_testType);
				E2E_1_NETWORK = getValueFromList(EnumParamTest.E2E_NETWORK,
						moduleFirstTest, resultFirstTest, E2E_1_testType);
				E2E_1_TYPE = workflowSpecificConnectivity
						.getE2ETestInformation().getTestType();
				E2E_1_MSISDN = workflowSpecificConnectivity
						.getE2ETestInformation().getMsisdn();
				E2E_1_IP = workflowSpecificConnectivity.getE2ETestInformation()
						.getIPaddress();
				E2E_1_IP_PKT_TX = getValueFromList(EnumParamTest.E2E_IP_PKT_TX,
						moduleFirstTest, resultFirstTest, E2E_1_testType);
				E2E_1_IP_PKT_RX = getValueFromList(EnumParamTest.E2E_IP_PKT_RX,
						moduleFirstTest, resultFirstTest, E2E_1_testType);

				String LOCAL_E2E_1_IP_RTD_MAX = getValueFromList(EnumParamTest.E2E_IP_RTD_MAX, moduleFirstTest,resultFirstTest, E2E_1_testType);
				E2E_1_IP_RTD_MAX = (LOCAL_E2E_1_IP_RTD_MAX.equals("NA"))?LOCAL_E2E_1_IP_RTD_MAX:LOCAL_E2E_1_IP_RTD_MAX+" ms";
				
				String LOCAL_E2E_1_IP_RTD_AVG = getValueFromList(EnumParamTest.E2E_IP_RTD_AVG, moduleFirstTest,resultFirstTest, E2E_1_testType);
				E2E_1_IP_RTD_AVG = (LOCAL_E2E_1_IP_RTD_AVG.equals("NA"))?LOCAL_E2E_1_IP_RTD_AVG:LOCAL_E2E_1_IP_RTD_AVG+ " ms";
				
				String LOCAL_E2E_1_IP_LOSS = getValueFromList(EnumParamTest.E2E_IP_LOSS,moduleFirstTest, resultFirstTest, E2E_1_testType);
				E2E_1_IP_LOSS = (LOCAL_E2E_1_IP_LOSS.equals("NA"))?(LOCAL_E2E_1_IP_LOSS):(LOCAL_E2E_1_IP_LOSS + " %");
				
				E2E_1_SMS_MODEM_RESPONSE = getValueFromList(
						EnumParamTest.E2E_SMS_MODEM_RESPONSE, moduleFirstTest,
						resultFirstTest, E2E_1_testType);
				
				SIM_FINAL = workflowSpecificConnectivity.getE2ETestInformation()
						.getMsisdn()
						+ "/"
						+ workflowSpecificConnectivity.getE2ETestInformation()
								.getIPaddress();
				
				Logger.v(tag, LogType.Trace, "20.1 - E2E_1_RESULT :"+E2E_1_RESULT);
				
				if (InternalErroChecker(E2E_1_RESULT))
					endpointEnum = EnumEndPoint.END_POINT_5;
				else if (!E2E_1_RESULT.contains("NOK")) {
					Logger.v(tag, LogType.Trace, "21 - EnumEndPoint.END_POINT_3");
					endpointEnum = EnumEndPoint.END_POINT_3;
				}
				else {
					Logger.v(tag, LogType.Trace, "21 -  EnumEndPoint.END_POINT_4");
					endpointEnum = EnumEndPoint.END_POINT_4;
				}
			} else {
				Logger.v(tag, LogType.Trace, "21 - EnumEndPoint.END_POINT_4");
				endpointEnum = EnumEndPoint.END_POINT_4;
			}
			
			break;
			
		}
		
		Logger.v(tag, LogType.Trace, "21");
		

		StringBuilder message = new StringBuilder();
		
		if (NetworkType.contains("GPRS")) {
			mode = "0";
		} else if (NetworkType.contains("EDGE")) {
			mode = "1";
		} else if (NetworkType.contains("HSDPA")) {
			mode = "3";
		} else if (NetworkType.contains("HSPA")) {
			mode = "5";
		} else if (NetworkType.contains("HSUPA")) {
			mode = "4";
		} else if (NetworkType.contains("UMTS")) {
			mode = "2";
		}
		
		Logger.v(tag, LogType.Trace, "22");

		message.append("{");
			message.append("\"deviceId\":\"" + DeviceID + "\",");
			message.append("\"radiolog\":[");
				message.append("{");
					message.append("\"module\": \""+module+"\",");
					message.append("\"timestamp\": \"" + timestamp + "\",");
					message.append("\"mac\": \""+mac+"\",");
					message.append("\"network\": {");
						message.append("\"mode\": \""+mode+"\",");
						message.append("\"roaming\": \""+ roaming + "\",");
						message.append("\"rssi\": \"" + rssi + "\",");
						message.append("\"cellid\":\"" + GsmCellID + "\",");
						message.append("\"plmn\":\"" + OperatorName + "\",");
						message.append("\"mcc\":\"" + MCC_MNC + "\",");
						message.append("\"psc\":\"" + GsmPsc + "\",");
						message.append("\"ber\":\"" + GsmBitErrorRate + "\",");
						message.append("\"lac\":\"" + GsmLac + "\"");
					message.append("},");
					
					message.append("\"event\":{");
						message.append("\"type\":"+eventType+",");
						message.append("\"origin\": {");
						
							message.append("\"ACTIVITY\" : \""+activity_action+"\",");
							message.append("\"METTER_SERIAL\" : \""+METTER_SERIAL+"\",");
							message.append("\"METTER_ADD_INFO\" : \""+modelo_selo+"\",");
							message.append("\"RESULT\" : \""+convertEndPointToWorkflowStateDescription(endpointEnum, resultDetails).replace(", tente novamente...","")+"\",");
							message.append("\"FINAL_GPS_INFO\" : \""+FINAL_GPS_INFO+"\",");
							message.append("\"SIM_INIT\" : \""+SIM_INIT+"\",");
							message.append("\"SIM_FINAL\" : \""+SIM_FINAL+"\",");
							message.append("\"COMMENT\" : \""+COMMENT+"\",");
							if (hasNetworkMetrics) {
							message.append("\"NET_MEASURES_1\" : {");
								message.append("\"GPS\" : \""+NET_MEASURES_1_GPS+"\",");
								message.append("\"RSSI\" : \""+NET_MEASURES_1_RSSI+"\",");
								message.append("\"LAC/TAC\" : \""+NET_MEASURES_1_LAC_TAC+"\",");
								message.append("\"CELLID\" : \""+NET_MEASURES_1_CELLID+"\",");
								message.append("\"LINK\" : \""+NET_MEASURES_1_LINK+"\",");
								message.append("\"IMEI\" : \""+NET_MEASURES_1_IMEI+"\",");
								message.append("\"IMSI\" : \""+NET_MEASURES_1_IMSI+"\"");
							message.append("},");
							}
							message.append("\"E2E_1\" : {");
								message.append("\"RESULT\" : \""+E2E_1_RESULT+"\",");
								message.append("\"CAUSE\" : \""+E2E_1_CAUSE+"\",");
								message.append("\"NETWORK\" : \""+E2E_1_NETWORK+"\",");
								message.append("\"TYPE\" : \""+E2E_1_TYPE+"\",");
								message.append("\"MSISDN\" : \""+E2E_1_MSISDN+"\",");
								message.append("\"IP\" : \""+E2E_1_IP+"\",");
								message.append("\"IP_PKT_TX\" : \""+E2E_1_IP_PKT_TX+"\",");
								message.append("\"IP_PKT_RX\" : \""+E2E_1_IP_PKT_RX+"\",");
								message.append("\"IP_RTD_MAX\" : \""+E2E_1_IP_RTD_MAX+"\",");
								message.append("\"IP_RTD_AVG\" : \""+E2E_1_IP_RTD_AVG+"\",");
								message.append("\"IP_LOSS\" : \""+E2E_1_IP_LOSS+"\",");
								message.append("\"SMS_MODEM_RESPONSE\" : \""+E2E_1_SMS_MODEM_RESPONSE+"\"");
							message.append("},");
							message.append("\"E2E_2\" : {");
								message.append("\"RESULT\" : \""+E2E_2_RESULT+"\",");
								message.append("\"CAUSE\" : \""+E2E_2_CAUSE+"\",");
								message.append("\"NETWORK\" : \""+E2E_2_NETWORK+"\",");
								message.append("\"TYPE\" : \""+E2E_2_TYPE+"\",");
								message.append("\"MSISDN\" : \""+E2E_2_MSISDN+"\",");
								message.append("\"IP\" : \""+E2E_2_IP+"\",");
								message.append("\"IP_PKT_TX\" : \""+E2E_2_IP_PKT_TX+"\",");
								message.append("\"IP_PKT_RX\" : \""+E2E_2_IP_PKT_RX+"\",");
								message.append("\"IP_RTD_MAX\" : \""+E2E_2_IP_RTD_MAX+"\",");
								message.append("\"IP_RTD_AVG\" : \""+E2E_2_IP_RTD_AVG+"\",");
								message.append("\"IP_LOSS\" : \""+E2E_2_IP_LOSS+"\",");
								message.append("\"SMS_MODEM_RESPONSE\" : \""+E2E_2_SMS_MODEM_RESPONSE+"\"");
							message.append("}");
						message.append("}");
					message.append("},");
					
					message.append("\"gps\":\"" + Latitude + " " + Longitude +" 0.0 0.0\"");
			message.append("}]");
		message.append("}");
		
		
		Logger.v(tag, LogType.Trace, "message :" + message.toString());
		
		try {

			// Send the Post
			String responseServer = SendPost(message.toString());

			Logger.v(tag, LogType.Trace, "response :" + responseServer);

			Logger.v(tag, LogType.Trace,
					"message before return :" + message.toString());

			return new Pair(
					(responseServer.contains("000") ? EnumSendInformationResult.OK
							: EnumSendInformationResult.NOTOK),
					message.toString());

		} catch (Exception ex) {
			Logger.v(tag, LogType.Trace, "Error :" + ex.toString());
		}
		
		return new Pair(EnumSendInformationResult.NETWORK_EXCEPTION, message.toString()) ;
	}
	
	public String convertEndPointToWorkflowStateDescription(EnumEndPoint endpointEnum, String resultDetails) {
		
		switch(endpointEnum) {
		case END_POINT_1:
			return "Falha no teste E2E inicial "+ resultDetails;
		case END_POINT_2:
			return "Sem cobertura de rede "+ resultDetails;
		case END_POINT_3:
			return "Sucesso "+ resultDetails;
		case END_POINT_4:
			return "Falha no teste E2E final "+ resultDetails;
		case END_POINT_5:
			return "Falha "+resultDetails;
		}
		
		return "NA";
	}
	
	public boolean sendInformationJSONToServer(String msg) {
		String method = "saveAndSendWorkFlowResult";
		
		Logger.v(tag, method, LogType.Debug, "message :" + msg);
		
		String responseServer = SendPost(msg);
		
		Logger.v(tag, method, LogType.Debug, "response :" + responseServer);
		
		return responseServer.contains("Ok");
	}
	
	public static String convertNumberNormalDataFormat(int number) {

		if (number < 10 && number > 0) {
			return "0" + number;
		} else {
			return number + "";
		}
	}
	
	
	// String moduleID == taskModule
	public static String getValueFromList(EnumParamTest eParamTest, String moduleID, ArrayList<ExecutionResults> resultList, EnumTestType testType) {
		String method = "getValueFromList";
		
		Tuplo myTuploIP = convertEnumParamTestToInternacionalCodeIP(eParamTest, moduleID);
		Tuplo myTuploCSD = convertEnumParamTestToInternacionalCodeCSD(eParamTest, moduleID);
		
		switch(testType) {
		case M2M_CSD:
			for (ExecutionResults er :resultList) {
				if (er.getinternacionalcode().equals(myTuploCSD.getFirst()) && er.gettaskOrderNumber().equals(myTuploCSD.getSecond()) && er.gettaskModule().equals(myTuploCSD.getThree())) {
					Logger.v(tag, method, LogType.Debug, "M2M_CSD :: er: " + er.toString());
					return er.getvalue();
				}
			}
			break;
		case M2M_IP:
			for (ExecutionResults er :resultList) {
				if (er.getinternacionalcode().equals(myTuploIP.getFirst()) && er.gettaskOrderNumber().equals(myTuploIP.getSecond()) && er.gettaskModule().equals(myTuploIP.getThree())) {
					Logger.v(tag, method, LogType.Debug, "M2M_IP :: er: " + er.toString());
					return er.getvalue();
				}
			}
			break;
		}
		
		return "NOK";
	}
	
	public static Tuplo convertEnumParamTestToInternacionalCodeCSD(EnumParamTest eParamTest, String taskModule) {
		
		String InternacionalCode = "NA";
		String TaskOrderNumber = "1";
		String ModuleID = taskModule;
		
		Logger.v(tag, LogType.Trace, "convertEnumParamTestToInternacionalCodeCSD :: taskModule:" + taskModule);
		
		switch(eParamTest) {
			case E2E_RESULT:
				InternacionalCode = "R_01_06";
				break;
			case E2E_CAUSE:
				InternacionalCode = "R_01_07";
				break;
			case E2E_NETWORK:
				InternacionalCode = "R_01_05";
				break;
			case E2E_IP_PKT_TX:
				InternacionalCode = "R_44_11";
				TaskOrderNumber = "2";
				break;
			case E2E_IP_PKT_RX:
				InternacionalCode = "R_44_12";
				TaskOrderNumber = "2";
				break;
			case E2E_IP_RTD_MAX:
				InternacionalCode = "R_27_08";
				TaskOrderNumber = "2";
				break;
			case E2E_IP_RTD_AVG:
				InternacionalCode = "R_27_09";
				TaskOrderNumber = "2";
				break;
			case E2E_IP_LOSS:
				InternacionalCode = "R_44_13";
				TaskOrderNumber = "2";
				break;
			case E2E_SMS_MODEM_RESPONSE:
				InternacionalCode = "R_48_09";
				TaskOrderNumber = "4";
				break;
		}
		
		return new Tuplo(InternacionalCode, TaskOrderNumber, ModuleID);
	}
	
	public static EnumTestType convertTestTypeStringToTestTypeEnum(String testType) {
		if (testType.equals(M2M_CSDService.testName)) {
			return EnumTestType.M2M_CSD;
		}
		
		return EnumTestType.M2M_IP;
	}
	
	public static Tuplo convertEnumParamTestToInternacionalCodeIP(EnumParamTest eParamTest, String taskModule) {
		
		String InternacionalCode = "NA";
		String TaskOrderNumber = "2";
		String ModuleID = taskModule;
		
		Logger.v(tag, LogType.Trace, "convertEnumParamTestToInternacionalCodeIP :: taskModule:" + taskModule);
		
		switch(eParamTest) {
			case E2E_RESULT:
				InternacionalCode = "R_01_06";
				TaskOrderNumber = "2";
				break;
			case E2E_CAUSE:
				InternacionalCode = "R_01_07";
				TaskOrderNumber = "2";
				break;
			case E2E_NETWORK:
				InternacionalCode = "R_01_05";
				TaskOrderNumber = "2";
				break;
			case E2E_IP_PKT_TX:
				InternacionalCode = "R_44_11";
				TaskOrderNumber = "2";
				break;
			case E2E_IP_PKT_RX:
				InternacionalCode = "R_44_12";
				TaskOrderNumber = "2";
				break;
			case E2E_IP_RTD_MAX:
				InternacionalCode = "R_27_08";
				TaskOrderNumber = "2";
				break;
			case E2E_IP_RTD_AVG:
				InternacionalCode = "R_27_09";
				TaskOrderNumber = "2";
				break;
			case E2E_IP_LOSS:
				InternacionalCode = "R_44_13";
				TaskOrderNumber = "2";
				break;
			case E2E_SMS_MODEM_RESPONSE:
				InternacionalCode = "R_48_09";
				TaskOrderNumber = "5";
				break;
		}
		
		return new Tuplo(InternacionalCode, TaskOrderNumber, ModuleID);
	}
}

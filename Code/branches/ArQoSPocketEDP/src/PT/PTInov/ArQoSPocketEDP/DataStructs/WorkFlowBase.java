package PT.PTInov.ArQoSPocketEDP.DataStructs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import PT.PTInov.ArQoSPocketEDP.JSONConnector.SendWorkFlowInformationJSON;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.EnumParamTest;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.ExecutionResults;
import PT.PTInov.ArQoSPocketEDP.Utils.EnumTestType;
import PT.PTInov.ArQoSPocketEDP.Utils.LogType;
import PT.PTInov.ArQoSPocketEDP.Utils.Logger;
import android.location.Location;
import android.telephony.NeighboringCellInfo;

public class WorkFlowBase {
	
	private final static String tag = "WorkFlowBase";
	private final static int minimRSSI = -95;
	
	private Boolean workflowSucess;
	private Boolean workflowDone;
	
	// data de criação do test
	private Date createTestDate;
	
	// data de termino do teste
	private Date testDoneDate;
		
	// data de termino/envio do teste
	private Date sendTestDate;
	
	private WorkFlowType myWorkflowType;
	
	private String modemSerialNumber;

	// leituras da rede
	private NetworkInformationDataStruct networkMetricsInformation;
	private EnumNetworkMetrics networkMetricsState;
	
	// localização do teste
	private Location myLocation = null;
	
	WorkFlowBase(WorkFlowType WorkflowType, Location testLocation) {
		
		myWorkflowType = WorkflowType;
		
		myLocation = testLocation;
		
		workflowSucess = false;
		workflowDone = false;
		
		createTestDate = new Date();
		sendTestDate = null;
		testDoneDate = null;
		
		networkMetricsState = EnumNetworkMetrics.NA;
		networkMetricsInformation = null;
	}
	
	public void setWorkflowSucess() {
		workflowSucess = true;
		
		// se o workflow foi teve sucesso, então já terminou!
		setWorkflowDone();
	}
	
	public Location getLocation() {
		return myLocation;
	}
	
	public Boolean getWorkflowSucessState() {
		return workflowSucess;
	}
	
	public void setWorkflowDone() {
		testDoneDate = new Date();
		workflowDone = true;
	}
	
	public Boolean getWorkflowDoneState() {
		return workflowDone;
	}
	
	public void setTestSent() {
		sendTestDate = new Date();
	}
	
	public Date getCreateTestDate() {
		return createTestDate;
	}
	
	public Date getTestDoneDate() {
		return testDoneDate;
	}
	
	public Date getSendTestDate() {
		return sendTestDate;
	}
	
	public WorkFlowType getMyWorkFlowType() {
		return myWorkflowType;
	}
	
	public EnumNetworkMetrics getNetworkMetricsState() {
		return networkMetricsState;
	}
	
	public NetworkInformationDataStruct getNetworkMetricsInformation() {
		return networkMetricsInformation;
	}
	
	public String getModemSerialNumber() {
		return modemSerialNumber;
	}

	public void setModemSerialNumber(String modemSerialNumber) {
		this.modemSerialNumber = modemSerialNumber;
	}
	
	public boolean getLastTestState() {
		String method = "getLastTestState";

		boolean result = false;
		
		try {

			switch (myWorkflowType) {
			case WORKFLOW_CHANGE_SIM_CARD:
				WorkFlowChangeSIMCard workFlowChangeSIMCard = (WorkFlowChangeSIMCard) this;

				if (workFlowChangeSIMCard.getSecoundE3ETestState() != EnumTestE2EState.NA && workFlowChangeSIMCard.getSecoundE3ETestInformation() != null) {

					String moduleSecondTest = workFlowChangeSIMCard
							.getSecoundE3ETestInformation()
							.getModuleInformation().getId();
					ArrayList<ExecutionResults> resultSecondTest = workFlowChangeSIMCard
							.getSecoundE3ETestInformation()
							.getAllExecutionResults();

					EnumTestType E2E_2_testType = SendWorkFlowInformationJSON
							.convertTestTypeStringToTestTypeEnum(workFlowChangeSIMCard
									.getSecoundE3ETestInformation()
									.getTestType());

					/*
					if (SendWorkFlowInformationJSON.getValueFromList(
							EnumParamTest.E2E_RESULT, moduleSecondTest,
							resultSecondTest, E2E_2_testType).contains("NOK")) {
						result = true;
					}*/
					
					// A pedido a issue ARQOSPOCKETFW-84, é necessário considerar as seguintes string como caso de erro interno.
					// “NOK: M100000 ERRO_PROGRAMACAO_TAREFA”
					// “Erro de acesso”
					// “GENERIC_ERROR”
					// “ERRO_GENERICO”
					
					String resultFromTest = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_RESULT, 
							moduleSecondTest,resultSecondTest, E2E_2_testType);
					if (!resultFromTest.contains("NOK") && !resultFromTest.contains("NOK: M100000 ERRO_PROGRAMACAO_TAREFA")
							&& !resultFromTest.contains("Erro de acesso") && !resultFromTest.contains("GENERIC_ERROR")
							&& !resultFromTest.contains("ERRO_GENERICO")) {
						result = true;
					}

				} else if (workFlowChangeSIMCard.getFirstE3ETestState() != EnumTestE2EState.NA) {

					String moduleFirstTest = workFlowChangeSIMCard
							.getFirstE3ETestInformation()
							.getModuleInformation().getId();
					ArrayList<ExecutionResults> resultFirstTest = workFlowChangeSIMCard
							.getFirstE3ETestInformation()
							.getAllExecutionResults();

					EnumTestType E2E_1_testType = SendWorkFlowInformationJSON
							.convertTestTypeStringToTestTypeEnum(workFlowChangeSIMCard
									.getFirstE3ETestInformation().getTestType());

					/*
					if (SendWorkFlowInformationJSON.getValueFromList(
							EnumParamTest.E2E_RESULT, moduleFirstTest,
							resultFirstTest, E2E_1_testType).contains("NOK")) {
						result = true;
					}*/
					
					// A pedido a issue ARQOSPOCKETFW-84, é necessário considerar as seguintes string como caso de erro interno.
					// “NOK: M100000 ERRO_PROGRAMACAO_TAREFA”
					// “Erro de acesso”
					// “GENERIC_ERROR”
					// “ERRO_GENERICO”
					
					String resultFromTest = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_RESULT, moduleFirstTest,
							resultFirstTest, E2E_1_testType);
					if (!resultFromTest.contains("NOK") && !resultFromTest.contains("NOK: M100000 ERRO_PROGRAMACAO_TAREFA")
							&& !resultFromTest.contains("Erro de acesso") && !resultFromTest.contains("GENERIC_ERROR")
							&& !resultFromTest.contains("ERRO_GENERICO")) {
						result = true;
					}

				}

				break;
			case WORKFLOW_INSERT_SIM_CARD:
				WorkFlowInsertSIMCard workFlowInsertSIMCard = (WorkFlowInsertSIMCard) this;

				if (workFlowInsertSIMCard.getE3ETestState() != EnumTestE2EState.NA) {

					EnumTestType E2E_1_testType = SendWorkFlowInformationJSON
							.convertTestTypeStringToTestTypeEnum(workFlowInsertSIMCard
									.getE2ETestInformation().getTestType());

					String moduleFirstTest = workFlowInsertSIMCard
							.getE2ETestInformation().getModuleInformation()
							.getId();
					ArrayList<ExecutionResults> resultFirstTest = workFlowInsertSIMCard
							.getE2ETestInformation().getAllExecutionResults();

					/*
					if (!SendWorkFlowInformationJSON.getValueFromList(
							EnumParamTest.E2E_RESULT, moduleFirstTest,
							resultFirstTest, E2E_1_testType).contains("NOK")) {
						result = true;
					}*/
					
					// A pedido a issue ARQOSPOCKETFW-84, é necessário considerar as seguintes string como caso de erro interno.
					// “NOK: M100000 ERRO_PROGRAMACAO_TAREFA”
					// “Erro de acesso”
					// “GENERIC_ERROR”
					// “ERRO_GENERICO”
					
					String resultFromTest = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_RESULT, moduleFirstTest,
							resultFirstTest, E2E_1_testType);
					if (!resultFromTest.contains("NOK") && !resultFromTest.contains("NOK: M100000 ERRO_PROGRAMACAO_TAREFA")
							&& !resultFromTest.contains("Erro de acesso") && !resultFromTest.contains("GENERIC_ERROR")
							&& !resultFromTest.contains("ERRO_GENERICO")) {
						result = true;
					}
				}

				break;
			case WORKFLOW_INSERT_TEST_CONNECTIVITY:
				WorkFlowTestConnectivity workFlowTestConnectivity = (WorkFlowTestConnectivity) this;

				if (workFlowTestConnectivity.getE2ETestState() != EnumTestE2EState.NA) {

					EnumTestType E2E_1_testType = SendWorkFlowInformationJSON
							.convertTestTypeStringToTestTypeEnum(workFlowTestConnectivity
									.getE2ETestInformation().getTestType());

					String moduleFirstTest = workFlowTestConnectivity
							.getE2ETestInformation().getModuleInformation()
							.getId();
					ArrayList<ExecutionResults> resultFirstTest = workFlowTestConnectivity
							.getE2ETestInformation().getAllExecutionResults();

					/*
					if (!SendWorkFlowInformationJSON.getValueFromList(
							EnumParamTest.E2E_RESULT, moduleFirstTest,
							resultFirstTest, E2E_1_testType).contains("NOK")) {
						result = true;
					}*/
					
					// A pedido a issue ARQOSPOCKETFW-84, é necessário considerar as seguintes string como caso de erro interno.
					// “NOK: M100000 ERRO_PROGRAMACAO_TAREFA”
					// “Erro de acesso”
					// “GENERIC_ERROR”
					// “ERRO_GENERICO”
					
					String resultFromTest = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_RESULT, moduleFirstTest,
							resultFirstTest, E2E_1_testType);
					if (!resultFromTest.contains("NOK") && !resultFromTest.contains("NOK: M100000 ERRO_PROGRAMACAO_TAREFA")
							&& !resultFromTest.contains("Erro de acesso") && !resultFromTest.contains("GENERIC_ERROR")
							&& !resultFromTest.contains("ERRO_GENERICO")) {
						result = true;
					}
				}

				break;
			}
		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return result;
	}
	
	public void setNetworkInformationDataStruct(String pLatitude,
			String pLongitude, int pgsmSignalStrength, int pgsmBitErrorRate,
			String pgsmCellID, String pgsmLac, int pgsmPsc, String pMCC,
			String pIMEI, String pMCC_MNC, String pOperatorName,
			String pNetworkType, String pISOCountry, String pSIMSerialNumber,
			String pIMSI, String pRoaming,
			List<NeighboringCellInfo> pNeighboringCellList, Date ptestExecDate) {
		
		networkMetricsInformation = new NetworkInformationDataStruct(pLatitude, pLongitude,
				pgsmSignalStrength, pgsmBitErrorRate, pgsmCellID, pgsmLac, pgsmPsc, pMCC, pIMEI,
				pMCC_MNC, pOperatorName, pNetworkType, pISOCountry, pSIMSerialNumber, pIMSI,
				pRoaming, pNeighboringCellList, ptestExecDate);

		if (pgsmSignalStrength > minimRSSI)
			networkMetricsState = EnumNetworkMetrics.DONE_WITH_RANGE;
		else
			networkMetricsState = EnumNetworkMetrics.DONE_WITHOUT_RANGE;
	}

	
	public String buildMailReport(String Comment, String modelo_selo, String resultDetails, NetworkInformationDataStruct sendMetrics) {
		String report = "";
		
		String atividade = null;
		String metter_serial = modemSerialNumber;
		String METTER_ADD_INFO = modelo_selo;
		String FINAL_latitude = sendMetrics.getLatitude();
		String FINAL_Longitude = sendMetrics.getLongitude();
		
		String SIM_INIT_MSISDN = null;
		String SIM_INIT_IP = null;
		String SIM_FINAL_MSISDN = null;
		String SIM_FINAL_IP = null;
		String comment = Comment;
		
		String NET_MEASURES_Latitude = null;
		String NET_MEASURES_Longitude = null;
		String NET_MEASURES_RSSI = null;
		String NET_MEASURES_LAC = null;
		String NET_MEASURES_CELLID = null;
		String NET_MEASURES_LINK = null;
		String NET_MEASURES_IMEI = null;
		String NET_MEASURES_IMSI = null;
		String NET_MEASURES_MCC_MNC = null;
		String NET_MEASURES_OPERATOR_NAME = null;
		
		if (networkMetricsInformation != null) {
			NET_MEASURES_Latitude = networkMetricsInformation.getLatitude();
			NET_MEASURES_Longitude = networkMetricsInformation.getLongitude();
			NET_MEASURES_RSSI = networkMetricsInformation.getGsmSignalStrength()+"";
			NET_MEASURES_LAC = networkMetricsInformation.getGsmLac();
			NET_MEASURES_CELLID = networkMetricsInformation.getGsmCellID();
			NET_MEASURES_LINK = networkMetricsInformation.getNetworkType();
			NET_MEASURES_IMEI = networkMetricsInformation.getIMEI();
			NET_MEASURES_IMSI = networkMetricsInformation.getIMSI();
			NET_MEASURES_MCC_MNC = networkMetricsInformation.getMCC_MNC();
			NET_MEASURES_OPERATOR_NAME = networkMetricsInformation.getOperatorName();
		}
		
		String E2E_1_RESULT = null;
		String E2E_1_CAUSE = null;
		String E2E_1_PROBE_NETWK = null;
		String E2E_1_TYPE = null;
		String E2E_1_MSISDN = null;
		String E2E_1_IP = null;
		String E2E_1_IP_PKT_Tx = null;
		String E2E_1_IP_PKT_Rx = null;
		String E2E_1_IP_LOSS = null;
		String E2E_1_IP_RTD_MAX = null;
		String E2E_1_IP_RTD_AVG = null;
		String E2E_1_SMS_MODEM_RESPONSE = null;
		
		String E2E_2_RESULT = null;
		String E2E_2_CAUSE = null;
		String E2E_2_PROBE_NETWK = null;
		String E2E_2_TYPE = null;
		String E2E_2_MSISDN = null;
		String E2E_2_IP = null;
		String E2E_2_IP_PKT_Tx = null;
		String E2E_2_IP_PKT_Rx = null;
		String E2E_2_IP_LOSS = null;
		String E2E_2_IP_RTD_MAX = null;
		String E2E_2_IP_RTD_AVG = null;
		String E2E_2_SMS_MODEM_RESPONSE = null;
		
		switch (myWorkflowType) {
			case WORKFLOW_CHANGE_SIM_CARD:
				WorkFlowChangeSIMCard workFlowChangeSIMCard = (WorkFlowChangeSIMCard) this;
				
				atividade = "Troca SIM";
				
				if (workFlowChangeSIMCard.getFirstE3ETestState() != EnumTestE2EState.NA) {

					String moduleFirstTest = workFlowChangeSIMCard
							.getFirstE3ETestInformation().getModuleInformation()
							.getId();
					ArrayList<ExecutionResults> resultFirstTest = workFlowChangeSIMCard
							.getFirstE3ETestInformation().getAllExecutionResults();

					Logger.v(tag, LogType.Trace, "12");
					
					EnumTestType E2E_1_testType = SendWorkFlowInformationJSON.convertTestTypeStringToTestTypeEnum(workFlowChangeSIMCard.getFirstE3ETestInformation().getTestType());

					E2E_1_RESULT = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_RESULT,
							moduleFirstTest, resultFirstTest, E2E_1_testType);
					E2E_1_CAUSE = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_CAUSE,
							moduleFirstTest, resultFirstTest, E2E_1_testType);
					E2E_1_PROBE_NETWK = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_NETWORK,
							moduleFirstTest, resultFirstTest, E2E_1_testType);
					E2E_1_TYPE = workFlowChangeSIMCard.getFirstE3ETestInformation()
							.getTestType();
					E2E_1_MSISDN = workFlowChangeSIMCard.getFirstE3ETestInformation()
							.getMsisdn();
					E2E_1_IP = workFlowChangeSIMCard.getFirstE3ETestInformation()
							.getIPaddress();
					E2E_1_IP_PKT_Tx = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_IP_PKT_TX,
							moduleFirstTest, resultFirstTest, E2E_1_testType);
					E2E_1_IP_PKT_Rx = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_IP_PKT_RX,
							moduleFirstTest, resultFirstTest, E2E_1_testType);
					
					String LOCAL_E2E_1_IP_RTD_MAX = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_IP_RTD_MAX, moduleFirstTest,resultFirstTest, E2E_1_testType);
					E2E_1_IP_RTD_MAX = (LOCAL_E2E_1_IP_RTD_MAX.equals("NA"))?LOCAL_E2E_1_IP_RTD_MAX:LOCAL_E2E_1_IP_RTD_MAX+" s";
					
					String LOCAL_E2E_1_IP_RTD_AVG = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_IP_RTD_AVG, moduleFirstTest,resultFirstTest, E2E_1_testType);
					E2E_1_IP_RTD_AVG = (LOCAL_E2E_1_IP_RTD_AVG.equals("NA"))?LOCAL_E2E_1_IP_RTD_AVG:LOCAL_E2E_1_IP_RTD_AVG+ " s";
					
					String LOCAL_E2E_1_IP_LOSS = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_IP_LOSS,moduleFirstTest, resultFirstTest, E2E_1_testType);
					E2E_1_IP_LOSS = (LOCAL_E2E_1_IP_LOSS.equals("NA"))?(LOCAL_E2E_1_IP_LOSS):(LOCAL_E2E_1_IP_LOSS + " %");
					
					E2E_1_SMS_MODEM_RESPONSE = SendWorkFlowInformationJSON.getValueFromList(
							EnumParamTest.E2E_SMS_MODEM_RESPONSE, moduleFirstTest,
							resultFirstTest, E2E_1_testType);

				}
				
				SIM_INIT_MSISDN = E2E_1_MSISDN;
				SIM_INIT_IP = E2E_1_IP;
				
				if (workFlowChangeSIMCard.getSecoundE3ETestState() != EnumTestE2EState.NA && workFlowChangeSIMCard.getSecoundE3ETestInformation() != null) {

					String moduleSecondTest = workFlowChangeSIMCard
							.getSecoundE3ETestInformation().getModuleInformation()
							.getId();
					ArrayList<ExecutionResults> resultSecondTest = workFlowChangeSIMCard
							.getSecoundE3ETestInformation()
							.getAllExecutionResults();

					Logger.v(tag, LogType.Trace, "14");
					
					EnumTestType E2E_2_testType = SendWorkFlowInformationJSON.convertTestTypeStringToTestTypeEnum(workFlowChangeSIMCard.getSecoundE3ETestInformation().getTestType());

					E2E_2_RESULT = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_RESULT,
							moduleSecondTest, resultSecondTest, E2E_2_testType);
					E2E_2_CAUSE = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_CAUSE,
							moduleSecondTest, resultSecondTest, E2E_2_testType);
					E2E_2_PROBE_NETWK = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_NETWORK,
							moduleSecondTest, resultSecondTest, E2E_2_testType);
					E2E_2_TYPE = workFlowChangeSIMCard.getSecoundE3ETestInformation()
							.getTestType();
					E2E_2_MSISDN = workFlowChangeSIMCard.getSecoundE3ETestInformation()
							.getMsisdn();
					E2E_2_IP = workFlowChangeSIMCard.getSecoundE3ETestInformation()
							.getIPaddress();
					E2E_2_IP_PKT_Tx = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_IP_PKT_TX,
							moduleSecondTest, resultSecondTest, E2E_2_testType);
					E2E_2_IP_PKT_Rx = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_IP_PKT_RX,
							moduleSecondTest, resultSecondTest, E2E_2_testType);
					
					String LOCAL_E2E_2_IP_RTD_MAX = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_IP_RTD_MAX, moduleSecondTest, resultSecondTest, E2E_2_testType);
					E2E_2_IP_RTD_MAX = (LOCAL_E2E_2_IP_RTD_MAX.equals("NA"))?LOCAL_E2E_2_IP_RTD_MAX:LOCAL_E2E_2_IP_RTD_MAX+" s";
					
					String LOCAL_E2E_2_IP_RTD_AVG = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_IP_RTD_AVG, moduleSecondTest, resultSecondTest, E2E_2_testType);
					E2E_2_IP_RTD_AVG = (LOCAL_E2E_2_IP_RTD_AVG.equals("NA"))?LOCAL_E2E_2_IP_RTD_AVG:LOCAL_E2E_2_IP_RTD_AVG+ " s";
					
					String LOCAL_E2E_2_IP_LOSS = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_IP_LOSS,moduleSecondTest, resultSecondTest, E2E_2_testType);
					E2E_2_IP_LOSS = (LOCAL_E2E_2_IP_LOSS.equals("NA"))?(LOCAL_E2E_2_IP_LOSS):(LOCAL_E2E_2_IP_LOSS + " %");
					
					E2E_2_SMS_MODEM_RESPONSE = SendWorkFlowInformationJSON.getValueFromList(
							EnumParamTest.E2E_SMS_MODEM_RESPONSE, moduleSecondTest,
							resultSecondTest, E2E_2_testType);
				}
				
				SIM_FINAL_MSISDN = E2E_2_MSISDN;
				SIM_FINAL_IP = E2E_2_IP;
				
				break;
			case WORKFLOW_INSERT_SIM_CARD:
				WorkFlowInsertSIMCard workFlowInsertSIMCard = (WorkFlowInsertSIMCard) this;
				
				atividade = "Inserir SIM";

				if (workFlowInsertSIMCard.getE3ETestState() != EnumTestE2EState.NA) {
				
					EnumTestType E2E_1_testType = SendWorkFlowInformationJSON.convertTestTypeStringToTestTypeEnum(workFlowInsertSIMCard.getE2ETestInformation().getTestType());

					String moduleFirstTest = workFlowInsertSIMCard
						.getE2ETestInformation().getModuleInformation().getId();
					ArrayList<ExecutionResults> resultFirstTest = workFlowInsertSIMCard
						.getE2ETestInformation().getAllExecutionResults();
				
					E2E_1_RESULT = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_RESULT,moduleFirstTest, resultFirstTest, E2E_1_testType);;
					E2E_1_CAUSE = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_CAUSE, moduleFirstTest, resultFirstTest, E2E_1_testType);
					E2E_1_PROBE_NETWK = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_NETWORK,
						moduleFirstTest, resultFirstTest, E2E_1_testType);
					E2E_1_TYPE = workFlowInsertSIMCard.getE2ETestInformation()
						.getTestType();
					E2E_1_MSISDN = workFlowInsertSIMCard.getE2ETestInformation()
						.getMsisdn();
					E2E_1_IP = workFlowInsertSIMCard.getE2ETestInformation()
						.getIPaddress();
					E2E_1_IP_PKT_Tx = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_IP_PKT_TX,
						moduleFirstTest, resultFirstTest, E2E_1_testType);
					E2E_1_IP_PKT_Rx = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_IP_PKT_RX,
						moduleFirstTest, resultFirstTest, E2E_1_testType);
					E2E_1_IP_LOSS = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_IP_LOSS,moduleFirstTest, resultFirstTest, E2E_1_testType);
					E2E_1_IP_RTD_MAX = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_IP_RTD_MAX, moduleFirstTest,resultFirstTest, E2E_1_testType);
					E2E_1_IP_RTD_AVG = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_IP_RTD_AVG, moduleFirstTest,resultFirstTest, E2E_1_testType);
					E2E_1_SMS_MODEM_RESPONSE = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_SMS_MODEM_RESPONSE, moduleFirstTest,resultFirstTest, E2E_1_testType);
				}
				
				SIM_FINAL_MSISDN = E2E_1_MSISDN;
				SIM_FINAL_IP = E2E_1_IP;
				
				break;
			case WORKFLOW_INSERT_TEST_CONNECTIVITY:
				WorkFlowTestConnectivity workFlowTestConnectivity = (WorkFlowTestConnectivity) this;
				
				atividade = "Teste de Connectividade";
				
				if (workFlowTestConnectivity.getE2ETestState() != EnumTestE2EState.NA) {
					
					EnumTestType E2E_1_testType = SendWorkFlowInformationJSON.convertTestTypeStringToTestTypeEnum(workFlowTestConnectivity.getE2ETestInformation().getTestType());

					String moduleFirstTest = workFlowTestConnectivity
						.getE2ETestInformation().getModuleInformation().getId();
					ArrayList<ExecutionResults> resultFirstTest = workFlowTestConnectivity
						.getE2ETestInformation().getAllExecutionResults();
				
					E2E_1_RESULT = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_RESULT,moduleFirstTest, resultFirstTest, E2E_1_testType);;
					E2E_1_CAUSE = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_CAUSE, moduleFirstTest, resultFirstTest, E2E_1_testType);
					E2E_1_PROBE_NETWK = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_NETWORK,
						moduleFirstTest, resultFirstTest, E2E_1_testType);
					E2E_1_TYPE = workFlowTestConnectivity.getE2ETestInformation()
						.getTestType();
					E2E_1_MSISDN = workFlowTestConnectivity.getE2ETestInformation()
						.getMsisdn();
					E2E_1_IP = workFlowTestConnectivity.getE2ETestInformation()
						.getIPaddress();
					E2E_1_IP_PKT_Tx = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_IP_PKT_TX,
						moduleFirstTest, resultFirstTest, E2E_1_testType);
					E2E_1_IP_PKT_Rx = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_IP_PKT_RX,
						moduleFirstTest, resultFirstTest, E2E_1_testType);
					E2E_1_IP_LOSS = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_IP_LOSS,moduleFirstTest, resultFirstTest, E2E_1_testType);
					E2E_1_IP_RTD_MAX = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_IP_RTD_MAX, moduleFirstTest,resultFirstTest, E2E_1_testType);
					E2E_1_IP_RTD_AVG = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_IP_RTD_AVG, moduleFirstTest,resultFirstTest, E2E_1_testType);
					E2E_1_SMS_MODEM_RESPONSE = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_SMS_MODEM_RESPONSE, moduleFirstTest,resultFirstTest, E2E_1_testType);
				}
				
				SIM_FINAL_MSISDN = E2E_1_MSISDN;
				SIM_FINAL_IP = E2E_1_IP;
				
				break;
		}
		
		return buildMailInformation(atividade, metter_serial, METTER_ADD_INFO, FINAL_latitude, FINAL_Longitude,
				SIM_INIT_MSISDN, SIM_INIT_IP, SIM_FINAL_MSISDN, SIM_FINAL_IP, comment, NET_MEASURES_Latitude,
				NET_MEASURES_Longitude, NET_MEASURES_RSSI, NET_MEASURES_LAC, NET_MEASURES_CELLID, NET_MEASURES_LINK,
				NET_MEASURES_IMEI, NET_MEASURES_IMSI, NET_MEASURES_MCC_MNC, NET_MEASURES_OPERATOR_NAME,
				E2E_1_RESULT, E2E_1_CAUSE, E2E_1_PROBE_NETWK, E2E_1_TYPE, E2E_1_MSISDN, E2E_1_IP,
				E2E_1_IP_PKT_Tx, E2E_1_IP_PKT_Rx, E2E_1_IP_LOSS, E2E_1_IP_RTD_MAX, E2E_1_IP_RTD_AVG, E2E_1_SMS_MODEM_RESPONSE,
				E2E_2_RESULT, E2E_2_CAUSE, E2E_2_PROBE_NETWK, E2E_2_TYPE, E2E_2_MSISDN, E2E_2_IP,
				E2E_2_IP_PKT_Tx, E2E_2_IP_PKT_Rx, E2E_2_IP_LOSS, E2E_2_IP_RTD_MAX, E2E_2_IP_RTD_AVG, E2E_2_SMS_MODEM_RESPONSE, resultDetails);
	}
	
	public static String buildMailInformation(String atividade, String metter_serial, String METTER_ADD_INFO, String FINAL_latitude, String FINAL_Longitude,
			String SIM_INIT_MSISDN, String SIM_INIT_IP, String SIM_FINAL_MSISDN, String SIM_FINAL_IP, String comment, String NET_MEASURES_Latitude,
			String NET_MEASURES_Longitude, String NET_MEASURES_RSSI, String NET_MEASURES_LAC, String NET_MEASURES_CELLID, String NET_MEASURES_LINK,
			String NET_MEASURES_IMEI, String NET_MEASURES_IMSI, String NET_MEASURES_MCC_MNC, String NET_MEASURES_OPERATOR_NAME,
			String E2E_1_RESULT, String E2E_1_CAUSE, String E2E_1_PROBE_NETWK, String E2E_1_TYPE, String E2E_1_MSISDN, String E2E_1_IP,
			String E2E_1_IP_PKT_Tx, String E2E_1_IP_PKT_Rx, String E2E_1_IP_LOSS, String E2E_1_IP_RTD_MAX, String E2E_1_IP_RTD_AVG, String E2E_1_SMS_MODEM_RESPONSE,
			String E2E_2_RESULT, String E2E_2_CAUSE, String E2E_2_PROBE_NETWK, String E2E_2_TYPE, String E2E_2_MSISDN, String E2E_2_IP,
			String E2E_2_IP_PKT_Tx, String E2E_2_IP_PKT_Rx, String E2E_2_IP_LOSS, String E2E_2_IP_RTD_MAX, String E2E_2_IP_RTD_AVG, String E2E_2_SMS_MODEM_RESPONSE, String resultDetails) {
		StringBuilder sb = new StringBuilder();
		
		//sb.append("\nATIVIDADE:'"+atividade+"'");
		sb.append("\nMETTER_SERIAL:’"+metter_serial+"‘");
		sb.append("\nMETTER_ADD_INFO:’"+METTER_ADD_INFO+"‘");
		sb.append("\nRESULT:’"+resultDetails.replace(", tente novamente...","")+"‘");
		//sb.append("\nFINAL_GPS_INFO: ‘"+FINAL_latitude+"/"+FINAL_Longitude+"’");
		sb.append("\nSIM_INIT:’"+SIM_INIT_MSISDN+"‘ '"+SIM_INIT_IP+"'");
		sb.append("\nSIM_FINAL:’"+SIM_FINAL_MSISDN+"‘ '"+SIM_FINAL_IP+"'");
		sb.append("\nCOMMENT:’"+comment+"’");

		//sb.append("\n\nNET_MEASURES_1: GPS:'"+NET_MEASURES_Latitude+"/"+NET_MEASURES_Longitude+"' RSSI:'"+NET_MEASURES_RSSI+" dBm' LAC/TAL:'"+NET_MEASURES_LAC+"' CellId:'"+NET_MEASURES_CELLID+"' LINK:'"+NET_MEASURES_LINK+"' IMEI:'"+NET_MEASURES_IMEI+"' IMSI:'"+NET_MEASURES_IMSI+"'");
		//sb.append("OPERATOR_MCC_MNC:'"+NET_MEASURES_MCC_MNC+"' OPERATOR_NAME:'"+NET_MEASURES_OPERATOR_NAME+"'");

		//sb.append("\n\nE2E_1: RESULT:‘"+E2E_1_RESULT+"' CAUSE:'"+E2E_1_CAUSE+"' PROBE_NETWK:'"+E2E_1_PROBE_NETWK+"' TYPE:'"+E2E_1_TYPE+"' MSISDN:'"+E2E_1_MSISDN+"' IP:'"+E2E_1_IP+"'");
		//sb.append("IP_PKT_Tx:'"+E2E_1_IP_PKT_Tx+"' IP_PKT_Rx:'"+E2E_1_IP_PKT_Rx+"' IP_LOSS: '"+E2E_1_IP_LOSS+" %' IP_RTD_MAX:'"+E2E_1_IP_RTD_MAX+" s' IP_RTD_AVG:'"+E2E_1_IP_RTD_AVG+" s' SMS_MODEM_RESPONSE:'"+E2E_1_SMS_MODEM_RESPONSE+"'");
		
		//sb.append("\n\nE2E_2: RESULT:‘"+E2E_2_RESULT+"' CAUSE:'"+E2E_2_CAUSE+"' PROBE_NETWK:'"+E2E_2_PROBE_NETWK+"' TYPE:'"+E2E_2_TYPE+"' MSISDN:'"+E2E_2_MSISDN+"' IP:'"+E2E_2_IP+"'");
		//sb.append("IP_PKT_Tx:'"+E2E_2_IP_PKT_Tx+"' IP_PKT_Rx:'"+E2E_2_IP_PKT_Rx+"' IP_LOSS: '"+E2E_2_IP_LOSS+" %' IP_RTD_MAX:'"+E2E_2_IP_RTD_MAX+" s' IP_RTD_AVG:'"+E2E_2_IP_RTD_AVG+" s' SMS_MODEM_RESPONSE:'"+E2E_2_SMS_MODEM_RESPONSE+"'");
		
		
		sb.append("\n\nTeste realizado por ArQoS e-Mon");
		sb.append("\nCopyright 2012 Portugal Telecom. Todos os direitos reservados.");
		
		return sb.toString();
	}
}

package PT.PTInov.ArQoSPocketEDP.DataStructs;

import android.annotation.SuppressLint;
import java.util.ArrayList;

import PT.PTInov.ArQoSPocketEDP.JSONConnector.SendWorkFlowInformationJSON;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.EnumParamTest;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.ExecutionResults;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.ServiceResponse;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.TestModuleResponse;
import PT.PTInov.ArQoSPocketEDP.Utils.EnumTestType;
import PT.PTInov.ArQoSPocketEDP.Utils.LogType;
import PT.PTInov.ArQoSPocketEDP.Utils.Logger;

@SuppressLint("ParserError")
public class E2EInformationDataStruct {
	
	private final static String tag = "E2EInformationDataStruct";

	// module information
	private TestModuleResponse moduleInformation;

	// Teste information
	private long waitTimeForResultsInMillisec;
	private String destination;
	private String testType;
	private String msisdn;
	private String IPaddress;
	private ArrayList<ServiceResponse> responseFromServer;

	public E2EInformationDataStruct(TestModuleResponse localModuleInformation, 
			long waitTimeForResultsInMillisec, String destination, String testType,
			String msisdn, String IPaddress, ArrayList<ServiceResponse> responseFromServer) {
		
		moduleInformation = localModuleInformation;
		this.waitTimeForResultsInMillisec = waitTimeForResultsInMillisec;
		this.destination = destination;
		this.testType = testType;
		this.responseFromServer = responseFromServer;
		this.msisdn = msisdn;
		this.IPaddress = IPaddress;
	}
	
	public TestModuleResponse getModuleInformation() {
		return moduleInformation;
	}
	
	public long getWaitTimeForResultsInMillisec() {
		return waitTimeForResultsInMillisec;
	}
	
	public String getModule() {
		return ((moduleInformation != null)?moduleInformation.getName():"null");
	}
	
	public ArrayList<ServiceResponse> getResponseFromServer() {
		return responseFromServer;
	}
	
	public String getProbeID() {
		return ((moduleInformation != null)?moduleInformation.getId():"null");
	}
	
	public String getTestType() {
		return testType;
	}
	
	public String getDestination() {
		return destination;
	}
	
	public String getIPaddress() {
		return IPaddress;
	}
	
	public String getMsisdn() {
		return msisdn;
	}
	
	public ServiceResponse getFinalOKServeResponse() {
		
		for (ServiceResponse sr :responseFromServer) {
			if (sr.getExecutionstatus().equals("1"))
				return sr;
		}
		
		return null;
	}
	
	public ArrayList<ExecutionResults> getAllExecutionResults() {
		
		ArrayList<ExecutionResults> allList = new ArrayList<ExecutionResults>();
		
		for (ServiceResponse sr :responseFromServer)
			for (ExecutionResults er :sr.getExecutionResults())
				allList.add(er);
		
		return allList;
		
	}
	
	public boolean test_probe_error() {
		String method = "test_probe_error";
		
		boolean result = false;
		
		EnumTestType E2E_1_testType = SendWorkFlowInformationJSON.convertTestTypeStringToTestTypeEnum(testType);

		String moduleFirstTest = getModuleInformation().getId();
		ArrayList<ExecutionResults> resultFirstTest = getAllExecutionResults();

		Logger.v(tag, method, LogType.Debug, "moduleFirstTest :"+moduleFirstTest);
		Logger.v(tag, method, LogType.Debug, "resultFirstTest :"+resultFirstTest);
		Logger.v(tag, method, LogType.Debug, "E2E_1_testType :"+E2E_1_testType);
		
		String resultFromTest = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_RESULT, moduleFirstTest, resultFirstTest, E2E_1_testType);
		Logger.v(tag, method, LogType.Debug, "resultFromTest :"+resultFromTest);
		
		// A pedido a issue ARQOSPOCKETFW-84, é necessário considerar as seguintes string como caso de erro interno.
		// “NOK: M100000 ERRO_PROGRAMACAO_TAREFA”
		// “Erro de acesso”
		// “GENERIC_ERROR”
		// “ERRO_GENERICO”

		
		if (resultFromTest.contains("NOK: M100000 ERRO_PROGRAMACAO_TAREFA")
				|| resultFromTest.contains("Erro de acesso")
				|| resultFromTest.contains("GENERIC_ERROR")
				|| resultFromTest.contains("ERRO_GENERICO")) {
			result = true;
		}
		
		Logger.v(tag, method, LogType.Debug, "result :"+result);
		return result;
	}
	
	public boolean testOK() {
		String method = "testOK";
		
		boolean result = false;
		
		EnumTestType E2E_1_testType = SendWorkFlowInformationJSON.convertTestTypeStringToTestTypeEnum(testType);

		String moduleFirstTest = getModuleInformation().getId();
		ArrayList<ExecutionResults> resultFirstTest = getAllExecutionResults();

		Logger.v(tag, method, LogType.Debug, "moduleFirstTest :"+moduleFirstTest);
		Logger.v(tag, method, LogType.Debug, "resultFirstTest :"+resultFirstTest);
		Logger.v(tag, method, LogType.Debug, "E2E_1_testType :"+E2E_1_testType);
		
		String resultFromTest = SendWorkFlowInformationJSON.getValueFromList(EnumParamTest.E2E_RESULT, moduleFirstTest, resultFirstTest, E2E_1_testType);
		Logger.v(tag, method, LogType.Debug, "resultFromTest :"+resultFromTest);
		
		// A pedido a issue ARQOSPOCKETFW-84, é necessário considerar as seguintes string como caso de erro interno.
		// “NOK: M100000 ERRO_PROGRAMACAO_TAREFA”
		// “Erro de acesso”
		// “GENERIC_ERROR”
		// “ERRO_GENERICO”
		
		if (!resultFromTest.contains("NOK") && !resultFromTest.contains("NOK: M100000 ERRO_PROGRAMACAO_TAREFA")
				&& !resultFromTest.contains("Erro de acesso") && !resultFromTest.contains("GENERIC_ERROR")
				&& !resultFromTest.contains("ERRO_GENERICO")) {
			result = true;
		}
		
		Logger.v(tag, method, LogType.Debug, "result :"+result);
		return result;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\nmoduleInformation :"+moduleInformation.toString());
		sb.append("\nwaitTimeForResultsInMillisec :"+waitTimeForResultsInMillisec);
		sb.append("\nmodule :"+((moduleInformation != null)?moduleInformation.getName():"null"));
		sb.append("\ndestination :"+destination);
		sb.append("\ntestType :"+testType);
		sb.append("\nprobeID :"+((moduleInformation != null)?moduleInformation.getId():"null"));
		sb.append("\nmsisdn :"+msisdn);
		sb.append("\nIPaddress :"+IPaddress);
		sb.append("\nresponseFromServer :"+responseFromServer.toString());
		
		return sb.toString();
	}
}

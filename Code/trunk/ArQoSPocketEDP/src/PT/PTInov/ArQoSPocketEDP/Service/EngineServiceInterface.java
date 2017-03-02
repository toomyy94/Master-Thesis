package PT.PTInov.ArQoSPocketEDP.Service;

import android.annotation.SuppressLint;
import java.util.Date;
import java.util.List;

import PT.PTInov.ArQoSPocketEDP.DataStructs.DoNetworkTestResultEnum;
import PT.PTInov.ArQoSPocketEDP.DataStructs.EnumTestE2EState;
import PT.PTInov.ArQoSPocketEDP.DataStructs.NetworkInformationDataStruct;
import PT.PTInov.ArQoSPocketEDP.DataStructs.StoreInformation;
import PT.PTInov.ArQoSPocketEDP.DataStructs.WorkFlowBase;
import PT.PTInov.ArQoSPocketEDP.DataStructs.WorkFlowType;
import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.DiscoveryEnum;
import PT.PTInov.ArQoSPocketEDP.Utils.EnumOperator;
import PT.PTInov.ArQoSPocketEDP.Utils.RowDataTwoLines;

@SuppressLint("ParserError")
public interface EngineServiceInterface {
	
	public final static int FisrtTest = 1;
	public final static int SecoundTest = 2;

	public EnumTestE2EState doE2ETestCSD(String MSISDN, String IPAddr, Date testExecDate, int flagTest);
	public EnumTestE2EState doE2ETestIP(String MSISDN, String IPAddr,Date testExecDate, int flagTest);
	public DoNetworkTestResultEnum doNetworkTest();
	public boolean saveAndSendWorkFlowResult(String comment, String modelo_selo, String resultDetails);
	
	public NetworkInformationDataStruct getNetworkInfoFromTest();
	public EnumOperator getActualOperatorOnPhone();
	public void clearLastTest();
	public void insertModemSerialNumber(String modemSerialNumber);
	
	public void restartWorkFlow();
	public void setWorkFlow(WorkFlowType newWorkFlowType);
	public WorkFlowType getActualWorkFlow();
	public EnumTestE2EState getStateOfLastTest();
	public boolean firstTestDone();
	public boolean secoundTestDone();
	
	public List<WorkFlowBase> getMyHistory();
	public List<WorkFlowBase> getAllResult();
	
	public void setServerHost(String ip, String port);
	public String getPort();
	public String getIP();
	
	
	//callback
	public void setDescoveryStatus(DiscoveryEnum status);
	
	
	public String getEmailToSend();
	public void setEmailToSend(String email);
	
}

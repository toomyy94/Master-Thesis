package PT.PTInov.ArQoSPocketEDP.DataStructs;

import android.location.Location;
import PT.PTInov.ArQoSPocketEDP.Utils.LogType;
import PT.PTInov.ArQoSPocketEDP.Utils.Logger;

public class WorkFlowInsertSIMCard extends WorkFlowBase {

	private String tag = "WorkFlowInsertTMNSIMCard";

	private final static WorkFlowType myWorkflowType = WorkFlowType.WORKFLOW_INSERT_SIM_CARD;

	private E2EInformationDataStruct E2ETestInformation;

	private EnumTestE2EState E2ETestState;

	public WorkFlowInsertSIMCard(Location testLocation) {
		super(myWorkflowType, testLocation);

		Logger.v(tag, "WorkFlowInsertTMNSIMCard", LogType.Trace, "In");

		E2ETestState = EnumTestE2EState.NA;
		E2ETestInformation = null;

		Logger.v(tag, "WorkFlowInsertTMNSIMCard", LogType.Trace, "Out");
	}
	
	public void setTestResult(E2EInformationDataStruct pE3ETestInformation, Boolean testResultState) {
		
		E2ETestInformation = pE3ETestInformation;
		E2ETestState = (testResultState)?EnumTestE2EState.DONE_WITH_SUCCESS:EnumTestE2EState.DONE_WITHOUT_SUCCESS;
		
	}
	
	public EnumTestE2EState getE3ETestState() {
		return E2ETestState;
	}
	
	public E2EInformationDataStruct getE2ETestInformation() {
		return E2ETestInformation;
	}
	
	public void clearLastTest() {
		
		E2ETestState = EnumTestE2EState.NA;
		E2ETestInformation = null;
	
	}
}

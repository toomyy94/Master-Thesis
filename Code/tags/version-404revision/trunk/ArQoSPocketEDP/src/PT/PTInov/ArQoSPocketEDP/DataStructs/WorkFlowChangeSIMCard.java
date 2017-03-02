package PT.PTInov.ArQoSPocketEDP.DataStructs;

import java.util.Date;
import java.util.List;

import PT.PTInov.ArQoSPocketEDP.Service.EngineServiceInterface;
import PT.PTInov.ArQoSPocketEDP.Utils.LogType;
import PT.PTInov.ArQoSPocketEDP.Utils.Logger;
import android.annotation.SuppressLint;
import android.location.Location;
import android.telephony.NeighboringCellInfo;

@SuppressLint("ParserError")
public class WorkFlowChangeSIMCard extends WorkFlowBase {

	private String tag = "WorkFlowChangeSIMCard";

	private final static WorkFlowType myWorkflowType = WorkFlowType.WORKFLOW_CHANGE_SIM_CARD;

	// 1º teste E2E
	private E2EInformationDataStruct firstE3ETestInformation = null;
	private EnumTestE2EState firstE3ETestState;

	// 2º teste E2E
	private E2EInformationDataStruct secoundE3ETestInformation = null;
	private EnumTestE2EState secoundE3ETestState;

	public WorkFlowChangeSIMCard(Location testLocation) {
		super(myWorkflowType, testLocation);

		Logger.v(tag, "WorkFlowChangeSIMCard", LogType.Trace, "In");

		firstE3ETestState = EnumTestE2EState.NA;
		secoundE3ETestState = EnumTestE2EState.NA;
		firstE3ETestInformation = null;
		secoundE3ETestInformation = null;

		Logger.v(tag, "WorkFlowChangeSIMCard", LogType.Trace, "Out");
	}
	
	public void setTestResult(E2EInformationDataStruct E3ETestInformation, Boolean testResultState, int flagTest) {
		
		switch(flagTest) {
			case EngineServiceInterface.FisrtTest:
				
				firstE3ETestInformation = E3ETestInformation;
				firstE3ETestState = (testResultState)?EnumTestE2EState.DONE_WITH_SUCCESS:EnumTestE2EState.DONE_WITHOUT_SUCCESS;
				break;
				
			case EngineServiceInterface.SecoundTest:
				
				secoundE3ETestInformation = E3ETestInformation;
				secoundE3ETestState = (testResultState)?EnumTestE2EState.DONE_WITH_SUCCESS:EnumTestE2EState.DONE_WITHOUT_SUCCESS;
				break;
		}
		
	}
	
	public EnumTestE2EState getFirstE3ETestState() {
		return firstE3ETestState;
	}
	
	public EnumTestE2EState getSecoundE3ETestState() {
		return secoundE3ETestState;
	}
	
	public E2EInformationDataStruct getFirstE3ETestInformation() {
		return firstE3ETestInformation;
	}
	
	public E2EInformationDataStruct getSecoundE3ETestInformation() {
		return secoundE3ETestInformation;
	}

	public void clearLastTest() {
		
		Logger.v(tag, "clearLastTest", LogType.Trace, "In");
		
		if (secoundE3ETestState != EnumTestE2EState.NA) {
			
			Logger.v(tag, "clearLastTest", LogType.Trace, "IN :: secoundE3ETestState != EnumTestE2EState.NA");
			
			secoundE3ETestState = EnumTestE2EState.NA;
			secoundE3ETestInformation = null;
			
			Logger.v(tag, "clearLastTest", LogType.Trace, "OUT :: secoundE3ETestState != EnumTestE2EState.NA");
		} else {
			
			Logger.v(tag, "clearLastTest", LogType.Trace, "IN :: secoundE3ETestState == EnumTestE2EState.NA");
			
			firstE3ETestState = EnumTestE2EState.NA;
			firstE3ETestInformation = null;
			
			Logger.v(tag, "clearLastTest", LogType.Trace, "OUT :: secoundE3ETestState == EnumTestE2EState.NA");
		}
		
		Logger.v(tag, "clearLastTest", LogType.Trace, "Out");
	}
}

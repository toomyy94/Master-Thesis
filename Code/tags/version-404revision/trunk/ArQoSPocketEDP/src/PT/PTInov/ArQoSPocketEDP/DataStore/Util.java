package PT.PTInov.ArQoSPocketEDP.DataStore;

import PT.PTInov.ArQoSPocketEDP.DataStructs.WorkFlowType;

public class Util {
	
	public static int ConvertWorkflowTypeToInt(WorkFlowType workflowType) {
		
		switch(workflowType) {
			case WORKFLOW_CHANGE_SIM_CARD:
				return 0;
			case WORKFLOW_INSERT_SIM_CARD:
				return 1;
			case WORKFLOW_INSERT_TEST_CONNECTIVITY:
				return 2;
		}
		
		return -1;
	}
	
	public static WorkFlowType ConvertIntToWorkFlowType(int workflowType) {
		
		switch(workflowType) {
			case 0:
				return WorkFlowType.WORKFLOW_CHANGE_SIM_CARD;
			case 1:
				return WorkFlowType.WORKFLOW_INSERT_SIM_CARD;
			case 2:
				return WorkFlowType.WORKFLOW_INSERT_TEST_CONNECTIVITY;
		}
		
		return WorkFlowType.NA;
	}
	
	public static int ConvertBooleanToInt(boolean b) {
		
		if (b) return 1;
		
		return 0;
	}
	
	public static boolean ConvertIntToBoolean(int i) {
		if (i == 0) return false;
		
		return true;
	}

}

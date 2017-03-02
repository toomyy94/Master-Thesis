package PT.PTInov.ArQoSPocket.structs;

import PT.PTInov.ArQoSPocket.Enums.ActionState;



public class AssociationResultState {

	private final static String tag = "ConnectWiFiState";
	
	private ActionState AssociatePTWiFi;
	private long associateTime = -1; //(millisecounds)
	
	public AssociationResultState() {
		AssociatePTWiFi = ActionState.NA;
		associateTime = -1;
	}
	
	
	public AssociationResultState( ActionState pAssociatePTWiFi, long pAssociateTime) {
		AssociatePTWiFi = pAssociatePTWiFi;
		associateTime = pAssociateTime;
	}
	
	public ActionState getAssociatePTWiFiState() {
		return AssociatePTWiFi;
	}
	
	public long getAssociateTime() {
		return associateTime;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("AssociatePTWiFi :"+AssociatePTWiFi);
		sb.append("associateTime :"+associateTime);
		
		return sb.toString();
	}
}

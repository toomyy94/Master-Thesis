package pt.ptinovacao.arqospocket.service.tasks.structs;


public class AssociationResultState {

	private final static String TAG = "ConnectWiFiState";
	
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
	
	public enum ActionState {
		OK, NOTOK, NA
	}
}

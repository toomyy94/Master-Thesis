package pt.ptinovacao.arqospocket.core.radiologs;

public enum EEvent {
	HANDOVER, REGISTER_STATUS, PLMN_CHANGE, ROAMING_STATUS, PAUSE_SCAN,
	RESUME_SCAN, NO_NETWORKS_DETECTED, CELL_RESELECTION, CALL_SETUP,
	EMON_POCKET, CALL_ESTABLISHED, CALL_END, CALL_DROP, CALL_RELEASE;

	public String toString() {
        String r_value;
        r_value = name().charAt(0) + name().substring(1).toLowerCase();
        r_value = r_value.replace("_"," ");

        return r_value;
	}
}



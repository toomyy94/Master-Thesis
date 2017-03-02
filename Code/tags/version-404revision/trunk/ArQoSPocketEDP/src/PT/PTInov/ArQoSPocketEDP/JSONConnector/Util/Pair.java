package PT.PTInov.ArQoSPocketEDP.JSONConnector.Util;

import PT.PTInov.ArQoSPocketEDP.DataStructs.EnumSendInformationResult;


public class Pair {
	
	private EnumSendInformationResult key = null;
	private String Value = null;
	
	

	public Pair(EnumSendInformationResult key, String value) {
		this.key = key;
		this.Value = value;
	}
	
	public String getValue() {
		return Value;
	}
	
	public EnumSendInformationResult getKey() {
		return key;
	}

	public String toString() {
		return "Key :"+key+" Value :"+Value;
	}
}

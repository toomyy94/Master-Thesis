package PT.PTInov.ArQoSPocketEDP.JSONConnector;

import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.RunServiceResponse;

public class M2MIPService extends RunService {
	
	private final String tag = "M2MIPService";
	public static final String testName = "M2M_IP";

	public M2MIPService(String userName, String password, String ip, String port) {
		super(userName, password, ip, port);
	}
	
	
	public RunServiceResponse runService(String module, String msisdn, String ip, String probeID) {
		return super.runService(module, msisdn, ip, testName, probeID);
	}

}
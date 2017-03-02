package PT.PTInov.ArQoSPocketWiFi.Service;

import java.util.Date;
import java.util.List;

import PT.PTInov.ArQoSPocketWiFi.Utils.StoreInformation;

public interface EngineServiceInterface {

	public boolean saveActualInformation(String userListLocation, Date testExecDate);
	//public List<StoreInformation> getMyHistory();
	public List<StoreInformation> getAllResult();
	public void setServerHost(String ip, String port);
	public String getPort();
	public String getIP();
	
	public void refreshInformation();
}

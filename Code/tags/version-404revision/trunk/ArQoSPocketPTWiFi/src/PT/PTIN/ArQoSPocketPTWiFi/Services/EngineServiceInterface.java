package PT.PTIN.ArQoSPocketPTWiFi.Services;

import java.util.Date;
import java.util.List;

import android.os.Handler;

import PT.PTIN.ArQoSPocketPTWiFi.Store.StoreInformation;

public interface EngineServiceInterface {

	// Set actions
	public void doTest();
	public void setHandlerTestDone(Handler ref);
	public void setHandlerReportTestAction(Handler ref);
	
	
	// Get Service state Information
	public boolean isRunningTest();
	public List<StoreInformation> getAllStoreInformation();
	
	public void setServerHost(String ip, String port);
	public String getPort();
	public String getIP();
	
}

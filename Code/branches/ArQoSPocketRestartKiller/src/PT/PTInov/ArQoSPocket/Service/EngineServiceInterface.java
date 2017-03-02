package PT.PTInov.ArQoSPocket.Service;

import android.annotation.SuppressLint;
import android.os.Handler;

import java.util.Date;
import java.util.List;

import PT.PTInov.ArQoSPocket.UI.TestResultCallback;
import PT.PTInov.ArQoSPocket.Utils.RowDataTwoLines;
import PT.PTInov.ArQoSPocket.Utils.StoreAllTestInformation;
import PT.PTInov.ArQoSPocket.Utils.StoreInformation;

@SuppressLint("ParserError")
public interface EngineServiceInterface {

	//public boolean saveActualInformation(String userListLocation);
	public boolean doTest(String userListLocation, Date testExecDate, Handler handlerReportTestAction);
	public List<StoreAllTestInformation> getMyHistory();
	public List<StoreAllTestInformation> getAllResult();
	
	public void setServerHost(String ip, String port);
	public String getPort();
	public String getIP();
}

package PT.PTInov.ArQoSPocketEDP.Service;

import java.net.URL;
import java.util.Date;

import PT.PTInov.ArQoSPocketEDP.DataStructs.EnumTestE2EState;
import PT.PTInov.ArQoSPocketEDP.UI.MainActivityDoTestTaskInterfaceCallBack;
import PT.PTInov.ArQoSPocketEDP.Utils.LogType;
import PT.PTInov.ArQoSPocketEDP.Utils.Logger;
import android.annotation.SuppressLint;
import android.os.AsyncTask;

public class DoTestE2E extends AsyncTask<URL, Integer, EnumTestE2EState> {

	private final static String tag = "DoTestE2EDadosTask";
	
	private String MSISDN = null;
	private String IPAddr = null;
	private Date testExecDate = null;
	private int flagTest = -1;
	private MainActivityDoTestTaskInterfaceCallBack callbackRef = null;
	private EngineServiceInterface engineService = null;
	private boolean stopTest = false;
	
	private static DoTestE2E myInternalPointer = null;
	
	public DoTestE2E(String MSISDN, String IPAddr, Date testExecDate, int flagTest, MainActivityDoTestTaskInterfaceCallBack callbackRef, EngineServiceInterface engineService) {
		String methodName = "DoTestE2E";
		
		this.MSISDN = MSISDN;
		this.IPAddr = IPAddr;
		this.testExecDate = testExecDate;
		this.flagTest = flagTest;
		this.callbackRef = callbackRef;
		this.engineService = engineService;
		stopTest = false;
		
		try {
			
			if (myInternalPointer != null)
				myInternalPointer.stopTest();
			
			myInternalPointer = this;
			
		} catch (Exception ex) {
			Logger.v(tag, methodName, LogType.Error, "Error" + ex.toString());
		}
	}
	
	public void stopTest() {
		String methodName = "stopTest";
		
		Logger.v(tag, methodName, LogType.Trace, "In");
		stopTest = true;
	}

	@Override
	protected EnumTestE2EState doInBackground(URL... params) {
		String methodName = "doInBackground";
		
		try {
			
			Logger.v(tag, methodName, LogType.Trace, "MSISDN :"+MSISDN);
			Logger.v(tag, methodName, LogType.Trace, "IPAddr :"+IPAddr);
			
			// if have IP defined --> use doE2ETestM2MDadosService
			if (!IPAddr.equals(""))
				return engineService.doE2ETestIP(MSISDN, IPAddr, testExecDate, flagTest);
			
			// else --> use doE2ETestM2MCallService
			return engineService.doE2ETestCSD(MSISDN, IPAddr, testExecDate, flagTest);
			
			
		} catch(Exception ex) {
			Logger.v(tag, methodName, LogType.Error, "Error"+ex.toString());
		}
		return EnumTestE2EState.MODULE_UNAVAILABLE;
	}
	
	@SuppressLint("ParserError")
	protected void onPostExecute(EnumTestE2EState result){
		String methodName = "onPostExecute";
		
		try {
			
			Logger.v(tag, methodName, LogType.Trace, "In - result:"+result);
		
			if (!stopTest){
				
				Logger.v(tag, methodName, LogType.Trace, "doCallBack");
				callbackRef.testDone(result);
			}
		
		} catch(Exception ex) {
			Logger.v(tag, methodName, LogType.Error, "Error"+ex.toString());
		}
		
		Logger.v(tag, methodName, LogType.Trace, "Out");
	}
}

package PT.PTInov.ArQoSPocket.Service;

import java.net.URL;
import java.util.Date;

import PT.PTInov.ArQoSPocket.Enums.TestEnum;
import PT.PTInov.ArQoSPocket.UI.TestResultCallback;
import PT.PTInov.ArQoSPocket.Utils.LogType;
import PT.PTInov.ArQoSPocket.Utils.Logger;
import PT.PTInov.ArQoSPocket.Utils.TestEnumToInt;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class DoTestWorker extends AsyncTask<URL, Integer, Boolean>{
	
	private final static String tag = "DoTestWorker";
	
	private Date testExecDate = null;
	private String userListLocation = null;
	
	private TestResultCallback callbackRef = null;
	private EngineServiceInterface engineService = null;
	private boolean stopTest = false;
	
	private static DoTestWorker myInternalPointer = null;
	
	private Handler handlerReportTestAction = null;
	
	public DoTestWorker(String userListLocation, TestResultCallback callbackRef, EngineServiceInterface engineService) {
		String methodName = "DoTestE2E";
		
		this.testExecDate = new Date();
		this.userListLocation = userListLocation;
		
		this.callbackRef = callbackRef;
		this.engineService = engineService;
		stopTest = false;
		
		try {
			
			handlerReportTestAction = new Handler() {

				public void handleMessage(Message msg) {
					Logger.v(tag, "handlerReportTestAction", LogType.Trace, "In");
					
					publishProgress(msg.arg1);			
				}
			};
			
			if (myInternalPointer != null)
				myInternalPointer.stopTest();
			
			myInternalPointer = this;
			
		} catch (Exception ex) {
			Logger.v(tag, methodName, LogType.Error, "Error" + ex.toString());
		}
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		String methodName = "onProgressUpdate";
		
		
		try {
			Logger.v(tag, methodName, LogType.Trace, "In");
			
			TestEnum state = TestEnumToInt.intToTestEnum(values[0]);
			callbackRef.reportTestState(state);
		} catch(Exception ex) {
			Logger.v(tag, methodName, LogType.Error, "Error" + ex.toString());
		}
	}
	
	public void stopTest() {
		String methodName = "stopTest";
		
		Logger.v(tag, methodName, LogType.Trace, "In");
		stopTest = true;
	}

	@SuppressLint("HandlerLeak")
	@Override
	protected Boolean doInBackground(URL... params) {
		String methodName = "doInBackground";
		
		try {
			
			return engineService.doTest(userListLocation, testExecDate, handlerReportTestAction);
			
		} catch(Exception ex) {
			Logger.v(tag, methodName, LogType.Error, "Error"+ex.toString());
		}
		return false;
	}
	
	@SuppressLint("ParserError")
	protected void onPostExecute(Boolean result){
		String methodName = "onPostExecute";
		
		try {
			
			Logger.v(tag, methodName, LogType.Trace, "In");
		
			if (!stopTest){
				
				Logger.v(tag, methodName, LogType.Trace, "doCallBack");
				callbackRef.testResultCallBack(result, testExecDate);
			}
		
		} catch(Exception ex) {
			Logger.v(tag, methodName, LogType.Error, "Error"+ex.toString());
		}
		
		Logger.v(tag, methodName, LogType.Trace, "Out");
	}

}

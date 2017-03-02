package PTInov.IEX.ArQoSPocket.ResultLogs;

import java.util.ArrayList;

import android.util.Log;

public class TestLogsResult {
	
	private static final String tag = "TestLogsResult";

	private String IDMacro;
	private String taskNumber;
	private String dataInicio;
	private String dataFim;
	private String taskID;
	private String ICCID;
	private String InfoCelula;
	private String infoGPS;
	private String errorStatus;
	private ArrayList<String> params;
	
	public TestLogsResult(String pIDMacro, String ptaskNumber, String pdataInicio, 
			String pdataFim, String ptaskID, String pICCID, String pInfoCelula, String pinfoGPS,
			String perrorStatus, ArrayList<String> pparams) {
		IDMacro = pIDMacro;
		taskNumber = ptaskNumber;
		dataInicio = pdataInicio;
		dataFim = pdataFim;
		taskID = ptaskID;
		ICCID = pICCID;
		InfoCelula = pInfoCelula;
		infoGPS = pinfoGPS;
		errorStatus = perrorStatus;
		params = pparams;
	}
	
	public String toString() {
		return toFile();
	}
	
	public String toFile() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("|");
		sb.append(IDMacro);
		sb.append("|");
		sb.append(taskNumber);
		sb.append("|");
		sb.append(dataInicio);
		sb.append("|");
		sb.append(dataFim);
		sb.append("|");
		sb.append(taskID);
		sb.append("|");
		sb.append(ICCID);
		sb.append("|");
		sb.append(InfoCelula);
		sb.append("|");
		sb.append(infoGPS);
		sb.append("|");
		sb.append(errorStatus);
		sb.append("|");
		
		if (params == null) return sb.toString();
		
		//aficiona a lista de parametro
		for (String s : params) {
			sb.append(s);
			sb.append("|");
		}
		
		return sb.toString();
	}

	public String getIDMacro() {
		return IDMacro;
	}
	
	public String getTaskNumber() {
		return taskNumber;
	}
	
	public String getDataInicio() {
		return dataInicio;
	}
	
	public String getDataFim() {
		return dataFim;
	}
	
	public String getTaskID() {
		return taskID;
	}
	
	public String getICCID() {
		return ICCID;
	}
	
	public String getInfoCelula() {
		return InfoCelula;
	}
	
	public String getInfoGPS() {
		return infoGPS;
	}
	
	public String getErrorStatus() {
		return errorStatus;
	}
	
	public ArrayList<String> getListparams() {
		return params;
	}
	
	public String getListparams(int index) {
		return params.get(index);
	}
	
	public TestLogsResult clone() {
		
		ArrayList<String> pparamsClone = new ArrayList<String>();
		
		if (params != null) {
			for (String s:params) {
				pparamsClone.add(new String(s));
			}
		}
		
		String mIDMacro = null;
		if (IDMacro!=null) mIDMacro = new String(IDMacro);
		
		String mtaskNumber = null;
		if (taskNumber!=null) mtaskNumber = new String(taskNumber);
		
		String mdataInicio = null;
		if (dataInicio!=null) mdataInicio = new String(dataInicio); 
		
		String mdataFim = null;
		if (dataFim!=null) mdataFim = new String(dataFim);
		
		String mtaskID = null;
		if (taskID!=null) mtaskID = new String(taskID);
		
		String mICCID = null;
		if (ICCID!=null) mICCID = new String(ICCID);
		
		String mInfoCelula = null;
		if (InfoCelula!=null) mInfoCelula = new String(InfoCelula);
		
		String minfoGPS = null;
		if (infoGPS!=null) minfoGPS = new String(infoGPS);
		
		String merrorStatus = null; 
		if (errorStatus!=null) merrorStatus = new String(errorStatus);
		
		return new TestLogsResult(mIDMacro, mtaskNumber, mdataInicio, mdataFim, mtaskID, mICCID, mInfoCelula, minfoGPS, merrorStatus, pparamsClone);
	}
}

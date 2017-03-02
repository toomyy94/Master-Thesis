package PTInov.IEX.ArQoSPocket.ResultLogs;

import java.io.Serializable;
import java.util.ArrayList;

import android.util.Log;

public class TestLogsResult implements Serializable {
	

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
}

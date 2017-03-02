package PTInov.IEX.ArQoSPocket.ParserFileTasks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;


import PTInov.IEX.ArQoSPocket.ServicesInterfaces.TaskInterface;
import android.util.Log;

public class TaskStruct implements TaskInterface,Serializable {
	
	private static String tag = "TaskStruct";

	private String IDmacro;
	private String taskNumber;
	private String ICCID;
	
	// Tempo em segundos relativamente ao inicio do teste
	private long tempoInicioSegundos;
	private String timeOut;
	private String IDTarefa;
	private String execNow;
	private String dependencia;
	private ArrayList<String> paramList;
	
	
	public TaskStruct(String pIDmacro, String ptaskNumber, String pICCID, long ptempoInicioSegundos, String ptimeOut, String pIDTarefa, String pexecNow, String pdependencia, ArrayList<String> pparamList) {
		IDmacro = pIDmacro;
		taskNumber = ptaskNumber;
		ICCID = pICCID;
		tempoInicioSegundos = ptempoInicioSegundos;
		timeOut = ptimeOut;
		IDTarefa = pIDTarefa;
		execNow = pexecNow;
		dependencia = pdependencia;
		paramList = pparamList;
	}
	
	/*
	public void updateData(Date d, Date baseDate) {
		Date myActualDate = new Date();
		myActualDate.setTime(dataInicio.getTime());
		
		long baseDelay = dataInicio.getTime()-baseDate.getTime();
		dataInicio.setTime(d.getTime()+baseDelay);
	}
	
	public void addDelayData(long delayInMiliSec, Date baseDate) {
		
		Date myActualDate = new Date();
		myActualDate.setTime(dataInicio.getTime());
		
		//Verifica a diferença para a data da task anterior, essa diferença deve ser mantida
		
		if (baseDate == null) {
		
			Date d = new Date();
			dataInicio.setTime(d.getTime()+delayInMiliSec);
		} else {
			long baseDelay = dataInicio.getTime()-baseDate.getTime();
			Log.v(tag, "baseDelay: "+baseDelay);
			
			Date d = new Date();
			dataInicio.setTime(d.getTime()+delayInMiliSec+baseDelay);
		}
	}*/
	
	public String getIDmacro() {
		return IDmacro;
	}
	
	public String getTaskNumber() {
		return taskNumber;
	}
	
	public String getICCID() {
		return ICCID;
	}
	
	public long getDataInicio() {
		return tempoInicioSegundos;
	}	
	
	public String getTimeOut() {
		return timeOut;
	}
	
	public String getTaskId() {
		return IDTarefa;
	}
	
	public String getExecNow() {
		return execNow;
	}
	
	public String getDependencia() {
		return dependencia;
	}
	
	public ArrayList<String> getparamList() {
		return paramList;
	}
	
	public String toString() {
		return "IDmacro:"+IDmacro+"|\ntaskNumber:"+taskNumber+"|\nICCID:"+ICCID+"|\nInstanteDeInicio:"+tempoInicioSegundos+"|\ntimeOut:"+timeOut+"|\nIDTarefa:"+IDTarefa+"|\nexecNow:"+execNow+"|\ndependencia:"+dependencia+"|\nListaParametros:"+paramList.toString();
	}
	
	public TaskStruct clone() {
		ArrayList<String> paramListclone = new ArrayList<String>();
		
		for (String s:paramList) {
			paramListclone.add(new String(s));
		}
		
		return new TaskStruct(new String(IDmacro), new String(taskNumber), new String(ICCID), tempoInicioSegundos, new String(timeOut), new String(IDTarefa), new String(execNow), new String(dependencia), paramListclone);
	}
}

package PTInov.IEX.ArQoSPocketService.CallService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import android.util.Log;

public class TaskStruct implements TaskInterface,Serializable {
	
	private static String tag = "TaskStruct";

	private String IDmacro;
	private String taskNumber;
	private String ICCID;
	private String dataInicio;
	private String timeOut;
	private String IDTarefa;
	private String execNow;
	private String dependencia;
	private ArrayList<String> paramList;
	
	public TaskStruct(String pIDmacro, String ptaskNumber, String pICCID, String pdataInicio, String ptimeOut, String pIDTarefa, String pexecNow, String pdependencia, ArrayList<String> pparamList) {
		IDmacro = pIDmacro;
		taskNumber = ptaskNumber;
		ICCID = pICCID;
		dataInicio = pdataInicio;
		timeOut = ptimeOut;
		IDTarefa = pIDTarefa;
		execNow = pexecNow;
		dependencia = pdependencia;
		paramList = pparamList;
	}
	
	public String getIDmacro() {
		return IDmacro;
	}
	
	public String getTaskNumber() {
		return taskNumber;
	}
	
	public String getICCID() {
		return ICCID;
	}
	
	public String getDataInicio() {
		return dataInicio;
	}
	
	public Date getDataInicioInDateType() {
		Date d = new Date();
		
		String data = dataInicio.substring(0, dataInicio.indexOf("T"));
		String time = dataInicio.substring(dataInicio.indexOf("T")+1, dataInicio.indexOf("."));
		
		int indexbase = data.indexOf("-"); 
		String ano = data.substring(0, indexbase);
		String mes = data.substring(indexbase+1, data.indexOf("-", indexbase+1));
		String dia = data.substring(data.indexOf("-", indexbase+1)+1,data.length());
		
		indexbase = time.indexOf(":"); 
		String hora = time.substring(0, indexbase);
		String min = time.substring(indexbase+1, time.indexOf(":", indexbase+1));
		String sec = time.substring(time.indexOf(":", indexbase+1)+1,time.length());
		
		Log.v(tag, "data: "+data+" time: "+time);
		Log.v(tag, "ano: "+ano+" mes: "+mes+" dia:"+dia);
		Log.v(data, "hora: "+hora+" min: "+min+" sec:"+sec);
		
		d.setYear(Integer.parseInt(ano)-1900);
		d.setMonth(Integer.parseInt(mes)-1);
		d.setDate(Integer.parseInt(dia));
		
		d.setHours(Integer.parseInt(hora));
		d.setMinutes(Integer.parseInt(min));
		d.setSeconds(Integer.parseInt(sec));
		
		return d;
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
		if (paramList != null)
			return "IDmacro: "+IDmacro+" taskNumber: "+taskNumber+" ICCID: "+ICCID+" dataInicio: "+dataInicio+" timeOut: "+timeOut+" IDTarefa: "+IDTarefa+" execNow: "+execNow+" dependencia: "+dependencia+" ListaParametros: "+paramList.toString();
		else
			return "IDmacro: "+IDmacro+" taskNumber: "+taskNumber+" ICCID: "+ICCID+" dataInicio: "+dataInicio+" timeOut: "+timeOut+" IDTarefa: "+IDTarefa+" execNow: "+execNow+" dependencia: "+dependencia+" ListaParametros: empty";
	}
}

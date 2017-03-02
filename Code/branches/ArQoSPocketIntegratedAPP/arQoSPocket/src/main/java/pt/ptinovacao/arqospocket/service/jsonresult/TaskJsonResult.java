package pt.ptinovacao.arqospocket.service.jsonresult;

import java.io.Serializable;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.enums.EConnectionTechnology;
import pt.ptinovacao.arqospocket.service.enums.ETestTaskState;
import pt.ptinovacao.arqospocket.service.interfaces.ITask;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.structs.MyLocation;


public abstract class TaskJsonResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(TaskJsonResult.class);
	
	protected String task_id;
	protected String task_name;
	protected String macro_id;
	protected String task_number;
	protected String init_date;
	protected String end_date;
	protected String iccid;
	protected String cell_id;
	protected String loc_gps;
	protected String status;
	
	protected Date dateini;
	protected Date datefim;
	
	protected MyLocation taskLocation = null;
	
	protected EConnectionTechnology execution_in_technology = EConnectionTechnology.NA;
	
	public TaskJsonResult(String task_id, String task_name, String macro_id, 
			String task_number, String iccid, String cell_id, String loc_gps, 
			String status, Date dateini, Date datefim, MyLocation taskLocation, EConnectionTechnology execution_in_technology) {
		final String method = "TaskJsonResult";
		
		try {
			
			this.task_id = task_id;
			this.task_name = task_name;
			this.macro_id = macro_id;
			this.task_number = task_number;
			
			this.dateini = dateini;
			this.datefim = datefim;
			
			this.iccid = iccid;
			this.cell_id = cell_id;
			this.loc_gps = loc_gps;
			this.status = status;
			
			this.taskLocation = taskLocation;
			
			this.execution_in_technology = execution_in_technology;
			
			this.init_date = dateini.getYear()+dateini.getMonth()+dateini.getDate()+dateini.getHours()+dateini.getMinutes()+dateini.getSeconds()+"";
			this.end_date = datefim.getYear()+datefim.getMonth()+datefim.getDate()+datefim.getHours()+datefim.getMinutes()+datefim.getSeconds()+"";
			
			
			MyLogger.debug(logger, method, "task_id :"+task_id);
			MyLogger.debug(logger, method, "task_name :"+task_name);
			MyLogger.debug(logger, method, "macro_id :"+macro_id);
			MyLogger.debug(logger, method, "task_number :"+task_number);
			MyLogger.debug(logger, method, "dateini :"+dateini);
			MyLogger.debug(logger, method, "datefim :"+datefim);
			MyLogger.debug(logger, method, "iccid :"+iccid);
			MyLogger.debug(logger, method, "cell_id :"+cell_id);
			MyLogger.debug(logger, method, "loc_gps :"+loc_gps);
			MyLogger.debug(logger, method, "status :"+status);
			MyLogger.debug(logger, method, "init_date :"+init_date);
			MyLogger.debug(logger, method, "end_date :"+end_date);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public abstract ETestTaskState get_task_state();
	
	public String get_task_id() {
		return task_id;
	}
	
	public String get_task_name() {
		return task_name;
	}
	
	public String get_macro_id() {
		return macro_id;
	}
	
	public String get_task_number() {
		return task_number;
	}
	
	public Date get_dateini() {
		return dateini;
	}
	
	public Date get_datefim() {
		return datefim;
	}
	
	public String get_iccid() {
		return iccid;
	}
	
	public String get_cell_id() {
		return cell_id;
	}
	
	public String get_loc_gps() {
		return loc_gps;
	}
	
	public String get_status() {
		return status;
	}
	
	public String get_init_date() {
		return init_date;
	}
	
	public String get_end_date() {
		return end_date;
	}
	
	public MyLocation get_task_execution_location() {
		return taskLocation;
	}
	
	public EConnectionTechnology get_task_technology() {
		return execution_in_technology;
	}

	public String getAudioFileName() {
		return null;
	}

    public void setAudioFileName(String fileName) {

    }
	
	public String buildTaskJsonResult() {
		final String method = "buildTaskJsonResult";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("{");
			sb.append("\"task_id\":\""+task_id+"\",");
			sb.append("\"task_name\":\""+task_name+"\",");
			sb.append("\"macro_id\":\""+macro_id+"\",");
			sb.append("\"task_number\":\""+task_number+"\",");
			sb.append("\"init_date\":\""+init_date+"\",");
			sb.append("\"end_date\":\""+end_date+"\",");
			sb.append("\"iccid\":\""+iccid+"\",");
			sb.append("\"cell_id\":\""+cell_id+"\",");
			sb.append("\"loc_gps\":\""+loc_gps+"\",");
			sb.append("\"status\":\""+status+"\"");
			sb.append("}");
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}

	public String buildTaskStringResult() {
		final String method = "buildTaskJsonResult";

		StringBuilder sb = new StringBuilder();

		try {
			sb.append(macro_id + "|");
			sb.append(task_number + "|");
			sb.append(init_date + "|");
			sb.append(end_date + "|");
			sb.append(task_id + "|");
			sb.append(iccid + "|");
			sb.append(cell_id + "|");
			sb.append(loc_gps + "|");
			sb.append(status + "|");
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}

		return sb.toString().replace("null", "");
	}
	
	public boolean equals(TaskJsonResult taskStruct) {
		
		if (!this.task_id.equals(taskStruct.task_id))
			return false;
		
		if (!this.task_name.equals(taskStruct.task_name))
			return false;
		
		if (!this.task_number.equals(taskStruct.task_number))
			return false;
		
		return true;
	}
	
	public boolean equals(ITask iTask) {
		
		if (!this.task_id.equals(iTask.get_task_id()))
			return false;
		
		if (!this.task_name.equals(iTask.get_task_name()))
			return false;
		
		if (!this.task_number.equals(iTask.get_task_number()))
			return false;
		
		return true;
	}

	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\ntask_id :"+task_id);
			sb.append("\ntask_name :"+task_name);
			sb.append("\nmacro_id :"+macro_id);
			sb.append("\ntask_number :"+task_number);
			sb.append("\ndateini :"+dateini);
			sb.append("\ndatefim :"+datefim);
			sb.append("\niccid :"+iccid);
			sb.append("\ncell_id :"+cell_id);
			sb.append("\nloc_gps :"+loc_gps);
			sb.append("\nstatus :"+status);
			sb.append("\ninit_date :"+init_date);
			sb.append("\nend_date :"+end_date);			
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
	
	public abstract Object clone();
}

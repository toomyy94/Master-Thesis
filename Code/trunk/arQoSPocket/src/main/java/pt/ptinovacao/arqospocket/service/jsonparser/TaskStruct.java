package pt.ptinovacao.arqospocket.service.jsonparser;

import java.io.Serializable;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.interfaces.ITask;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public abstract class TaskStruct implements ITask, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(TaskStruct.class);

	protected String jObjectSerialized;;
	
	private String task_id;
	private String task_name;
	private String macro_id;
	private String task_number;
	private String iccid;
	private String instanteExec;
	private String timeout;
	private String immediate;
	
	public TaskStruct(String task_id, String task_name, String macro_id, String task_number, String iccid,
			String instanteExec, String timeout, String immediate) {
		final String method = "TaskStruct - all";
		
		try {
			
			this.task_id = task_id;
			this.task_name = task_name;
			this.macro_id = macro_id;
			this.task_number = task_number;
			this.iccid = iccid;
			this.instanteExec = instanteExec;
			this.timeout = timeout;
			this.immediate = immediate;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public TaskStruct(JSONObject jObject) {
		final String method = "TaskStruct";
		
		try {
			
			//this.jObject = jObject;
			
			this.task_id = jObject.getString("task_id");
			this.task_name = jObject.getString("task_name");
			this.macro_id = jObject.getString("macro_id");
			this.task_number = jObject.getString("task_number");
			this.iccid = jObject.getString("iccid");
			this.instanteExec = jObject.getString("instanteExec");
			this.timeout = jObject.getString("timeout");
			this.immediate = jObject.getString("immediate");
			
			MyLogger.debug(logger, method, "task_id :"+task_id);
			MyLogger.debug(logger, method, "task_name :"+task_name);
			MyLogger.debug(logger, method, "macro_id :"+macro_id);
			MyLogger.debug(logger, method, "task_number :"+task_number);
			MyLogger.debug(logger, method, "iccid :"+iccid);
			MyLogger.debug(logger, method, "instanteExec :"+instanteExec);
			MyLogger.debug(logger, method, "timeout :"+timeout);
			MyLogger.debug(logger, method, "immediate :"+immediate);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	public TaskStruct(String[] parametersArray, String taskName) {
		final String method = "TaskStruct";

		try {

			this.macro_id = parametersArray[0];;
			this.task_number = parametersArray[1];
			this.iccid = parametersArray[2];
			this.instanteExec = parametersArray[3];
			this.timeout = parametersArray[4];
			this.task_id = parametersArray[5];
			this.immediate = parametersArray[6];
			this.task_name = taskName;

			MyLogger.debug(logger, method, "task_id :"+task_id);
			MyLogger.debug(logger, method, "task_name :"+task_name);
			MyLogger.debug(logger, method, "macro_id :"+macro_id);
			MyLogger.debug(logger, method, "task_number :"+task_number);
			MyLogger.debug(logger, method, "iccid :"+iccid);
			MyLogger.debug(logger, method, "instanteExec :"+instanteExec);
			MyLogger.debug(logger, method, "timeout :"+timeout);
			MyLogger.debug(logger, method, "immediate :"+immediate);

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
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
	
	public String get_iccid() {
		return iccid;
	}
	
	public String get_instanteExec() {
		return instanteExec;
	}
	
	public String get_timeout() {
		return timeout;
	}
	
	public String get_immediate() {
		return immediate;
	}
	
	public boolean equals(TaskStruct taskStruct) {
		
		if (!this.task_id.equals(taskStruct.task_id))
			return false;
		
		if (!this.task_name.equals(taskStruct.task_name))
			return false;
		
		if (!this.task_number.equals(taskStruct.task_number))
			return false;
		
		if (!this.instanteExec.equals(taskStruct.instanteExec))
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
			sb.append("\niccid :"+iccid);
			sb.append("\ninstanteExec :"+instanteExec);
			sb.append("\ntimeout :"+timeout);
			sb.append("\nimmediate :"+immediate);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
	
	public abstract Object clone();
}

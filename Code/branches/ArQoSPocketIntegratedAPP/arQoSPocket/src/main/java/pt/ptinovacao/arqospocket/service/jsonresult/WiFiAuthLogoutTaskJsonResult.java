package pt.ptinovacao.arqospocket.service.jsonresult;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.service.enums.EConnectionTechnology;
import pt.ptinovacao.arqospocket.service.enums.ERunTestTaskState;
import pt.ptinovacao.arqospocket.service.enums.ETestTaskState;
import pt.ptinovacao.arqospocket.service.interfaces.ITaskResult;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.structs.MyLocation;

import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_OK;

public class WiFiAuthLogoutTaskJsonResult extends TaskJsonResult implements ITaskResult, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(WiFiAuthLogoutTaskJsonResult.class);

	private String response_code;
	private String response_time;

	public WiFiAuthLogoutTaskJsonResult(String task_id, String task_name,
			String macro_id, String task_number, String iccid, String cell_id,
			String loc_gps, String status, Date dateini, Date datefim, String response_code, 
			String response_time, MyLocation taskLocation, EConnectionTechnology execution_in_technology) {
		
		super(task_id, task_name, macro_id, task_number, iccid, cell_id, loc_gps,
				status, dateini, datefim, taskLocation, execution_in_technology);
		
		final String method = "WiFiAuthLogoutTaskJsonResult";
		
		try {
		
			this.response_code = response_code;
			this.response_time = response_time;
			
			MyLogger.debug(logger, method, "response_code :"+response_code);
			MyLogger.debug(logger, method, "response_time :"+response_time);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public String get_response_code() {
		return response_code;
	}
	
	public String get_response_time() {
		return response_time;
	}
	
	public String buildTaskJsonResult() {
		final String method = "buildTaskJsonResult";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("{");
			sb.append("\"task_id\":\""+super.get_task_id()+"\",");
			sb.append("\"task_name\":\""+super.get_task_name()+"\",");
			sb.append("\"macro_id\":\""+super.get_macro_id()+"\",");
			sb.append("\"task_number\":\""+super.get_task_number()+"\",");
			sb.append("\"init_date\":\""+super.get_init_date()+"\",");
			sb.append("\"end_date\":\""+super.get_end_date()+"\",");
			sb.append("\"iccid\":\""+super.get_iccid()+"\",");
			sb.append("\"cell_id\":\""+super.get_cell_id()+"\",");
			sb.append("\"loc_gps\":\""+super.get_loc_gps()+"\",");
			sb.append("\"status\":\""+super.get_status()+"\",");
			sb.append("\"response_code\":\""+response_code+"\",");
			sb.append("\"response_time\":\""+response_time+"\"");
			sb.append("}");
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
	

	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\ntask_id :"+super.get_task_id());
			sb.append("\ntask_name :"+super.get_task_name());
			sb.append("\nmacro_id :"+super.get_macro_id());
			sb.append("\ntask_number :"+super.get_task_number());
			sb.append("\ndateini :"+super.get_dateini());
			sb.append("\ndatefim :"+super.get_datefim());
			sb.append("\niccid :"+super.get_iccid());
			sb.append("\ncell_id :"+super.get_cell_id());
			sb.append("\nloc_gps :"+super.get_loc_gps());
			sb.append("\nstatus :"+super.get_status());
			sb.append("\ninit_date :"+super.get_init_date());
			sb.append("\nend_date :"+super.get_end_date());
			sb.append("\nresponse_code :"+response_code);
			sb.append("\nresponse_time :"+response_time);		
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
	
	
	/************
	 * 
	 * 		ITaskResult
	 * 
	 * ***********************/

	@Override
	public ETestTaskState get_task_state() {
		final String method = "get_task_state";
		
		try {
			return super.get_status().startsWith(STATUS_OK)?ETestTaskState.OK:ETestTaskState.NOK;
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}

	@Override
	public ERunTestTaskState get_run_task_state() {

		return ERunTestTaskState.DONE;
	}

	@Override
	public Map<String, String> get_task_results(Context context) {
		final String method = "get_task_results";
		
		try {
			
			Map<String, String> task_result_list = new TreeMap<String, String>();
			
			task_result_list.put(context.getString(R.string.task_result_wifi_authentication_answer), response_code);
			task_result_list.put(context.getString(R.string.task_result_response_time), response_time);
			
			return task_result_list;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}

	@Override
	public EConnectionTechnology get_task_technology() {
		return super.get_task_technology();
	}
	
	@Override
	public MyLocation get_test_execution_location() {
		return super.get_task_execution_location();
	}
	
	public Object clone() {
		final String method = "clone";
		
		try {
			
			return new WiFiAuthLogoutTaskJsonResult(super.task_id, super.task_name, super.macro_id, super.task_number, super.iccid, 
					super.cell_id, super.loc_gps, super.status, super.dateini, super.datefim, this.response_code,
					this.response_time, (MyLocation) super.taskLocation.clone(), super.execution_in_technology);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
}

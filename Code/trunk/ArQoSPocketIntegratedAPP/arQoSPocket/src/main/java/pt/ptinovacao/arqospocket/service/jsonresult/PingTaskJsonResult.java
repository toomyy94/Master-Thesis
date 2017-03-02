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
import pt.ptinovacao.arqospocket.service.enums.ETestTaskState;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.enums.ERunTestTaskState;
import pt.ptinovacao.arqospocket.service.interfaces.ITaskResult;
import pt.ptinovacao.arqospocket.service.structs.MyLocation;

import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_OK;

public class PingTaskJsonResult extends TaskJsonResult implements ITaskResult, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(PingTaskJsonResult.class);

	private String min_in_msec;
	private String med_in_msec;
	private String max_in_msec;
	private String sent_packets;
	private String received_packets;
	private String lost_packets;

	
	public PingTaskJsonResult(String task_id, String task_name,
			String macro_id, String task_number, String iccid, String cell_id,
			String loc_gps, String status, Date dateini, Date datefim, String min_in_msec,
			String med_in_msec, String max_in_msec, String sent_packets, String received_packets, 
			String lost_packets, MyLocation taskLocation, EConnectionTechnology execution_in_technology) {
		
		super(task_id, task_name, macro_id, task_number, iccid, cell_id, loc_gps,
				status, dateini, datefim, taskLocation, execution_in_technology);
		
		final String method = "PingTaskJsonResult";
		
		try {
		
			this.min_in_msec = min_in_msec;
			this.med_in_msec = med_in_msec;
			this.max_in_msec = max_in_msec;
			this.sent_packets = sent_packets;
			this.received_packets = received_packets;
			this.lost_packets = lost_packets;
			
			MyLogger.debug(logger, method, "min_in_msec :"+min_in_msec);
			MyLogger.debug(logger, method, "med_in_msec :"+med_in_msec);
			MyLogger.debug(logger, method, "max_in_msec :"+max_in_msec);
			MyLogger.debug(logger, method, "sent_packets :"+sent_packets);
			MyLogger.debug(logger, method, "received_packets :"+received_packets);
			MyLogger.debug(logger, method, "lost_packets :"+lost_packets);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public String get_min_in_msec() {
		return min_in_msec;
	}
	
	public String get_med_in_msec() {
		return med_in_msec;
	}
	
	public String get_max_in_msec() {
		return max_in_msec;
	}
	
	public String get_sent_packets() {
		return sent_packets;
	}
	
	public String get_received_packets() {
		return received_packets;
	}
	
	public String get_lost_packets() {
		return lost_packets;
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
			sb.append("\"min_in_msec\":\""+min_in_msec+"\",");
			sb.append("\"med_in_msec\":\""+med_in_msec+"\",");
			sb.append("\"max_in_msec\":\""+max_in_msec+"\",");
			sb.append("\"sent_packets\":\""+sent_packets+"\",");
			sb.append("\"received_packets\":\""+received_packets+"\",");
			sb.append("\"lost_packets\":\""+lost_packets+"\"");
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
			sb.append("\nmin_in_msec :"+min_in_msec);
			sb.append("\nmed_in_msec :"+med_in_msec);
			sb.append("\nmax_in_msec :"+max_in_msec);	
			sb.append("\nsent_packets :"+sent_packets);	
			sb.append("\nlost_packets :"+lost_packets);	
			
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
			
			task_result_list.put(context.getString(R.string.task_result_min_rtt_ms), min_in_msec);
			task_result_list.put(context.getString(R.string.task_result_max_rtt_ms), max_in_msec);
			task_result_list.put(context.getString(R.string.task_result_average_rtt_ms), med_in_msec);
			task_result_list.put(context.getString(R.string.task_result_sent_packets), sent_packets);
			task_result_list.put(context.getString(R.string.task_result_received_packets), received_packets);
			task_result_list.put(context.getString(R.string.task_result_lost_packets), lost_packets.replace("%", ""));
			
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
			
			return new PingTaskJsonResult(super.task_id, super.task_name, super.macro_id, super.task_number, super.iccid, 
					super.cell_id, super.loc_gps, super.status, super.dateini, super.datefim, this.min_in_msec,
					this.med_in_msec, this.max_in_msec, this.sent_packets, this.received_packets, this.lost_packets,
					(MyLocation) super.taskLocation.clone(), super.execution_in_technology);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
}

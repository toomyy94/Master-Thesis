package pt.ptinovacao.arqospocket;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pt.ptinovacao.arqospocket.service.enums.EConnectionTechnology;
import pt.ptinovacao.arqospocket.service.enums.ERunTestTaskState;
import pt.ptinovacao.arqospocket.service.enums.ETestTaskState;
import pt.ptinovacao.arqospocket.service.enums.ETestType;
import pt.ptinovacao.arqospocket.service.interfaces.ITaskResult;
import pt.ptinovacao.arqospocket.service.structs.MyLocation;
import pt.ptinovacao.arqospocket.util.History;
import pt.ptinovacao.arqospocket.util.Utils;
import android.content.Context;

public class HistoricoTestesItem extends History {

	private Context ctx;
	private String test_id;
	private String test_name;
	private ETestType test_type;
	private ETestTaskState test_state;
	private ERunTestTaskState run_test_state;
	private List<ITaskResult> task_list;
	private boolean test_already_sent;
	private int percent_done;
	private MyLocation location;
	private Date date_end_execution;
	private String date_end_execution_string;

	public HistoricoTestesItem(Context ctx, String test_id, String test_name,
			ETestType test_type, ETestTaskState test_state, ERunTestTaskState run_test_state,
			List<ITaskResult> task_list, boolean test_already_sent, int percent_done, MyLocation location,
			Date date_end_execution) {
		super();
		this.ctx = ctx;
		this.test_id = test_id;
		this.test_name = test_name;
		this.test_type = test_type;
		this.test_state = test_state;
		this.run_test_state = run_test_state;
		this.task_list = task_list;
		this.test_already_sent = test_already_sent;
		this.percent_done = percent_done;
		this.location = location;
		this.date_end_execution = date_end_execution;
		
		/* Convert Date to String. Format example: "14 Jan 2014, 18:45" */
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
		String anomalie_report_date_string = sdf.format(date_end_execution);
		this.date_end_execution_string = anomalie_report_date_string;
	}
	
	@Override
	public String getType() {
		EConnectionTechnology tech = getTechnologyType();
		
		if(tech != null) {
			switch(tech) {
				case WIFI: 
					return ctx.getString(R.string.wifi);
				case MOBILE: 
					return ctx.getString(R.string.mobile_network);
				case NOT_CONNECTED: 
					return ctx.getString(R.string.test_not_connected);
				case NA:
				default: 
					return Utils.EMPTY_STRING;
			}
		}
		
		return Utils.EMPTY_STRING;
	}
	
	public String getTest_id() {
		return test_id;
	}
	public void setTest_id(String test_id) {
		this.test_id = test_id;
	}
	public String getTest_name() {
		return test_name;
	}
	public void setTest_name(String test_name) {
		this.test_name = test_name;
	}
	public ETestType getTest_type() {
		return test_type;
	}
	public void setTest_type(ETestType test_type) {
		this.test_type = test_type;
	}
	public ETestTaskState getTest_state() {
		return test_state;
	}
	public void setTest_state(ETestTaskState test_state) {
		this.test_state = test_state;
	}
	public ERunTestTaskState getRun_test_state() {
		return run_test_state;
	}
	public void setRun_test_state(ERunTestTaskState run_test_state) {
		this.run_test_state = run_test_state;
	}
	public List<ITaskResult> getTask_list() {
		return task_list;
	}
	public void setTask_list(List<ITaskResult> task_list) {
		this.task_list = task_list;
	}
	public boolean isTest_already_sent() {
		return test_already_sent;
	}
	public void setTest_already_sent(boolean test_already_sent) {
		this.test_already_sent = test_already_sent;
	}
	public int getPercent_done() {
		return percent_done;
	}
	public void setPercent_done(int percent_done) {
		this.percent_done = percent_done;
	}		
	public MyLocation getLocation() {
		return location;
	}
	public void setLocation(MyLocation location) {
		this.location = location;
	}
	
	public EConnectionTechnology getTechnologyType() {
		EConnectionTechnology technology = null;

		if (task_list != null && task_list.size() > 0) {
			for (ITaskResult task : task_list) {
				if (technology == null)
					technology = task.get_task_technology();
				else if (technology != task.get_task_technology()&&
						!task.get_task_id().equals("0002") &&
								!task.get_task_id().equals("0007")&&
								!task.get_task_id().equals("0003") &&
										!task.get_task_id().equals("0008")) {
				
					technology = EConnectionTechnology.MIXED;
					}
						
				}
			}				

		return technology;
	}

	public Date getDate_end_execution() {
		return date_end_execution;
	}

	public void setDate_end_execution(Date date_end_execution) {
		this.date_end_execution = date_end_execution;
	}
	
	public String getDate_end_execution_string() {
		return date_end_execution_string;
	}

	public void setDate_end_execution_string(String date_end_execution) {
		this.date_end_execution_string = date_end_execution;
	}
	
}

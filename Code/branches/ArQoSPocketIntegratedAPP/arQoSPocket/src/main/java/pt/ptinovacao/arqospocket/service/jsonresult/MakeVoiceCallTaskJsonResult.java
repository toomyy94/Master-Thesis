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

public class MakeVoiceCallTaskJsonResult extends TaskJsonResult implements ITaskResult, Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(MakeVoiceCallTaskJsonResult.class);

	private String iccid;
	private String networkInformation;
	private String destinationNumber;
	private String callSetupTime;
	private String audioRecordFileName;

	public MakeVoiceCallTaskJsonResult(String task_id, String task_name,
									   String macro_id, String task_number, String iccid, String networkInformation,
									   String loc_gps, String status, Date dateini, Date datefim,
									   String destinationNumber, String callSetupTime, String audioRecordFileName,
									   MyLocation taskLocation, EConnectionTechnology execution_in_technology) {

		super(task_id, task_name, macro_id, task_number, iccid, networkInformation, loc_gps,
				status, dateini, datefim, taskLocation, execution_in_technology);

		final String method = "MakeVoiceCallTaskJsonResult";

		try {
			this.iccid = iccid;
			this.networkInformation = networkInformation;
			this.destinationNumber = destinationNumber;
			this.callSetupTime = callSetupTime;
			this.audioRecordFileName = audioRecordFileName;
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	@Override
	public String getAudioFileName() {
		return audioRecordFileName;
	}

	@Override
	public void setAudioFileName(String fileName) {
		audioRecordFileName = fileName;
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
			sb.append("\"iccid\":\""+iccid+"\",");
			sb.append("\"cell_id\":\""+super.get_cell_id()+"\",");
			sb.append("\"network_info\":\""+networkInformation+"\",");
			sb.append("\"loc_gps\":\""+super.get_loc_gps()+"\",");
			sb.append("\"status\":\""+super.get_status()+"\",");
			sb.append("\"destinationNumber\":\""+destinationNumber+"\",");
			sb.append("\"callSetupTime\":\""+callSetupTime+"\"");
			sb.append("\"audioRecordFileName\":\""+audioRecordFileName+"\"");
			sb.append("}");

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}

		return sb.toString();
	}

	@Override
	public String buildTaskStringResult() {
		final String method = "buildTaskJsonResult";

		StringBuilder sb = new StringBuilder();

		try {
			sb.append(super.buildTaskStringResult());
			sb.append("1|");
			sb.append(destinationNumber + "|");
			sb.append(callSetupTime + "|");
			sb.append("||||||"); //4-9
			sb.append(audioRecordFileName + "|");
			sb.append("|||||||||||||||||||"); //11-29

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}

		return sb.toString().replace("null", "");

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
			sb.append("\niccid :"+iccid);
			sb.append("\ncell_id :"+super.get_cell_id());
			sb.append("\nnetwork_info:"+networkInformation);
			sb.append("\nloc_gps :"+super.get_loc_gps());
			sb.append("\nstatus :"+super.get_status());
			sb.append("\ninit_date :"+super.get_init_date());
			sb.append("\nend_date :"+super.get_end_date());
			sb.append("\ndestinationNumber :"+destinationNumber);
			sb.append("\ncallSetupTime :"+callSetupTime);
			sb.append("\naudioRecordFilename :"+ audioRecordFileName);

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

			task_result_list.put(context.getString(R.string.task_result_destination_number), destinationNumber);
			task_result_list.put(context.getString(R.string.task_result_call_setup_time), callSetupTime);
            if (audioRecordFileName != null) {
                task_result_list.put(context.getString(R.string.task_result_audio_recording), audioRecordFileName);
            }

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

			return new MakeVoiceCallTaskJsonResult(super.task_id, super.task_name, super.macro_id,
                    super.task_number, super.iccid, super.cell_id, super.loc_gps, super.status,
                    super.dateini, super.datefim, destinationNumber, this.callSetupTime,
                    this.audioRecordFileName, (MyLocation) super.taskLocation.clone(), super.execution_in_technology);

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}

		return null;
	}
}

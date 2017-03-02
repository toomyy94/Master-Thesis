package pt.ptinovacao.arqospocket.service.jsonparser;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.AnswerVoiceCallTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.HTTPDownloadTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.HangUpVoiceCallTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.MakeVoiceCallTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.PingTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.ReassociateWiFiTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.RecordAudioTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.TurnOFFWiFiTaskStruct;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.AssociateWiFiTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.DeassociateWiFiTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.HTTPUploadTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.ScanWiFiNetworksTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.SendMMSTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.SendMailTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.SendSMSTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.TurnOFFMobileTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.TurnONMobileTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.TurnONWiFiTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.WiFiAuthLoginTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.WiFiAuthLogoutTaskStruct;

public class TaskIDtoObject {
	
	private final static Logger logger = LoggerFactory.getLogger(TaskIDtoObject.class);
	
	public final static String AWTS = "0006";
	public final static String DWTS = "0013";
	public final static String HDTS = "0004";
	public final static String HUTS = "0005";
	public final static String PTS = "0001";
	public final static String RWTS = "0009"; 
	public final static String SWNTS = "0012";
	public final static String SMTS = "";
	public final static String SMMSTS = "";
	public final static String SSTS = "";
	public final static String TOFFWTS = "0007";
	public final static String TONTS = "0008";
	public final static String VCTS = "0014";
	public final static String WALITS = "0010";
	public final static String WALOTS = "0011";
	public final static String TOMTS = "0002";
	public final static String TONMTS = "0003";

	public static TaskStruct taskID_to_Object(JSONObject jObject) {
		final String method = "taskID_to_Object";
		
		try {
			
			
			String task_id = jObject.getString("task_id");
			
			MyLogger.debug(logger, method, "task_id :"+task_id);
			
			if (task_id.equals(AWTS))// DONE
				return new AssociateWiFiTaskStruct(jObject);
			else if (task_id.equals(DWTS))// DONE
				return new DeassociateWiFiTaskStruct(jObject);
			else if (task_id.equals(HDTS)) // DONE
				return new HTTPDownloadTaskStruct(jObject);
			else if (task_id.equals(HUTS)) // DONE
				return new HTTPUploadTaskStruct(jObject);
			else if (task_id.equals(PTS)) // DONE
				return new PingTaskStruct(jObject);
			else if (task_id.equals(RWTS))// DONE
				return new ReassociateWiFiTaskStruct(jObject);
			else if (task_id.equals(SWNTS))// DONE
				return new ScanWiFiNetworksTaskStruct(jObject);
			else if (task_id.equals(SMTS))
				return new SendMailTaskStruct(jObject);
			else if (task_id.equals(SMMSTS))
				return new SendMMSTaskStruct(jObject);
			else if (task_id.equals(SSTS))
				return new SendSMSTaskStruct(jObject);
			else if (task_id.equals(TOFFWTS))// DONE
				return new TurnOFFWiFiTaskStruct(jObject);
			else if (task_id.equals(TONTS))// DONE
				return new TurnONWiFiTaskStruct(jObject);
			else if (task_id.equals(TOMTS)) // DONE
				return new TurnOFFMobileTaskStruct(jObject);
			else if (task_id.equals(TONMTS)) // DONE
				return new TurnONMobileTaskStruct(jObject);
			else if (task_id.equals(WALITS))// DONE
				return new WiFiAuthLoginTaskStruct(jObject);
			else if (task_id.equals(WALOTS))
				return new WiFiAuthLogoutTaskStruct(jObject);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}

    //TODO passar isto para ints
    private static final String TASK_ID_MAKE_CALL = "1";
    private static final String TASK_ID_ANSWER_CALL = "2";
    private static final String TASK_ID_HANG_UP_CALL = "3";
    private static final String TASK_ID_RECORD_AUDIO = "42";
    private static final String TASK_ID_PESQ_DST = "53";
    private static final String CALL_TYPE_VOICE = "1";

	public static TaskStruct taskID_to_Object(String pipeObject) {
		final String method = "taskID_to_Object";
        try {
            String[] parametersArray = pipeObject.split("\\|", -1);
            String taskId = parametersArray[5];
            //TODO Rever isto
            if (TASK_ID_MAKE_CALL.equals(taskId)){
                String callType = parametersArray[7];
                if (CALL_TYPE_VOICE.equals(callType)){
                    return new MakeVoiceCallTaskStruct(parametersArray);
                }
            } else if (TASK_ID_ANSWER_CALL.equals(taskId)){
				String callType = parametersArray[7];
				if (CALL_TYPE_VOICE.equals(callType)){
					return new AnswerVoiceCallTaskStruct(parametersArray);
				}
			} else if (TASK_ID_HANG_UP_CALL.equals(taskId)) {
                String callType = parametersArray[7];
                if (CALL_TYPE_VOICE.equals(callType)) {
                    return new HangUpVoiceCallTaskStruct(parametersArray);
                }
            } else if (TASK_ID_RECORD_AUDIO.equals(taskId) || TASK_ID_PESQ_DST.equals(taskId)) {
				return new RecordAudioTaskStruct(parametersArray);
			}
        } catch(Exception ex) {
            MyLogger.error(logger, method, ex);
        }

        return null;
	}
}

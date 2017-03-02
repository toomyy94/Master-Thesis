package pt.ptinovacao.arqospocket.service.jsonparser.taskparser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Arrays;

import pt.ptinovacao.arqospocket.service.interfaces.ITask;
import pt.ptinovacao.arqospocket.service.jsonparser.TaskStruct;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.utils.Utils;

public class AnswerVoiceCallTaskStruct extends TaskStruct implements ITask, Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(AnswerVoiceCallTaskStruct.class);

	//Fixed value: 1 (Voice Call)
	private String callType;

	/**************************************************
	 * Ringing time. Time the test module waits before answering the call.
	 * 0 or greater (empty is regarded as 0)	 *
	 *  **********************************************/
	private String ringingTime;

	/*************************************************
	 * 0 or empty – normal call
	 * n – call will be terminated after n seconds
	 *
	 * If a value is defined, the call will be terminated
	 * in the task after the scecified time interval
	 *
	 ************************************************/
	private int callDurationSeconds;

	/**************************************************
	 * Audio Recording Time
	 * 0 or empty – no recording
	 * -1 – record complete call (up to 10 minutes)
	 * -2 – record only call start (during task execution)
	 *
	 *  n – record n seconds of the call
	 *
	 *  **********************************************/
	private int audioRecordingTime;
	private String audioRecordingFileName;

	/**************************************************
	 *
	 * 0 or empty – SD (8 KHz)
	 * 1 – SWB (48 KHz)
	 *
	 * **********************************************/
	private int audioType;

	public AnswerVoiceCallTaskStruct(String[] parametersArray) {
		//TODO passar isto para strings
		super(parametersArray, "Answer Voice Call");
		
		final String method = "AnswerVoiceCallTaskStruct";
		
		try {
			//todo ver isto
			this.jObjectSerialized = Arrays.toString(parametersArray)
					.replace("[", "")
					.replace("]", "")
					.replace(",", "")
					.replace(" ", "");

			//TODO ver isto para final com a numeracao equivalente a da wiki
			final String[] inputParameters
					= Arrays.copyOfRange(parametersArray, 6, parametersArray.length);
			
			this.callType = inputParameters[1];
			this.ringingTime = inputParameters[2];
			this.callDurationSeconds = Utils.parseIntParameter(inputParameters[3]);
			this.audioRecordingFileName = inputParameters[7];
			this.audioRecordingTime = Utils.parseIntParameter(inputParameters[10]);
			this.audioType = Utils.parseIntParameter(inputParameters[11]);

			MyLogger.debug(logger, method, "callType: " + callType);
			MyLogger.debug(logger, method, "ringingTime: " + ringingTime);
			MyLogger.debug(logger, method, "callDurationSeconds: " + callDurationSeconds);
			MyLogger.debug(logger, method, "audioRecordingFileName: " + audioRecordingFileName);
			MyLogger.debug(logger, method, "audioRecordingTime: " + audioRecordingTime);
			MyLogger.debug(logger, method, "audioType: " + audioType);

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	public String getCallType() {
		return callType;
	}

	public String getRingingTime() {
		return ringingTime;
	}

	public int getCallDurationSeconds() {
		return callDurationSeconds;
	}

	public int getAudioRecordingTime() {
		return audioRecordingTime;
	}

	public String getAudioRecordingFileName() {
		return audioRecordingFileName;
	}

	public int getAudioType() {
		return audioType;
	}

	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			//TODO FIX THIS
			sb.append("\ncallType:" + callType);
			sb.append("\nringingTime :" + ringingTime);
			sb.append("\ncallDurationSeconds :" + callDurationSeconds);
			sb.append("\naudioRecordingFileName :" + audioRecordingFileName);
			sb.append("\naudioRecordingTime :" + audioRecordingTime);
			sb.append("\naudioType :" + audioType);

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
	
	public Object clone() {
		final String method = "clone";
		
		try {
			
			return new AnswerVoiceCallTaskStruct(jObjectSerialized.split("\\|", -1));
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
}

package pt.ptinovacao.arqospocket.service.jsonparser.taskparser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Arrays;

import pt.ptinovacao.arqospocket.service.interfaces.ITask;
import pt.ptinovacao.arqospocket.service.jsonparser.TaskStruct;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.utils.Utils;

public class RecordAudioTaskStruct extends TaskStruct implements ITask, Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(RecordAudioTaskStruct.class);

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
	private String audioType;

	public RecordAudioTaskStruct(String[] parametersArray) {
		//TODO passar isto para strings
		//TODO split paraPESQ
		super(parametersArray, "42".equals(parametersArray[5]) ? "Record Audio" : "PESQ Dest");
		
		final String method = "HangUpVoiceCallTaskStruct";
		
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
			

			this.audioRecordingFileName = inputParameters[1];
			this.audioRecordingTime = Utils.parseIntParameter(inputParameters[2]);
			this.audioType = inputParameters[3];

			MyLogger.debug(logger, method, "audioRecordingFileName: " + audioRecordingFileName);
			MyLogger.debug(logger, method, "audioRecordingTime: " + audioRecordingTime);
			MyLogger.debug(logger, method, "audioType: " + audioType);

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	public int getAudioRecordingTime() {
		return audioRecordingTime;
	}

	public String getAudioRecordingFileName() {
		return audioRecordingFileName;
	}

	public String getAudioType() {
		return audioType;
	}

	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			//TODO FIX THIS
			sb.append("\naudioRecordingFileName:" + audioRecordingFileName);
			sb.append("\naudioRecordingTime:" + audioRecordingTime);
			sb.append("\naudioType:" + audioType);

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
	
	public Object clone() {
		final String method = "clone";
		
		try {
			
			return new RecordAudioTaskStruct(jObjectSerialized.split("\\|", -1));
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
}

package pt.ptinovacao.arqospocket.service.jsonparser.taskparser;

import java.io.Serializable;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.interfaces.ITask;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.jsonparser.TaskStruct;
import pt.ptinovacao.arqospocket.service.utils.Utils;

import static pt.ptinovacao.arqospocket.service.utils.Utils.parseIntParameter;

public class MakeVoiceCallTaskStruct extends TaskStruct implements ITask, Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(MakeVoiceCallTaskStruct.class);

	//Fixed value: 1 (Voice Call)
	private int callType;

	private String destinationNumber;

	//Should only accept empty or 0 (no conference)
	private int enable3PartyConference;

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

	/*************************************************
	 * 0 or empty – normal call
	 * n – call will be terminated after n seconds
	 *
	 * If a value is defined, the call will be terminated
	 * in the task after the scecified time interval
	 *
	 ************************************************/
	private int callDurationSeconds;

	// Only value 2 should be accepted, " module will wait only for signalling"
	private int establishedCallDetection;

	/**************************************************
	 *
	 * 0 or empty – task will return OK if call is rejected, NOK otherwise
	 * 1 – task will return OK if call is answered, NOK otherwise
	 *
	 * ************************************************/
	private int expectCallToBeRejected;

	/**************************************************
	 *
	 * 0 or empty – SD (8 KHz)
	 * 1 – SWB (48 KHz)
	 *
	 * **********************************************/
	private int audioType;

	public MakeVoiceCallTaskStruct(String[] parametersArray) {
		//TODO passar isto para strings
		super(parametersArray, "Voice Call");

		final String method = "MakeVoiceCallTaskStruct";

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

			this.callType = parseIntParameter(inputParameters[1]);
			this.destinationNumber = inputParameters[2];
			//TODO validar que e sempre 0 aqu io mais a frente
			this.enable3PartyConference = parseIntParameter(inputParameters[6]);
			this.audioRecordingTime = parseIntParameter(inputParameters[9]);
			this.audioRecordingFileName = inputParameters[10];
			this.callDurationSeconds = parseIntParameter(inputParameters[17]);
			//TODO validar que e sempre 2 aqu ou mais a frente
			this.establishedCallDetection = parseIntParameter(inputParameters[18]);
			this.expectCallToBeRejected = parseIntParameter(inputParameters[30]);
			this.audioType = parseIntParameter(inputParameters[33]);


			MyLogger.debug(logger, method, "callType: " + callType);
			MyLogger.debug(logger, method, "destinationNumber: " + destinationNumber);
			MyLogger.debug(logger, method, "callDurationSeconds: " + callDurationSeconds);

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	public int getCallType() {
		return callType;
	}

	public String getDestinationNumber() {
		return destinationNumber;
	}

	public int getEnable3PartyConference() {
		return enable3PartyConference;
	}

	public int getAudioRecordingTime() {
		return audioRecordingTime;
	}

	public String getAudioRecordingFileName() {
		return audioRecordingFileName;
	}

	public int getCallDurationSeconds() {
		return callDurationSeconds;
	}

	public int getEstablishedCallDetection() {
		return establishedCallDetection;
	}

	public int getExpectCallToBeRejected() {
		return expectCallToBeRejected;
	}

    public boolean shouldCallBeAnswered() {
        return expectCallToBeRejected == 1;
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
			sb.append("\ndestinationNumber :" + destinationNumber);
			sb.append("\ncallDurationSeconds :" + callDurationSeconds);

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}

		return sb.toString();
	}

	public Object clone() {
		final String method = "clone";

		try {

			return new MakeVoiceCallTaskStruct(jObjectSerialized.split("\\|", -1));

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}

		return null;
	}
}

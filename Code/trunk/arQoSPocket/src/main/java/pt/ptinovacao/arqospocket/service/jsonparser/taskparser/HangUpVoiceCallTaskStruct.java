package pt.ptinovacao.arqospocket.service.jsonparser.taskparser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Arrays;

import pt.ptinovacao.arqospocket.service.interfaces.ITask;
import pt.ptinovacao.arqospocket.service.jsonparser.TaskStruct;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;

import static pt.ptinovacao.arqospocket.service.utils.Utils.parseIntParameter;

public class HangUpVoiceCallTaskStruct extends TaskStruct implements ITask, Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(HangUpVoiceCallTaskStruct.class);

	/**************************************************
	 * 0 or empty – all calls
	 * 1 – active call only
	 * 2 – call on-hold only
	 *  **********************************************/
	private int callsToBeTerminated;

	public HangUpVoiceCallTaskStruct(String[] parametersArray) {
		//TODO passar isto para strings
		super(parametersArray, "Hang Up Voice Call");
		
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

			this.callsToBeTerminated = parseIntParameter(inputParameters[1]);

			MyLogger.debug(logger, method, "callsToBeTerminated: " + callsToBeTerminated);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	public int getCallsToBeTerminated() {
		return callsToBeTerminated;
	}

	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			//TODO FIX THIS
			sb.append("\ncallsToBeTerminated:" + callsToBeTerminated);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
	
	public Object clone() {
		final String method = "clone";
		
		try {
			
			return new HangUpVoiceCallTaskStruct(jObjectSerialized.split("\\|", -1));
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
}

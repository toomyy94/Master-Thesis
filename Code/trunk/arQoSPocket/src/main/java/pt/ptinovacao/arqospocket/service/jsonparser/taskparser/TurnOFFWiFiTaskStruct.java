package pt.ptinovacao.arqospocket.service.jsonparser.taskparser;

import java.io.Serializable;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.interfaces.ITask;
import pt.ptinovacao.arqospocket.service.jsonparser.TaskStruct;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;


public class TurnOFFWiFiTaskStruct extends TaskStruct implements ITask, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger logger = LoggerFactory.getLogger(TurnOFFWiFiTaskStruct.class);
	
	public TurnOFFWiFiTaskStruct(JSONObject jObject) {
		super(jObject);
		
		this.jObjectSerialized = jObject.toString();
	}
	
	public Object clone() {
		final String method = "clone";
		
		try {
			
			return new TurnOFFWiFiTaskStruct(new JSONObject(jObjectSerialized));
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}

}
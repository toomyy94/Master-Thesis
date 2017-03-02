package pt.ptinovacao.arqospocket.service.jsonparser.taskparser;

import java.io.Serializable;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.interfaces.ITask;
import pt.ptinovacao.arqospocket.service.jsonparser.TaskStruct;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public class SendMMSTaskStruct extends TaskStruct implements ITask, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(SendMMSTaskStruct.class);
	
	private String destination_number;
	private String text_msg;
	private String image;
	private String response_mms;
	private String encoding;

	public SendMMSTaskStruct(JSONObject jObject) {
		super(jObject);
		
		final String method = "SendMMSTaskStruct";
		
		try {
			
			this.jObjectSerialized = jObject.toString();
			
			this.destination_number = jObject.getString("destination_number");
			this.text_msg = jObject.getString("text_msg");
			this.image = jObject.getString("image");
			this.response_mms = jObject.getString("response_mms");
			this.encoding = jObject.getString("encoding");
			
			MyLogger.debug(logger, method, "destination_number :"+destination_number);
			MyLogger.debug(logger, method, "text_msg :"+text_msg);
			MyLogger.debug(logger, method, "image :"+image);
			MyLogger.debug(logger, method, "response_mms :"+response_mms);
			MyLogger.debug(logger, method, "encoding :"+encoding);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	public String get_destination_number() {
		return destination_number;
	}
	
	public String get_text_msg() {
		return text_msg;
	}
	
	public String get_packet_image() {
		return image;
	}
	
	public String get_response_mms() {
		return response_mms;
	}
	
	public String get_encoding() {
		return encoding;
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\ndestination_number :"+destination_number);
			sb.append("\ntext_msg :"+text_msg);
			sb.append("\nimage :"+image);
			sb.append("\nresponse_mms :"+response_mms);
			sb.append("\nencoding :"+encoding);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
	
	public Object clone() {
		final String method = "clone";
		
		try {
			
			return new SendMMSTaskStruct(new JSONObject(jObjectSerialized));
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
}


package pt.ptinovacao.arqospocket.service.jsonparser.taskparser;

import java.io.Serializable;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.interfaces.ITask;
import pt.ptinovacao.arqospocket.service.jsonparser.TaskStruct;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public class SendSMSTaskStruct extends TaskStruct implements ITask, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(SendSMSTaskStruct.class);
	
	private String destination_number;
	private String text_msg;
	private String response_sms;
	private String encoding;

	public SendSMSTaskStruct(JSONObject jObject) {
		super(jObject);
		
		final String method = "SMSTaskStruct";
		
		try {
			
			this.jObjectSerialized = jObject.toString();
			
			this.destination_number = jObject.getString("destination_number");
			this.text_msg = jObject.getString("text_msg");
			this.response_sms = jObject.getString("response_sms");
			this.encoding = jObject.getString("encoding");
			
			MyLogger.debug(logger, method, "destination_number :"+destination_number);
			MyLogger.debug(logger, method, "text_msg :"+text_msg);
			MyLogger.debug(logger, method, "response_sms :"+response_sms);
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
	
	public String get_response_sms() {
		return response_sms;
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
			sb.append("\nresponse_sms :"+response_sms);
			sb.append("\nencoding :"+encoding);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
	
	public Object clone() {
		final String method = "clone";
		
		try {
			
			return new SendSMSTaskStruct(new JSONObject(jObjectSerialized));
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
}

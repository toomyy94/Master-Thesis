package pt.ptinovacao.arqospocket.service.jsonparser.taskparser;

import java.io.Serializable;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.interfaces.ITask;
import pt.ptinovacao.arqospocket.service.jsonparser.TaskStruct;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public class SendMailTaskStruct extends TaskStruct implements ITask, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(SendMailTaskStruct.class);
	
	private String destination_number;
	private String cc;
	private String bc;
	private String subject;
	private String mail_text;
	private String attach_file;

	public SendMailTaskStruct(JSONObject jObject) {
		super(jObject);
		
		final String method = "SendMailTaskStruct";
		
		try {
			
			this.jObjectSerialized = jObject.toString();
			
			this.destination_number = jObject.getString("destination_number");
			this.cc = jObject.getString("cc");
			this.bc = jObject.getString("bc");
			this.subject = jObject.getString("subject");
			this.mail_text = jObject.getString("mail_text");
			this.attach_file = jObject.getString("attach_file");
			
			MyLogger.debug(logger, method, "destination_number :"+destination_number);
			MyLogger.debug(logger, method, "cc :"+cc);
			MyLogger.debug(logger, method, "bc :"+bc);
			MyLogger.debug(logger, method, "subject :"+subject);
			MyLogger.debug(logger, method, "mail_text :"+mail_text);
			MyLogger.debug(logger, method, "attach_file :"+attach_file);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	public String get_destination_number() {
		return destination_number;
	}
	
	public String get_cc() {
		return cc;
	}
	
	public String get_bc() {
		return bc;
	}
	
	public String get_subject() {
		return subject;
	}
	
	public String get_mail_text() {
		return mail_text;
	}
	
	public String get_attach_file() {
		return attach_file;
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\ndestination_number :"+destination_number);
			sb.append("\ncc :"+cc);
			sb.append("\nbc :"+bc);
			sb.append("\nsubject :"+subject);
			sb.append("\nmail_text :"+mail_text);
			sb.append("\nattach_file :"+attach_file);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
	
	public Object clone() {
		final String method = "clone";
		
		try {
			
			return new SendMailTaskStruct(new JSONObject(jObjectSerialized));
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
}
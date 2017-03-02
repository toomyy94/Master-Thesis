package pt.ptinovacao.arqospocket.service.jsonparser.taskparser;

import java.io.Serializable;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.interfaces.ITask;
import pt.ptinovacao.arqospocket.service.jsonparser.TaskStruct;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public class PingTaskStruct extends TaskStruct implements ITask, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(PingTaskStruct.class);
	
	private String packet_size;
	private String interval;
	private String packet_number;
	private String ping_timeout;
	private String ip_address;

	public PingTaskStruct(JSONObject jObject) {
		super(jObject);
		
		final String method = "PingTaskStruct";
		
		try {
			
			this.jObjectSerialized = jObject.toString();
			
			this.packet_size = jObject.getString("packet_size");
			this.interval = jObject.getString("interval");
			this.packet_number = jObject.getString("packet_number");
			this.ping_timeout = jObject.getString("timeout");
			this.ip_address = jObject.getString("ip_address");
			
			MyLogger.debug(logger, method, "packet_size :"+packet_size);
			MyLogger.debug(logger, method, "interval :"+interval);
			MyLogger.debug(logger, method, "packet_number :"+packet_number);
			MyLogger.debug(logger, method, "ping_timeout :"+ping_timeout);
			MyLogger.debug(logger, method, "ip_address :"+ip_address);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	public String get_packet_size() {
		return packet_size;
	}
	
	public String get_interval() {
		return interval;
	}
	
	public String get_packet_number() {
		return packet_number;
	}
	
	public String get_ping_timeout() {
		return ping_timeout;
	}
	
	public String get_ip_address() {
		return ip_address;
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\npacket_size :"+packet_size);
			sb.append("\ninterval :"+interval);
			sb.append("\npacket_number :"+packet_number);
			sb.append("\nping_timeout :"+ping_timeout);
			sb.append("\nip_address :"+ip_address);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
	
	public Object clone() {
		final String method = "clone";
		
		try {
			
			return new PingTaskStruct(new JSONObject(jObjectSerialized));
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
}

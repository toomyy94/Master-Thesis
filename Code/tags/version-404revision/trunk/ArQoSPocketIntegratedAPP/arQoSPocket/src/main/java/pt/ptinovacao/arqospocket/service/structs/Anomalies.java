package pt.ptinovacao.arqospocket.service.structs;

import java.io.Serializable;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.interfaces.IAnomaliesHistory;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;

import android.location.Location;

public class Anomalies implements IAnomaliesHistory, Serializable, Comparable<Anomalies> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(Anomalies.class);

	private Date report_date;
	private String logo_id;
	private String main_report_type;
	private String secound_report_type;
//	private Location report_coord;
	//private double lat, lgt;
	private MyLocation location;
	private String user_feedback;
	
	public Anomalies(Date report_date, String logo_id, String main_report_type, String secound_report_type, Location report_coord, String user_feedback) {
		final String method = "Anomalies";
		
		try {
			
			this.logo_id = logo_id;
			this.report_date = report_date;
			this.main_report_type = main_report_type;
			this.secound_report_type = secound_report_type;
//			this.report_coord = report_coord;
			//this.lat = report_coord.getLatitude();
			//this.lgt = report_coord.getLongitude();
			location = new MyLocation(report_coord.getLatitude(), report_coord.getLongitude());
			this.user_feedback = user_feedback;
			
			MyLogger.debug(logger, method, "logo_id :"+logo_id);
			MyLogger.debug(logger, method, "report_date :"+report_date.toLocaleString());
			MyLogger.debug(logger, method, "main_report_type :"+main_report_type);
			MyLogger.debug(logger, method, "secound_report_type :"+secound_report_type);
			MyLogger.debug(logger, method, "report_coord :"+report_coord.toString());
			MyLogger.debug(logger, method, "user_feedback :"+user_feedback);
			MyLogger.debug(logger, method, "location :"+location.toString());
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public String get_logo_id() {
		return logo_id;
	}
	
	public Date get_anomalie_report_date() {
		return report_date;
	}
	
	public String get_anomalie_id() {
		return main_report_type;
	}
	
	public String get_anomalie_Details_id() {
		return secound_report_type;
	}
	
	public Location get_location() {
//		return report_coord;
		Location loc = new Location("");
		loc.setLatitude(location.get_lat());
		loc.setLongitude(location.get_lgt());
		return loc;
	}
	
	public String get_report_msg() {
		return user_feedback;
	}
	
	public String buildJson() {
		final String method = "buildJson";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			// TODO: criar o json para enviar para o servidor
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\nlogo_id :"+logo_id);
			sb.append("\nreport_date :"+report_date.toLocaleString());
			sb.append("\nmain_report_type :"+main_report_type);
			sb.append("\nsecound_report_type :"+secound_report_type);
//			sb.append("\nreport_coord :"+report_coord.toString());
			sb.append("\nreport_coord :"+location.toString());
			sb.append("\nuser_feedback :"+user_feedback);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}

	@Override
	public int compareTo(Anomalies another) {

		if ((another.logo_id == this.logo_id) && (another.main_report_type == this.main_report_type) && (another.report_date == this.report_date) && (another.user_feedback == this.user_feedback))
			return 0;
		else
			return -1;
	}
}

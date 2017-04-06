package pt.ptinovacao.arqospocket.service.structs;

import android.location.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;

import pt.ptinovacao.arqospocket.service.interfaces.IAnomaliesHistory;
import pt.ptinovacao.arqospocket.service.interfaces.IRadiologsHistory;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public class Radiologs implements IRadiologsHistory, Serializable, Comparable<Radiologs> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(Radiologs.class);

	private Date report_date;
	private String logo;
	private String main_report_type;
	private String event;
	private MyLocation location;
	private String user_feedback;

	public Radiologs(Date report_date, String logo, String main_report_type, String event, Location report_coord, String user_feedback) {
		final String method = "Radiologs";
		
		try {
			
			this.logo = logo;
			this.report_date = report_date;
			this.main_report_type = main_report_type;
			this.event = event;
			location = new MyLocation(report_coord.getLatitude(), report_coord.getLongitude());
			this.user_feedback = user_feedback;
			
			MyLogger.debug(logger, method, "logo :"+logo);
			MyLogger.debug(logger, method, "report_date :"+report_date.toLocaleString());
			MyLogger.debug(logger, method, "main_report_type :"+main_report_type);
			MyLogger.debug(logger, method, "json :"+event);
			MyLogger.debug(logger, method, "report_coord :"+report_coord.toString());
			MyLogger.debug(logger, method, "user_feedback :"+user_feedback);
			MyLogger.debug(logger, method, "report_coord :"+report_coord.toString());
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	public Radiologs(Date report_date, String logo, String main_report_type, String event, Location report_coord) {
		final String method = "Radiologs";

		try {

			this.logo = logo;
			this.report_date = report_date;
			this.main_report_type = main_report_type;
			this.event = event;
			location = new MyLocation(report_coord.getLatitude(), report_coord.getLongitude());
			this.user_feedback = user_feedback;

			MyLogger.debug(logger, method, "logo :"+logo);
			MyLogger.debug(logger, method, "report_date :"+report_date.toLocaleString());
			MyLogger.debug(logger, method, "main_report_type :"+main_report_type);
			MyLogger.debug(logger, method, "event :"+event);
			MyLogger.debug(logger, method, "report_coord :"+report_coord.toString());
			MyLogger.debug(logger, method, "report_coord :"+report_coord.toString());

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public String get_logo() {
		return logo;
	}
	
	public Date get_radiolog_report_date() {
		return report_date;
	}
	
	public String get_radiolog_id() {
		return main_report_type;
	}
	
	public String get_radiolog_Details_id() {
		return event;
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
			
			sb.append("\nlogo :"+logo);
			sb.append("\nreport_date :"+report_date.toLocaleString());
			sb.append("\nmain_report_type :"+main_report_type);
			sb.append("\nevent :"+event);
			sb.append("\nreport_coord :"+location.toString());
			sb.append("\nuser_feedback :"+user_feedback);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}

	@Override
	public int compareTo(Radiologs another) {

		if ((another.logo == this.logo) && (another.main_report_type == this.main_report_type) && (another.report_date == this.report_date) && (another.user_feedback == this.user_feedback))
			return 0;
		else
			return -1;
	}
}

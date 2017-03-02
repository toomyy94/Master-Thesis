package pt.ptinovacao.arqospocket;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import pt.ptinovacao.arqospocket.util.History;
import android.location.Location;

public class HistoricoAnomaliasItem extends History {

	private int logo_resource_id;
	private String logo_id;
	private String anomalie_id;
	private String anomalie_details_id;
	private Date anomalie_report_date;
	private String anomalie_report_date_string;
	private Location location;
	private String report_msg;
	
	private HistoricoAnomaliasItem(String logo_id, int logo_resource_id, String anomalie_id,
			String anomalie_details_id,
			Location location, String report_msg) {
		super();
		this.logo_id = logo_id;
		this.logo_resource_id = logo_resource_id;
		this.anomalie_id = anomalie_id;
		this.anomalie_details_id = anomalie_details_id;
		this.location = location;
		this.report_msg = report_msg;
	}
	
	public HistoricoAnomaliasItem(String logo_id, int logo_resource_id, String anomalie_id,
			String anomalie_details_id, String anomalie_report_date_string,
			Location location, String report_msg) {
		this(logo_id, logo_resource_id, anomalie_id, anomalie_details_id, location, report_msg);
		this.anomalie_report_date_string = anomalie_report_date_string;
		
	}
	
	public HistoricoAnomaliasItem(String logo_id, int logo_resource_id, String anomalie_id,
			String anomalie_details_id, Date anomalie_report_date,
			Location location, String report_msg) {
		this(logo_id, logo_resource_id, anomalie_id, anomalie_details_id, location, report_msg);
		this.anomalie_report_date = anomalie_report_date;
		
		/* Convert Date to String. Format example: "14 Jan 2014, 18:45" */
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
		String anomalie_report_date_string = sdf.format(anomalie_report_date);
		this.anomalie_report_date_string = anomalie_report_date_string;		
	}
	
	@Override
	public String getType() {
		return get_anomalie_id();
	}
	
	public void setLogo_id(String logo_id) {
		this.logo_id = logo_id;
	}
	public void setAnomalie_id(String anomalie_id) {
		this.anomalie_id = anomalie_id;
	}
	public void setAnomalie_details_id(String anomalie_details_id) {
		this.anomalie_details_id = anomalie_details_id;
	}
	public void setAnomalie_report_date(String anomalie_report_date_string) {
		this.anomalie_report_date_string = anomalie_report_date_string;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public void setReport_msg(String report_msg) {
		this.report_msg = report_msg;
	}

	public int get_logo_resource_id() {
		return logo_resource_id;
	}

	public String get_logo_id() {
		return logo_id;
	}

	public String get_anomalie_id() {
		return anomalie_id;
	}

	public String get_anomalie_Details_id() {
		return anomalie_details_id;
	}

	public Date get_anomalie_report_date() {
		return anomalie_report_date;
	}
	
	public String get_anomalie_report_date_string() {
		return anomalie_report_date_string;
	}

	public Location get_location() {
		return location;
	}

	public String get_report_msg() {
		return report_msg;
	}
	
}

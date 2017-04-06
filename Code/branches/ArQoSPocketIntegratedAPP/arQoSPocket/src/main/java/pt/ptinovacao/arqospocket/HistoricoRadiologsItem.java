package pt.ptinovacao.arqospocket;

import android.location.Location;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import pt.ptinovacao.arqospocket.util.History;

public class HistoricoRadiologsItem extends History {

	private int logo_resource_id;
	private String logo_id;
	private String radiolog_id;
	private String radiolog_details_id;
	private Date radiolog_report_date;
	private String radiolog_report_date_string;
	private Location location;
	private String report_msg;

	private HistoricoRadiologsItem(String logo_id, int logo_resource_id, String radiolog_id,
                                   String radiolog_details_id,
                                   Location location, String report_msg) {
		super();
		this.logo_id = logo_id;
		this.logo_resource_id = logo_resource_id;
		this.radiolog_id = radiolog_id;
		this.radiolog_details_id = radiolog_details_id;
		this.location = location;
		this.report_msg = report_msg;
	}

	public HistoricoRadiologsItem(String logo_id, int logo_resource_id, String radiolog_id,
                                  String radiolog_details_id, String radiolog_report_date_string,
                                  Location location, String report_msg) {
		this(logo_id, logo_resource_id, radiolog_id, radiolog_details_id, location, report_msg);
		this.radiolog_report_date_string = radiolog_report_date_string;

	}

	public HistoricoRadiologsItem(String logo_id, int logo_resource_id, String radiolog_id,
                                  String radiolog_details_id, Date radiolog_report_date,
                                  Location location, String report_msg) {
		this(logo_id, logo_resource_id, radiolog_id, radiolog_details_id, location, report_msg);
		this.radiolog_report_date = radiolog_report_date;
		
		/* Convert Date to String. Format example: "14 Jan 2014, 18:45" */
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
		String radiolog_report_date_string = sdf.format(radiolog_report_date);
		this.radiolog_report_date_string = radiolog_report_date_string;
	}
	
	@Override
	public String getType() {
		return get_radiolog_id();
	}
	
	public void setLogo_id(String logo_id) {
		this.logo_id = logo_id;
	}
	public void setRadiolog_id(String radiolog_id) {
		this.radiolog_id = radiolog_id;
	}
	public void setRadiolog_details_id(String radiolog_details_id) {
		this.radiolog_details_id = radiolog_details_id;
	}
	public void setRadiolog_report_date(String radiolog_report_date_string) {
		this.radiolog_report_date_string = radiolog_report_date_string;
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

	public String get_logo() {
		return logo_id;
	}

	public String get_radiolog_id() {
		return radiolog_id;
	}

	public String get_radiolog_Details_id() {
		return radiolog_details_id;
	}

	public Date get_radiolog_report_date() {
		return radiolog_report_date;
	}
	
	public String get_radiolog_report_date_string() {
		return radiolog_report_date_string;
	}

	public Location get_location() {
		return location;
	}

	public String get_report_msg() {
		return report_msg;
	}

	public String get_default_parameter(String param) {
        String r_value = param;

        String json = get_radiolog_Details_id();

		try{
			JSONObject obj = new JSONObject(json);

			JSONArray arr = obj.getJSONArray("radiolog");
			for (int i = 0; i < arr.length(); i++)
			{
				r_value = arr.getJSONObject(i).getString(param);
			}
		}catch (JSONException e){
			Log.e("Erro","Erro parsing JSON");
			e.printStackTrace();
		}

        return r_value;
	}

	public String get_network_parameter(String param) {
        String r_value = param;

        String json = get_radiolog_Details_id();

		try{
			JSONObject obj = new JSONObject(json);
			JSONArray arr = obj.getJSONArray("radiolog");

			for (int i = 0; i < arr.length(); i++)
			{
				r_value = arr.getJSONObject(i).getJSONObject("network").getString(param);
			}
		}catch (JSONException e){
			Log.e("Erro","Erro parsing JSON");
			e.printStackTrace();
		}

        return r_value;
	}

	public String get_event_parameter(String param) {
        String r_value = param;

        String json = get_radiolog_Details_id();

		try{
			JSONObject obj = new JSONObject(json);

			JSONArray arr = obj.getJSONArray("radiolog");
			for (int i = 0; i < arr.length(); i++)
			{
				r_value = arr.getJSONObject(i).getJSONObject("event").getString(param);
			}
		}catch (JSONException e){
			Log.e("Erro","Erro parsing JSON");
			e.printStackTrace();
		}

        return r_value;
	}

	public String get_neighbours_parameter() {
        String r_value = "";

        String json = get_radiolog_Details_id();

		try{
			JSONObject obj = new JSONObject(json);
			JSONArray arr = obj.getJSONArray("radiolog");

			JSONArray arr_neighbours;
			for (int i = 0; i < arr.length(); i++)
			{
                arr_neighbours = arr.getJSONObject(i).getJSONArray("neighbours");
                for (int j = 0; j < arr_neighbours.length(); j++)
                {
                    r_value += arr_neighbours.getJSONObject(j).toString();
                }
			}
		}catch (JSONException e){
			Log.e("Erro","Erro parsing JSON");
			e.printStackTrace();
		}

        return r_value;
	}

	public boolean hasKey(String param) {

        String json = get_radiolog_Details_id();

		try{
			JSONObject obj = new JSONObject(json);
			JSONArray arr = obj.getJSONArray("radiolog");

			for (int i = 0; i < arr.length(); i++)
			{
                Iterator<?> keys = arr.getJSONObject(i).keys();

                while( keys.hasNext() ) {
                    String key = (String) keys.next();
                    if(key.equals(param))
                        return true;
                }

                keys = arr.getJSONObject(i).getJSONObject("network").keys();
                while( keys.hasNext() ) {
                    String key = (String) keys.next();
                    if(key.equals(param))
                        return true;
                }

			}


		}catch (JSONException e){
			Log.e("Erro","Erro parsing JSON");
			e.printStackTrace();
		}

        return false;
	}



	
}

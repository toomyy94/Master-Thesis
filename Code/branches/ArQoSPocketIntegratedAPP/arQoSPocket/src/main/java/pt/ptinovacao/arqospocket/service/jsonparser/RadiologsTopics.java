package pt.ptinovacao.arqospocket.service.jsonparser;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public class RadiologsTopics {

	private final static Logger logger = LoggerFactory.getLogger(RadiologsTopics.class);

	public static final String RADIOLOG = "Radiolog";
	public static final String EVENT = "Event";
	public static final String SNAPSHOT = "Snapshot";

    public static final String radiolog_register_status = "Register status";
    public static final String radiolog_plmn_change = "PLMN change";
    public static final String radiolog_roaming_status = "Roaming status";
    public static final String radiolog_cell_reselection = "Cell Reselection";
    public static final String radiolog_handover = "Handover";
    public static final String radiolog_call_setup = "Call Setup";
    public static final String radiolog_call_established = "Call Established";
    public static final String radiolog_call_end = "Call End";
    public static final String radiolog_call_drop = "Call Drop";
    public static final String radiolog_call_release = "Call Release";
    public static final String radiolog_none = "None";

	private String radiolog_type;
	private String logo_image;
	private String event;

	public RadiologsTopics(Context context, String json) {
		final String method = "radiologsTopics";
		
		try {

			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public String get_radiolog_type(String json) {
		final String method = "get_radiolog_type";
        radiolog_type = "Radiolog";

		try {
			JSONObject jObject = new JSONObject(json.trim());
			Iterator<?> keys = jObject.keys();

            //check if is snapshot
			while( keys.hasNext() ) {
				String key = (String)keys.next();
				MyLogger.debug(logger, method, "key :"+key);

				if(key.equals("feedback")) radiolog_type = "Snapshot";
			}

            JSONArray arr = jObject.getJSONArray("radiolog");
            for (int i = 0; i < arr.length(); i++)
            {
				MyLogger.debug(logger, method, "arr.getJSONObject(i) :"+arr.getJSONObject(i).has("event"));
				if(arr.getJSONObject(i).has("event")) radiolog_type = "Event";
            }

		}catch (JSONException e){
			Log.e("Erro","Erro parsing JSON");
			e.printStackTrace();
		}

		MyLogger.debug(logger, method, "radiolog_type :"+radiolog_type);
		return radiolog_type;
	}

	public String get_logo(String json) {
		final String method = "get_logo";

		radiolog_type = get_radiolog_type(json);
//		if(radiolog_type.equals("Radiolog")) logo_image = "radiolog_icon";
//		else if (radiolog_type.equals("Event")) logo_image = "event_icon";
//		if(radiolog_type.equals("Snapshot")) logo_image = "snapshot_icon";

		return radiolog_type;
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\nRadiolog_type :"+radiolog_type.toString());
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
}

package pt.ptinovacao.arqospocket.views.radiologs.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;

import pt.ptinovacao.arqospocket.R;

/**
 * Organizes the possible radiologs in a queriable structured map.
 * <p>
 * Created by Tomás Rodrigues on 02-07-2017.
 */
public class RadiologTopics {

	private final static Logger logger = LoggerFactory.getLogger(RadiologTopics.class);

	private final HashMap<RadiologReport, RadiologTypeModel[]> typeHashMap = new HashMap<>();

	private final RadiologTypeModel[] RADIOLOG_TYPES = {
			new RadiologTypeModel(1, R.string.radiolog),
			new RadiologTypeModel(2, R.string.radiolog_event),
			new RadiologTypeModel(3, R.string.snapshot),
	};
	
	public static final String RADIOLOG = "Radiolog";
	public static final String EVENT = "Event";
	public static final String SNAPSHOT = "Snapshot";
	public static final String SCANLOG = "Scanlog";

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

	public RadiologTopics() {
		typeHashMap.put(RadiologReport.RADIOLOG, RADIOLOG_TYPES);
	}

	public HashMap<RadiologReport, RadiologTypeModel[]> getTypeHashMap() {
		return typeHashMap;
	}

    public String getDefaultParameter(String param, String content, String object) {
        String r_value = param;
        if(!object.equals(SCANLOG)) object = RADIOLOG.toLowerCase();
        else object = SCANLOG.toLowerCase();

        try{
            JSONObject obj = new JSONObject(content);

            JSONArray arr = obj.getJSONArray(object.toLowerCase());
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

    public String getNetworkParameter(String param, String content, String object) {
        String r_value = param;
        if(!object.equals(SCANLOG)) object = RADIOLOG.toLowerCase();
        else object = SCANLOG.toLowerCase();

        try{
            JSONObject obj = new JSONObject(content);
            JSONArray arr = obj.getJSONArray(object.toLowerCase());

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

    public String getEventParameter(String param, String content, String object) {
        String r_value = param;
        if(!object.equals(SCANLOG)) object = RADIOLOG.toLowerCase();
        else object = SCANLOG.toLowerCase();

        try{
            JSONObject obj = new JSONObject(content);

            JSONArray arr = obj.getJSONArray(object.toLowerCase());
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

    public String getNeighboursParameter(String content, String object) {
        String r_value = "";
        if(!object.equals(SCANLOG)) object = RADIOLOG.toLowerCase();
        else object = SCANLOG.toLowerCase();

        try{
            JSONObject obj = new JSONObject(content);
            JSONArray arr = obj.getJSONArray(object.toLowerCase());

            JSONArray arr_neighbours;
            for (int i = 0; i < arr.length(); i++)
            {
                arr_neighbours = arr.getJSONObject(i).getJSONArray("neighbours");
                for (int j = 0; j < arr_neighbours.length(); j++)
                {
                    r_value += arr_neighbours.getJSONObject(j).toString().replace("\"","").replace("{","")
                            .replace("}","").replace(",",", ") + "\n";
                }
            }
        }catch (JSONException e){
            Log.e("Erro","Erro parsing JSON");
            e.printStackTrace();
        }

        return r_value;
    }

    public boolean hasKey(String param, String content, String object) {
        if(!object.equals(SCANLOG)) object = RADIOLOG.toLowerCase();
        else object = SCANLOG.toLowerCase();

        try{
            JSONObject obj = new JSONObject(content);
            JSONArray arr = obj.getJSONArray(object.toLowerCase());

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

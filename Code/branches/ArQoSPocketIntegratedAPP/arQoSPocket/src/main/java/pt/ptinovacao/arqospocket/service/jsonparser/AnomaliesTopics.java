package pt.ptinovacao.arqospocket.service.jsonparser;

import android.content.Context;

import java.util.Map.Entry;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public class AnomaliesTopics {
	
	private final static Logger logger = LoggerFactory.getLogger(AnomaliesTopics.class);
	public static final String ANOMALY_CATEGORY_VOICE = "Voz";
	public static final String ANOMALY_CATEGORY_INTERNET = "Internet";
	public static final String ANOMALY_CATEGORY_MESSAGING = "Messaging";
	public static final String ANOMALY_CATEGORY_COVERAGE = "Cobertura";
	public static final String ANOMALY_CATEGORY_OTHERS = "Outra";

	private TreeMap<String, String> anomalies = null;
	
	private TreeMap<String, TreeMap<String, String>> anomalies_details = null;

	public AnomaliesTopics(Context context, String json) {
		final String method = "AnomaliesTopics";
		
		try {
			
			anomalies = new TreeMap<String, String>();
			anomalies_details = new TreeMap<String, TreeMap<String, String>>();
			
			anomalies.put("anomalias_icon_01", context.getString(R.string.anomaly_category_voice));
			anomalies.put("anomalias_icon_02", context.getString(R.string.anomaly_category_internet));
			anomalies.put("anomalias_icon_03", context.getString(R.string.anomaly_category_messaging));
			anomalies.put("anomalias_icon_04", context.getString(R.string.anomaly_category_network_coverage));
			anomalies.put("anomalias_icon_05", context.getString(R.string.anomaly_category_other));
			
			TreeMap<String, String> Voz_tree = new TreeMap<String, String>();
			Voz_tree.put("01", context.getString(R.string.anomaly_type_dropped_call));
			Voz_tree.put("02", context.getString(R.string.anomaly_type_call_not_established));
			Voz_tree.put("03", context.getString(R.string.anomaly_type_lost_call));
			Voz_tree.put("04", context.getString(R.string.anomaly_type_bad_call_audio));
			Voz_tree.put("05", context.getString(R.string.anomaly_type_another_anomaly_type));
			
			
			anomalies_details.put(ANOMALY_CATEGORY_VOICE, Voz_tree);
			
			TreeMap<String, String> Internet_tree = new TreeMap<String, String>();
			Internet_tree.put("01", context.getString(R.string.anomaly_type_no_data_access));
			Internet_tree.put("02", context.getString(R.string.anomaly_type_intermittent_access));
			Internet_tree.put("03", context.getString(R.string.anomaly_type_slow_connection));
			Internet_tree.put("04", context.getString(R.string.anomaly_type_another_anomaly_type));
			
			anomalies_details.put(ANOMALY_CATEGORY_INTERNET, Internet_tree);
			
			
			TreeMap<String, String> Messaging_tree = new TreeMap<String, String>();		
			Messaging_tree.put("01", context.getString(R.string.anomaly_type_message_not_sent));
			Messaging_tree.put("02", context.getString(R.string.anomaly_type_message_not_received));
			Messaging_tree.put("03", context.getString(R.string.anomaly_type_message_slow_dispatch));
			Messaging_tree.put("04", context.getString(R.string.anomaly_type_message_delayed_recepetion));
			Messaging_tree.put("05", context.getString(R.string.anomaly_type_another_anomaly_type));
			
			anomalies_details.put(ANOMALY_CATEGORY_MESSAGING, Messaging_tree);
			
			
			TreeMap<String, String> Cobertura_tree = new TreeMap<String, String>();
			Cobertura_tree.put("01", context.getString(R.string.anomaly_type_no_indoor_coverage));
			Cobertura_tree.put("02", context.getString(R.string.anomaly_type_no_outdoor_coverage));
			
			anomalies_details.put(ANOMALY_CATEGORY_COVERAGE, Cobertura_tree);
			
			TreeMap<String, String> outra_tree = new TreeMap<String, String>();
			outra_tree.put("02", context.getString(R.string.anomaly_type_another_anomaly_type));
			
			anomalies_details.put(ANOMALY_CATEGORY_OTHERS, outra_tree);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public String get_logo_id_from_anomalie(String anomalie) {
		final String method = "get_logo_id_from_anomalie";
		
		try {

			/* Previous version
			for (String key :anomalies_details.keySet())
				if (anomalies_details.get(key).equals(anomalie))
					return key;
			*/
			
			for (Entry<String, String> entry : anomalies.entrySet())
				if (entry.getValue().equals(anomalie))
					return entry.getKey();
					
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
	public TreeMap<String, String> get_anomalies() {
		return anomalies;
	}
	
	public TreeMap<String, String> get_anomalies_details(String anomaly) {
		final String method = "get_anomalies_details";
		
		try {
			
			for (String key :anomalies_details.keySet())
				if (key.equals(anomaly))
					return anomalies_details.get(key);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\nanomalies :"+anomalies.toString());
			sb.append("\nanomalies_details :"+anomalies_details.toString());
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
}

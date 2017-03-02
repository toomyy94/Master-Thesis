package PT.PTInov.ArQoSPocketEDP.Culture;

import java.util.Locale;

import PT.PTInov.ArQoSPocketEDP.R;
import android.content.Context;

public class Dictionary {
	
	//public static CultureEnum myCulture = CultureEnum.Portuguese;
	public static CultureEnum myCulture = CultureEnum.English;
	
	
	public static void setSystemCulture() {
		
		String culture = Locale.getDefault().getDisplayLanguage();
		
		myCulture = CultureEnum.Portuguese;
		
		/*
		if (culture.contains("português")) {
			myCulture = CultureEnum.Portuguese;
		} else {
			myCulture = CultureEnum.English;
		}*/
	}
	
	private static String getContryLang() {
		
		switch (myCulture) {
			case Portuguese:
				return "pt";
			case English:
				return "eng";
			default:
				return "eng";
		}
	}
	
	private static String getResource(Context c, String rName) {
		return c.getResources().getText(c.getResources().getIdentifier(rName, "string", c.getPackageName()))+"";
	}
	
	
	/*
	 * 
	 * Lang Methods
	 * 
	 */
	
	public static String getOther(Context c) {
		return getResource(c, "app_other_"+getContryLang());
	}
	
	public static String getDetails(Context c) {
		return getResource(c, "app_details_"+getContryLang());
	}
	
	public static String getNeighboring_networks(Context c) {
		return getResource(c, "app_neighboring_networks_"+getContryLang());
	}
	
	public static String getOperatorName(Context c) {
		return getResource(c, "app_operator_name_"+getContryLang());
	}
	
	public static String getNoResults(Context c) {
		return getResource(c, "app_state_no_results_"+getContryLang());
	}
	
	public static String getMoreDetails(Context c) {
		return getResource(c, "mais_detalhes_"+getContryLang());
	}
	
	public static String getLocal(Context c) {
		return getResource(c, "local_"+getContryLang());
	}
	
	public static int getListResourceID(Context c) {
		return c.getResources().getIdentifier("places_array_"+getContryLang(), "array", c.getPackageName());
	}
	
	public static String[] getList(Context c) {
		return c.getResources().getStringArray(c.getResources().getIdentifier("places_array_"+getContryLang(), "array", c.getPackageName()));
	}
	
	public static String getApp_state_exec(Context c) {
        
		return getResource(c, "app_state_exec_"+getContryLang());
	}
	
	public static String getApp_state_exec_success(Context c) {
        
		return getResource(c, "app_state_exec_success_"+getContryLang());
	}
	
	public static String getApp_state_exec_fail(Context c) {
        
		return getResource(c, "app_state_exec_fail_"+getContryLang());
	}
	
	public static String getApp_state_stopped(Context c) {
        
		return getResource(c, "app_state_stopped_"+getContryLang());
	}
	
	public static String getApp_registe_state(Context c) {
        
		return getResource(c, "app_registe_state_"+getContryLang());
	}
	
	public static String getApp_doaction(Context c) {
        
		return getResource(c, "app_doaction_"+getContryLang());
	}
	
	public static String getApp_registe_state_time(Context c) {
	    
		return getResource(c, "app_registe_state_time_"+getContryLang());
	}
	
	public static String getApp_results_header(Context c) {
        
		return getResource(c, "app_results_header_"+getContryLang());
	}
	
	public static String getApp_results_details_header(Context c) {
        
		return getResource(c, "app_results_details_header_"+getContryLang());
	}
	
	public static String getApp_results_location_header(Context c) {
    
		return getResource(c, "app_results_location_header_"+getContryLang());
	}

	public static String getApp_results_latitude(Context c) {
    
		return getResource(c, "app_results_latitude_"+getContryLang());
	}
	
	public static String getApp_results_longitude(Context c) {
    
		return getResource(c, "app_results_longitude_"+getContryLang());
	}
	
	public static String getApp_results_Network(Context c) {
    
		return getResource(c, "app_results_Network_"+getContryLang());
	}

	public static String getApp_results_RSSI(Context c) {
    
		return getResource(c, "app_results_RSSI_"+getContryLang());
	}
	
	public static String getApp_results_dBm(Context c) {
    
		return getResource(c, "app_results_dBm_"+getContryLang());
	}

	public static String getApp_results_Bit_Error_Rate(Context c) {
    
		return getResource(c, "app_results_Bit_Error_Rate_"+getContryLang());
	}
	
	public static String getApp_results_Celula_ID(Context c) {
    
		return getResource(c, "app_results_Celula_ID_"+getContryLang());
	}
	
	public static String getApp_results_Lac_Tac(Context c) {
    
		return getResource(c, "app_results_Lac_Tac_"+getContryLang());
	}
	
	public static String getApp_results_connection(Context c) {
    
		return getResource(c, "app_results_connection_"+getContryLang());
	}
	
	public static String getApp_results_details(Context c) {
    
		return getResource(c, "app_results_details_"+getContryLang());
	}

	public static String getApp_results_send_to_server(Context c) {
    
		return getResource(c, "app_results_send_to_server_"+getContryLang());
	}
	
}

package PT.PTInov.ArQoSPocket.Culture;

import java.util.Locale;

import android.R;
import android.content.Context;

public class Dictionary {
	
	//public static CultureEnum myCulture = CultureEnum.Portuguese;
	public static CultureEnum myCulture = CultureEnum.English;
	
	
	public static void setSystemCulture() {
		
		String culture = Locale.getDefault().getDisplayLanguage();
		
		if (culture.contains("português")) {
			myCulture = CultureEnum.Portuguese;
		} else {
			myCulture = CultureEnum.English;
		}
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
	
	public static String getApp_medidas_radio(Context c) {
		return getResource(c, "app_medidas_radio_"+getContryLang());
	}
	
	public static String getApp_teste_banda(Context c) {
		return getResource(c, "app_teste_banda_"+getContryLang());
	}
	
	public static String getApp_teste_ftp(Context c) {
		return getResource(c, "app_teste_ftp_"+getContryLang());
	}
	
	public static String getApp_results_REGISTED_OPERATOR_NAME_ID(Context c) {
		return getResource(c, "app_results_REGISTED_OPERATOR_NAME_ID_"+getContryLang());
	}
	
	public static String getApp_results_REGISTED_OPERATOR_NAME(Context c) {
		return getResource(c, "app_results_REGISTED_OPERATOR_NAME_"+getContryLang());
	}

	public static String getApp_results_Network_Mode(Context c) {
		return getResource(c, "app_results_Network_Mode_"+getContryLang());
	}
	
	public static String getApp_teste_acesso(Context c) {
		return getResource(c, "app_teste_acesso_"+getContryLang());
	}
	
	public static String getApp_min_rtt(Context c) {
		return getResource(c, "app_min_rtt_"+getContryLang());
	}
	
	public static String getApp_max_rtt(Context c) {
		return getResource(c, "app_max_rtt_"+getContryLang());
	}
	
	public static String getApp_med_rtt(Context c) {
		return getResource(c, "app_med_rtt_"+getContryLang());
	}
	
	public static String getApp_packet_lost(Context c) {
		return getResource(c, "app_packet_lost_"+getContryLang());
	}
	
	public static String getApp_debito_download(Context c) {
		return getResource(c, "app_debito_download_"+getContryLang());
	}
	
	public static String getApp_debito_upload(Context c) {
		return getResource(c, "app_debito_upload_"+getContryLang());
	}
	
	public static String getApp_max_debito_download(Context c) {
		return getResource(c, "app_max_debito_download_"+getContryLang());
	}
	
	public static String getApp_max_debito_upload(Context c) {
		return getResource(c, "app_max_debito_upload_"+getContryLang());
	}
	
	public static String getApp_testar_medidas_radio(Context c) {
		return getResource(c, "app_testar_medidas_radio_"+getContryLang());
	}
	
	public static String getApp_testar_teste_acesso(Context c) {
		return getResource(c, "app_testar_teste_acesso_"+getContryLang());
	}
	
	public static String getApp_testar_teste_banda(Context c) {
		return getResource(c, "app_testar_teste_banda_"+getContryLang());
	}
	
	public static String getApp_testar_teste_ftp(Context c) {
		return getResource(c, "app_testar_teste_ftp_"+getContryLang());
	}
	
	public static String getApp_sending_info_to_server(Context c) {
		return getResource(c, "app_sending_info_to_server_"+getContryLang());
	}
	
	
}

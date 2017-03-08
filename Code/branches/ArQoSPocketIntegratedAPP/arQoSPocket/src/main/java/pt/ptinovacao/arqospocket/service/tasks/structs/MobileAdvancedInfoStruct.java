package pt.ptinovacao.arqospocket.service.tasks.structs;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.telephony.CellInfo;
import android.telephony.NeighboringCellInfo;

import pt.ptinovacao.arqospocket.service.enums.EMobileNetworkMode;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.enums.EMobileState;

public class MobileAdvancedInfoStruct extends MobileBasicInfoStruct {
	
	private final static Logger logger = LoggerFactory.getLogger(MobileAdvancedInfoStruct.class);

	private String device_id;
	private String msisdn;
	private String network_operator_name;
	private String imsi;
	private boolean roaming;
	
	private List<NeighboringCellInfo> neighboring_cell_list;
	private List<CellInfo> all_cell_info_list;
	
	public MobileAdvancedInfoStruct(String signal_level, String id_celula,
									String cell_location, EMobileNetworkMode network_type, EMobileState mobile_state,
									String mcc, String mcc_mnc, String device_id, String msisdn, String network_operator_name,
									String imsi, boolean roaming, List<NeighboringCellInfo> neighboring_cell_list, List<CellInfo> all_cell_info_list) {
		super(signal_level, id_celula, cell_location, network_type, mobile_state, mcc, mcc_mnc);
		
		this.device_id = device_id;
		this.msisdn = msisdn;
		this.network_operator_name = network_operator_name;
		this.imsi = imsi;
		this.roaming = roaming;
		
		this.neighboring_cell_list = neighboring_cell_list;
		this.all_cell_info_list = all_cell_info_list;
	}

	
	public String get_device_id() {
		return device_id;
	}

	public String get_msisdn() {
		return msisdn;
	}
	
	public String get_network_operator_name() {
		return network_operator_name;
	}
	
	public String get_imsi() {
		return imsi;
	}

	public boolean get_roaming() {
		return roaming;
	}

	public List<NeighboringCellInfo> get_neighboring_cell_list() {
		return neighboring_cell_list;
	}
	
	public List<CellInfo> get_all_cell_info_list() {
		return all_cell_info_list;
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\ndevice_id :"+device_id);
			sb.append("\nmsisdn :"+msisdn);
			sb.append("\nnetwork_operator_name :"+network_operator_name);
			sb.append("\nimsi :"+imsi);
			sb.append("\nroaming :"+roaming);
			
			if (neighboring_cell_list != null)
				sb.append("\nneighboring_cell_list :"+neighboring_cell_list.toString());
			else
				sb.append("\nneighboring_cell_list :null");
			
			if (all_cell_info_list != null)
				sb.append("\nall_cell_info_list :"+all_cell_info_list.toString());
			else
				sb.append("\nall_cell_info_list :null");
			
			sb.append("\nMobileBasicInfoStruct :"+super.toString());
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
}

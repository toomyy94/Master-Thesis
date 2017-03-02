package pt.ptinovacao.arqospocket.service.tasks.structs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.enums.EMobileNetworkMode;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.enums.EMobileState;

public class MobileBasicInfoStruct {
	
	private final static Logger logger = LoggerFactory.getLogger(MobileBasicInfoStruct.class);

	private String signal_level;
	private String id_cell;
	private String cell_location;
	private EMobileNetworkMode network_type;
	
	private String mcc;
	private String mcc_mnc;
	
	private EMobileState mobile_state;
	
	public MobileBasicInfoStruct(String signal_level, String id_cell, String cell_location, 
			EMobileNetworkMode network_type, EMobileState mobile_state, String mcc, String mcc_mnc) {
		final String method = "MobileBasicInfoStruct";
		
		try {
			
			this.signal_level = signal_level;
			this.id_cell = id_cell;
			this.cell_location = cell_location;
			this.network_type = network_type;
			
			this.mcc = mcc;
			this.mcc_mnc = mcc_mnc;
			
			this.mobile_state = mobile_state;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
	}
	
	public String get_signal_level() {
		return signal_level;
	}
	
	public String get_id_cell() {
		return id_cell;
	}
	
	public String get_cell_location() {
		return cell_location;
	}
	
	public EMobileNetworkMode get_network_type() {
		return network_type;
	}
	
	public EMobileState get_mobile_state() {
		return mobile_state;
	}
	
	public String get_mcc_mnc() {
		return mcc_mnc;
	}
	
	public String get_mcc() {
		return mcc;
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\nsignal_level :"+signal_level);
			sb.append("\nid_cell :"+id_cell);
			sb.append("\ncell_location :"+cell_location);
			sb.append("\nnetwork_type :"+network_type);
			sb.append("\nmobile_state :"+mobile_state);
			sb.append("\nmcc_mnc :"+mcc_mnc);
			sb.append("\nmcc :"+mcc);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
}

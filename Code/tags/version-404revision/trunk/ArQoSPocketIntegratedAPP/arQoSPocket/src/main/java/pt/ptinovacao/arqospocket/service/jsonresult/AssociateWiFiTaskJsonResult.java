package pt.ptinovacao.arqospocket.service.jsonresult;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.service.enums.EConnectionTechnology;
import pt.ptinovacao.arqospocket.service.enums.ETestTaskState;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.enums.ERunTestTaskState;
import pt.ptinovacao.arqospocket.service.interfaces.ITaskResult;
import pt.ptinovacao.arqospocket.service.structs.MyLocation;

import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_OK;

public class AssociateWiFiTaskJsonResult extends TaskJsonResult implements ITaskResult, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(AssociateWiFiTaskJsonResult.class);

	private String ssid;
	private String mac;
	private String frequency;
	private String channel;
	private String mode;
	
	private String protocol;
	private String bitrate;
	private String encryption;
	private String association_time_in_sec;
	private String signal_level;
	private String noise_level;
	private String ratio_signal_noise;
	private String address;
	private String mask;
	private String gateway;
	private String dns;
	private String domain;
	private String lease_in_sec;
	
	public AssociateWiFiTaskJsonResult(String task_id, String task_name,
			String macro_id, String task_number, String iccid, String cell_id,
			String loc_gps, String status, Date dateini, Date datefim, String ssid,
			String mac, String frequency, String channel, String mode, String protocol,
			String bitrate, String encryption, String association_time_in_sec, String signal_level,
			String noise_level, String ratio_signal_noise, String address, String mask, String gateway,
			String dns, String domain, String lease_in_sec, MyLocation taskLocation, EConnectionTechnology execution_in_technology) {
		
		super(task_id, task_name, macro_id, task_number, iccid, cell_id, loc_gps,
				status, dateini, datefim, taskLocation, execution_in_technology);
		
		final String method = "AssociateWiFiTaskResult";
		
		try {
		
			this.ssid = ssid;
			this.mac = mac;
			this.frequency = frequency;
			this.channel = channel;
			this.mode = mode;
			
			this.protocol = protocol;
			this.bitrate = bitrate;
			this.encryption = encryption;
			this.association_time_in_sec = association_time_in_sec;
			this.signal_level = signal_level;
			this.noise_level = noise_level;
			this.ratio_signal_noise = ratio_signal_noise;
			this.address = address;
			this.mask = mask;
			this.gateway = gateway;
			this.dns = dns;
			this.domain = domain;
			this.lease_in_sec = lease_in_sec;
			
			
			MyLogger.debug(logger, method, "ssid :"+ssid);
			MyLogger.debug(logger, method, "mac :"+mac);
			MyLogger.debug(logger, method, "frequency :"+frequency);
			MyLogger.debug(logger, method, "channel :"+channel);
			MyLogger.debug(logger, method, "mode :"+mode);
			
			MyLogger.debug(logger, method, "protocol :"+protocol);
			MyLogger.debug(logger, method, "bitrate :"+bitrate);
			MyLogger.debug(logger, method, "encryption :"+encryption);
			MyLogger.debug(logger, method, "association_time_in_sec :"+association_time_in_sec);
			MyLogger.debug(logger, method, "signal_level :"+signal_level);
			MyLogger.debug(logger, method, "noise_level :"+noise_level);
			MyLogger.debug(logger, method, "ratio_signal_noise :"+ratio_signal_noise);
			MyLogger.debug(logger, method, "address :"+address);
			MyLogger.debug(logger, method, "mask :"+mask);
			MyLogger.debug(logger, method, "gateway :"+gateway);
			MyLogger.debug(logger, method, "dns :"+dns);
			MyLogger.debug(logger, method, "domain :"+domain);
			MyLogger.debug(logger, method, "lease_in_sec :"+lease_in_sec);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public String get_ssid() {
		return ssid;
	}
	
	public String get_mac() {
		return mac;
	}
	
	public String get_frequency() {
		return frequency;
	}
	
	public String get_channel() {
		return channel;
	}
	
	public String get_mode() {
		return mode;
	}
	
	public String get_protocol() {
		return protocol;
	}
	
	public String get_bitrate() {
		return bitrate;
	}
	
	public String get_encryption() {
		return encryption;
	}
	
	public String get_association_time_in_sec() {
		return association_time_in_sec;
	}
	
	public String get_signal_level() {
		return signal_level;
	}
	
	public String get_noise_level() {
		return noise_level;
	}
	
	public String get_ratio_signal_noise() {
		return ratio_signal_noise;
	}
	
	public String get_address() {
		return address;
	}
	
	public String get_mask() {
		return mask;
	}
	
	public String get_gateway() {
		return gateway;
	}
	
	public String get_dns() {
		return dns;
	}
	
	public String get_domain() {
		return domain;
	}
	
	public String get_lease_in_sec() {
		return lease_in_sec;
	}
	
	public String buildTaskJsonResult() {
		final String method = "buildTaskJsonResult";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("{");
			sb.append("\"task_id\":\""+super.get_task_id()+"\",");
			sb.append("\"task_name\":\""+super.get_task_name()+"\",");
			sb.append("\"macro_id\":\""+super.get_macro_id()+"\",");
			sb.append("\"task_number\":\""+super.get_task_number()+"\",");
			sb.append("\"init_date\":\""+super.get_init_date()+"\",");
			sb.append("\"end_date\":\""+super.get_end_date()+"\",");
			sb.append("\"iccid\":\""+super.get_iccid()+"\",");
			sb.append("\"cell_id\":\""+super.get_cell_id()+"\",");
			sb.append("\"loc_gps\":\""+super.get_loc_gps()+"\",");
			sb.append("\"status\":\""+super.get_status()+"\",");
			sb.append("\"ssid\":\""+ssid+"\",");
			sb.append("\"mac\":\""+mac+"\",");
			sb.append("\"frequency\":\""+frequency+"\",");
			sb.append("\"channel\":\""+channel+"\",");
			sb.append("\"mode\":\""+mode+"\",");
			sb.append("\"protocol\":\""+protocol+"\",");
			sb.append("\"bitrate\":\""+bitrate+"\",");
			sb.append("\"encryption\":\""+encryption+"\",");
			sb.append("\"association_time_in_sec\":\""+association_time_in_sec+"\",");
			sb.append("\"signal_level\":\""+signal_level+"\",");
			sb.append("\"noise_level\":\""+noise_level+"\",");
			sb.append("\"ratio_signal_noise\":\""+ratio_signal_noise+"\",");
			sb.append("\"address\":\""+address+"\",");
			sb.append("\"mask\":\""+mask+"\",");
			sb.append("\"gateway\":\""+gateway+"\",");
			sb.append("\"dns\":\""+dns+"\",");
			sb.append("\"domain\":\""+domain+"\",");
			sb.append("\"lease_in_sec\":\""+lease_in_sec+"\"");
			sb.append("}");
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
	

	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\ntask_id :"+super.get_task_id());
			sb.append("\ntask_name :"+super.get_task_name());
			sb.append("\nmacro_id :"+super.get_macro_id());
			sb.append("\ntask_number :"+super.get_task_number());
			sb.append("\ndateini :"+super.get_dateini());
			sb.append("\ndatefim :"+super.get_datefim());
			sb.append("\niccid :"+super.get_iccid());
			sb.append("\ncell_id :"+super.get_cell_id());
			sb.append("\nloc_gps :"+super.get_loc_gps());
			sb.append("\nstatus :"+super.get_status());
			sb.append("\ninit_date :"+super.get_init_date());
			sb.append("\nend_date :"+super.get_end_date());
			sb.append("\nssid :"+ssid);
			sb.append("\nmac :"+mac);
			sb.append("\nfrequency :"+frequency);	
			sb.append("\nchannel :"+channel);	
			sb.append("\nmode :"+mode);			
			sb.append("\nprotocol :"+protocol);	
			sb.append("\nbitrate :"+bitrate);	
			sb.append("\nencryption :"+encryption);	
			sb.append("\nassociation_time_in_sec :"+association_time_in_sec);	
			sb.append("\nsignal_level :"+signal_level);	
			sb.append("\nnoise_level :"+noise_level);	
			sb.append("\nratio_signal_noise :"+ratio_signal_noise);	
			sb.append("\naddress :"+address);
			sb.append("\nmask :"+mask);
			sb.append("\ngateway :"+gateway);	
			sb.append("\ndns :"+dns);	
			sb.append("\ndomain :"+domain);	
			sb.append("\nlease_in_sec :"+lease_in_sec);	
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
	
	
	/************
	 * 
	 * 		ITaskResult
	 * 
	 * ***********************/

	@Override
	public ETestTaskState get_task_state() {
		final String method = "get_task_state";
		
		try {
			return super.get_status().startsWith(STATUS_OK)?ETestTaskState.OK:ETestTaskState.NOK;
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}

	@Override
	public ERunTestTaskState get_run_task_state() {

		return ERunTestTaskState.DONE;
	}

	@Override
	public Map<String, String> get_task_results(Context context) {
		final String method = "get_task_results";
		try {
			
			Map<String, String> task_result_list = new TreeMap<String, String>();
			
			task_result_list.put(context.getString(R.string.ssid).toUpperCase(), ssid);
			task_result_list.put(context.getString(R.string.task_result_mac), mac);
			task_result_list.put(context.getString(R.string.frequency_mhz), frequency);
			task_result_list.put(context.getString(R.string.task_result_channel), channel);
			task_result_list.put(context.getString(R.string.task_result_mode), mode);
			task_result_list.put(context.getString(R.string.task_result_protocol), protocol);
			task_result_list.put(context.getString(R.string.task_result_bitrate), bitrate);
			task_result_list.put(context.getString(R.string.task_result_encryption), encryption);
			task_result_list.put(context.getString(R.string.task_result_association_time_sec), association_time_in_sec);
			task_result_list.put(context.getString(R.string.task_result_signal_level), signal_level);
			task_result_list.put(context.getString(R.string.task_result_noise_level), noise_level);
			task_result_list.put(context.getString(R.string.task_result_ip_address), address);
			task_result_list.put(context.getString(R.string.task_result_subnet_mask), mask);
			task_result_list.put(context.getString(R.string.task_result_gateway), gateway);
			task_result_list.put(context.getString(R.string.task_result_dns), dns);
			task_result_list.put(context.getString(R.string.task_result_domain), domain);
			task_result_list.put(context.getString(R.string.task_result_lease_time_sec), lease_in_sec);
			
			return task_result_list;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}

	@Override
	public EConnectionTechnology get_task_technology() {
		return super.get_task_technology();
	}

	@Override
	public MyLocation get_test_execution_location() {
		return super.get_task_execution_location();
	}

	public Object clone() {
		final String method = "clone";
		
		try {
			
			return new AssociateWiFiTaskJsonResult(super.task_id, super.task_name, super.macro_id, super.task_number, super.iccid, 
					super.cell_id, super.loc_gps, super.status, super.dateini, super.datefim, this.ssid,
					this.mac, this.frequency, this.channel, this.mode, this.protocol,
					this.bitrate, this.encryption, this.association_time_in_sec, this.signal_level,
					this.noise_level, this.ratio_signal_noise, this.address, this.mask, this.gateway,
					this.dns, this.domain, this.lease_in_sec, (MyLocation) super.taskLocation.clone(), super.execution_in_technology);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
}

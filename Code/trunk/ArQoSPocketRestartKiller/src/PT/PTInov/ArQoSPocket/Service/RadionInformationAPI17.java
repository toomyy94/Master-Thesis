package PT.PTInov.ArQoSPocket.Service;

import java.util.List;

import PT.PTInov.ArQoSPocket.Utils.LogType;
import PT.PTInov.ArQoSPocket.Utils.Logger;
import PT.PTInov.ArQoSPocket.structs.ECallState;
import PT.PTInov.ArQoSPocket.structs.EDataActivity;
import PT.PTInov.ArQoSPocket.structs.EDataState;
import PT.PTInov.ArQoSPocket.structs.ESimState;
import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

@SuppressLint("NewApi")
public class RadionInformationAPI17 {

	private final static String tag = "RadionInformationAPI17";
	
	private Context myContext = null;
	private static TelephonyManager telephonyManager = null;
	
	
	private List<CellInfo> observedCells = null;
	private List<NeighboringCellInfo> neighboringCellList = null;
	
	// Informação da Celula a qual estou ligado
	private SignalStrength observedSignalStrength = null;
	
	
	public RadionInformationAPI17(Context c) {
		final String method = "RadioInformation";
		
		try {

			myContext = c;
			
			telephonyManager = (TelephonyManager) myContext.getSystemService(Context.TELEPHONY_SERVICE);
			
			// registry the listeners
			telephonyManager.listen(cellStrengthListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
			telephonyManager.listen(cellInfoListener,PhoneStateListener.LISTEN_CELL_INFO);

			
		} catch(Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
	}
	
	
	
	
	public int get_current_network_type() {
		final String method = "get_current_network_type";
		
		try {
			Logger.v(tag, method, LogType.Trace, "In");
			
			int currentNetworkType = telephonyManager.getNetworkType();
			Logger.v(tag, method, LogType.Trace, "currentNetworkType :"+currentNetworkType);
			
			return currentNetworkType;
		
		} catch(Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return -1;
	}
	
	public int get_phone_type() {
		final String method = "get_phone_type";
		
		try {
			Logger.v(tag, method, LogType.Trace, "In");
			
			int phoneType = telephonyManager.getPhoneType();
			Logger.v(tag, method, LogType.Trace, "currentNetworkType :"+phoneType);
			
			return phoneType;
		
		} catch(Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return -1;
	}
	
	public List<CellInfo> get_all_cells() {
		final String method = "get_all_cells";
		
		List<CellInfo> all_Cells = null;
		
		try {
			Logger.v(tag, method, LogType.Trace, "In");
			
			all_Cells = telephonyManager.getAllCellInfo();
			Logger.v(tag, method, LogType.Trace, "all_Cells :"+all_Cells.toString());
			
			if (observedCells == null)
				observedCells = all_Cells;
			else
				all_Cells = observedCells;
				
		
		} catch(Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return all_Cells;
	}

	public ECallState get_phone_call_state() {
		final String method = "get_phone_call_state";

		try {
			Logger.v(tag, method, LogType.Trace, "In");

			int call_state = telephonyManager.getCallState();
			Logger.v(tag, method, LogType.Trace, "call_state :" + call_state);

			switch (call_state) {
				case TelephonyManager.CALL_STATE_IDLE:
					return ECallState.CALL_STATE_IDLE;
				case TelephonyManager.CALL_STATE_OFFHOOK:
					return ECallState.CALL_STATE_OFFHOOK;
				case TelephonyManager.CALL_STATE_RINGING:
					return ECallState.CALL_STATE_RINGING;
			}

		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}

		return ECallState.NA;
	}
	
	public EDataActivity get_data_activity() {
		final String method = "get_data_activity";

		try {
			Logger.v(tag, method, LogType.Trace, "In");

			int call_state = telephonyManager.getDataActivity();
			Logger.v(tag, method, LogType.Trace, "call_state :" + call_state);

			switch (call_state) {
				case TelephonyManager.DATA_ACTIVITY_NONE:
					return EDataActivity.DATA_ACTIVITY_NONE;
				case TelephonyManager.DATA_ACTIVITY_IN:
					return EDataActivity.DATA_ACTIVITY_IN;
				case TelephonyManager.DATA_ACTIVITY_OUT:
					return EDataActivity.DATA_ACTIVITY_OUT;
				case TelephonyManager.DATA_ACTIVITY_INOUT:
					return EDataActivity.DATA_ACTIVITY_INOUT;
				case TelephonyManager.DATA_ACTIVITY_DORMANT:
					return EDataActivity.DATA_ACTIVITY_DORMANT;
			}

		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}

		return EDataActivity.NA;
	}
	
	public EDataState get_data_state() {
		final String method = "get_data_state";

		try {
			Logger.v(tag, method, LogType.Trace, "In");

			int call_state = telephonyManager.getDataState();
			Logger.v(tag, method, LogType.Trace, "call_state :" + call_state);

			switch (call_state) {
				case TelephonyManager.DATA_DISCONNECTED:
					return EDataState.DATA_DISCONNECTED;
				case TelephonyManager.DATA_CONNECTING:
					return EDataState.DATA_CONNECTING;
				case TelephonyManager.DATA_CONNECTED:
					return EDataState.DATA_CONNECTED;
				case TelephonyManager.DATA_SUSPENDED:
					return EDataState.DATA_SUSPENDED;
			}

		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}

		return EDataState.NA;
	}
	
	public String get_device_id() {
		
		final String method = "get_device_id";

		try {
			Logger.v(tag, method, LogType.Trace, "In");

			String device_id = telephonyManager.getDeviceId();
			Logger.v(tag, method, LogType.Trace, "device_id :" + device_id);

			return device_id;
			
		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}

		return "NA";
	}
	
	public String get_device_software_version() {
		
		final String method = "get_device_software_version";

		try {
			Logger.v(tag, method, LogType.Trace, "In");

			String device_software_version = telephonyManager.getDeviceSoftwareVersion();
			Logger.v(tag, method, LogType.Trace, "device_software_version :" + device_software_version);

			return device_software_version;
			
		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}

		return "NA";
	}
	
	public String get_line_number() {
		
		final String method = "get_line_number";

		try {
			Logger.v(tag, method, LogType.Trace, "In");

			String line_number = telephonyManager.getLine1Number();
			Logger.v(tag, method, LogType.Trace, "line_number :" + line_number);

			return line_number;
			
		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}

		return "NA";
	}
	
	public List<NeighboringCellInfo> get_neighboring_cell_info() {
		final String method = "get_neighboring_cell_info";

		try {
			Logger.v(tag, method, LogType.Trace, "In");

			List<NeighboringCellInfo> neighboring_cell_list = telephonyManager.getNeighboringCellInfo();
			Logger.v(tag, method, LogType.Trace, "neighboring_cell_list :" + neighboring_cell_list.toString());

			if (neighboringCellList == null)
				neighboringCellList = neighboring_cell_list;
			
			return neighboringCellList;
			
		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}

		return null;
	}
	
	public String get_network_country_iso() {
		final String method = "get_network_country_iso";

		try {
			Logger.v(tag, method, LogType.Trace, "In");

			String network_country_iso = telephonyManager.getNetworkCountryIso();
			Logger.v(tag, method, LogType.Trace, "network_country_iso :" + network_country_iso);

			return network_country_iso;
			
		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}

		return "NA";
	}
	
	public String get_network_operator() {
		final String method = "get_network_operator";

		try {
			Logger.v(tag, method, LogType.Trace, "In");

			String network_operator = telephonyManager.getNetworkOperator ();
			Logger.v(tag, method, LogType.Trace, "network_operator :" + network_operator);

			return network_operator;
			
		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}

		return "NA";
	}
	
	public String get_network_operator_name() {
		final String method = "get_network_operator_name";

		try {
			Logger.v(tag, method, LogType.Trace, "In");

			String network_operator_name = telephonyManager.getNetworkOperatorName();
			Logger.v(tag, method, LogType.Trace, "network_operator_name :" + network_operator_name);

			return network_operator_name;
			
		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}

		return "NA";
	}
	
	public String get_sim_country_iso() {
		final String method = "get_sim_country_iso";

		try {
			Logger.v(tag, method, LogType.Trace, "In");

			String sim_country_iso = telephonyManager.getSimCountryIso();
			Logger.v(tag, method, LogType.Trace, "sim_country_iso :" + sim_country_iso);

			return sim_country_iso;
			
		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}

		return "NA";
	}
	
	public String get_sim_operator() {
		final String method = "get_sim_operator";

		try {
			Logger.v(tag, method, LogType.Trace, "In");

			String sim_operator = telephonyManager.getSimOperator();
			Logger.v(tag, method, LogType.Trace, "sim_operator :" + sim_operator);

			return sim_operator;
			
		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}

		return "NA";
	}
	
	public String get_sim_operator_name() {
		final String method = "get_sim_operator_name";

		try {
			Logger.v(tag, method, LogType.Trace, "In");

			String sim_operator_name = telephonyManager.getSimOperatorName();
			Logger.v(tag, method, LogType.Trace, "sim_operator_name :" + sim_operator_name);

			return sim_operator_name;
			
		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}

		return "NA";
	}
	
	public String get_sim_serial_number() {
		final String method = "get_sim_serial_number";

		try {
			Logger.v(tag, method, LogType.Trace, "In");

			String sim_serial_number = telephonyManager.getSimSerialNumber();
			Logger.v(tag, method, LogType.Trace, "sim_serial_number :" + sim_serial_number);

			return sim_serial_number;
			
		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}

		return "NA";
	}
	
	public ESimState get_sim_state() {
		final String method = "get_sim_state";

		try {
			Logger.v(tag, method, LogType.Trace, "In");

			int sim_state = telephonyManager.getSimState();
			Logger.v(tag, method, LogType.Trace, "sim_state :" + sim_state);

			switch(sim_state) {
				case TelephonyManager.SIM_STATE_UNKNOWN:
					return ESimState.SIM_STATE_UNKNOWN;
				case TelephonyManager.SIM_STATE_ABSENT:
					return ESimState.SIM_STATE_ABSENT;
				case TelephonyManager.SIM_STATE_PIN_REQUIRED:
					return ESimState.SIM_STATE_PIN_REQUIRED;
				case TelephonyManager.SIM_STATE_PUK_REQUIRED:
					return ESimState.SIM_STATE_PUK_REQUIRED;
				case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
					return ESimState.SIM_STATE_NETWORK_LOCKED;
				case TelephonyManager.SIM_STATE_READY:
					return ESimState.SIM_STATE_READY;
			}
			
		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}

		return ESimState.NA;
	}
	
	// IMSI for GSM
	public String get_subscriber_id() {
		final String method = "get_subscriber_id";

		try {
			Logger.v(tag, method, LogType.Trace, "In");

			String subscriber_id = telephonyManager.getSubscriberId();
			Logger.v(tag, method, LogType.Trace, "subscriber_id :" + subscriber_id);

			return subscriber_id;
			
		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}

		return "NA";
	}
	
	public String get_voice_mail_alpha_tag() {
		final String method = "get_voice_mail_alpha_tag";

		try {
			Logger.v(tag, method, LogType.Trace, "In");

			String voice_mail_alpha_tag = telephonyManager.getVoiceMailAlphaTag();
			Logger.v(tag, method, LogType.Trace, "voice_mail_alpha_tag :" + voice_mail_alpha_tag);

			return voice_mail_alpha_tag;
			
		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}

		return "NA";
	}
	
	public String get_voice_mail_number() {
		final String method = "get_voice_mail_number";

		try {
			Logger.v(tag, method, LogType.Trace, "In");

			String voice_mail_number = telephonyManager.getVoiceMailNumber();
			Logger.v(tag, method, LogType.Trace, "voice_mail_number :" + voice_mail_number);

			return voice_mail_number;
			
		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}

		return "NA";
	}
	
	public boolean is_network_roaming() {
		final String method = "is_network_roaming";

		try {
			Logger.v(tag, method, LogType.Trace, "In");

			boolean etwork_roaming = telephonyManager.isNetworkRoaming();
			Logger.v(tag, method, LogType.Trace, "etwork_roaming :" + etwork_roaming);

			return etwork_roaming;
			
		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}

		return false;
	}
	
	/***
	 *
	 * Events 
	 * 
	 ***/
	 
	private PhoneStateListener cellInfoListener = new PhoneStateListener() {
		public void onCellInfoChanged(List<CellInfo> cellInfo) {
			final String method = "onCellInfoChanged";
			
			Logger.v(tag, method, LogType.Trace, "In");

			try {
				
				observedCells = cellInfo;
				
			} catch (Exception ex) {
				Logger.v(tag, method, LogType.Error,ex.toString());
			}
		}
	};
	
	private PhoneStateListener cellStrengthListener = new PhoneStateListener() {
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			final String method = "onSignalStrengthsChanged";
			
			Logger.v(tag, method, LogType.Trace, "onSignalStrengthsChanged");

			try {
				
				observedSignalStrength = signalStrength;

			} catch (Exception ex) {
				Logger.v(tag, method, LogType.Error, ex.toString());
			}
		}
	};
	
	/***
	 * 
	 * Wrapper SignalStrength information
	 * 
	 */
	
	@SuppressLint("NewApi")
	public int get_rssi() {
		final String method = "get_rssi";
		
		try {
			return observedSignalStrength.getEvdoDbm();
		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return -1;
	}
	
	@SuppressLint("NewApi")
	public int get_ecio() {
		final String method = "get_ecio";
		
		try {
			return observedSignalStrength.getCdmaEcio();
		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return -1;
	}
	
	@SuppressLint("NewApi")
	public int get_signal_noise_ratio() {
		final String method = "get_signal_noise_ratio";
		
		try {
			return observedSignalStrength.getEvdoSnr();
		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return -1;
	}
	
	@SuppressLint("NewApi")
	public int get_bit_error_rate() {
		final String method = "get_bit_error_rate";
		
		try {
			return observedSignalStrength.getGsmBitErrorRate();
		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return -1;
	}
	
	public int get_signal_strength() {
		final String method = "get_signal_strength";
		
		try {
			return observedSignalStrength.getGsmSignalStrength();
		} catch (Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return -1;
	}
	
	/***
	 * 
	 * Wrapper Cell Identity
	 * 
	 */
	
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			Logger.v(tag, method, LogType.Trace, "1");
			sb.append("\n ----- All cells information ----------");
			if (this.get_all_cells() != null)
			for (CellInfo c :this.get_all_cells()) {
				sb.append("\n----------");
				if (c instanceof CellInfoLte) {
					
					CellInfoLte localc = (CellInfoLte) c;
					
					sb.append("\n ---");
					
					sb.append("\n localc.describeContents :"+ localc.describeContents());
					sb.append("\n localc.getTimeStamp :"+ localc.getTimeStamp());
					sb.append("\n localc.isRegistered :"+ localc.isRegistered());
					
					sb.append("\n ---");
					
					CellIdentityLte cellIdentityLte = localc.getCellIdentity();
					
					sb.append("\n cellIdentityLte.describeContents :"+ cellIdentityLte.describeContents());
					sb.append("\n cellIdentityLte.getCi :"+ cellIdentityLte.getCi());
					sb.append("\n cellIdentityLte.getMcc :"+ cellIdentityLte.getMcc());
					sb.append("\n cellIdentityLte.getMnc :"+ cellIdentityLte.getMnc());
					sb.append("\n cellIdentityLte.getPci :"+ cellIdentityLte.getPci());
					sb.append("\n cellIdentityLte.getTac :"+ cellIdentityLte.getTac());
					
					sb.append("\n ---");
					
					CellSignalStrengthLte cellSignalStrengthLte = localc.getCellSignalStrength();
					
					sb.append("\n cellSignalStrengthLte.describeContents :"+ cellSignalStrengthLte.describeContents());
					sb.append("\n cellSignalStrengthLte.getAsuLevel :"+ cellSignalStrengthLte.getAsuLevel());
					sb.append("\n cellSignalStrengthLte.getDbm :"+ cellSignalStrengthLte.getDbm());
					sb.append("\n cellSignalStrengthLte.getLevel :"+ cellSignalStrengthLte.getLevel());
					sb.append("\n cellSignalStrengthLte.getTimingAdvance :"+ cellSignalStrengthLte.getTimingAdvance());
					
					sb.append("\n ----------");
					
				} else if (c instanceof CellInfoGsm) {
					
					CellInfoGsm localc = (CellInfoGsm) c;
					
					sb.append("\n ---");
					
					sb.append("\n localc.describeContents :"+ localc.describeContents());
					sb.append("\n localc.getTimeStamp :"+ localc.getTimeStamp());
					sb.append("\n localc.isRegistered :"+ localc.isRegistered());
					
					sb.append("\n ---");
					
					CellIdentityGsm cellIdentityGsm = localc.getCellIdentity();
					
					sb.append("\n cellIdentityGsm.describeContents :"+ cellIdentityGsm.describeContents());
					sb.append("\n cellIdentityGsm.getCid :"+ cellIdentityGsm.getCid());
					sb.append("\n cellIdentityGsm.getLac :"+ cellIdentityGsm.getLac());
					sb.append("\n cellIdentityGsm.getMcc :"+ cellIdentityGsm.getMcc());
					sb.append("\n cellIdentityGsm.getMnc :"+ cellIdentityGsm.getMnc());
					sb.append("\n cellIdentityGsm.getPsc :"+ cellIdentityGsm.getPsc());
					
					sb.append("\n ---");
					
					CellSignalStrengthGsm cellSignalStrengthGsm = localc.getCellSignalStrength();
					
					sb.append("\n cellSignalStrengthGsm.describeContents :"+ cellSignalStrengthGsm.describeContents());
					sb.append("\n cellSignalStrengthGsm.getAsuLevel :"+ cellSignalStrengthGsm.getAsuLevel());
					sb.append("\n cellSignalStrengthGsm.getDbm :"+ cellSignalStrengthGsm.getDbm());
					sb.append("\n cellSignalStrengthGsm.getLevel :"+ cellSignalStrengthGsm.getLevel());
					
					sb.append("\n ----------");
					
				} else {
					// TODO: CellInfoCdma and CellInfoWCdma
				}
				sb.append("\n ----------");
			}
			
			Logger.v(tag, method, LogType.Trace, "2");
			sb.append("\n ----- All neighboring cells information ----------");
			if (this.get_neighboring_cell_info() != null)
				for (NeighboringCellInfo nci :this.get_neighboring_cell_info()) {
					sb.append("\n ----------");
				
					sb.append("\n cellIdentityGsm.describeContents :"+ nci.describeContents());
					sb.append("\n cellIdentityGsm.getCid :"+ nci.getCid());
					sb.append("\n cellIdentityGsm.getLac :"+ nci.getLac());
					sb.append("\n cellIdentityGsm.getNetworkType :"+ nci.getNetworkType());
					sb.append("\n cellIdentityGsm.getPsc :"+ nci.getPsc());
					sb.append("\n cellIdentityGsm.getRssi :"+ nci.getRssi());
				
					sb.append("\n ----------");
				}
			
			Logger.v(tag, method, LogType.Trace, "3");
			sb.append("\n get_bit_error_rate :"+get_bit_error_rate());
			Logger.v(tag, method, LogType.Trace, "4");
			sb.append("\n get_current_network_type :"+get_current_network_type());
			Logger.v(tag, method, LogType.Trace, "5");
			sb.append("\n get_data_activity :"+get_data_activity());
			Logger.v(tag, method, LogType.Trace, "6");
			sb.append("\n get_data_state :"+get_data_state());
			Logger.v(tag, method, LogType.Trace, "7");
			sb.append("\n get_device_id :"+get_device_id());
			Logger.v(tag, method, LogType.Trace, "8");
			sb.append("\n get_device_software_version :"+get_device_software_version());
			Logger.v(tag, method, LogType.Trace, "9");
			sb.append("\n get_ecio :"+get_ecio());
			Logger.v(tag, method, LogType.Trace, "10");
			sb.append("\n get_line_number :"+get_line_number());
			Logger.v(tag, method, LogType.Trace, "11");
			sb.append("\n get_network_country_iso :"+get_network_country_iso());
			Logger.v(tag, method, LogType.Trace, "12");
			sb.append("\n get_network_operator :"+get_network_operator());
			Logger.v(tag, method, LogType.Trace, "13");
			sb.append("\n get_network_operator_name :"+get_network_operator_name());
			Logger.v(tag, method, LogType.Trace, "14");
			sb.append("\n get_phone_call_state :"+get_phone_call_state());
			Logger.v(tag, method, LogType.Trace, "15");
			sb.append("\n get_phone_type :"+get_phone_type());
			Logger.v(tag, method, LogType.Trace, "16");
			sb.append("\n get_rssi :"+get_rssi());
			Logger.v(tag, method, LogType.Trace, "17");
			sb.append("\n get_signal_noise_ratio :"+get_signal_noise_ratio());
			Logger.v(tag, method, LogType.Trace, "18");
			sb.append("\n get_signal_strength :"+get_signal_strength());
			Logger.v(tag, method, LogType.Trace, "19");
			sb.append("\n get_sim_country_iso :"+get_sim_country_iso());
			Logger.v(tag, method, LogType.Trace, "20");
			sb.append("\n get_sim_operator :"+get_sim_operator());
			Logger.v(tag, method, LogType.Trace, "21");
			sb.append("\n get_sim_operator_name :"+get_sim_operator_name());
			Logger.v(tag, method, LogType.Trace, "22");
			sb.append("\n get_sim_serial_number :"+get_sim_serial_number());
			Logger.v(tag, method, LogType.Trace, "23");
			sb.append("\n get_sim_state :"+get_sim_state());
			Logger.v(tag, method, LogType.Trace, "24");
			sb.append("\n get_subscriber_id :"+get_subscriber_id());
			Logger.v(tag, method, LogType.Trace, "25");
			sb.append("\n get_voice_mail_alpha_tag :"+get_voice_mail_alpha_tag());
			Logger.v(tag, method, LogType.Trace, "26");
			sb.append("\n get_voice_mail_number :"+get_voice_mail_number());
			Logger.v(tag, method, LogType.Trace, "27");
			
		} catch(Exception ex) {
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return sb.toString();
	}
}

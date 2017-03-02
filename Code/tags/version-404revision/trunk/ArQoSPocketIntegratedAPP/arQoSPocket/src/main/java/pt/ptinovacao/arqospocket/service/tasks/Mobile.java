package pt.ptinovacao.arqospocket.service.tasks;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.enums.EMobileNetworkMode;
import pt.ptinovacao.arqospocket.service.enums.EMobileState;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.tasks.interfaces.IMobileCallback;
import pt.ptinovacao.arqospocket.service.tasks.interfaces.IMobileONOFFAdapterTask;
import pt.ptinovacao.arqospocket.service.tasks.structs.MobileAdvancedInfoStruct;
import pt.ptinovacao.arqospocket.service.tasks.structs.MobileBasicInfoStruct;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import static android.telephony.TelephonyManager.CALL_STATE_IDLE;
import static android.telephony.TelephonyManager.CALL_STATE_OFFHOOK;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EDGE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_GPRS;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSDPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSPAP;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSUPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_LTE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_UMTS;
import static android.telephony.TelephonyManager.NETWORK_TYPE_UNKNOWN;
import static pt.ptinovacao.arqospocket.service.enums.EMobileNetworkMode.GPRS;


@SuppressLint("NewApi")
public class Mobile {

	private final static Logger logger = LoggerFactory.getLogger(Mobile.class);
	private final static String NETWORK_INFO_SEPARATOR = ",";
    // used to calculate the RSSI for most LTE networks,
    // but not all (though it's close enough without a good way to determine the band of LTE a device is using)
    private final static int RSSI_CONSTANT = 17;

	private Context context;
	private IMobileCallback iMobileCallback;
	
	private MyPhoneStateListener MyListener;
	private TelephonyManager telephonyManager;
	private EMobileNetworkMode mobileTechnology;
	private EMobileState mobileState;
	
	private int mobileSignalDBM = 0;
	private int mobileSignal = 0;
	
	private int id_celula = -1;
	private int cell_location = -1;
	private int psc = -1;
	private int pci = -1;
	private int ber = -1;
    private String lteRssi;
    private int lteRsrp;
    private String lteRsrq;
    private String lteRssnr;
    //is this sinr??
    private String lteCqi;

	private boolean hasSim;
	
	private List<IMobileONOFFAdapterTask> mobile_data_plan_change_listeners = null;
	

	public EMobileNetworkMode getMobileTechnology() {
		return mobileTechnology;
	}
	
	private int convertToDBM(int signalStrengthSystem) {
		final String method = "convertToDBM";
		
		try {
		
			int multDBM = 1;
			int subDBM = 116;
		
			switch(mobileTechnology) {
				case GPRS:
					multDBM = 2;
					subDBM = 113;
					break;
				case EDGE:
					multDBM = 2;
					subDBM = 113;
					break;
				default:
						break;
			}
			
			return (-subDBM + (multDBM * signalStrengthSystem));
					
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return -1;
		
	}
	
	public MobileBasicInfoStruct get_MobileBasicInfoStruct() {
		final String method = "get_MobileBasicInfoStruct";
		
		try {
			
			String signal_level = mobileSignalDBM+"";
			String id_cell = getCellID();
			String cell_location = getLocation();
			EMobileNetworkMode network_type = mobileTechnology;
			EMobileState mobile_state = mobileState;
			String mcc = telephonyManager.getNetworkCountryIso();
			String mcc_mnc = telephonyManager.getNetworkOperator();
			
			return new MobileBasicInfoStruct(convertToDBM(Integer.parseInt(signal_level))+"", id_cell, cell_location, network_type, mobile_state, mcc, mcc_mnc);		
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
	public MobileAdvancedInfoStruct get_MobileAdvancedInfoStruct() {
		final String method = "get_MobileAdvancedInfoStruct";
		
		try {
			
			String signal_level = mobileSignalDBM+"";
			String id_cell = getCellID();
			String cell_location = getLocation();
			EMobileNetworkMode network_type = mobileTechnology;
			EMobileState mobile_state = mobileState;
			String mcc = telephonyManager.getNetworkCountryIso();
			String mcc_mnc = telephonyManager.getNetworkOperator();
			
			String device_id = telephonyManager.getDeviceId();
			String msisdn = telephonyManager.getLine1Number(); // TODO: Nao está a fucnionar nem
			//String msisdn = "NA";
			String network_operator_name = telephonyManager.getNetworkOperatorName();
			String imsi = telephonyManager.getSubscriberId();
			boolean roaming = telephonyManager.isNetworkRoaming();
			
			List<NeighboringCellInfo> neighboring_cell_list = telephonyManager.getNeighboringCellInfo();
			
			List<CellInfo> all_cell_info_list = null;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
				all_cell_info_list = telephonyManager.getAllCellInfo();
			}
			
			return new MobileAdvancedInfoStruct(convertToDBM(Integer.parseInt(signal_level))+"", id_cell, cell_location, network_type,
					mobile_state, mcc, mcc_mnc, device_id, msisdn, network_operator_name, imsi, roaming, 
					neighboring_cell_list, all_cell_info_list);
		
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}

	public String getNetworkInfo(){
		if (getNetworkMode() == EMobileNetworkMode.LTE){
			return getLTERadioInfo();
		} else {
			return getRadioInfo();
		}

	}

	public String getRadioInfo(){
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(getLocation());
			sb.append("-");
			sb.append(getCellID());
			sb.append(NETWORK_INFO_SEPARATOR);
			sb.append(mobileSignalDBM);
			sb.append(NETWORK_INFO_SEPARATOR);
			//sb.append("gsm_band"); //TODO gsm_band
			sb.append(NETWORK_INFO_SEPARATOR);
			sb.append(getConnectionMode());
			sb.append(NETWORK_INFO_SEPARATOR);
			sb.append(telephonyManager.getNetworkOperatorName());
			sb.append(",,,,,,,,,,,,,,,,,,,,,,,,,,");
			//sb.append("rscp");
			sb.append(NETWORK_INFO_SEPARATOR);
			//sb.append("ecio");
			sb.append(NETWORK_INFO_SEPARATOR);
			//sb.append("arfcn");
			sb.append(NETWORK_INFO_SEPARATOR);
			//sb.append(psc);
			sb.append(NETWORK_INFO_SEPARATOR);
			//sb.append("bsic");
			sb.append(NETWORK_INFO_SEPARATOR);
			//sb.append("rac");
			sb.append(NETWORK_INFO_SEPARATOR);
			sb.append(ber);
			sb.append(NETWORK_INFO_SEPARATOR);
			sb.append(telephonyManager.getSubscriberId());
		} catch(Exception ex) {
			MyLogger.error(logger, "getRadioInfo", ex);
		}

		return sb.toString();

	}

	public String getLTERadioInfo(){
		StringBuilder sb = new StringBuilder();
		try {
			sb.append( ",,,,,,,,,");
			//sb.append("lte_reg_stat"); //TODO
			sb.append(NETWORK_INFO_SEPARATOR);
			//sb.append("lte_band");
			sb.append(NETWORK_INFO_SEPARATOR);
			//sb.append("lte_bw");
			sb.append(NETWORK_INFO_SEPARATOR);
			sb.append(telephonyManager.getNetworkOperatorName());
			sb.append(NETWORK_INFO_SEPARATOR);
			//sb.append("lte_txch");
			sb.append(NETWORK_INFO_SEPARATOR);
			//sb.append("lte_rxch");
			sb.append(NETWORK_INFO_SEPARATOR);
			sb.append(calculateLteRssi());
			sb.append(NETWORK_INFO_SEPARATOR);
			sb.append(mobileSignalDBM);
			sb.append(NETWORK_INFO_SEPARATOR);
			sb.append(lteRsrq);
			sb.append(NETWORK_INFO_SEPARATOR);
			//sb.append(lteCqi);
			sb.append(NETWORK_INFO_SEPARATOR);
			sb.append(getLocation());
			sb.append("-");
			sb.append(getCellID());
			sb.append(NETWORK_INFO_SEPARATOR);
			sb.append(telephonyManager.getSubscriberId());
		} catch(Exception ex) {
			MyLogger.error(logger, "getRadioInfo", ex);
		}

		return sb.toString();

	}

    public String calculateLteRssi(){
        lteRsrp = mobileSignalDBM;
        if ("".equals(lteRsrp) && !"".equals(lteRsrq)){
            return (RSSI_CONSTANT + lteRsrp - Integer.parseInt(lteRsrq))  + "";
        }
        return "";
    }


	//public void setMobileTechnology(EMobileTechnology mobileTechnology) {
	//	this.mobileTechnology = mobileTechnology;
	//}

	/*
	public EMobileState getMobileState() {
		return mobileState;
	}

	public void setMobileState(EMobileState mobileState) {
		this.mobileState = mobileState;
	}
	*/
	
	public String getLocation() {
		final String method = "getLocation";
		
		try {
			
			if (cell_location != -1)
				return cell_location+"";
		
			CellLocation cellLocation = telephonyManager.getCellLocation();
		
			if (cellLocation != null) {
				
				if (cellLocation instanceof CdmaCellLocation) {
				
					return ((CdmaCellLocation)cellLocation).getBaseStationId()+"";
				
				} else if (cellLocation instanceof GsmCellLocation) {
				
					return ((GsmCellLocation)cellLocation).getLac()+"";
				}
			}
			else { // check LTE location
				
				
			}
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return "-1";
	}
	
	public String getCellID() {
		final String method = "getCellID";
		
		try {
			
			if (id_celula != -1)
				return id_celula+"";
			
			CellLocation cellLocation = telephonyManager.getCellLocation();
			
			if (cellLocation != null) {
				
				if (cellLocation instanceof CdmaCellLocation) {
				
					return ((CdmaCellLocation)cellLocation).getBaseStationId()+"";
				
				} else if (cellLocation instanceof GsmCellLocation) {
				
					return ((GsmCellLocation)cellLocation).getCid()+"";
				}
			}
			else { // check LTE location
				
				
			}
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return "-1";
	}

	public int getMobileSignal() {
		return mobileSignal;
	}

	public void setMobileSignal(int mobileSignal) {
		this.mobileSignal = mobileSignal;
	}
	
	public boolean isSimReady() {
		final String method = "isSimReady";
		
		try {
			return telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}

	public boolean isMobileAvailable() {
		final String method = "isMobileAvailable";
		
		try {

			if (isSimReady() == false)
				return false;
			else {
//				Log.i("CODE", "SimOperatorName: " + telephonyManager.getSimOperatorName());
//				Log.i("CODE", "SimOperator: " + telephonyManager.getSimOperator());
				Log.i("CODE", "NetworkOperator: " + telephonyManager.getNetworkOperator());
				if (!telephonyManager.getNetworkOperator().equals(""))
					return true;
			}

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}

	public boolean setAirPlaneMode(Boolean on) {
		final String method = "setAirPlaneMode";
		
		try {
		
			if (on) {
				
				Settings.System.putInt(context.getContentResolver(),Settings.System.AIRPLANE_MODE_ON, 1);  
				
				Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
				intent.putExtra("state", true);//true; indicate that state of Airplane mode is ON.
				context.sendBroadcast(intent);
				
			} else {
				
				Settings.System.putInt(context.getContentResolver(),Settings.System.AIRPLANE_MODE_ON, 0);
				
				Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
				intent.putExtra("state", false);//false; indicate that state of Airplane mode is OFF.
				context.sendBroadcast(intent);
				
			}
		
			return true;
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}
	

	@SuppressLint("InlinedApi")
	public Mobile(Context context, IMobileCallback iMobileCallback) {
		final String method = "Mobile";
		
		try {
		
			this.context = context;
			this.iMobileCallback = iMobileCallback;

			telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		
			//connectedCellInfo = new MobileInfoStruct(-1,-1,-1,-1);
			mobile_data_plan_change_listeners = new ArrayList<IMobileONOFFAdapterTask>();

			int simState = telephonyManager.getSimState();

			if (simState == TelephonyManager.SIM_STATE_READY) {
				hasSim = true;
				mobileTechnology = this.getNetworkMode();
				mobileState = (this.checkDataEnabled() ? EMobileState.CONNECTED : EMobileState.DISCONNECTED);
			

				/* start listeners */
				MyListener = new MyPhoneStateListener();
			
				MyLogger.debug(logger, method, "Build.VERSION.SDK_INT :" + Build.VERSION.SDK_INT);

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
					telephonyManager
						.listen(this.MyListener,
								PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
										| PhoneStateListener.LISTEN_DATA_CONNECTION_STATE
										| PhoneStateListener.LISTEN_CELL_INFO);
				} else {
					telephonyManager
						.listen(this.MyListener,
								PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
										| PhoneStateListener.LISTEN_DATA_CONNECTION_STATE
										| PhoneStateListener.LISTEN_CELL_LOCATION);

				}
			}
		
			else {
				mobileTechnology = EMobileNetworkMode.NONE;
				mobileState = EMobileState.DISCONNECTED;
				hasSim = false;
			}
		
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	public boolean checkDataEnabled() {
		final String method = "checkDataEnabled";
		
		try {
			
			ConnectivityManager localConnectivityManager = (ConnectivityManager) context
					.getSystemService("connectivity");
			
			Method localMethod = Class.forName(
					localConnectivityManager.getClass().getName())
					.getDeclaredMethod("getMobileDataEnabled", new Class[0]);
			localMethod.setAccessible(true);
			boolean bool = ((Boolean) localMethod.invoke(
					localConnectivityManager, new Object[0])).booleanValue();
			return bool;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		return false;
	}
	
	public boolean remove_data_plan_change_listener(IMobileONOFFAdapterTask iMobileONOFFAdapterTask) {
		final String method = "remove_data_plan_change_listener";
		
		try {
			
			return mobile_data_plan_change_listeners.remove(iMobileONOFFAdapterTask);
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" }) 
	public boolean connectDataPlan(IMobileONOFFAdapterTask listener) { 
		final String method = "connectDataPlan";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			ConnectivityManager localConnectivityManager = (ConnectivityManager) context
					.getSystemService("connectivity");
			localConnectivityManager.setNetworkPreference(1);
			
			Field localField = Class.forName(
					localConnectivityManager.getClass().getName())
					.getDeclaredField("mService");
			
			localField.setAccessible(true);
			Object localObject = localField.get(localConnectivityManager);
			Class localClass = Class.forName(localObject.getClass().getName());
			Class[] arrayOfClass = new Class[1];
			arrayOfClass[0] = Boolean.TYPE;
			Method localMethod = localClass.getDeclaredMethod(
					"setMobileDataEnabled", arrayOfClass);
			localMethod.setAccessible(true);
			Object[] arrayOfObject = new Object[1];
			arrayOfObject[0] = Boolean.valueOf(true);
			localMethod.invoke(localObject, arrayOfObject);
			
			mobile_data_plan_change_listeners.add(listener);

			MyLogger.trace(logger, method, "Out - true");
			return true;
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}

		MyLogger.trace(logger, method, "Out - false");
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean disconnectDataPlan(IMobileONOFFAdapterTask listener) {
		final String method = "disconnectDataPlan";
		
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			ConnectivityManager localConnectivityManager = (ConnectivityManager) context
					.getSystemService("connectivity");
			
			Field localField = Class.forName(
					localConnectivityManager.getClass().getName())
					.getDeclaredField("mService");
			localField.setAccessible(true);
			Object localObject = localField.get(localConnectivityManager);
			Class localClass = Class.forName(localObject.getClass().getName());
			Class[] arrayOfClass = new Class[1];
			arrayOfClass[0] = Boolean.TYPE;
			Method localMethod = localClass.getDeclaredMethod(
					"setMobileDataEnabled", arrayOfClass);
			localMethod.setAccessible(true);
			Object[] arrayOfObject = new Object[1];
			arrayOfObject[0] = Boolean.valueOf(false);
			localMethod.invoke(localObject, arrayOfObject);
			
			mobile_data_plan_change_listeners.add(listener);
			
			MyLogger.trace(logger, method, "out - true");
			return true;
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		MyLogger.trace(logger, method, "out - false");
		return false;
	}
	
	
	private EMobileNetworkMode mapping_int_to_EMobileNetworkMode(int networkType) {
		final String method = "EMobileNetworkMode";

		EMobileNetworkMode returnMap = EMobileNetworkMode.NONE;
		
		try {

			switch (networkType) {

			case TelephonyManager.NETWORK_TYPE_LTE:
				returnMap = EMobileNetworkMode.LTE;
				break;

			case TelephonyManager.NETWORK_TYPE_CDMA:
				returnMap = EMobileNetworkMode.UMTS;
				break;

			case TelephonyManager.NETWORK_TYPE_EDGE:
				returnMap = EMobileNetworkMode.EDGE;
				break;

			case TelephonyManager.NETWORK_TYPE_UMTS:
				returnMap = EMobileNetworkMode.UMTS;
				break;

			case TelephonyManager.NETWORK_TYPE_HSPA:
				returnMap = EMobileNetworkMode.HSPA;
				break;

			case TelephonyManager.NETWORK_TYPE_HSDPA:
				returnMap = EMobileNetworkMode.HSPA;
				break;

			case TelephonyManager.NETWORK_TYPE_HSPAP:
				returnMap = EMobileNetworkMode.HSPA;
				break;

			case TelephonyManager.NETWORK_TYPE_HSUPA:
				returnMap = EMobileNetworkMode.HSPA;
				break;

			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				returnMap = EMobileNetworkMode.NONE;
				break;

			case NETWORK_TYPE_GPRS:
				returnMap = GPRS;
				break;

			default:
				returnMap = EMobileNetworkMode.NONE;
		}
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);;
		}
		
		return returnMap;
	}
  
  
	public EMobileNetworkMode getNetworkMode() {
		final String method = "getNetworkMode";

		EMobileNetworkMode returnMap = EMobileNetworkMode.NONE;
		
		try {
		
			returnMap = mapping_int_to_EMobileNetworkMode(telephonyManager.getNetworkType());
		
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
			
		return returnMap;
	}

	public int getConnectionMode(){
		int networkType = telephonyManager.getNetworkType();
		switch (networkType){
			case NETWORK_TYPE_GPRS:
				return 0;
			case NETWORK_TYPE_EDGE:
				return 1;
			case NETWORK_TYPE_UMTS:
				return 2;
			case NETWORK_TYPE_HSDPA:
				return 3;
			case NETWORK_TYPE_HSUPA:
				return 4;
			case NETWORK_TYPE_HSPA:
				return 5;
			case NETWORK_TYPE_UNKNOWN:
				return 7;
			case NETWORK_TYPE_HSPAP:
				return 8;
			case NETWORK_TYPE_LTE:
				return 10;
			default:
				return 6; //NONE

		}
	}

	
	public static boolean isOnline(final Context ctx) {
		final String method = "isOnline";
		
		try {
			
			ConnectivityManager conman = (ConnectivityManager) ctx
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			
			if (conman.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
					.isConnected()) {

				return true;
			} else {

				return false;
			}
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}
	
	private class MyPhoneStateListener extends PhoneStateListener {
		
		@Override
		public void onCellInfoChanged(List<CellInfo> cellInfo) {
			final String method = "MyPhoneStateListener :: onCellInfoChanged :: Cell Location (>= JellyBean) ::";

			try {
			
				if(cellInfo == null) {
					MyLogger.debug(logger, method, "CellLocation is null");
					//connectedCellInfo = new MobileInfoStruct(-1,-1,-1,-1);
					return;
				}
			
				for (int i = 0; i < cellInfo.size(); i++) {

					CellInfo ncell = cellInfo.get(i);

					if (ncell.isRegistered()) {

						if (ncell instanceof CellInfoGsm) {
							CellInfoGsm ncellGsm = (CellInfoGsm) ncell;
						
							mobileSignalDBM = ncellGsm.getCellSignalStrength().getDbm();
						
							id_celula = ncellGsm.getCellIdentity().getCid();
							cell_location = ncellGsm.getCellIdentity().getLac();
							psc = ncellGsm.getCellIdentity().getPsc();
							pci = -1;
						

						} else if (ncell instanceof CellInfoCdma) {
							CellInfoCdma ncellCdma = (CellInfoCdma) ncell;
						
							mobileSignalDBM = ncellCdma.getCellSignalStrength().getCdmaDbm();
						
							id_celula = ncellCdma.getCellIdentity().getNetworkId();
							cell_location = ncellCdma.getCellIdentity().getBasestationId();
							psc = -1;
							pci = -1;


						} else if (ncell instanceof CellInfoLte){
							CellInfoLte ncellLte = (CellInfoLte) ncell;
							mobileSignalDBM = ncellLte.getCellSignalStrength().getDbm();

							id_celula = ncellLte.getCellIdentity().getCi();
							cell_location = ncellLte.getCellIdentity().getTac();
							psc = -1;
							pci = ncellLte.getCellIdentity().getPci();

                            String ssignal = ncellLte.getCellSignalStrength().toString();

                            String[] parts = ssignal.split(" ");

                            lteRsrq =  parts[10];
                            lteRssnr = parts[11];
                            //is this sinr??
                            lteCqi =  parts[12];


						} //else
							//connectedCellInfo = new MobileInfoStruct(-1,-1,-1,-1);

					
						iMobileCallback.mobile_information_change();
					
						//MyLogger.debug(logger, method, "Info: " + connectedCellInfo.toString());
						return;
					}
				}
			
			} catch(Exception ex) {
				MyLogger.error(logger, method, ex);
			}

		}
		
		@Override
		public void onCellLocationChanged(CellLocation cellLocation) {
			final String method = "onCellLocationChanged :: Cell Location (< JellyBean)";

			try {
				if (cellLocation instanceof GsmCellLocation) {
					GsmCellLocation ncellGsm = (GsmCellLocation) cellLocation;
					
					id_celula = ncellGsm.getCid();
					cell_location = ncellGsm.getLac();

				} else if (cellLocation instanceof CdmaCellLocation) {
					CdmaCellLocation ncellCdma = (CdmaCellLocation) cellLocation;
					
					id_celula = ncellCdma.getNetworkId();
					cell_location = ncellCdma.getBaseStationId();
				}

				iMobileCallback.mobile_information_change();
				
			} catch (Exception ex) {
				MyLogger.error(logger, method, ex);
			}
		}
		
		private void report_data_connection_satate_change(EMobileState eMobileState) {
			final String method = "report_data_connection_satate_change";
			
			try {
				
				MyLogger.trace(logger, method, "In");
				
				for (IMobileONOFFAdapterTask iMobileONOFFAdapterTask: mobile_data_plan_change_listeners ) {
					try {
						iMobileONOFFAdapterTask.DataPlanChange(eMobileState);
					} catch (Exception ex) {
						MyLogger.error(logger, method, ex);
					}
				} 
				
			} catch (Exception ex) {
				MyLogger.error(logger, method, ex);
			}
			
			MyLogger.trace(logger, method, "Out");
		}
		
		/* data connection state changed */
		public void onDataConnectionStateChanged(int state, int networkType) {
			final String method = "onDataConnectionStateChanged";

			try {
				
				MyLogger.trace(logger, method, "In");
			
				switch (state) {

					case TelephonyManager.DATA_DISCONNECTED:
						mobileState = EMobileState.DISCONNECTED;
						break;
	
					case TelephonyManager.DATA_CONNECTED:
						mobileState = EMobileState.CONNECTED;
						break;
	
					case TelephonyManager.DATA_CONNECTING:
						mobileState = EMobileState.CONNECTING;
						break;
	
					case TelephonyManager.DATA_SUSPENDED:
						mobileState = EMobileState.DISCONNECTED;
						break;
	
					default:
						mobileState = EMobileState.DISCONNECTED;
						break;
				}
			
				MyLogger.trace(logger, method, "report_data_connection_satate_change");
				report_data_connection_satate_change(mobileState);
				
				MyLogger.trace(logger, method, "report_data_connection_satate_change - end");

				mobileTechnology = mapping_int_to_EMobileNetworkMode(networkType);
			
				iMobileCallback.mobile_information_change();
				iMobileCallback.mobile_params_change();

				MyLogger.debug(logger, method, "Mobile Technology: " + mobileTechnology.toString() + " Mobile State: " + mobileState.toString());
			
			} catch (Exception ex) {
				MyLogger.error(logger, method, ex);
			}
		}

		/* signal strength changed */
		public void onSignalStrengthsChanged(SignalStrength paramSignalStrength) {
			final String method = "onSignalStrengthsChanged";
			
			try {
				
				MyLogger.debug(logger, method, "Mobile :: In");
				MyLogger.debug(logger, method, "Mobile :: getGsmSignalStrength :"+paramSignalStrength.getGsmSignalStrength());
				
			
				int signalStrenght = mobileSignal = paramSignalStrength.getGsmSignalStrength();				
				ber = paramSignalStrength.getGsmBitErrorRate();

				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2
						&& signalStrenght == 99) { 
					
					MyLogger.debug(logger, method, "1");

					//final CellSignalStrengthLte lte = ((CellInfoLte) telephonyManager.getAllCellInfo().get(0)).getCellSignalStrength();
					List<CellInfo> CellsList = telephonyManager.getAllCellInfo();
					
					if ((CellsList != null) && (CellsList.size()>0)) {
						
						// get CellInfo
						CellInfo connectedCell = CellsList.get(0);
						
						if (connectedCell instanceof CellInfoGsm) {
							
							MyLogger.debug(logger, method, "CellInfoGsm");
							MyLogger.debug(logger, method, "Mobile :: getAsuLevel :" + ((CellInfoGsm)connectedCell).getCellSignalStrength().getAsuLevel());
							MyLogger.debug(logger, method, "Mobile :: getDbm :" + ((CellInfoGsm)connectedCell).getCellSignalStrength().getDbm());
							MyLogger.debug(logger, method, "Mobile :: getLevel :" + ((CellInfoGsm)connectedCell).getCellSignalStrength().getLevel());
							
							mobileSignal = signalStrenght = ((CellInfoGsm)connectedCell).getCellSignalStrength().getAsuLevel();

							id_celula = ((CellInfoGsm)connectedCell).getCellIdentity().getCid();
							cell_location = ((CellInfoGsm)connectedCell).getCellIdentity().getLac();
							psc = ((CellInfoGsm)connectedCell).getCellIdentity().getPsc();
							
						} else if (connectedCell instanceof CellInfoLte) {
							
							MyLogger.debug(logger, method, "CellInfoLte");
							MyLogger.debug(logger, method, "Mobile :: getAsuLevel :" + ((CellInfoLte)connectedCell).getCellSignalStrength().getAsuLevel());
							MyLogger.debug(logger, method, "Mobile :: getDbm :" + ((CellInfoLte)connectedCell).getCellSignalStrength().getDbm());
							MyLogger.debug(logger, method, "Mobile :: getLevel :" + ((CellInfoLte)connectedCell).getCellSignalStrength().getLevel());
							
							mobileSignal = signalStrenght = ((CellInfoLte)connectedCell).getCellSignalStrength().getAsuLevel();
							
							id_celula = ((CellInfoLte)connectedCell).getCellIdentity().getCi();
							cell_location = ((CellInfoLte)connectedCell).getCellIdentity().getTac();
							pci = ((CellInfoLte)connectedCell).getCellIdentity().getPci();

                            //TODO zero values
                            String ssignal = ((CellInfoLte)connectedCell).getCellSignalStrength().toString();

                            String[] parts = ssignal.split(" ");

                            lteRsrq =  parts[10];
                            lteRssnr = parts[11];
                            //is this sinr??
                            lteCqi =  parts[12];
						
						} else if (connectedCell instanceof CellInfoWcdma) {
							
							MyLogger.debug(logger, method, "CellInfoWcdma");
							MyLogger.debug(logger, method, "Mobile :: getAsuLevel :" + ((CellInfoWcdma)connectedCell).getCellSignalStrength().getAsuLevel());
							MyLogger.debug(logger, method, "Mobile :: getDbm :" + ((CellInfoWcdma)connectedCell).getCellSignalStrength().getDbm());
							MyLogger.debug(logger, method, "Mobile :: getLevel :" + ((CellInfoWcdma)connectedCell).getCellSignalStrength().getLevel());
							
							mobileSignal = signalStrenght = ((CellInfoWcdma)connectedCell).getCellSignalStrength().getAsuLevel();
							
							id_celula = ((CellInfoWcdma)connectedCell).getCellIdentity().getCid();
							cell_location = ((CellInfoWcdma)connectedCell).getCellIdentity().getLac();
							psc = ((CellInfoWcdma)connectedCell).getCellIdentity().getPsc();
							
						} else if (connectedCell instanceof CellInfoCdma) {
							
							MyLogger.debug(logger, method, "CellInfoCdma");
							MyLogger.debug(logger, method, "Mobile :: getAsuLevel :" + ((CellInfoCdma)connectedCell).getCellSignalStrength().getAsuLevel());
							MyLogger.debug(logger, method, "Mobile :: getDbm :" + ((CellInfoCdma)connectedCell).getCellSignalStrength().getDbm());
							MyLogger.debug(logger, method, "Mobile :: getLevel :" + ((CellInfoCdma)connectedCell).getCellSignalStrength().getLevel());
							
							mobileSignal = signalStrenght = ((CellInfoWcdma)connectedCell).getCellSignalStrength().getAsuLevel();
							
							id_celula = ((CellInfoWcdma)connectedCell).getCellIdentity().getCid();
							cell_location = ((CellInfoWcdma)connectedCell).getCellIdentity().getLac();
							psc = ((CellInfoWcdma)connectedCell).getCellIdentity().getPsc();
							
						}
					}

				}
				
				MyLogger.debug(logger, method, "Mobile signalStrenght: " + signalStrenght);

				if (signalStrenght == 99) {
					return;
				}
				mobileSignalDBM = signalStrenght;
			
				int scaledSignal = 0;

				if ((signalStrenght <= 0) || (signalStrenght == 99))
					scaledSignal = 0;
				
				else if (signalStrenght >= 18)
					scaledSignal = 5;

				else if (signalStrenght >= 12)
					scaledSignal = 4;

				else if (signalStrenght >= 8)
					scaledSignal = 3;

				else if (signalStrenght >= 5)
					scaledSignal = 2;

				else if (signalStrenght >= 0)
					scaledSignal = 1;

				mobileSignal = scaledSignal;
			
				iMobileCallback.mobile_information_change();
				MyLogger.debug(logger, method, "Mobile Signal: " + mobileSignal);
			
			} catch (Exception ex) {
				MyLogger.error(logger, method, ex);
			}
		}

	}

	public String getSimIccid(){
		return telephonyManager.getSimSerialNumber();
	}
	public int getSimState(){
		return telephonyManager.getSimState();
	}

	public boolean isAnyCallActive(){
		return telephonyManager.getCallState() != CALL_STATE_IDLE;
	}

}
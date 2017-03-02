package pt.ptinovacao.arqospocket.fragments;

import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.enums.EMobileNetworkMode;
import pt.ptinovacao.arqospocket.service.enums.EMobileState;
import pt.ptinovacao.arqospocket.service.enums.ENetworkAction;
import pt.ptinovacao.arqospocket.service.interfaces.IService;
import pt.ptinovacao.arqospocket.service.interfaces.IUI;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.MyApplication;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.activities.ActivityDashboardMain;
import pt.ptinovacao.arqospocket.interfaces.IUpdateUI;
import pt.ptinovacao.arqospocket.util.GaugeType;
import pt.ptinovacao.arqospocket.util.Utils;
import pt.ptinovacao.arqospocket.views.GaugeView;
import pt.ptinovacao.arqospocket.views.GaugeView.NeedleDeflectListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentDashboard extends Fragment implements IUI, IUpdateUI {          

	private final static Logger logger = LoggerFactory  
			.getLogger(FragmentDashboard.class);
	private static final String TAG = "arqos";

	private FragmentDashboard myRef = null;
	private View myView = null;
	ActivityDashboardMain activity;

	ImageView btn, btn2;
	LinearLayout ll;
	FragmentActionBar actionBar;
	private int screenWidth, screenHeight;
	private int graphicSize = 0;

	private TextView mobile_network_state, wifi_state;
	private LinearLayout activity_dashboard_lower_layout1, activity_dashboard_lower_layout2;
	private LinearLayout activity_dashboard_mobile_network_info, activity_dashboard_wifi_info;
	private ImageView wifiDetail, mobileNetworkDetail;
	private ImageView icon_rede_movel, icon_wifi;

	TextView mobileBigValue, mobileSmallValue, mobileDescriptionValue,
			wifiBigValue, wifiSmallValue, wifiDescriptionValue, textcid,
			textlac, textchannel, textSpeed, tipRede, 
			//textsaid,
			textessid;
	
	private GaugeView gv, gv2;
	
	private MyApplication myApplicationRef;
	private IService iService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		activity = (ActivityDashboardMain) getActivity();
		
		myApplicationRef = (MyApplication) getActivity()
				.getApplicationContext();
		iService = myApplicationRef.getEngineServiceRef();
	}
	
	@Override
	public void onPause() {
		super.onPause();

		if (iService != null) {
			/* Wi-Fi */
			iService.remove_registry_update_wifi_information(this);
			iService.remove_registry_update_wifi_params(this);
			/* Mobile */
			iService.remove_registry_update_mobile_information(this);
			iService.remove_registry_update_mobile_params(this);
		}
		
		activity.remove_registry_dashboard_mobile_information(this);
	}	

	@Override
	public void onResume() {		
		super.onResume();
				
		if (iService != null) {
			/* Wi-Fi */
			iService.registry_update_wifi_information(this);
			iService.registry_update_wifi_params(this);
			iService.get_wifi_information(this);
			/* Mobile */
			iService.registry_update_mobile_information(this);
			iService.registry_update_mobile_params(this);
			iService.get_mobile_information(this);
		}
		
		activity.registry_update_dashboard_information(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		myView = inflater.inflate(R.layout.activity_dash, container, false);
		myRef = this;
				
		mobile_network_state = (TextView) myView.findViewById(R.id.network_state);
		
		wifi_state = (TextView) myView.findViewById(R.id.wi_fi_state);
		
		activity_dashboard_mobile_network_info = (LinearLayout) myView
				.findViewById(R.id.activity_dashboard_mobile_network_info);
		
		activity_dashboard_wifi_info = (LinearLayout) myView
				.findViewById(R.id.activity_dashboard_wifi_info);
		
		activity_dashboard_lower_layout1 = (LinearLayout) myView
				.findViewById(R.id.activity_dashboard_lower_layout1);
		
		activity_dashboard_lower_layout2 = (LinearLayout) myView
				.findViewById(R.id.activity_dashboard_lower_layout2);	
		
		mobileBigValue = (TextView) myView
				.findViewById(R.id.fragment_dashboard_mobile_network_big_value);
		mobileSmallValue = (TextView) myView
				.findViewById(R.id.fragment_dashboard_mobile_network_small_value);

		mobileDescriptionValue = (TextView) myView
				.findViewById(R.id.fragment_dashboard_mobile_network_description_value);

		wifiBigValue = (TextView) myView
				.findViewById(R.id.fragment_dashboard_wifi_big_value);
		wifiSmallValue = (TextView) myView
				.findViewById(R.id.fragment_dashboard_wifi_small_value);

		wifiDescriptionValue = (TextView) myView
				.findViewById(R.id.fragment_dashboard_wifi_network_description_value);

		textcid = (TextView) myView.findViewById(R.id.cid);
		textlac = (TextView) myView.findViewById(R.id.lac);
		textchannel = (TextView) myView.findViewById(R.id.channel);
		textSpeed = (TextView) myView.findViewById(R.id.LinkSpeed);
		tipRede = (TextView) myView.findViewById(R.id.TipRede);
		//textsaid = (TextView) myView.findViewById(R.id.said);
		textessid = (TextView) myView.findViewById(R.id.essid);
		btn = (ImageView) myView.findViewById(R.id.btn1);
		btn2 = (ImageView) myView.findViewById(R.id.btn2);
		
		mobileNetworkDetail = (ImageView) myView
				.findViewById(R.id.activity_dashboard_mobile_network_detail);
		
		wifiDetail = (ImageView) myView
				.findViewById(R.id.activity_dashboard_wifi_detail);
		
		icon_rede_movel = (ImageView) myView
				.findViewById(R.id.icon_rede_movel);     
		
		icon_wifi = (ImageView) myView
				.findViewById(R.id.icon_wi_fi);         
			
		updateUI();
		
		// regista para receber os eventos do wifi e da rede movel
		myApplicationRef = (MyApplication) getActivity()
				.getApplicationContext();
		iService = myApplicationRef.getEngineServiceRef();

//		if (iService != null) {
//			iService.registry_update_wifi_information(this);
//			iService.registry_update_wifi_params(this);
//			iService.get_wifi_information(this);
//
//			iService.registry_update_mobile_information(this);
//			iService.registry_update_mobile_params(this);
//			iService.get_mobile_information(this);
//		}
		
		activity.registry_update_dashboard_information(this);

		return myView;
	}
	
	@Override
	public void updateUI() {
		/*  */
		DisplayMetrics displayMetrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		screenWidth = displayMetrics.widthPixels;
		screenHeight = displayMetrics.heightPixels;

		graphicSize = (screenWidth / 2);

		ll = (LinearLayout) myView.findViewById(R.id.gaugeNeedleLay);
		
		if (!activity.is3gOn()) {
			/** TODO
			 * Show faded gauge */
			
			/* Show unavailability icon */
			icon_rede_movel.setImageResource(R.drawable.rede_movel_indisponivel);
			
			/* Hide info params */
			activity_dashboard_mobile_network_info.setVisibility(View.GONE);
			mobileNetworkDetail.setVisibility(View.GONE);
			btn.setVisibility(View.GONE);
			activity_dashboard_lower_layout1.setVisibility(View.GONE);
						
			/* Show unavailability text */
			mobile_network_state.setVisibility(View.VISIBLE);
			mobile_network_state.setText(R.string.unavailable);
			
			if(gv == null)
				gv = (GaugeView) myView.findViewById(R.id.gv1);
			gv.setVisibility(View.VISIBLE);
			gv.setType(GaugeType.Mobile_disabled);
			gv.setDiameter(graphicSize);
			gv.createNeedle(graphicSize, graphicSize, 0, -graphicSize / 2);
			gv.setNeedleAnimation(true);
			gv.setCurrentValue(0);
			
//			/* Remove listener, if registered */
//			if (iService != null) {
//				iService.remove_registry_update_mobile_information(this);
//				iService.remove_registry_update_mobile_params(this);
//			}
		} else {
			/* Show availability icon */
			icon_rede_movel.setImageResource(R.drawable.icon_rede_movel);
			
			/* Show info params */
			activity_dashboard_mobile_network_info.setVisibility(View.VISIBLE);
			mobileNetworkDetail.setVisibility(View.VISIBLE);
			btn.setVisibility(View.VISIBLE);
			activity_dashboard_lower_layout1.setVisibility(View.VISIBLE);
			
			mobileNetworkDetail.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					activity.goToDashboardDetailsPage(Utils.NETWORK_DASH_ID);
				}
			});
			
			/* Hide unavailability text */
			mobile_network_state.setVisibility(View.INVISIBLE);
			
			// btn = (ImageView) myView.findViewById(R.id.btn1);
			if(gv == null)
				gv = (GaugeView) myView.findViewById(R.id.gv1);
			gv.setVisibility(View.VISIBLE);
			// gv.setType(GaugeType.Unknown);
			gv.setDiameter(graphicSize);
			gv.createNeedle(graphicSize, graphicSize, 0, -graphicSize / 2);
			gv.setNeedleAnimation(true);
			gv.setAnimationTime(250, 250);

			gv.setOnNeedleDeflectListener(new NeedleDeflectListener() {

				@Override
				public void onDeflect(float angle, float value) {
					Log.d(TAG, "VALUE: " + value + "   ANGLE: " + angle);

					mobileBigValue.setText(Integer.toString((int) value));
					// mobileBigValue.setText(Float.valueOf(value).toString());
					int color = getColor(gv.getType(), angle);
					mobileBigValue.setTextColor(color);
					mobileSmallValue.setTextColor(color);
					mobileDescriptionValue.setTextColor(color);

					Float small = (value % 1);
					if (small != 0) {
						mobileSmallValue.setText(".0");
					} else
						mobileSmallValue.setText(".0");
				}
			});
		}
		
		if (!activity.isWifiOn()) {
			/** TODO
			 * Show faded gauge */
			
			/* Show unavailability icon */
			icon_wifi.setImageResource(R.drawable.wi_fi_indisponivel);
			
			/* Hide info params */
			activity_dashboard_wifi_info.setVisibility(View.GONE);
			wifiDetail.setVisibility(View.GONE);
			btn2.setVisibility(View.GONE);
			activity_dashboard_lower_layout2.setVisibility(View.GONE);
			textSpeed.setVisibility(View.GONE);
			//textsaid.setVisibility(View.GONE);
			
			/* Show unavailability text */
			wifi_state.setVisibility(View.VISIBLE);
			wifi_state.setText(R.string.unavailable);
			
			if(gv2 == null)
				gv2 = (GaugeView) myView.findViewById(R.id.gv2);
			gv2.setVisibility(View.VISIBLE);
			gv2.setType(GaugeType.Wifi_disabled);
			gv2.setDiameter(graphicSize);
			gv2.createNeedle(graphicSize, graphicSize, 0, -graphicSize / 2);
			gv2.setNeedleAnimation(true);
			gv2.setCurrentValue(90); // zero
			
//			/* Remove listener, if registered */
//			if (iService != null) {
//				iService.remove_registry_update_wifi_information(this);
//				iService.remove_registry_update_wifi_params(this);
//			}
		} else {
			/* Show availability icon */
			icon_wifi.setImageResource(R.drawable.icon_wi_fi);
			
			/* Show info params */
			activity_dashboard_wifi_info.setVisibility(View.VISIBLE);
			wifiDetail.setVisibility(View.VISIBLE);
			btn2.setVisibility(View.VISIBLE);
			activity_dashboard_lower_layout2.setVisibility(View.VISIBLE);
			textSpeed.setVisibility(View.VISIBLE);
			//textsaid.setVisibility(View.VISIBLE);
			
			wifiDetail.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					activity.goToDashboardDetailsPage(Utils.WIFI_DASH_ID);
				}
			});
			
			/* Hide unavailability text */
			wifi_state.setVisibility(View.INVISIBLE);
			
			if(gv2 == null)
				gv2 = (GaugeView) myView.findViewById(R.id.gv2);
			gv2.setVisibility(View.VISIBLE);
			gv2.setDiameter(graphicSize);
			gv2.setType(GaugeType.WifiSignalLevel);
			gv2.createNeedle(graphicSize, graphicSize, 0, -graphicSize / 2);
			gv2.setAngleRange(0, 225);
			gv2.setNeedleAnimation(true);
			gv2.setAnimationTime(500, 500);

			gv2.setOnNeedleDeflectListener(new NeedleDeflectListener() {

				@Override
				public void onDeflect(float angle, float value) {
					final String method = "onDeflect";

					MyLogger.debug(logger, method, "angle :" + angle + " value :"
							+ value);

					wifiBigValue.setText(Integer.toString((int) value));
					// mobileBigValue.setText(Float.valueOf(value).toString());
					int color = getColor(gv2.getType(), angle);
					wifiBigValue.setTextColor(color);
					wifiSmallValue.setTextColor(color);
					wifiDescriptionValue.setTextColor(color);

					Float small = (value % 1);
					if (small != 0) {
						wifiSmallValue.setText(".0");
					} else
						wifiSmallValue.setText(".0");
				}
			});
			
			if(activity.hasMobileParams())
				activity.getCurrentMobileParams(this);
		}
	}

	@SuppressWarnings("ResourceType")
	public int getColor(GaugeType type, float angle) {

		TypedArray gaugeColors = getResources().obtainTypedArray(R.array.gauge_colors);

		if (type == GaugeType.MobileNetwork3G
				|| type == GaugeType.MobileNetwork4G
				|| type == GaugeType.WifiSignalLevel) {

			if (angle >= 0 && angle <= 45)
				return gaugeColors.getColor(0,0);
			if (angle > 45 && angle <= 90)
				return gaugeColors.getColor(1,0);
			if (angle > 90 && angle <= 135)
				return gaugeColors.getColor(2,0);
			if (angle > 135 && angle <= 180)
				return gaugeColors.getColor(3,0);
			if (angle > 180 && angle <= 225)
				return gaugeColors.getColor(4,0);
			if (angle > 180 && angle <= 225)
				return gaugeColors.getColor(5,0);

		}  else if (type == GaugeType.MobileNetwork2G) {

			if (angle >= 0 && angle <= 45)
				return gaugeColors.getColor(1,0);
			if (angle >= 45 && angle <= 90)
				return gaugeColors.getColor(2,0);
			if (angle > 90 && angle <= 135)
				return gaugeColors.getColor(3,0);
			if (angle > 135 && angle <= 180)
				return gaugeColors.getColor(4,0);

		}

		return gaugeColors.getColor(0,0);

	}

	public void update_wifi_information(String link_speed, String ssid,
			String rx_level, String channel) {
		final String method = "update_wifi_information";

		try {
//			Log.i("WIFI_UPDATE", "ssid: " + ssid); 
			if(!activity.isWifiOn() && ssid != null) {
				activity.setWifiOn(true);
				updateUI();
			}

			MyLogger.debug(logger, method, "link_speed :" + link_speed
					+ " ssid :" + ssid + " rx_level :" + rx_level
					+ " channel :" + channel);

			if(gv2 == null)
				gv2 = (GaugeView) myView.findViewById(R.id.gv2);
			/* If previous value != current value */
			if(gv2.getCurrentValue() != Float.parseFloat(rx_level)) {
				gv2.setCurrentValue(Float.parseFloat(rx_level));
//				Log.i("WIFI_UPDATE", "rx_level: " + rx_level);
			}

			if (ssid == null) {
				//textsaid.setText(R.string.na);
				textessid.setText(R.string.na);
//				Apagar NA
//				textessid.setVisibility(View.INVISIBLE);
//				textsaid.setVisibility(View.INVISIBLE);
//				btn2.setVisibility(View.INVISIBLE);
				
			} else {
				/* Exclude quotation marks (").*/
				//textsaid.setText(ssid.replace("\"", ""));
				textessid.setText(ssid.replace("\"", ""));
			}
			get_wifi_operator_branding();

			textchannel.setText(channel);

			if (Integer.parseInt(link_speed) <= 0) {
				textSpeed.setText(R.string.na);
//				apagar speed
//				textSpeed.setVisibility(View.INVISIBLE);
			} else {
				textSpeed.setText(link_speed + getString(R.string.mbps));
			}
			get_wifi_operator_branding();
			
			activity.setWifiInformation(link_speed, ssid, rx_level, channel);

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	public void update_mobile_information(EMobileState mobile_state,
			EMobileNetworkMode network_mode, String operator_code,
			String rx_level, String cid, String lac) {
		final String method = "update_mobile_information";

//		if (!activity.is3gOn()) {
//			activity.set3gOn(true);
//			updateUI();
//		}
		
//		if (activity.is3gOn() && network_mode.equals(EMobileNetworkMode.NONE)) {
//			activity.set3gOn(false);
//			updateUI();
//			return;
//		} else 
			if(!activity.is3gOn() && !network_mode.equals(EMobileNetworkMode.NONE)) {
			activity.set3gOn(true);
			updateUI();
		}
		
//		Log.i("CODE", (network_mode != null ? "network_mode: " + network_mode.name() : "NULL"));
//		Log.i("CODE", (mobile_state != null ? "mobile_state: " + mobile_state.name() : "NULL"));
//		Log.i("CODE", (operator_code != null ? "operator_code: " + operator_code : "NULL"));
		
		if(!activity.is3gOn())
			return;
		
		try {

			MyLogger.debug(logger, method, "mobile_state :" + mobile_state
					+ " network_mode :" + network_mode + " operator_code :"
					+ operator_code + " rx_level :" + rx_level + " cid :" + cid
					+ " lac :" + lac);

			final GaugeView gv = (GaugeView) myView.findViewById(R.id.gv1);

			switch (network_mode) {
			case GPRS:
				gv.setType(GaugeType.MobileNetwork2G);
				tipRede.setText(R.string.network_type_2g);
				break;
			case EDGE:
				gv.setType(GaugeType.MobileNetwork2G);
				tipRede.setText(R.string.network_type_2g);
				break;
			case UMTS:
				gv.setType(GaugeType.MobileNetwork3G);
				tipRede.setText(R.string.network_type_3g);
				break;
			case HSPA:
				gv.setType(GaugeType.MobileNetwork3G);
				tipRede.setText(R.string.network_type_3g);
				break;
			case LTE:
				gv.setType(GaugeType.MobileNetwork4G);
				tipRede.setText(R.string.network_type_4g);
				break;
			case NONE:
				gv.setType(GaugeType.Unknown);
				tipRede.setText("");
				break;
			}
			get_mobile_operator_branding();

			if (Integer.parseInt(lac) <= 0) {
				textlac.setText(R.string.na);
			} else {
				textlac.setText(lac);
			}

			if (Integer.parseInt(cid) <= 0) {
				textcid.setText(R.string.na);
			} else {
				textcid.setText(cid);
			}

			// setOperator(operator_code);
			gv.setCurrentValue(Float.parseFloat(rx_level));
			
			activity.setMobileInformation(mobile_state, network_mode, operator_code, rx_level, cid, lac);

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	// //ver se o operador mudou
	// public void setOperator(String operator_code)
	// {
	// if(operator_code.equals("26806"))
	// {
	// get_mobile_operator_branding();
	//
	//
	// }
	// else {
	//
	// MyLogger.debug(logger, "O operador mudou", null);
	//
	// }
	// }

	@Override
	public void update_mobile_params(TreeMap<String, String> keyValueParams) {
		activity.setMobileParams(keyValueParams);
	}

	@Override
	public void update_wifi_params(TreeMap<String, String> keyValueParams) {
		activity.setWifiParams(keyValueParams);
	}


	public void send_pending_tests_ack(ENetworkAction action_state) {

	}

	public void send_report_ack(ENetworkAction action_state) {

	}

	public Pair<Bitmap, String> get_mobile_operator_branding() {

		btn.setImageResource(R.drawable.logo_meo);

		return null;
	}
	
	private Pair<Bitmap, String> get_wifi_operator_branding() {
		
		return null;
	}

	public static Fragment newInstance() {
		return new FragmentDashboard();
	}

	@Override
	public void update_test_info() {

	}

	@Override
	public void update_test_task(String test_id) {

	}

}

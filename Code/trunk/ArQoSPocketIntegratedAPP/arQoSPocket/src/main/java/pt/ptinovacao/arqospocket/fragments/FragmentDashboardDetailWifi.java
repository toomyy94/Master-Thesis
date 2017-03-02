package pt.ptinovacao.arqospocket.fragments;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.adapters.AdapterDashboardDetail;
import pt.ptinovacao.arqospocket.service.enums.EMobileNetworkMode;
import pt.ptinovacao.arqospocket.service.enums.EMobileState;
import pt.ptinovacao.arqospocket.service.enums.ENetworkAction;
import pt.ptinovacao.arqospocket.service.interfaces.IService;
import pt.ptinovacao.arqospocket.service.interfaces.IUI;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.DetailItem;
import pt.ptinovacao.arqospocket.MyApplication;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.activities.ActivityDashboardMain;
import pt.ptinovacao.arqospocket.util.GaugeType;
import pt.ptinovacao.arqospocket.views.GaugeView;
import pt.ptinovacao.arqospocket.views.GaugeView.NeedleDeflectListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentDashboardDetailWifi extends Fragment implements IUI {
	private final static Logger logger = LoggerFactory
			.getLogger(FragmentDashboardDetailWifi.class);

	private FragmentDashboardDetailWifi myRef = null;
	private View v = null;
	ImageView wifiRestart, wifiDetail;
	LinearLayout ll;
	int screenWidth, screenHeight;
	ListView details;
	ArrayList<DetailItem> testList;
	AdapterDashboardDetail adapter;
	ActivityDashboardMain activity;

	TextView wifiBigValueDetail, wifiSmallValueDetail, textSpeed, wifiDescriptionValueDetail;
	
	private MyApplication myApplicationRef;
	private IService iService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		activity = (ActivityDashboardMain) getActivity();
		
		// Regista para receber os eventos do wifi
		myApplicationRef = (MyApplication) getActivity()
				.getApplicationContext();
		iService = myApplicationRef.getEngineServiceRef();
	}
		
	@Override
	public void onPause() {
		super.onPause();

		if (iService != null) {
			iService.remove_registry_update_wifi_information(this);
			iService.remove_registry_update_wifi_params(this);
		}
	}
	
	@Override
	public void onResume() {		
		super.onResume();
				
		if (iService != null) {
			iService.registry_update_wifi_information(this);
			iService.registry_update_wifi_params(this);
			iService.get_wifi_params(this);
		}
		
		getCurrentValues();
	}
	
	public void getCurrentValues() {
		if(activity.hasWifiInfo())
			activity.getCurrentWifiInfo(this);
		
		if(activity.hasWifiParams())
			activity.getCurrentWifiParams(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		v = inflater.inflate(R.layout.fragment_dash_detail_wifi, container,
				false);
		myRef = this;
		textSpeed = (TextView) v.findViewById(R.id.speed);
		wifiBigValueDetail = (TextView) v.findViewById(R.id.big_value);
		wifiSmallValueDetail = (TextView) v.findViewById(R.id.small_value);

		wifiDescriptionValueDetail = (TextView) v
				.findViewById(R.id.description_value);
		
		/* Button to restart wi-fi interface */
		wifiRestart = (ImageView) v.findViewById(R.id.fragment_dashboard_detail_wifi_restart);
		wifiRestart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(iService != null) {
					/* Restart wi-fi interface */
					iService.restart_wifi_interface();
					/* Return to main Dashboard page */
					activity.goToDashboardMainPage();
				}
			}
		});

		DisplayMetrics displayMetrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		screenWidth = displayMetrics.widthPixels;
		screenHeight = displayMetrics.heightPixels;

		int graphicSize = (screenWidth / 3);

		ll = (LinearLayout) v.findViewById(R.id.gaugeNeedleLay);
		
		details = (ListView) v.findViewById(R.id.fragment_dashboard_detail_wifi_listview);
		
		final GaugeView gvWiFi = (GaugeView) v
				.findViewById(R.id.fragment_dashboard_detail_wifi_gaugeview);
		
		gvWiFi.setDiameter(graphicSize);
		gvWiFi.setType(GaugeType.WifiSignalLevel);
		gvWiFi.createNeedle(graphicSize, graphicSize, 0, -graphicSize / 2);
//		gv2.setAngleRange(0, 225);
		gvWiFi.setNeedleAnimation(true);
		gvWiFi.setAnimationTime(500, 500);

		gvWiFi.setOnNeedleDeflectListener(new NeedleDeflectListener() {		
			@Override
			public void onDeflect(float angle, float value) {
				final String method = "onDeflect";

				wifiBigValueDetail.setText(Integer.toString((int) value));
				// mobileBigValue.setText(Float.valueOf(value).toString());
				int color = getColor(gvWiFi.getType(), angle);
				wifiBigValueDetail.setTextColor(color);
				wifiSmallValueDetail.setTextColor(color);
				wifiDescriptionValueDetail.setTextColor(color);

				Float small = (value % 1);
				if (small != 0) {
					wifiSmallValueDetail.setText(".0");
				} else
					wifiSmallValueDetail.setText(".0");
			}
		});

		final ListView details = (ListView) v
				.findViewById(R.id.fragment_dashboard_detail_wifi_listview);

		testList = new ArrayList<DetailItem>();
		
		
//		// regista para receber os eventos do wifi e da rede movel
//		MyApplication MyApplicationRef = (MyApplication) getActivity()
//				.getApplicationContext();
//		IService iService = MyApplicationRef.getEngineServiceRef();
//		if (iService != null) {
//			iService.registry_update_wifi_information(this);
//			iService.registry_update_wifi_params(this);
//			iService.get_wifi_information(this);
//			iService.get_wifi_params(this);
//		}
		adapter = new AdapterDashboardDetail(
				getActivity().getApplicationContext(), testList);
		details.setAdapter(adapter);

		getCurrentValues();
		
		return v;

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
	
	@Override
	public void update_mobile_information(EMobileState mobile_state,
			EMobileNetworkMode network_mode, String operator_code,
			String rx_level, String cid, String lac) {
	}

	@Override
	public void update_wifi_information(String link_speed, String ssid,
			String rx_level, String channel) {
		
		final String method = "update_wifi_information";

		try {

			MyLogger.debug(logger, method, "link_speed :" + link_speed
					+ " ssid :" + ssid + " rx_level :" + rx_level
					+ " channel :" + channel);

			final GaugeView gvWiFi = (GaugeView) v.findViewById(R.id.fragment_dashboard_detail_wifi_gaugeview);
			gvWiFi.setCurrentValue(Float.parseFloat(rx_level));

//			get_wifi_operator_branding();
			
			if (Integer.parseInt(link_speed) <= 0) {
				textSpeed.setText(R.string.na);
			} else {
				textSpeed.setText(link_speed + getString(R.string.mbps));
			}
	
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	


	@Override
	public void update_mobile_params(TreeMap<String, String> keyValueParams) {

	}

	@Override
	public void update_wifi_params(TreeMap<String, String> keyValueParams) {

		testList.clear();
		for(Entry<String, String> entry : keyValueParams.entrySet()) {
			DetailItem item = new DetailItem(entry.getKey(), entry.getValue());
			testList.add(item);
			MyLogger.debug(logger, "keyName: ", item.getFieldName());
			MyLogger.debug(logger, "keyValue: ", item.getFieldValue());
		}
		adapter.notifyDataSetChanged();
	
	}

	@Override
	public void send_pending_tests_ack(ENetworkAction action_state) {

	}

	@Override
	public void send_report_ack(ENetworkAction action_state) {

	}

	public static Fragment newInstance() {
		return new FragmentDashboardDetailWifi();
	}

	@Override
	public void update_test_info() {

	}

	@Override
	public void update_test_task(String test_id) {

	}


}

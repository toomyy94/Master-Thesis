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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentDashboardDetailMobile extends Fragment implements IUI {
	private final static Logger logger = LoggerFactory
			.getLogger(FragmentDashboardDetailMobile.class);
	private static final String TAG = "FragDashboardDetailMob";

	ImageView mobileRestart;
	LinearLayout ll;
	int screenWidth, screenHeight;
	ListView details;
	TextView tiprede,mobileBigValue, mobileSmallValue, mobileDescriptionValue;
	AdapterDashboardDetail adapter;
	ImageView mobileNetworkDetail;
	private View v = null;
	private FragmentDashboardDetailMobile myRef = null;
	ArrayList<DetailItem> testList;
	ActivityDashboardMain activity;
	
	private MyApplication myApplicationRef;
	private IService iService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		activity = (ActivityDashboardMain) getActivity();
		
		// Regista para receber os eventos da rede móvel
		myApplicationRef = (MyApplication) getActivity()
				.getApplicationContext();
		iService = myApplicationRef.getEngineServiceRef();
	}
	
	@Override
	public void onPause() {
		super.onPause();

		if (iService != null) {	
			iService.remove_registry_update_mobile_information(this);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
				
		if (iService != null) {
			iService.registry_update_mobile_information(this);
			iService.registry_update_mobile_params(this);
			iService.get_mobile_params(this);
		}
				
		getCurrentValues();
	}
	
	public void getCurrentValues() {
		if(activity.hasMobileInfo())
			activity.getCurrentMobileInfo(this);
		
		if(activity.hasMobileParams())
			activity.getCurrentMobileParams(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		v = inflater.inflate(R.layout.fragment_dashboard_detail_mobile, container, false);
		myRef = this;
		mobileBigValue = (TextView) v
				.findViewById(R.id.mobileBigValue);
		mobileSmallValue = (TextView) v
				.findViewById(R.id.SmallValue);
		tiprede = (TextView) v.findViewById(R.id.tiprede);
		
		/* Button to restart mobile interface */
		mobileRestart = (ImageView) v.findViewById(R.id.fragment_dashboard_detail_mobile_restart);
		mobileRestart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(iService != null) {
					/* Restart mobile interface */
					iService.restart_mobile_interface();
					/* Return to main Dashboard page */
					activity.goToDashboardMainPage();
				}
			}
		});
		
		mobileDescriptionValue = (TextView) v
				.findViewById(R.id.descriptionvalue);
	
		DisplayMetrics displayMetrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		screenWidth = displayMetrics.widthPixels;
		screenHeight = displayMetrics.heightPixels;

		int graphicSize = (screenWidth / 3);

		ll = (LinearLayout) v.findViewById(R.id.gaugeNeedleLay);
		details = (ListView) v.findViewById(R.id.fragment_dashboard_detail_mobile_listview);
		
		
		
		final GaugeView gv = (GaugeView) v.findViewById(R.id.gvMobile);
		
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
		


		testList = new ArrayList<DetailItem>();
		
//		// regista para receber os eventos do wifi e da rede movel
//		MyApplication MyApplicationRef = (MyApplication) getActivity()
//				.getApplicationContext();
//		IService iService = MyApplicationRef.getEngineServiceRef();
//
//		if (iService != null) {
//			iService.registry_update_mobile_information(this);
//			iService.get_mobile_information(this);
//			iService.registry_update_mobile_params(this);
//		}
		
		adapter = new AdapterDashboardDetail(getActivity().getApplicationContext(), testList);		
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
		
		final String method = "update_mobile_information";
		
		final GaugeView gv = (GaugeView) v.findViewById(R.id.gvMobile);
		gv.setCurrentValue(Float.parseFloat(rx_level));
		
		try {			
			switch (network_mode) {
			case GPRS:
				gv.setType(GaugeType.MobileNetwork2G);
				tiprede.setText(getString(R.string.network_type_2g));
				break;
			case EDGE:
				gv.setType(GaugeType.MobileNetwork2G);
				tiprede.setText(getString(R.string.network_type_2g));
				break;
			case UMTS:
				gv.setType(GaugeType.MobileNetwork3G);
				tiprede.setText(getString(R.string.network_type_3g));
				break;
			case HSPA:
				gv.setType(GaugeType.MobileNetwork3G);
				tiprede.setText(getString(R.string.network_type_3g));
				break;
			case LTE:
				gv.setType(GaugeType.MobileNetwork4G);
				tiprede.setText(getString(R.string.network_type_4g));
				break;
			case NONE:
				gv.setType(GaugeType.Unknown);
				tiprede.setText("");
				break;
			}
	

			// setOperator(operator_code);
			

		} catch (Exception ex) {
		
			MyLogger.error(logger, method, ex);	
			
		}
	}

	
	@Override
	public void update_wifi_information(String link_speed, String ssid,
			String rx_level, String channel) {
		
		
	}

	@Override
	public void update_mobile_params(TreeMap<String, String> keyValueParams) {
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
	public void update_wifi_params(TreeMap<String, String> keyValueParams) {

	}

	@Override
	public void send_pending_tests_ack(ENetworkAction action_state) {

	}

	@Override
	public void send_report_ack(ENetworkAction action_state) {

	}

	public static Fragment newInstance() {
		return new FragmentDashboardDetailMobile();
	}

	@Override
	public void update_test_info() {

	}

	@Override
	public void update_test_task(String test_id) {

	}

}

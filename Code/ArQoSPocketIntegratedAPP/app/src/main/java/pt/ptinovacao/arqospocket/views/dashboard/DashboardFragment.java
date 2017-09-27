package pt.ptinovacao.arqospocket.views.dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.ArQoSBaseFragment;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.network.MobileNetworkManager;
import pt.ptinovacao.arqospocket.core.network.MobileNetworkMode;
import pt.ptinovacao.arqospocket.core.network.WifiNetworkManager;
import pt.ptinovacao.arqospocket.core.network.data.mobile.MobileInfoData;
import pt.ptinovacao.arqospocket.core.network.data.wifi.WifiInfoData;
import pt.ptinovacao.arqospocket.views.GaugeType;
import pt.ptinovacao.arqospocket.views.GaugeView;

/**
 * Dashboard.
 * <p>
 * Created by pedro on 10/04/2017.
 */
public class DashboardFragment extends ArQoSBaseFragment {

    private final static Logger LOGGER = LoggerFactory.getLogger(DashboardFragment.class);

    private ImageView mobileLogoImageView;

    private ImageView wifiLogoImageView;

    private TextView mobileNetworkStateTextView;

    private TextView wifiStateTextView;

    private LinearLayout dashboardLayout;

    private LinearLayout dashboardLowerLayout;

    private LinearLayout dashboardMobileLayout;

    private LinearLayout dashboardWifiLayout;

    private ImageView wifiDetailImageView;

    private ImageView mobileNetworkDetailImageView;

    private ImageView mobileNetworkImageView;

    private ImageView wifiImageView;

    private TextView mobileBigValueTextView;

    private TextView mobileSmallValueTextView;

    private TextView mobileDescriptionValueTextView;

    private TextView wifiBigValueTextView;

    private TextView wifiSmallValueTextView;

    private TextView wifiDescriptionValueTextView;

    private TextView cidTextView;

    private TextView lacTextView;

    private TextView channelTextView;

    private TextView speedTextView;

    private TextView networkTipTextView;

    private TextView ssidTextView;

    private GaugeView mobileGaugeView;

    private GaugeView wifiGaugeView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard, container, false);

        initializeViews(rootView);
        adjustUI();

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().registerReceiver(dataUpdateReceiver, DashboardActivity.dataUpdateDataMobileIntentFilter());
        getActivity().registerReceiver(dataUpdateReceiver, DashboardActivity.dataUpdateDataWifiIntentFilter());

        updateWifiUI();
        updateMobileUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        LOGGER.debug("Dashboard stopped");
        getActivity().unregisterReceiver(dataUpdateReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LOGGER.debug("onDestroy");
    }

    @Override
    public int getActivityTitle() {
        return R.string.dashboard;
    }

    private void initializeViews(ViewGroup rootView) {

        mobileNetworkStateTextView = (TextView) rootView.findViewById(R.id.network_state);
        wifiStateTextView = (TextView) rootView.findViewById(R.id.wifi_state);
        dashboardMobileLayout = (LinearLayout) rootView.findViewById(R.id.activity_dashboard_mobile_network_info);
        dashboardWifiLayout = (LinearLayout) rootView.findViewById(R.id.activity_dashboard_wifi_info);
        dashboardLayout = (LinearLayout) rootView.findViewById(R.id.activity_dashboard_lower_layout1);
        dashboardLowerLayout = (LinearLayout) rootView.findViewById(R.id.activity_dashboard_lower_layout2);
        mobileBigValueTextView = (TextView) rootView.findViewById(R.id.fragment_dashboard_mobile_network_big_value);
        mobileSmallValueTextView = (TextView) rootView.findViewById(R.id.fragment_dashboard_mobile_network_small_value);
        mobileDescriptionValueTextView =
                (TextView) rootView.findViewById(R.id.fragment_dashboard_mobile_network_description_value);
        wifiBigValueTextView = (TextView) rootView.findViewById(R.id.fragment_dashboard_wifi_big_value);
        wifiSmallValueTextView = (TextView) rootView.findViewById(R.id.fragment_dashboard_wifi_small_value);
        wifiDescriptionValueTextView =
                (TextView) rootView.findViewById(R.id.fragment_dashboard_wifi_network_description_value);
        cidTextView = (TextView) rootView.findViewById(R.id.cid);
        lacTextView = (TextView) rootView.findViewById(R.id.lac);
        channelTextView = (TextView) rootView.findViewById(R.id.channel);
        speedTextView = (TextView) rootView.findViewById(R.id.LinkSpeed);
        networkTipTextView = (TextView) rootView.findViewById(R.id.TipRede);
        ssidTextView = (TextView) rootView.findViewById(R.id.essid);
        mobileLogoImageView = (ImageView) rootView.findViewById(R.id.btn1);
        wifiLogoImageView = (ImageView) rootView.findViewById(R.id.btn2);
        mobileNetworkDetailImageView = (ImageView) rootView.findViewById(R.id.activity_dashboard_mobile_network_detail);
        wifiDetailImageView = (ImageView) rootView.findViewById(R.id.activity_dashboard_wifi_detail);
        mobileNetworkImageView = (ImageView) rootView.findViewById(R.id.icon_rede_movel);
        wifiImageView = (ImageView) rootView.findViewById(R.id.icon_wi_fi);

        mobileGaugeView = (GaugeView) rootView.findViewById(R.id.gv1);
        wifiGaugeView = (GaugeView) rootView.findViewById(R.id.gv2);
    }

    private void adjustUI() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;

        int graphicSize = (screenWidth / 2);

        if (!MobileNetworkManager.getInstance(getArQosApplication()).isMobileAvailable()) {
            setMobileNetworkUnavailable(graphicSize);
        } else {
            setMobileNetworkAvailable(graphicSize);
        }

        if (!WifiNetworkManager.getInstance(getArQosApplication()).isWifiAvailable()) {
            setWifiNetworkUnavailable(graphicSize);
        } else {
            setWifiNetworkAvailable(graphicSize);
        }
    }

    private void setWifiNetworkAvailable(int graphicSize) {
        // Show availability icon
        wifiImageView.setImageResource(R.mipmap.icon_wi_fi);

        // Show info params
        dashboardWifiLayout.setVisibility(View.VISIBLE);
        wifiDetailImageView.setVisibility(View.VISIBLE);
        wifiLogoImageView.setVisibility(View.VISIBLE);
        dashboardLowerLayout.setVisibility(View.VISIBLE);
        speedTextView.setVisibility(View.VISIBLE);

        wifiDetailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DashboardActivity) getActivity()).showWifiDetails();
            }
        });

        // Hide unavailability text
        wifiStateTextView.setVisibility(View.INVISIBLE);

        wifiGaugeView.setVisibility(View.VISIBLE);
        wifiGaugeView.setDiameter(graphicSize);
        wifiGaugeView.setType(GaugeType.WifiSignalLevel);
        wifiGaugeView.createNeedle(graphicSize, graphicSize, 0, -graphicSize / 2);
        wifiGaugeView.setAngleRange(0, 225);
        wifiGaugeView.setNeedleAnimation(true);
        wifiGaugeView.setAnimationTime(500, 500);

        wifiGaugeView.setOnNeedleDeflectListener(new GaugeView.NeedleDeflectListener() {

            @Override
            public void onDeflect(float angle, float value) {
                wifiBigValueTextView.setText(String.valueOf((int) value));
                int color = wifiGaugeView.getColor(wifiGaugeView.getType(), angle);
                wifiBigValueTextView.setTextColor(color);
                wifiSmallValueTextView.setTextColor(color);
                wifiDescriptionValueTextView.setTextColor(color);

                float small = (value % 1);
                if (small != 0) {
                    wifiSmallValueTextView.setText(".0");
                } else {
                    wifiSmallValueTextView.setText(".0");
                }
            }
        });
    }

    private void setWifiNetworkUnavailable(int graphicSize) {
        // Show unavailability icon
        wifiImageView.setImageResource(R.mipmap.wifi_unavailable);

        // Hide info params
        dashboardWifiLayout.setVisibility(View.GONE);
        wifiDetailImageView.setVisibility(View.GONE);
        wifiLogoImageView.setVisibility(View.GONE);
        dashboardLowerLayout.setVisibility(View.GONE);
        speedTextView.setVisibility(View.GONE);
        wifiStateTextView.setVisibility(View.VISIBLE);
        wifiStateTextView.setText(R.string.unavailable);

        wifiGaugeView.setVisibility(View.VISIBLE);
        wifiGaugeView.setType(GaugeType.Wifi_disabled);
        wifiGaugeView.setDiameter(graphicSize);
        wifiGaugeView.createNeedle(graphicSize, graphicSize, 0, -graphicSize / 2);
        wifiGaugeView.setNeedleAnimation(true);
        wifiGaugeView.setCurrentValue(90); // zero
    }

    private void setMobileNetworkAvailable(int graphicSize) {
        // Show availability icon
        mobileNetworkImageView.setImageResource(R.mipmap.icon_network_mobile);

        // Show info params
        dashboardMobileLayout.setVisibility(View.VISIBLE);
        mobileNetworkDetailImageView.setVisibility(View.VISIBLE);
        mobileLogoImageView.setVisibility(View.VISIBLE);
        dashboardLayout.setVisibility(View.VISIBLE);

        mobileNetworkDetailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DashboardActivity) getActivity()).showMobileDetails();
            }
        });

        // Hide unavailability text
        mobileNetworkStateTextView.setVisibility(View.INVISIBLE);

        mobileGaugeView.setVisibility(View.VISIBLE);
        mobileGaugeView.setDiameter(graphicSize);
        mobileGaugeView.createNeedle(graphicSize, graphicSize, 0, -graphicSize / 2);
        mobileGaugeView.setNeedleAnimation(true);
        mobileGaugeView.setAnimationTime(250, 250);

        mobileGaugeView.setOnNeedleDeflectListener(new GaugeView.NeedleDeflectListener() {

            @Override
            public void onDeflect(float angle, float value) {

                mobileBigValueTextView.setText(String.valueOf((int) value));
                int color = mobileGaugeView.getColor(mobileGaugeView.getType(), angle);
                mobileBigValueTextView.setTextColor(color);
                mobileSmallValueTextView.setTextColor(color);
                mobileDescriptionValueTextView.setTextColor(color);

                float small = (value % 1);
                if (small != 0) {
                    mobileSmallValueTextView.setText(".0");
                } else {
                    mobileSmallValueTextView.setText(".0");
                }
            }
        });
    }

    private void setMobileNetworkUnavailable(int graphicSize) {
        // Show unavailability icon
        mobileNetworkImageView.setImageResource(R.mipmap.network_mobile_unavailable);

        // Hide info params
        dashboardMobileLayout.setVisibility(View.GONE);
        mobileNetworkDetailImageView.setVisibility(View.GONE);
        mobileLogoImageView.setVisibility(View.GONE);
        dashboardLayout.setVisibility(View.GONE);

        // Show unavailability text
        mobileNetworkStateTextView.setVisibility(View.VISIBLE);
        mobileNetworkStateTextView.setText(R.string.unavailable);

        mobileGaugeView.setVisibility(View.VISIBLE);
        mobileGaugeView.setType(GaugeType.Mobile_disabled);
        mobileGaugeView.setDiameter(graphicSize);
        mobileGaugeView.createNeedle(graphicSize, graphicSize, 0, -graphicSize / 2);
        mobileGaugeView.setNeedleAnimation(true);
        mobileGaugeView.setCurrentValue(0);
    }

    private void updateWifiUI() {

        WifiInfoData data = WifiNetworkManager.getInstance(getArQosApplication()).getWifiInfoData();
        String linkSpeed = data.getLinkSpeed();
        String ssid = data.getSsid();
        String rxLevel = data.getRxLevel();
        String channel = data.getChannel();
        Integer state = data.getWifiState();

        try {

            adjustUI();

//            LOGGER.debug("Update Wifi Information -> linkSpeed: {}, ssid: {}, rxLevel: {}, channel: {}, state: {}",
//                    linkSpeed, ssid, rxLevel, channel, state);

            if (wifiGaugeView.getCurrentValue() != Float.parseFloat(rxLevel)) {
                wifiGaugeView.setCurrentValue(Float.parseFloat(rxLevel));
            }

            if (ssid == null) {
                ssidTextView.setText(R.string.na);
            } else {
                ssidTextView.setText(ssid.replace("\"", ""));
            }

            channelTextView.setText(channel);

            if (Integer.parseInt(linkSpeed) <= 0) {
                speedTextView.setText(R.string.na);
            } else {
                String speed = linkSpeed + getString(R.string.mbps);
                speedTextView.setText(speed);
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    private void updateMobileUI() {

        adjustUI();

        MobileInfoData data = MobileNetworkManager.getInstance(getArQosApplication()).getMobileInfoData();
        MobileNetworkMode mobileNetworkMode = data.getMobileNetworkMode();
        int signalLevel = data.getSignalLevel();
        String idCell = data.getIdCell();
        String cellLocation = data.getCellLocation();

       // LOGGER.debug("Update Mobile Information -> signalLevel: {}, idCell: {}, cellLocation: {}", signalLevel, idCell,
         //       cellLocation);

        DashboardActivity
                .setLayoutMobile(mobileNetworkMode, mobileGaugeView, networkTipTextView, lacTextView, cidTextView,
                        signalLevel, idCell, cellLocation);
    }

    private final BroadcastReceiver dataUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (WifiNetworkManager.ACTION_WIFI_DATA_CHANGED.equals(action)) {
                updateWifiUI();
            }

            if (MobileNetworkManager.ACTION_MOBILE_DATA_CHANGED.equals(action)) {
                updateMobileUI();
            }
        }
    };
}
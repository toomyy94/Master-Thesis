package pt.ptinovacao.arqospocket.views.dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import pt.ptinovacao.arqospocket.ArQoSBaseFragment;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.network.WifiNetworkManager;
import pt.ptinovacao.arqospocket.core.network.data.wifi.WifiInfoData;
import pt.ptinovacao.arqospocket.views.GaugeType;
import pt.ptinovacao.arqospocket.views.GaugeView;
import pt.ptinovacao.arqospocket.views.dashboard.adapter.DashboardDetailAdapter;
import pt.ptinovacao.arqospocket.views.dashboard.adapter.DetailItem;

/**
 * DashboardFragmentWifiDetails.
 * <p>
 * Created by pedro on 10/04/2017.
 */
public class DashboardFragmentWifiDetails extends ArQoSBaseFragment {

    private final static Logger LOGGER = LoggerFactory.getLogger(DashboardFragmentWifiDetails.class);

    private TextView wifiBigValueDetail;

    private TextView wifiSmallValueDetail;

    private TextView textSpeed;

    private TextView wifiDescriptionValueDetail;

    private ImageView wifiRestart;

    private GaugeView gaugeView;

    private ArrayList<DetailItem> details;

    private DashboardDetailAdapter adapter;

    private final BroadcastReceiver networkDataUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (WifiNetworkManager.ACTION_WIFI_DATA_CHANGED.equals(action)) {
                updatedWifiUI();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_wifi_dashboard, container, false);

        initializeViews(rootView);
        initializeListeners();

        updatedWifiUI();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(networkDataUpdateReceiver, DashboardActivity.dataUpdateDataWifiIntentFilter());
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(networkDataUpdateReceiver);
    }

    @Override
    public int getActivityTitle() {
        return R.string.wifi;
    }

    private void initializeViews(ViewGroup rootView) {
        textSpeed = (TextView) rootView.findViewById(R.id.speed);
        wifiBigValueDetail = (TextView) rootView.findViewById(R.id.big_value);
        wifiSmallValueDetail = (TextView) rootView.findViewById(R.id.small_value);
        wifiDescriptionValueDetail = (TextView) rootView.findViewById(R.id.description_value);
        wifiRestart = (ImageView) rootView.findViewById(R.id.fragment_dashboard_detail_wifi_restart);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int graphicSize = (screenWidth / 3);

        gaugeView = (GaugeView) rootView.findViewById(R.id.fragment_dashboard_detail_wifi_gaugeview);
        gaugeView.setDiameter(graphicSize);
        gaugeView.setType(GaugeType.WifiSignalLevel);
        gaugeView.createNeedle(graphicSize, graphicSize, 0, -graphicSize / 2);
        gaugeView.setNeedleAnimation(true);
        gaugeView.setAnimationTime(500, 500);

        gaugeView.setOnNeedleDeflectListener(new GaugeView.NeedleDeflectListener() {
            @Override
            public void onDeflect(float angle, float value) {
                wifiBigValueDetail.setText(String.valueOf((int) value));
                int color = gaugeView.getColor(gaugeView.getType(), angle);
                wifiBigValueDetail.setTextColor(color);
                wifiSmallValueDetail.setTextColor(color);
                wifiDescriptionValueDetail.setTextColor(color);

                Float small = (value % 1);
                if (small != 0) {
                    wifiSmallValueDetail.setText(".0");
                } else {
                    wifiSmallValueDetail.setText(".0");
                }
            }
        });

        ListView details = (ListView) rootView.findViewById(R.id.fragment_dashboard_detail_wifi_listview);
        this.details = new ArrayList<>();
        adapter = new DashboardDetailAdapter(getArQosApplication(), this.details);
        details.setAdapter(adapter);
    }

    private void initializeListeners() {
        wifiRestart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                WifiNetworkManager.getInstance(getArQosApplication()).restartWifi();
                showMainPage();
            }
        });

    }

    private void updatedWifiUI() {
        WifiInfoData data = WifiNetworkManager.getInstance(getArQosApplication()).getWifiInfoData();
        String linkSpeed = data.getLinkSpeed();
        String rxLevel = data.getRxLevel();

        float value;
        try {
            value = Float.parseFloat(Strings.nullToEmpty(rxLevel));
        } catch (NumberFormatException e) {
            value = 0.0f;
        }
        gaugeView.setCurrentValue(value);

        if ( linkSpeed != null && Integer.parseInt(linkSpeed) <= 0) {
            textSpeed.setText(R.string.na);
        } else {
            String speed = linkSpeed + getString(R.string.mbps);
            textSpeed.setText(speed);
        }

        updateWifiDetails();
    }

    private void updateWifiDetails() {
        WifiInfoData data = WifiNetworkManager.getInstance(getArQosApplication()).getWifiInfoData();
        details.clear();

        details.add(new DetailItem(getString(R.string.wifi_params_essid), data.getSsid()));
        details.add(new DetailItem(getString(R.string.wifi_params_channel), data.getChannel()));
        details.add(new DetailItem(getString(R.string.wifi_params_bssid), data.getBssid()));
        details.add(new DetailItem(getString(R.string.wifi_params_hidden_ssid),
                data.isHiddenSsid() ? getString(R.string.yes) : getString(R.string.no)));
        if (data.isRealMacAddress()) {
            details.add(new DetailItem(getString(R.string.wifi_params_mac_address), data.getMacAddress()));
        }
        details.add(new DetailItem(getString(R.string.wifi_params_ip_address), data.getIpAddress()));
        details.add(new DetailItem(getString(R.string.wifi_params_primary_dns), data.getDns1()));
        details.add(new DetailItem(getString(R.string.wifi_params_secondary_dns), data.getDns2()));
        details.add(new DetailItem(getString(R.string.wifi_params_gateway_adress), data.getGateway()));
        details.add(new DetailItem(getString(R.string.wifi_params_lease_duration), getLeaseDuration(data)));
        details.add(new DetailItem(getString(R.string.wifi_params_netmask), data.getNetMask()));
        details.add(new DetailItem(getString(R.string.wifi_params_server_address), data.getServerAddress()));

        adapter.notifyDataSetChanged();
    }

    private String getLeaseDuration(WifiInfoData data) {
        Integer leaseDuration = data.getLeaseDuration();
        if (leaseDuration == null || leaseDuration < 0) {
            return getString(R.string.na);
        }
        return String.valueOf(leaseDuration);
    }

    private void showMainPage() {
        FragmentActivity activity = getActivity();
        LOGGER.debug("Activity class: {}", activity.getClass());
        if (activity instanceof DashboardActivity) {
            ((DashboardActivity) activity).showMainDashboardPage();
        }
    }
}
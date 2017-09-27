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
import pt.ptinovacao.arqospocket.core.network.MobileNetworkManager;
import pt.ptinovacao.arqospocket.core.network.MobileNetworkMode;
import pt.ptinovacao.arqospocket.core.network.data.mobile.MobileInfoData;
import pt.ptinovacao.arqospocket.views.GaugeView;
import pt.ptinovacao.arqospocket.views.dashboard.adapter.DashboardDetailAdapter;
import pt.ptinovacao.arqospocket.views.dashboard.adapter.DetailItem;

/**
 * DashboardFragmentMobileDetails
 * <p>
 * Created by pedro on 10/04/2017.
 */
public class DashboardFragmentMobileDetails extends ArQoSBaseFragment {

    private final static Logger LOGGER = LoggerFactory.getLogger(DashboardFragmentMobileDetails.class);

    private ImageView mobileRestart;

    private TextView networkTip;

    private TextView mobileBigValue;

    private TextView mobileSmallValue;

    private TextView mobileDescriptionValue;

    private GaugeView gaugeView;

    private ArrayList<DetailItem> details;

    private DashboardDetailAdapter adapter;

    private final BroadcastReceiver mNetworkDataUpdateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (MobileNetworkManager.ACTION_MOBILE_DATA_CHANGED.equals(action)) {
                updatedMobileUI();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mobile_dashboard, container, false);

        initializeViews(rootView);
        initializeListeners();

        updatedMobileUI();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity()
                .registerReceiver(mNetworkDataUpdateReceiver, DashboardActivity.dataUpdateDataMobileIntentFilter());
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mNetworkDataUpdateReceiver);
    }

    @Override
    public int getActivityTitle() {
        return R.string.mobile_network;
    }

    private void initializeViews(ViewGroup rootView) {
        mobileBigValue = (TextView) rootView.findViewById(R.id.mobileBigValue);
        mobileSmallValue = (TextView) rootView.findViewById(R.id.SmallValue);
        networkTip = (TextView) rootView.findViewById(R.id.tiprede);

        mobileRestart = (ImageView) rootView.findViewById(R.id.fragment_dashboard_detail_mobile_restart);

        mobileDescriptionValue = (TextView) rootView.findViewById(R.id.descriptionvalue);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;

        int graphicSize = (screenWidth / 3);

        gaugeView = (GaugeView) rootView.findViewById(R.id.gvMobile);

        gaugeView.setDiameter(graphicSize);
        gaugeView.createNeedle(graphicSize, graphicSize, 0, -graphicSize / 2);

        gaugeView.setNeedleAnimation(true);
        gaugeView.setAnimationTime(250, 250);

        gaugeView.setOnNeedleDeflectListener(new GaugeView.NeedleDeflectListener() {

            @Override
            public void onDeflect(float angle, float value) {

                mobileBigValue.setText(String.valueOf((int) value));
                int color = gaugeView.getColor(gaugeView.getType(), angle);
                mobileBigValue.setTextColor(color);
                mobileSmallValue.setTextColor(color);
                mobileDescriptionValue.setTextColor(color);

                Float small = (value % 1);
                if (small != 0) {
                    mobileSmallValue.setText(".0");
                } else {
                    mobileSmallValue.setText(".0");
                }
            }
        });

        ListView details = (ListView) rootView.findViewById(R.id.fragment_dashboard_detail_mobile_listview);
        this.details = new ArrayList<>();
        adapter = new DashboardDetailAdapter(getArQosApplication(), this.details);
        details.setAdapter(adapter);
    }

    private void initializeListeners() {
        mobileRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobileNetworkManager.getInstance(getArQosApplication()).restartNetwork();
                showMainPage();
            }
        });
    }

    public void updatedMobileUI() {

        MobileInfoData data = MobileNetworkManager.getInstance(getArQosApplication()).getMobileInfoData();
        MobileNetworkMode mobileNetworkMode = data.getMobileNetworkMode();
        int signalLevel = data.getSignalLevel();
        String idCell = data.getIdCell();
        String cellLocation = data.getCellLocation();

        DashboardActivity.setLayoutMobile(mobileNetworkMode, gaugeView, networkTip, null, null, signalLevel, idCell,
                cellLocation);

        updateMobileDetails();
    }

    private void updateMobileDetails() {
        MobileInfoData data = MobileNetworkManager.getInstance(getArQosApplication()).getMobileInfoData();
        details.clear();

        details.add(new DetailItem(getString(R.string.mobile_params_device_id), data.getDeviceId()));
        details.add(new DetailItem(getString(R.string.mobile_params_cell_id), getDeviceId(data)));
        details.add(new DetailItem(getString(R.string.mobile_params_provider_code), data.getMccMnc()));
        details.add(new DetailItem(getString(R.string.mobile_params_msisdn), getMsisdn(data)));
        details.add(
                new DetailItem(getString(R.string.mobile_params_network_operator_name), data.getNetworkOperatorName()));
        details.add(new DetailItem(getString(R.string.mobile_params_imsi), data.getImsi()));
        details.add(new DetailItem(getString(R.string.mobile_params_roaming),
                data.isRoaming() ? getString(R.string.yes) : getString(R.string.no)));

        adapter.notifyDataSetChanged();
    }

    private String getMsisdn(MobileInfoData data) {
        String msisdn = Strings.nullToEmpty(data.getMsisdn());

        if (msisdn.length() == 0) {
            return getString(R.string.na);
        }

        return msisdn;
    }

    private String getDeviceId(MobileInfoData data) {
        String cellId = Strings.nullToEmpty(data.getIdCell());

        if (cellId.length() == 0 || cellId.equals("-1")) {
            return getString(R.string.na);
        }

        return cellId;
    }

    private void showMainPage() {
        final FragmentActivity activity = getActivity();
        LOGGER.debug("Activity class: {}", activity.getClass());
        if (activity instanceof DashboardActivity) {
            ((DashboardActivity) activity).showMainDashboardPage();
        }
    }
}
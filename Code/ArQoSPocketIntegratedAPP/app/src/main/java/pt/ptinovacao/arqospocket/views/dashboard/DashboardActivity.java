package pt.ptinovacao.arqospocket.views.dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import pt.ptinovacao.arqospocket.ArQoSBaseFragment;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.network.MobileNetworkManager;
import pt.ptinovacao.arqospocket.core.network.MobileNetworkMode;
import pt.ptinovacao.arqospocket.core.network.WifiNetworkManager;
import pt.ptinovacao.arqospocket.menu.MainFragmentsActivity;
import pt.ptinovacao.arqospocket.utils.RequestPermissionsUtils;
import pt.ptinovacao.arqospocket.views.GaugeType;
import pt.ptinovacao.arqospocket.views.GaugeView;

/**
 * DashboardActivity
 * <p>
 * Created by pedro on 12/04/2017.
 */
public class DashboardActivity extends MainFragmentsActivity {

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardActivity.class);

    private final DashboardFragment DASHBOARD_FRAGMENT = new DashboardFragment();

    private final DashboardFragmentMobileDetails DASHBOARD_MOBILE_FRAGMENT = new DashboardFragmentMobileDetails();

    private final DashboardFragmentWifiDetails DASHBOARD_WIFI_FRAGMENT = new DashboardFragmentWifiDetails();

    private ScreenSlidePagerAdapter pagerAdapter;

    private int mobileHalfHeight;

    private boolean switched = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int actionBarHeight = (int) getResources().getDimension(R.dimen.action_bar_height);
        mobileHalfHeight = (screenHeight - actionBarHeight) / 2;

        RequestPermissionsUtils.ignoreBatteryOptimizationSettings(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        if (getPager().getCurrentItem() == 2) {
            getPager().setCurrentItem(getPager().getCurrentItem() - 1);
        }
        super.onBackPressed();
    }

    @Override
    public void setPagerAdapter() {
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        setPagerAdapter(pagerAdapter);
        updatePages();
        getPager().setOffscreenPageLimit(2);
    }

    @Override
    protected boolean onViewPagerTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE && !switched && getPager().getCurrentItem() == 0) {
            switched = true;
            validateTouchArea(event);
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            switched = false;
        }

        return false;
    }

    private void validateTouchArea(MotionEvent event) {
        if (event.getY() < mobileHalfHeight) {
            updatePages(true, false);
        } else {
            updatePages(false, true);
        }
    }

    @Override
    public void setNumberPage() {
        setNumPages(2);
    }

    @Override
    public void onResume() {
        super.onResume();

        registerReceiver(dataUpdateReceiver, DashboardActivity.dataUpdateDataMobileIntentFilter());
        registerReceiver(dataUpdateReceiver, DashboardActivity.dataUpdateDataWifiIntentFilter());
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(dataUpdateReceiver);
    }

    public void showMainDashboardPage() {
        getPager().setCurrentItem(0);
    }

    public void showMobileDetails() {
        updatePages(true, false);
        getPager().setCurrentItem(1);
    }

    public void showWifiDetails() {
        updatePages(false, true);
        getPager().setCurrentItem(1);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> fragments = new ArrayList<>();

        private final Object locker = new Object();

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        void updateItems(boolean mobileOn, boolean wifiOn) {
            synchronized (locker) {
                fragments.clear();

                fragments.add(DASHBOARD_FRAGMENT);
                if (mobileOn) {
                    fragments.add(DASHBOARD_MOBILE_FRAGMENT);
                }
                if (wifiOn) {
                    fragments.add(DASHBOARD_WIFI_FRAGMENT);
                }
                setNumPages(fragments.size());
            }
            pagerAdapter.notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            synchronized (locker) {
                try {
                    return fragments.get(position);
                } catch (Exception e) {
                    LOGGER.error("Error creating fragment", e);
                    return new DashboardFragment();
                }
            }
        }

        @Override
        public int getCount() {
            return getNumPages();
        }

        @Override
        public int getItemPosition(Object object) {
            Fragment fragment = (Fragment) object;
            if (fragment.getClass().equals(DASHBOARD_FRAGMENT.getClass())) {
                return 0;
            }
            int indexOf = fragments.indexOf(fragment);
            return indexOf >= 0 ? indexOf : POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            ArQoSBaseFragment fragment = (ArQoSBaseFragment) fragments.get(position);
            return getString(fragment.getActivityTitle());
        }
    }

    public static void setLayoutMobile(MobileNetworkMode mobileNetworkMode, GaugeView gaugeView, TextView textView,
            TextView lacTextView, TextView cidTextView, int rxLevel, String cid, String lac) {

        switch (mobileNetworkMode) {
            case GPRS:
                gaugeView.setType(GaugeType.MobileNetwork2G);
                textView.setText(R.string.network_type_2g);
                break;
            case EDGE:
                gaugeView.setType(GaugeType.MobileNetwork2G);
                textView.setText(R.string.network_type_2g);
                break;
            case UMTS:
                gaugeView.setType(GaugeType.MobileNetwork3G);
                textView.setText(R.string.network_type_3g);
                break;
            case HSPA:
                gaugeView.setType(GaugeType.MobileNetwork3G);
                textView.setText(R.string.network_type_3g);
                break;
            case LTE:
                gaugeView.setType(GaugeType.MobileNetwork4G);
                textView.setText(R.string.network_type_4g);
                break;
            case NONE:
                gaugeView.setType(GaugeType.Unknown);
                textView.setText("");
                break;
        }

        if (lacTextView != null) {
            if (Integer.parseInt(lac) <= 0) {
                lacTextView.setText(R.string.na);
            } else {
                lacTextView.setText(lac);
            }
        }

        if (cidTextView != null) {
            if (Integer.parseInt(cid) <= 0) {
                cidTextView.setText(R.string.na);
            } else {
                cidTextView.setText(cid);
            }
        }

        gaugeView.setCurrentValue(rxLevel);
    }

    public static IntentFilter dataUpdateDataMobileIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MobileNetworkManager.ACTION_MOBILE_DATA_CHANGED);
        return intentFilter;
    }

    public static IntentFilter dataUpdateDataWifiIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiNetworkManager.ACTION_WIFI_DATA_CHANGED);
        return intentFilter;
    }

    private final BroadcastReceiver dataUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getPager().getCurrentItem() == 0) {
                updatePages();
            }
        }
    };

    private void updatePages() {
        boolean wifiOn = WifiNetworkManager.getInstance(getArQosApplication()).isWifiAvailable();
        boolean mobileOn = MobileNetworkManager.getInstance(getArQosApplication()).isMobileAvailable();

        pagerAdapter.updateItems(mobileOn, !mobileOn && wifiOn);
    }

    private void updatePages(boolean enableMobile, boolean enableWifi) {
        boolean wifiOn = WifiNetworkManager.getInstance(getArQosApplication()).isWifiAvailable();
        boolean mobileOn = MobileNetworkManager.getInstance(getArQosApplication()).isMobileAvailable();

        if (mobileOn && wifiOn) {
            pagerAdapter.updateItems(enableMobile, enableWifi);
        }
    }
}

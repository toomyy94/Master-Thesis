package pt.ptinovacao.arqospocket.views.anomaly;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.core.anomalies.AnomaliesManager;
import pt.ptinovacao.arqospocket.core.location.LocationInfo;
import pt.ptinovacao.arqospocket.menu.MainFragmentsActivity;
import pt.ptinovacao.arqospocket.persistence.models.Anomaly;
import pt.ptinovacao.arqospocket.views.anomaly.anomalysubtype.AnomalySubTypeFragment;
import pt.ptinovacao.arqospocket.views.anomaly.anomalytype.AnomalyTypeFragment;
import pt.ptinovacao.arqospocket.views.anomaly.data.AnomalyReport;
import pt.ptinovacao.arqospocket.views.anomaly.location.AnomalyLocationFragment;
import pt.ptinovacao.arqospocket.views.anomaly.send.AnomalySendFragment;

/**
 * Activity to collect a new anomaly.
 * <p>
 * Created by pedro on 12/04/2017.
 */
public class AnomalyActivity extends MainFragmentsActivity implements AnomalyTypeFragment.OnAnomalyTypeSelectedListener,
        AnomalySubTypeFragment.OnAnomalySubTypeSelectedListener, AnomalySendFragment.OnAnomalySendListener,
        AnomalyLocationFragment.OnAnomalyLocationSelectedListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(AnomalyActivity.class);

    private AnomalyReport anomalyTypeSelected = AnomalyReport.VOICE;

    private int anomalySubTypeSelected = 1;

    private LocationInfo locationInfo = new LocationInfo();

    private AnomalyBaseViewPager anomalyBaseViewPager;

    @Override
    public void setPagerAdapter() {
        setPagerAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));
    }

    @Override
    public void setNumberPage() {
        setNumPages(1);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                default:
                    anomalyBaseViewPager = new AnomalyBaseViewPager();
                    return anomalyBaseViewPager;
            }
        }

        @Override
        public int getCount() {
            return getNumPages();
        }
    }

    @Override
    public void onAnomalyTypeSelected(AnomalyReport type) {
        LOGGER.debug("Setting type: " + type);
        this.anomalyTypeSelected = type;
    }

    @Override
    public void onAnomalySend(String feedback) {
        LOGGER.debug("Sending feedback");

        Anomaly anomaly = new Anomaly();

        if (locationInfo != null) {
            anomaly.setLatitude(locationInfo.getLatitude());
            anomaly.setLongitude(locationInfo.getLongitude());
        }
        anomaly.setLogoId(getBaseContext().getResources().getString(anomalyTypeSelected.getResourceText()));
        anomaly.setReportSubType(anomalySubTypeSelected);
        anomaly.setReportType(getBaseContext().getResources().getString(anomalyTypeSelected.getResourceText()));
        anomaly.setUserFeedback(feedback);

        AnomaliesManager anomaliesManager = AnomaliesManager.getInstance(getArQosApplication());
        anomaliesManager.reportAnomaly(anomaly);

        onBackPressed();
    }

    @Override
    public void onAnomalySubTypeSelected(int subType) {
        LOGGER.debug("Setting sub type: " + subType);
        this.anomalySubTypeSelected = subType;
    }

    @Override
    public void onAnomalyLocationSelected(LocationInfo locationInfo) {
        LOGGER.debug("Setting location: " + locationInfo);
        this.locationInfo = locationInfo;
    }

    public AnomalyReport getAnomalyTypeSelected() {
        return anomalyTypeSelected;
    }

    @Override
    public void nextTabSelected() {
        anomalyBaseViewPager.nextTabSelected();
    }

    public int getAnomalySubTypeSelected() {
        return anomalySubTypeSelected;
    }

    public LocationInfo getLocationInfo() {
        return locationInfo;
    }

    public static AnomalyActivity getConvertActivity(Activity activity) {
        return (AnomalyActivity) activity;
    }

    public interface OnAnomalySelectTabListener {

        void nextTabListener(AnomalyReport type);
    }

    public interface OnControlViewPagerListener {

        void nextTabSelected();
    }
}

package pt.ptinovacao.arqospocket.views.anomalyhistoric.anomalydetails;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import pt.ptinovacao.arqospocket.menu.MainFragmentsActivity;
import pt.ptinovacao.arqospocket.persistence.models.Anomaly;

/**
 * Created by pedro on 20/04/2017.
 */
public class AnomalyDetailsActivity extends MainFragmentsActivity {

    public static final String KEY_ANOMALY_TO_DETAILS = "KEY_ANOMALY_TO_DETAILS";

    private Long valueTest;

    private ArrayList<Anomaly> testDataHistoricArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        testDataHistoricArrayList = new ArrayList<>();
        testDataHistoricArrayList
                .addAll(getArQosApplication().getDatabaseHelper().createAnomalyDao().readAllAnomalies());

        super.onCreate(savedInstanceState);

        valueTest = getIntent().getLongExtra(AnomalyDetailsActivity.KEY_ANOMALY_TO_DETAILS, 0L);

        getPager().setCurrentItem(calculatePosition());

    }

    @Override
    public void setPagerAdapter() {
        setPagerAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));
    }

    @Override
    public void setNumberPage() {
        setNumPages(testDataHistoricArrayList.size());
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return AnomalyDetailsFragment.newInstance(testDataHistoricArrayList.get(position).getId(), position > 0,
                    position + 1 < testDataHistoricArrayList.size());
        }

        @Override
        public int getCount() {
            return getNumPages();
        }
    }

    private int calculatePosition() {
        int i = 0;
        for (Anomaly anomaly : testDataHistoricArrayList) {
            if (!valueTest.equals(anomaly.getId())) {
                i++;
            } else {
                break;
            }
        }
        return i;
    }
}

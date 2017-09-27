package pt.ptinovacao.arqospocket.views.radiologshistoric.radiologsdetails;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import pt.ptinovacao.arqospocket.menu.MainFragmentsActivity;
import pt.ptinovacao.arqospocket.persistence.models.Radiolog;

/**
 * Created by Tomás Rodrigues on 20/04/2017.
 */
public class RadiologsDetailsActivity extends MainFragmentsActivity {

    public static final String KEY_RADIOLOG_TO_DETAILS = "KEY_RADIOLOG_TO_DETAILS";

    private Long valueTest;

    private ArrayList<Radiolog> testDataHistoricArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        testDataHistoricArrayList = new ArrayList<>();
        testDataHistoricArrayList
                .addAll(getArQosApplication().getDatabaseHelper().createRadiologDao().readAllRadiologs());

        super.onCreate(savedInstanceState);

        valueTest = getIntent().getLongExtra(RadiologsDetailsActivity.KEY_RADIOLOG_TO_DETAILS, 0L);
        getPager().setCurrentItem(calculatePosition());
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void setPagerAdapter() {
        setPagerAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));
    }

    @Override
    public void setNumberPage() {
        setNumPages(testDataHistoricArrayList.size());
    }

    private int calculatePosition() {
        int i = 0;
        for (Radiolog radiolog : testDataHistoricArrayList) {
            if (!valueTest.equals(radiolog.getId())) {
                i++;
            } else {
                break;
            }
        }
        return i;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return RadiologsDetailsFragment.newInstance(testDataHistoricArrayList.get(position).getId(), position > 0,
                    position + 1 < testDataHistoricArrayList.size());
        }

        @Override
        public int getCount() {
            return getNumPages();
        }
    }

}

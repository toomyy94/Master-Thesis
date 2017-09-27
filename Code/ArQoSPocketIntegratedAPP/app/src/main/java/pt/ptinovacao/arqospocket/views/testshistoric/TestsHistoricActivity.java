package pt.ptinovacao.arqospocket.views.testshistoric;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import pt.ptinovacao.arqospocket.menu.MainFragmentsActivity;

/**
 * Created by pedro on 12/04/2017.
 */
public class TestsHistoricActivity extends MainFragmentsActivity {

    @Override
    public void setPagerAdapter() {
        setPagerAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));
    }

    @Override
    public void setNumberPage() {
        setNumPages(2);
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new TestsHistoricListFragment();
                default:
                    return new TestHistoricMapFragment();
            }
        }

        @Override
        public int getCount() {
            return getNumPages();
        }
    }
}

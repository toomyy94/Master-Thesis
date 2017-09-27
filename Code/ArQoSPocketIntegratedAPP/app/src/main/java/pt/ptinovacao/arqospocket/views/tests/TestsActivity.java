package pt.ptinovacao.arqospocket.views.tests;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.menu.MainFragmentsActivity;

/**
 * Created by pedro on 12/04/2017.
 */
public class TestsActivity extends MainFragmentsActivity {
    private final static Logger LOGGER = LoggerFactory.getLogger(TestsActivity.class);

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
            return new TestsFragment();
        }

        @Override
        public int getCount() {
            return getNumPages();
        }
    }
}

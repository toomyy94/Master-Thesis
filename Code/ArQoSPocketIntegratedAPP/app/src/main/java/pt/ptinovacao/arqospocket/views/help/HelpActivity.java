package pt.ptinovacao.arqospocket.views.help;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import pt.ptinovacao.arqospocket.menu.MainFragmentsActivity;

/**
 * Created by pedro on 12/04/2017.
 */
public class HelpActivity extends MainFragmentsActivity {


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
                    return new HelpFragment();
        }

        @Override
        public int getCount() {
            return getNumPages();
        }
    }
}

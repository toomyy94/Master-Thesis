package pt.ptinovacao.arqospocket.views.radiologs;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.core.radiologs.RadiologsManager;
import pt.ptinovacao.arqospocket.menu.MainFragmentsActivity;
import pt.ptinovacao.arqospocket.persistence.models.Radiolog;

/**
 * Activity to collect a new snapshot.
 * <p>
 * Created by Tomás Rodrigues on 13/06/2017.
 */
public class RadiologsActivity extends MainFragmentsActivity implements RadiologsFragment.OnRadiologSendListener{

    private final static Logger LOGGER = LoggerFactory.getLogger(RadiologsActivity.class);

    @Override
    public void setPagerAdapter() {
        setPagerAdapter(new RadiologsActivity.ScreenSlidePagerAdapter(getSupportFragmentManager()));
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

            return new RadiologsFragment();
        }

        @Override
        public int getCount() {
            return getNumPages();
        }
    }

    @Override
    public void onRadiologSend(Radiolog snapshot) {

        RadiologsManager radiologsManager = RadiologsManager.getInstance(getArQosApplication());
        radiologsManager.reportRadiolog(snapshot);

        onBackPressed();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static RadiologsActivity getConvertActivity(Activity activity) {
        return (RadiologsActivity) activity;
    }
}

package pt.ptinovacao.arqospocket.views.tests.testsdetails;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import pt.ptinovacao.arqospocket.menu.MainFragmentsActivity;
import pt.ptinovacao.arqospocket.views.tests.TestsFragment;
import pt.ptinovacao.arqospocket.views.tests.TestsTabsTestFragment;

/**
 * Created by pedro on 20/04/2017.
 */
public class TestsDetailsActivity extends MainFragmentsActivity {

    public static final String KEY_TEST_TO_DETAILS = "KEY_TEST_TO_DETAILS";

    private Long valueTest;

    private TestsFragment.StateTest stateTest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        valueTest = getIntent().getLongExtra(KEY_TEST_TO_DETAILS, 0L);
        stateTest =
                (TestsFragment.StateTest) getIntent().getSerializableExtra(TestsTabsTestFragment.KEY_EXTRA_TO_STATE);
    }

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
            return TestsDetailsFragment.newInstance(valueTest, stateTest);
        }

        @Override
        public int getCount() {
            return getNumPages();
        }
    }
}

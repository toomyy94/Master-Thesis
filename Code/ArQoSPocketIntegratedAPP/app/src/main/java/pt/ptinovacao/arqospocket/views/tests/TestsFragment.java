package pt.ptinovacao.arqospocket.views.tests;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import pt.ptinovacao.arqospocket.ArQoSBaseFragment;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.notify.TaskProgress;
import pt.ptinovacao.arqospocket.core.notify.TestNotificationManager;
import pt.ptinovacao.arqospocket.core.notify.TestProgress;
import pt.ptinovacao.arqospocket.core.notify.TestProgressNotifier;
import pt.ptinovacao.arqospocket.persistence.ExecutingEventDao;
import pt.ptinovacao.arqospocket.persistence.OnDemandEventDao;
import pt.ptinovacao.arqospocket.persistence.ScheduledEventDao;

/**
 * TestsFragment
 * <p>
 * Created by pedro on 12/04/2017.
 */
public class TestsFragment extends ArQoSBaseFragment {

    private ViewPager viewPager;

    private LinearLayout llEvery;

    private LinearLayout llOngoing;

    private LinearLayout llInactive;

    private TextView tvAll;

    private TextView tvInactive;

    private TextView tvOngoing;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tests, container, false);

        initializeViews(rootView);
        onListeners();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        TestNotificationManager.register(getContext(), testProgressNotifier);

        updateOptionPage();
    }

    @Override
    public void onPause() {
        super.onPause();
        TestNotificationManager.unregister(getContext(), testProgressNotifier);
    }

    private TestProgressNotifier testProgressNotifier = new TestProgressNotifier() {
        @Override
        public void onTestExecutionStarted(TestProgress testProgress) {
            updateOptionPage();
        }

        @Override
        public void onTaskExecutionStarted(TaskProgress taskProgress) {
            updateOptionPage();
        }

        @Override
        public void onTaskExecutionFinished(TaskProgress taskProgress) {
            updateOptionPage();
        }

        @Override
        public void onTestExecutionFinished(TestProgress testProgress) {
            updateOptionPage();
        }

        @Override
        public void onTestDataChanged(TestProgress testProgress) {
            updateOptionPage();
        }
    };

    private void updateOptionPage() {

        OnDemandEventDao onDemandEventDao = getArQosApplication().getDatabaseHelper().createOnDemandEventDao();
        ExecutingEventDao executingEventDao = getArQosApplication().getDatabaseHelper().createExecutingEventDao();
        ScheduledEventDao scheduledEventDao = getArQosApplication().getDatabaseHelper().createScheduledEventDao();

        int all = onDemandEventDao.countAllOnDemandEvents();
        int ongoing = executingEventDao.countAllExecutingEvents();

        int inactive = scheduledEventDao.countAllScheduledEvent();

        tvAll.setText(String.valueOf(all + ongoing + inactive));
        tvInactive.setText(String.valueOf(onDemandEventDao.countInactiveOnDemandEvents() + inactive));
        tvOngoing.setText(String.valueOf(ongoing));
    }

    private void initializeViews(ViewGroup rootView) {

        viewPager = (ViewPager) rootView.findViewById(R.id.pager_tests);
        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        llEvery = (LinearLayout) rootView.findViewById(R.id.test_every);
        llOngoing = (LinearLayout) rootView.findViewById(R.id.tests_ongoing);
        llInactive = (LinearLayout) rootView.findViewById(R.id.tests_inative);

        tvAll = (TextView) rootView.findViewById(R.id.text_bola_todas);
        tvInactive = (TextView) rootView.findViewById(R.id.text_bola_inac);
        tvOngoing = (TextView) rootView.findViewById(R.id.text_bola_emcurso);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return TestsTabsTestFragment.newInstance(StateTest.ONGOIONG);
                case 2:
                    return TestsTabsTestFragment.newInstance(StateTest.INACTIVE);
                default:
                case 0:
                    return TestsTabsTestFragment.newInstance(StateTest.EVERY);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public enum StateTest {
        EVERY,
        ONGOIONG,
        INACTIVE,
        COMPLETED,
        FAILED
    }

    private void onListeners() {

        llEvery.setSelected(true);

        llEvery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llEvery.setSelected(true);
                llOngoing.setSelected(false);
                llInactive.setSelected(false);
                viewPager.setCurrentItem(0);
            }
        });
        llOngoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llEvery.setSelected(false);
                llOngoing.setSelected(true);
                llInactive.setSelected(false);
                viewPager.setCurrentItem(1);
            }
        });
        llInactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llEvery.setSelected(false);
                llOngoing.setSelected(false);
                llInactive.setSelected(true);
                viewPager.setCurrentItem(2);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        llEvery.setSelected(true);
                        llOngoing.setSelected(false);
                        llInactive.setSelected(false);
                        break;
                    case 1:
                        llEvery.setSelected(false);
                        llOngoing.setSelected(true);
                        llInactive.setSelected(false);
                        break;
                    case 2:
                        llEvery.setSelected(false);
                        llOngoing.setSelected(false);
                        llInactive.setSelected(true);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

}
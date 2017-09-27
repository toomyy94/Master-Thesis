package pt.ptinovacao.arqospocket.menu;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenuController;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pt.ptinovacao.arqospocket.ArQoSBaseActivity;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.alarms.AlarmsManager;
import pt.ptinovacao.arqospocket.core.radiologs.RadiologsManager;
import pt.ptinovacao.arqospocket.core.utils.SystemUtils;
import pt.ptinovacao.arqospocket.views.ArQosTextView;
import pt.ptinovacao.arqospocket.views.anomaly.AnomalyActivity;
import pt.ptinovacao.arqospocket.views.anomalyhistoric.AnomalyHistoricActivity;
import pt.ptinovacao.arqospocket.views.anomalyhistoric.anomalydetails.AnomalyDetailsActivity;
import pt.ptinovacao.arqospocket.views.dashboard.DashboardActivity;
import pt.ptinovacao.arqospocket.views.help.HelpActivity;
import pt.ptinovacao.arqospocket.views.radiologs.RadiologsActivity;
import pt.ptinovacao.arqospocket.views.radiologshistoric.RadiologsHistoricActivity;
import pt.ptinovacao.arqospocket.views.radiologshistoric.radiologsdetails.RadiologsDetailsActivity;
import pt.ptinovacao.arqospocket.views.settings.SettingsActivity;
import pt.ptinovacao.arqospocket.views.tests.TestsActivity;
import pt.ptinovacao.arqospocket.views.tests.testsdetails.TestsDetailsActivity;
import pt.ptinovacao.arqospocket.views.testshistoric.TestsHistoricActivity;
import pt.ptinovacao.arqospocket.views.testshistoric.testsdetails.TaskDetailsActivity;

public class MainFragmentsActivity extends ArQoSBaseActivity implements SlidingMenuController {

    private static final Class[] WHITE_LIST_ACTIVITIES_BACK =
            { TaskDetailsActivity.class, TestsDetailsActivity.class, AnomalyDetailsActivity.class, RadiologsDetailsActivity.class };

    private FragmentActionBar fragmentActionBar;

    private SlidingMenu slidingMenu;

    private LinearLayout dashboard;

    private LinearLayout anomaly;

    private LinearLayout anomalyHistory;

    private LinearLayout tests;

    private LinearLayout testsHistory;

    private LinearLayout radiologs;

    private LinearLayout radiologsHistory;

    private LinearLayout settings;

    private LinearLayout help;

    private int numPages = 0;

    private ViewPager pager;

    private PagerAdapter pagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setNumberPage();

        pager = (ViewPager) findViewById(R.id.view_pager_id);
        setPagerAdapter();
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPagerPageChangeListener());

        RadiologsManager.getInstance(getArQosApplication()).generatePeriodicRadiologs();
        AlarmsManager.getInstance(getArQosApplication()).generatePeriodicAlarms();

        createMenu();
        setOnClicksOfItemMenu();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        pager.clearOnPageChangeListeners();
        pager = null;
        pagerAdapter = null;
    }

    @Override
    public void onBackPressed() {
        if (slidingMenuIsOpen()) {
            onSlidingMenuToggle();
        } else if (pager.getCurrentItem() == 0) {
            redirectToPageInitial();
        } else {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }

    public void setPagerAdapter() {

    }

    public void setNumberPage() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        Single.just("su").subscribeOn(Schedulers.newThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                SystemUtils.requestPermission();
            }
        });
    }

    public static void startActivity(AppCompatActivity context, Class nameClass) {
        Intent intent = new Intent(context, nameClass);
        context.startActivity(intent);
        context.finish();
    }

    public boolean actionBarEnabled() {
        return true;
    }

    public void setActionBarTitle(CharSequence title) {
        fragmentActionBar.setActionBarTitle(title);
    }

    @Override
    public void onSlidingMenuToggle() {
        slidingMenu.toggle(true);
    }

    public PagerAdapter getPagerAdapter() {
        return pagerAdapter;
    }

    public ViewPager getPager() {
        return pager;
    }

    public void setPagerAdapter(PagerAdapter pagerAdapter) {
        this.pagerAdapter = pagerAdapter;
    }

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }

    protected boolean onViewPagerTouch(View view, MotionEvent event) {
        return false;
    }

    protected boolean slidingMenuIsOpen() {
        return slidingMenu.isMenuShowing();
    }

    private Boolean redirectToPageInitial() {

        Class homepageClass = MenuPreferenceHomepageUtils.getHomepage(getArQosApplication());

        if (this.getClass().equals(homepageClass) || isWhiteListActivitiesNotOnBack(this.getClass())) {
            super.onBackPressed();
            return false;
        } else {
            startActivity(MainFragmentsActivity.this, homepageClass);
            return true;
        }
    }

    private boolean isWhiteListActivitiesNotOnBack(Class classActivityNow) {
        for (Class classActivity : WHITE_LIST_ACTIVITIES_BACK) {
            if (classActivity.equals(classActivityNow)) {
                return true;
            }
        }
        return false;
    }

    private void createMenu() {

        fragmentActionBar =
                (FragmentActionBar) getSupportFragmentManager().findFragmentById(R.id.fragment_base_menu_action_bar);

        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);

        findViewById(R.id.view_pager_id).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (slidingMenuIsOpen()) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        onSlidingMenuToggle();
                    }
                    return true;
                }
                return onViewPagerTouch(v, event);
            }
        });

        slidingMenu.setShadowWidthRes(R.dimen.sliding_menu_shadow_width);
        slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        slidingMenu.setTouchModeBehind(SlidingMenu.TOUCHMODE_NONE);
        slidingMenu.setMenu(R.layout.sliding_menu);

        dashboard = (LinearLayout) findViewById(R.id.dashboard);
        anomaly = (LinearLayout) findViewById(R.id.anormaly);
        anomalyHistory = (LinearLayout) findViewById(R.id.anormaly_historic);
        tests = (LinearLayout) findViewById(R.id.tests);
        testsHistory = (LinearLayout) findViewById(R.id.tests_historic);
        radiologs = (LinearLayout) findViewById(R.id.radiologs);
        radiologsHistory = (LinearLayout) findViewById(R.id.radiologs_historic);
        settings = (LinearLayout) findViewById(R.id.settings);
        help = (LinearLayout) findViewById(R.id.help);

        setOnClicksOfItemMenu();

        if (!actionBarEnabled()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(fragmentActionBar).commit();
        } else {
            updatePageTitle(0);
        }

        slidingMenu.isMenuShowing();
        showHideActionBar(true);

        normalText();
    }

    private void setOnClicksOfItemMenu() {

        dashboard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(MainFragmentsActivity.this, DashboardActivity.class);
            }

        });

        anomaly.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(MainFragmentsActivity.this, AnomalyActivity.class);

            }
        });

        anomalyHistory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(MainFragmentsActivity.this, AnomalyHistoricActivity.class);
            }
        });

        tests.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(MainFragmentsActivity.this, TestsActivity.class);

            }
        });

        testsHistory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(MainFragmentsActivity.this, TestsHistoricActivity.class);
            }
        });

        radiologs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(MainFragmentsActivity.this, RadiologsActivity.class);

            }
        });

        radiologsHistory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(MainFragmentsActivity.this, RadiologsHistoricActivity.class);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(MainFragmentsActivity.this, SettingsActivity.class);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(MainFragmentsActivity.this, HelpActivity.class);
            }
        });
    }

    private void showHideActionBar(boolean show) {
        if (actionBarEnabled() && fragmentActionBar != null) {
            if (show) {
                getSupportFragmentManager().beginTransaction().show(fragmentActionBar).commit();
            } else {
                getSupportFragmentManager().beginTransaction().hide(fragmentActionBar).commit();
            }
        }
    }

    private void normalText() {
        ArQosTextView stvDashboard = (ArQosTextView) findViewById(R.id.dashboard_text);
        stvDashboard.setTypeface(null, Typeface.NORMAL);

        ArQosTextView stvAnomaly = (ArQosTextView) findViewById(R.id.anomaly_text);
        stvAnomaly.setTypeface(null, Typeface.NORMAL);

        ArQosTextView stvAnomalyHistoric = (ArQosTextView) findViewById(R.id.anomaly_historic_text);
        stvAnomalyHistoric.setTypeface(null, Typeface.NORMAL);

        ArQosTextView stvTests = (ArQosTextView) findViewById(R.id.tests_text);
        stvTests.setTypeface(null, Typeface.NORMAL);

        ArQosTextView stvHistoricTests = (ArQosTextView) findViewById(R.id.historic_tests_text);
        stvHistoricTests.setTypeface(null, Typeface.NORMAL);

        ArQosTextView stvRadiologs = (ArQosTextView) findViewById(R.id.radiologs_text);
        stvRadiologs.setTypeface(null, Typeface.NORMAL);

        ArQosTextView stvHistoricRadiologs = (ArQosTextView) findViewById(R.id.radiologs_historic_text);
        stvHistoricRadiologs.setTypeface(null, Typeface.NORMAL);

        ArQosTextView stvSettings = (ArQosTextView) findViewById(R.id.settings_text);
        stvSettings.setTypeface(null, Typeface.NORMAL);

        ArQosTextView stvHelp = (ArQosTextView) findViewById(R.id.help_text);
        stvHelp.setTypeface(null, Typeface.NORMAL);
    }

    private void updatePageTitle(int position) {
        CharSequence title = pagerAdapter.getPageTitle(position);
        if (title != null) {
            setActionBarTitle(title);
        } else {
            setActionBarTitle(getString(R.string.app_name));
        }
    }

    private class ViewPagerPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            updatePageTitle(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
package pt.ptinovacao.arqospocket.views.testshistoric.testsdetails;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import pt.ptinovacao.arqospocket.menu.MainFragmentsActivity;
import pt.ptinovacao.arqospocket.persistence.models.ExecutingEvent;
import pt.ptinovacao.arqospocket.views.tests.testsdetails.TestsDetailsActivity;

/**
 * Created by pedro on 20/04/2017.
 */
public class TaskDetailsActivity extends MainFragmentsActivity {

    private final static Logger LOGGER = LoggerFactory.getLogger(TaskDetailsActivity.class);

    private Long valueTest;

    private ArrayList<ExecutingEvent> testDataHistoricArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        testDataHistoricArrayList = new ArrayList<>();

        getArQosApplication().getDatabaseHelper().createExecutingEventDao()
                .readAllExecutedEventsByConnectionTechnology(0).subscribe(new Consumer<ExecutingEvent>() {
            @Override
            public void accept(@NonNull ExecutingEvent executingEvent) throws Exception {
                testDataHistoricArrayList.add(executingEvent);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                LOGGER.debug(throwable.getMessage());
            }
        });

        super.onCreate(savedInstanceState);

        valueTest = getIntent().getLongExtra(TestsDetailsActivity.KEY_TEST_TO_DETAILS, 0L);

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

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TaskDetailsFragment
                    .newInstance(valueTest, position > 0, position + 1 < testDataHistoricArrayList.size());
        }

        @Override
        public int getCount() {
            return getNumPages();
        }
    }

    private int calculatePosition() {
        int i = 0;
        for (ExecutingEvent executingEvent : testDataHistoricArrayList) {
            if (!valueTest.equals(executingEvent.getId())) {
                i++;
            } else {
                break;
            }
        }
        return i;
    }
}

package pt.ptinovacao.arqospocket.views.tests;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import pt.ptinovacao.arqospocket.ArQoSBaseActivity;
import pt.ptinovacao.arqospocket.ArQoSBaseFragment;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.notify.TaskProgress;
import pt.ptinovacao.arqospocket.core.notify.TestNotificationManager;
import pt.ptinovacao.arqospocket.core.notify.TestProgress;
import pt.ptinovacao.arqospocket.core.notify.TestProgressNotifier;
import pt.ptinovacao.arqospocket.persistence.ExecutingEventDao;
import pt.ptinovacao.arqospocket.persistence.OnDemandEventDao;
import pt.ptinovacao.arqospocket.persistence.ScheduledEventDao;
import pt.ptinovacao.arqospocket.persistence.models.BaseEvent;
import pt.ptinovacao.arqospocket.persistence.models.ExecutingEvent;
import pt.ptinovacao.arqospocket.persistence.models.OnDemandEvent;
import pt.ptinovacao.arqospocket.persistence.models.ScheduledEvent;

/**
 * TestsTabsTestFragment.
 * <p>
 * Created by pedro on 13/04/2017.
 */
public class TestsTabsTestFragment extends ArQoSBaseFragment {

    private final static Logger LOGGER = LoggerFactory.getLogger(TestsTabsTestFragment.class);

    private TestsAdapter adapter;

    public final static String KEY_EXTRA_TO_STATE = "KEY_EXTRA_TO_STATE";

    private ArrayList<BaseEvent> testDataArrayList;

    private TestsFragment.StateTest stateTest;

    public static TestsTabsTestFragment newInstance(TestsFragment.StateTest stateTest) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_EXTRA_TO_STATE, stateTest);

        TestsTabsTestFragment fragment = new TestsTabsTestFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stateTest = (TestsFragment.StateTest) getArguments().getSerializable(KEY_EXTRA_TO_STATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_every_tests, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        testDataArrayList = new ArrayList<>();

        adapter = new TestsAdapter((ArQoSBaseActivity) getActivity(), testDataArrayList);
        recyclerView.setAdapter(adapter);

        setValues();
        return rootView;
    }

    private void setValues() {

        if (testDataArrayList == null) {
            testDataArrayList = new ArrayList<>();
        } else {
            testDataArrayList.clear();
            adapter.notifyDataSetChanged();
        }

        OnDemandEventDao onDemandEventDao = getArQosApplication().getDatabaseHelper().createOnDemandEventDao();
        ExecutingEventDao executingEventDao = getArQosApplication().getDatabaseHelper().createExecutingEventDao();
        ScheduledEventDao scheduledEventDao = getArQosApplication().getDatabaseHelper().createScheduledEventDao();

        switch (stateTest) {
            case EVERY:
                onDemandEventDao.readAllOnDemandEvents().subscribe(new Consumer<OnDemandEvent>() {
                    @Override
                    public void accept(@NonNull OnDemandEvent onDemandEvent) throws Exception {
                        testDataArrayList.add(onDemandEvent);
                    }
                });

                executingEventDao.readAllExecutingEvents().subscribe(new Consumer<ExecutingEvent>() {
                    @Override
                    public void accept(@NonNull ExecutingEvent executingEvent) throws Exception {
                        testDataArrayList.add(executingEvent);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LOGGER.error(throwable.getMessage());
                    }
                });

                scheduledEventDao.readAllScheduledEventForTests().subscribe(new Consumer<ScheduledEvent>() {
                    @Override
                    public void accept(@NonNull ScheduledEvent scheduledEvent) throws Exception {
                        testDataArrayList.add(scheduledEvent);
                    }
                });

                break;
            case ONGOIONG:

                executingEventDao.readAllExecutingEvents().subscribe(new Consumer<ExecutingEvent>() {
                    @Override
                    public void accept(@NonNull ExecutingEvent executingEvent) throws Exception {
                        testDataArrayList.add(executingEvent);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LOGGER.error(throwable.getMessage());
                    }
                });

                break;
            case INACTIVE:

                onDemandEventDao.readInactiveOnDemandEvents().subscribe(new Consumer<OnDemandEvent>() {
                    @Override
                    public void accept(@NonNull OnDemandEvent onDemandEvent) throws Exception {
                        testDataArrayList.add(onDemandEvent);
                    }
                });

                scheduledEventDao.readAllScheduledEvent().subscribe(new Consumer<ScheduledEvent>() {
                    @Override
                    public void accept(@NonNull ScheduledEvent scheduledEvent) throws Exception {
                        testDataArrayList.add(scheduledEvent);
                    }
                });

                break;
        }
    }

    private TestProgressNotifier testProgressNotifier = new TestProgressNotifier() {
        @Override
        public void onTestExecutionStarted(TestProgress testProgress) {
            setValues();
        }

        @Override
        public void onTaskExecutionStarted(TaskProgress taskProgress) {
            setValues();
        }

        @Override
        public void onTaskExecutionFinished(TaskProgress taskProgress) {
            setValues();
        }

        @Override
        public void onTestExecutionFinished(TestProgress testProgress) {
            setValues();
        }

        @Override
        public void onTestDataChanged(TestProgress testProgress) {
            setValues();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        TestNotificationManager.register(getContext(), testProgressNotifier);
        setValues();
    }

    @Override
    public void onPause() {
        super.onPause();
        TestNotificationManager.unregister(getContext(), testProgressNotifier);
    }
}

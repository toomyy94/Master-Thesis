package pt.ptinovacao.arqospocket.views.tests.testsdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import pt.ptinovacao.arqospocket.ArQoSBaseActivity;
import pt.ptinovacao.arqospocket.ArQoSBaseFragment;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.TestExecutor;
import pt.ptinovacao.arqospocket.core.TestParser;
import pt.ptinovacao.arqospocket.core.notify.TaskProgress;
import pt.ptinovacao.arqospocket.core.notify.TestNotificationManager;
import pt.ptinovacao.arqospocket.core.notify.TestProgress;
import pt.ptinovacao.arqospocket.core.notify.TestProgressNotifier;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TestData;
import pt.ptinovacao.arqospocket.persistence.ExecutingEventDao;
import pt.ptinovacao.arqospocket.persistence.OnDemandEventDao;
import pt.ptinovacao.arqospocket.persistence.ScheduledEventDao;
import pt.ptinovacao.arqospocket.persistence.models.BaseEvent;
import pt.ptinovacao.arqospocket.persistence.models.ExecutingEvent;
import pt.ptinovacao.arqospocket.persistence.models.OnDemandEvent;
import pt.ptinovacao.arqospocket.persistence.models.ScheduledEvent;
import pt.ptinovacao.arqospocket.utils.CalculateStateTestsToResourceUtils;
import pt.ptinovacao.arqospocket.views.tests.TestsFragment;
import pt.ptinovacao.arqospocket.views.tests.TestsTabsTestFragment;

/**
 * TestsDetailsFragment.
 * <p>
 * Created by pedro on 20/04/2017.
 */
public class TestsDetailsFragment extends ArQoSBaseFragment {

    private final static Logger LOGGER = LoggerFactory.getLogger(TestsDetailsFragment.class);

    private RecyclerView recyclerView;

    private ArrayList<TaskData> testDataHistoricArrayList;

    private ImageView ivPlay;

    private ImageView ivPercentage;

    private ImageView ivTypeTest;

    private TextView tvPercentage;

    private TextView tvName;

    private Long valueOfTest;

    private TestsFragment.StateTest stateTest;

    private BaseEvent baseEvent;

    public static TestsDetailsFragment newInstance(Long eventId, TestsFragment.StateTest stateTest) {
        Bundle args = new Bundle();
        args.putLong(TestsDetailsActivity.KEY_TEST_TO_DETAILS, eventId);
        args.putSerializable(TestsTabsTestFragment.KEY_EXTRA_TO_STATE, stateTest);

        TestsDetailsFragment fragment = new TestsDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        valueOfTest = getArguments().getLong(TestsDetailsActivity.KEY_TEST_TO_DETAILS);
        stateTest = (TestsFragment.StateTest) getArguments().getSerializable(TestsTabsTestFragment.KEY_EXTRA_TO_STATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_testes_detalhes, container, false);

        initializeView(rootView);
        createRecyclerView();
        callCore();

        initializeListeners();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        TestNotificationManager.register(getContext(), testProgressNotifier);
    }

    @Override
    public void onPause() {
        super.onPause();
        TestNotificationManager.unregister(getContext(), testProgressNotifier);
    }

    private TestProgressNotifier testProgressNotifier = new TestProgressNotifier() {
        @Override
        public void onTestExecutionStarted(TestProgress testProgress) {
            callCore();
        }

        @Override
        public void onTaskExecutionStarted(TaskProgress taskProgress) {
            callCore();
        }

        @Override
        public void onTaskExecutionFinished(TaskProgress taskProgress) {
            callCore();
        }

        @Override
        public void onTestExecutionFinished(TestProgress testProgress) {
            callCore();
        }

        @Override
        public void onTestDataChanged(TestProgress testProgress) {
            callCore();
        }
    };

    private void initializeView(ViewGroup rootView) {

        ivPlay = (ImageView) rootView.findViewById(R.id.botPlay);

        ivPercentage = (ImageView) rootView.findViewById(R.id.imagepercent_deta);
        ivTypeTest = (ImageView) rootView.findViewById(R.id.tipodeteste);
        tvPercentage = (TextView) rootView.findViewById(R.id.textpercent_deta);
        tvName = (TextView) rootView.findViewById(R.id.text_nome_deta);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
    }

    private void setValues() {

        CalculateStateTestsToResourceUtils calculateStateTestsToResourceUtils;
        if (isExecuting()) {
            calculateStateTestsToResourceUtils =
                    new CalculateStateTestsToResourceUtils(TestsFragment.StateTest.ONGOIONG);
        } else {
            calculateStateTestsToResourceUtils =
                    new CalculateStateTestsToResourceUtils(TestsFragment.StateTest.INACTIVE);
        }

        TestParser testParser = new TestParser();
        TestData testData = testParser.parseSingleTest(baseEvent.getTestData());

        ivPercentage.setImageResource(calculateStateTestsToResourceUtils.getIntResourceImage());
        tvPercentage.setTextColor(
                ContextCompat.getColor(getContext(), calculateStateTestsToResourceUtils.getIntResourceColor()));
        tvPercentage
                .setText(getActivity().getString(calculateStateTestsToResourceUtils.getIntResourceStringPercentage()));
        tvName.setText(testData.getTestName());
        tvName.setTextColor(
                ContextCompat.getColor(getContext(), calculateStateTestsToResourceUtils.getIntResourceColor()));

        if (testData.getTestType() == 7) {
            ivTypeTest.setBackgroundResource(R.mipmap.icon_on_demand_litle);
        } else {
            ivTypeTest.setBackgroundResource(R.mipmap.icon_agendado);
        }

        testDataHistoricArrayList.clear();

        Collections.addAll(testDataHistoricArrayList, testData.getTasksData());
        initializeListeners();
    }

    @SuppressWarnings("SimplifiableIfStatement")
    private boolean isExecuting() {
        if (baseEvent == null) {
            return false;
        }

        if (baseEvent instanceof OnDemandEvent && ((OnDemandEvent) baseEvent).isExecuting()) {
            return true;
        }

        if (baseEvent instanceof ExecutingEvent && ((ExecutingEvent) baseEvent).isExecuting()) {
            return true;
        }

        return stateTest == TestsFragment.StateTest.INACTIVE;
    }

    private boolean isNotExecutable() {
        return baseEvent != null && (baseEvent instanceof ExecutingEvent || baseEvent instanceof ScheduledEvent);

    }

    private void initializeListeners() {
        if (isExecuting()) {
            ivPlay.setImageResource(R.mipmap.bot_play_indisponivel);
            ivPlay.setOnClickListener(null);
        } else {
            ivPlay.setImageResource(R.mipmap.bot_play);
            if (!isNotExecutable()) {
                ivPlay.setOnClickListener(new ExecuteTaskOnClickListener());
            }
        }
    }

    private class ExecuteTaskOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            ScheduledEventDao scheduledEventDao = getArQosApplication().getDatabaseHelper().createScheduledEventDao();

            TestParser testParser = new TestParser();
            TestData testData = testParser.parseSingleTest(baseEvent.getTestData());

            long sumEndDate = Calendar.getInstance().getTimeInMillis();

            if (testData != null) {
                try {
                    for (TaskData taskData : testData.getTasksData()) {
                        sumEndDate += (Integer.parseInt(taskData.getTimeout()) * 1000);
                    }
                } catch (NumberFormatException e) {
                    LOGGER.debug("Error parse date");
                }
            }

            if (TestExecutor.getInstance(getArQosApplication()).isAnyTestExecuting() ||
                    scheduledEventDao.isAnyScheduledTestBeforeDate(sumEndDate)) {
                AlertTestsDialog alertTestsDialog = new AlertTestsDialog(getActivity());
                alertTestsDialog.show();
                return;
            }

            if (baseEvent != null) {
                getArQosApplication().getOnDemandTestManager().executeOnDemand(valueOfTest, false);
                ivPlay.setImageResource(R.mipmap.bot_play_indisponivel);
                ivPlay.setOnClickListener(null);
            }
        }
    }

    private void callCore() {
        ExecutingEventDao executingEventDao = getArQosApplication().getDatabaseHelper().createExecutingEventDao();
        OnDemandEventDao onDemandEventDao = getArQosApplication().getDatabaseHelper().createOnDemandEventDao();
        ScheduledEventDao scheduledEventDao = getArQosApplication().getDatabaseHelper().createScheduledEventDao();

        switch (stateTest) {
            case EVERY:
                onDemandEventDao.readOnDemandEvent(valueOfTest).subscribe(new Consumer<OnDemandEvent>() {
                    @Override
                    public void accept(@NonNull OnDemandEvent onDemandEvent) throws Exception {
                        baseEvent = onDemandEvent;
                        setValues();
                    }
                });
                break;
            case ONGOIONG:

                executingEventDao.readExecutingEvent(valueOfTest).subscribe(new Consumer<ExecutingEvent>() {
                    @Override
                    public void accept(@NonNull ExecutingEvent onDemandEvent) throws Exception {
                        baseEvent = onDemandEvent;
                        setValues();
                    }
                });

                break;
            case INACTIVE:
                baseEvent = onDemandEventDao.readInactiveEvent(valueOfTest).firstOrNull();

                if (baseEvent != null) {
                    setValues();
                }

                scheduledEventDao.readScheduledEvent(valueOfTest).subscribe(new Consumer<ScheduledEvent>() {
                    @Override
                    public void accept(@NonNull ScheduledEvent scheduledEvent) throws Exception {
                        baseEvent = scheduledEvent;
                        setValues();
                    }
                });

                break;
        }

    }

    private void createRecyclerView() {
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        testDataHistoricArrayList = new ArrayList<>();

        TasksAdapter adapter =
                new TasksAdapter((ArQoSBaseActivity) getActivity(), stateTest, testDataHistoricArrayList);
        recyclerView.setAdapter(adapter);
    }
}

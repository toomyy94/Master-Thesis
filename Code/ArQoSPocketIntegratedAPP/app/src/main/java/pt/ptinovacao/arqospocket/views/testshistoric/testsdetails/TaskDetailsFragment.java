package pt.ptinovacao.arqospocket.views.testshistoric.testsdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import pt.ptinovacao.arqospocket.ArQoSBaseActivity;
import pt.ptinovacao.arqospocket.ArQoSBaseFragment;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.TestParser;
import pt.ptinovacao.arqospocket.core.network.ConnectionTechnology;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.TestData;
import pt.ptinovacao.arqospocket.core.tests.data.TestResult;
import pt.ptinovacao.arqospocket.core.utils.DateUtils;
import pt.ptinovacao.arqospocket.persistence.ExecutingEventDao;
import pt.ptinovacao.arqospocket.persistence.models.BaseTaskEvent;
import pt.ptinovacao.arqospocket.persistence.models.ExecutingEvent;
import pt.ptinovacao.arqospocket.utils.CalculateStateTestsToResourceUtils;
import pt.ptinovacao.arqospocket.views.anomalyhistoric.anomalydetails.AnomalyDetailsFragment;
import pt.ptinovacao.arqospocket.views.tests.TestsFragment;
import pt.ptinovacao.arqospocket.views.tests.testsdetails.TestsDetailsActivity;
import pt.ptinovacao.arqospocket.views.testshistoric.TestsHistoricAdapter;

/**
 * Created by pedro on 20/04/2017.
 */
public class TaskDetailsFragment extends ArQoSBaseFragment implements OnMapReadyCallback {

    private final static Logger LOGGER = LoggerFactory.getLogger(ArQoSBaseActivity.class);

    private RecyclerView recyclerView;

    private TasksAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<TaskResult> testDataHistoricArrayList;

    private GoogleMap googleMap;

    private TextView tvTitle, tvDate;

    private ExecutingEvent executingEvent;

    private Long valueOfTest;

    private boolean activeLeft, activeRight;

    private ImageView swipeLeft;

    private ImageView swipeRight;

    public static TaskDetailsFragment newInstance(Long eventId, boolean activeLeft, boolean activeRight) {
        Bundle args = new Bundle();
        args.putLong(TestsDetailsActivity.KEY_TEST_TO_DETAILS, eventId);
        args.putBoolean(AnomalyDetailsFragment.KEY_SWIPE_LEFT, activeLeft);
        args.putBoolean(AnomalyDetailsFragment.KEY_SWIPE_RIGHT, activeRight);

        TaskDetailsFragment fragment = new TaskDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        valueOfTest = getArguments().getLong(TestsDetailsActivity.KEY_TEST_TO_DETAILS);
        activeLeft = getArguments().getBoolean(AnomalyDetailsFragment.KEY_SWIPE_LEFT);
        activeRight = getArguments().getBoolean(AnomalyDetailsFragment.KEY_SWIPE_RIGHT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tests_historic_details, container, false);

        initializeView(rootView);
        initializeMap(rootView, savedInstanceState);
        createRecyclerView();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!activeLeft) {
            swipeLeft.setVisibility(View.GONE);
        }

        if (!activeRight) {
            swipeRight.setVisibility(View.GONE);
        }
    }

    private void initializeMap(ViewGroup rootView, Bundle savedInstanceState) {
        MapView mapView = (MapView) rootView.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    private void setValues() {

        final TestParser testParser = new TestParser();
        final TestResult testResult = testParser.parseSingleResult(executingEvent.getResultData());
        final TestData testData = testParser.parseSingleTest(executingEvent.getTestData());

        tvDate.setText(DateUtils.convertDateToString(testResult.getEndDate()));
        tvTitle.setText(testData.getTestName());

        if (testResult != null) {
            for (TaskResult taskResult : testResult.getTaskResults()) {
                testDataHistoricArrayList.add(taskResult);
            }
        }

        ExecutingEventDao executingEventDao = getArQosApplication().getDatabaseHelper().createExecutingEventDao();
        BaseTaskEvent baseTaskEvent = executingEventDao.readExecutingEventFirstOrNull(valueOfTest);

        if (baseTaskEvent != null) {
            ConnectionTechnology connectionTechnology =
                    ConnectionTechnology.fromConnectionTechnology(baseTaskEvent.getConnectionTechnology());

            int imageBackground = CalculateStateTestsToResourceUtils.getTestBigRowIconResource(connectionTechnology,
                    !TestsHistoricAdapter.isTaskDataWithSuccess(testResult));

            tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, imageBackground, 0, 0);
        }
        addPointsToMap();
    }

    private void initializeView(ViewGroup rootView) {

        tvTitle = (TextView) rootView.findViewById(R.id.teste_entry_title);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        tvDate = (TextView) rootView.findViewById(R.id.test_entry_date);
        swipeLeft = (ImageView) rootView.findViewById(R.id.bt_swipe_left);
        swipeRight = (ImageView) rootView.findViewById(R.id.bt_swipe_right);

    }

    private void createRecyclerView() {
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        testDataHistoricArrayList = new ArrayList<>();

        adapter = new TasksAdapter((ArQoSBaseActivity) getActivity(), TestsFragment.StateTest.COMPLETED,
                testDataHistoricArrayList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        this.googleMap.setMyLocationEnabled(true);

        loadValues();
    }

    private void loadValues() {
        getArQosApplication().getDatabaseHelper().createExecutingEventDao().readExecutedEvent(valueOfTest)
                .subscribe(new Consumer<ExecutingEvent>() {
                    @Override
                    public void accept(@NonNull ExecutingEvent executingEvent) throws Exception {

                        TaskDetailsFragment.this.executingEvent = executingEvent;
                        setValues();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LOGGER.error("Error: ", throwable);
                    }
                });
    }

    private void addPointsToMap() {
        final TestParser testParser = new TestParser();

        if (googleMap == null) {
            return;
        }
        googleMap.clear();

        final TestResult testResult = testParser.parseSingleResult(executingEvent.getResultData());

        BaseTaskEvent baseTaskEvent = getArQosApplication().getDatabaseHelper().createExecutingEventDao()
                .readExecutingEventFirstOrNull(executingEvent.getId());
        TestData testData = testParser.parseSingleTest(executingEvent.getTestData());

        for (TaskResult taskResult : testResult.getTaskResults()) {

            MarkerOptions marker =
                    new MarkerOptions().position(new LatLng(baseTaskEvent.getLatitude(), baseTaskEvent.getLongitude()))
                            .title(testData.getTestName());

            ConnectionTechnology connectionTechnology =
                    ConnectionTechnology.fromConnectionTechnology(baseTaskEvent.getConnectionTechnology());

            int valueResource = CalculateStateTestsToResourceUtils.getTestPinResource(connectionTechnology, true,
                    BaseExecutableTask.RESULT_TASK_SUCCESS.equals(taskResult.getStatus()));
            if (valueResource != -1) {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(valueResource);
                marker.icon(icon);
            }
            googleMap.addMarker(marker);

            LatLng latLng = new LatLng(baseTaskEvent.getLatitude(), baseTaskEvent.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }

    }

}

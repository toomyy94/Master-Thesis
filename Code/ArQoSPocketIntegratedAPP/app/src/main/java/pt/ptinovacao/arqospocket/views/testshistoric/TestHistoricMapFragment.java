package pt.ptinovacao.arqospocket.views.testshistoric;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pt.ptinovacao.arqospocket.ArQoSBaseFragment;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.TestParser;
import pt.ptinovacao.arqospocket.core.network.ConnectionTechnology;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.data.TestData;
import pt.ptinovacao.arqospocket.core.tests.data.TestResult;
import pt.ptinovacao.arqospocket.persistence.ExecutingEventDao;
import pt.ptinovacao.arqospocket.persistence.models.BaseTaskEvent;
import pt.ptinovacao.arqospocket.persistence.models.ExecutingEvent;
import pt.ptinovacao.arqospocket.utils.CalculateStateTestsToResourceUtils;

/**
 * AnomalyHistoricMapFragment.
 * <p>
 * Created by pedro on 13/04/2017.
 */
public class TestHistoricMapFragment extends ArQoSBaseFragment implements OnMapReadyCallback {

    private final static Logger LOGGER = LoggerFactory.getLogger(TestHistoricMapFragment.class);

    private Spinner spinnerFilter;

    private GoogleMap gmLocationTest;

    private ImageView ivButtonZoom;

    private LinearLayout llOccurrence;

    private PinView pinViewMobile;

    private PinView pinViewWifi;

    private PinView pinViewCoverage;

    private ExecutingEventDao executingEventDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        executingEventDao = getArQosApplication().getDatabaseHelper().createExecutingEventDao();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_map_historic_tests, container, false);

        initializeViews(rootView);
        initializeListeners();
        initializeMap(rootView, savedInstanceState);

        setValuesOfPinView(ConnectionTechnology.NA);

        createSpinner();
        return rootView;
    }

    private void initializeViews(ViewGroup rootView) {

        ivButtonZoom = (ImageView) rootView.findViewById(R.id.icon_zoom_mapa);
        llOccurrence = (LinearLayout) rootView.findViewById(R.id.above_map_layout);
        spinnerFilter = (Spinner) rootView.findViewById(R.id.spinner_filter);
        pinViewMobile = (PinView) rootView.findViewById(R.id.pinLegenda_rede);
        pinViewWifi = (PinView) rootView.findViewById(R.id.pinLegenda_wifi);
        pinViewCoverage = (PinView) rootView.findViewById(R.id.pinLegenda_misto);
    }

    private void initializeListeners() {

        ivButtonZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llOccurrence.getVisibility() == View.GONE) {
                    llOccurrence.setVisibility(View.VISIBLE);
                } else {
                    llOccurrence.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initializeMap(ViewGroup rootView, Bundle savedInstanceState) {
        MapView mapView = (MapView) rootView.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    private synchronized void loadValues(final ConnectionTechnology connectionTechnology) {
        Single.fromCallable(new Callable<List<ExecutingEvent>>() {
            @Override
            public List<ExecutingEvent> call() throws Exception {
                return getArQosApplication().getDatabaseHelper().createExecutingEventDao()
                        .listAllExecutedEventsByConnectionTechnology(connectionTechnology.getValue());
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ExecutingEvent>>() {
                    @Override
                    public void accept(@NonNull List<ExecutingEvent> executingEvents) throws Exception {
                        addPointsToMap(executingEvents);
                        setValuesOfPinView(connectionTechnology);
                    }
                });
    }

    private void setValuesOfPinView(ConnectionTechnology valuesOfPinView) {
        final String valueZero = "0";

        switch (valuesOfPinView) {
            case MOBILE:
                pinViewMobile.setLabel(String.valueOf(executingEventDao
                        .countExecutedEventsByConnectionTechnology(ConnectionTechnology.MOBILE.getValue())));
                pinViewWifi.setLabel(valueZero);
                pinViewCoverage.setLabel(valueZero);
                break;
            case WIFI:
                pinViewMobile.setLabel(valueZero);
                pinViewWifi.setLabel(String.valueOf(executingEventDao
                        .countExecutedEventsByConnectionTechnology(ConnectionTechnology.WIFI.getValue())));
                pinViewCoverage.setLabel(valueZero);
                break;
            case NA:
                pinViewMobile.setLabel(String.valueOf(executingEventDao
                        .countExecutedEventsByConnectionTechnology(ConnectionTechnology.MOBILE.getValue())));
                pinViewWifi.setLabel(String.valueOf(executingEventDao
                        .countExecutedEventsByConnectionTechnology(ConnectionTechnology.WIFI.getValue())));
                pinViewCoverage.setLabel(String.valueOf(executingEventDao
                        .countExecutedEventsByConnectionTechnology(ConnectionTechnology.MIXED.getValue())));
                break;
        }
    }

    private void addPointsToMap(List<ExecutingEvent> executingEvents) {
        TestParser testParser = new TestParser();

        if (gmLocationTest == null) {
            return;
        }

        gmLocationTest.clear();

        for (ExecutingEvent executingEvent : executingEvents) {
            final TestResult testResult = testParser.parseSingleResult(executingEvent.getResultData());
            final TestData testData = testParser.parseSingleTest(executingEvent.getTestData());

            BaseTaskEvent baseTaskEvent = getArQosApplication().getDatabaseHelper().createExecutingEventDao()
                    .readExecutingEventFirstOrNull(executingEvent.getId());

            if (baseTaskEvent != null) {
                MarkerOptions marker = new MarkerOptions()
                        .position(new LatLng(baseTaskEvent.getLatitude(), baseTaskEvent.getLongitude()))
                        .title(testData.getTestName());

                ConnectionTechnology connectionTechnology =
                        ConnectionTechnology.fromConnectionTechnology(baseTaskEvent.getConnectionTechnology());

                int valueResource = CalculateStateTestsToResourceUtils.getTestPinResource(connectionTechnology, true,
                        BaseExecutableTask.RESULT_TASK_SUCCESS.equals(testResult.getTaskResults()[0].getStatus()));
                if (valueResource != -1) {
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(valueResource);
                    marker.icon(icon);
                }
                gmLocationTest.addMarker(marker);
                LatLng latLng = new LatLng(baseTaskEvent.getLatitude(), baseTaskEvent.getLongitude());
                gmLocationTest.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                gmLocationTest.moveCamera(CameraUpdateFactory.zoomTo(14));
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gmLocationTest = googleMap;

        this.gmLocationTest.getUiSettings().setMyLocationButtonEnabled(true);
        this.gmLocationTest.setMyLocationEnabled(true);

        loadValues(ConnectionTechnology.NA);
    }

    private void createSpinner() {

        ArrayList<SpinnerRowItem> dialogItems = new ArrayList<>();
        List<String> stringOfRows = Arrays.asList(getResources().getStringArray(R.array.testes));
        SpinnerFilterTestsUtils.createSpinner(getContext(), spinnerFilter, stringOfRows, dialogItems,
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        loadValues(SpinnerFilterTestsUtils.parsePosition(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
    }
}
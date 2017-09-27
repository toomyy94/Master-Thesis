package pt.ptinovacao.arqospocket.views.anomalyhistoric;

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

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pt.ptinovacao.arqospocket.ArQoSBaseFragment;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.persistence.AnomalyDao;
import pt.ptinovacao.arqospocket.persistence.models.Anomaly;
import pt.ptinovacao.arqospocket.views.anomaly.data.AnomalyReport;
import pt.ptinovacao.arqospocket.views.testshistoric.PinView;

/**
 * AnomalyHistoricMapFragment.
 * <p>
 * Created by pedro on 13/04/2017.
 */
public class AnomalyHistoricMapFragment extends ArQoSBaseFragment implements OnMapReadyCallback {

    private final static Logger LOGGER = LoggerFactory.getLogger(AnomalyHistoricMapFragment.class);

    private Spinner spinnerFilter;

    private GoogleMap gmLocationTest;

    private ImageView ivButtonZoom;

    private LinearLayout llOccurrence;

    private PinView pinViewVoice;

    private PinView pinViewInternet;

    private PinView pinViewMessaging;

    private PinView pinViewCobertura;

    private PinView pinViewOther;

    private AnomalyDao anomalyDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        anomalyDao = getArQosApplication().getDatabaseHelper().createAnomalyDao();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_anomalias_historic_map, container, false);

        initializeViews(rootView);
        initializeListeners();
        initializeMap(rootView, savedInstanceState);

        setValuesOfPinView(AnomalyReport.ALL);

        createSpinner();
        return rootView;
    }

    private void initializeViews(ViewGroup rootView) {

        ivButtonZoom = (ImageView) rootView.findViewById(R.id.icon_zoom_mapa);
        llOccurrence = (LinearLayout) rootView.findViewById(R.id.above_map_layout);
        spinnerFilter = (Spinner) rootView.findViewById(R.id.spinner_filter);

        pinViewVoice = (PinView) rootView.findViewById(R.id.pinLegenda_voz);
        pinViewInternet = (PinView) rootView.findViewById(R.id.pinLegenda_internet);
        pinViewMessaging = (PinView) rootView.findViewById(R.id.pinLegenda_messaging);
        pinViewCobertura = (PinView) rootView.findViewById(R.id.pinLegenda_cobertura);
        pinViewOther = (PinView) rootView.findViewById(R.id.pinLegenda_outra);
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

    private synchronized void loadValues(final AnomalyReport anomalyReport) {

        Single.fromCallable(new Callable<List<Anomaly>>() {
            @Override
            public List<Anomaly> call() throws Exception {
                return getArQosApplication().getDatabaseHelper().createAnomalyDao().readAnomaliesByType(
                        AnomalyReport.ALL == anomalyReport ? null :
                                getContext().getResources().getString(anomalyReport.getResourceText()));
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Anomaly>>() {
                    @Override
                    public void accept(@NonNull List<Anomaly> executingEvents) throws Exception {

                        addPointsToMap(executingEvents);
                        setValuesOfPinView(anomalyReport);
                    }
                });
    }

    private void setValuesOfPinView(AnomalyReport valuesOfPinView) {
        final String valueZero = "0";

        switch (valuesOfPinView) {
            case ALL:
                pinViewVoice.setLabel(String.valueOf(anomalyDao.countAnomaliesByType(
                        getContext().getResources().getString(AnomalyReport.VOICE.getResourceText()))));
                pinViewOther.setLabel(String.valueOf(anomalyDao.countAnomaliesByType(
                        getContext().getResources().getString(AnomalyReport.OTHER.getResourceText()))));
                pinViewMessaging.setLabel(String.valueOf(anomalyDao.countAnomaliesByType(
                        getContext().getResources().getString(AnomalyReport.MESSAGING.getResourceText()))));
                pinViewInternet.setLabel(String.valueOf(anomalyDao.countAnomaliesByType(
                        getContext().getResources().getString(AnomalyReport.INTERNET.getResourceText()))));
                pinViewCobertura.setLabel(String.valueOf(anomalyDao.countAnomaliesByType(
                        getContext().getResources().getString(AnomalyReport.COVERAGE.getResourceText()))));
                break;
            case COVERAGE:
                pinViewVoice.setLabel(valueZero);
                pinViewInternet.setLabel(valueZero);
                pinViewMessaging.setLabel(valueZero);
                pinViewCobertura.setLabel(String.valueOf(anomalyDao.countAnomaliesByType(
                        getContext().getResources().getString(AnomalyReport.COVERAGE.getResourceText()))));
                pinViewOther.setLabel(valueZero);
                break;
            case INTERNET:
                pinViewVoice.setLabel(valueZero);
                pinViewInternet.setLabel(String.valueOf(anomalyDao.countAnomaliesByType(
                        getContext().getResources().getString(AnomalyReport.INTERNET.getResourceText()))));
                pinViewMessaging.setLabel(valueZero);
                pinViewCobertura.setLabel(valueZero);
                pinViewOther.setLabel(valueZero);
                break;
            case MESSAGING:
                pinViewVoice.setLabel(valueZero);
                pinViewInternet.setLabel(valueZero);
                pinViewMessaging.setLabel(String.valueOf(anomalyDao.countAnomaliesByType(
                        getContext().getResources().getString(AnomalyReport.MESSAGING.getResourceText()))));
                pinViewCobertura.setLabel(valueZero);
                pinViewOther.setLabel(valueZero);
                break;
            case OTHER:
                pinViewVoice.setLabel(valueZero);
                pinViewInternet.setLabel(valueZero);
                pinViewMessaging.setLabel(valueZero);
                pinViewCobertura.setLabel(valueZero);
                pinViewOther.setLabel(String.valueOf(anomalyDao.countAnomaliesByType(
                        getContext().getResources().getString(AnomalyReport.OTHER.getResourceText()))));
                break;
            case VOICE:
                pinViewVoice.setLabel(String.valueOf(anomalyDao.countAnomaliesByType(
                        getContext().getResources().getString(AnomalyReport.VOICE.getResourceText()))));
                pinViewInternet.setLabel(valueZero);
                pinViewMessaging.setLabel(valueZero);
                pinViewCobertura.setLabel(valueZero);
                pinViewOther.setLabel(valueZero);
                break;
        }
    }

    private void addPointsToMap(List<Anomaly> anomalies) {

        if (gmLocationTest == null) {
            return;
        }

        gmLocationTest.clear();

        for (Anomaly anomaly : anomalies) {

            MarkerOptions marker =
                    new MarkerOptions().position(new LatLng(anomaly.getLatitude(), anomaly.getLongitude()))
                            .title(anomaly.getReportType());

            int resourceImage = AnomalyReport.getPinResourceImage(getContext(), anomaly.getLogoId());
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(resourceImage);
            marker.icon(icon);
            gmLocationTest.addMarker(marker);

            LatLng latLng = new LatLng(anomaly.getLatitude(), anomaly.getLongitude());
            gmLocationTest.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            gmLocationTest.moveCamera(CameraUpdateFactory.zoomTo(10));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gmLocationTest = googleMap;

        this.gmLocationTest.getUiSettings().setMyLocationButtonEnabled(true);
        this.gmLocationTest.setMyLocationEnabled(true);

        loadValues(AnomalyReport.ALL);
    }

    private void createSpinner() {
        SpinnerFilterAnomalyUtils.createSpinner(getContext(), spinnerFilter, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadValues((position == 0 ? AnomalyReport.ALL : AnomalyReport.getListTypeAnomaly()[position - 1]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
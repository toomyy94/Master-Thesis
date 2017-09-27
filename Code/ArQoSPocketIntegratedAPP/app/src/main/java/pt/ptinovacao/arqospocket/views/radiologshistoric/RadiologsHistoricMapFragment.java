package pt.ptinovacao.arqospocket.views.radiologshistoric;

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
import pt.ptinovacao.arqospocket.persistence.RadiologDao;
import pt.ptinovacao.arqospocket.persistence.models.Radiolog;
import pt.ptinovacao.arqospocket.views.radiologs.data.RadiologReport;
import pt.ptinovacao.arqospocket.views.testshistoric.PinView;

/**
 * RadiologsHistoricMapFragment.
 * <p>
 * Created by Tomás on 13/04/2017.
 */
public class RadiologsHistoricMapFragment extends ArQoSBaseFragment implements OnMapReadyCallback {

    private final static Logger LOGGER = LoggerFactory.getLogger(RadiologsHistoricMapFragment.class);

    private final static String RADIOLOG = "Radiolog";
    private final static String EVENT = "Event";
    private final static String SNAPSHOT = "Snapshot";
    private final static String SCANLOG = "Scanlog";

    private final static int pinRadiologs = R.mipmap.pin_radiologs_ok;
    private final static int pinEvent = R.mipmap.pin_event_ok;
    private final static int pinSnapshot = R.mipmap.pin_snapshot_ok;
    private final static int pinScanlog = R.mipmap.pin_mapa_wifi_sucesso;

    private Spinner spinnerFilter;

    private GoogleMap gmLocationTest;

    private ImageView ivButtonZoom;

    private LinearLayout llOccurrence;

    private PinView pinViewRadiolog;

    private PinView pinViewEvent;

    private PinView pinViewSnapshot;

    private PinView pinViewScanlog;

    private RadiologDao radiologDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        radiologDao = getArQosApplication().getDatabaseHelper().createRadiologDao();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_radiologs_historic_mapa, container, false);

        initializeViews(rootView);
        initializeListeners();
        initializeMap(rootView, savedInstanceState);

        setValuesOfPinView(RadiologReport.ALL);

        createSpinner();
        return rootView;
    }

    private void initializeViews(ViewGroup rootView) {

        ivButtonZoom = (ImageView) rootView.findViewById(R.id.icon_zoom_mapa);
        llOccurrence = (LinearLayout) rootView.findViewById(R.id.above_map_layout);
        spinnerFilter = (Spinner) rootView.findViewById(R.id.spinner_filter);

        pinViewRadiolog = (PinView) rootView.findViewById(R.id.pinLegenda_radiolog);
        pinViewEvent = (PinView) rootView.findViewById(R.id.pinLegenda_event);
        pinViewSnapshot = (PinView) rootView.findViewById(R.id.pinLegenda_snapshot);
        pinViewScanlog = (PinView) rootView.findViewById(R.id.pinLegenda_scanlog);
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

    private synchronized void loadValues(final RadiologReport radiologReport) {

        Single.fromCallable(new Callable<List<Radiolog>>() {
            @Override
            public List<Radiolog> call() throws Exception {
                return getArQosApplication().getDatabaseHelper().createRadiologDao().readRadiologsByType(
                        RadiologReport.ALL == radiologReport ? null :
                                getContext().getResources().getString(radiologReport.getResourceText()));
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Radiolog>>() {
                    @Override
                    public void accept(@NonNull List<Radiolog> executingEvents) throws Exception {

                        addPointsToMap(executingEvents);
                        setValuesOfPinView(radiologReport);
                    }
                });
    }

    private void setValuesOfPinView(RadiologReport valuesOfPinView) {
        final String valueZero = "0";

        switch (valuesOfPinView) {
            case ALL:
                pinViewRadiolog.setLabel(String.valueOf(radiologDao.countRadiologsByType(
                        getContext().getResources().getString(RadiologReport.RADIOLOG.getResourceText()))));
                pinViewSnapshot.setLabel(String.valueOf(radiologDao.countRadiologsByType(
                        getContext().getResources().getString(RadiologReport.SNAPSHOT.getResourceText()))));
                pinViewEvent.setLabel(String.valueOf(radiologDao.countRadiologsByType(
                        getContext().getResources().getString(RadiologReport.EVENT.getResourceText()))));
                pinViewScanlog.setLabel(String.valueOf(radiologDao.countRadiologsByType(
                        getContext().getResources().getString(RadiologReport.SCANLOG.getResourceText()))));
                break;
            case RADIOLOG:
                pinViewRadiolog.setLabel(String.valueOf(radiologDao.countRadiologsByType(
                        getContext().getResources().getString(RadiologReport.RADIOLOG.getResourceText()))));
                pinViewEvent.setLabel(valueZero);
                pinViewSnapshot.setLabel(valueZero);
                pinViewScanlog.setLabel(valueZero);
                break;
            case EVENT:
                pinViewRadiolog.setLabel(valueZero);
                pinViewEvent.setLabel(String.valueOf(radiologDao.countRadiologsByType(
                        getContext().getResources().getString(RadiologReport.EVENT.getResourceText()))));
                pinViewSnapshot.setLabel(valueZero);
                pinViewScanlog.setLabel(valueZero);
                break;
            case SNAPSHOT:
                pinViewRadiolog.setLabel(valueZero);
                pinViewEvent.setLabel(valueZero);
                pinViewSnapshot.setLabel(String.valueOf(radiologDao.countRadiologsByType(
                        getContext().getResources().getString(RadiologReport.SNAPSHOT.getResourceText()))));
                pinViewScanlog.setLabel(valueZero);
                break;
            case SCANLOG:
                pinViewRadiolog.setLabel(valueZero);
                pinViewEvent.setLabel(valueZero);
                pinViewSnapshot.setLabel(valueZero);
                pinViewScanlog.setLabel(String.valueOf(radiologDao.countRadiologsByType(
                        getContext().getResources().getString(RadiologReport.SNAPSHOT.getResourceText()))));
                break;
        }
    }

    private void addPointsToMap(List<Radiolog> radiologs) {

        if (gmLocationTest == null) {
            return;
        }

        gmLocationTest.clear();

        BitmapDescriptor iconRadiologs = BitmapDescriptorFactory.fromResource(pinRadiologs);
        BitmapDescriptor iconEvent = BitmapDescriptorFactory.fromResource(pinEvent);
        BitmapDescriptor iconSnapshot = BitmapDescriptorFactory.fromResource(pinSnapshot);
        BitmapDescriptor iconScanlog = BitmapDescriptorFactory.fromResource(pinScanlog);

        for (Radiolog radiolog : radiologs) {

            MarkerOptions marker =
                    new MarkerOptions().position(new LatLng(radiolog.getLatitude(), radiolog.getLongitude()))
                            .title(radiolog.getReportType());

            int resourceImage = RadiologReport.getPinResourceImage(getContext(), radiolog.getReportType());
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(resourceImage);
            marker.icon(icon);
            gmLocationTest.addMarker(marker);

            LatLng latLng = new LatLng(radiolog.getLatitude(), radiolog.getLongitude());
            gmLocationTest.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            gmLocationTest.moveCamera(CameraUpdateFactory.zoomTo(14));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gmLocationTest = googleMap;

        this.gmLocationTest.getUiSettings().setMyLocationButtonEnabled(true);
        this.gmLocationTest.setMyLocationEnabled(true);

        loadValues(RadiologReport.ALL);
    }

    private void createSpinner() {
        SpinnerFilterRadiologsUtils.createSpinner(getContext(), spinnerFilter, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadValues((position == 0 ? RadiologReport.ALL : RadiologReport.getListTypeRadiolog()[position - 1]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
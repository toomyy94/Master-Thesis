package pt.ptinovacao.arqospocket.views.anomalyhistoric.anomalydetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
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

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import pt.ptinovacao.arqospocket.ArQoSBaseFragment;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.utils.DateUtils;
import pt.ptinovacao.arqospocket.persistence.models.Anomaly;
import pt.ptinovacao.arqospocket.views.anomaly.data.AnomalyReport;
import pt.ptinovacao.arqospocket.views.anomaly.data.AnomalyTopics;
import pt.ptinovacao.arqospocket.views.anomaly.data.AnomalyTypeModel;

/**
 * Created by pedro on 20/04/2017.
 */
public class AnomalyDetailsFragment extends ArQoSBaseFragment implements OnMapReadyCallback {

    private final static Logger LOGGER = LoggerFactory.getLogger(AnomalyDetailsFragment.class);

    public static final String KEY_SWIPE_LEFT = "KEY_SWIPE_LEFT";

    public static final String KEY_SWIPE_RIGHT = "KEY_SWIPE_RIGHT";

    private GoogleMap googleMap;

    private TextView anomType;

    private TextView detailsType;

    private TextView detailsFeedback;

    private TextView details_date;

    private TextView detailsLocal;

    private Anomaly anomaly;

    private Long valueOfAnomaly;

    private ImageView swipeLeft;

    private ImageView swipeRight;

    private boolean activeLeft, activeRight;

    public static AnomalyDetailsFragment newInstance(Long eventId, boolean activeLeft, boolean activeRight) {
        Bundle args = new Bundle();
        args.putLong(AnomalyDetailsActivity.KEY_ANOMALY_TO_DETAILS, eventId);
        args.putBoolean(KEY_SWIPE_LEFT, activeLeft);
        args.putBoolean(KEY_SWIPE_RIGHT, activeRight);

        AnomalyDetailsFragment fragment = new AnomalyDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        valueOfAnomaly = getArguments().getLong(AnomalyDetailsActivity.KEY_ANOMALY_TO_DETAILS);
        activeLeft = getArguments().getBoolean(KEY_SWIPE_LEFT);
        activeRight = getArguments().getBoolean(KEY_SWIPE_RIGHT);

        LOGGER.debug("valueOfAnomaly: " + valueOfAnomaly);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView =
                (ViewGroup) inflater.inflate(R.layout.fragment_anomalias_historic_details, container, false);

        initializeView(rootView);
        initializeMap(rootView, savedInstanceState);

        return rootView;
    }

    private void initializeMap(ViewGroup rootView, Bundle savedInstanceState) {
        MapView mapView = (MapView) rootView.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    private void initializeView(ViewGroup rootView) {

        anomType = (TextView) rootView.findViewById(R.id.anomalias_entry_title);
        detailsType = (TextView) rootView.findViewById(R.id.details_type);
        detailsFeedback = (TextView) rootView.findViewById(R.id.details_feedback);
        details_date = (TextView) rootView.findViewById(R.id.details_date);
        detailsLocal = (TextView) rootView.findViewById(R.id.details_local);
        swipeLeft = (ImageView) rootView.findViewById(R.id.bt_swipe_left);
        swipeRight = (ImageView) rootView.findViewById(R.id.bt_swipe_right);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        this.googleMap.setMyLocationEnabled(true);

        loadValues();
    }

    private void loadValues() {
        getArQosApplication().getDatabaseHelper().createAnomalyDao().readAnomaliesById(valueOfAnomaly)
                .subscribe(new Consumer<Anomaly>() {
                    @Override
                    public void accept(@NonNull Anomaly anomaly) throws Exception {
                        AnomalyDetailsFragment.this.anomaly = anomaly;

                        anomType.setText(anomaly.getReportType());

                        anomType.setCompoundDrawablesWithIntrinsicBounds(0,
                                AnomalyReport.geImageOfText(getActivity(), anomaly.getReportType()), 0, 0);

                        AnomalyReport anomalyReport =
                                AnomalyReport.geAnomalyReportOfText(getContext(), anomaly.getReportType());

                        AnomalyTopics anomalyTopics = new AnomalyTopics();
                        AnomalyTypeModel[] anomalyTypeModels = anomalyTopics.getTypeHashMap().get(anomalyReport);

                        @StringRes
                        int text = anomalyTypeModels[anomaly.getReportSubType() - 1].getValue();

                        detailsType.setText(getContext().getString(text));

                        detailsFeedback.setText(anomaly.getUserFeedback());
                        details_date.setText(DateUtils.convertDateToString(anomaly.getReportDate()));
                        detailsLocal.setText(anomaly.getLatitude() + ", " + anomaly.getLongitude());

                        addPointsToMap();
                    }
                });
    }

    private void addPointsToMap() {

        if (googleMap == null) {
            return;
        }
        googleMap.clear();

        MarkerOptions marker = new MarkerOptions().position(new LatLng(anomaly.getLatitude(), anomaly.getLongitude()))
                .title(anomaly.getReportType());
        int resourceImage = AnomalyReport.getPinResourceImage(getContext(), anomaly.getLogoId());
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(resourceImage);
        marker.icon(icon);

        googleMap.addMarker(marker);

        LatLng latLng = new LatLng(anomaly.getLatitude(), anomaly.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(13));
    }

}

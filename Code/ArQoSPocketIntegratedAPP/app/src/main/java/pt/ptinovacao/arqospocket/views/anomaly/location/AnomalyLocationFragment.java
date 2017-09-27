package pt.ptinovacao.arqospocket.views.anomaly.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.ArQoSBaseFragment;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.location.GeoLocationManager;
import pt.ptinovacao.arqospocket.core.location.LocationInfo;
import pt.ptinovacao.arqospocket.views.anomaly.AnomalyActivity;
import pt.ptinovacao.arqospocket.views.anomaly.data.AnomalyReport;

public class AnomalyLocationFragment extends ArQoSBaseFragment implements OnMapReadyCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnomalyLocationFragment.class);

    public static final int MAP_ZOOM_VALUE = 10;

    private GoogleMap gmLocationTest;

    private ImageView ivTitle;

    private TextView tvTitle;

    private LinearLayout llTitleLayout;

    private ImageView ivZoom;

    private OnAnomalyLocationSelectedListener listener;

    private LocationInfo locationInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_anomaly_location, container, false);

        initializeViews(rootView);
        listeners();
        initializeMap(rootView, savedInstanceState);

        setValues();

        return rootView;
    }

    private void initializeMap(ViewGroup rootView, Bundle savedInstanceState) {
        MapView mapView = (MapView) rootView.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    private void initializeViews(ViewGroup rootView) {
        ivTitle = (ImageView) rootView.findViewById(R.id.iv_title);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);

        ivZoom = (ImageView) rootView.findViewById(R.id.icon_zoom_mapa);
        llTitleLayout = (LinearLayout) rootView.findViewById(R.id.occurrencesLayout);
    }

    private void listeners() {
        ivZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llTitleLayout.getVisibility() == View.VISIBLE) {
                    llTitleLayout.setVisibility(View.GONE);
                } else {
                    llTitleLayout.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void setValues() {
        AnomalyReport anomalyTypeSelected = ((AnomalyActivity) getActivity()).getAnomalyTypeSelected();

        ivTitle.setBackgroundResource(anomalyTypeSelected.getResourceIcon());
        tvTitle.setText(anomalyTypeSelected.getResourceText());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gmLocationTest = googleMap;

        this.gmLocationTest.getUiSettings().setMyLocationButtonEnabled(true);
        this.gmLocationTest.setMyLocationEnabled(true);

        loadPointMap();
    }

    private void loadPointMap() {

        GeoLocationManager geoLocationManager = GeoLocationManager.getInstance(getArQosApplication());
        Location lastKnownLocation = geoLocationManager.getLocation();

        if (lastKnownLocation != null) {
            LatLng curLatlng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(curLatlng, MAP_ZOOM_VALUE);
            this.gmLocationTest.animateCamera(cameraUpdate);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser == false && this.gmLocationTest != null) {
            LatLng center = this.gmLocationTest.getCameraPosition().target;

            locationInfo = new LocationInfo();
            locationInfo.setLongitude(center.longitude);
            locationInfo.setLatitude(center.latitude);

            if (listener != null) {
                listener.onAnomalyLocationSelected(locationInfo);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAnomalyLocationSelectedListener) {
            listener = (OnAnomalyLocationSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnAnomalyLocationSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnAnomalyLocationSelectedListener {

        void onAnomalyLocationSelected(LocationInfo locationInfo);
    }
}

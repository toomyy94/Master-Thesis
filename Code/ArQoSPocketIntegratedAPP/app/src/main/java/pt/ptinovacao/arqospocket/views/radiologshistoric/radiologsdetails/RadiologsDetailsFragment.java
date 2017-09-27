package pt.ptinovacao.arqospocket.views.radiologshistoric.radiologsdetails;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import pt.ptinovacao.arqospocket.ArQoSBaseFragment;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.radiologs.RadiologsManager;
import pt.ptinovacao.arqospocket.core.utils.DateUtils;
import pt.ptinovacao.arqospocket.persistence.models.Radiolog;
import pt.ptinovacao.arqospocket.views.anomalyhistoric.anomalydetails.AnomalyDetailsFragment;
import pt.ptinovacao.arqospocket.views.radiologs.data.RadiologReport;
import pt.ptinovacao.arqospocket.views.radiologs.data.RadiologTopics;

/**
 * Created by Tomás Rodrigues on 06/07/2017.
 */
public class RadiologsDetailsFragment extends ArQoSBaseFragment implements OnMapReadyCallback {

    private final static Logger LOGGER = LoggerFactory.getLogger(RadiologsDetailsFragment.class);

    private final static String RADIOLOG = "Radiolog";
    private final static String EVENT = "Event";
    private final static String SNAPSHOT = "Snapshot";
    private final static String SCANLOG = "Scanlog";

    private final static int pinRadiologs = R.mipmap.pin_radiolog;
    private final static int pinEvent = R.mipmap.pin_event;
    private final static int pinSnapshot = R.mipmap.pin_snapshot;
    private final static int pinScanlog = R.mipmap.pin_legenda_wi_fi;
    BitmapDescriptor iconRadiologs = BitmapDescriptorFactory.fromResource(pinRadiologs);
    BitmapDescriptor iconEvent = BitmapDescriptorFactory.fromResource(pinEvent);
    BitmapDescriptor iconSnapshot = BitmapDescriptorFactory.fromResource(pinSnapshot);
    BitmapDescriptor iconScanlog = BitmapDescriptorFactory.fromResource(pinScanlog);

    private GoogleMap googleMap;
    private boolean activeLeft, activeRight;
    private ImageView swipeLeft;
    private ImageView swipeRight;

    RadiologsManager radiologsManager;

    TextView title, details_date, detailsFeedback, detailsLocal, detailsType;
    LinearLayout detailsFeedback_layout, detailsEvent_layout;

    //Radiologs
    TextView details_cellid, details_status, details_rxlevel, details_technology, details_plmn, details_roaming, details_mcc,
            details_pci, details_rsrp, details_rsrq, details_cqi, details_rssnr, details_area_title, details_area, details_ber, details_psc, details_neighbours;
    LinearLayout detailsCellId_layout, detailsStatus_layout, detailsPci_layout, detailsArea_layout, detailsRsrp_layout, detailsTechnology_layout, detailsRsrq_layout, detailsCqi_layout,
            detailsRssnr_layout, detailsBer_layout, detailsPsc_layout, detailsNeigh_layout, detailsPlmn_layout, detailsRoaming_layout, detailsMcc_layout;

    //Scanlogs
    TextView details_bssid, details_channel, details_essid;
    LinearLayout detailsBssid_layout, detailsChannel_layout, detailsEssid_layout;

    private Radiolog radiolog;

    private Long valueOfRadiolog;

    public static RadiologsDetailsFragment newInstance(Long eventId, boolean activeLeft, boolean activeRight) {
        Bundle args = new Bundle();
        args.putLong(RadiologsDetailsActivity.KEY_RADIOLOG_TO_DETAILS, eventId);
        args.putBoolean(AnomalyDetailsFragment.KEY_SWIPE_LEFT, activeLeft);
        args.putBoolean(AnomalyDetailsFragment.KEY_SWIPE_RIGHT, activeRight);

        RadiologsDetailsFragment fragment = new RadiologsDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        valueOfRadiolog = getArguments().getLong(RadiologsDetailsActivity.KEY_RADIOLOG_TO_DETAILS);
        activeLeft = getArguments().getBoolean(AnomalyDetailsFragment.KEY_SWIPE_LEFT);
        activeRight = getArguments().getBoolean(AnomalyDetailsFragment.KEY_SWIPE_RIGHT);

        radiologsManager = RadiologsManager.getInstance(getArQosApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final ViewGroup rootView =
                (ViewGroup) inflater.inflate(R.layout.fragment_radiolog_historic_details, container, false);

        initializeView(rootView);
        initializeMap(rootView, savedInstanceState);
        loadValues(rootView);

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

    private void initializeView(ViewGroup view) {

        title = (TextView) view.findViewById(R.id.radiolog_entry_title);
        detailsType = (TextView) view.findViewById(R.id.details_type);
        details_date = (TextView) view.findViewById(R.id.details_date);
        detailsFeedback_layout = (LinearLayout) view.findViewById(R.id.details_feedback_layout);
        detailsFeedback = (TextView) view.findViewById(R.id.details_feedback);
        detailsLocal = (TextView) view.findViewById(R.id.details_local);
        detailsEvent_layout = (LinearLayout) view.findViewById(R.id.details_event_layout);
        detailsType = (TextView) view.findViewById(R.id.event_text);
        //Radiologs
        details_cellid = (TextView) view.findViewById(R.id.cellid_text);
        detailsCellId_layout = (LinearLayout) view.findViewById(R.id.details_cellid_layout);
        details_status = (TextView) view.findViewById(R.id.status_text);
        detailsStatus_layout = (LinearLayout) view.findViewById(R.id.details_status_layout);
        details_rxlevel = (TextView) view.findViewById(R.id.signalstrenght_text);
        details_technology = (TextView) view.findViewById(R.id.technology_text);
        detailsTechnology_layout = (LinearLayout) view.findViewById(R.id.details_technology_layout);
        details_plmn = (TextView) view.findViewById(R.id.operatorname_text);
        detailsPlmn_layout = (LinearLayout) view.findViewById(R.id.details_operatorname_layout);
        details_roaming = (TextView) view.findViewById(R.id.roaming_text);
        detailsRoaming_layout = (LinearLayout) view.findViewById(R.id.details_roaming_layout);
        details_mcc = (TextView) view.findViewById(R.id.mcc_text);
        detailsMcc_layout = (LinearLayout) view.findViewById(R.id.details_mcc_layout);
        detailsPci_layout = (LinearLayout) view.findViewById(R.id.details_pci_layout);
        details_pci = (TextView) view.findViewById(R.id.pci_text);
        detailsArea_layout = (LinearLayout) view.findViewById(R.id.details_area_layout);
        details_area = (TextView) view.findViewById(R.id.area_text);
        detailsRsrp_layout = (LinearLayout) view.findViewById(R.id.details_rsrp_layout);
        details_rsrp = (TextView) view.findViewById(R.id.rsrp_text);
        detailsRsrq_layout = (LinearLayout) view.findViewById(R.id.details_rsrq_layout);
        details_rsrq = (TextView) view.findViewById(R.id.rsrq_text);
        detailsCqi_layout = (LinearLayout) view.findViewById(R.id.details_cqi_layout);
        details_cqi = (TextView) view.findViewById(R.id.cqi_text);
        detailsRssnr_layout = (LinearLayout) view.findViewById(R.id.details_rssnr_layout);
        details_rssnr = (TextView) view.findViewById(R.id.rssnr_text);
        detailsArea_layout = (LinearLayout) view.findViewById(R.id.details_area_layout);
        details_area_title = (TextView) view.findViewById(R.id.area_title);
        details_area = (TextView) view.findViewById(R.id.area_text);
        detailsBer_layout = (LinearLayout) view.findViewById(R.id.details_ber_layout);
        details_ber = (TextView) view.findViewById(R.id.ber_text);
        detailsPsc_layout = (LinearLayout) view.findViewById(R.id.details_psc_layout);
        details_psc = (TextView) view.findViewById(R.id.psc_text);
        detailsNeigh_layout = (LinearLayout) view.findViewById(R.id.details_neighbours_layout);
        details_neighbours = (TextView) view.findViewById(R.id.neighbours_text);
        //Scanlogs
        detailsBssid_layout = (LinearLayout) view.findViewById(R.id.details_bssid_layout);
        details_bssid = (TextView) view.findViewById(R.id.bssid_text);
        detailsChannel_layout = (LinearLayout) view.findViewById(R.id.details_channel_layout);
        details_channel = (TextView) view.findViewById(R.id.channel_text);
        detailsEssid_layout = (LinearLayout) view.findViewById(R.id.details_essid_layout);
        details_essid = (TextView) view.findViewById(R.id.essid_text);
        //Swipe
        swipeLeft = (ImageView) view.findViewById(R.id.bt_swipe_left);
        swipeRight = (ImageView) view.findViewById(R.id.bt_swipe_right);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        this.googleMap.setMyLocationEnabled(true);
    }

    private void loadValues(ViewGroup view) {
        getArQosApplication().getDatabaseHelper().createRadiologDao().readRadiologsById(valueOfRadiolog)
                .subscribe(new Consumer<Radiolog>() {
                    @Override
                    public void accept(@NonNull Radiolog radiolog) throws Exception {
                        RadiologsDetailsFragment.this.radiolog = radiolog;

                        if(radiolog.getReportType().equals(SCANLOG)) {
                            loadScanlog(radiolog);
                        }
                        else {
                            loadRadiolog(radiolog);
                        }

                    }
                });
    }

    private void loadRadiolog(Radiolog radiolog){
        RadiologTopics radiologTopics = new RadiologTopics();

        title.setText(radiolog.getReportType());
        title.setCompoundDrawablesWithIntrinsicBounds(0,
                RadiologReport.geImageOfText(getActivity(), radiolog.getReportType()), 0, 0);

        details_date.setText(DateUtils.convertDateToString(radiolog.getReportDate()));

        removeScanlogsTextviews();
        addRadiologsTextviews(radiolog, radiologTopics);

        if(!radiolog.getReportType().equals(SNAPSHOT)) detailsFeedback_layout.setVisibility(View.GONE);
        else {
            detailsFeedback.setText(radiolog.getUserFeedback());
        }

        if(!radiolog.getReportType().equals(EVENT)) detailsEvent_layout.setVisibility(View.GONE);
        else {
            radiologsManager.getIntToEEvent(Integer.parseInt(radiologTopics.getEventParameter("type", radiolog.getRadiologContent(), radiolog.getReportType())));
            detailsType.setText(radiologsManager.getIntToEEvent(Integer.parseInt(radiologTopics.getEventParameter("type", radiolog.getRadiologContent(),  radiolog.getReportType())))
                    + ": " + radiologTopics.getEventParameter("origin", radiolog.getRadiologContent(),  radiolog.getReportType()));
        }

        details_cellid.setText(radiologTopics.getNetworkParameter("cellid", radiolog.getRadiologContent(),  radiolog.getReportType()));
        details_status.setText(radiologsManager.getIntToStatus(Integer.parseInt(radiologTopics.getNetworkParameter("status", radiolog.getRadiologContent(),  radiolog.getReportType()))));

        details_rxlevel.setText(radiologTopics.getNetworkParameter("rssi", radiolog.getRadiologContent(),  radiolog.getReportType()) + " dBm");
        details_technology.setText(radiologsManager.getIntToMode(Integer.parseInt(radiologTopics.getNetworkParameter("mode", radiolog.getRadiologContent(),  radiolog.getReportType()))));
        details_plmn.setText(radiologTopics.getNetworkParameter("plmn", radiolog.getRadiologContent(),  radiolog.getReportType()));
        details_roaming.setText(radiologsManager.getIntToRoaming(Integer.parseInt(radiologTopics.getNetworkParameter("roaming", radiolog.getRadiologContent(),  radiolog.getReportType()))));
        details_mcc.setText(radiologTopics.getNetworkParameter("mcc", radiolog.getRadiologContent(),  radiolog.getReportType()) + "/" + radiologTopics.getNetworkParameter("mnc", radiolog.getRadiologContent(),  radiolog.getReportType()));

        if(Integer.parseInt(radiologTopics.getNetworkParameter("mode", radiolog.getRadiologContent(),  radiolog.getReportType())) == 10){
            if(radiologTopics.hasKey("pci", radiolog.getRadiologContent(),  radiolog.getReportType())) {
                details_pci.setText(radiologTopics.getNetworkParameter("pci", radiolog.getRadiologContent(),  radiolog.getReportType()));
            }if(radiologTopics.hasKey("tac", radiolog.getRadiologContent(),  radiolog.getReportType())) {
                details_area.setText(radiologTopics.getNetworkParameter("tac", radiolog.getRadiologContent(),  radiolog.getReportType()));
            }if(radiologTopics.hasKey("rsrp", radiolog.getRadiologContent(),  radiolog.getReportType())) {
                details_rsrp.setText(radiologTopics.getNetworkParameter("rsrp", radiolog.getRadiologContent(),  radiolog.getReportType()) + " dBm");
            }if(radiologTopics.hasKey("rsrq", radiolog.getRadiologContent(),  radiolog.getReportType())) {
                details_rsrq.setText(radiologTopics.getNetworkParameter("rsrq", radiolog.getRadiologContent(),  radiolog.getReportType()) + " dBm");
            }if(radiologTopics.hasKey("cqi", radiolog.getRadiologContent(),  radiolog.getReportType())) {
                details_cqi.setText(radiologTopics.getNetworkParameter("cqi", radiolog.getRadiologContent(),  radiolog.getReportType()));
            }if(radiologTopics.hasKey("rssnr", radiolog.getRadiologContent(),  radiolog.getReportType())) {
                details_rssnr.setText(radiologTopics.getNetworkParameter("rssnr", radiolog.getRadiologContent(),  radiolog.getReportType()));
            }
        }
        else {
            if (radiologTopics.hasKey("lac", radiolog.getRadiologContent(),  radiolog.getReportType())) {
                details_area_title.setText(getResources().getString(R.string.Lac));
                details_area.setText(radiologTopics.getNetworkParameter("lac", radiolog.getRadiologContent(),  radiolog.getReportType()));
            }
//            if (radiologTopics.hasKey("ber", radiolog.getRadiologContent(),  radiolog.getReportType())) {
//                details_ber.setText(radiologTopics.getNetworkParameter("ber", radiolog.getRadiologContent(),  radiolog.getReportType()));
//            }
            if (radiologTopics.hasKey("psc", radiolog.getRadiologContent(),  radiolog.getReportType())) {
                details_psc.setText(radiologTopics.getNetworkParameter("psc", radiolog.getRadiologContent(),  radiolog.getReportType()));
            }
        }

        if(radiologTopics.hasKey("neighbours", radiolog.getRadiologContent(),  radiolog.getReportType())) {
            detailsNeigh_layout.setVisibility(View.VISIBLE);
            details_neighbours.setText(radiologTopics.getNeighboursParameter(radiolog.getRadiologContent(),  radiolog.getReportType()));
        }

        detailsFeedback.setText(radiolog.getUserFeedback());
        details_date.setText(DateUtils.convertDateToString(radiolog.getReportDate()));
        LatLng latLng = new LatLng(radiolog.getLatitude(), radiolog.getLongitude());
        //                        detailsLocal.setText(getReverseGeocode(latLng));
        detailsLocal.setText(radiolog.getLatitude() + ", " + radiolog.getLongitude());

        addPointsToMap();
    }

    private void loadScanlog(Radiolog scanlog){
        RadiologTopics radiologTopics = new RadiologTopics();

        title.setText(radiolog.getReportType());
        title.setCompoundDrawablesWithIntrinsicBounds(0,
                RadiologReport.geImageOfText(getActivity(), radiolog.getReportType()), 0, 0);

        details_date.setText(DateUtils.convertDateToString(radiolog.getReportDate()));

        removeRadiologsTextviews();
        addScanlogsTextviews();

        details_bssid.setText(radiologTopics.getNetworkParameter("bssid",radiolog.getRadiologContent(),  radiolog.getReportType()));
        details_channel.setText(radiologTopics.getNetworkParameter("channel",radiolog.getRadiologContent(),  radiolog.getReportType()));
        details_essid.setText(radiologTopics.getNetworkParameter("essid",radiolog.getRadiologContent(),  radiolog.getReportType()));
        details_rxlevel.setText(radiologTopics.getNetworkParameter("rxlevel", radiolog.getRadiologContent(),  radiolog.getReportType()) + " dBm");

        details_date.setText(DateUtils.convertDateToString(radiolog.getReportDate()));
        LatLng latLng = new LatLng(radiolog.getLatitude(), radiolog.getLongitude());
        //                        detailsLocal.setText(getReverseGeocode(latLng));
        detailsLocal.setText(radiolog.getLatitude() + ", " + radiolog.getLongitude());

        addPointsToMap();
    }

    private void addPointsToMap() {

        if (googleMap == null) {
            return;
        }
        googleMap.clear();

        MarkerOptions marker = new MarkerOptions().position(new LatLng(radiolog.getLatitude(), radiolog.getLongitude()))
                .title(radiolog.getReportType());

        int resourceImage = RadiologReport.getPinResourceImage(getContext(), radiolog.getReportType());
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(resourceImage);
        marker.icon(icon);

        googleMap.addMarker(marker);

        LatLng latLng = new LatLng(radiolog.getLatitude(), radiolog.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
    }

    private String getReverseGeocode(LatLng latLng) {
        Geocoder geoCoder = new Geocoder(getActivity());
        List<Address> matches = null;
        String address = null;

        try {
            if(geoCoder != null)
                matches = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if(matches != null && !matches.isEmpty()) {
            Address bestMatch = matches.get(0);
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < bestMatch.getMaxAddressLineIndex(); i++) {
                sb.append(bestMatch.getAddressLine(i));
                sb.append("\n");
            }
            address = sb.toString();
        }

        return address;
    }

    private void removeRadiologsTextviews(){
        detailsPci_layout.setVisibility(View.GONE); detailsArea_layout.setVisibility(View.GONE); detailsRsrp_layout.setVisibility(View.GONE); detailsFeedback_layout.setVisibility(View.GONE); detailsEvent_layout.setVisibility(View.GONE);
        detailsPlmn_layout.setVisibility(View.GONE); detailsTechnology_layout.setVisibility(View.GONE); detailsCellId_layout.setVisibility(View.GONE); detailsStatus_layout.setVisibility(View.GONE); detailsRoaming_layout.setVisibility(View.GONE); detailsMcc_layout.setVisibility(View.GONE);
        detailsRsrq_layout.setVisibility(View.GONE); detailsCqi_layout.setVisibility(View.GONE); detailsRssnr_layout.setVisibility(View.GONE); detailsBer_layout.setVisibility(View.GONE); detailsPsc_layout.setVisibility(View.GONE); detailsNeigh_layout.setVisibility(View.GONE);
    }

    private void addRadiologsTextviews(Radiolog radiolog, RadiologTopics radiologTopics){
        detailsArea_layout.setVisibility(View.VISIBLE);  detailsFeedback_layout.setVisibility(View.VISIBLE); detailsEvent_layout.setVisibility(View.VISIBLE);
        detailsPlmn_layout.setVisibility(View.VISIBLE); detailsTechnology_layout.setVisibility(View.VISIBLE); detailsCellId_layout.setVisibility(View.VISIBLE);  detailsMcc_layout.setVisibility(View.VISIBLE);
        detailsNeigh_layout.setVisibility(View.VISIBLE); details_neighbours.setVisibility(View.VISIBLE); detailsStatus_layout.setVisibility(View.VISIBLE); detailsRoaming_layout.setVisibility(View.VISIBLE);

        if(Integer.parseInt(radiologTopics.getNetworkParameter("mode", radiolog.getRadiologContent(),  radiolog.getReportType())) == 10){
            detailsRssnr_layout.setVisibility(View.VISIBLE); detailsRsrq_layout.setVisibility(View.VISIBLE); detailsRsrp_layout.setVisibility(View.VISIBLE); detailsCqi_layout.setVisibility(View.VISIBLE);
            detailsPsc_layout.setVisibility(View.GONE); detailsBer_layout.setVisibility(View.GONE);
        }
        else if(Integer.parseInt(radiologTopics.getNetworkParameter("mode", radiolog.getRadiologContent(),  radiolog.getReportType())) == 2 ||
                Integer.parseInt(radiologTopics.getNetworkParameter("mode", radiolog.getRadiologContent(),  radiolog.getReportType())) == 5 ||
                Integer.parseInt(radiologTopics.getNetworkParameter("mode", radiolog.getRadiologContent(),  radiolog.getReportType())) == 8){
            detailsPsc_layout.setVisibility(View.VISIBLE);
            detailsRssnr_layout.setVisibility(View.GONE); detailsRsrq_layout.setVisibility(View.GONE); detailsRsrp_layout.setVisibility(View.GONE); detailsCqi_layout.setVisibility(View.GONE);  detailsBer_layout.setVisibility(View.GONE);
        }
        else{
            detailsPsc_layout.setVisibility(View.GONE); detailsRssnr_layout.setVisibility(View.GONE); detailsRsrq_layout.setVisibility(View.GONE);
            detailsCqi_layout.setVisibility(View.GONE);  detailsBer_layout.setVisibility(View.GONE); detailsRsrp_layout.setVisibility(View.GONE);

        }
    }

    private void removeScanlogsTextviews(){
        detailsBssid_layout.setVisibility(View.GONE);  detailsChannel_layout.setVisibility(View.GONE);  detailsEssid_layout.setVisibility(View.GONE);
    }

    private void addScanlogsTextviews(){
        detailsBssid_layout.setVisibility(View.VISIBLE);  detailsChannel_layout.setVisibility(View.VISIBLE);  detailsEssid_layout.setVisibility(View.VISIBLE);
    }

}

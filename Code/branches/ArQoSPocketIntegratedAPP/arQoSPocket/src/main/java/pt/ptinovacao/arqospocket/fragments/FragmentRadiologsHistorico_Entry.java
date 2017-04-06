package pt.ptinovacao.arqospocket.fragments;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ptinovacao.arqospocket.HistoricoAnomaliasItem;
import pt.ptinovacao.arqospocket.HistoricoRadiologsItem;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.activities.ActivityAnomaliasHistorico;
import pt.ptinovacao.arqospocket.activities.ActivityRadiologs;
import pt.ptinovacao.arqospocket.activities.ActivityRadiologsHistorico;
import pt.ptinovacao.arqospocket.interfaces.IFragmentPager;
import pt.ptinovacao.arqospocket.interfaces.IUpdateMap;
import pt.ptinovacao.arqospocket.util.Utils;

public class FragmentRadiologsHistorico_Entry extends Fragment implements IUpdateMap {

	private static final String HISTORY_ITEM_POSITION = "HistoryItemPosition";
	private View view;
	private GoogleMap map;
	private Location spotLocation = null;
	private List<MarkerOptions> markersList;
	public static String tag;
	private ActivityRadiologsHistorico activity;
	private ArrayList<HistoricoRadiologsItem> historicoRadiologs;
	private int currentItem = 0;
	private HistoricoRadiologsItem radiolog;
	private Location radiologLocation;
	private LatLng cur_Latlng;
	private TextView detailsLocal;
	private Map<String,List<Marker>> mapaMarkersHistoricoRadiologs;
	private boolean setupMapAfterInit = false;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		this.activity = (ActivityRadiologsHistorico) activity;
		this.historicoRadiologs = this.activity.getHistory(); 
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tag = getTag();
		markersList = new ArrayList<MarkerOptions>();

		mapaMarkersHistoricoRadiologs = new HashMap<String, List<Marker>>();
		
		Bundle args = getArguments();		
		if(args != null && args.containsKey(HISTORY_ITEM_POSITION)) {
			currentItem = args.getInt(HISTORY_ITEM_POSITION);
			radiolog = historicoRadiologs.get(currentItem);
			
			/* Set radiolog location */
			radiologLocation = radiolog.get_location();
			cur_Latlng = new LatLng(radiologLocation.getLatitude(), radiologLocation.getLongitude());
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		((ViewGroup) view.getParent()).removeView(view);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_radiolog_historico_entry,
				container, false);
		
		if(historicoRadiologs != null) {			
			/* Set radiolog info parameters. */
			TextView title = (TextView) view.findViewById(R.id.radiolog_entry_title);
			title.setText(radiolog.get_radiolog_id());
			title.setCompoundDrawablesWithIntrinsicBounds(0, radiolog.get_logo_resource_id(), 0, 0);

			TextView details_date = (TextView) view.findViewById(R.id.details_date);
			details_date.setText(radiolog.get_radiolog_report_date_string());

            LinearLayout detailsFeedback_layout = (LinearLayout) view.findViewById(R.id.details_feedback_layout);
            if(!radiolog.get_radiolog_id().equals("Snapshot")) detailsFeedback_layout.setVisibility(View.GONE);
            else {
                TextView detailsFeedback = (TextView) view.findViewById(R.id.details_feedback);
                detailsFeedback.setText(radiolog.get_report_msg());
            }

            detailsLocal = (TextView) view.findViewById(R.id.details_local);

            LinearLayout detailsEvent_layout = (LinearLayout) view.findViewById(R.id.details_event_layout);
            if(!radiolog.get_radiolog_id().equals("Event")) detailsEvent_layout.setVisibility(View.GONE);
			else {
				TextView detailsType = (TextView) view.findViewById(R.id.event_text);
				activity.iService.get_IntToEEvent(Integer.parseInt(radiolog.get_event_parameter("type")));
				detailsType.setText(activity.iService.get_IntToEEvent(Integer.parseInt(radiolog.get_event_parameter("type")))
						+ ": " + radiolog.get_event_parameter("origin"));
			}

			TextView details_cellid = (TextView) view.findViewById(R.id.cellid_text);
			details_cellid.setText(radiolog.get_network_parameter("cellid"));

			TextView details_status = (TextView) view.findViewById(R.id.status_text);
			details_status.setText(activity.iService.get_IntToStatus(Integer.parseInt(radiolog.get_network_parameter("status"))));

			TextView details_rxlevel = (TextView) view.findViewById(R.id.signalstrenght_text);
            details_rxlevel.setText(radiolog.get_network_parameter("rxlevel"));

			TextView details_technology = (TextView) view.findViewById(R.id.technology_text);
            details_technology.setText(activity.iService.get_IntToMode(Integer.parseInt(radiolog.get_network_parameter("mode"))));

            TextView details_plmn = (TextView) view.findViewById(R.id.operatorname_text);
            details_plmn.setText(radiolog.get_network_parameter("plmn"));

            TextView details_roaming = (TextView) view.findViewById(R.id.roaming_text);
            details_roaming.setText(activity.iService.get_IntToRoaming(Integer.parseInt(radiolog.get_network_parameter("roaming"))));

            TextView details_mcc = (TextView) view.findViewById(R.id.mcc_text);
            details_mcc.setText(radiolog.get_network_parameter("mcc") + "/" + radiolog.get_network_parameter("mnc"));

            if(Integer.parseInt(radiolog.get_network_parameter("mode"))==10){
                if(radiolog.hasKey("pci")) {
                    LinearLayout detailsPci_layout = (LinearLayout) view.findViewById(R.id.details_pci_layout);
                    detailsPci_layout.setVisibility(View.VISIBLE);
                    TextView details_pci = (TextView) view.findViewById(R.id.pci_text);
                    details_pci.setText(radiolog.get_network_parameter("pci"));
                }if(radiolog.hasKey("tac")) {
                    LinearLayout detailsArea_layout = (LinearLayout) view.findViewById(R.id.details_area_layout);
                    detailsArea_layout.setVisibility(View.VISIBLE);
                    TextView details_area = (TextView) view.findViewById(R.id.area_text);
                    details_area.setText(radiolog.get_network_parameter("tac"));
                }if(radiolog.hasKey("rsrp")) {
                    LinearLayout detailsRsrp_layout = (LinearLayout) view.findViewById(R.id.details_rsrp_layout);
                    detailsRsrp_layout.setVisibility(View.VISIBLE);
                    TextView details_rsrp = (TextView) view.findViewById(R.id.rsrp_text);
                    details_rsrp.setText(radiolog.get_network_parameter("rsrp"));
                }if(radiolog.hasKey("rsrq")) {
                    LinearLayout detailsRsrq_layout = (LinearLayout) view.findViewById(R.id.details_rsrq_layout);
                    detailsRsrq_layout.setVisibility(View.VISIBLE);
                    TextView details_rsrq = (TextView) view.findViewById(R.id.rsrq_text);
                    details_rsrq.setText(radiolog.get_network_parameter("rsrq"));
                }if(radiolog.hasKey("cqi")) {
                    LinearLayout detailsCqi_layout = (LinearLayout) view.findViewById(R.id.details_cqi_layout);
                    detailsCqi_layout.setVisibility(View.VISIBLE);
                    TextView details_cqi = (TextView) view.findViewById(R.id.cqi_text);
                    details_cqi.setText(radiolog.get_network_parameter("cqi"));
                }if(radiolog.hasKey("rssnr")) {
                    LinearLayout detailsRssnr_layout = (LinearLayout) view.findViewById(R.id.details_rssnr_layout);
                    detailsRssnr_layout.setVisibility(View.VISIBLE);
                    TextView details_rssnr = (TextView) view.findViewById(R.id.rssnr_text);
                    details_rssnr.setText(radiolog.get_network_parameter("rssnr"));
                }
            }
            else {
                if (radiolog.hasKey("lac")) {
                    LinearLayout detailsArea_layout = (LinearLayout) view.findViewById(R.id.details_area_layout);
                    detailsArea_layout.setVisibility(View.VISIBLE);
                    TextView details_area_title = (TextView) view.findViewById(R.id.area_title);
                    details_area_title.setText(getResources().getString(R.string.Lac));
                    TextView details_area = (TextView) view.findViewById(R.id.area_text);
                    details_area.setText(radiolog.get_network_parameter("lac"));
                }
                if (radiolog.hasKey("ber")) {
                    LinearLayout detailsBer_layout = (LinearLayout) view.findViewById(R.id.details_ber_layout);
                    detailsBer_layout.setVisibility(View.VISIBLE);
                    TextView details_ber = (TextView) view.findViewById(R.id.ber_text);
                    details_ber.setText(radiolog.get_network_parameter("ber"));
                }
                if (radiolog.hasKey("psc")) {
                    LinearLayout detailsPsc_layout = (LinearLayout) view.findViewById(R.id.details_psc_layout);
                    detailsPsc_layout.setVisibility(View.VISIBLE);
                    TextView details_psc = (TextView) view.findViewById(R.id.psc_text);
                    details_psc.setText(radiolog.get_network_parameter("psc"));
                }
            }
            if(radiolog.hasKey("neighbours")) {
                LinearLayout detailsNeigh_layout = (LinearLayout) view.findViewById(R.id.details_neighbours_layout);
                detailsNeigh_layout.setVisibility(View.VISIBLE);
                TextView details_neighbours = (TextView) view.findViewById(R.id.neighbours_text);
                details_neighbours.setText(radiolog.get_neighbours_parameter());
            }



			if(activity instanceof IFragmentPager) {
				ImageView previousradiolog = (ImageView) view.findViewById(R.id.bt_swipe_left);
				if(currentItem != Utils.HISTORY_FIRST_ITEM) {
					previousradiolog.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							activity.previousPage();
						}
					});
				} else {
					previousradiolog.setVisibility(View.INVISIBLE);
				}
				
				ImageView nextRadiolog = (ImageView) view.findViewById(R.id.bt_swipe_right);
				if(currentItem != (historicoRadiologs.size() - 1)) {
					nextRadiolog.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							activity.nextPage();
						}
					});
				} else {
					nextRadiolog.setVisibility(View.INVISIBLE);
				}
			}
		}

		return view;
	}
			
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		map = activity.initMap();
		
		try {
			new ReverseGeocodingTask().execute();			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(setupMapAfterInit) {
			updateMap();
			setupMapAfterInit = false;	
		}
	}

	private String getReverseGeocode(LatLng latLng) {
		Geocoder geoCoder = new Geocoder(activity);
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
	
	//* Finding address using reverse geocoding (coordinates -> address) */
	private class ReverseGeocodingTask extends AsyncTask<LatLng, Void, String> {		
		@Override
		protected String doInBackground(LatLng... latLng) {
			/* Get location address */
			String address = getReverseGeocode(cur_Latlng);
			return address;
		}

		@Override
		protected void onPostExecute(String address) {
			if(detailsLocal != null) {
				if(address == null) {
					address = cur_Latlng.latitude + ", " + cur_Latlng.longitude;
				}
				detailsLocal.setText(address);
			}				
		}
	}
	
	@Override
	public void updateMap() {
		if (map != null && radiolog != null 
				&& historicoRadiologs != null && historicoRadiologs.size() > 0) {
			map.clear();
			LatLng cur_Latlng = null;
			for(HistoricoRadiologsItem radiolog : historicoRadiologs) {
				Location radiologLocation = radiolog.get_location();
				cur_Latlng = new LatLng(radiologLocation.getLatitude(), radiologLocation.getLongitude());
				
				BitmapDescriptor icon = null;
				
				int radiologIconResource;
				if(radiolog.equals(this.radiolog))
					radiologIconResource = activity.getRadiologBigIconResource(radiolog.getType());
				else		
					radiologIconResource = activity.getRadiologIconResource(radiolog.getType(), false);
				
				if(radiologIconResource != Utils.INVALID_RESOURCE) {
					/* Map icon (ballon) */
					icon = BitmapDescriptorFactory
						.fromResource(radiologIconResource);
				
					/* Set marker options */
					MarkerOptions markerOps = new MarkerOptions()
						.position(cur_Latlng)
	//					.title("Anomalia")	-> Sets title
	//					.snippet("Voz")		-> Sets snippet
						.icon(icon);
					Marker marker = map.addMarker(markerOps);
					
					String type = radiolog.getType();
					if(mapaMarkersHistoricoRadiologs.containsKey(type))
						mapaMarkersHistoricoRadiologs.get(type).add(marker);
					else {
						List<Marker> list = new ArrayList<Marker>();
						list.add(marker);
						mapaMarkersHistoricoRadiologs.put(type, list);
					}
				}
			}
			
			cur_Latlng = new LatLng(radiologLocation.getLatitude(), radiologLocation.getLongitude());
			if(cur_Latlng != null) {
				float zoomLevel = Utils.isZoomFartherThanDefault(map.getCameraPosition().zoom) ? Utils.ZOOM_VALUE : map.getCameraPosition().zoom;
				CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(cur_Latlng, zoomLevel);
				map.animateCamera(cameraUpdate);
			}
		} else {
			setupMapAfterInit = true;
		}
	}

	/**
	 * @param query
	 *            query string used to filter markers
	 */
	public void filterMarkers(String query) {
		if (query != null)
			query = query.toLowerCase();

		if (map != null) {
			map.clear();
			for (MarkerOptions m : markersList)
				if (query == null || m.getTitle().toLowerCase().contains(query)
						|| m.getSnippet().toLowerCase().contains(query))
					map.addMarker(m);
		}
	}

	private String locationToString(Location location) {

		if (spotLocation != null) {
			return location.getLatitude() + "," + spotLocation.getLongitude();
		} else {
			return "";
		}
	}

}

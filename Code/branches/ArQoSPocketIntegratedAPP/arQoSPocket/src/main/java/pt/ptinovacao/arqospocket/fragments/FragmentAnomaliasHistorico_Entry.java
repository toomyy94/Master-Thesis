package pt.ptinovacao.arqospocket.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ptinovacao.arqospocket.HistoricoAnomaliasItem;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.activities.ActivityAnomaliasHistorico;
import pt.ptinovacao.arqospocket.interfaces.IFragmentPager;
import pt.ptinovacao.arqospocket.interfaces.IUpdateMap;
import pt.ptinovacao.arqospocket.util.Utils;
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
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class FragmentAnomaliasHistorico_Entry extends Fragment implements IUpdateMap {

	private static final String HISTORY_ITEM_POSITION = "HistoryItemPosition";
	private View view;
	private GoogleMap map;
	private Location spotLocation = null;
	private List<MarkerOptions> markersList;
	public static String tag;
	private ActivityAnomaliasHistorico activity;
	private ArrayList<HistoricoAnomaliasItem> historicoAnomalias;
	private int currentItem = 0;
	private HistoricoAnomaliasItem anomaly;
	private Location anomalyLocation;
	private LatLng cur_Latlng;
	private TextView detailsLocal;
	private Map<String,List<Marker>> mapaMarkersHistoricoAnomalias;
	private boolean setupMapAfterInit = false;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		this.activity = (ActivityAnomaliasHistorico) activity;
		this.historicoAnomalias = this.activity.getHistory(); 
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tag = getTag();
		markersList = new ArrayList<MarkerOptions>();

		mapaMarkersHistoricoAnomalias = new HashMap<String, List<Marker>>();
		
		Bundle args = getArguments();		
		if(args != null && args.containsKey(HISTORY_ITEM_POSITION)) {
			currentItem = args.getInt(HISTORY_ITEM_POSITION);
			anomaly = historicoAnomalias.get(currentItem);
			
			/* Set anomaly location */
			anomalyLocation = anomaly.get_location();
			cur_Latlng = new LatLng(anomalyLocation.getLatitude(), anomalyLocation.getLongitude());
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		((ViewGroup) view.getParent()).removeView(view);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_anomalias_historico_entry,
				container, false);
		
		if(historicoAnomalias != null) {			
			/* Set anomaly info parameters. */
			TextView anomType = (TextView) view.findViewById(R.id.anomalias_entry_title);
			anomType.setText(anomaly.get_anomalie_id());
			anomType.setCompoundDrawablesWithIntrinsicBounds(0, anomaly.get_logo_resource_id(), 0, 0);
			
			TextView detailsType = (TextView) view.findViewById(R.id.details_type);
			detailsType.setText(anomaly.get_anomalie_Details_id());
			
			detailsLocal = (TextView) view.findViewById(R.id.details_local);
			
			TextView detailsFeedback = (TextView) view.findViewById(R.id.details_feedback);
			detailsFeedback.setText(anomaly.get_report_msg());
			
			TextView details_date = (TextView) view.findViewById(R.id.details_date);
			details_date.setText(anomaly.get_anomalie_report_date_string());			
			
			if(activity instanceof IFragmentPager) {
				ImageView previousAnom = (ImageView) view.findViewById(R.id.bt_swipe_left);
				if(currentItem != Utils.HISTORY_FIRST_ITEM) {
					previousAnom.setOnClickListener(new OnClickListener() {				
						@Override
						public void onClick(View v) {
							activity.previousPage();
						}
					});
				} else {
					previousAnom.setVisibility(View.INVISIBLE);
				}
				
				ImageView nextAnom = (ImageView) view.findViewById(R.id.bt_swipe_right);
				if(currentItem != (historicoAnomalias.size() - 1)) {
					nextAnom.setOnClickListener(new OnClickListener() {				
						@Override
						public void onClick(View v) {
							activity.nextPage();
						}
					});
				} else {
					nextAnom.setVisibility(View.INVISIBLE);
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
		if (map != null && anomaly != null 
				&& historicoAnomalias != null && historicoAnomalias.size() > 0) {
			map.clear();
			LatLng cur_Latlng = null;
			for(HistoricoAnomaliasItem anom : historicoAnomalias) {
				Location anomLocation = anom.get_location();
				cur_Latlng = new LatLng(anomLocation.getLatitude(), anomLocation.getLongitude());
				
				BitmapDescriptor icon = null;
				
				int anomIconResource;
				if(anom.equals(this.anomaly))
					anomIconResource = activity.getAnomalyBigIconResource(anom.getType());
				else		
					anomIconResource = activity.getAnomalyIconResource(anom.getType(), false);
				
				if(anomIconResource != Utils.INVALID_RESOURCE) {
					/* Map icon (ballon) */
					icon = BitmapDescriptorFactory
						.fromResource(anomIconResource);
				
					/* Set marker options */
					MarkerOptions markerOps = new MarkerOptions()
						.position(cur_Latlng)
	//					.title("Anomalia")	-> Sets title
	//					.snippet("Voz")		-> Sets snippet
						.icon(icon);
					Marker marker = map.addMarker(markerOps);
					
					String type = anom.getType();
					if(mapaMarkersHistoricoAnomalias.containsKey(type))
						mapaMarkersHistoricoAnomalias.get(type).add(marker);
					else {
						List<Marker> list = new ArrayList<Marker>();
						list.add(marker);
						mapaMarkersHistoricoAnomalias.put(type, list);
					}
				}
			}
			
			cur_Latlng = new LatLng(anomalyLocation.getLatitude(), anomalyLocation.getLongitude());
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

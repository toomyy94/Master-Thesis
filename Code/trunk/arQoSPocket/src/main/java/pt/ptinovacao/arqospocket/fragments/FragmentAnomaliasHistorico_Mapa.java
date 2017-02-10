package pt.ptinovacao.arqospocket.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import pt.ptinovacao.arqospocket.adapters.Adapter_spinner;
import pt.ptinovacao.arqospocket.DialogRowItem;
import pt.ptinovacao.arqospocket.HistoricoAnomaliasItem;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.activities.ActivityAnomaliasHistorico;
import pt.ptinovacao.arqospocket.map.PinLegenda_Layout;
import pt.ptinovacao.arqospocket.util.Utils;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class FragmentAnomaliasHistorico_Mapa extends Fragment {
	
	private View view;
	private MapView mapView;
	private GoogleMap map;
	private Location spotLocation = null;
	private List<MarkerOptions> markersList;
	public static String tag;
	
	private Spinner spinnerFilter;
	private LinearLayout occurrencesLayout;
	private boolean mapStretched;
	private ImageView mapZoomer;
	
	private ActivityAnomaliasHistorico activity;
	private ArrayList<HistoricoAnomaliasItem> historicoAnomalias;
	private Map<String, List<HistoricoAnomaliasItem>> mapaHistoricoAnomalias;
	private Map<String,List<Marker>> mapaMarkersHistoricoAnomalias;
	private boolean filterFirstExecution;
	private ArrayList<DialogRowItem> dialogItems = new ArrayList<DialogRowItem>();

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		this.activity = (ActivityAnomaliasHistorico) activity;
		this.historicoAnomalias = this.activity.getHistory();
		this.mapaHistoricoAnomalias = this.activity.getHistoryMap();		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tag = getTag();
		markersList = new ArrayList<MarkerOptions>();
		mapStretched = false;
		
		mapaMarkersHistoricoAnomalias = new HashMap<String, List<Marker>>();
		
		filterFirstExecution = true;
		
		if(dialogItems != null && dialogItems.size() == 0) {
			for(int i = 0; i < activity.anomTypes.size(); i++) {
				dialogItems.add(new DialogRowItem(activity.anomTypes.get(i), Utils.Anomalies_icons[i]));
			}
		}
	}

//	@Override
//	public void onDestroyView() {
//		super.onDestroyView();
//		((ViewGroup) view.getParent()).removeView(view);
//
//	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		if (view != null)
//			return view;

//		super.onCreateView(inflater, container, savedInstanceState);

		view = inflater.inflate(R.layout.fragment_anomalias_historico_mapa,
				container, false);

		occurrencesLayout = (LinearLayout) view.findViewById(R.id.above_map_layout);
		
		mapZoomer = (ImageView) view.findViewById(R.id.icon_zoom_mapa);
		mapZoomer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				
//				LatLng center = map.getCameraPosition().target;
//				Log.i("Map center", "Lat: " + center.latitude + "\nLong: " + center.longitude);
				
				mapStretched = !mapStretched;
				
				if(mapStretched) {
					occurrencesLayout.setVisibility(View.GONE);
				} else {
					occurrencesLayout.setVisibility(View.VISIBLE);
				}
				
			}
		});
		
		Adapter_spinner spinnerAdapter = new Adapter_spinner(activity, dialogItems);
		spinnerAdapter.setDropDownViewResource(R.layout.rowdrop);
		
		spinnerFilter = (Spinner) view.findViewById(R.id.spinner_filter);
		
		//Adicionado
		spinnerFilter.setAdapter(spinnerAdapter);
		
		if(historicoAnomalias != null) {
			spinnerFilter.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					if(filterFirstExecution)
						filterFirstExecution = false;
					else
						filterAnomalies(arg2);
				}
	
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {				
				}
			});
			
			if(mapaHistoricoAnomalias != null && mapaHistoricoAnomalias.size() > 0) {
				/* Set number of anomalies per type */			
				PinLegenda_Layout pinLegenda_voz = (PinLegenda_Layout) view.findViewById(R.id.pinLegenda_voz);
				if(mapaHistoricoAnomalias.containsKey((activity.anomTypes.get(1))))
						pinLegenda_voz.setLabel(String.valueOf(mapaHistoricoAnomalias.get(activity.anomTypes.get(1)).size()));
				
				PinLegenda_Layout pinLegenda_internet = (PinLegenda_Layout) view.findViewById(R.id.pinLegenda_internet);
				if(mapaHistoricoAnomalias.containsKey((activity.anomTypes.get(2))))
					pinLegenda_internet.setLabel(String.valueOf(mapaHistoricoAnomalias.get(activity.anomTypes.get(2)).size()));
				
				PinLegenda_Layout pinLegenda_messaging = (PinLegenda_Layout) view.findViewById(R.id.pinLegenda_messaging);
				if(mapaHistoricoAnomalias.containsKey((activity.anomTypes.get(3))))
					pinLegenda_messaging.setLabel(String.valueOf(mapaHistoricoAnomalias.get(activity.anomTypes.get(3)).size()));
				
				PinLegenda_Layout pinLegenda_cobertura = (PinLegenda_Layout) view.findViewById(R.id.pinLegenda_cobertura);
				if(mapaHistoricoAnomalias.containsKey((activity.anomTypes.get(4))))
					pinLegenda_cobertura.setLabel(String.valueOf(mapaHistoricoAnomalias.get(activity.anomTypes.get(4)).size()));
				
				PinLegenda_Layout pinLegenda_outra = (PinLegenda_Layout) view.findViewById(R.id.pinLegenda_outra);
				if(mapaHistoricoAnomalias.containsKey((activity.anomTypes.get(5))))
					pinLegenda_outra.setLabel(String.valueOf(mapaHistoricoAnomalias.get(activity.anomTypes.get(5)).size()));
			}
		}
		
//		map = ((SupportMapFragment) getFragmentManager().findFragmentById(
//		R.id.map)).getMap();
		
		mapView = (MapView) view.findViewById(R.id.mapview);
		mapView.onCreate(savedInstanceState);
		
		map = mapView.getMap();
		if(map != null) {
			map.getUiSettings().setMyLocationButtonEnabled(true);
			map.setMyLocationEnabled(true);
	 
			// Needs to call MapsInitializer before doing any CameraUpdateFactory calls
			MapsInitializer.initialize(this.getActivity());
	
			// Updates the location and zoom of the MapView
//			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(40.625854, -8.64741), 10);
//			map.animateCamera(cameraUpdate);
		}

		return view;
	}
	
	protected void filterAnomalies(int anomalyTypeToFilter) {
//		<string-array name="anomalias">
//      <item>@string/anomalias_todas</item>
//      <item>@string/voz</item>
//      <item>@string/internet</item>
//      <item>@string/messaging</item>
//      <item>@string/cobertura</item>
//      <item>@string/outra</item>
//  	</string-array>
  
		if(historicoAnomalias != null) {			
			try {				
				/* All anomalies */
				if(anomalyTypeToFilter == 0) {
//					Collection<List<Marker>> list = mapaMarkersHistoricoAnomalias.values();
//					for(List<Marker> markerList : list)
//						for(Marker marker : markerList)
//							marker.setVisible(true);
					Iterator<Entry<String, List<Marker>>> it = mapaMarkersHistoricoAnomalias.entrySet().iterator();
					while (it.hasNext()) {
						List<Marker> markerList = it.next().getValue();
						for(Marker marker : markerList)
							marker.setVisible(true);
					}
				} else {
					Set<String> set = mapaMarkersHistoricoAnomalias.keySet();
					for(String key : set) {
						boolean visible = key.equals(activity.anomTypes.get(anomalyTypeToFilter));

						List<Marker> list = mapaMarkersHistoricoAnomalias.get(key);
						for(Marker marker : list)
							marker.setVisible(visible);
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}		
	}

	@Override
	public void onResume() {
		mapView.onResume();
		spinnerFilter.setSelection(0);
		filterFirstExecution = true;
		super.onResume();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
 
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		try {
			if (map != null && historicoAnomalias != null && historicoAnomalias.size() > 0) {
				LatLng cur_Latlng = null;
				for(HistoricoAnomaliasItem anomaly : historicoAnomalias) {
					Location anomalyLocation = anomaly.get_location();
					cur_Latlng = new LatLng(anomalyLocation.getLatitude(), anomalyLocation.getLongitude());
					
					BitmapDescriptor icon = null;
					/* Map icon (ballon) */
					int anomIconResource = activity.getAnomalyIconResource(anomaly.getType(), true);
					if(anomIconResource != Utils.INVALID_RESOURCE)
						icon = BitmapDescriptorFactory
								.fromResource(anomIconResource);
					
					/* Set marker options */
					MarkerOptions markerOps = new MarkerOptions()
						.position(cur_Latlng)
	//					.title("Anomalia")	-> Sets title
	//					.snippet("Voz")		-> Sets snippet
						.icon(icon);
					Marker marker = map.addMarker(markerOps);
					
					String type = anomaly.getType();
					if(mapaMarkersHistoricoAnomalias.containsKey(type))
						mapaMarkersHistoricoAnomalias.get(type).add(marker);
					else {
						List<Marker> list = new ArrayList<Marker>();
						list.add(marker);
						mapaMarkersHistoricoAnomalias.put(type, list);
					}
				}
				if(cur_Latlng != null) {
					CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(cur_Latlng, Utils.ZOOM_VALUE);
					map.animateCamera(cameraUpdate);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
//		if (map != null) {
//			Log.d(getClass().getSimpleName(), "Map ready for use!");
//			map.setMyLocationEnabled(true);
//
//			/* Dummy coordinates: 40.625854,-8.64741 */
//			LatLng cur_Latlng = new LatLng(40.625854, -8.64741);
//			/* Map icon (ballon) */
//			BitmapDescriptor icon = BitmapDescriptorFactory
//					.fromResource(R.drawable.pin_mapa_voz);
//			/* Set marker options */
//			MarkerOptions markerOps = new MarkerOptions()
//				.position(cur_Latlng)
////				.title("Anomalia")	-> Sets title
////				.snippet("Voz")		-> Sets snippet
//				.icon(icon);
//			map.addMarker(markerOps);
//			map.moveCamera(CameraUpdateFactory.newLatLng(cur_Latlng));
//			map.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_VALUE));
//		}
		
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

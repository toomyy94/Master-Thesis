package pt.ptinovacao.arqospocket.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import pt.ptinovacao.arqospocket.adapters.Adapter_spinner;
import pt.ptinovacao.arqospocket.service.structs.MyLocation;
import pt.ptinovacao.arqospocket.DialogRowItem;
import pt.ptinovacao.arqospocket.HistoricoTestesItem;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.activities.ActivityTestesHistorico;
import pt.ptinovacao.arqospocket.map.PinLegenda_Layout;
import pt.ptinovacao.arqospocket.util.LocaleHelper;
import pt.ptinovacao.arqospocket.util.Utils;
import android.app.Activity;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

public class FragmentTestesHistorico_Mapa extends Fragment {

	private static final String TAG = "FragTestesHistMap";
	private View view;
	private MapView mapView;
	private GoogleMap map;
	private Location spotLocation = null;
	private List<MarkerOptions> markersList;
	public static String tag;
	private String language_code;
	private ImageView map_image;
	
	private Spinner spinnerFilter;
	private LinearLayout occurrencesLayout;
	private boolean mapStretched;
	private ImageView mapZoomer;
	
	private ActivityTestesHistorico activity;
	private ArrayList<HistoricoTestesItem> historicoTestes;
	private Map<String, List<HistoricoTestesItem>> mapaHistoricoTestes;
	private Map<String,List<Marker>> mapaMarkersHistoricoTestes;
	private boolean filterFirstExecution;
	ArrayList<DialogRowItem> dialogItems = new ArrayList<DialogRowItem>();
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		this.activity = (ActivityTestesHistorico) activity;	
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tag = getTag();
		markersList = new ArrayList<MarkerOptions>();
		mapStretched = false;
		
		this.historicoTestes = this.activity.getHistory();
		this.mapaHistoricoTestes = this.activity.getHistoryMap();	
		mapaMarkersHistoricoTestes = new HashMap<String, List<Marker>>();
		
		filterFirstExecution = true;

		if(dialogItems != null && dialogItems.size() == 0) {
			for(int i = 0; i < activity.testTypes.size(); i++) {
				dialogItems.add(new DialogRowItem(activity.testTypes.get(i), Utils.Tests_icons[i]));
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

		view = inflater.inflate(R.layout.fragment_testes_historico_mapa,
				container, false);
		Adapter_spinner spinnerAdapter = new Adapter_spinner(activity, dialogItems);
		spinnerAdapter.setDropDownViewResource(R.layout.rowdrop);

		//Added to internatianalization
		language_code = LocaleHelper.getLanguage(getActivity().getApplicationContext());

		map_image = (ImageView) view.findViewById(R.id.bt_swipe_list);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			if (language_code.equals("pt"))
				map_image.setBackground(getResources().getDrawable(R.drawable.tit_mapa));
			else if (language_code.equals("en"))
				map_image.setBackground(getResources().getDrawable(R.drawable.tit_mapa_en));
			else if (language_code.equals("fr"))
				map_image.setBackground(getResources().getDrawable(R.drawable.tit_mapa_fr));
			else map_image.setBackground(getResources().getDrawable(R.drawable.tit_mapa_en));
		}
		else{
			if (language_code.equals("pt"))
				map_image.setBackgroundDrawable(getResources().getDrawable(R.drawable.tit_mapa));
			else if (language_code.equals("en"))
				map_image.setBackgroundDrawable(getResources().getDrawable(R.drawable.tit_mapa_en));
			else if (language_code.equals("fr"))
				map_image.setBackgroundDrawable(getResources().getDrawable(R.drawable.tit_mapa_fr));
			else map_image.setBackgroundDrawable(getResources().getDrawable(R.drawable.tit_mapa_en));
		}
		
		Spinner spinner = (Spinner) view.findViewById(R.id.spinner_filter);
		//Adicionado
		spinner.setAdapter(spinnerAdapter);
		
//		ListView list = (ListView) view.findViewById(R.id.list);
//		
//		/* add dummy entries */
//		ArrayList<HistoricoAnomaliasItem> testList = new ArrayList<HistoricoAnomaliasItem>();
//		HistoricoAnomaliasItem d1 = new HistoricoAnomaliasItem(R.drawable.anom_icon_menu_voz, "Voz", null, "14 fev 2014, 18:45", null, null);
//		HistoricoAnomaliasItem d2 = new HistoricoAnomaliasItem(R.drawable.anom_icon_menu_voz, "Internet", null, "14 fev 2014, 15:28", null, null);
//		HistoricoAnomaliasItem d3 = new HistoricoAnomaliasItem(R.drawable.anom_icon_menu_voz, "Messaging", null, "13 fev 2014, 22:15", null, null);
//		
//		testList.add(d1);
//		testList.add(d2);
//		testList.add(d3);
//		testList.add(d1);
//		testList.add(d2);
//		testList.add(d3);
//		
//		Adapter_List_Historico_Anomalias adapter = new Adapter_List_Historico_Anomalias(getActivity(), testList);		
//		list.setAdapter(adapter);
		
		occurrencesLayout = (LinearLayout) view.findViewById(R.id.above_map_layout);
		
		mapZoomer = (ImageView) view.findViewById(R.id.icon_zoom_mapa);
		mapZoomer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				
				LatLng center = map.getCameraPosition().target;
				Log.i(TAG, "Map center: \nLat: " + center.latitude + "\nLong: " + center.longitude);
				
				mapStretched = !mapStretched;
				
				if(mapStretched) {
					occurrencesLayout.setVisibility(View.GONE);
				} else {
					occurrencesLayout.setVisibility(View.VISIBLE);
				}
				
			}
		});
		
		spinnerFilter = (Spinner) view.findViewById(R.id.spinner_filter);
		
		if(historicoTestes!= null) {
			spinnerFilter.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					if(filterFirstExecution)
						filterFirstExecution = false;
					else
						filterTests(arg2);
				}
	
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {				
				}
			});
			
			if(mapaHistoricoTestes != null && mapaHistoricoTestes.size() > 0) {
				/* Set number of anomalies per type */
				PinLegenda_Layout pinLegenda_voz = (PinLegenda_Layout) view.findViewById(R.id.pinLegenda_rede);
				if(mapaHistoricoTestes.containsKey((activity.testTypes.get(1))))
						pinLegenda_voz.setLabel(String.valueOf(mapaHistoricoTestes.get(activity.testTypes.get(1)).size()));
				
				PinLegenda_Layout pinLegenda_internet = (PinLegenda_Layout) view.findViewById(R.id.pinLegenda_wifi);
				if(mapaHistoricoTestes.containsKey((activity.testTypes.get(2))))
					pinLegenda_internet.setLabel(String.valueOf(mapaHistoricoTestes.get(activity.testTypes.get(2)).size()));
				
				PinLegenda_Layout pinLegenda_messaging = (PinLegenda_Layout) view.findViewById(R.id.pinLegenda_misto);
				if(mapaHistoricoTestes.containsKey((activity.testTypes.get(3))))
					pinLegenda_messaging.setLabel(String.valueOf(mapaHistoricoTestes.get(activity.testTypes.get(3)).size()));
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
	
	protected void filterTests(int testTypeToFilter) {
//		<string-array name="testes">
//      <item>@string/testes_todos</item>
//      <item>@string/rede</item>
//      <item>@string/wifi</item>
//      <item>@string/misto</item>
//  	</string-array>
  
		if(historicoTestes != null) {			
			try {				
				/* All tests */
				if(testTypeToFilter == 0) {
					Iterator<Entry<String, List<Marker>>> it = mapaMarkersHistoricoTestes.entrySet().iterator();
					while (it.hasNext()) {
						List<Marker> markerList = it.next().getValue();
						for(Marker marker : markerList)
							marker.setVisible(true);
					}
				} else {
					Set<String> set = mapaMarkersHistoricoTestes.keySet();
					for(String key : set) {
						boolean visible = key.equals(activity.testTypes.get(testTypeToFilter));

						List<Marker> list = mapaMarkersHistoricoTestes.get(key);
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
		
		try {
			if (map != null && historicoTestes != null && historicoTestes.size() > 0) {
				LatLng cur_Latlng = null;
				for(HistoricoTestesItem test : historicoTestes) {
					MyLocation testLocation = test.getLocation();
					cur_Latlng = new LatLng(testLocation.get_lat(), testLocation.get_lgt());
					
					BitmapDescriptor icon = null;
					/* Map icon (ballon) */
					int testIconResource = activity.getTestPinResource(test.getTechnologyType(), true, test.getTest_state());
					if(testIconResource != Utils.INVALID_RESOURCE)
						icon = BitmapDescriptorFactory
								.fromResource(testIconResource);
					
					/* Set marker options */
					MarkerOptions markerOps = new MarkerOptions()
						.position(cur_Latlng)
	//					.title("Anomalia")	-> Sets title
	//					.snippet("Voz")		-> Sets snippet
						.icon(icon);
					Marker marker = map.addMarker(markerOps);
					
					String type = test.getType();
					if(mapaMarkersHistoricoTestes.containsKey(type))
						mapaMarkersHistoricoTestes.get(type).add(marker);
					else {
						List<Marker> list = new ArrayList<Marker>();
						list.add(marker);
						mapaMarkersHistoricoTestes.put(type, list);
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

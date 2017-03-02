package pt.ptinovacao.arqospocket.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.Map.Entry;

import pt.ptinovacao.arqospocket.DetailItem;
import pt.ptinovacao.arqospocket.GroupTaskDetails;
import pt.ptinovacao.arqospocket.HistoricoTestesItem;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.activities.ActivityTestesHistorico;
import pt.ptinovacao.arqospocket.adapters.AdapterListViewTaskDetails;
import pt.ptinovacao.arqospocket.adapters.AdapterListViewTestesDetalhes;
import pt.ptinovacao.arqospocket.core.RowItemTestesDetalhes;
import pt.ptinovacao.arqospocket.dialog.AudioPlayerDialog;
import pt.ptinovacao.arqospocket.dialog.TestDetailsDialog;
import pt.ptinovacao.arqospocket.interfaces.IFragmentPager;
import pt.ptinovacao.arqospocket.interfaces.IUpdateMap;
import pt.ptinovacao.arqospocket.map.MapUtils;
import pt.ptinovacao.arqospocket.service.enums.EConnectionTechnology;
import pt.ptinovacao.arqospocket.service.enums.ETestTaskState;
import pt.ptinovacao.arqospocket.service.interfaces.ITaskResult;
import pt.ptinovacao.arqospocket.service.structs.MyLocation;
import pt.ptinovacao.arqospocket.util.ColorPercent;
import pt.ptinovacao.arqospocket.util.Utils;

public class FragmentTestesHistorico_Entry extends Fragment implements IUpdateMap {

	private static final String HISTORY_ITEM_POSITION = "HistoryItemPosition";
	private static final String TAG = "FragTestesHistEntry";
	ColorPercent colorPercent = new ColorPercent();
	int name;
	private View view;
//	private MapView mapView;
	private GoogleMap map;
	private Location spotLocation = null;
	private List<MarkerOptions> markersList;
	public static String tag;
	private ListView listView;
	private TextView testTitle, date;	
	List<RowItemTestesDetalhes> rowItemstestesdetalhes;
	private List<ITaskResult> taskResults;	
	private SparseArray<GroupTaskDetails> groups;
	RowItemTestesDetalhes item;
	int state;
	private ActivityTestesHistorico activity;
	private ArrayList<HistoricoTestesItem> historicoTestes;
	private HistoricoTestesItem test;
	private MyLocation testLocation;
	private LatLng cur_Latlng;
	private int currentItem = 0;
	private Map<String,List<Marker>> mapaMarkersHistoricoTestes;
	private boolean setupMapAfterInit = false;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		this.activity = (ActivityTestesHistorico) activity;
		this.historicoTestes = this.activity.getHistory(); 
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tag = getTag();
		markersList = new ArrayList<MarkerOptions>();
		
		mapaMarkersHistoricoTestes = new HashMap<String, List<Marker>>();
		
		Bundle args = getArguments();		
		if(args != null && args.containsKey(HISTORY_ITEM_POSITION)) {
			currentItem = args.getInt(HISTORY_ITEM_POSITION);
			test = historicoTestes.get(currentItem);
			
			/* Set anomaly location */
			testLocation = test.getLocation();
			Log.i(TAG, "TestLocation: " + testLocation.get_lat() + ", " + testLocation.get_lgt());
//			testLocation = validateLocation(testLocation);
			
			cur_Latlng = new LatLng(testLocation.get_lat(), testLocation.get_lgt());
		}
	}
	
	private MyLocation validateLocation(MyLocation testLocation) {
		try {
			if(testLocation != null 
					&& testLocation.get_lat() == 0.0
					&& testLocation.get_lgt() == 0.0) {
				
				/* Location manager */
				LocationManager locationMgr = 
				          (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
				
				/* Get best last known location */
				Location lastKnownLocation = MapUtils.getBestLastKnownLocation(locationMgr);
				if(lastKnownLocation != null) {
					Log.i(TAG, "TestLocation: " + lastKnownLocation.getLatitude() + ", " + lastKnownLocation.getLongitude());
					testLocation = new MyLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return testLocation;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		((ViewGroup) view.getParent()).removeView(view);
	}

	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_testes_historico_entry,
				container, false);


		testTitle = (TextView) view.findViewById(R.id.teste_entry_title);
		if (test != null) {
			testTitle.setText(test.getTest_name());
			if(test.getTechnologyType() == EConnectionTechnology.NA || test.getTechnologyType() == null) {
				testTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			} else {
				int resource = activity.getTestBigRowIconResource(test.getTechnologyType(), test.getTest_state());
				testTitle.setCompoundDrawablesWithIntrinsicBounds(0, resource, 0, 0);
			}
		}
		
		date = (TextView) view.findViewById(R.id.test_entry_date);
		date.setText(test.getDate_end_execution_string());
		
//		mapView = (MapView) view.findViewById(R.id.mapview);
//		mapView.onCreate(savedInstanceState);
//		map = mapView.getMap();
//		if(map != null) {
//			map.getUiSettings().setMyLocationButtonEnabled(true);
//			map.setMyLocationEnabled(true);
//	 
//			// Needs to call MapsInitializer before doing any CameraUpdateFactory calls
//			MapsInitializer.initialize(this.getActivity());
//		}
		
//		listExp = (ExpandableListView) view.findViewById(R.id.list);
//		listExp.setGroupIndicator(null);
		listView = (ListView) view.findViewById(R.id.list);

		/* add dummy entries */
//		groups = new SparseArray<GroupTaskDetails>();
//		createData();

//		Adapter_ExpandableList adapter = new Adapter_ExpandableList(
//				getActivity(), groups);
			
		rowItemstestesdetalhes = new ArrayList<RowItemTestesDetalhes>();
		if(historicoTestes != null) {
//			HistoricoTestesItem test = historicoTestes.get(currentItem);		
	
			if(test.getTask_list() != null && test.getTask_list().size() > 0) {
				taskResults = test.getTask_list();
				Log.i(TAG, "testsHistory: Task_list size: " + test.getTask_list().size());
				for(ITaskResult taskResult : taskResults) {
					/** TODO
					 * fill arraylist 'rowItemstestesdetalhes' with 'taskResult' tasks */
					
					
					switch(taskResult.get_run_task_state()) {
					case DONE:
						activity.getTestPercentageIconResource(100, taskResult.get_task_state());
						if(taskResult.get_task_state() == ETestTaskState.OK)
							colorPercent.TestType(R.string.test_completed, 100);
						else
							colorPercent.TestType(R.string.test_failed, 0);
						break;
					case RUNNING:
//						activity.getTestPercentageIconResource(50, taskResult.get_task_state());
						if(taskResult.get_task_state() == ETestTaskState.OK)
							colorPercent.TestType(R.string.test_completed, 50);
						else
							colorPercent.TestType(R.string.test_failed, 50);
						break;
					case WAITING:
//						activity.getTestPercentageIconResource(0, taskResult.get_task_state());
						if(taskResult.get_task_state() == ETestTaskState.OK)
							colorPercent.TestType(R.string.test_completed, 0);
						else
							colorPercent.TestType(R.string.test_failed, 0);
						break;
					}
					
//					colorPercent.TestType(R.string.concluido, 100);
					item = new RowItemTestesDetalhes(colorPercent.getimage(),
							colorPercent.getpercent(), state, taskResult.get_task_name(),
							true, colorPercent.getColor());
					rowItemstestesdetalhes.add(item);
				}
			} else {
				if(test.getTask_list() == null)
					Log.i(TAG, "testsHistory: Task_list null");
				else
					Log.i(TAG, "testsHistory Task_list size: " + test.getTask_list().size());
			}
		}

		groups = new SparseArray<GroupTaskDetails>();
//		createData();

//		final Adapter_ExpandableList adapter = new Adapter_ExpandableList(
//				getActivity(), groups, this);
		
		AdapterListViewTestesDetalhes adapter = new AdapterListViewTestesDetalhes(activity, rowItemstestesdetalhes);
		
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(onClickListener);
//		listExp.setAdapter(adapter);		
//		listExp.setOnGroupExpandListener(new OnGroupExpandListener() {
//
//			public void onGroupExpand(int groupPosition) {
//
//				int len = adapter.getGroupCount();
//
//				for (int i = 0; i < len; i++) {
//
//					if (i != groupPosition) {
//
//						listExp.collapseGroup(i);
//					}
//				}
//			}
//		});
		
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
			if(currentItem != (historicoTestes.size() - 1)) {
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

		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		map = activity.initMap();
		
//		try {
//			new ReverseGeocodingTask().execute();			
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
		
		if(setupMapAfterInit) {
			updateMap();
			setupMapAfterInit = false;	
		}
	}
	
	OnItemClickListener onClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			List<DetailItem> taskDetails = new ArrayList<DetailItem>();
			ITaskResult taskResult;
			
			if(taskResults == null)
				taskResults = test.getTask_list();
			
			if(taskResults != null && taskResults.size() > 0 && position < taskResults.size()) {
				Log.i(TAG, "testsHistory: Task_list size: " + test.getTask_list().size());
				taskResult = taskResults.get(position);
				for(Entry<String, String> taskEntry : taskResult.get_task_results(activity).entrySet()) {
					taskDetails.add(new DetailItem(taskEntry.getKey(), taskEntry.getValue()));
				}
			}

			AdapterListViewTaskDetails.OnPlayClickListener onPlayClickListener = new AdapterListViewTaskDetails.OnPlayClickListener() {
				@Override
				public void onPlayClickListener(String filePath) {
					AudioPlayerDialog playerDialog = new AudioPlayerDialog(activity, filePath);
					playerDialog.show();
				}
			};
			
			AdapterListViewTaskDetails taskDetailsAdapter = new AdapterListViewTaskDetails(activity, taskDetails, onPlayClickListener);
			
			/* Test details dialog builder. */
			TestDetailsDialog dialog = new TestDetailsDialog(activity,
					R.layout.dialog_list_testes_detalhes, 
					taskDetailsAdapter, 
					rowItemstestesdetalhes.get(position));
			dialog.show();
		}
	};

//	public void createData() {
//		for (int j = 0; j < rowItemstestesdetalhes.size(); j++) {
//			GroupTaskDetails group = new GroupTaskDetails(
//					rowItemstestesdetalhes.get(j));
//			group.children.add(new DetailItem("Impulsos medidos", "45"));
//			group.children.add(new DetailItem("Tempo de espera", "00:25"));
//			group.children.add(new DetailItem());
//			groups.append(j, group);
//		}
//	}
	
	@Override
	public void updateMap() {
		if (map != null && test != null 
				&& historicoTestes != null && historicoTestes.size() > 0) {
			map.clear();
			cur_Latlng = null;
			for(HistoricoTestesItem test : historicoTestes) {
				MyLocation anomLocation = test.getLocation();
//				anomLocation = validateLocation(anomLocation);
				cur_Latlng = new LatLng(anomLocation.get_lat(), anomLocation.get_lgt());
				
				BitmapDescriptor icon = null;
				
				int anomIconResource;
				if(test.equals(this.test))
					anomIconResource = activity.getTestBigPinResource(test.getTechnologyType(), test.getTest_state());
				else
					anomIconResource = activity.getTestPinResource(test.getTechnologyType(), false, test.getTest_state());
				
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
					
					String type = test.getType();
					if(mapaMarkersHistoricoTestes.containsKey(type))
						mapaMarkersHistoricoTestes.get(type).add(marker);
					else {
						List<Marker> list = new ArrayList<Marker>();
						list.add(marker);
						mapaMarkersHistoricoTestes.put(type, list);
					}
				}
			}
			
			cur_Latlng = new LatLng(testLocation.get_lat(), testLocation.get_lgt());
			if(cur_Latlng != null) {
				float zoomLevel = Utils.isZoomFartherThanDefault(map.getCameraPosition().zoom) ? Utils.ZOOM_VALUE : map.getCameraPosition().zoom;
				CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(cur_Latlng, zoomLevel);
				map.animateCamera(cameraUpdate);
			}
		} else {
			setupMapAfterInit = true;
		}
	}

//	@Override
//	public void onResume() {
//		mapView.onResume();
//		super.onResume();
//		
////		List<DetailItem> taskDetails = new ArrayList<DetailItem>();
////		taskDetails.add(new DetailItem("Impulsos medidos", "45"));
////		taskDetails.add(new DetailItem("Tempo de espera", "00:25"));
////		
////		AdapterListViewTaskDetails taskDetailsAdapter = new AdapterListViewTaskDetails(activity, taskDetails);
////		
////		/* Instructions dialog builder. */
////		CustomListDialog dialog = new CustomListDialog(activity,
////				R.layout.dialog_list_testes_detalhes, taskDetailsAdapter);
////		dialog.show();
//	}

//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		mapView.onDestroy();
//	}
//
//	@Override
//	public void onLowMemory() {
//		super.onLowMemory();
//		mapView.onLowMemory();
//	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

////		if (map != null) {
////			Log.d(getClass().getSimpleName(), "Map ready for use!");
////			map.setMyLocationEnabled(true);
////
////			/* Dummy coordinates: 40.625854,-8.64741 */
////			LatLng cur_Latlng = new LatLng(40.625854, -8.64741);
////			/* Map icon (ballon) */
////			BitmapDescriptor icon = BitmapDescriptorFactory
////					.fromResource(R.drawable.pin_mapa_voz);
////			/* Set marker options */
////			MarkerOptions markerOps = new MarkerOptions().position(cur_Latlng)
////			// .title("Anomalia") -> Sets title
////			// .snippet("Voz") -> Sets snippet
////					.icon(icon);
////			map.addMarker(markerOps);
////			map.moveCamera(CameraUpdateFactory.newLatLng(cur_Latlng));
////			map.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_VALUE));
////		}
//		
//		if (map != null && test != null 
//				&& historicoTestes != null && historicoTestes.size() > 0) {
//			map.clear();
//			cur_Latlng = null;
//			for(HistoricoTestesItem test : historicoTestes) {
//				MyLocation anomLocation = test.getLocation();
////				anomLocation = validateLocation(anomLocation);
//				cur_Latlng = new LatLng(anomLocation.get_lat(), anomLocation.get_lgt());
//				
//				BitmapDescriptor icon = null;
//				
//				int anomIconResource;
//				if(test.equals(this.test))
//					anomIconResource = activity.getTestBigPinResource(test.getTechnologyType(), test.getTest_state());
//				else
//					anomIconResource = activity.getTestPinResource(test.getTechnologyType(), false, test.getTest_state());
//				
//				if(anomIconResource != Utils.INVALID_RESOURCE) {
//					/* Map icon (ballon) */
//					icon = BitmapDescriptorFactory
//						.fromResource(anomIconResource);
//				
//					/* Set marker options */
//					MarkerOptions markerOps = new MarkerOptions()
//						.position(cur_Latlng)
//	//					.title("Anomalia")	-> Sets title
//	//					.snippet("Voz")		-> Sets snippet
//						.icon(icon);
//					Marker marker = map.addMarker(markerOps);
//					
//					String type = test.getType();
//					if(mapaMarkersHistoricoTestes.containsKey(type))
//						mapaMarkersHistoricoTestes.get(type).add(marker);
//					else {
//						List<Marker> list = new ArrayList<Marker>();
//						list.add(marker);
//						mapaMarkersHistoricoTestes.put(type, list);
//					}
//				}
//			}
//			
//			cur_Latlng = new LatLng(testLocation.get_lat(), testLocation.get_lgt());
//			if(cur_Latlng != null) {
//				CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(cur_Latlng, Utils.ZOOM_VALUE);
//				map.animateCamera(cameraUpdate);
//			}
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

//	@Override
//	public void expandOrCollapseList(int groupPosition) {
//		listExp.performItemClick(
//				listExp.getAdapter().getView(groupPosition, null, null),
//				groupPosition, listExp.getItemIdAtPosition(groupPosition));
//		
//		/*
//		 * Código abaixo seria o correto, mas não inclui o scroll automático
//		 * para mostrar os sub-items da lista (necessário devido ao mapa abaixo
//		 * da lista).
//		 * 
//		 * if (listExp.isGroupExpanded(groupPosition)) {
//		 * listExp.collapseGroup(groupPosition); } else {
//		 * listExp.expandGroup(groupPosition); }
//		 */
//	}
}

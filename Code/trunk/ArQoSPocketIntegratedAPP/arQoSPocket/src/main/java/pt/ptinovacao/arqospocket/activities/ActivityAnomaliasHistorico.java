package pt.ptinovacao.arqospocket.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pt.ptinovacao.arqospocket.service.enums.EMobileNetworkMode;
import pt.ptinovacao.arqospocket.service.enums.EMobileState;
import pt.ptinovacao.arqospocket.service.enums.ENetworkAction;
import pt.ptinovacao.arqospocket.service.interfaces.IAnomaliesHistory;
import pt.ptinovacao.arqospocket.service.interfaces.IService;
import pt.ptinovacao.arqospocket.service.interfaces.IUI;
import pt.ptinovacao.arqospocket.ArqosActivity;
import pt.ptinovacao.arqospocket.HistoricoAnomaliasItem;
import pt.ptinovacao.arqospocket.MyApplication;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.fragments.FragmentHistoricoAnomalias_Pager;
import pt.ptinovacao.arqospocket.fragments.FragmentHistorico_EntryPager;
import pt.ptinovacao.arqospocket.interfaces.IFragmentChange;
import pt.ptinovacao.arqospocket.interfaces.IFragmentPager;
import pt.ptinovacao.arqospocket.interfaces.IHistoryProvider;
import pt.ptinovacao.arqospocket.util.AnomaliasIcons_Enum;
import pt.ptinovacao.arqospocket.util.Homepage;
import pt.ptinovacao.arqospocket.util.MenuOption;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

public class ActivityAnomaliasHistorico extends ArqosActivity implements
IHistoryProvider, IFragmentChange, IFragmentPager, IUI {

	private static final String HISTORY_ITEM_POSITION = "HistoryItemPosition";
	private ArrayList<HistoricoAnomaliasItem> historicoAnomalias;
	private FragmentManager fragMgr;
	private FragmentHistorico_EntryPager pager;
	private List<IAnomaliesHistory> anomaliesHistory;
	private Map<String, List<HistoricoAnomaliasItem>> anomaliesHistoryMap;
	public List<String> anomTypes;
	public int anomalyTypeToFilter;
	MenuOption HomeP;
	Homepage home;
	
	private MapView mapView;
	private GoogleMap map;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.onActionBarSetTitle(getResources().getString(R.string.anomaly_log));
		super.setMenuOption(MenuOption.HistoricoAnomalias);
		setContentView(R.layout.activity_historico);
		home = new Homepage (this);
		fragMgr = getSupportFragmentManager();
		
		fragMgr.beginTransaction()
		.add(R.id.framelayout_historico, new FragmentHistoricoAnomalias_Pager())
		.commit();
		
		anomTypes = Arrays.asList(getResources().getStringArray(R.array.anomalias));
		
		// regista para receber o histórico de anomalias
		MyApplication MyApplicationRef = (MyApplication) getApplicationContext();
		IService iService = MyApplicationRef.getEngineServiceRef();
		
		anomaliesHistory = iService.get_all_anomalies_history();
		
		/* Get histórico de anomalias */
		historicoAnomalias = acquireHistoricoAnomalias();
		
		if(hasAnomaliesHistory()) {
			anomaliesHistoryMap = new HashMap<String, List<HistoricoAnomaliasItem>>();
		
			for(HistoricoAnomaliasItem anom : historicoAnomalias) {
				String type = anom.getType();
				if(anomTypes.contains(type)) {
					if(anomaliesHistoryMap.containsKey(type))
						anomaliesHistoryMap.get(type).add(anom);
					else {
						List<HistoricoAnomaliasItem> list = new ArrayList<HistoricoAnomaliasItem>();
						list.add(anom);
						anomaliesHistoryMap.put(type, list);
					}
				}
			}
		}

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.onCreate(savedInstanceState);
		mapView.setVisibility(View.GONE);
	}
	
	public void showMap() {
		mapView.setVisibility(View.VISIBLE);
	}
	
	public void hideMap() {
		mapView.setVisibility(View.GONE);
	}
	
	public GoogleMap initMap() {
		showMap();
		
		map = mapView.getMap();
		if (map != null) {
			map.getUiSettings().setMyLocationButtonEnabled(true);
			map.setMyLocationEnabled(true);

			// Needs to call MapsInitializer before doing any
			// CameraUpdateFactory calls
			MapsInitializer.initialize(this);
		}
		return map;
	}
	
	@Override
	public void onResume() {
		mapView.onResume();
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
	
	public boolean hasAnomaliesHistory() {
		return anomaliesHistory != null && anomaliesHistory.size() > 0;
	}
	
	public ArrayList<HistoricoAnomaliasItem> acquireHistoricoAnomalias() {
		ArrayList<HistoricoAnomaliasItem> testList = null;
		if(hasAnomaliesHistory()) {
			testList = new ArrayList<HistoricoAnomaliasItem>();
			
			for(IAnomaliesHistory anomaly : anomaliesHistory) {
				HistoricoAnomaliasItem anomalyItem = new HistoricoAnomaliasItem(
						anomaly.get_logo_id(),
						AnomaliasIcons_Enum.getIconOfAnom(this, anomaly.get_logo_id()),
						anomaly.get_anomalie_id(),
						anomaly.get_anomalie_Details_id(),
						anomaly.get_anomalie_report_date(), anomaly.get_location(),
						anomaly.get_report_msg());
				testList.add(anomalyItem);
			}
		}
		
//		/* add dummy entries */
//		HistoricoAnomaliasItem d1 = new HistoricoAnomaliasItem(R.drawable.icon_menu_voz, "Voz", null, "14 fev 2014, 18:45", null, null);
//		HistoricoAnomaliasItem d2 = new HistoricoAnomaliasItem(R.drawable.icon_menu_voz, "Internet", null, "14 fev 2014, 15:28", null, null);
//		HistoricoAnomaliasItem d3 = new HistoricoAnomaliasItem(R.drawable.icon_menu_voz, "Messaging", null, "13 fev 2014, 22:15", null, null);
//		
//		testList.add(d1);
//		testList.add(d2);
//		testList.add(d3);
		
		return testList;
	}
	
	@Override
	public void changeFragment(int position) {
		try {
			/* Check correct position, regarding 'anomalyTypeToFilter' */
			if(anomalyTypeToFilter != 0) {
				List<HistoricoAnomaliasItem> list;
				if((list = anomaliesHistoryMap.get(anomTypes.get(anomalyTypeToFilter))) != null) {
					HistoricoAnomaliasItem item = list.get(position);
					position = historicoAnomalias.indexOf(item);
				}
			}
			
			Bundle bundle = new Bundle();
			bundle.putInt(HISTORY_ITEM_POSITION, position);
			
			pager = new FragmentHistorico_EntryPager();
			pager.setArguments(bundle);
			
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.framelayout_historico, pager)
			.addToBackStack(null)
			.commit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void previousPage() {
		if(pager != null) {
			pager.previousPage();
		}
	}
	
	@Override
	public void nextPage() {
		if(pager != null) {
			pager.nextPage();
		}
	}

	@Override
	public ArrayList<HistoricoAnomaliasItem> getHistory() {
		if(historicoAnomalias != null)
			/* Returns a copy of the anomalies history list */
			return new ArrayList<HistoricoAnomaliasItem>(historicoAnomalias);
		else 
			return new ArrayList<HistoricoAnomaliasItem>();
	}
	
	public Map<String, List<HistoricoAnomaliasItem>> getHistoryMap() {
		if(historicoAnomalias != null)
			/* Returns a copy of the anomalies history map */
			return new HashMap<String, List<HistoricoAnomaliasItem>>(anomaliesHistoryMap);
		else 
			return new HashMap<String, List<HistoricoAnomaliasItem>>();
	}
	
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public ArrayList<HistoricoAnomaliasItem> getFilteredList(ArrayList<HistoricoAnomaliasItem> list, String filter) {
//		ArrayList filteredList = new ArrayList();
//		
//		for(History item : list) {
//			if(item.getType().equals(filter))
//				filteredList.add(item);
//		}
//		
//		return filteredList;		
//	}
	
	public int getAnomalyIconResource(String anomalyType, boolean isMarkerStateOn) {
		int resource = -1;
		
		if(anomTypes.contains(anomalyType)) {
			int anomIndex = anomTypes.indexOf(anomalyType);
			
			switch(anomIndex) {
			case 1: resource = (isMarkerStateOn ? R.drawable.pin_mapa_voz : R.drawable.pin_mapa_voz_off);
				break;
			case 2: resource = (isMarkerStateOn ? resource = R.drawable.pin_mapa_internet : R.drawable.pin_mapa_internet_off);
				break;
			case 3: resource = (isMarkerStateOn ? resource = R.drawable.pin_mapa_messaging : R.drawable.pin_mapa_messaging_off);
				break;
			case 4: resource = (isMarkerStateOn ? resource = R.drawable.pin_mapa_cobertura : R.drawable.pin_mapa_cobertura_off);
				break;
			case 5: resource = (isMarkerStateOn ? resource = R.drawable.pin_mapa_outra : R.drawable.pin_mapa_outra_off);
				break;
			}
		}
		
		return resource;
	}
	
	public int getAnomalyBigIconResource(String anomalyType) {
		int resource = -1;
		
		if(anomTypes.contains(anomalyType)) {
			int anomIndex = anomTypes.indexOf(anomalyType);

			switch(anomIndex) {
			case 1: resource = R.drawable.pin_big_mapa_voz;
				break;
			case 2: resource = R.drawable.pin_big_mapa_internet;
				break;
			case 3: resource = R.drawable.pin_big_mapa_messaging;
				break;
			case 4: resource = R.drawable.pin_big_mapa_cobertura;
				break;
			case 5: resource = R.drawable.pin_big_mapa_outra;
				break;
			}
		}
		
		return resource;
	}

	public void onBackPressed() {
		if (slidingMenuIsOpen()) {
			onSlidingMenuToggle();
		}
		else {
			if(fragMgr == null)
				fragMgr = getSupportFragmentManager();
			
			if(fragMgr.getBackStackEntryCount() > 0) {
				fragMgr.popBackStack();
				overridePendingTransition(0, 0);
			} else {	
				HomeP=home.ReadHome();
				if(HomeP==MenuOption.HistoricoAnomalias){
					finish();
				}else{
				home.TesteTipe(HomeP);
				finish();
			
				super.onBackPressed();
			}}
			hideMap();
		}
	}

	@Override
	public void update_mobile_information(EMobileState mobile_state,
			EMobileNetworkMode network_mode, String operator_code,
			String rx_level, String cid, String lac) {

	}

	@Override
	public void update_wifi_information(String link_speed, String ssid,
			String rx_level, String channel) {

	}

	@Override
	public void update_mobile_params(TreeMap<String, String> keyValueParams) {

	}

	@Override
	public void update_wifi_params(TreeMap<String, String> keyValueParams) {

	}


	@Override
	public void send_pending_tests_ack(ENetworkAction action_state) {

	}

	@Override
	public void send_report_ack(ENetworkAction action_state) {

	}

	@Override
	public void update_test_info() {

	}

	@Override
	public void update_test_task(String test_id) {

	}
}

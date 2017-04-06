package pt.ptinovacao.arqospocket.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pt.ptinovacao.arqospocket.ArqosActivity;
import pt.ptinovacao.arqospocket.HistoricoAnomaliasItem;
import pt.ptinovacao.arqospocket.HistoricoRadiologsItem;
import pt.ptinovacao.arqospocket.MyApplication;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.fragments.FragmentHistoricoAnomalias_Pager;
import pt.ptinovacao.arqospocket.fragments.FragmentHistoricoRadiologs_Pager;
import pt.ptinovacao.arqospocket.fragments.FragmentHistorico_EntryPager;
import pt.ptinovacao.arqospocket.interfaces.IFragmentChange;
import pt.ptinovacao.arqospocket.interfaces.IFragmentPager;
import pt.ptinovacao.arqospocket.interfaces.IHistoryProvider;
import pt.ptinovacao.arqospocket.service.enums.EMobileNetworkMode;
import pt.ptinovacao.arqospocket.service.enums.EMobileState;
import pt.ptinovacao.arqospocket.service.enums.ENetworkAction;
import pt.ptinovacao.arqospocket.service.interfaces.IAnomaliesHistory;
import pt.ptinovacao.arqospocket.service.interfaces.IRadiologsHistory;
import pt.ptinovacao.arqospocket.service.interfaces.IService;
import pt.ptinovacao.arqospocket.service.interfaces.IUI;
import pt.ptinovacao.arqospocket.util.AnomaliasIcons_Enum;
import pt.ptinovacao.arqospocket.util.Homepage;
import pt.ptinovacao.arqospocket.util.MenuOption;
import pt.ptinovacao.arqospocket.util.RadiologsIcons_Enum;

public class ActivityRadiologsHistorico extends ArqosActivity implements
IHistoryProvider, IFragmentChange, IFragmentPager, IUI {

	private static final String HISTORY_ITEM_POSITION = "HistoryItemPosition";
	public ArrayList<HistoricoRadiologsItem> historicoRadiologs;
	private FragmentManager fragMgr;
	private FragmentHistorico_EntryPager pager;
	private List<IRadiologsHistory> radiologsHistory;
	private Map<String, List<HistoricoRadiologsItem>> radiologsHistoryMap;
	public List<String> radioTypes;
	public int radiologsTypeToFilter;
	public IService iService;
	MenuOption HomeP;
	Homepage home;
	
	private MapView mapView;
	private GoogleMap map;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.onActionBarSetTitle(getResources().getString(R.string.radiolog_log));
		super.setMenuOption(MenuOption.HistoricoRadiologs);
		setContentView(R.layout.activity_historico);
		home = new Homepage (this);
		fragMgr = getSupportFragmentManager();
		
		fragMgr.beginTransaction()
		.add(R.id.framelayout_historico, new FragmentHistoricoRadiologs_Pager())
		.commit();
		
		radioTypes = Arrays.asList(getResources().getStringArray(R.array.radiologs));
		
		// regista para receber o histórico de anomalias
		MyApplication MyApplicationRef = (MyApplication) getApplicationContext();
		iService = MyApplicationRef.getEngineServiceRef();
		
		radiologsHistory = iService.get_all_radiologs_history();
		
		/* Get histórico de radiologs */
		historicoRadiologs = acquireHistoricoAnomalias();
		
		if(hasRadiologsHistory()) {
			radiologsHistoryMap = new HashMap<String, List<HistoricoRadiologsItem>>();
		
			for(HistoricoRadiologsItem radio : historicoRadiologs) {
				String type = radio.getType();
				if(radioTypes.contains(type)) {
					if(radiologsHistoryMap.containsKey(type))
						radiologsHistoryMap.get(type).add(radio);
					else {
						List<HistoricoRadiologsItem> list = new ArrayList<HistoricoRadiologsItem>();
						list.add(radio);
						radiologsHistoryMap.put(type, list);
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
	
	public boolean hasRadiologsHistory() {
		return radiologsHistory != null && radiologsHistory.size() > 0;
	}
	
	public ArrayList<HistoricoRadiologsItem> acquireHistoricoAnomalias() {
		ArrayList<HistoricoRadiologsItem> testList = null;
		if(hasRadiologsHistory()) {
			testList = new ArrayList<HistoricoRadiologsItem>();
			
			for(IRadiologsHistory radiolog : radiologsHistory) {
				HistoricoRadiologsItem radiologItem = new HistoricoRadiologsItem(
						radiolog.get_logo(),
						RadiologsIcons_Enum.getIconOfRadiolog(this, radiolog.get_logo()),
                                radiolog.get_radiolog_id(),
                                radiolog.get_radiolog_Details_id(),
                                radiolog.get_radiolog_report_date(),
                                radiolog.get_location(),
                                radiolog.get_report_msg());
				testList.add(radiologItem);
			}
		}
		
//		/* add dummy entries */
//		HistoricoRadiologsItem d1 = new HistoricoRadiologsItem(R.drawable.icon_menu_voz, "Voz", null, "14 fev 2014, 18:45", null, null);
//		HistoricoRadiologsItem d2 = new HistoricoRadiologsItem(R.drawable.icon_menu_voz, "Internet", null, "14 fev 2014, 15:28", null, null);
//		HistoricoRadiologsItem d3 = new HistoricoRadiologsItem(R.drawable.icon_menu_voz, "Messaging", null, "13 fev 2014, 22:15", null, null);
//		
//		testList.add(d1);
//		testList.add(d2);
//		testList.add(d3);
		
		return testList;
	}
	
	@Override
	public void changeFragment(int position) {
		try {
			/* Check correct position, regarding 'radiologsTypeToFilter' */
			if(radiologsTypeToFilter != 0) {
				List<HistoricoRadiologsItem> list;
				if((list = radiologsHistoryMap.get(radioTypes.get(radiologsTypeToFilter))) != null) {
					HistoricoRadiologsItem item = list.get(position);
					position = historicoRadiologs.indexOf(item);
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
	public ArrayList<HistoricoRadiologsItem> getHistory() {
		if(historicoRadiologs != null)
			/* Returns a copy of the anomalies history list */
			return new ArrayList<HistoricoRadiologsItem>(historicoRadiologs);
		else 
			return new ArrayList<HistoricoRadiologsItem>();
	}
	
	public Map<String, List<HistoricoRadiologsItem>> getHistoryMap() {
		if(historicoRadiologs != null)
			/* Returns a copy of the anomalies history map */
			return new HashMap<String, List<HistoricoRadiologsItem>>(radiologsHistoryMap);
		else 
			return new HashMap<String, List<HistoricoRadiologsItem>>();
	}
	
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public ArrayList<HistoricoRadiologsItem> getFilteredList(ArrayList<HistoricoRadiologsItem> list, String filter) {
//		ArrayList filteredList = new ArrayList();
//		
//		for(History item : list) {
//			if(item.getType().equals(filter))
//				filteredList.add(item);
//		}
//		
//		return filteredList;		
//	}
	
	public int getRadiologIconResource(String radiologType, boolean isMarkerStateOn) {
		int resource = -1;
		
		if(radioTypes.contains(radiologType)) {
			int anomIndex = radioTypes.indexOf(radiologType);
			
			switch(anomIndex) {
			case 1: resource = (isMarkerStateOn ? R.drawable.pin_mapa_voz : R.drawable.pin_mapa_voz_off);
				break;
			case 2: resource = (isMarkerStateOn ? resource = R.drawable.pin_mapa_internet : R.drawable.pin_mapa_internet_off);
				break;
			case 3: resource = (isMarkerStateOn ? resource = R.drawable.pin_mapa_messaging : R.drawable.pin_mapa_messaging_off);
				break;
			}
		}
		
		return resource;
	}
	
	public int getRadiologBigIconResource(String radiologType) {
		int resource = -1;
		
		if(radioTypes.contains(radiologType)) {
			int radiologIndex = radioTypes.indexOf(radiologType);

			switch(radiologIndex) {
			case 1: resource = R.drawable.pin_big_mapa_voz;
				break;
			case 2: resource = R.drawable.pin_big_mapa_internet;
				break;
			case 3: resource = R.drawable.pin_big_mapa_messaging;
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
				if(HomeP==MenuOption.HistoricoRadiologs){
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

package pt.ptinovacao.arqospocket.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

import pt.ptinovacao.arqospocket.service.enums.EConnectionTechnology;
import pt.ptinovacao.arqospocket.service.enums.ETestTaskState;
import pt.ptinovacao.arqospocket.service.interfaces.IService;
import pt.ptinovacao.arqospocket.service.interfaces.ITestResult;
import pt.ptinovacao.arqospocket.ArqosActivity;
import pt.ptinovacao.arqospocket.HistoricoTestesItem;
import pt.ptinovacao.arqospocket.MyApplication;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.fragments.FragmentHistoricoTestes_Pager;
import pt.ptinovacao.arqospocket.fragments.FragmentHistorico_EntryPager;
import pt.ptinovacao.arqospocket.interfaces.IFragmentChange;
import pt.ptinovacao.arqospocket.interfaces.IFragmentPager;
import pt.ptinovacao.arqospocket.interfaces.IHistoryProvider;
import pt.ptinovacao.arqospocket.util.History;
import pt.ptinovacao.arqospocket.util.Homepage;
import pt.ptinovacao.arqospocket.util.MenuOption;
import pt.ptinovacao.arqospocket.util.Utils;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

public class ActivityTestesHistorico extends ArqosActivity implements
		IHistoryProvider, IFragmentChange, IFragmentPager {

	private static final String HISTORY_ITEM_POSITION = "HistoryItemPosition";
	private ArrayList<HistoricoTestesItem> historicoTestes;
	private FragmentManager fragMgr;
	private FragmentHistorico_EntryPager pager;
	private List<ITestResult> testsHistory;
	private Map<String, List<HistoricoTestesItem>> testsHistoryMap;
	public List<String> testTypes;
	private IService iService;
	MenuOption HomeP;
	Homepage home;
	
	private MapView mapView;
	private GoogleMap map;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.onActionBarSetTitle(getResources().getString(
				R.string.test_log));
		super.setMenuOption(MenuOption.HistoricoTestes);
		setContentView(R.layout.activity_historico);

		home = new Homepage(this);

		fragMgr = getSupportFragmentManager();

		fragMgr.beginTransaction()
				.add(R.id.framelayout_historico,
						new FragmentHistoricoTestes_Pager()).commit();

		testTypes = Arrays 
				.asList(getResources().getStringArray(R.array.testes));

		// regista para receber o histórico de anomalias
		MyApplication MyApplicationRef = (MyApplication) getApplicationContext();
		iService = MyApplicationRef.getEngineServiceRef();

		testsHistory = iService.get_tests_history();
		
		historicoTestes = acquireHistoricoTestes();

		updateTestsHistoryMap();
	
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
	
	private boolean updateTestsHistoryMap() {
		if (historicoTestes != null && historicoTestes.size() > 0) {
			testsHistoryMap = new HashMap<String, List<HistoricoTestesItem>>();

			for (HistoricoTestesItem test : historicoTestes) {
				String type = test.getType();
				if (testTypes.contains(type)) {
					if (testsHistoryMap.containsKey(type))
						testsHistoryMap.get(type).add(test);
					else {
						List<HistoricoTestesItem> list = new ArrayList<HistoricoTestesItem>();
						list.add(test);
						testsHistoryMap.put(type, list);
					}
				}
			}
			return true;
		}

		return false;
	}

	public boolean hasTestsHistory() {
		return testsHistory != null && testsHistory.size() > 0;
	}

	public boolean updateTestsHistory() {
		if (iService != null) {
			this.testsHistory = iService.get_tests_history();
			acquireHistoricoTestes();

			/* Update tests history map. */
			updateTestsHistoryMap();

			return true;
		}
		return false;
	}

	public ArrayList<HistoricoTestesItem> acquireHistoricoTestes() {
		ArrayList<HistoricoTestesItem> testList = null;
		if (hasTestsHistory()) {
			testList = new ArrayList<HistoricoTestesItem>();

			for (ITestResult testResult : testsHistory) {
				HistoricoTestesItem anomalyItem = new HistoricoTestesItem(this,
						testResult.get_test_id(), testResult.get_test_name(),
						testResult.get_test_type(),
						testResult.get_test_state(),
						testResult.get_run_test_state(),
						testResult.get_task_list(),
						testResult.test_already_sent(),
						testResult.get_number_of_tests_done(),
						testResult.get_test_execution_location(),
						testResult.get_date_end_execution());
				testList.add(anomalyItem);
			}
		}

	

		return testList;
	}

	public void changeFragment(int position) {
		Bundle bundle = new Bundle();
		bundle.putInt(HISTORY_ITEM_POSITION, position);

		pager = new FragmentHistorico_EntryPager();
		pager.setArguments(bundle);

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.framelayout_historico, pager).addToBackStack(null)
				.commit();
		// mViewPager.setVisibility(View.GONE);
	}

	public IService getServiceInterface() {
		return iService;
	}

	public ArrayList<HistoricoTestesItem> getHistory() {
		if (historicoTestes != null)
			/* Returns a copy of the anomalies history list */
			return new ArrayList<HistoricoTestesItem>(historicoTestes);
		else
			return new ArrayList<HistoricoTestesItem>();
	}

	public Map<String, List<HistoricoTestesItem>> getHistoryMap() {
		if (testsHistoryMap != null)
			/* Returns a copy of the anomalies history map */
			return new HashMap<String, List<HistoricoTestesItem>>(
					testsHistoryMap);
		else
			return new HashMap<String, List<HistoricoTestesItem>>();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<HistoricoTestesItem> getFilteredList(
			ArrayList<HistoricoTestesItem> list, String filter) {
		ArrayList filteredList = new ArrayList();

		for (History item : list) {
			if (item.getType().equals(filter))
				filteredList.add(item);
		}

		return filteredList;
	}

	public int getTestPinResource(String testType, boolean isMarkerStateOn,
			boolean isOK) {
		int resource = -1;

		if (testTypes.contains(testType)) {
			int anomIndex = testTypes.indexOf(testType);

			switch (anomIndex) {
			case 1:
				resource = (isMarkerStateOn ? (isOK ? R.drawable.pin_mapa_rede_movel_sucesso
						: R.drawable.pin_mapa_rede_movel_erro)
						: R.drawable.pin_mapa_rede_movel_off);
				break;
			case 2:
				resource = (isMarkerStateOn ? (isOK ? R.drawable.pin_mapa_wifi_sucesso
						: R.drawable.pin_mapa_wifi_erro)
						: R.drawable.pin_mapa_wi_fi_off);
				break;
			case 3:
				resource = (isMarkerStateOn ? (isOK ? R.drawable.pin_mapa_misto_sucesso
						: R.drawable.pin_mapa_misto_erro)
						: R.drawable.pin_mapa_misto_off);
				break;
			}
		}

		return resource;
	}

	public int getTestPinResource(EConnectionTechnology testTechnology,
			boolean isMarkerStateOn, boolean isOK) {
		int resource = -1;

		if (testTypes.size() > testTechnology.ordinal()) {
			int anomIndex = testTechnology.ordinal();

			switch (anomIndex) {
			case 0:
				resource = (isMarkerStateOn ? (isOK ? R.drawable.pin_mapa_wifi_sucesso
						: R.drawable.pin_mapa_wifi_erro)
						: R.drawable.pin_mapa_wi_fi_off);
				break;
			case 1:
				resource = (isMarkerStateOn ? (isOK ? R.drawable.pin_mapa_rede_movel_sucesso
						: R.drawable.pin_mapa_rede_movel_erro)
						: R.drawable.pin_mapa_rede_movel_off);
				break;
			case 2:
				resource = (isMarkerStateOn ? (isOK ? R.drawable.pin_mapa_misto_sucesso
						: R.drawable.pin_mapa_misto_erro)
						: R.drawable.pin_mapa_misto_off);
				break;
			}
		}

		return resource;
	}

	public int getTestPinResource(String testType, boolean isMarkerStateOn,
			ETestTaskState state) {
		try {
			boolean isOK = (state != null ? state.equals(ETestTaskState.OK)
					: false);

			return getTestPinResource(testType, isMarkerStateOn, isOK);
		} catch (Exception e) {
			e.printStackTrace();
			return Utils.INVALID_RESOURCE;
		}
	}

	public int getTestPinResource(EConnectionTechnology testTechnology,
			boolean isMarkerStateOn, ETestTaskState state) {
		try {
			boolean isOK = (state != null ? state.equals(ETestTaskState.OK)
					: false);

			return getTestPinResource(testTechnology, isMarkerStateOn, isOK);
		} catch (Exception e) {
			e.printStackTrace();
			return Utils.INVALID_RESOURCE;
		}
	}

	public int getTestBigPinResource(String testType, boolean ok) {
		int resource = -1;

		if (testTypes.contains(testType)) {
			int anomIndex = testTypes.indexOf(testType);

			switch (anomIndex) {
			case 0:
				resource = (ok ? R.drawable.pin_big_mapa_wifi_sucesso
						: R.drawable.pin_big_mapa_wifi_erro);
				break;
			case 1:
				resource = (ok ? R.drawable.pin_big_mapa_rede_movel_sucesso
						: R.drawable.pin_big_mapa_rede_movel_erro);
				break;
			case 2:
				resource = (ok ? R.drawable.pin_big_mapa_misto_sucesso
						: R.drawable.pin_big_mapa_misto_erro);
				break;
			case 3:
				resource = (ok ? R.drawable.pin_big_mapa_misto_sucesso
						: R.drawable.pin_big_mapa_misto_erro);
				break;
			case 4:
				resource = R.drawable.pin_big_mapa_misto_erro;
				break;
//			case 1:
//				resource = (ok ? R.drawable.pin_big_mapa_rede_movel_sucesso
//						: R.drawable.pin_big_mapa_rede_movel_erro);
//				break;
//			case 2:
//				resource = (ok ? R.drawable.pin_big_mapa_wifi_sucesso
//						: R.drawable.pin_big_mapa_wifi_erro);
//				break;
//			case 3:
//				resource = (ok ? R.drawable.pin_big_mapa_misto_sucesso
//						: R.drawable.pin_big_mapa_misto_erro);
//				break;
			}
		}

		return resource;
	}

	public int getTestBigPinResource(EConnectionTechnology testTechnology,
			boolean ok) {
		int resource = -1;

		if (testTypes.size() >= testTechnology.ordinal()) {
			int anomIndex = testTechnology.ordinal();

			switch (anomIndex) {
			case 0:
				resource = (ok ? R.drawable.pin_big_mapa_wifi_sucesso
						: R.drawable.pin_big_mapa_wifi_erro);
				break;
			case 1:
				resource = (ok ? R.drawable.pin_big_mapa_rede_movel_sucesso
						: R.drawable.pin_big_mapa_rede_movel_erro);
				break;
			case 2:
				resource = (ok ? R.drawable.pin_big_mapa_misto_sucesso
						: R.drawable.pin_big_mapa_misto_erro);
				break;
			case 3:
				resource = (ok ? R.drawable.pin_big_mapa_misto_sucesso
						: R.drawable.pin_big_mapa_misto_erro);
				break;
			case 4:
				resource = R.drawable.pin_big_mapa_misto_erro;
				break;
			}
		}

		return resource;
	}

	public int getTestBigPinResource(String testType, ETestTaskState state) {
		try {
			boolean isOK = (state != null ? state.equals(ETestTaskState.OK)
					: false);

			return getTestBigPinResource(testType, isOK);
		} catch (Exception e) {
			e.printStackTrace();
			return Utils.INVALID_RESOURCE;
		}
	}

	public int getTestBigPinResource(EConnectionTechnology testType,
			ETestTaskState state) {
		try {
			boolean isOK = (state != null ? state.equals(ETestTaskState.OK)
					: false);

			return getTestBigPinResource(testType, isOK);
		} catch (Exception e) {
			e.printStackTrace();
			return Utils.INVALID_RESOURCE;
		}
	}

	public int getTestBigRowIconResource(String testType, boolean ok) {
		int resource = -1;

		if (testTypes.contains(testType)) {
			int anomIndex = testTypes.indexOf(testType);

			switch (anomIndex) {
			case 0:
				resource = (ok ? R.drawable.icon_wi_fi_sucesso_big
						: R.drawable.icon_wi_fi_erro_big);
				break;
			case 1:
				resource = (ok ? R.drawable.icon_rede_movel_sucesso_big
						: R.drawable.icon_rede_movel_erro_big);
				break;
			case 2:
				resource = (ok ? R.drawable.icon_misto_sucesso_big
						: R.drawable.icon_misto_erro_big);
				break;
			case 3:
				resource = (ok ? R.drawable.icon_misto_sucesso_big
						: R.drawable.icon_misto_erro_big);
				break;
			case 4:
				resource = R.drawable.icon_misto_erro_big;
				break;
			}
		}

		return resource;
	}

	public int getTestBigRowIconResource(EConnectionTechnology testTechnology,
			boolean ok) {
		int resource = -1;

		if (testTypes.size() >= testTechnology.ordinal()) {
			int anomIndex = testTechnology.ordinal();

			switch (anomIndex) {
			case 0:
				resource = (ok ? R.drawable.icon_wi_fi_sucesso_big
						: R.drawable.icon_wi_fi_erro_big);
				break;
			case 1:
				resource = (ok ? R.drawable.icon_rede_movel_sucesso_big
						: R.drawable.icon_rede_movel_erro_big);
				break;
			case 2:
				resource = (ok ? R.drawable.icon_misto_sucesso_big
						: R.drawable.icon_misto_erro_big);
				break;
			case 3:
				resource = (ok ? R.drawable.icon_misto_sucesso_big
						: R.drawable.icon_misto_erro_big);
				break;
			case 4:
				resource = R.drawable.icon_misto_erro_big;
				break;
			}
		}

		return resource;
	}

	public int getTestBigRowIconResource(String testType, ETestTaskState state) {
		boolean isOK = (state != null ? state.equals(ETestTaskState.OK) : false);

		return getTestBigRowIconResource(testType, isOK);
	}

	public int getTestBigRowIconResource(EConnectionTechnology testType,
			ETestTaskState state) {
		boolean isOK = (state != null ? state.equals(ETestTaskState.OK) : false);

		return getTestBigRowIconResource(testType, isOK);
	}

	/*
	 * Percentage resources
	 */
	public int getTestPercentageIconResource(int percentage, boolean ok) {
		int resource = -1;

		switch (percentage) {
		case 0:
			resource = R.drawable.falhado_default;
			break;
		// case 50: resource = (ok ? R.drawable.concluido_50 :
		// R.drawable.falhado_50);
		// break;
		case 100:
			resource = (ok ? R.drawable.concluido_100 : R.drawable.falhado_100);
			break;
		default:
			resource = R.drawable.falhado_default;
			break;
		}

		return resource;
	}

	public int getTestPercentageIconResource(int percentage,
			ETestTaskState state) {
		boolean isOK = (state != null ? state.equals(ETestTaskState.OK) : false);

		return getTestPercentageIconResource(percentage, isOK);
	}

	/*
	 * Percentage resources
	 */

	public void onBackPressed() {
		if (slidingMenuIsOpen()) {
			onSlidingMenuToggle();
		} else {
			if (fragMgr == null)
				fragMgr = getSupportFragmentManager();

			if (fragMgr.getBackStackEntryCount() > 0) {
				fragMgr.popBackStack();
				overridePendingTransition(0, 0);
			} else {
				HomeP = home.ReadHome();
				if (HomeP == MenuOption.HistoricoTestes) {
					finish();
				} else
					home.TesteTipe(HomeP);
				finish();

				super.onBackPressed();
			}
			hideMap();
		}
	}

	@Override
	public void previousPage() {
		if (pager != null) {
			pager.previousPage();
		}
	}

	@Override
	public void nextPage() {
		if (pager != null) {
			pager.nextPage();
		}
	}
}

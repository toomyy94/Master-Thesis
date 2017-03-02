package pt.ptinovacao.arqospocket.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pt.ptinovacao.arqospocket.adapters.Adapter_List_Historico_Testes;
import pt.ptinovacao.arqospocket.adapters.Adapter_spinner;
import pt.ptinovacao.arqospocket.service.enums.EMobileNetworkMode;
import pt.ptinovacao.arqospocket.service.enums.EMobileState;
import pt.ptinovacao.arqospocket.service.enums.ENetworkAction;
import pt.ptinovacao.arqospocket.service.interfaces.IService;
import pt.ptinovacao.arqospocket.service.interfaces.IUI;
import pt.ptinovacao.arqospocket.DialogRowItem;
import pt.ptinovacao.arqospocket.HistoricoTestesItem;
import pt.ptinovacao.arqospocket.MyApplication;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.activities.ActivityTestesHistorico;
import pt.ptinovacao.arqospocket.interfaces.IFragmentChange;
import pt.ptinovacao.arqospocket.swipablelistview.BaseSwipeListViewListener;
import pt.ptinovacao.arqospocket.swipablelistview.SwipeListView;
import pt.ptinovacao.arqospocket.util.Utils;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class FragmentTestesHistorico extends Fragment implements IUI {

	private ActivityTestesHistorico activity;
	private SwipeListView list;
	private Adapter_List_Historico_Testes adapter;
	private ArrayList<HistoricoTestesItem> historicoTodosTestes, historicoTestes;
	private Map<String, List<HistoricoTestesItem>> mapaHistoricoTestes;
	private Spinner spinnerFilter;
	private IService iService;
	
	ArrayList<DialogRowItem> dialogItems = new ArrayList<DialogRowItem>();
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		this.activity = (ActivityTestesHistorico) activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.historicoTodosTestes = this.activity.getHistory();
		this.mapaHistoricoTestes = this.activity.getHistoryMap();
		
		if(dialogItems != null && dialogItems.size() == 0) {
			for(int i = 0; i < activity.testTypes.size(); i++) {
				dialogItems.add(new DialogRowItem(activity.testTypes.get(i), Utils.Tests_icons[i]));
			}
		}
		
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_testes_historico,
				container, false);

		list = (SwipeListView) v.findViewById(R.id.list);
		Adapter_spinner spinnerAdapter = new Adapter_spinner(activity, dialogItems);
		spinnerAdapter.setDropDownViewResource(R.layout.rowdrop);
		
		Spinner spinner = (Spinner) v.findViewById(R.id.spinner_filter);
		//Adicionado
		spinner.setAdapter(spinnerAdapter);
		/* add dummy entries */
//		ArrayList<HistoricoTestesItem> testList = new ArrayList<HistoricoTestesItem>();
//		HistoricoTestesItem d1 = new HistoricoTestesItem("Test1",
//				"Teste integer tincidun", ETestType.SCHEDULED,
//				ETestTaskState.NOK, ERunTestTaskState.DONE, null, false, 10, new Location(""));
//		HistoricoTestesItem d2 = new HistoricoTestesItem("Test2",
//				"Teste integer tincidun", ETestType.SCHEDULED,
//				ETestTaskState.OK, ERunTestTaskState.DONE, null, false, 10, new Location(""));
//		HistoricoTestesItem d3 = new HistoricoTestesItem("Test3",
//				"Teste integer tincidun", ETestType.SCHEDULED,
//				ETestTaskState.NOK, ERunTestTaskState.DONE, null, false, 10, new Location(""));
//		testList.add(d1);
//		testList.add(d2);
//		testList.add(d3);		
		/* add dummy entries */

		LinearLayout sendPendingTests = (LinearLayout) v.findViewById(R.id.bt_sendPendingTests);
		sendPendingTests.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				/* Send pending tests. */
				activity.getServiceInterface().send_pending_tests(FragmentTestesHistorico.this);
			}
		});
		
		spinnerFilter = (Spinner) v.findViewById(R.id.spinner_filter);

		if(historicoTodosTestes != null) {
			spinnerFilter.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					filterTestes(arg2);
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {				
				}
			});
			
			historicoTestes = new ArrayList<HistoricoTestesItem>(historicoTodosTestes);
			adapter = new Adapter_List_Historico_Testes(activity, historicoTestes);
			list.setAdapter(adapter);
//			list.setOnItemClickListener(onClickListener);
			list.setSwipeListViewListener(new BaseSwipeListViewListener((IFragmentChange) activity, activity, activity.getSupportFragmentManager()));
		}
		
		// regista para receber os eventos do wifi e da rede movel
		MyApplication MyApplicationRef = (MyApplication) getActivity()
				.getApplicationContext();
		iService = MyApplicationRef.getEngineServiceRef();

		if (iService != null) {
			iService.registry_update_test_info(this);
		}
		
		return v;
	}
	
	@Override
	public void onPause() {
		super.onPause();

		if (iService == null) {
			MyApplication MyApplicationRef = (MyApplication) getActivity()
					.getApplicationContext();
			iService = MyApplicationRef.getEngineServiceRef();
		}

		iService.remove_registry_update_test_info(this);
	}
	
	private void filterTestes(int testTypeToFilter) {
//		<string-array name="testes">
//        <item>@string/testes_todos</item>
//        <item>@string/rede</item>
//        <item>@string/wifi</item>
//        <item>@string/misto</item>
//    	</string-array>
    
		if(historicoTestes == null)
			historicoTestes = new ArrayList<HistoricoTestesItem>();
		
		try {
			/* Clear current entries */
			historicoTestes.clear();
			
			/*All anomalies */
			if(testTypeToFilter == 0)			
				historicoTestes.addAll(historicoTodosTestes);
			else {
				List<HistoricoTestesItem> list;
				if((list = mapaHistoricoTestes.get(activity.testTypes.get(testTypeToFilter))) != null)
					historicoTestes.addAll(list);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		adapter.notifyDataSetChanged();
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
		// TODO Implement this one!

	}

	@Override
	public void send_report_ack(ENetworkAction action_state) {		
	}

	@Override
	public void update_test_info() {
		/* Update tests history */
		this.activity.updateTestsHistory();
		
		/* Get updated tests history */
		this.historicoTodosTestes = this.activity.getHistory();
		this.mapaHistoricoTestes = this.activity.getHistoryMap();
		
		/* Filter tests according to the filter
		 * (also refreshes the adapter) */
		filterTestes(spinnerFilter.getSelectedItemPosition());
	}

	@Override
	public void update_test_task(String test_id) {

	}

}

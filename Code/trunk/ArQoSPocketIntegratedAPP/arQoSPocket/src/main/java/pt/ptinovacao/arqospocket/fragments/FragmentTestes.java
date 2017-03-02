package pt.ptinovacao.arqospocket.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import pt.ptinovacao.arqospocket.adapters.AdapterListViewTestes;
import pt.ptinovacao.arqospocket.service.enums.EMobileNetworkMode;
import pt.ptinovacao.arqospocket.service.enums.EMobileState;
import pt.ptinovacao.arqospocket.service.enums.ENetworkAction;
import pt.ptinovacao.arqospocket.service.interfaces.IService;
import pt.ptinovacao.arqospocket.service.interfaces.IUI;
import pt.ptinovacao.arqospocket.MyApplication;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.activities.ActivityTestes;
import pt.ptinovacao.arqospocket.core.RowItemTestes;
import pt.ptinovacao.arqospocket.swipablelistview.BaseSwipeListViewListener;
import pt.ptinovacao.arqospocket.swipablelistview.SwipeListView;
import pt.ptinovacao.arqospocket.util.ColorPercent;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentTestes extends Fragment implements IUI, OnClickListener {
	int numero_linhas = 4;

	private SwipeListView listView;
	AdapterListViewTestes adapter;
	List<RowItemTestes> rowItemstestes;
	RowItemTestes item;
	int todostestes, color, percent, filtro = 0;
	TextView txttodos, txtinactivo, txtemcurso;
	Fragment fragment;
	ColorPercent colorPercent = new ColorPercent();
	private ActivityTestes activity;
	private ArrayList<RowItemTestes> Testes;
	private ArrayList<RowItemTestes> TestesExecution;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		this.activity = (ActivityTestes) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_testes, container, false);

		listView = (SwipeListView) v.findViewById(R.id.listTestes);
		listView.setSwipeListViewListener(new BaseSwipeListViewListener(
				activity, activity, activity.getSupportFragmentManager()));

		rowItemstestes = new ArrayList<RowItemTestes>();
		final LinearLayout Linetodos = (LinearLayout) v
				.findViewById(R.id.todostestes);
		final LinearLayout Lineemcurso = (LinearLayout) v
				.findViewById(R.id.testesemcurso);
		final LinearLayout Lineinactivos = (LinearLayout) v
				.findViewById(R.id.testesinactivos);

		txttodos = (TextView) v.findViewById(R.id.text_bola_todas);
		txtinactivo = (TextView) v.findViewById(R.id.text_bola_inac);
		txtemcurso = (TextView) v.findViewById(R.id.text_bola_emcurso);

		color = colorPercent.getimage();

		this.Testes = this.activity.getTestes();
		this.TestesExecution = this.activity.getTestesExecution();
		TestesExecution.addAll(Testes);

		adapter = new AdapterListViewTestes(getActivity(), color,
				TestesExecution);
		listView.setAdapter(adapter);
		activity.setFiltro(filtro);

		// apresentar o nº de testes
		txtinactivo.setText(String.valueOf(activity.getInactivo()));
		txtemcurso.setText(String.valueOf(activity.getEmcurso()));
		todostestes = activity.getInactivo() + activity.getEmcurso();
		txttodos.setText(String.valueOf(todostestes));

		Linetodos.setSelected(true);

		MyApplication MyApplicationRef = (MyApplication) activity
				.getApplicationContext();
		IService iService = MyApplicationRef.getEngineServiceRef();

		if (iService != null) {
			iService.registry_update_test_info(this);

			// filtros de testes
			Linetodos.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Lineemcurso.setSelected(false);
					Linetodos.setSelected(true);
					Lineinactivos.setSelected(false);

					filtro = 0;
					activity.setFiltro(filtro);
					activity.getInfo();
					// actualizar dados
					Testes = activity.getTestes();
					TestesExecution.clear();
					TestesExecution.addAll(activity.getTestesExecution());
					TestesExecution.addAll(Testes);
					adapter.notifyDataSetChanged();
				}

			});
			Lineemcurso.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Lineemcurso.setSelected(true);
					Linetodos.setSelected(false);
					Lineinactivos.setSelected(false);
					filtro = 1;
					activity.setFiltro(filtro);
					activity.getInfo();
					// actualizar dados
					// Testes = activity.getTestes();
					TestesExecution.clear();
					TestesExecution.addAll(activity.getTestesExecution());
					// TestesExecution.addAll(Testes);
					adapter.notifyDataSetChanged();
				}

			});
			Lineinactivos.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Lineemcurso.setSelected(false);
					Linetodos.setSelected(false);
					Lineinactivos.setSelected(true);
					filtro = 2;
					activity.setFiltro(filtro);
					activity.getInfo();
					// actualizar dados
					Testes = activity.getTestes();
					TestesExecution.clear();
					// TestesExecution.addAll(activity.getTestesExecution());
					TestesExecution.addAll(Testes);
					adapter.notifyDataSetChanged();
				}

			});
		}

		return v;

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
	public void update_test_info() {

		activity.getInfo();
		// actualiza legenda
		txtinactivo.setText(String.valueOf(activity.getInactivo()));
		txtemcurso.setText(String.valueOf(activity.getEmcurso()));
		todostestes = activity.getInactivo() + activity.getEmcurso();
		txttodos.setText(String.valueOf(todostestes));
		// actualizar dados
		if (filtro == 0) {
			this.Testes = this.activity.getTestes();
			this.TestesExecution.clear();
			this.TestesExecution.addAll(this.activity.getTestesExecution());
			TestesExecution.addAll(Testes);
			adapter.notifyDataSetChanged();
		}
		if (filtro == 1) {
			this.Testes = this.activity.getTestes();
			this.TestesExecution.clear();
			this.TestesExecution.addAll(this.activity.getTestesExecution());
			// TestesExecution.addAll(Testes);
			adapter.notifyDataSetChanged();
		}
		if (filtro == 2) {
			this.Testes = this.activity.getTestes();
			this.TestesExecution.clear();
			// this.TestesExecution.addAll(this.activity.getTestesExecution());
			TestesExecution.addAll(Testes);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void update_test_task(String test_id) {
		activity.getInfo();
		// actualizar legenda

		txtinactivo.setText(String.valueOf(activity.getInactivo()));
		txtemcurso.setText(String.valueOf(activity.getEmcurso()));
		todostestes = activity.getInactivo() + activity.getEmcurso();
		txttodos.setText(String.valueOf(todostestes));

		// actualizar dados
		if (filtro == 0) {
			this.Testes = this.activity.getTestes();
			this.TestesExecution.clear();
			this.TestesExecution.addAll(this.activity.getTestesExecution());
			TestesExecution.addAll(Testes);
			adapter.notifyDataSetChanged();
		}
		if (filtro == 1) {
			this.Testes = this.activity.getTestes();
			this.TestesExecution.clear();
			this.TestesExecution.addAll(this.activity.getTestesExecution());
			// TestesExecution.addAll(Testes);
			adapter.notifyDataSetChanged();
		}
		if (filtro == 2) {
			this.Testes = this.activity.getTestes();
			this.TestesExecution.clear();
			// this.TestesExecution.addAll(this.activity.getTestesExecution());
			TestesExecution.addAll(Testes);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void send_pending_tests_ack(ENetworkAction action_state) {

	}

	@Override
	public void send_report_ack(ENetworkAction action_state) {

	}

	@Override
	public void onClick(View v) {

	}

}

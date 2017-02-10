package pt.ptinovacao.arqospocket.fragments;

import java.util.ArrayList;
import java.util.List;

import java.util.TreeMap;



import pt.ptinovacao.arqospocket.adapters.AdapterListViewTipfalha;

import pt.ptinovacao.arqospocket.service.interfaces.IService;
import pt.ptinovacao.arqospocket.MyApplication;
import pt.ptinovacao.arqospocket.R;

import pt.ptinovacao.arqospocket.activities.ActivityAnomalias;

import pt.ptinovacao.arqospocket.core.RowItemtip;
import pt.ptinovacao.arqospocket.util.I_OnDataPass;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import static pt.ptinovacao.arqospocket.service.jsonparser.AnomaliesTopics.ANOMALY_CATEGORY_COVERAGE;
import static pt.ptinovacao.arqospocket.service.jsonparser.AnomaliesTopics.ANOMALY_CATEGORY_INTERNET;
import static pt.ptinovacao.arqospocket.service.jsonparser.AnomaliesTopics.ANOMALY_CATEGORY_MESSAGING;
import static pt.ptinovacao.arqospocket.service.jsonparser.AnomaliesTopics.ANOMALY_CATEGORY_OTHERS;
import static pt.ptinovacao.arqospocket.service.jsonparser.AnomaliesTopics.ANOMALY_CATEGORY_VOICE;

@SuppressLint("ResourceAsColor")
public class FragmentTipfalha<Initialization, SetPagerTip> extends Fragment
		implements OnItemClickListener, OnClickListener, OnItemSelectedListener {

	private static final String TAG = "FragmentTipfalha";
	int selectTip = 0;
	int posselected, posselectedTip;
	String cell_string = null;
	ViewPager pager;
	ImageView logo;
	I_OnDataPass dataPass;
	List<RowItemtip> rowItemstip;
	RowItemtip item;
	TextView textView, numView, textlogo;
	AdapterListViewTipfalha adapter;
	ListView listView;
	ActivityAnomalias activityTip;


	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_tipfalha_layout, container,
				false);

		dataPass = (I_OnDataPass) getActivity();
		logo = (ImageView) v.findViewById(R.id.imageTip);
		numView = (TextView) v.findViewById(R.id.numlistViewTip);
		textView = (TextView) v.findViewById(R.id.textlistViewTip);
		listView = (ListView) v.findViewById(R.id.listanomaliastip);
		textlogo = (TextView) v.findViewById(R.id.textTip);
		activityTip = (ActivityAnomalias) getActivity();

		pager = activityTip.getpager();
		rowItemstip = new ArrayList<RowItemtip>();
		
		
		//get_anomalies_details(posselected);
		LinhaTip(posselected);

		adapter = new AdapterListViewTipfalha(getActivity()
				.getApplicationContext(), R.layout.listview_anomalias_tip,
				rowItemstip);

		listView.setClickable(true);
		listView.setOnItemClickListener(this);
		listView.setAdapter(adapter);
		
		cell_string = rowItemstip.get(selectTip).getDesc();
		((I_OnDataPass) activityTip).setTextTdf(cell_string);
		return v;

	}

	public void LinhaTip(int position) {

		MyApplication MyApplicationRef = (MyApplication) getActivity().getApplicationContext();
		IService iService = MyApplicationRef.getEngineServiceRef();		
		
	switch (position) {	
	case 0:
		
			logo.setImageResource(R.drawable.icon_menu_voz);
			textlogo.setText(R.string.anomaly_category_voice);

			TreeMap<String,String> mapVoz = iService.get_anomalies_details(ANOMALY_CATEGORY_VOICE);

			int indexVoz = 0;
			for (TreeMap.Entry<String, String> entry : mapVoz.entrySet()) {
				if (indexVoz == selectTip) {
					item = new RowItemtip(entry.getKey(), entry.getValue(), true);
				} else {
					item = new RowItemtip(entry.getKey(), entry.getValue(), false);
				}
				
				rowItemstip.add(item);
				indexVoz++;
			}
			 
			break;		

		case 1:
			logo.setImageResource(R.drawable.icon_menu_internet);
			textlogo.setText(R.string.anomaly_category_internet);
			TreeMap<String,String> mapInternet = iService.get_anomalies_details(ANOMALY_CATEGORY_INTERNET);

			int indexInternet = 0;
			for (TreeMap.Entry<String, String> entry : mapInternet.entrySet()) {
				if (indexInternet == selectTip) {
					item = new RowItemtip(entry.getKey(), entry.getValue(), true);
				} else {
					item = new RowItemtip(entry.getKey(), entry.getValue(), false);
				}
				
				rowItemstip.add(item);
				indexInternet++;
			

			}
			break;
		case 2:
			logo.setImageResource(R.drawable.icon_menu_messaging);
			textlogo.setText(R.string.anomaly_category_messaging);
			TreeMap<String,String> mapMessaging = iService.get_anomalies_details(ANOMALY_CATEGORY_MESSAGING);

			int indexMessaging = 0;
			for (TreeMap.Entry<String, String> entry : mapMessaging.entrySet()) {
				if (indexMessaging == selectTip) {
					item = new RowItemtip(entry.getKey(), entry.getValue(), true);
				} else {
					item = new RowItemtip(entry.getKey(), entry.getValue(), false);
				}
				
				rowItemstip.add(item);
				indexMessaging++;
			}

			break;
		case 3:
			logo.setImageResource(R.drawable.icon_menu_cobertura);
			textlogo.setText(R.string.anomaly_category_network_coverage);
			TreeMap<String,String> mapCobertura = iService.get_anomalies_details(ANOMALY_CATEGORY_COVERAGE);

			int indexCobertura = 0;
			for (TreeMap.Entry<String, String> entry : mapCobertura.entrySet()) {
				if (indexCobertura == selectTip) {
					item = new RowItemtip(entry.getKey(), entry.getValue(), true);
				} else {
					item = new RowItemtip(entry.getKey(), entry.getValue(), false);
				}
				
				rowItemstip.add(item);
				indexCobertura++;
			}

			break;
		case 4:
			logo.setImageResource(R.drawable.icon_menu_outra);
			textlogo.setText(R.string.anomaly_category_other);
			TreeMap<String,String> mapOutra = iService.get_anomalies_details(ANOMALY_CATEGORY_OTHERS);

			int indexOutra=0;
			for (TreeMap.Entry<String, String> entry : mapOutra.entrySet()) {
				if (indexOutra == selectTip) {
					item = new RowItemtip(entry.getKey(), entry.getValue(), true);
				} else {
					item = new RowItemtip(entry.getKey(), entry.getValue(), false);
				}
				
				rowItemstip.add(item);
				indexOutra++;
			}
			break;
		default:

			break;

		}
	}

	public void getPositionSelected() {

		if (dataPass == null)
			dataPass = (I_OnDataPass) getActivity();
		if (this.posselected != dataPass.getPositionSelected()) {
			this.posselected = dataPass.getPositionSelected();
			Log.i(TAG, "TipFragment" + posselected);
			selectTip = 0;
		}
	}

	public void refresh() {

		adapter.clear();

		getPositionSelected();

		LinhaTip(posselected);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		rowItemstip.get(selectTip).setSelect(false);
		selectTip = arg2;
		cell_string = rowItemstip.get(selectTip).getDesc();
		rowItemstip.get(selectTip).setSelect(true);

		adapter.notifyDataSetChanged();

		Log.i(TAG, "TipFragmentTip" + selectTip);
		((I_OnDataPass) activityTip).setSelectTip(selectTip);
		((I_OnDataPass) activityTip).setTextTdf(cell_string);
		
		pager.setCurrentItem(2);
		// ActivityAnomalias.setPositionSelectTip(selectTip);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

	}

	
}

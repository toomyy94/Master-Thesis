package pt.ptinovacao.arqospocket.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.ptinovacao.arqospocket.activities.ActivityConfiguracoes;
import pt.ptinovacao.arqospocket.adapters.Adapter_List_Historico_Anomalias;
import pt.ptinovacao.arqospocket.adapters.Adapter_spinner;
import pt.ptinovacao.arqospocket.DialogRowItem;
import pt.ptinovacao.arqospocket.HistoricoAnomaliasItem;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.activities.ActivityAnomaliasHistorico;
import pt.ptinovacao.arqospocket.swipablelistview.BaseSwipeListViewListener;
import pt.ptinovacao.arqospocket.swipablelistview.SwipeListView;
import pt.ptinovacao.arqospocket.util.LocaleHelper;
import pt.ptinovacao.arqospocket.util.Utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.Spinner;


public class FragmentAnomaliasHistorico extends Fragment {

	private ActivityAnomaliasHistorico activity;
	private SwipeListView list;
	private Adapter_List_Historico_Anomalias adapter;
	private ArrayList<HistoricoAnomaliasItem> historicoTodasAnomalias, historicoAnomalias;
	private Map<String, List<HistoricoAnomaliasItem>> mapaHistoricoAnomalias;
	private ImageView lista_image;
	private String language_code;

	ArrayList<DialogRowItem> dialogItems = new ArrayList<DialogRowItem>();
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		this.activity = (ActivityAnomaliasHistorico) activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.historicoTodasAnomalias = this.activity.getHistory();
		this.mapaHistoricoAnomalias = this.activity.getHistoryMap();
		
		if(dialogItems != null && dialogItems.size() == 0) {
			for(int i = 0; i < activity.anomTypes.size(); i++) {
				dialogItems.add(new DialogRowItem(activity.anomTypes.get(i), Utils.Anomalies_icons[i]));
			}
		}
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_anomalias_historico,
				container, false);
		
		list = (SwipeListView) v.findViewById(R.id.list);

		//Added to internatianalization
		language_code = LocaleHelper.getLanguage(getActivity().getApplicationContext());

		lista_image = (ImageView) v.findViewById(R.id.bt_swipe_list);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			if (language_code.equals("pt"))
				lista_image.setBackground(getResources().getDrawable(R.drawable.tit_lista));
			else if (language_code.equals("en"))
				lista_image.setBackground(getResources().getDrawable(R.drawable.tit_lista_en));
			else if (language_code.equals("fr"))
				lista_image.setBackground(getResources().getDrawable(R.drawable.tit_lista_fr));
			else lista_image.setBackground(getResources().getDrawable(R.drawable.tit_lista_en));
		}
		else{
			if (language_code.equals("pt"))
				lista_image.setBackgroundDrawable(getResources().getDrawable(R.drawable.tit_lista));
			else if (language_code.equals("en"))
				lista_image.setBackgroundDrawable(getResources().getDrawable(R.drawable.tit_lista_en));
			else if (language_code.equals("fr"))
				lista_image.setBackgroundDrawable(getResources().getDrawable(R.drawable.tit_lista_fr));
			else lista_image.setBackgroundDrawable(getResources().getDrawable(R.drawable.tit_lista_en));
		}



		Adapter_spinner spinnerAdapter = new Adapter_spinner(activity, dialogItems);
		spinnerAdapter.setDropDownViewResource(R.layout.rowdrop);
		
		Spinner spinner = (Spinner) v.findViewById(R.id.spinner_filter);
		//Adicionado
		spinner.setAdapter(spinnerAdapter);
						
		if(historicoTodasAnomalias != null) {
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					filterAnomalies(arg2);				
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {				
				}
			});
			
			historicoAnomalias = new ArrayList<HistoricoAnomaliasItem>(historicoTodasAnomalias);
			adapter = new Adapter_List_Historico_Anomalias(getActivity(), historicoAnomalias);	
			list.setAdapter(adapter);
			list.setSwipeListViewListener(new BaseSwipeListViewListener(activity, activity, activity.getSupportFragmentManager()));
		}	

		return v;

	}

	
	private void filterAnomalies(int anomalyTypeToFilter) {

		/* Set 'anomalyTypeToFilter' in 'activity' */
		activity.anomalyTypeToFilter = anomalyTypeToFilter;
    
		if(historicoAnomalias == null)
			historicoAnomalias = new ArrayList<HistoricoAnomaliasItem>();
		
		try {
			/* Clear current entries */
			historicoAnomalias.clear();
			
			/*All anomalies */
			if(anomalyTypeToFilter == 0)			
				historicoAnomalias.addAll(historicoTodasAnomalias);
			else {
				List<HistoricoAnomaliasItem> list;
				if((list = mapaHistoricoAnomalias.get(activity.anomTypes.get(anomalyTypeToFilter))) != null)
					historicoAnomalias.addAll(list);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		adapter.notifyDataSetChanged();
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
	
//	private OnItemClickListener onClickListener = new OnItemClickListener() {
//		@Override
//		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//				long arg3) {
//			
////			activity.nextPage(0);
////			FragmentAnomaliasHistorico_Entry entryFragment = new FragmentAnomaliasHistorico_Entry();
////			((FragmentContainer) getParentFragment()).changeFrag(entryFragment);
//
//		}
//	};

}

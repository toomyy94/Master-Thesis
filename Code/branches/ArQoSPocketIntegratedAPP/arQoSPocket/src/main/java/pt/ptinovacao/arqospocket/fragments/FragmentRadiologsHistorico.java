package pt.ptinovacao.arqospocket.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.ptinovacao.arqospocket.DialogRowItem;
import pt.ptinovacao.arqospocket.HistoricoAnomaliasItem;
import pt.ptinovacao.arqospocket.HistoricoRadiologsItem;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.activities.ActivityAnomaliasHistorico;
import pt.ptinovacao.arqospocket.activities.ActivityRadiologs;
import pt.ptinovacao.arqospocket.activities.ActivityRadiologsHistorico;
import pt.ptinovacao.arqospocket.adapters.Adapter_List_Historico_Anomalias;
import pt.ptinovacao.arqospocket.adapters.Adapter_List_Historico_Radiologs;
import pt.ptinovacao.arqospocket.adapters.Adapter_spinner;
import pt.ptinovacao.arqospocket.swipablelistview.BaseSwipeListViewListener;
import pt.ptinovacao.arqospocket.swipablelistview.SwipeListView;
import pt.ptinovacao.arqospocket.util.LocaleHelper;
import pt.ptinovacao.arqospocket.util.Utils;


public class FragmentRadiologsHistorico extends Fragment {

	private ActivityRadiologsHistorico activity;
	private SwipeListView list;
	private Adapter_List_Historico_Radiologs adapter;
	private ArrayList<HistoricoRadiologsItem> historicoTodasRadiologs, historicoRadiologs;
	private Map<String, List<HistoricoRadiologsItem>> mapaHistoricoRadiologs;
	private ImageView lista_image;
	private String language_code;

	ArrayList<DialogRowItem> dialogItems = new ArrayList<DialogRowItem>();
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		this.activity = (ActivityRadiologsHistorico) activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.historicoTodasRadiologs = this.activity.getHistory();
		this.mapaHistoricoRadiologs = this.activity.getHistoryMap();
		
		if(dialogItems != null && dialogItems.size() == 0) {
			for(int i = 0; i < activity.radioTypes.size(); i++) {
				dialogItems.add(new DialogRowItem(activity.radioTypes.get(i), Utils.Radiologs_icons[i]));
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
        spinner.setPrompt(getResources().getString(R.string.radiolog_type));
		spinner.setAdapter(spinnerAdapter);
						
		if(historicoTodasRadiologs != null) {
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
			
			historicoRadiologs = new ArrayList<HistoricoRadiologsItem>(historicoTodasRadiologs);
			adapter = new Adapter_List_Historico_Radiologs(getActivity(), historicoRadiologs);
			list.setAdapter(adapter);
			list.setSwipeListViewListener(new BaseSwipeListViewListener(activity, activity, activity.getSupportFragmentManager()));
		}	

		return v;

	}

	
	private void filterAnomalies(int radiologsTypeToFilter) {

		/* Set 'anomalyTypeToFilter' in 'activity' */
		activity.radiologsTypeToFilter = radiologsTypeToFilter;
    
		if(historicoRadiologs == null)
			historicoRadiologs = new ArrayList<HistoricoRadiologsItem>();
		
		try {
			/* Clear current entries */
			historicoRadiologs.clear();
			
			/*All anomalies */
			if(radiologsTypeToFilter == 0)
				historicoRadiologs.addAll(historicoTodasRadiologs);
			else {
				List<HistoricoRadiologsItem> list;
				if((list = mapaHistoricoRadiologs.get(activity.radioTypes.get(radiologsTypeToFilter))) != null)
					historicoRadiologs.addAll(list);
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

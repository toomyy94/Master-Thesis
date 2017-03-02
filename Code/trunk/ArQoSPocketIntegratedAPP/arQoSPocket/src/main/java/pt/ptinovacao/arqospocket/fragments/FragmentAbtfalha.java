package pt.ptinovacao.arqospocket.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import pt.ptinovacao.arqospocket.adapters.AdapterListViewAbtfalha;
import pt.ptinovacao.arqospocket.service.interfaces.IService;
import pt.ptinovacao.arqospocket.MyApplication;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.activities.ActivityAnomalias;
import pt.ptinovacao.arqospocket.core.RowItem;
import pt.ptinovacao.arqospocket.util.I_OnDataPass;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class FragmentAbtfalha<Initialization> extends Fragment implements
		OnClickListener, OnItemClickListener, OnItemSelectedListener {
	int posselected = 0, selectTip = 0;
	
	String cell_string = null;	
	ViewPager pager;
	LinearLayout linha;
	TextView textView;
	ImageView imageView;
	ListView listView;
	AdapterListViewAbtfalha adapter;
	List<RowItem> rowItems;
	ActivityAnomalias activity;
		
	public static final Integer[] imagens = { R.drawable.selector_voz,
			R.drawable.selector_internet, R.drawable.selector_messaging,
			R.drawable.selector_cobertura, R.drawable.selector_outra };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_abtfalha_layout, container,
				false);

		activity = (ActivityAnomalias) getActivity();
		pager = activity.getpager();
		rowItems = new ArrayList<RowItem>();


		MyApplication MyApplicationRef = (MyApplication) getActivity().getApplicationContext();
		IService iService = MyApplicationRef.getEngineServiceRef();

		iService.get_anomalies();
		TreeMap<String,String> map = iService.get_anomalies();


		int index = 0;
		
		for (TreeMap.Entry<String, String> entry : map.entrySet()) {
			RowItem item;
			
			int iconID = getResourceID(entry.getKey());
			
			if (index == posselected) {
				item = new RowItem(iconID, entry.getValue(), true);
				
			} else {
				item = new RowItem(iconID, entry.getValue(), false);
			}
			
			rowItems.add(item);
			index++;
		

		}

		imageView = (ImageView) v.findViewById(R.id.imagelistView);
		textView = (TextView) v.findViewById(R.id.textlistView);
		listView = (ListView) v.findViewById(R.id.listanomalias);

		adapter = new AdapterListViewAbtfalha(getActivity()
				.getApplicationContext(),
				R.layout.listview_anomalias_abt_layout, rowItems);

		listView.setClickable(true);
		listView.setOnItemClickListener(this);
		listView.setAdapter(adapter);
		//posição sem nada selecionado
		cell_string = rowItems.get(posselected).getDesc();
		((I_OnDataPass) activity).setTextAbt(cell_string);
		return v;

	}

	//TODO ver isto
	private int getResourceID(String iconID) {
		if(iconID.equals("anomalias_icon_01")) {
			return imagens[0];
		} else if(iconID.equals("anomalias_icon_02")) {
			return imagens[1];
		}  else if(iconID.equals("anomalias_icon_03")) {
			return imagens[2];
		}  else if(iconID.equals("anomalias_icon_04")) {
			return imagens[3];
		} else {
			return imagens[4];
		}
		
	}

	private ListView findViewById(int listanomalias) {
		return null;
	}

	protected Context getBaseContext() {
		return null;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		rowItems.get(posselected).setSelect(false);
		posselected = arg2;
		
		cell_string = rowItems.get(posselected).getDesc();
		
		rowItems.get(posselected).setSelect(true);
		adapter.notifyDataSetChanged();
		
		((I_OnDataPass) activity).setPositionSelected(posselected);
		((I_OnDataPass) activity).setSelectTip(selectTip);
		((I_OnDataPass) activity).setTextAbt(cell_string);
		pager.setCurrentItem(1);

	}
	
	@Override
	public void onClick(View v) {

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	public int setViewPager() {

		return posselected;
	}
}

package pt.ptinovacao.arqospocket.fragments;

import java.util.ArrayList;

import pt.ptinovacao.arqospocket.adapters.Adapter_Frag_Hist_Entry;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.interfaces.IFragmentPager;
import pt.ptinovacao.arqospocket.interfaces.IHistoryProvider;
import pt.ptinovacao.arqospocket.interfaces.IUpdateMap;
import pt.ptinovacao.arqospocket.util.History;
import pt.ptinovacao.arqospocket.util.ListAwareViewPager;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentHistorico_EntryPager extends Fragment implements IFragmentPager {

	private static final String HISTORY_ITEM_POSITION = "HistoryItemPosition";
	private IHistoryProvider historyProvider;
//	private SwipeListView list;
	private ListAwareViewPager viewPager;
	private ArrayList<History> historico;
	private Adapter_Frag_Hist_Entry adapter;
	private int currentItem;
	
	@SuppressWarnings("unchecked")
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		this.historyProvider = (IHistoryProvider) activity;
		this.historico = (ArrayList<History>) historyProvider.getHistory(); 
	}	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		currentItem = 0;
		Bundle args = getArguments();
		if(args != null && args.containsKey(HISTORY_ITEM_POSITION)) {
			currentItem = args.getInt(HISTORY_ITEM_POSITION);
		}
	}


	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		
		View v = inflater.inflate(R.layout.fragment_viewpager,
				container, false);
		viewPager = (ListAwareViewPager) v.findViewById(R.id.viewpager);
		
		/* Sets margin between pages */
//		viewPager.setPageMargin(getResources().getDimensionPixelSize(
//				R.dimen.viewpager_page_margin));
		
		adapter = new Adapter_Frag_Hist_Entry(getActivity(),
				getChildFragmentManager(), historyProvider, historico.size());

		viewPager.setAdapter(adapter);		
		viewPager.setOnPageChangeListener(pageChangeListener);
		
		return v;
	}
	
		
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		viewPager.setCurrentItem(currentItem);
		pageChangeListener.onPageSelected(currentItem);
	}

	OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			try {
				IUpdateMap frag = (IUpdateMap) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
				frag.updateMap();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	@Override
	public void previousPage() {
		if(viewPager.getCurrentItem() > 0)
			viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
	}

	@Override
	public void nextPage() {
		if(viewPager.getCurrentItem() < historico.size())
			viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
	}
}

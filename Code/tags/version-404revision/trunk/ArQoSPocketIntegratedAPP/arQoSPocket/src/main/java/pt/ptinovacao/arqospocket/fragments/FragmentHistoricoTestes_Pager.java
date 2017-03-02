package pt.ptinovacao.arqospocket.fragments;

import pt.ptinovacao.arqospocket.adapters.Adapter_Fragments_Historico_Testes;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.util.ListAwareViewPager;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentHistoricoTestes_Pager extends Fragment {

//	private ActivityTestesHistorico activity;
	private ListAwareViewPager viewPager;
	private Adapter_Fragments_Historico_Testes adapter;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
//		this.activity = (ActivityTestesHistorico) activity;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_viewpager,
				container, false);
		viewPager = (ListAwareViewPager) v.findViewById(R.id.viewpager);
		viewPager.setPageMargin(getResources().getDimensionPixelSize(
				R.dimen.viewpager_page_margin));
		
		adapter = new Adapter_Fragments_Historico_Testes(
				getChildFragmentManager());

		viewPager.setAdapter(adapter);
		
		return v;
	}
}

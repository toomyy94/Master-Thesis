package pt.ptinovacao.arqospocket.adapters;

import java.util.ArrayList;

import pt.ptinovacao.arqospocket.HistoricoTestesItem;
import pt.ptinovacao.arqospocket.fragments.FragmentTestesHistorico;
import pt.ptinovacao.arqospocket.fragments.FragmentTestesHistorico_Mapa;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

//public class Adapter_Fragments_AnomaliasHistorico extends FragmentPagerAdapter {
public class Adapter_Fragments_Historico_Testes extends
		FragmentStatePagerAdapter {

	private int NUMBER_OF_PAGES = 2;
	private ArrayList<HistoricoTestesItem> historicoTestes;

	public Adapter_Fragments_Historico_Testes(FragmentManager fm) {
		super(fm);
	}
	
	public Adapter_Fragments_Historico_Testes(FragmentManager fm, ArrayList<HistoricoTestesItem> historicoTestes) {
		this(fm);
		this.historicoTestes = historicoTestes;
	}	

	@Override
	public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			return new FragmentTestesHistorico();

		case 1:
			return new FragmentTestesHistorico_Mapa();

		default:
			break;
		}
		return null;

	}

	@Override
	public int getCount() {
		return NUMBER_OF_PAGES;
	}

	public void setCount(int count) {
		this.NUMBER_OF_PAGES = count;
	}

}

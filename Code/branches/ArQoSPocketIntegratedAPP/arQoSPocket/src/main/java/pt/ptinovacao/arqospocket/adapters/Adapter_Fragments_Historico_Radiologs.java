package pt.ptinovacao.arqospocket.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import pt.ptinovacao.arqospocket.fragments.FragmentRadiologsHistorico;
import pt.ptinovacao.arqospocket.fragments.FragmentRadiologsHistorico_Mapa;


public class Adapter_Fragments_Historico_Radiologs extends
		FragmentStatePagerAdapter {

	private int NUMBER_OF_PAGES = 2;

	public Adapter_Fragments_Historico_Radiologs(FragmentManager fm) {
		super(fm);
	}

	@Override
	public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			return new FragmentRadiologsHistorico();

		case 1:
			return new FragmentRadiologsHistorico_Mapa();

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

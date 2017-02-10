package pt.ptinovacao.arqospocket.adapters;

import pt.ptinovacao.arqospocket.fragments.FragmentAnomaliasHistorico;
import pt.ptinovacao.arqospocket.fragments.FragmentAnomaliasHistorico_Entry;
import pt.ptinovacao.arqospocket.fragments.FragmentAnomaliasHistorico_Mapa;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

//public class Adapter_Fragments_AnomaliasHistorico extends FragmentPagerAdapter {
public class Adapter_Fragments_AnomaliasHistorico extends
		FragmentStatePagerAdapter {

	private int NUMBER_OF_PAGES = 2;

	public Adapter_Fragments_AnomaliasHistorico(FragmentManager fm) {
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
			return new FragmentAnomaliasHistorico();

		case 1:
			return new FragmentAnomaliasHistorico_Mapa();

		case 2:
			return new FragmentAnomaliasHistorico_Entry();

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

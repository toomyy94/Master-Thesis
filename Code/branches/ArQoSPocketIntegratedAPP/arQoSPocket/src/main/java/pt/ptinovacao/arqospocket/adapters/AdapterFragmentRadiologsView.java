package pt.ptinovacao.arqospocket.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import pt.ptinovacao.arqospocket.fragments.FragmentEnvfeedback_Anomaly;
import pt.ptinovacao.arqospocket.fragments.FragmentEnvfeedback_Radiologs;

public class AdapterFragmentRadiologsView extends FragmentPagerAdapter {

	ArrayList<Fragment> fragments;

	public AdapterFragmentRadiologsView(FragmentManager fm) {
		super(fm);

		fragments = new ArrayList<Fragment>();
		fragments.add(new FragmentEnvfeedback_Radiologs());
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return 1;
	}

}

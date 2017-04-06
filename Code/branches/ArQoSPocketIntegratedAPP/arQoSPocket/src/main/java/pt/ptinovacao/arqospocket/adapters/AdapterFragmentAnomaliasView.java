package pt.ptinovacao.arqospocket.adapters;

import java.util.ArrayList;

import pt.ptinovacao.arqospocket.fragments.FragmentAbtfalha;
import pt.ptinovacao.arqospocket.fragments.FragmentEnvfeedback_Anomaly;
import pt.ptinovacao.arqospocket.fragments.FragmentIdtlocal;
import pt.ptinovacao.arqospocket.fragments.FragmentTipfalha;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AdapterFragmentAnomaliasView extends FragmentPagerAdapter {

	ArrayList<Fragment> fragments;

	public AdapterFragmentAnomaliasView(FragmentManager fm) {
		super(fm);

		fragments = new ArrayList<Fragment>();

		fragments.add(new FragmentAbtfalha());
		fragments.add(new FragmentTipfalha());
		fragments.add(new FragmentIdtlocal());
		fragments.add(new FragmentEnvfeedback_Anomaly());

	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			// if(position < fragments.size())
			return fragments.get(position);
			// else {
			// fragments.add(new FragmentAbtfalha());
			// return fragments.get(position);
			// }
		case 1:
			// if(position < fragments.size())
			return fragments.get(position);
			// else{
			// fragments.add(new FragmentTipfalha());
			// return fragments.get(position);
			// }

		case 2:
			// if(position < fragments.size())
			return fragments.get(position);
			// else{
			// fragments.add(new FragmentIdtlocal());
			// return fragments.get(position);
			// }

		case 3:
			// if(position < fragments.size())
			return fragments.get(position);
			// else{
			// fragments.add(new FragmentEnvfeedback_Anomaly());
			// return fragments.get(position);
			// }

		default:
			break;
		}
		return null;

	}

	@Override
	public int getCount() {

		return 4;
	}

}

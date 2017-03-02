package pt.ptinovacao.arqospocket.adapters;

import pt.ptinovacao.arqospocket.activities.ActivityDashboardMain;
import pt.ptinovacao.arqospocket.fragments.FragmentDashboard;
import pt.ptinovacao.arqospocket.fragments.FragmentDashboardDetailMobile;
import pt.ptinovacao.arqospocket.fragments.FragmentDashboardDetailWifi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class DashBoardPageAdapter extends FragmentPagerAdapter {

	private static final String TAG = "DashBoardPageAdapter";
	boolean isMobile;

	public DashBoardPageAdapter(FragmentManager fm, boolean isMobile) {
		super(fm);
		this.isMobile = isMobile;
	}

	@Override
	public Fragment getItem(int position) {

		if (position == 0) {

			return new FragmentDashboard();
		} else if (position == 1) {
			Log.d(TAG, "Returning Mobile Network Version");
			return new FragmentDashboardDetailMobile();
		} else if (position == 2) {
			Log.d(TAG, "Returning WiFi Version");
			return new FragmentDashboardDetailWifi();
		}
		return null;

	}

	@Override
	public int getCount() {

		return ActivityDashboardMain.PAGE_NUMBER;
	}

}

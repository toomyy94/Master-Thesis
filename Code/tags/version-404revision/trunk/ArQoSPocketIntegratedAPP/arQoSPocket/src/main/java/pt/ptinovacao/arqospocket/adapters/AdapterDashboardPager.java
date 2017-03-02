package pt.ptinovacao.arqospocket.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import pt.ptinovacao.arqospocket.fragments.FragmentDashboard;
import pt.ptinovacao.arqospocket.fragments.FragmentDashboardDetailMobile;
import pt.ptinovacao.arqospocket.fragments.FragmentDashboardDetailWifi;
import pt.ptinovacao.arqospocket.util.MyObject;
import pt.ptinovacao.arqospocket.util.Utils;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AdapterDashboardPager extends FragmentPagerAdapter {

	boolean isMobile;
	private HashMap<Long, Fragment> mItems = new HashMap<Long, Fragment>();
	private ArrayList<MyObject> dataset;
	
	public AdapterDashboardPager(FragmentManager fm, boolean isMobile, ArrayList<MyObject> objects) {
		super(fm);
		this.isMobile = isMobile;
		this.dataset = objects;
		
        mItems.put(Utils.DASH_ID, FragmentDashboard.newInstance());
        mItems.put(Utils.NETWORK_DASH_ID, FragmentDashboardDetailMobile.newInstance());
        mItems.put(Utils.WIFI_DASH_ID, FragmentDashboardDetailWifi.newInstance());
	}

	@Override
    public int getCount() {
        return dataset.size();
    }

    @Override
    public Fragment getItem(int position) {
        long id = getItemId(position);

        if(mItems.get(id) != null) {
            // caching to prevent multiple instances of the same fragment
            // for the same position/id
            return mItems.get(id);
        }

        Fragment f = null;
//        if(position == 0)
//        	f = FragmentDashboard.newInstance();
//        else {
//        	if(dataset.get(1).getUniqueId() == Utils.NETWORK_DASH_ID)
//        		f = FragmentDashboardDetailMobile.newInstance();
//        	else if(dataset.get(1).getUniqueId() == Utils.WIFI_DASH_ID)
//        		f = FragmentDashboardDetailWifi.newInstance();
//        }
//
//        mItems.put(id, f);

        return f;
    }

    @Override
    public long getItemId(int position) {
        // return a unique id
        return dataset.get(position).getUniqueId();
    }

    @Override
    public int getItemPosition(Object object) {
        /*
         * Purpose of this method is to check whether an item in the adapter
         * still exists in the dataset and where it should show.
         * For each entry in dataset, request its Fragment.
         * 
         * If the Fragment is found, return its (new) position. There's
         * no need to return POSITION_UNCHANGED; ViewPager handles it.
         * 
         * If the Fragment passed to this method is not found, remove all
         * references and let the ViewPager remove it from display by
         * by returning POSITION_NONE;
         */
        Fragment f = (Fragment) object;

        for(int i = 0; i < getCount(); i++) {

            Fragment item = (Fragment) getItem(i);
            if(item.equals(f)) {
                // item still exists in dataset; return position
                return i;
            }
        }

        // if we arrive here, the data-item for which the Fragment was created
        // does not exist anymore.

        // Also, cleanup: remove reference to Fragment from mItems
//        for(Map.Entry<Long, Fragment> entry : mItems.entrySet()) {
//            if(entry.getValue().equals(f)) {
//                mItems.remove(entry.getKey());
//                break;
//            }
//        }

        // Let ViewPager remove the Fragment by returning POSITION_NONE.
        return POSITION_NONE;
    }
	
//	@Override
//	public Fragment getItem(int position) {
//
//		if (position == 0) {
//
//			return new FragmentDashboard();
//		} else if (position == 1) {
//			Log.d("arqos", "Returning Mobile Network Version");
//			return new FragmentDashboardDetailMobile();
//		} else if (position == 2) {
//			Log.d("arqos", "Returning WiFi Version");
//			return new FragmentDashboardDetailWifi();
//		}
//		return null;
//
//	}
}

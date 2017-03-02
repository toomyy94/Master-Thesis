// from https://gist.github.com/4392030

package pt.ptinovacao.arqospocket.map;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.SupportMapFragment;

public class ArqOsSupportMapFragment extends SupportMapFragment{

	private FragmentActivity mActivity;
 
	public FragmentActivity getSherlockActivity() {
		
		return mActivity;
	
	}

	
	@Override
	public void onAttach(Activity activity) {
		if (!(activity instanceof FragmentActivity)) {
			throw new IllegalStateException(getClass().getSimpleName() + " must be attached to a SherlockFragmentActivity.");
		}
		
		mActivity=(FragmentActivity) activity;

		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		
		mActivity=null;
		super.onDetach();
	
	}
	
	


	//  @Override
	//  public final void onCreateOptionsMenu(android.view.Menu menu,
	//                                        android.view.MenuInflater inflater) {
	//    onCreateOptionsMenu(new MenuWrapper(menu),
	//                        mActivity.getSupportMenuInflater());
	//  }
	//
	//  @Override
	//  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	//    // Nothing to see here.
	//  }
	//
	//  @Override
	//  public final void onPrepareOptionsMenu(android.view.Menu menu) {
	//    onPrepareOptionsMenu(new MenuWrapper(menu));
	//  }
	//
	//  @Override
	//  public void onPrepareOptionsMenu(Menu menu) {
	//    // Nothing to see here.
	//  }
	//
	//  @Override
	//  public final boolean onOptionsItemSelected(android.view.MenuItem item) {
	//    return onOptionsItemSelected(new MenuItemWrapper(item));
	//  }
	//
	//  @Override
	//  public boolean onOptionsItemSelected(MenuItem item) {
	//    // Nothing to see here.
	//    return false;
	//  }
}
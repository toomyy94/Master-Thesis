/**
 * 
 */
package pt.ptinovacao.arqospocket.map;

import java.util.Stack;

import pt.ptinovacao.arqospocket.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

/**
 *
 */
public class PreferencesFragmentActivity extends FragmentActivity {

    private Stack<Fragment> mFragmentStack = new Stack<Fragment>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getSupportActionBar().setHomeButtonEnabled(false);
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setDisplayShowTitleEnabled(true);
		FragmentManager fragmentManager = getSupportFragmentManager();
		setContentView(R.layout.content_frame);
		Fragment preferencesFragment = new ArQosMapFragment();
		FragmentTransaction transaction = fragmentManager.beginTransaction().replace(R.id.content_frame,
			preferencesFragment);
		transaction.commit();
    }

    @Override
    public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, 0);
    }

    @Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case android.R.id.home:
			if (mFragmentStack.size() > 0)
				mFragmentStack.pop();
			else
				onBackPressed();
			break;
		}

		return true;
	}
}

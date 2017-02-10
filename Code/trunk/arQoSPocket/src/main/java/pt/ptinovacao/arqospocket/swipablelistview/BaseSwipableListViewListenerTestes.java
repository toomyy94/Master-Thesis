package pt.ptinovacao.arqospocket.swipablelistview;

import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.fragments.FragmentTestesDetalhes;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class BaseSwipableListViewListenerTestes extends
		BaseSwipeListViewListener {

	FragmentManager mgr;

	public BaseSwipableListViewListenerTestes(Context context,
			FragmentManager mgr) {
		super(context);
		this.mgr = mgr;
	}

	@Override
	public void onClickFrontView(int position) {
		/* Same as the onClickListener. */

		FragmentTransaction ft = mgr.beginTransaction();
		FragmentTestesDetalhes llf = new FragmentTestesDetalhes();
		ft.replace(R.id.fragment_testes, llf);
		ft.addToBackStack(null);
		ft.commit();

	}

}

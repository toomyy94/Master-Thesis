package pt.ptinovacao.arqospocket.adapters;

import pt.ptinovacao.arqospocket.activities.ActivityTestesHistorico;
import pt.ptinovacao.arqospocket.fragments.FragmentAnomaliasHistorico_Entry;
import pt.ptinovacao.arqospocket.fragments.FragmentTestesHistorico_Entry;
import pt.ptinovacao.arqospocket.interfaces.IHistoryProvider;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class Adapter_Frag_Hist_Entry extends FragmentPagerAdapter {
	private static final String HISTORY_ITEM_POSITION = "HistoryItemPosition";
	Context ctx = null;
	  IHistoryProvider provider;
	  int count = 0;

	  public Adapter_Frag_Hist_Entry(Context ctx, FragmentManager mgr, IHistoryProvider holder, int count) {
	    super(mgr);
	    this.ctx = ctx;
	    this.provider = holder;
	    this.count = count;
	  }

	  @Override
	  public int getCount() {
	    return(count);
	  }

	  @Override
	public Fragment getItem(int position) {
		Bundle bundle = new Bundle();
		bundle.putInt(HISTORY_ITEM_POSITION, position);
		Fragment frag;

		if (provider instanceof ActivityTestesHistorico)
			frag = new FragmentTestesHistorico_Entry();
		else
			frag = new FragmentAnomaliasHistorico_Entry();

		frag.setArguments(bundle);
		return frag;
	}

//	  @Override
//	  public String getPageTitle(int position) {
//	    return(EditorFragment.getTitle(ctxt, position));
//	  }
}

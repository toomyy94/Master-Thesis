package pt.ptinovacao.arqospocket.util;

import pt.ptinovacao.arqospocket.swipablelistview.SwipeListView;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

public class ListAwareViewPager extends ViewPager {
	public ListAwareViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		if (v instanceof ListView || v instanceof SwipeListView) {
			return (true);
		}

		return (super.canScroll(v, checkV, dx, x, y));
	}
}

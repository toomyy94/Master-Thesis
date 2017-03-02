package pt.ptinovacao.arqospocket.swipablelistview;

import pt.ptinovacao.arqospocket.interfaces.IFragmentChange;
import android.content.Context;
import android.support.v4.app.FragmentManager;

public class BaseSwipeListViewListener implements SwipeListViewListener {
	
	private Context context;
	private FragmentManager fragMgr;
	private IFragmentChange fc;
	
	public BaseSwipeListViewListener(Context context) {
		this.context = context;
	}
	
	public BaseSwipeListViewListener(IFragmentChange fc, Context context, FragmentManager fragMgr) {
		this.context = context;
		this.fragMgr = fragMgr;
		this.fc = fc;
	}
	
    @Override
    public void onOpened(int position, boolean toRight) {
    }

    @Override
    public void onClosed(int position, boolean fromRight) {
    }

    @Override
    public void onListChanged() {
    }

    @Override
    public void onMove(int position, float x) {
    }

    @Override
    public void onStartOpen(int position, int action, boolean right) {
    }

    @Override
    public void onStartClose(int position, boolean right) {
    }

    @Override
    public void onClickFrontView(int position) {
    	/* Same as the onClickListener. */
//    	((INextPage) context).nextPage(0);
    	
//    	fragMgr.beginTransaction()
//		.add(android.R.id.content,
//				new FragmentTestesHistorico_Entry_Pager()).commit();
    	
//    	context.startActivity(new Intent(context, ActivityTestesHistorico_Detalhes.class));
    	
    	fc.changeFragment(position);
    }

    @Override
    public void onClickBackView(int position) {
    }

    @Override
    public void onDismiss(int[] reverseSortedPositions) {
    }

    @Override
    public int onChangeSwipeMode(int position) {
        return SwipeListView.SWIPE_MODE_DEFAULT;
    }

    @Override
    public void onChoiceChanged(int position, boolean selected) {
    }

    @Override
    public void onChoiceStarted() {
    }

    @Override
    public void onChoiceEnded() {
    }

    @Override
    public void onFirstListItem() {
    }

    @Override
    public void onLastListItem() {
    }
}

package PTInov.IEX.ArQoSPocket.UserInterface;

import java.util.List;

import PTInov.IEX.ArQoSPocket.UserInterface.Style.RowDataTwoLinesHistory;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ShowTarefaResult extends ListActivity{
	
	private final String tag = "ShowTarefaResult";
	
	private LayoutInflater mInflater = null;
	private ListAdapter adapter = null;
	private List<RowDataTwoLinesHistory> data = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	  
		Log.v(tag, "Estou na ShowTestResultLog!!!");
		
		//String[] history = MainInterface.getHistoryCircularStore().getAllTask(HistoryInterface.indexHistory);
		/*
		String[] history = MainInterface.getHistoryCircularStore().getAllInformationAboutTask(HistoryInterface.indexHistory, ShowTestResultLog.indexTask);
		
		this.setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, history));
		*/
		
		if (HistoryInterface.historyType == 0) data = MainInterface.getHistoryCircularStore().getAllInformationAboutTask(HistoryInterface.indexHistory, ShowTestResultLog.indexTask);
		else data = MainInterface.getErrorHistoryCircularStore().getAllInformationAboutTask(HistoryInterface.indexHistory, ShowTestResultLog.indexTask);

		adapter = new ListAdapter(this,R.layout.rowlistwithtwolines, data);
		setListAdapter(adapter);
	}
	
	
	
	private class ListAdapter extends ArrayAdapter<RowDataTwoLinesHistory> {

		public ListAdapter(Context context,
				int textViewResourceId, List<RowDataTwoLinesHistory> objects) {
			super(context, textViewResourceId, objects);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;

			// widgets displayed by each item in your list
			TextView item = null;
			TextView description = null;

			// data from your adapter
			RowDataTwoLinesHistory rowData = getItem(position);

			// we want to reuse already constructed row views...
			if (null == convertView) {
				
				convertView = mInflater.inflate(R.layout.rowlistwithtwolines, null);			
				holder = new ViewHolder(convertView);				
				convertView.setTag(holder);
			}
			
			holder = (ViewHolder) convertView.getTag();
			item = holder.getItem();
			item.setText(rowData.mItem());

			description = holder.getDescription();
			description.setText(rowData.mDescription());
			
			return convertView;
		}
	}

	/**
	 * Wrapper for row data.
	 * 
	 */
	private class ViewHolder {
		private View mRow;
		private TextView description = null;
		private TextView item = null;

		public ViewHolder(View row) {
			mRow = row;
		}

		public TextView getDescription() {
			
			if (null == description) {
				description = (TextView) mRow.findViewById(R.id.logtext2);
			}
			
			return description;
		}

		public TextView getItem() {
			
			if (null == item) {
				item = (TextView) mRow.findViewById(R.id.logtext1);
			}

			return item;
		}
	}

}

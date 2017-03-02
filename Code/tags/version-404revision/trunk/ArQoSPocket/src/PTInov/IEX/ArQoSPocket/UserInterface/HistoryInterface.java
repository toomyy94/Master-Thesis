package PTInov.IEX.ArQoSPocket.UserInterface;


import java.util.ArrayList;
import java.util.List;

import PTInov.IEX.ArQoSPocket.TaskStore.TaskStoreStruct;
import PTInov.IEX.ArQoSPocket.UserInterface.Style.RowDataTwoLinesHistory;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HistoryInterface extends ListActivity{
	
	private final String tag = "HistoryInterface";
	
	public static int indexHistory = -1;
	
	// historico normal - 0, historico de erros - 1
	public static int historyType = 0;
	
	private static final int NORMAL_HISTORY = Menu.FIRST;
	private static final int ERROR_HISTORY = Menu.FIRST + 1;
	
	private Menu myMenu = null;
	
	private LayoutInflater mInflater = null;
	private ListAdapter adapter = null;
	private List<RowDataTwoLinesHistory> data = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	  
		Log.v(tag, "Estou na HistoryInterface!!!");
		
		mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		/*
		String[] history = MainInterface.getHistoryCircularStore().getAllHistory();
		
		this.setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, history));
				*/
		
		if (historyType == 0) data = convertTaskStoreStructToRowData(MainInterface.getHistoryCircularStore().getHistoryList());
		else data = convertTaskStoreStructToRowData(MainInterface.getErrorHistoryCircularStore().getHistoryList());

		adapter = new ListAdapter(this,R.layout.rowlistwithtwolines, data);
		setListAdapter(adapter);
		
	}
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		indexHistory = position;
		Log.v(tag, "You selected: " + position+" item");
		
		startActivity(new Intent(this, ShowTestResultLog.class));
	}
	
	
	private List<RowDataTwoLinesHistory> convertTaskStoreStructToRowData(List<TaskStoreStruct> tssList) {
		
		ArrayList<RowDataTwoLinesHistory> myRowData = new ArrayList<RowDataTwoLinesHistory>();
		
		if (tssList.size()==0) {
			myRowData.add(new RowDataTwoLinesHistory("Histórico Vazio"," ",true));
		}
		
		for (TaskStoreStruct tss:tssList) {
			myRowData.add(new RowDataTwoLinesHistory(tss.getTestHeadStruct().getTestName(),tss.getFileNameStructObject().getDataInicioInterfaceFormat(),tss.getTestState()));
		}
		
		return myRowData;
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
			if (!rowData.mSucess()) {
				item.setTextColor(Color.RED);
			}

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

	
	// ---------------------- Create Options menu

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, NORMAL_HISTORY, 0, "Histórico Testes");
		menu.add(0, ERROR_HISTORY, 0, "Histórico Erros");

		if (historyType == 0) menu.findItem(NORMAL_HISTORY).setEnabled(false);
		else menu.findItem(ERROR_HISTORY).setEnabled(false);
		
		myMenu = menu;
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case NORMAL_HISTORY:
			
			data = convertTaskStoreStructToRowData(MainInterface.getHistoryCircularStore().getHistoryList());
			adapter = new ListAdapter(this,R.layout.rowlistwithtwolines, data);
			adapter.notifyDataSetChanged();
			
			item.setEnabled(false);
			myMenu.findItem(ERROR_HISTORY).setEnabled(true);
			historyType = 0;
			
			startActivity(new Intent(this, HistoryInterface.class));
			this.finish();
			
			break;
		case ERROR_HISTORY:
			
			data = convertTaskStoreStructToRowData(MainInterface.getErrorHistoryCircularStore().getHistoryList());
			List<RowDataTwoLinesHistory> data2 = new ArrayList<RowDataTwoLinesHistory>();
			adapter = new ListAdapter(this,R.layout.rowlistwithtwolines, data2);
			adapter.notifyDataSetChanged();
			
			item.setEnabled(false);
			myMenu.findItem(NORMAL_HISTORY).setEnabled(true);
			historyType = 1;
			
			startActivity(new Intent(this, HistoryInterface.class));
			this.finish();
			
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}

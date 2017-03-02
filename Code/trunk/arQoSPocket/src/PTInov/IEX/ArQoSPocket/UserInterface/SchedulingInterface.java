package PTInov.IEX.ArQoSPocket.UserInterface;

import java.util.ArrayList;
import java.util.List;


import PTInov.IEX.ArQoSPocket.TaskStore.TaskStoreStruct;
import PTInov.IEX.ArQoSPocket.UserInterface.Style.RowDataTwoLinesHistory;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SchedulingInterface extends ListActivity{

	private final String tag = "SchedulingInterface";
	
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
		
		data = convertTaskStoreStructToRowData(MainInterface.getMyRuningTaskStore(), MainInterface.getMyPausedTaskStore(), MainInterface.getMySuspendedTaskStore());

		adapter = new ListAdapter(this,R.layout.rowlistwithtwolines, data);
		setListAdapter(adapter);

		
	}
	
	private List<RowDataTwoLinesHistory> convertTaskStoreStructToRowData(List<TaskStoreStruct> tssRunList, List<TaskStoreStruct> tssPauseList, List<TaskStoreStruct> suspendedList) {
		
		ArrayList<RowDataTwoLinesHistory> myRowData = new ArrayList<RowDataTwoLinesHistory>();
		
		if (tssRunList.size()==0 && tssPauseList.size()==0) {
			myRowData.add(new RowDataTwoLinesHistory("Agendamento Vazio"," ",true));
		}
		
		for (TaskStoreStruct tss:tssRunList) {
			myRowData.add(new RowDataTwoLinesHistory(tss.getTestHeadStruct().getTestName(),tss.getFileNameStructObject().getDataInicioInterfaceFormat(),true));
		}
		
		for (TaskStoreStruct tss:tssPauseList) {
			myRowData.add(new RowDataTwoLinesHistory(tss.getTestHeadStruct().getTestName(),tss.getFileNameStructObject().getDataInicioInterfaceFormat(),true));
		}
		
		for (TaskStoreStruct tss:suspendedList) {
			myRowData.add(new RowDataTwoLinesHistory(tss.getTestHeadStruct().getTestName(),tss.getFileNameStructObject().getDataInicioInterfaceFormat(),true));
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
}

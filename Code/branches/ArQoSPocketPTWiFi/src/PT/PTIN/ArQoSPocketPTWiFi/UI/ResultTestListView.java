package PT.PTIN.ArQoSPocketPTWiFi.UI;

import java.util.List;

import PT.PTIN.ArQoSPocketPTWiFi.R;
import PT.PTIN.ArQoSPocketPTWiFi.Utils.RowDataTwoLines;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultTestListView extends ArrayAdapter<RowDataTwoLines>{
	
	private final String tag = "SchedulingListViewAdapter";
	
	private Context context;
	
	public ResultTestListView(Context pcontext,
			int textViewResourceId, List<RowDataTwoLines> objects) {
		super(pcontext, textViewResourceId, objects);
		
		context = pcontext;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		// widgets displayed by each item in your list
		TextView item = null;
		TextView description = null;
		
		String tagMethod = "onCreate";
		
		// data from your adapter
		RowDataTwoLines rowData = getItem(position);

		// we want to reuse already constructed row views...
		if (null == convertView) {
			
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();			
			convertView = inflater.inflate(R.layout.rowlistwithtwolinesandtwoimgs, null);						
			holder = new ViewHolder(convertView);					
			convertView.setTag(holder);
		}

		holder = (ViewHolder) convertView.getTag();
		item = holder.getItem();		
		item.setText(rowData.mItem());

		description = holder.getDescription();
		description.setText(rowData.mDescription());
		
		// caso seja a msg de default de lista vazia, não escreve o estado de teste
		if (!rowData.mItem().equals("Sem Resultados")) {
		
			ImageView iv = null;
			
			// verifica se houve ou não sucesso
			if (!rowData.mSucess()) {			
				//mete a seta vermelha
				iv = (ImageView) convertView.findViewById(R.id.stateImg);
				iv.setImageResource(R.drawable.notok);
				iv.setVisibility(1);
			} else {
				//mete a seta verde
				iv = (ImageView) convertView.findViewById(R.id.stateImg);
				iv.setImageResource(R.drawable.ok);
				iv.setVisibility(1);
			}
			
			// adiciona a seta
			iv = (ImageView) convertView.findViewById(R.id.menuInfoImg);
			iv.setVisibility(1);
		}
		
		
		
		
		return convertView;
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
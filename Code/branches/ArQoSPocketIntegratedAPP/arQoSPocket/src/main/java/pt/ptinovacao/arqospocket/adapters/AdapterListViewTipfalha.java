package pt.ptinovacao.arqospocket.adapters;

import java.util.List;

import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.RowItemtip;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class AdapterListViewTipfalha extends ArrayAdapter<RowItemtip> {

	Context context;

	public AdapterListViewTipfalha(Context context, int resourceId,
			List<RowItemtip> items) {
		super(context, resourceId, items);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		RowItemtip rowItemtip = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listview_anomalias_tip,
					parent, false);
		}

		TextView txtDesc = (TextView) convertView
				.findViewById(R.id.textlistViewTip);
		TextView txtNum = (TextView) convertView
				.findViewById(R.id.numlistViewTip);
		LinearLayout linha = (LinearLayout) convertView
				.findViewById(R.id.linearLayoutLinhaTip);
		txtDesc.setText(rowItemtip.getDesc());
		txtNum.setText(String.valueOf(rowItemtip.getNum()));

		Boolean select = rowItemtip.getSelect();
		if (select == true) {
			convertView
					.setBackgroundResource(R.drawable.list_view_abtanomalias_selected);
		} else {
			convertView
					.setBackgroundResource(R.drawable.list_view_abtanomalias);
		}

		return convertView;

	}

}

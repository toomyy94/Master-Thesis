package pt.ptinovacao.arqospocket.adapters;

import java.util.ArrayList;

import pt.ptinovacao.arqospocket.DetailItem;
import pt.ptinovacao.arqospocket.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterDashboardDetail extends BaseAdapter {

	Context context;
	ArrayList<DetailItem> dList;

	public AdapterDashboardDetail(Context context, ArrayList<DetailItem> details) {
		this.context = context;
		dList = details;
	}

	@Override
	public int getCount() {

		return dList.size();
	}

	@Override
	public Object getItem(int position) {

		return dList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		TextView label, value;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(
					R.layout.adapter_dashboard_detail_wifi, parent, false);
		}

		label = (TextView) convertView
				.findViewById(R.id.adapter_dashboard_detail_label);
		value = (TextView) convertView
				.findViewById(R.id.adapter_dashboard_detail_value);

		label.setText(dList.get(position).getFieldName());
		value.setText(dList.get(position).getFieldValue());

		return convertView;
	}

}

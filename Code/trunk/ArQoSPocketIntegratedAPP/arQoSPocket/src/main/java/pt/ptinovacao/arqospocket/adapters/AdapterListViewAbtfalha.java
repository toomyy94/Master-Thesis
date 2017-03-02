package pt.ptinovacao.arqospocket.adapters;

import java.util.List;

import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.RowItem;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class AdapterListViewAbtfalha extends ArrayAdapter<RowItem> {

	Context context;

	public AdapterListViewAbtfalha(Context context, int resourceId,
			List<RowItem> items) {
		super(context, resourceId, items);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		RowItem rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.listview_anomalias_abt_layout, parent, false);
		}

		TextView txtDesc = (TextView) convertView
				.findViewById(R.id.textlistView);
		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.imagelistView);
		LinearLayout linha = (LinearLayout) convertView
				.findViewById(R.id.linearLayoutLinha);
		txtDesc.setText(rowItem.getDesc());
		imageView.setImageResource(rowItem.getImage());

		Boolean select = rowItem.getSelect();
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

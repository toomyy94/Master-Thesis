package pt.ptinovacao.arqospocket.adapters;

import java.util.List;

import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.RowItemTestes;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterListViewTestes extends ArrayAdapter<RowItemTestes> {
	Context context;

	public AdapterListViewTestes(Context context, int resourceId,
			List<RowItemTestes> items) {
		super(context, resourceId, items);
		this.context = context;

	}

	@SuppressLint("ResourceAsColor")
	public View getView(int position, View convertView, ViewGroup parent) {

		RowItemTestes rowItemTestes = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.swipable_row_testes,
					parent, false);
		}
		ImageView imgPerc = (ImageView) convertView
				.findViewById(R.id.imagePercent);

		TextView txtPerc = (TextView) convertView
				.findViewById(R.id.text_percent);
		TextView txtName = (TextView) convertView.findViewById(R.id.nome_teste);
		
		ImageView imgState = (ImageView) convertView
				.findViewById(R.id.imagestate);
		ImageView imgbutton = (ImageView) convertView
				.findViewById(R.id.imagebutton);

		imgPerc.setImageResource(rowItemTestes.getPercImg());

		// txtPerc.setText(rowItemTestes.getTextPerc());
		//
		// txtPerc.setTextColor(rowItemTestes.getColor());
		// cor da percentagem
		if (rowItemTestes.getColor() != 0) {
			txtPerc.setTextColor(context.getResources().getColor(
					rowItemTestes.getColor()));
			txtPerc.setText(rowItemTestes.getTextPerc());
			// txtinactivo.setText(rowItemTestes.getInactivo());
		}
		txtName.setText(rowItemTestes.getTextName());
		imgState.setImageResource(rowItemTestes.getState());
		imgbutton.setImageResource(rowItemTestes.getButton());
		// txtinactivo.setText("0");

		return convertView;

	}
}

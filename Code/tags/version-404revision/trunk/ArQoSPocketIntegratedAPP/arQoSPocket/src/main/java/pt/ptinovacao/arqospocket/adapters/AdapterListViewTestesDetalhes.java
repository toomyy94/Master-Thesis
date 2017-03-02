package pt.ptinovacao.arqospocket.adapters;

import java.util.List;

import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.RowItemTestesDetalhes;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterListViewTestesDetalhes extends ArrayAdapter<RowItemTestesDetalhes> {
	private Context context;
	private static int resId = R.layout.listview_testes_detalhes;
	String utf8;
	public AdapterListViewTestesDetalhes(Context context, int resourceId,
			List<RowItemTestesDetalhes> items) {
		super(context, resourceId, items);
		this.context = context;
		resId = resourceId;
	}

	public AdapterListViewTestesDetalhes(Context context,
			List<RowItemTestesDetalhes> items) {
		super(context, resId, items);
		this.context = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listview_testes_detalhes, parent,
					false);
		}
		
		try {
		
			// tenho de personalizar
			RowItemTestesDetalhes rowItemTestesDetalhes = getItem(position);
		
			ImageView imgPerc = (ImageView) convertView
				.findViewById(R.id.image_Percent_deta);
			TextView txtPerc = (TextView) convertView
				.findViewById(R.id.text_percent_deta);
			ImageView imgState = (ImageView) convertView
				.findViewById(R.id.imageState_deta);
			TextView txtName = (TextView) convertView.findViewById(R.id.nome_teste_deta);		
		
			ImageView imgbutton = (ImageView) convertView
				.findViewById(R.id.imagebutton_deta);
		
			imgPerc.setImageResource(rowItemTestesDetalhes.getPercImg());
		
		
			txtPerc.setText(rowItemTestesDetalhes.getTextPerc());
		
			txtPerc.setTextColor(context.getResources().getColor(rowItemTestesDetalhes.getColor()));				
			txtName.setText(rowItemTestesDetalhes.getTextName());
//		utf8=Encoding_utf8.encoding(rowItemTestesDetalhes.getTextName());
//		txtName.setText(utf8);
	
			imgState.setImageResource(rowItemTestesDetalhes.getState());
			if (rowItemTestesDetalhes.getButton() == true) {
				imgbutton.setImageResource(R.drawable.bot_more_teste);
				imgbutton.setVisibility(View.VISIBLE);
				imgbutton.setClickable(true);
			} else {
				imgbutton.setVisibility(View.INVISIBLE);
//			convertView.setClickable(false);			
			}
		
		} catch(Exception ex) {
			
		}

		return convertView;
	}
}




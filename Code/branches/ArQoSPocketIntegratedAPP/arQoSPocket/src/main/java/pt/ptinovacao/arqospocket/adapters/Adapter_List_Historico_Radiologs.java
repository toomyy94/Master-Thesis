package pt.ptinovacao.arqospocket.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pt.ptinovacao.arqospocket.HistoricoAnomaliasItem;
import pt.ptinovacao.arqospocket.HistoricoRadiologsItem;
import pt.ptinovacao.arqospocket.R;

public class Adapter_List_Historico_Radiologs extends
		ArrayAdapter<HistoricoRadiologsItem> {

	// private static final int rowLayout =
	// R.layout.fragment_anomalias_historico_item;
	private static final int rowLayout = R.layout.swipable_row_anomalias;
	private Context context;

	public Adapter_List_Historico_Radiologs(Context context,
                                            ArrayList<HistoricoRadiologsItem> historicoTestes) {
		super(context, rowLayout, historicoTestes);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HistoricoRadiologsItem item = (HistoricoRadiologsItem) getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		convertView = mInflater.inflate(rowLayout, parent, false);

		ImageView imgType = (ImageView) convertView
				.findViewById(R.id.img_historico_anomalias_type);
		imgType.setImageResource(item.get_logo_resource_id());

		TextView type = (TextView) convertView
				.findViewById(R.id.tv_historico_anomalias_type);
		type.setText(item.get_radiolog_id());

		TextView date = (TextView) convertView
				.findViewById(R.id.tv_historico_anomalias_date);
		date.setText(item.get_radiolog_report_date_string());

		return convertView;
	}

}

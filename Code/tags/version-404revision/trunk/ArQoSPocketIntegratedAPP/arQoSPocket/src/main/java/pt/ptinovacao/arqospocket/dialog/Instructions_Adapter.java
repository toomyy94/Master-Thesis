package pt.ptinovacao.arqospocket.dialog;

import java.util.List;

import pt.ptinovacao.arqospocket.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Instructions_Adapter extends ArrayAdapter<Instruction_Entry> {

	private static final int rowLayout = R.layout.instructions_row;
	private Context context;

	public Instructions_Adapter(Context context, List<Instruction_Entry> objects) {
		super(context, rowLayout, objects);
		this.context = context;
	}

	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {
		Instruction_Entry instrEntry = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		convertView = mInflater.inflate(rowLayout, parent, false);

		TextView rowText = (TextView) convertView.findViewById(R.id.row_text);
		rowText.setText(instrEntry.getText());

		ImageView moveTypeIcon = (ImageView) convertView.findViewById(R.id.move_type_icon);
		moveTypeIcon.setBackgroundResource(instrEntry.getMoveIcon_id());
		
//		if(instrEntry.getExplanation() != Integer.MIN_VALUE) {
//			TextView rowExplanationText = (TextView) convertView
//					.findViewById(R.id.row_explanation);
//			rowExplanationText.setText(instrEntry.getExplanation());
//			rowExplanationText.setVisibility(View.VISIBLE);
//		}
//		
//		TextView rowSubText = (TextView) convertView
//				.findViewById(R.id.row_sub_text);
//		rowSubText.setText(instrEntry.getSubText());
//
//		ImageView icon = (ImageView) convertView.findViewById(R.id.move_icon);
//		icon.setBackgroundResource(instrEntry.getIcon_id());

		return convertView;
	}
}

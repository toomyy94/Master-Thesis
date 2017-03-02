package pt.ptinovacao.arqospocket.adapters;

import java.util.List;

import pt.ptinovacao.arqospocket.DialogRowItem;
import pt.ptinovacao.arqospocket.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Adapter_spinner extends ArrayAdapter<DialogRowItem> {

	/**
     * Resource layout to use in this spinner.
     */
    private static int spinnerItemRes = android.R.layout.simple_spinner_item;
    /**
     * Resource layout to use in the dropdown/dialog of this spinner.
     */
    private static int spinnerDropdownRes = R.layout.rowdrop;
    
    public Adapter_spinner(Context context, List<DialogRowItem> items) {
		super(context, spinnerItemRes, items);
	}
    
	public Adapter_spinner(Context context, int resource, List<DialogRowItem> items) {
		super(context, resource, items);
		spinnerItemRes = resource;
	}

	public Adapter_spinner(Context ctx, int resource, int dropDownResource, List<DialogRowItem> items) {
		this(ctx, resource, items);
		spinnerDropdownRes = dropDownResource;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {

		DialogRowItem item = getItem(position);

		LayoutInflater inflater = getLayoutInflater();

		if(convertView == null)
			convertView = inflater.inflate(spinnerDropdownRes, parent, false);
		
		TextView label = (TextView) convertView.findViewById(R.id.textlistView);
		label.setText(item.getFieldName());

		ImageView icon = (ImageView) convertView.findViewById(R.id.imagelistView);
		icon.setImageResource(item.getFieldValue());

		return convertView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DialogRowItem item = getItem(position);

		LayoutInflater inflater = getLayoutInflater();

		if (convertView == null)
			convertView = inflater.inflate(spinnerItemRes, parent, false);

		TextView label = (TextView) convertView
				.findViewById(android.R.id.text1);
		label.setText(item.getFieldName());

		return convertView;
	}

	private LayoutInflater getLayoutInflater() {
		return LayoutInflater.from(this.getContext());
	}

}

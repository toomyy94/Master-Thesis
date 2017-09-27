package pt.ptinovacao.arqospocket.views.testshistoric;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pt.ptinovacao.arqospocket.R;

public class SpinnerFilterAdapter extends ArrayAdapter<SpinnerRowItem> {

    /**
     * Resource layout to use in this spinner.
     */
    private static int spinnerItemRes = android.R.layout.simple_spinner_item;
    /**
     * Resource layout to use in the dropdown/dialog of this spinner.
     */
    private static int spinnerDropdownRes = R.layout.rowdrop;

    public SpinnerFilterAdapter(Context context, List<SpinnerRowItem> items) {
        super(context, spinnerItemRes, items);
    }

    public SpinnerFilterAdapter(Context context, int resource, List<SpinnerRowItem> items) {
        super(context, resource, items);
        spinnerItemRes = resource;
    }

    public SpinnerFilterAdapter(Context ctx, int resource, int dropDownResource, List<SpinnerRowItem> items) {
        this(ctx, resource, items);
        spinnerDropdownRes = dropDownResource;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {

        SpinnerRowItem item = getItem(position);

        LayoutInflater inflater = getLayoutInflater();

        if (convertView == null)
            convertView = inflater.inflate(spinnerDropdownRes, parent, false);

        TextView label = (TextView) convertView.findViewById(R.id.textlistView);
        label.setText(item.getFieldName());

        ImageView icon = (ImageView) convertView.findViewById(R.id.imagelistView);
        icon.setImageResource(item.getFieldValue());

        return convertView;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        SpinnerRowItem item = getItem(position);

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

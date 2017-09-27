package pt.ptinovacao.arqospocket.views.dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import pt.ptinovacao.arqospocket.R;

/**
 * Adapter to display dashboard network data.
 * <p>
 * Created by Emílio Simões on 03-05-2017.
 */
public class DashboardDetailAdapter extends BaseAdapter {

    private Context context;

    private List<DetailItem> details;

    public DashboardDetailAdapter(Context context, List<DetailItem> details) {
        this.context = context;
        this.details = details;
    }

    @Override
    public int getCount() {
        return details != null ? details.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        if (details == null) {
            return null;
        }
        return details.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label;
        TextView value;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_dashboard_detail_wifi, parent, false);
        }

        label = (TextView) convertView.findViewById(R.id.adapter_dashboard_detail_label);
        value = (TextView) convertView.findViewById(R.id.adapter_dashboard_detail_value);

        label.setText(details.get(position).getName());
        value.setText(details.get(position).getValue());

        return convertView;
    }
}

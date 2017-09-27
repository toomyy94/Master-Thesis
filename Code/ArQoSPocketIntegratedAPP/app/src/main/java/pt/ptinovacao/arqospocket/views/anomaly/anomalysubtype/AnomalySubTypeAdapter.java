package pt.ptinovacao.arqospocket.views.anomaly.anomalysubtype;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.views.anomaly.data.AnomalyTypeModel;

/**
 * Created by pedro on 11/05/2017.
 */
public class AnomalySubTypeAdapter extends RecyclerView.Adapter<AnomalySubTypeAdapter.ViewHolder> {

    private final static Logger LOGGER = LoggerFactory.getLogger(AnomalySubTypeAdapter.class);

    private AnomalyTypeModel[] anomalyTypeModels;

    private AnomalySubTypeFragment.OnAnomalySubTypeSelectedListener listener;

    public AnomalySubTypeAdapter(AnomalyTypeModel[] anomalyReports,
            AnomalySubTypeFragment.OnAnomalySubTypeSelectedListener listener) {
        this.anomalyTypeModels = anomalyReports;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_anormaly_subtype_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvId.setText(String.valueOf(anomalyTypeModels[position].getId()));
        holder.tvName.setText(anomalyTypeModels[position].getValue());

        editBackgroundColor(holder, anomalyTypeModels[position]);

        holder.llRecyclerViewRow.setTag(position);

        holder.llRecyclerViewRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAnomalySubTypeSelected(anomalyTypeModels[(int) v.getTag()].getId());
                notifyDataSetChanged();
                listener.nextTabSelected();
            }
        });
    }

    private void editBackgroundColor(ViewHolder holder, AnomalyTypeModel anomalyTypeModel) {
        if (listener.getAnomalySubTypeSelected() == anomalyTypeModel.getId()) {
            holder.llRecyclerViewRow.setBackgroundResource(R.drawable.list_view_abtanomalias_selected);
        } else {
            holder.llRecyclerViewRow.setBackgroundResource(R.drawable.list_view_abtanomalias);
        }
    }

    @Override
    public int getItemCount() {
        if (anomalyTypeModels == null) {
            return 0;
        } else {
            return anomalyTypeModels.length;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvId;

        TextView tvName;

        LinearLayout llRecyclerViewRow;

        public ViewHolder(View view) {
            super(view.getRootView());

            tvId = (TextView) view.findViewById(R.id.numlistViewTip);
            tvName = (TextView) view.findViewById(R.id.textlistViewTip);
            llRecyclerViewRow = (LinearLayout) view.findViewById(R.id.ll_recycler_view_row);
        }
    }
}

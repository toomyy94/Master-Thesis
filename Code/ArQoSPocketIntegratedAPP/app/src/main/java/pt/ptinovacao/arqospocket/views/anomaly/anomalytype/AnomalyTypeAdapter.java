package pt.ptinovacao.arqospocket.views.anomaly.anomalytype;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.views.anomaly.data.AnomalyReport;

/**
 * Created by pedro on 11/05/2017.
 */
public class AnomalyTypeAdapter extends RecyclerView.Adapter<AnomalyTypeAdapter.ViewHolder> {

    private final static Logger LOGGER = LoggerFactory.getLogger(AnomalyTypeAdapter.class);

    private AnomalyReport[] anomalyReports;

    private AnomalyTypeFragment.OnAnomalyTypeSelectedListener listener;

    public AnomalyTypeAdapter(AnomalyReport[] anomalyReports,
            AnomalyTypeFragment.OnAnomalyTypeSelectedListener listener) {
        this.anomalyReports = anomalyReports;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_anormaly_type_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.ivIcon.setBackgroundResource(anomalyReports[position].getResourceIcon());
        holder.tvAnomalyTitle.setText(anomalyReports[position].getResourceText());

        editBackgroundColor(holder, anomalyReports[position]);

        holder.llRecyclerViewRow.setTag(position);

        holder.llRecyclerViewRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAnomalyTypeSelected(anomalyReports[(int) v.getTag()]);
                listener.nextTabSelected();
                notifyDataSetChanged();
            }
        });
    }

    private void editBackgroundColor(ViewHolder holder, AnomalyReport anomalyReport) {
        if (listener.getAnomalyTypeSelected().equals(anomalyReport)) {
            holder.llRecyclerViewRow.setBackgroundResource(R.drawable.list_view_abtanomalias_selected);
        } else {
            holder.llRecyclerViewRow.setBackgroundResource(R.drawable.list_view_abtanomalias);
        }
    }

    @Override
    public int getItemCount() {
        return anomalyReports.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivIcon;

        TextView tvAnomalyTitle;

        LinearLayout llRecyclerViewRow;

        public ViewHolder(View view) {
            super(view.getRootView());

            ivIcon = (ImageView) view.findViewById(R.id.imagelistView);
            tvAnomalyTitle = (TextView) view.findViewById(R.id.textlistView);
            llRecyclerViewRow = (LinearLayout) view.findViewById(R.id.ll_recycler_view_row);
        }
    }
}

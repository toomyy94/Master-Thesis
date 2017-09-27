package pt.ptinovacao.arqospocket.views.anomalyhistoric;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import pt.ptinovacao.arqospocket.ArQoSBaseActivity;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.persistence.models.Anomaly;
import pt.ptinovacao.arqospocket.views.AddSwipeInRecyclerView;
import pt.ptinovacao.arqospocket.views.anomaly.data.AnomalyReport;
import pt.ptinovacao.arqospocket.views.anomalyhistoric.anomalydetails.AnomalyDetailsActivity;

/**
 * AnomalyHistoricAdapter
 * <p>
 * Created by pedro on 17/04/2017.
 */
public class AnomalyHistoricAdapter extends RecyclerView.Adapter<AnomalyHistoricAdapter.ViewHolder>
        implements AddSwipeInRecyclerView.OnClickOfSwipe {

    private final static Logger LOGGER = LoggerFactory.getLogger(AnomalyHistoricAdapter.class);

    private ArrayList<Anomaly> anomalyArrayList;

    private ArQoSBaseActivity activity;

    final private AddSwipeInRecyclerView recyclerViewSwipeUtils = new AddSwipeInRecyclerView();

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        recyclerViewSwipeUtils.setUpItemTouchHelper(recyclerView, this);
        recyclerViewSwipeUtils.setUpAnimationDecoratorHelper(recyclerView);
    }

    AnomalyHistoricAdapter(ArQoSBaseActivity activity, ArrayList<Anomaly> myDataset) {
        this.activity = activity;
        this.anomalyArrayList = myDataset;
    }

    @Override
    public AnomalyHistoricAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_anomalias, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Anomaly anomaly = anomalyArrayList.get(position);

        holder.imgType.setImageResource(AnomalyReport.geImageOfText(activity, anomaly.getLogoId()));
        holder.type.setText(anomaly.getReportType());
        holder.date.setText(anomaly.getReportDate().toString());

        holder.cardView.setTag(position);
        holder.cardView.setOnClickListener(new AnomalyHistoricAdapter.OnClickListenerToHistoric());

    }

    private class OnClickListenerToHistoric implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            AnomalyHistoricAdapter.this.onClick(((Integer) v.getTag()));
        }
    }

    @Override
    public void onClick(Integer position) {

        if (position == null) {
            return;
        }

        LOGGER.debug(anomalyArrayList.get(position).getId() + "");

        Intent intent = new Intent(activity, AnomalyDetailsActivity.class);
        intent.putExtra(AnomalyDetailsActivity.KEY_ANOMALY_TO_DETAILS, anomalyArrayList.get(position).getId());
        notifyDataSetChanged();

        activity.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return anomalyArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgType;

        TextView type;

        TextView date;

        FrameLayout cardView;

        public ViewHolder(View view) {
            super(view.getRootView());

            imgType = (ImageView) view.findViewById(R.id.img_historico_anomalias_type);
            type = (TextView) view.findViewById(R.id.tv_historico_anomalias_type);
            date = (TextView) view.findViewById(R.id.tv_historico_anomalias_date);
            cardView = (FrameLayout) view.findViewById(R.id.card_view_id);
        }
    }

}
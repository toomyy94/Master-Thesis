package pt.ptinovacao.arqospocket.views.radiologshistoric;

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
import pt.ptinovacao.arqospocket.core.utils.DateUtils;
import pt.ptinovacao.arqospocket.persistence.models.Radiolog;
import pt.ptinovacao.arqospocket.views.AddSwipeInRecyclerView;
import pt.ptinovacao.arqospocket.views.radiologs.data.RadiologReport;
import pt.ptinovacao.arqospocket.views.radiologshistoric.radiologsdetails.RadiologsDetailsActivity;

/**
 * RadiologsHistoricAdapter
 * <p>
 * Created by Tomás Rodrigues on 17/04/2017.
 */
public class RadiologsHistoricAdapter extends RecyclerView.Adapter<RadiologsHistoricAdapter.ViewHolder>
        implements AddSwipeInRecyclerView.OnClickOfSwipe {

    private final static Logger LOGGER = LoggerFactory.getLogger(RadiologsHistoricAdapter.class);

    private ArrayList<Radiolog> radiologArrayList;

    private ArQoSBaseActivity activity;

    final private AddSwipeInRecyclerView recyclerViewSwipeUtils = new AddSwipeInRecyclerView();

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        recyclerViewSwipeUtils.setUpItemTouchHelper(recyclerView, this);
        recyclerViewSwipeUtils.setUpAnimationDecoratorHelper(recyclerView);
    }

    RadiologsHistoricAdapter(ArQoSBaseActivity activity, ArrayList<Radiolog> myDataset) {
        this.activity = activity;
        this.radiologArrayList = myDataset;
    }

    @Override
    public RadiologsHistoricAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_anomalias, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Radiolog radiolog = radiologArrayList.get(position);

        holder.imgType.setImageResource(RadiologReport.geImageOfText(activity, radiolog.getReportType()));
        holder.type.setText(radiolog.getReportType());
        holder.date.setText(DateUtils.convertDateToString(radiolog.getReportDate()));

        holder.cardView.setTag(position);
        holder.cardView.setOnClickListener(new RadiologsHistoricAdapter.OnClickListenerToHistoric());

    }

    private class OnClickListenerToHistoric implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            RadiologsHistoricAdapter.this.onClick(((Integer) v.getTag()));
        }
    }

    @Override
    public void onClick(Integer position) {

        if (position == null) {
            return;
        }

        LOGGER.debug(radiologArrayList.get(position).getId() + "");

        Intent intent = new Intent(activity, RadiologsDetailsActivity.class);
        intent.putExtra(RadiologsDetailsActivity.KEY_RADIOLOG_TO_DETAILS, radiologArrayList.get(position).getId());
        notifyDataSetChanged();

        activity.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return radiologArrayList.size();
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
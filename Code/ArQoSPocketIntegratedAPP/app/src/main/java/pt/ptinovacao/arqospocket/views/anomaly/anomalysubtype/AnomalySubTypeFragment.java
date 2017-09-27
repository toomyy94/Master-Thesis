package pt.ptinovacao.arqospocket.views.anomaly.anomalysubtype;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.ArQoSBaseFragment;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.views.anomaly.AnomalyActivity;
import pt.ptinovacao.arqospocket.views.anomaly.anomalytype.AnomalyTypeFragment;
import pt.ptinovacao.arqospocket.views.anomaly.data.AnomalyReport;
import pt.ptinovacao.arqospocket.views.anomaly.data.AnomalyTopics;
import pt.ptinovacao.arqospocket.views.anomaly.data.AnomalyTypeModel;

public class AnomalySubTypeFragment extends ArQoSBaseFragment {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnomalySubTypeFragment.class);

    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    private OnAnomalySubTypeSelectedListener listener;

    private AnomalyTypeModel[] anomalyTypeModels;

    private ImageView ivTitle;

    private TextView tvTitle;

    private final AnomalyTopics anomalyTopics = new AnomalyTopics();

    private AnomalyReport anomalyTypeSelected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_anomaly_sub_type, container, false);

        initializeViews(rootView);
        createRecyclerView();
        listeners();

        return rootView;
    }

    private void initializeViews(ViewGroup rootView) {

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        ivTitle = (ImageView) rootView.findViewById(R.id.iv_title);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
    }

    private void createRecyclerView() {
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        getValueCurrentAnomaly();

        mAdapter = new AnomalySubTypeAdapter(anomalyTypeModels, listener);
        mRecyclerView.setAdapter(mAdapter);

        AnomalyTypeFragment.addDividerInRecyclerView(getContext(), mRecyclerView);
    }

    private void listeners() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser == true) {
            getValueCurrentAnomaly();
        }
    }

    private void getValueCurrentAnomaly() {

        anomalyTypeSelected = ((AnomalyActivity) getActivity()).getAnomalyTypeSelected();
        anomalyTypeModels = anomalyTopics.getTypeHashMap().get(anomalyTypeSelected);

        mAdapter = new AnomalySubTypeAdapter(anomalyTypeModels, listener);
        mRecyclerView.setAdapter(mAdapter);

        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }

        setValues();
    }

    private void setValues() {
        ivTitle.setBackgroundResource(anomalyTypeSelected.getResourceIcon());
        tvTitle.setText(anomalyTypeSelected.getResourceText());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAnomalySubTypeSelectedListener) {
            listener = (OnAnomalySubTypeSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnAnomalySubTypeSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnAnomalySubTypeSelectedListener {

        void onAnomalySubTypeSelected(int subType);

        int getAnomalySubTypeSelected();

        void nextTabSelected();
    }
}

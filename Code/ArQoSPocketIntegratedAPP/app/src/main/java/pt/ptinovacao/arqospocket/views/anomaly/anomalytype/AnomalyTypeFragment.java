package pt.ptinovacao.arqospocket.views.anomaly.anomalytype;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.ArQoSBaseFragment;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.views.anomaly.data.AnomalyReport;

public class AnomalyTypeFragment extends ArQoSBaseFragment {

    private final static Logger LOGGER = LoggerFactory.getLogger(AnomalyTypeFragment.class);

    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    private OnAnomalyTypeSelectedListener listener;

    private final AnomalyReport[] anomalyReports = AnomalyReport.getListTypeAnomaly();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_anomaly_type, container, false);

        initializeView(rootView);
        createRecyclerView();
        initializeValues();

        return rootView;
    }

    private void initializeView(ViewGroup rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
    }

    private void createRecyclerView() {

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new AnomalyTypeAdapter(anomalyReports, listener);
        mRecyclerView.setAdapter(mAdapter);

        addDividerInRecyclerView(getContext(), mRecyclerView);
    }

    public static void addDividerInRecyclerView(Context context, RecyclerView mRecyclerView) {
        DividerItemDecoration divider =
                new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(context, R.mipmap.linha_separador_open));
        mRecyclerView.addItemDecoration(divider);
    }

    private void initializeValues() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAnomalyTypeSelectedListener) {
            listener = (OnAnomalyTypeSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnAnomalySubTypeSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnAnomalyTypeSelectedListener{

        void onAnomalyTypeSelected(AnomalyReport type);

        AnomalyReport getAnomalyTypeSelected();

        void nextTabSelected();
    }
}

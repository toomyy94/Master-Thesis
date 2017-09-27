package pt.ptinovacao.arqospocket.views.radiologshistoric;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import pt.ptinovacao.arqospocket.ArQoSBaseActivity;
import pt.ptinovacao.arqospocket.ArQoSBaseFragment;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.persistence.models.Radiolog;
import pt.ptinovacao.arqospocket.views.radiologs.data.RadiologReport;

/**
 * RadiologsHistoricListFragment.
 * <p>
 * Created by Tomás Rodrigues on 12/04/2017.
 */
public class RadiologsHistoricListFragment extends ArQoSBaseFragment {

    private final static Logger LOGGER = LoggerFactory.getLogger(RadiologsHistoricListFragment.class);

    private RecyclerView recyclerView;

    private RadiologsHistoricAdapter adapter;

    private Spinner spinnerFilter;

    private ArrayList<Radiolog> testDataHistoricArrayList;

    private RadiologReport radiologReport = RadiologReport.ALL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list_historic_tests, container, false);

        initializeViews(rootView);
        createSpinner();
        createRecyclerView();

        loadValues();

        return rootView;
    }

    private void initializeViews(ViewGroup rootView) {

        spinnerFilter = (Spinner) rootView.findViewById(R.id.spinner_filter);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
    }

    private void createSpinner() {

        SpinnerFilterRadiologsUtils.createSpinner(getContext(), spinnerFilter, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                radiologReport = (position== 0? RadiologReport.ALL : RadiologReport.getListTypeRadiolog()[position-1]);
                loadValues();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void createRecyclerView() {
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        testDataHistoricArrayList = new ArrayList<>();

        adapter = new RadiologsHistoricAdapter((ArQoSBaseActivity) getActivity(), testDataHistoricArrayList);
        recyclerView.setAdapter(adapter);
    }

    private void loadValues() {

        testDataHistoricArrayList.clear();
        adapter.notifyDataSetChanged();

        testDataHistoricArrayList.addAll(getArQosApplication().getDatabaseHelper().createRadiologDao()
                .readRadiologsByType(RadiologReport.ALL == radiologReport ? null :
                        getContext().getResources().getString(radiologReport.getResourceText())));
    }

}
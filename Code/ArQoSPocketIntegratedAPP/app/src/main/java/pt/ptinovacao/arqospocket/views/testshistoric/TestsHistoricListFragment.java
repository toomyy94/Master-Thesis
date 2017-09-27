package pt.ptinovacao.arqospocket.views.testshistoric;

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
import java.util.Arrays;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import pt.ptinovacao.arqospocket.ArQoSBaseActivity;
import pt.ptinovacao.arqospocket.ArQoSBaseFragment;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.network.ConnectionTechnology;
import pt.ptinovacao.arqospocket.core.notify.TaskProgress;
import pt.ptinovacao.arqospocket.core.notify.TestNotificationManager;
import pt.ptinovacao.arqospocket.core.notify.TestProgress;
import pt.ptinovacao.arqospocket.core.notify.TestProgressNotifier;
import pt.ptinovacao.arqospocket.persistence.models.ExecutingEvent;

/**
 * AnomalyHistoricListFragment.
 * <p>
 * Created by pedro on 12/04/2017.
 */
public class TestsHistoricListFragment extends ArQoSBaseFragment {

    private final static Logger LOGGER = LoggerFactory.getLogger(TestsHistoricListFragment.class);

    private RecyclerView recyclerView;

    private TestsHistoricAdapter adapter;

    private Spinner spinnerFilter;

    private ArrayList<ExecutingEvent> testDataHistoricArrayList;

    private ConnectionTechnology currentConnectionTechnology = ConnectionTechnology.NA;

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

        ArrayList<SpinnerRowItem> dialogItems = new ArrayList<>();
        List<String> stringOfRows = Arrays.asList(getResources().getStringArray(R.array.testes));
        SpinnerFilterTestsUtils.createSpinner(getContext(), spinnerFilter, stringOfRows, dialogItems,
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        currentConnectionTechnology = SpinnerFilterTestsUtils.parsePosition(position);
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

        adapter = new TestsHistoricAdapter((ArQoSBaseActivity) getActivity(), testDataHistoricArrayList);
        recyclerView.setAdapter(adapter);
    }

    private void loadValues() {

        testDataHistoricArrayList.clear();
        adapter.notifyDataSetChanged();

        getArQosApplication().getDatabaseHelper().createExecutingEventDao()
                .readAllExecutedEventsByConnectionTechnology(currentConnectionTechnology.getValue())
                .subscribe(new Consumer<ExecutingEvent>() {
                    @Override
                    public void accept(@NonNull ExecutingEvent executingEvent) throws Exception {
                        testDataHistoricArrayList.add(executingEvent);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LOGGER.error("Error: ", throwable);
                    }
                });
    }

    private TestProgressNotifier testProgressNotifier = new TestProgressNotifier() {
        @Override
        public void onTestExecutionStarted(TestProgress testProgress) {
        }

        @Override
        public void onTaskExecutionStarted(TaskProgress taskProgress) {
        }

        @Override
        public void onTaskExecutionFinished(TaskProgress taskProgress) {
            loadValues();
        }

        @Override
        public void onTestExecutionFinished(TestProgress testProgress) {
            loadValues();
        }

        @Override
        public void onTestDataChanged(TestProgress testProgress) {
            loadValues();
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        TestNotificationManager.unregister(getContext(), testProgressNotifier);
    }

    @Override
    public void onResume() {
        super.onResume();
        TestNotificationManager.register(getContext(), testProgressNotifier);
    }

}
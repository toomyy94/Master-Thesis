package pt.ptinovacao.arqospocket.views.anomalyhistoric;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.ArrayList;

import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.views.anomaly.data.AnomalyReport;
import pt.ptinovacao.arqospocket.views.testshistoric.SpinnerFilterAdapter;
import pt.ptinovacao.arqospocket.views.testshistoric.SpinnerRowItem;

/**
 * Created by pedro on 20/04/2017.
 */

public class SpinnerFilterAnomalyUtils {

    public static void createSpinner(Context context, Spinner spinner,
            AdapterView.OnItemSelectedListener onItemSelectedListener) {
        ArrayList<SpinnerRowItem> dialogItems = new ArrayList<>();

        for (AnomalyReport anomalyReport : AnomalyReport.values()) {
            dialogItems.add(new SpinnerRowItem(context.getResources().getString(anomalyReport.getResourceText()),
                    anomalyReport.getResourceIcon()));
        }

        SpinnerFilterAdapter spinnerAdapter = new SpinnerFilterAdapter(context, dialogItems);
        spinnerAdapter.setDropDownViewResource(R.layout.rowdrop);

        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(onItemSelectedListener);
    }
}

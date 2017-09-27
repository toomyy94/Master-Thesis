package pt.ptinovacao.arqospocket.views.radiologshistoric;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.ArrayList;

import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.views.radiologs.data.RadiologReport;
import pt.ptinovacao.arqospocket.views.testshistoric.SpinnerFilterAdapter;
import pt.ptinovacao.arqospocket.views.testshistoric.SpinnerRowItem;

/**
 * Created by Tomás Rodrigues on 20/04/2017.
 */

public class SpinnerFilterRadiologsUtils {

    public static void createSpinner(Context context, Spinner spinner,
            AdapterView.OnItemSelectedListener onItemSelectedListener) {
        ArrayList<SpinnerRowItem> dialogItems = new ArrayList<>();

        for (RadiologReport radiologReport : RadiologReport.values()) {
            dialogItems.add(new SpinnerRowItem(context.getResources().getString(radiologReport.getResourceText()),
                    radiologReport.getResourceIcon()));
        }

        SpinnerFilterAdapter spinnerAdapter = new SpinnerFilterAdapter(context, dialogItems);
        spinnerAdapter.setDropDownViewResource(R.layout.rowdrop);

        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(onItemSelectedListener);
    }
}

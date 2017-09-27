package pt.ptinovacao.arqospocket.views.testshistoric;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.network.ConnectionTechnology;

/**
 * Created by pedro on 20/04/2017.
 */

public class SpinnerFilterTestsUtils {

    private static final int TESTS_ICONS[] = {R.drawable.todos_testes, R.drawable.rede_movel, R.drawable.wifi, R.drawable.misto};

    public static void createSpinner(Context context, Spinner spinner, List<String> stringOfRows, ArrayList<SpinnerRowItem> dialogItems, AdapterView.OnItemSelectedListener onItemSelectedListener) {

        if (dialogItems != null && dialogItems.size() == 0) {
            for (int i = 0; i < stringOfRows.size(); i++) {
                dialogItems.add(new SpinnerRowItem(stringOfRows.get(i), TESTS_ICONS[i]));
            }
        }

        SpinnerFilterAdapter spinnerAdapter = new SpinnerFilterAdapter(context, dialogItems);
        spinnerAdapter.setDropDownViewResource(R.layout.rowdrop);

        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(onItemSelectedListener);
    }

    public static ConnectionTechnology parsePosition(int position) {
        switch (position) {
            case 1:
                return ConnectionTechnology.MOBILE;
            case 2:
                return ConnectionTechnology.WIFI;
            case 3:
                return ConnectionTechnology.WIFI;
            default:
            case 0:
                return ConnectionTechnology.NA;
        }
    }
}

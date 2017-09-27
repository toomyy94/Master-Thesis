package pt.ptinovacao.arqospocket.views.tests.testsdetails;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.R;

/**
 * Created by pedro on 17/04/2017.
 */
public class AlertTestsDialog extends Dialog {

    private final static Logger LOGGER = LoggerFactory.getLogger(AlertTestsDialog.class);

    private Button bOK;

    public AlertTestsDialog(Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_tests_alert);

        initializeViews();
        listeners();
    }

    private void initializeViews() {
        bOK = (Button) findViewById(R.id.b_ok);
    }

    private void listeners() {
        bOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertTestsDialog.this.dismiss();
            }
        });
    }
}

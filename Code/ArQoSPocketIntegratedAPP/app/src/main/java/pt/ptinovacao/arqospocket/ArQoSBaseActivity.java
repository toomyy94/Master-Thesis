package pt.ptinovacao.arqospocket;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by pedro on 12/04/2017.
 */
public class ArQoSBaseActivity extends AppCompatActivity {

    private final static Logger LOGGER = LoggerFactory.getLogger(ArQoSBaseActivity.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setWindows(this);
    }

    public static void setWindows(AppCompatActivity appCompatActivity) {

        //hide status bar
        appCompatActivity.getWindow()
                .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //hide action bar
        appCompatActivity.getSupportActionBar().hide();
    }

    public ArQosApplication getArQosApplication() {
        return (ArQosApplication) getApplication();
    }
}
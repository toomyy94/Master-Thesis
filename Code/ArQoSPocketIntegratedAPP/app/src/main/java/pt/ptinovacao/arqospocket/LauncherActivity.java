package pt.ptinovacao.arqospocket;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import pt.ptinovacao.arqospocket.menu.MainFragmentsActivity;
import pt.ptinovacao.arqospocket.menu.MenuPreferenceHomepageUtils;

/**
 * Launcher activity.
 * <p>
 * Created by pedro on 14/04/2017.
 */
public class LauncherActivity extends AppCompatActivity {

    private static final int SECONDS_DELAYED = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        ArQoSBaseActivity.setWindows(this);

        new Handler().postDelayed(new Runnable() {
            public void run() {

                Class homepageClass = MenuPreferenceHomepageUtils.getHomepage((ArQosApplication) getApplication());
                if (!this.getClass().equals(homepageClass)) {
                    MainFragmentsActivity.startActivity(LauncherActivity.this, homepageClass);
                }
            }
        }, SECONDS_DELAYED * 1000);
    }
}

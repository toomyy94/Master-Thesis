package pt.ptinovacao.arqospocket.core.backoff;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.http.client.HttpClient;
import pt.ptinovacao.arqospocket.core.http.client.response.ProbeNotificationResponse;
import pt.ptinovacao.arqospocket.core.settings.SharedPreferencesManager;

/**
 * Created by pedro on 17/05/2017.
 */

public class AlarmBackOffReceiver extends BroadcastReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackOffManager.class);

    @Override
    public void onReceive(final Context context, Intent intent) {

        LOGGER.debug("AlarmBackOffReceiver :: " + intent.getAction());

        Single.just("").subscribeOn(Schedulers.newThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                CoreApplication applicationContext = ((CoreApplication) context.getApplicationContext());
                SharedPreferencesManager sharedPreferencesManager =
                        SharedPreferencesManager.getInstance(applicationContext);
                BackOffManager backOffManager = BackOffManager.getInstance(applicationContext);

                HttpClient client = new HttpClient(applicationContext);
                ArrayList<String> lastIp = sharedPreferencesManager.getLastIp();
                ProbeNotificationResponse probeNotificationResponse =
                        client.postProbeNotificationIpUpdate(lastIp.get(lastIp.size() - 2));

                if (probeNotificationResponse.isSuccess()) {
                    sharedPreferencesManager.resetIp();
                    backOffManager.setRunning(false);
                    backOffManager.setTimeDelay(BackOffManager.MIN_BACK_OFF);
                } else {
                    backOffManager.createAlarm();
                }
            }
        });

    }
}
package pt.ptinovacao.arqospocket.core.keepalive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.ResultsManager;
import pt.ptinovacao.arqospocket.core.alarms.AlarmsManager;
import pt.ptinovacao.arqospocket.core.backoff.BackOffManager;
import pt.ptinovacao.arqospocket.core.cdrs.CDRsManager;
import pt.ptinovacao.arqospocket.core.http.client.HttpClient;
import pt.ptinovacao.arqospocket.core.http.client.response.ProbeNotificationResponse;
import pt.ptinovacao.arqospocket.core.settings.SharedPreferencesManager;

/**
 * Created by pedro on 17/05/2017.
 */
public class AlarmKeepAliveAndAutoDiscoverReceive extends BroadcastReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmKeepAliveAndAutoDiscoverReceive.class);

    @Override
    public void onReceive(Context context, Intent intent) {

        LOGGER.debug("AlarmKeepAliveAndAutoDiscoverReceive :: " + intent.getAction());

        reportKeepAliveOrAutoDiscover(context, intent);
    }

    private void reportKeepAliveOrAutoDiscover(final Context context, final Intent intent) {
        Single.just("").subscribeOn(Schedulers.newThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                final CoreApplication applicationContext = ((CoreApplication) context.getApplicationContext());
                HttpClient client = new HttpClient(applicationContext);

                if (intent.getAction().equals(KeepAliveManager.ACTION_EXECUTE_AUTO_DISCOVERY)) {
                    sendToServer(applicationContext, client.postProbeNotificationAutoDiscovery());
                } else if (intent.getAction().equals(KeepAliveManager.ACTION_EXECUTE_KEEP_ALIVE)) {
                    sendToServer(applicationContext,
                            client.postProbeNotificationKeepAlive(KeepAliveManager.TIME_INTERVAL_KEEP_ALIVE / 1000));

                    Single.just("").subscribeOn(Schedulers.newThread())
                            .delay(BackOffManager.MIN_BACK_OFF, TimeUnit.SECONDS).subscribe(new Consumer<String>() {
                        @Override
                        public void accept(@NonNull String s) throws Exception {

                            if (SharedPreferencesManager.getInstance(applicationContext).getConnectionWithMSManual()) {
                                ResultsManager.getInstance(applicationContext).deliverPendingResults();
                                AlarmsManager.getInstance(applicationContext).deliverPendingAlarms();
                                CDRsManager.getInstance(applicationContext).deliverPendingCDRs();
                            }
                        }
                    });
                }
            }
        });
    }

    private void sendToServer(CoreApplication applicationContext, ProbeNotificationResponse response) {
        LOGGER.debug("Received response: [{}] {}", response.getCode(), response.getEntity());

        boolean isSuccess = response.isSuccess();

        KeepAliveManager.getInstance(applicationContext).setStateConnection(isSuccess);
        KeepAliveManager.getInstance(applicationContext).setConnectedWithSG(isSuccess);

        KeepAliveManager.getInstance(applicationContext).start();
    }
}
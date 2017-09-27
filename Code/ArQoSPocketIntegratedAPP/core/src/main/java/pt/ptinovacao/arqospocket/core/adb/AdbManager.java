package pt.ptinovacao.arqospocket.core.adb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.core.BuildConfig;
import pt.ptinovacao.arqospocket.core.CoreApplication;

/**
 * Created by Tomás Rodrigues on 26/09/2017.
 */

public class AdbManager {
    private final static Logger LOGGER = LoggerFactory.getLogger(AdbManager.class);

    //Example:
    //am broadcast -a pt.ptinovacao.arqospocket.core.ACTION_EXECUTE_TEST_DISCOVERY --es "cenas" 'ola', "debug" '{"debug": false, "title": "Application update!"}'
    public static final String ACTION_EXECUTE_TEST_DISCOVERY = BuildConfig.APPLICATION_ID + ".ACTION_EXECUTE_TEST_DISCOVERY";

    public static class AdbReceiver extends BroadcastReceiver {
        /**
         * This refers to intent messages sent trought adb.
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            final CoreApplication applicationContext = ((CoreApplication) context.getApplicationContext());

            if (intent.getAction().equals(ACTION_EXECUTE_TEST_DISCOVERY)) {

                Toast.makeText(applicationContext, "Broadcast Received: "+ intent.getStringExtra("debug"), Toast.LENGTH_SHORT).show();
            }

        }
    }
}


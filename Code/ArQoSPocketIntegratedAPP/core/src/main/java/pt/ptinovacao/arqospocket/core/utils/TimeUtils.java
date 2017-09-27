package pt.ptinovacao.arqospocket.core.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by pedro on 25/05/2017.
 */

public class TimeUtils {

    public static boolean isTimeAutomatic(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            //noinspection deprecation
            return android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
        }
    }

}

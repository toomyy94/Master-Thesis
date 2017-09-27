/* *
* Copyright (c) Present Technologies Lda., All Rights Reserved.
* (www.present-technologies.com)
*
* This software is the proprietary information of Present Technologies Lda.
* Use is subject to license terms.
*
* Created by $Author: pedrosimoes $
*/

package pt.ptinovacao.arqospocket.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

/**
 * Created by pedrosimoes on 26/11/2016.
 */

public class RequestPermissionsUtils {

    public static boolean ignoreBatteryOptimizationSettings(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            Intent intent = new Intent();
            String packageName = context.getPackageName();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("package:" + packageName));
                context.startActivity(intent);
                return false;
            }
        }
        return true;
    }
}

package pt.ptinovacao.arqospocket.core.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.core.CoreApplication;

/**
 * Created by pedro on 19/05/2017.
 */
public class BatteryUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatteryUtils.class);

    public static Integer getCapacityMAh(CoreApplication coreApplication) {
        Object mPowerProfile_ = null;

        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile_ =
                    Class.forName(POWER_PROFILE_CLASS).getConstructor(Context.class).newInstance(coreApplication);
        } catch (Exception e) {
            LOGGER.error("Error: ", e);
        }

        try {
            double batteryCapacity =
                    (Double) Class.forName(POWER_PROFILE_CLASS).getMethod("getAveragePower", java.lang.String.class)
                            .invoke(mPowerProfile_, "battery.capacity");

            return (int) batteryCapacity;
        } catch (Exception e) {
            LOGGER.error("Error: ", e);
        }
        return null;
    }

    public static Integer getCurrentCapacityMAh(CoreApplication coreApplication) {

        Integer capacityMAh = getCapacityMAh(coreApplication);
        Integer percentage = getPercentage(coreApplication);

        if (capacityMAh == null || percentage == null) {
            return null;
        }

        return percentage * capacityMAh / 100;

    }

    public static Integer getPercentage(CoreApplication coreApplication) {

        Integer batLevel = null;
        BatteryManager bm = (BatteryManager) coreApplication.getSystemService(coreApplication.BATTERY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        }
        return batLevel;
    }

    public static Integer getCurrentNow(CoreApplication coreApplication) {

        Integer batLevel = null;
        BatteryManager bm = (BatteryManager) coreApplication.getSystemService(coreApplication.BATTERY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);
        }
        return batLevel;
    }

    public static Integer getCapacity(CoreApplication coreApplication) {

        Integer batLevel = null;
        BatteryManager bm = (BatteryManager) coreApplication.getSystemService(coreApplication.BATTERY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        }
        return batLevel;
    }

    /**
     * The returned value is an int representing, for example, 27.5 Degrees Celcius as "275" , so it is accurate to a
     * tenth of a centigrade.
     */
    public static int batteryTemperature(Context context) {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int temp = (intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)) / 10;
        return temp;
    }

    public static int getStateBatteryEnergy(CoreApplication coreApplication) {

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = coreApplication.registerReceiver(null, intentFilter);

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        int statusBattery;

        switch (status) {
            case BatteryManager.BATTERY_STATUS_FULL:
                statusBattery = 1;
                break;
            case BatteryManager.BATTERY_STATUS_CHARGING:
                statusBattery = 2;
                break;
            default:
                statusBattery = 0;
        }

        return statusBattery;
    }

}

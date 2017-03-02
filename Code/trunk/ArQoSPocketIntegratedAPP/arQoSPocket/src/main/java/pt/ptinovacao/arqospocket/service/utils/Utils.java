package pt.ptinovacao.arqospocket.service.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * Created by x00881 on 10-01-2017.
 */

public class Utils {
    private final static String TAG = "Utils";

    public static int parseIntParameter(String param){
        if (param == null || param.isEmpty()){
            return 0;
        }
        try {
            return Integer.parseInt(param);
        } catch (NumberFormatException e) {
            return 0;
        }
    }


    /* Returns MAC address of the given interface name.
	* @param interfaceName eth0, wlan0 or NULL=use first interface
	* @return  mac address or null
	*/
    public static String grabMacAddress(final String interfaceName) {
        Log.d(TAG, "getMACAddress:: interfaceName: " + interfaceName);
        try {
            final List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (final NetworkInterface nif : interfaces) {

                if (interfaceName != null) {
                    if (!nif.getName().equalsIgnoreCase(interfaceName)) {
                        continue;
                    }
                }

                final  byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    if (interfaceName != null) {
                        return null;
                    } else {
                        continue;
                    }
                }

                final StringBuilder buf = new StringBuilder();
                for (byte b : macBytes) {
                    buf.append(String.format("%02X:", b));
                }

                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }

                final String macAddress = buf.toString();

                if (!macAddress.equals("")) {
                    Log.d(TAG, "getMACAddress:: returning mac for interface: " + nif.getName() +  " macAddress: " + macAddress);

                    return macAddress;
                }
            }
        } catch (final Exception ex) {

        }

        Log.d(TAG, "getMACAddress:: interface: " + interfaceName +  " macAddress: null");
        return "";
    }
}

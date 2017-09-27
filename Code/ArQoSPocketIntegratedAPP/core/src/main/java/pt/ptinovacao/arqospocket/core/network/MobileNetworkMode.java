package pt.ptinovacao.arqospocket.core.network;

import android.telephony.TelephonyManager;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * Mobile network modes.
 * <p>
 * Created by Emílio Simões on 20-04-2017.
 */
public enum MobileNetworkMode {
    NONE(TelephonyManager.NETWORK_TYPE_UNKNOWN),
    GPRS(TelephonyManager.NETWORK_TYPE_GPRS),
    EDGE(TelephonyManager.NETWORK_TYPE_EDGE),
    UMTS(TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_CDMA),
    HSPA(TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSPAP,
            TelephonyManager.NETWORK_TYPE_HSUPA, 30),
    LTE(TelephonyManager.NETWORK_TYPE_LTE);

    private final Set<Integer> types;

    private static final MobileNetworkMode[] VALUES = {
            NONE, GPRS, EDGE, UMTS, HSPA, LTE
    };

    MobileNetworkMode(Integer... types) {
        this.types = ImmutableSet.copyOf(types);
    }

    public Set<Integer> getTypes() {
        return types;
    }

    public static MobileNetworkMode fromNetworkType(int type) {
        for (MobileNetworkMode mode : VALUES) {
            if (mode.getTypes().contains(type)) {
                return mode;
            }
        }
        return NONE;
    }
}

package pt.ptinovacao.arqospocket.core.network;

import android.telephony.TelephonyManager;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * Identifies the mobile network state.
 * <p>
 * Created by Emílio Simões on 20-04-2017.
 */
public enum MobileState {
    NONE(TelephonyManager.NETWORK_TYPE_UNKNOWN),
    CONNECTING(TelephonyManager.DATA_CONNECTING),
    CONNECTED(TelephonyManager.DATA_CONNECTED),
    DISCONNECTED(TelephonyManager.DATA_DISCONNECTED, TelephonyManager.DATA_SUSPENDED);

    private final Set<Integer> types;

    private static final MobileState[] VALUES = {
            NONE, CONNECTING, CONNECTED, DISCONNECTED
    };

    MobileState(Integer... types) {
        this.types = ImmutableSet.copyOf(types);
    }

    public Set<Integer> getTypes() {
        return types;
    }

    public static MobileState fromNetworkState(int type) {
        for (MobileState state : VALUES) {
            if (state.getTypes().contains(type)) {
                return state;
            }
        }
        return NONE;
    }
}

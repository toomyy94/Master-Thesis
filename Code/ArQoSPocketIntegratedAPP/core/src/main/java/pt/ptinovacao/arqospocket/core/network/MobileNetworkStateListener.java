package pt.ptinovacao.arqospocket.core.network;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Listener to obtain phone network related data.
 * <p>
 * Created by Emílio Simões on 20-04-2017.
 */
class MobileNetworkStateListener extends PhoneStateListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileNetworkStateListener.class);

    private MobileNetworkManager manager;

    private TelephonyManager telephonyManager;

    MobileNetworkStateListener(MobileNetworkManager manager, TelephonyManager telephonyManager) {
        this.manager = manager;
        this.telephonyManager = telephonyManager;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void onCellInfoChanged(List<CellInfo> cellInfo) {
        if (cellInfo == null) {
            return;
        }

        for (CellInfo cell : cellInfo) {
            if (cell.isRegistered()) {
                notifyCellInfoChanged(cell);
            }
        }
    }

    @Override
    public void onCellLocationChanged(CellLocation location) {
        if (location == null) {
            return;
        }

//        notifyCellLocationChanged(location);
    }

    @Override
    public void onDataConnectionStateChanged(int state, int networkType) {
        manager.onDataConnectionStateChanged(MobileState.fromNetworkState(state),
                MobileNetworkMode.fromNetworkType(networkType));

        LOGGER.debug("MOBILE_CONNECTION Mobile State = {}", MobileState.fromNetworkState(state).toString() + " networkType: " + networkType);
    }

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        if (signalStrength == null) {
            return;
        }

        manager.onSignalStrengthsChanged(signalStrength);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            List<CellInfo> cellInfo = telephonyManager.getAllCellInfo();
            if (cellInfo != null) {
                for (CellInfo cell : cellInfo) {
                    if (cell.isRegistered()) {
                        notifyCellInfoChanged(cell);
                    }
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void notifyCellInfoChanged(CellInfo cellInfo) {
        if (cellInfo instanceof CellInfoGsm) {
            manager.onCellInfoGsmChanged((CellInfoGsm) cellInfo);
        } else if (cellInfo instanceof CellInfoLte) {
            manager.onCellInfoLteChanged((CellInfoLte) cellInfo);
        } else if (cellInfo instanceof CellInfoCdma) {
            manager.onCellInfoCdmaChanged((CellInfoCdma) cellInfo);
        } else if (cellInfo instanceof CellInfoWcdma) {
            manager.onCellInfoWcdmaChanged((CellInfoWcdma) cellInfo);
        }
    }

    private void notifyCellLocationChanged(CellLocation location) {
        if (location instanceof GsmCellLocation) {
            manager.notifyCellLocationGsmChanged((GsmCellLocation) location);
        } else if (location instanceof CdmaCellLocation) {
            manager.notifyCellLocationCdmaChanged((CdmaCellLocation) location);
        }
    }
}

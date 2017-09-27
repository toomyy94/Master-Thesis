package pt.ptinovacao.arqospocket.core.network.data.mobile;

import android.support.annotation.NonNull;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

import pt.ptinovacao.arqospocket.core.network.data.MobileRadioInfo;

/**
 * Default {@link MobileRadioInfo} implementation.
 * <p>
 * Created by Emílio Simões on 20-04-2017.
 */
public class DefaultRadioInfo extends MobileRadioInfo {

    private int connectionMode;

    private int bitErrorRate;

    private int primaryScramblingCode;

    private int rscp;

    public int getConnectionMode() {
        return connectionMode;
    }

    public void setConnectionMode(int connectionMode) {
        this.connectionMode = connectionMode;
    }

    public int getBitErrorRate() {
        return bitErrorRate;
    }

    public void setBitErrorRate(int bitErrorRate) {
        this.bitErrorRate = bitErrorRate;
    }

    public int getPrimaryScramblingCode() {
        return primaryScramblingCode;
    }

    public void setPrimaryScramblingCode(int primaryScramblingCode) {
        this.primaryScramblingCode = primaryScramblingCode;
    }

    public int getRscp() {
        return rscp;
    }

    public void setRscp(int rscp) {
        this.rscp = rscp;
    }

    @Override
    public String format() {
        List<String> values = getPositionalValues();

        return Joiner.on(',').useForNull("").join(values);
    }

    @NonNull
    @Override
    protected List<String> getPositionalValues() {
        List<String> values = new ArrayList<>();
        for (int i = 0; i < RADIO_INFO_FIELD_COUNT; i++) {
            values.add("");
        }

        values.set(0, getCellLocation() + "-" + getCellId());

        if (MobileMode.fromGMobile(getConnectionMode()) != MobileMode.MOBILE_3G) {
            values.set(1, String.valueOf(getMobileSignalStrength()));
        } else {
            values.set(31, String.valueOf(getRscp())); // Received signal code power (rscp).
        }

        values.set(2, ""); // gsm_band
        values.set(3, String.valueOf(getConnectionMode()));
        values.set(4, getNetworkOperatorName());
        values.set(32, ""); // Ec/Io (ecio)
        values.set(33, ""); // Absolute Radio Frequency Channel Number (arfcn)
        values.set(34, ""); // Primary scrambling code (psc)
        values.set(35, ""); // Base station identity code (bsic)
        values.set(36, ""); // Routing Area Code (rac)
        values.set(37, String.valueOf(getBitErrorRate()));
        values.set(40, getSubscriberId());

        return values;
    }

    private enum MobileMode {
        MOBILE_2G(new int[] { 0, 1 }),
        MOBILE_3G(new int[] { 2, 3, 4, 5, 8, 9 }),
        MOBILE_4G(new int[] { 10 }),
        NA(new int[] { -1 });

        private int[] index;

        MobileMode(int[] index) {
            this.index = index;
        }

        public static MobileMode fromGMobile(int type) {
            for (MobileMode mode : values()) {
                for (int i : mode.getIndex()) {
                    if (i == type) {
                        return mode;
                    }
                }
            }
            return NA;
        }

        public int[] getIndex() {
            return index;
        }
    }

}

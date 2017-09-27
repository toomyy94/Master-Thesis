package pt.ptinovacao.arqospocket.core.network.data.mobile;

import android.support.annotation.NonNull;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

import pt.ptinovacao.arqospocket.core.network.data.MobileRadioInfo;

/**
 * A {@link MobileRadioInfo} with LTE specific radio information.
 * <p>
 * Created by Emílio Simões on 20-04-2017.
 */
public class LteRadioInfo extends MobileRadioInfo {

    private int connectionMode;

    private int receivedSignalStrengthIndicator;

    private int referenceSignalReceivedQuality;

    private int referenceSignalReceivedPower;

    private int rssnr;

    private int physicalCellId;

    private int channelQualityIndicator;

    public int getConnectionMode() {
        return connectionMode;
    }

    public void setConnectionMode(int connectionMode) {
        this.connectionMode = connectionMode;
    }

    public int getReceivedSignalStrengthIndicator() {
        return receivedSignalStrengthIndicator;
    }

    public void setReceivedSignalStrengthIndicator(int receivedSignalStrengthIndicator) {
        this.receivedSignalStrengthIndicator = receivedSignalStrengthIndicator;
    }

    public int getReferenceSignalReceivedQuality() {
        return referenceSignalReceivedQuality;
    }

    public void setReferenceSignalReceivedQuality(int referenceSignalReceivedQuality) {
        this.referenceSignalReceivedQuality = referenceSignalReceivedQuality;
    }

    public int getReferenceSignalReceivedPower() {
        return referenceSignalReceivedPower;
    }

    public void setReferenceSignalReceivedPower(int referenceSignalReceivedPower) {
        this.referenceSignalReceivedPower = referenceSignalReceivedPower;
    }

    public int getChannelQualityIndicator() {
        return channelQualityIndicator;
    }

    public void setChannelQualityIndicator(int channelQualityIndicator) {
        this.channelQualityIndicator = channelQualityIndicator;
    }

    public int getRssnr() {
        return rssnr;
    }

    public void setRssnr(int rssnr) {
        this.rssnr = rssnr;
    }

    public int getPhysicalCellId() {
        return physicalCellId;
    }

    public void setPhysicalCellId(int physicalCellId) {
        this.physicalCellId = physicalCellId;
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

        values.set(9, ""); // Network registration status (lte_reg_stat)
        values.set(10, stringValue(getConnectionMode()));
        values.set(11, ""); // Frequency band (lte_band)
        values.set(12, ""); // Channel bandwidth (lte_bw)
        values.set(13, getNetworkOperatorName());
        values.set(14, ""); // E-UTRA Absolute Radio Frequency Channel Number for downlink (lte_txch)
        values.set(15, ""); // E-UTRA Absolute Radio Frequency Channel Number for uplink (lte_rxch)
        values.set(16, "");//stringValue(getReceivedSignalStrengthIndicator()));
        values.set(17, stringValue(getReferenceSignalReceivedPower()));
        values.set(18, stringValue(getReferenceSignalReceivedQuality()));
        values.set(19, "");//stringValue(getChannelQualityIndicator())); // lte_sinr
        values.set(21, getCellLocation() + "-" + getCellId());
        values.set(40, getSubscriberId());
        values.set(41, stringValue(getChannelQualityIndicator()));
        return values;
    }

    private String stringValue(int value) {
        return value == -1 ? "" : String.valueOf(value);
    }
}

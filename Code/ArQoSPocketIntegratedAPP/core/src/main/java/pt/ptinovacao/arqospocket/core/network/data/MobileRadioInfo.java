package pt.ptinovacao.arqospocket.core.network.data;

/**
 * Class that identifies an object that contains device related connectivity information.
 * <p>
 * Created by Emílio Simões on 20-04-2017.
 */
public abstract class MobileRadioInfo extends RadioInfo {

    private int cellLocation;

    private int cellId;

    private int mobileSignalStrength;

    private String networkOperatorName;

    private String subscriberId;

    public int getCellLocation() {
        return cellLocation;
    }

    public void setCellLocation(int cellLocation) {
        this.cellLocation = cellLocation;
    }

    public int getCellId() {
        return cellId;
    }

    public void setCellId(int cellId) {
        this.cellId = cellId;
    }

    public int getMobileSignalStrength() {
        return mobileSignalStrength;
    }

    public void setMobileSignalStrength(int mobileSignalStrength) {
        this.mobileSignalStrength = mobileSignalStrength;
    }

    public String getNetworkOperatorName() {
        return networkOperatorName;
    }

    public void setNetworkOperatorName(String networkOperatorName) {
        this.networkOperatorName = networkOperatorName;
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }
}

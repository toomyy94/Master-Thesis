package pt.ptinovacao.arqospocket.core.network.data.mobile;

import android.telephony.CellInfo;
import android.telephony.NeighboringCellInfo;

import java.util.List;

import pt.ptinovacao.arqospocket.core.network.MobileNetworkMode;
import pt.ptinovacao.arqospocket.core.network.MobileState;
import pt.ptinovacao.arqospocket.core.network.data.BaseNetworkInfo;
import pt.ptinovacao.arqospocket.core.utils.JsonHelper;

public class MobileInfoData extends BaseNetworkInfo {

    private int signalLevel;

    private String idCell;

    private String cellLocation;

    private MobileNetworkMode mobileNetworkMode;

    private int connectionMode;

    private String mcc;

    private String mccMnc;

    private MobileState mobileState;

    private String deviceId;

    private String simIccid;

    private String msisdn;

    private String networkOperatorName;

    private String imsi;

    private boolean roaming;

    private List<NeighboringCellInfo> neighboringCellInfoList;

    private List<CellInfo> allCellInfoList;

    public int getSignalLevel() {
        return signalLevel;
    }

    public void setSignalLevel(int signalLevel) {
        this.signalLevel = signalLevel;
    }

    public String getIdCell() {
        return idCell;
    }

    public void setIdCell(String idCell) {
        this.idCell = idCell;
    }

    public String getCellLocation() {
        return cellLocation;
    }

    public void setCellLocation(String cellLocation) {
        this.cellLocation = cellLocation;
    }

    public MobileNetworkMode getMobileNetworkMode() {
        return mobileNetworkMode;
    }

    public void setMobileNetworkMode(MobileNetworkMode mobileNetworkMode) {
        this.mobileNetworkMode = mobileNetworkMode;
    }

    public int getConnectionMode() {
        return connectionMode;
    }

    public void setConnectionMode(int connectionMode) {
        this.connectionMode = connectionMode;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getMccMnc() {
        return mccMnc;
    }

    public void setMccMnc(String mccMnc) {
        this.mccMnc = mccMnc;
    }

    public MobileState getMobileState() {
        return mobileState;
    }

    public void setMobileState(MobileState mobileState) {
        this.mobileState = mobileState;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSimIccid() {
        return simIccid;
    }

    public void setSimIccid(String simIccid) {
        this.simIccid = simIccid;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getNetworkOperatorName() {
        return networkOperatorName;
    }

    public void setNetworkOperatorName(String networkOperatorName) {
        this.networkOperatorName = networkOperatorName;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public boolean isRoaming() {
        return roaming;
    }

    public void setRoaming(boolean roaming) {
        this.roaming = roaming;
    }

    public List<NeighboringCellInfo> getNeighboringCellInfoList() {
        return neighboringCellInfoList;
    }

    public void setNeighboringCellInfoList(List<NeighboringCellInfo> neighboringCellInfoList) {
        this.neighboringCellInfoList = neighboringCellInfoList;
    }

    public List<CellInfo> getAllCellInfoList() {
        return allCellInfoList;
    }

    public void setAllCellInfoList(List<CellInfo> allCellInfoList) {
        this.allCellInfoList = allCellInfoList;
    }

    @Override
    public String toString() {
        return JsonHelper.getGsonInstance(true).toJson(this);
    }
}

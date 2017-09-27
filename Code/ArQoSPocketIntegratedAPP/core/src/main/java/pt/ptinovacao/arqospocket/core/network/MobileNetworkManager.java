package pt.ptinovacao.arqospocket.core.network;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.alarms.AlarmType;
import pt.ptinovacao.arqospocket.core.alarms.AlarmUtils;
import pt.ptinovacao.arqospocket.core.alarms.AlarmsManager;
import pt.ptinovacao.arqospocket.core.backoff.BackOffManager;
import pt.ptinovacao.arqospocket.core.keepalive.KeepAliveManager;
import pt.ptinovacao.arqospocket.core.network.data.MobileRadioInfo;
import pt.ptinovacao.arqospocket.core.network.data.mobile.DefaultRadioInfo;
import pt.ptinovacao.arqospocket.core.network.data.mobile.LteRadioInfo;
import pt.ptinovacao.arqospocket.core.network.data.mobile.MobileInfoData;
import pt.ptinovacao.arqospocket.core.radiologs.EEvent;
import pt.ptinovacao.arqospocket.core.radiologs.RadiologsManager;
import pt.ptinovacao.arqospocket.core.ssh.AttachmentsProcessManager;
import pt.ptinovacao.arqospocket.core.ssh.RadiologsAttachmentsProcessManager;
import pt.ptinovacao.arqospocket.core.ssh.ScanlogsAttachmentsProcessManager;
import pt.ptinovacao.arqospocket.core.utils.SystemUtils;

/**
 * Manager to get device network related information.
 * <p>
 * Created by Emílio Simões on 20-04-2017.
 */
public class MobileNetworkManager{

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileNetworkManager.class);

    public final static String ACTION_MOBILE_DATA_CHANGED =
            "pt.ptinovacao.arqospocket.core.network.action.MOBILE_DATA_CHANGED";

    private static final int UNKNOWN = -1;

    private static MobileNetworkManager instance;

    private final TelephonyManager telephonyManager;

    private final ConnectivityManager connectivityManager;

    private RadiologsManager radiologsManager;

    private final CoreApplication application;

    private final MobileNetworkStateListener phoneStateListener;

    private int cellLocation = UNKNOWN;

    private int cellId = UNKNOWN;
    private int prev_cellId = UNKNOWN;

    private boolean roaming = false;
    private boolean prev_roaming = false;

    private String plmn = "NONE";
    private String prev_plmn = "NONE";

    private int mobileSignalStrength = UNKNOWN;

    private MobileState mobileState = MobileState.NONE;

    private MobileNetworkMode mobileNetworkMode = MobileNetworkMode.NONE;

    private DefaultInfo defaultInfo = new DefaultInfo();

    private LteInfo lteInfo = new LteInfo();

    private MobileNetworkManager(CoreApplication application) {
        this.application = application;
        telephonyManager = (TelephonyManager) application.getSystemService(Context.TELEPHONY_SERVICE);
        phoneStateListener = new MobileNetworkStateListener(this, telephonyManager);
        connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * Gets the manager instance.
     *
     * @param application the application object.
     * @return the manager instance.
     */
    public synchronized static MobileNetworkManager getInstance(CoreApplication application) {
        if (instance == null) {
            instance = new MobileNetworkManager(application);
        }
        return instance;
    }

    /**
     * Initializes the manager by registering listeners that watch for network changes.
     */
    public void init() {
        int events = PhoneStateListener.LISTEN_SIGNAL_STRENGTHS | PhoneStateListener.LISTEN_DATA_CONNECTION_STATE |
                PhoneStateListener.LISTEN_CELL_LOCATION | PhoneStateListener.LISTEN_DATA_ACTIVITY;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            events |= PhoneStateListener.LISTEN_CELL_INFO;
        }
        telephonyManager.listen(phoneStateListener, events);
    }

    /**
     * Finalizes the manager un registering the listeners.
     */
    public void close() {
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
    }

    /**
     * Gets the mobile network radio information.
     *
     * @return the mobile network radio manager.
     */
    public MobileRadioInfo getNetworkInfo() {
        if (getConnectionMode() == 10) {
            return getLteRadioInfo();
        }
        return getDefaultRadioInfo();
    }

    /**
     * Gets the mobile network information.
     *
     * @return the mobile network information.
     */
    public MobileInfoData getMobileInfoData() {
        MobileInfoData data = new MobileInfoData();

        String networkOperator = Strings.nullToEmpty(getNetworkOperator());
        data.setDeviceId(getDeviceId());

        data.setMsisdn(getLine1Number());
        data.setNetworkOperatorName(getNetworkOperatorName());
        plmn = getNetworkOperatorName();
        data.setImsi(getSubscriberId());
        data.setRoaming(telephonyManager.isNetworkRoaming());
        roaming=telephonyManager.isNetworkRoaming();
        data.setSimIccid(getSimIccid());

        data.setIdCell(String.valueOf(getCellId() & 0xffff));
        cellId = getCellId();
        data.setCellLocation(String.valueOf(getCellLocation()));
        data.setSignalLevel(getMobileSignalStrength());

        data.setMobileNetworkMode(getMobileNetworkMode());
        data.setMobileState(getMobileState());
        data.setConnectionMode(getConnectionMode());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            data.setAllCellInfoList(telephonyManager.getAllCellInfo());
        }

        if (networkOperator.length() > 0) {
            data.setMcc(networkOperator.substring(0, 3));
            data.setMccMnc(networkOperator.substring(3));
        }

        return data;
    }

    /**
     * Gets if the mobile network is available.
     *
     * @return if the mobile network is available.
     */
    public boolean isMobileAvailable() {
        return isSimReady() && Strings.nullToEmpty(getNetworkOperator()).length() > 0;
    }

    public void restartNetwork() {
        // WARNING : This will NOT work on Android 4.2+ due to being considered an unsafe setting
        // Only system apps can change this setting
        // Also, trying to broadcast the Intent.ACTION_AIRPLANE_MODE_CHANGED will cause an exception for the same reason
        // Only system apps can send this broadcast
//        Settings.System.putInt(application.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
//        Settings.System.putInt(application.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 1);
//
//        if (Build.VERSION.SDK_INT >= 17) {
//            // Option B : User changes setting in system settings :)
//            Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            application.startActivity(intent);
//        }
//      TODO need Root
        try{
            if (!isAirplaneModeOn(application.getApplicationContext())) {
                Process turnAirplaneModeOn = SystemUtils.requestPermission();
                DataOutputStream os = new DataOutputStream(turnAirplaneModeOn.getOutputStream());
                os.writeBytes("settings put global airplane_mode_on 1\n");
                os.writeBytes("am broadcast -a android.intent.action.AIRPLANE_MODE\n");
                os.writeBytes("exit\n");
                os.flush();
                os.close();
                turnAirplaneModeOn.waitFor();

//                Thread.sleep(2000);
            }

            Process turnAirplaneModeOff = SystemUtils.requestPermission();
            DataOutputStream os2 = new DataOutputStream(turnAirplaneModeOff.getOutputStream());
            os2.writeBytes("settings put global airplane_mode_on 0\n");
            os2.writeBytes("am broadcast -a android.intent.action.AIRPLANE_MODE\n");
            os2.writeBytes("exit\n");
            os2.flush();
            os2.close();
//            turnAirplaneModeOff.waitFor();

            AlarmsManager.getInstance(application).generateAlarm(AlarmUtils.INICIO, AlarmType.A050.name(), AlarmType.A050.getAlarmContent());

        } catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Could not reboot module's radio");
        }

    }

    /**
     * Gets the state of Airplane Mode.
     *
     * @param context
     * @return true if enabled.
     */
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isAirplaneModeOn(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    void onCellInfoGsmChanged(CellInfoGsm cellInfo) {
        mobileSignalStrength = cellInfo.getCellSignalStrength().getDbm();
        cellId = cellInfo.getCellIdentity().getCid();
        cellLocation = cellInfo.getCellIdentity().getLac();

        notifyNetworkInformationChanged();
        if(prev_cellId != cellId)  notifyRadiologs(cellId);

//        LOGGER.debug("FREQUENCY GSM {} {}", cellInfo.getCellIdentity(), cellInfo.getCellSignalStrength());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    void onCellInfoCdmaChanged(CellInfoCdma cellInfo) {
        mobileSignalStrength = cellInfo.getCellSignalStrength().getDbm();
        cellId = cellInfo.getCellIdentity().getNetworkId();
        cellLocation = cellInfo.getCellIdentity().getBasestationId();

        notifyNetworkInformationChanged();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    void onCellInfoWcdmaChanged(CellInfoWcdma cellInfo) {
        mobileSignalStrength = cellInfo.getCellSignalStrength().getDbm();
        cellId = cellInfo.getCellIdentity().getCid();
        cellLocation = cellInfo.getCellIdentity().getLac();

        notifyNetworkInformationChanged();
        if(prev_cellId != cellId)  notifyRadiologs(cellId);

//        LOGGER.debug("FREQUENCY WCDMA {} {}", cellInfo.getCellIdentity(), cellInfo.getCellSignalStrength());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    void onCellInfoLteChanged(CellInfoLte cellInfo) {

        CellSignalStrengthLte strength = cellInfo.getCellSignalStrength();
        mobileSignalStrength = strength.getDbm();
        cellId = cellInfo.getCellIdentity().getCi();
        cellLocation = cellInfo.getCellIdentity().getTac();

        extractLteInfo(strength);

        notifyNetworkInformationChanged();
        if(prev_cellId != cellId)  notifyRadiologs(cellId);

//        LOGGER.debug("FREQUENCY LTE {} {}", cellInfo.getCellIdentity(), cellInfo.getCellSignalStrength());
    }

    void notifyCellLocationGsmChanged(GsmCellLocation location) {
        cellId = location.getCid();
        cellLocation = location.getLac();

        notifyNetworkInformationChanged();
    }

    public int getSimState() {

        TelephonyManager telMgr = (TelephonyManager) application.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        switch (simState) {

            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                return 1;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                return 2;
            case TelephonyManager.SIM_STATE_READY:
                return 0;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
            case TelephonyManager.SIM_STATE_ABSENT:
            case TelephonyManager.SIM_STATE_UNKNOWN:
            default:
                return 3;
        }
    }

    void notifyCellLocationCdmaChanged(CdmaCellLocation location) {

        cellId = location.getNetworkId();
        cellLocation = location.getBaseStationId();

        notifyNetworkInformationChanged();

    }

    void onDataConnectionStateChanged(MobileState state, MobileNetworkMode mode) {

        if (state == MobileState.CONNECTED) {
            KeepAliveManager.getInstance(application).start();
            AttachmentsProcessManager.startSendAttachment(application);
            RadiologsAttachmentsProcessManager.startSendAttachment(application);
            ScanlogsAttachmentsProcessManager.startSendAttachment(application);
            BackOffManager.setIpAddress(application);
        } else if (!WifiNetworkManager.getInstance(application).getWifiInfo().getWifiState()
                .equals(String.valueOf(WifiNetworkManager.CONNECTION_STATE_COMPLETED))) {
            KeepAliveManager.getInstance(application).stop();
            AttachmentsProcessManager.getInstance(application).stop();
            RadiologsAttachmentsProcessManager.getInstance(application).stop();
            ScanlogsAttachmentsProcessManager.getInstance(application).stop();
        }

        this.mobileState = state;
        this.mobileNetworkMode = mode;
        notifyNetworkInformationChanged();
    }

    void onSignalStrengthsChanged(SignalStrength signalStrength) {
        defaultInfo.setBitErrorRate(signalStrength.getGsmBitErrorRate());
        defaultInfo.setPrimaryScrambilingCode(getPrimaryScramblingCode());
        mobileSignalStrength = calculateMobileSignalStrength();
        notifyNetworkInformationChanged();
    }

    private void notifyNetworkInformationChanged() {
        Intent intent = new Intent(ACTION_MOBILE_DATA_CHANGED);
        application.sendBroadcast(intent);
    }

    private void notifyRadiologs(Integer cellId) {
        radiologsManager = RadiologsManager.getInstance(application);
//        Radiolog Events

        if(cellId != prev_cellId) {
            if(prev_cellId != UNKNOWN && cellId != UNKNOWN && prev_cellId != 0 && cellId != 0) {
                radiologsManager.generateRadiolog(EEvent.CELL_RESELECTION, "old cellid: "+ prev_cellId + ", new cellid: "+cellId);
            }
            prev_cellId = cellId;
        }

        if(!plmn.equals(prev_plmn)) {
            if(!prev_plmn.equals("NONE")) {
                radiologsManager.generateRadiolog(EEvent.PLMN_CHANGE, "old plmn: "+ prev_plmn + ", new plmn: "+plmn);
            }
            prev_plmn = plmn;
        }

        if(roaming != prev_roaming) {
            radiologsManager.generateRadiolog(EEvent.ROAMING_STATUS, "old roaming status: "+
                    prev_roaming + ", new roaming status: "+roaming);
            prev_roaming = roaming;
        }
    }

    private MobileRadioInfo getDefaultRadioInfo() {
        DefaultRadioInfo radioInfo = new DefaultRadioInfo();
        radioInfo.setCellLocation(getCellLocation());
        radioInfo.setCellId(getCellId());
        radioInfo.setMobileSignalStrength(getMobileSignalStrength());
        radioInfo.setConnectionMode(getConnectionMode());
        radioInfo.setNetworkOperatorName(getNetworkOperatorName());
        radioInfo.setBitErrorRate(getBitErrorRate());
        radioInfo.setSubscriberId(getSubscriberId());
        radioInfo.setRscp(getRscpMobileSignalStrength());
        radioInfo.setPrimaryScramblingCode(getPrimaryScramblingCode());
        return radioInfo;
    }

    private MobileRadioInfo getLteRadioInfo() {
        LteRadioInfo radioInfo = new LteRadioInfo();
        radioInfo.setCellLocation(getCellLocation());
        radioInfo.setCellId(getCellId());
        radioInfo.setMobileSignalStrength(getMobileSignalStrength());
        radioInfo.setNetworkOperatorName(getNetworkOperatorName());
        radioInfo.setSubscriberId(getSubscriberId());
        radioInfo.setReceivedSignalStrengthIndicator(getReceivedSignalStrengthIndicator());
        radioInfo.setReferenceSignalReceivedQuality(getReferenceSignalReceivedQuality());
        radioInfo.setReferenceSignalReceivedPower(getReferenceSignalReceivedPower());
        radioInfo.setRssnr(getRssnr());
        radioInfo.setPhysicalCellId(getPhysicalCellId());
        radioInfo.setChannelQualityIndicator(getChannelQualityIndicator());
        radioInfo.setConnectionMode(getConnectionMode());
        return radioInfo;
    }

    public int getReferenceSignalReceivedPower() {
        if (lteInfo.hasInfo()) {
            return lteInfo.getReferenceSignalReceivedPower();
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            CellSignalStrength strength = getCellSignalStrength();
            if (strength != null) {
                if (strength instanceof CellSignalStrengthLte) {
                    extractLteInfo((CellSignalStrengthLte) strength);
                    return lteInfo.getReferenceSignalReceivedPower();
                }
            }
        }

        return UNKNOWN;
    }

    public int getRssnr() {
        if (lteInfo.hasInfo()) {
            return lteInfo.getRssnr();
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            CellSignalStrength strength = getCellSignalStrength();
            if (strength != null) {
                if (strength instanceof CellSignalStrengthLte) {
                    extractLteInfo((CellSignalStrengthLte) strength);
                    return lteInfo.getRssnr();
                }
            }
        }

        return UNKNOWN;
    }

    public int getChannelQualityIndicator() {
        if (lteInfo.hasInfo()) {
            return lteInfo.getChannelQualityIndicator();
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            CellSignalStrength strength = getCellSignalStrength();
            if (strength != null) {
                if (strength instanceof CellSignalStrengthLte) {
                    extractLteInfo((CellSignalStrengthLte) strength);
                    return lteInfo.getChannelQualityIndicator();
                }
            }
        }

        return UNKNOWN;
    }

    public int getReferenceSignalReceivedQuality() {
        if (lteInfo.hasInfo()) {
            return lteInfo.getReferenceSignalReceivedQuality();
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            CellSignalStrength strength = getCellSignalStrength();
            if (strength != null) {
                if (strength instanceof CellSignalStrengthLte) {
                    extractLteInfo((CellSignalStrengthLte) strength);
                    return lteInfo.getReferenceSignalReceivedQuality();
                }
            }
        }

        return UNKNOWN;
    }

    private int getReceivedSignalStrengthIndicator() {
        if (lteInfo.hasInfo()) {
            return lteInfo.calculateReceivedSignalStrengthIndicator();
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            CellSignalStrength strength = getCellSignalStrength();
            if (strength != null) {
                if (strength instanceof CellSignalStrengthLte) {
                    extractLteInfo((CellSignalStrengthLte) strength);
                    return lteInfo.calculateReceivedSignalStrengthIndicator();
                }
            }
        }

        return UNKNOWN;
    }

    private void extractLteInfo(CellSignalStrengthLte strength) {
        lteInfo.setReferenceSignalReceivedPower(getFieldValue(strength, "mRsrp"));
        lteInfo.setReferenceSignalReceivedQuality(getFieldValue(strength, "mRsrq"));
        lteInfo.setChannelQualityIndicator(getFieldValue(strength, "mCqi"));
        lteInfo.setRssnr(getFieldValue(strength, "mRssnr"));
        lteInfo.setPhysicalCellId(getPhysicalCellId());
    }

    @SuppressLint("HardwareIds")
    private String getSubscriberId() {
        return telephonyManager.getSubscriberId();
    }

    @SuppressLint("HardwareIds")
    private String getLine1Number() {
        return telephonyManager.getLine1Number();
    }

    @SuppressLint("HardwareIds")
    private String getDeviceId() {
        return telephonyManager.getDeviceId();
    }

    @SuppressLint("HardwareIds")
    private String getSimIccid() {
        return telephonyManager.getSimSerialNumber();
    }

    @SuppressWarnings("deprecation")
    private List<NeighboringCellInfo> getNeighboringCellInfo() {
        return telephonyManager.getNeighboringCellInfo();
    }

    private int getMobileSignalStrength() {
        if (mobileSignalStrength != UNKNOWN) {
            return mobileSignalStrength;
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return calculateMobileSignalStrength();
        }

        return UNKNOWN;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int calculateMobileSignalStrength() {
        CellSignalStrength strength = getCellSignalStrength();
        if (strength != null) {
            return strength.getDbm();
        }
        return -1;
    }

    private int getRscpMobileSignalStrength() {
        if (mobileSignalStrength != UNKNOWN) {
            return mobileSignalStrength;
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            CellSignalStrength strength = getRscpCellSignalStrength();
            if (strength != null) {
                return strength.getDbm();
            }
        }

        return UNKNOWN;
    }


    public int getBitErrorRate() {
        if (defaultInfo.getBitErrorRate() != UNKNOWN) {
            return defaultInfo.getBitErrorRate();
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            CellSignalStrength strength = getCellSignalStrength();
            if (strength != null) {
                if (strength instanceof CellSignalStrengthGsm) {
                    return parseBitErrorRate(strength);
                }
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if (strength instanceof CellSignalStrengthWcdma) {
                        return parseBitErrorRate(strength);
                    }
                }
            }
        }

        return UNKNOWN;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private CellSignalStrength getCellSignalStrength() {
        List<CellInfo> allCellInfo = telephonyManager.getAllCellInfo();
        if (allCellInfo != null) {
            for (CellInfo cellInfo : allCellInfo) {
                if (cellInfo.isRegistered()) {
                    if (cellInfo instanceof CellInfoGsm) {
                        return ((CellInfoGsm) cellInfo).getCellSignalStrength();
                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        if (cellInfo instanceof CellInfoWcdma) {
                            return ((CellInfoWcdma) cellInfo).getCellSignalStrength();
                        }
                    }
                    if (cellInfo instanceof CellInfoCdma) {
                        return ((CellInfoCdma) cellInfo).getCellSignalStrength();
                    }
                    if (cellInfo instanceof CellInfoLte) {
                        return ((CellInfoLte) cellInfo).getCellSignalStrength();
                    }
                }
            }
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public int getPrimaryScramblingCode() {
        if (defaultInfo.getPrimaryScramblingCode() != UNKNOWN) {
            return defaultInfo.getPrimaryScramblingCode();
        }
        List<CellInfo> allCellInfo = telephonyManager.getAllCellInfo();
        if (allCellInfo != null) {
            for (CellInfo cellInfo : allCellInfo) {
                if (cellInfo.isRegistered()) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        if (cellInfo instanceof CellInfoWcdma) {
                            return ((CellInfoWcdma) cellInfo).getCellIdentity().getPsc();
                        }
                    }

                }
            }
        }
        return UNKNOWN;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private CellSignalStrength getRscpCellSignalStrength() {
        List<CellInfo> allCellInfo = telephonyManager.getAllCellInfo();
        if (allCellInfo == null) {
            return null;
        }
        for (CellInfo cellInfo : allCellInfo) {
            if (cellInfo.isRegistered()) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if (cellInfo instanceof CellInfoWcdma) {
                        return ((CellInfoWcdma) cellInfo).getCellSignalStrength();
                    }
                }
            }
        }

        return null;
    }

    private int parseBitErrorRate(Object strength) {
        int mBitErrorRate = getFieldValue(strength, "mBitErrorRate");
        if (mBitErrorRate == 0) {
            return UNKNOWN;
        }
        return mBitErrorRate;
    }

    private int getFieldValue(Object strength, String fieldName) {
        try {
            Class<?> aClass = strength.getClass();
            Field field = aClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (int) field.get(strength);
        } catch (Exception e) {
            return UNKNOWN;
        }
    }

    private MobileNetworkMode getMobileNetworkMode() {
        if (mobileNetworkMode == MobileNetworkMode.NONE) {
            return MobileNetworkMode.fromNetworkType(telephonyManager.getNetworkType());
        }
        return mobileNetworkMode;
    }

    private MobileState getMobileState() {
        if (mobileState == MobileState.NONE) {
            return MobileState.fromNetworkState(telephonyManager.getDataState());
        }
        return mobileState;
    }

    public Boolean isDataRoamingEnabled() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return (Settings.Global.getInt(application.getContentResolver(), Settings.Global.DATA_ROAMING, 0) == 1);
            } else {
                //noinspection deprecation
                return (Settings.System.getInt(application.getContentResolver(), Settings.Secure.DATA_ROAMING, 0) == 1);
            }
        } catch (Exception exception) {
            return false;
        }
    }

    public String getCountryCode() {
        return telephonyManager.getNetworkCountryIso();
    }

    private String getNetworkOperatorName() {
        return telephonyManager.getNetworkOperatorName();
    }

    private String getNetworkOperator() {
        return telephonyManager.getNetworkOperator();
    }

    private boolean isSimReady() {
        return telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    public static boolean isSimOnDevice(Context context)
    {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);  //gets the current TelephonyManager
        return !(tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT);
    }

    public int getConnectionMode() {
        int networkType = telephonyManager.getNetworkType();

        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return 0;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return 1;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return 2;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return 3;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return 4;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return 5;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return 7;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return 8;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return 10;
            case 30:
                return 5;
            default:
                return 6;
        }
    }

    public int getCDRsConnectionMode() {
        int networkType = telephonyManager.getNetworkType();

        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return 3;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return 12;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return 8;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return 13;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return 14;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return 15;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return 0;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return 8;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return 23;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return 16;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return 17;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return 18;
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return 22;
            case 30:
                return 8;
            default:
                return 0;
        }
    }

    private int getCellLocation() {
        if (cellLocation != UNKNOWN) {
            return cellLocation;
        }

        CellLocation location = telephonyManager.getCellLocation();
        if (location != null) {
            if (location instanceof CdmaCellLocation) {
                return ((CdmaCellLocation) location).getBaseStationId();
            }

            if (location instanceof GsmCellLocation) {
                return ((GsmCellLocation) location).getLac();
            }
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            List<CellInfo> allCellInfo = telephonyManager.getAllCellInfo();
            for (CellInfo cellInfo : allCellInfo) {
                if (cellInfo.isRegistered() && cellInfo instanceof CellInfoLte) {
                    return ((CellInfoLte) cellInfo).getCellIdentity().getTac();
                }
            }
        }

        return UNKNOWN;
    }

    public int getPhysicalCellId() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            List<CellInfo> allCellInfo = telephonyManager.getAllCellInfo();
            for (CellInfo cellInfo : allCellInfo) {
                if (cellInfo.isRegistered() && cellInfo instanceof CellInfoLte) {
                    return ((CellInfoLte) cellInfo).getCellIdentity().getPci();
                }
            }
        }

        return UNKNOWN;
    }

    private int getCellId() {
        if (cellId != UNKNOWN) {
            return cellId;
        }

        CellLocation location = telephonyManager.getCellLocation();
        if (location != null) {
            if (location instanceof CdmaCellLocation) {
                return ((CdmaCellLocation) location).getNetworkId();
            }
            if (location instanceof GsmCellLocation) {
                return ((GsmCellLocation) location).getCid();
            }
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            List<CellInfo> allCellInfo = telephonyManager.getAllCellInfo();
            for (CellInfo cellInfo : allCellInfo) {
                if (cellInfo.isRegistered() && cellInfo instanceof CellInfoLte) {
                    return ((CellInfoLte) cellInfo).getCellIdentity().getCi();
                }
            }
        }

        return UNKNOWN;
    }

    public ArrayList<Integer> getNeigCellId(List<CellInfo> allCellInfo) {
        ArrayList<Integer> neigCellId = new ArrayList<>();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            for (CellInfo cellInfo : allCellInfo) {
                if (cellInfo instanceof CellInfoLte) {
                    neigCellId.add(((CellInfoLte) cellInfo).getCellIdentity().getCi());
                }
                else if (cellInfo instanceof CellInfoGsm) {
                    neigCellId.add(((CellInfoGsm) cellInfo).getCellIdentity().getCid());
                }
                else if (cellInfo instanceof CellInfoWcdma) {
                    neigCellId.add(((CellInfoWcdma) cellInfo).getCellIdentity().getCid());
                }
            }
        }

        return neigCellId;
    }

    public ArrayList<Integer> getNeigLac(List<CellInfo> allCellInfo) {
        ArrayList<Integer> neigLac = new ArrayList<>();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            for (CellInfo cellInfo : allCellInfo) {
                if (cellInfo instanceof CellInfoWcdma) {
                    neigLac.add(((CellInfoWcdma) cellInfo).getCellIdentity().getLac());
                }
            }
        }

        return neigLac;
    }

    public ArrayList<Integer> getNeigPsc(List<CellInfo> allCellInfo) {
        ArrayList<Integer> neigPsc = new ArrayList<>();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            for (CellInfo cellInfo : allCellInfo) {
                if (cellInfo instanceof CellInfoWcdma) {
                    neigPsc.add(((CellInfoWcdma) cellInfo).getCellIdentity().getPsc());
                }
            }
        }

        return neigPsc;
    }

    public ArrayList<Integer> getNeigPci(List<CellInfo> allCellInfo) {
        ArrayList<Integer> neigPsc = new ArrayList<>();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            for (CellInfo cellInfo : allCellInfo) {
                if (cellInfo instanceof CellInfoLte) {
                    neigPsc.add(((CellInfoLte) cellInfo).getCellIdentity().getPci());
                }
            }
        }

        return neigPsc;
    }

    public ArrayList<Integer> getNeigTac(List<CellInfo> allCellInfo) {
        ArrayList<Integer> neigTac = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            for (CellInfo cellInfo : allCellInfo) {
                if (cellInfo instanceof CellInfoLte) {
                    neigTac.add(((CellInfoLte) cellInfo).getCellIdentity().getTac());
                }
            }
        }

        return neigTac;
    }

    public ArrayList<Integer> getNeigRsrp(List<CellInfo> allCellInfo) {
        ArrayList<Integer> neigRsrp = new ArrayList<>();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            for (CellInfo cellInfo : allCellInfo) {
                if (cellInfo instanceof CellInfoLte) {
                    String ssignal = ((CellInfoLte) cellInfo).getCellSignalStrength().toString();
                    String[] parts = ssignal.split(" ");

                    String[] partsLteRsrp = parts[2].split("=");
                    neigRsrp.add(Integer.parseInt(partsLteRsrp[1]));
                }
            }
        }

        return neigRsrp;
    }

    public ArrayList<Integer> getNeigRsrq(List<CellInfo> allCellInfo) {
        ArrayList<Integer> neigRsrq = new ArrayList<>();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            for (CellInfo cellInfo : allCellInfo) {
                if (cellInfo instanceof CellInfoLte) {
                    String ssignal = ((CellInfoLte) cellInfo).getCellSignalStrength().toString();
                    String[] parts = ssignal.split(" ");

                    String[] partsLteRsrq = parts[3].split("=");
                    neigRsrq.add(Integer.parseInt(partsLteRsrq[1]));
                }
            }
        }

        return neigRsrq;
    }

    public ArrayList<Integer> getNeigRssnr(List<CellInfo> allCellInfo) {
        ArrayList<Integer> neigRssnr = new ArrayList<>();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            for (CellInfo cellInfo : allCellInfo) {
                if (cellInfo instanceof CellInfoLte) {
                    String ssignal = ((CellInfoLte) cellInfo).getCellSignalStrength().toString();
                    String[] parts = ssignal.split(" ");

                    String[] partsLteRssnr = parts[4].split("=");
                    neigRssnr.add(Integer.parseInt(partsLteRssnr[1]));
                }
            }
        }

        return neigRssnr;
    }

    public String getDeviceImei() {
        return getDeviceId();
    }

    public String getDeviceImsi() {
        return getSubscriberId();
    }

    private class DefaultInfo {

        private int bitErrorRate = UNKNOWN;
        private int primaryScramblingCode = UNKNOWN;

        int getBitErrorRate() {
            return bitErrorRate;
        }

        void setBitErrorRate(int bitErrorRate) {
            this.bitErrorRate = bitErrorRate;
        }

        public int getPrimaryScramblingCode() {
            return primaryScramblingCode;
        }

        public void setPrimaryScrambilingCode(int primaryScramblingCode) {
            this.primaryScramblingCode = primaryScramblingCode;
        }
    }

    interface cellIdChanged{
        void callbackCellId();
    }

    cellIdChanged callback;

    void onCellIdChanged(){
        callback.callbackCellId();
    }

    private class LteInfo {

        private int referenceSignalReceivedPower = UNKNOWN;

        private int referenceSignalReceivedQuality = UNKNOWN;

        private int channelQualityIndicator = UNKNOWN;

        private int rssnr = UNKNOWN;

        private int physicalCellId = UNKNOWN;

        int getReferenceSignalReceivedPower() {
            return referenceSignalReceivedPower;
        }

        void setReferenceSignalReceivedPower(int referenceSignalReceivedPower) {
            this.referenceSignalReceivedPower = referenceSignalReceivedPower;
        }

        int getReferenceSignalReceivedQuality() {
            return referenceSignalReceivedQuality;
        }

        void setReferenceSignalReceivedQuality(int referenceSignalReceivedQuality) {
            this.referenceSignalReceivedQuality = referenceSignalReceivedQuality;
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

        int getChannelQualityIndicator() {
            return channelQualityIndicator;
        }

        void setChannelQualityIndicator(int channelQualityIndicator) {
            this.channelQualityIndicator = channelQualityIndicator;
        }

        int calculateReceivedSignalStrengthIndicator() {
            if (hasInfo()) {
                return referenceSignalReceivedPower - referenceSignalReceivedQuality;
            }
            return UNKNOWN;
        }

        boolean hasInfo() {
            return referenceSignalReceivedPower != UNKNOWN && referenceSignalReceivedQuality != UNKNOWN;
        }
    }

}

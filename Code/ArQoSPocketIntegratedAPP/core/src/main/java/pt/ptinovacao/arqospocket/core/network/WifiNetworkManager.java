package pt.ptinovacao.arqospocket.core.network;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.alarms.AlarmType;
import pt.ptinovacao.arqospocket.core.alarms.AlarmUtils;
import pt.ptinovacao.arqospocket.core.alarms.AlarmsManager;
import pt.ptinovacao.arqospocket.core.backoff.BackOffManager;
import pt.ptinovacao.arqospocket.core.keepalive.KeepAliveManager;
import pt.ptinovacao.arqospocket.core.network.data.WifiRadioInfo;
import pt.ptinovacao.arqospocket.core.network.data.wifi.DefaultWifiInfo;
import pt.ptinovacao.arqospocket.core.network.data.wifi.WifiInfoData;
import pt.ptinovacao.arqospocket.core.ssh.AttachmentsProcessManager;
import pt.ptinovacao.arqospocket.core.ssh.RadiologsAttachmentsProcessManager;
import pt.ptinovacao.arqospocket.core.ssh.ScanlogsAttachmentsProcessManager;

/**
 * Manager to get Wifi network related information.
 * <p>
 * Created by Emílio Simões on 28-04-2017.
 */
public class WifiNetworkManager{

    private static final Logger LOGGER = LoggerFactory.getLogger(WifiNetworkManager.class);

    public final static String ACTION_WIFI_DATA_CHANGED =
            "pt.ptinovacao.arqospocket.core.network.action.WIFI_DATA_CHANGED";

    public final static int CONNECTION_STATE_COMPLETED = 3;

    private final static int CONNECTION_STATE_DISCONNECTED = 4;

    private final static int CONNECTION_STATE_AUTHENTICATING = 2;

    private final static int CONNECTION_STATE_INTERFACE_DISABLED = 9;

    private final static int CONNECTION_STATE_SCANNING = 11;

    private final static int CONNECTION_STATE_UNINITIALIZED = 12;

    private final static int CONNECTION_STATE_INVALID = 10;

    private static final String DEFAULT_MAC_ADDRESS = "02:00:00:00:00:00";

    private static final int UNKNOWN = -1;

    private static WifiNetworkManager instance;

    private final CoreApplication application;

    private final WifiManager wifiManager;

    private int signalStrength = UNKNOWN;

    private int rssi = UNKNOWN;

    private int wifiState = CONNECTION_STATE_INVALID;

    private String ssid;

    private String bssid;

    private final ConnectivityManager connectivityManager;

    private boolean wifiRebootRequest;

    private WifiNetworkManager(CoreApplication application) {
        this.application = application;
        wifiManager = (WifiManager) application.getSystemService(Context.WIFI_SERVICE);
        connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * Gets the manager instance.
     *
     * @param application the application object.
     * @return the manager instance.
     */
    public synchronized static WifiNetworkManager getInstance(CoreApplication application) {
        if (instance == null) {
            instance = new WifiNetworkManager(application);
        }
        return instance;
    }

    /**
     * Initializes the manager by registering listeners that watch for network changes.
     */
    public void init() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        application.registerReceiver(wifiStateChangedReceiver, intentFilter);

        IntentFilter rssiIntentFilter = new IntentFilter();
        rssiIntentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        application.registerReceiver(wifiSignalReceiver, rssiIntentFilter);

        IntentFilter scanIntentFilter = new IntentFilter();
        scanIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        application.registerReceiver(wifiScanReceiver, scanIntentFilter);
    }

    /**
     * Finalizes the manager un registering the listeners.
     */
    public void close() {
        application.unregisterReceiver(wifiStateChangedReceiver);
        application.unregisterReceiver(wifiSignalReceiver);
        application.unregisterReceiver(wifiScanReceiver);
    }

    /**
     * Gets the wifi network information.
     *
     * @return the wifi network manager.
     */
    public WifiRadioInfo getWifiInfo() {
        return getDefaultWifiInfo();
    }

    private WifiRadioInfo getDefaultWifiInfo() {

        DefaultWifiInfo wifiInfo = new DefaultWifiInfo();

        wifiInfo.setBssid(getWifiInfoData().getBssid());
        wifiInfo.setSsid(getWifiInfoData().getSsid());
        wifiInfo.setIpAddress(getWifiInfoData().getIpAddress());
        wifiInfo.setLinkSpeed(getWifiInfoData().getLinkSpeed());
        wifiInfo.setRxLevel(getWifiInfoData().getRxLevel());
        wifiInfo.setLeaseDuration(getWifiInfoData().getLeaseDuration());
        wifiInfo.setWifiState(String.valueOf((getWifiInfoData().getWifiState())));

        return wifiInfo;
    }

    /**
     * Gets the wifi network information.
     *
     * @return the wifi network information.
     */
    public WifiInfoData getWifiInfoData() {
        WifiInfoData data = new WifiInfoData();

        data.setWifiState(wifiState);
        if (isWifiAvailable()) {
            data.setRssi(rssi);
            data.setSignal(signalStrength);
            data.setSsid(ssid);
            data.setBssid(bssid);

            for (ScanResult scanResult : wifiManager.getScanResults()) {
                if (areEqual(scanResult.SSID, ssid) && areEqual(scanResult.BSSID, bssid)) {
                    data.setChannel(WiFiUtils.mapChannel(scanResult.frequency).toString());
                }
            }

            WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            data.setLinkSpeed(String.valueOf(connectionInfo.getLinkSpeed()));
            data.setBssid(connectionInfo.getBSSID());
            data.setHiddenSsid(connectionInfo.getHiddenSSID());
            data.setMacAddress(getMacAddress(connectionInfo));
            data.setRealMacAddress(hasRealMacAddress(data.getMacAddress()));
            data.setIpAddress(NetworkUtils.intToIp(connectionInfo.getIpAddress()));

            DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
            data.setDns1(NetworkUtils.intToIp(dhcpInfo.dns1));
            data.setDns2(NetworkUtils.intToIp(dhcpInfo.dns2));
            data.setGateway(NetworkUtils.intToIp(dhcpInfo.gateway));
            data.setLeaseDuration(dhcpInfo.leaseDuration);
            data.setNetMask(NetworkUtils.intToIp(dhcpInfo.netmask));
            data.setServerAddress(NetworkUtils.intToIp(dhcpInfo.serverAddress));

            data.setScanWifiList(wifiManager.getScanResults());
            data.setRxLevel(data.getRssi().toString());
        }

        return data;
    }

    /**
     * Gets if the wifi network is available.
     *
     * @return if the wifi network is available.
     */
    public boolean isWifiAvailable() {
        if (!wifiManager.isWifiEnabled()) {
            return false;
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            return isWifiConnected();
        }

        return isLegacyWifiConnected();
    }

    /**
     * Tries to restart the wifi connection.
     */
    public void restartWifi() {
        wifiRebootRequest = wifiManager.setWifiEnabled(false);
    }

    private void turnWifiOn() {
        wifiManager.setWifiEnabled(true);
    }

    private boolean hasRealMacAddress(String macAddress) {
        return macAddress != null && !DEFAULT_MAC_ADDRESS.equals(macAddress);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean isWifiConnected() {
        Network[] allNetworks;
        try {
            allNetworks = connectivityManager.getAllNetworks();
        } catch (Exception e) {
            LOGGER.error("Error getting network list", e);
            return false;
        }

        for (Network network : allNetworks) {
            try {
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
                if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    return networkInfo.isConnectedOrConnecting();
                }
            } catch (Exception e) {
                LOGGER.error("Error getting network info", e);
            }
        }

        return false;
    }

    @SuppressWarnings("deprecation")
    private boolean isLegacyWifiConnected() {
        try {
            return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        } catch (Exception e) {
            LOGGER.error("Error getting legacy network info", e);
        }
        return false;
    }

    private boolean areEqual(String value1, String value2) {
        value1 = Strings.nullToEmpty(value1).replace("\"", "");
        value2 = Strings.nullToEmpty(value2).replace("\"", "");
        return value1.equals(value2);
    }

    @SuppressLint("HardwareIds")
    private String getMacAddress(WifiInfo connectionInfo) {
        return connectionInfo.getMacAddress();
    }

    private void disconnectingWifi() {
        wifiState = CONNECTION_STATE_DISCONNECTED;
    }

    private void disconnectedWifi() {
        wifiState = CONNECTION_STATE_DISCONNECTED;
        signalStrength = UNKNOWN;
        ssid = null;
        bssid = null;

        KeepAliveManager.getInstance(application).stop();
    }

    private void connectedWifi(String ssid, int signal, String bssid) {

        KeepAliveManager.getInstance(application).start();
        AttachmentsProcessManager.startSendAttachment(application);
        RadiologsAttachmentsProcessManager.startSendAttachment(application);
        ScanlogsAttachmentsProcessManager.startSendAttachment(application);
        BackOffManager.setIpAddress(application);

        wifiState = CONNECTION_STATE_COMPLETED;
        signalStrength = signal;
        this.ssid = ssid;
        this.bssid = bssid;
    }

    private void connectingWifi(String ssid) {
        wifiState = CONNECTION_STATE_AUTHENTICATING;
        this.ssid = ssid;
    }

    private void disablingWifi() {
        wifiState = CONNECTION_STATE_INTERFACE_DISABLED;
        signalStrength = UNKNOWN;
        ssid = null;
    }

    private void disabledWifi() {
        wifiState = CONNECTION_STATE_UNINITIALIZED;
        signalStrength = UNKNOWN;
        ssid = null;
        KeepAliveManager.getInstance(application).stop();
    }

    private void enablingWifi() {
        wifiState = CONNECTION_STATE_SCANNING;
    }

    private void enabledWifi() {
        wifiState = CONNECTION_STATE_SCANNING;
    }

    private void broadcastUpdate() {
        Intent intent = new Intent(ACTION_WIFI_DATA_CHANGED);
        application.sendBroadcast(intent);
    }

    private BroadcastReceiver wifiSignalReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            WifiInfo info = wifiManager.getConnectionInfo();

            signalStrength = WifiManager.calculateSignalLevel(info.getRssi(), 6);
            rssi = info.getRssi();
            broadcastUpdate();
        }
    };

    private BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            broadcastUpdate();
        }

    };

    private BroadcastReceiver wifiStateChangedReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (NetworkInfo.State.CONNECTED.equals(networkInfo.getState())) {
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    String ssid = wifiInfo.getSSID();
                    int signal = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 6);
                    connectedWifi(ssid, signal, wifiInfo.getBSSID());
                } else if (NetworkInfo.State.DISCONNECTED.equals(networkInfo.getState())) {
                    disconnectedWifi();
                } else if (NetworkInfo.State.DISCONNECTING.equals(networkInfo.getState())) {
                    disconnectingWifi();
                } else if (NetworkInfo.State.CONNECTING.equals(networkInfo.getState())) {
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    String ssid = wifiInfo.getSSID();
                    connectingWifi(ssid);
                }
                broadcastUpdate();
                return;
            }

            int extraWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
            switch (extraWifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    disabledWifi();
                    AlarmsManager.getInstance(application).generateAlarm(AlarmUtils.INICIO, AlarmType.A076.name(), AlarmType.A076.getAlarmContent(), "DISABLED");
                    if (wifiRebootRequest) {
                        turnWifiOn();
                    }
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    disablingWifi();
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    enabledWifi();
                    if(!isWifiAvailable()) AlarmsManager.getInstance(application).generateAlarm(AlarmUtils.FIM, AlarmType.A076.name(), AlarmType.A076.getAlarmContent(), "ENABLED");
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    enablingWifi();
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    disabledWifi();
                    break;
            }

            broadcastUpdate();
        }
    };
}

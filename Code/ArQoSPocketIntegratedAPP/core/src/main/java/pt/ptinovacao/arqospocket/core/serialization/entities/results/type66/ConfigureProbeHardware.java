package pt.ptinovacao.arqospocket.core.serialization.entities.results.type66;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.R;
import pt.ptinovacao.arqospocket.core.network.MobileNetworkManager;

/**
 * Created by pedro on 19/05/2017.
 */
public class ConfigureProbeHardware {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigureProbeHardware.class);

    private final static String TERMINAL = "TERMINAL";

    private ProbeHardware probeHardware;

    private static final String VER_HARD_PROBE_POCKET = "4";

    private CoreApplication coreApplication;

    public ConfigureProbeHardware(CoreApplication coreApplication) {
        this.coreApplication = coreApplication;

        MobileNetworkManager mobileNetworkManager = MobileNetworkManager.getInstance(coreApplication);

        probeHardware = new ProbeHardware();
        probeHardware.setVerHard(VER_HARD_PROBE_POCKET);
        probeHardware.setVer(getVersion());
        probeHardware.setMac(mobileNetworkManager.getDeviceImei());

        probeHardware.setVerR1(getAndroidVersion());

        probeHardware.setImeiR1(mobileNetworkManager.getDeviceImei());
        probeHardware.setImsiR1(mobileNetworkManager.getDeviceImsi());
        probeHardware.setTipoR1(TERMINAL);
    }

    public ProbeHardware getProbeHardware() {
        return probeHardware;
    }

    public String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return Build.MODEL + " " + sdkVersion + "(" + release + ")";
    }

    @NonNull
    private String getVersion() {
        return coreApplication.getApplicationContext().getResources().getString(R.string.app_name) + " v." +
                getVersionInfo();
    }

    public String getVersionInfo() {
        StringBuilder versionName = new StringBuilder();
        PackageInfo packageInfo;
        try {
            packageInfo = coreApplication.getApplicationContext().getPackageManager()
                    .getPackageInfo(coreApplication.getApplicationContext().getPackageName(), 0);
            versionName.append(packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            versionName.append("Unknown");
        }

        return versionName.toString();
    }
}

package pt.ptinovacao.arqospocket.core.http.client;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.keepalive.KeepAliveManager;
import pt.ptinovacao.arqospocket.core.settings.SharedPreferencesManager;

/**
 * Manager for the management service URLs.
 * <p>
 * Created by Tomas on 16/05/2017.
 */
public class RemoteServiceUrlManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteServiceUrlManager.class);

    public static String baseRequestUrl = "http://arqos.ptinovacao.pt/simulatorcore/";

    private CoreApplication application;

    private static RemoteServiceUrlManager instance;

    private RemoteServiceUrlManager(CoreApplication application) {
        this.application = application;

        String ipAddressManagement =
                Strings.emptyToNull(SharedPreferencesManager.getInstance(application).getIpAddressManagement());

        if (ipAddressManagement != null) {
            baseRequestUrl = ipAddressManagement;
            LOGGER.debug("Ip address of server {}.", ipAddressManagement);
        }
    }

    public static RemoteServiceUrlManager getInstance(CoreApplication coreApplication) {
        if (instance == null) {
            instance = new RemoteServiceUrlManager(coreApplication);
        }
        return instance;
    }

    public void updateBaseUrl(String ippAddress) {
        if (Strings.isNullOrEmpty(ippAddress)) {
            LOGGER.error("No validate ip address.");
            return;
        }

        baseRequestUrl = ippAddress;
        SharedPreferencesManager.getInstance(application).setIpAddressManagement(ippAddress);
        KeepAliveManager.getInstance(application).reset();
        LOGGER.debug("Updated ip address of server. To {}.", ippAddress);
    }

    String urlKeepAliveProcess() {
        return baseRequestUrl + "probe-announce?action=keepalive-process";
    }

    String urlAttachmentProcess() {
        return baseRequestUrl + "attach-files?action=attach-files-process";
    }

    String urlRadiologsAttachmentProcess() {
        return baseRequestUrl + "attach-files?action=radio-logs-process";
    }

    String urlScanlogsAttachmentProcess() {
        return baseRequestUrl + "attach-files?action=scan-logs-process";
    }

    String urlIpUpdate() {
        return baseRequestUrl + "probe-announce?action=ipupdate-process";
    }

    public String urlResultProcess() {
        return baseRequestUrl + "results?action=results-process";
    }

    public String urlAlarmsProcess() {
        return baseRequestUrl + "alarms?action=alarms-process";
    }

    public String urlCDRsProcess() {
        return baseRequestUrl + "cdrs?action=cdrs-process";
    }

    String urlAutoDiscoveryProcess() {
        return baseRequestUrl + "probe-announce?action=autodiscovery-process";
    }

}

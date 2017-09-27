package pt.ptinovacao.arqospocket.core.serialization.entities.results.type66;

import com.google.common.base.Strings;

import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.http.client.RemoteServiceUrlManager;
import pt.ptinovacao.arqospocket.core.settings.SharedPreferencesManager;
import pt.ptinovacao.arqospocket.core.ssh.AttachmentsProcessManager;

/**
 * ConfigureProbeConfiguration
 * <p>
 * Created by pedro on 19/05/2017.
 */
public class ProbeConfigurationManager {

    private ProbeConfiguration probeConfiguration;

    public ProbeConfigurationManager(CoreApplication coreApplication) {
        probeConfiguration = new ProbeConfiguration();
        SharedPreferencesManager preferencesManager = SharedPreferencesManager.getInstance(coreApplication);

        String ipAddressManagement = preferencesManager.getIpAddressManagement();

        if (!Strings.isNullOrEmpty(ipAddressManagement)) {
            probeConfiguration.setManagementServerAddress(ipAddressManagement);
        } else {
            probeConfiguration.setManagementServerAddress(RemoteServiceUrlManager.baseRequestUrl);
        }

        probeConfiguration.setMaxHistoryTime(preferencesManager.getDatabaseCleanupInterval());
        probeConfiguration.setPercentageMaxMemoryOccupied(preferencesManager.getPercentageMemoryOccupied());
        probeConfiguration.setMaxHistoryTimeFiles(preferencesManager.getFileCleanupInterval());

        String baseDestinationSFTP = preferencesManager.getBaseDestinationSFTP();
        if (Strings.isNullOrEmpty(baseDestinationSFTP)) {
            probeConfiguration.setBaseDestinationSFTP(AttachmentsProcessManager.baseDestinationSftp);
        } else {
            probeConfiguration.setBaseDestinationSFTP(baseDestinationSFTP);
        }

        probeConfiguration.setRadiologsDedicated(preferencesManager.getRadiologsDedicatedMode());
        probeConfiguration.setRadiologsIdle(preferencesManager.getRadiologsIdleMode());
        probeConfiguration.setScanlogsEnable(preferencesManager.getScanlogsEnable());
        if(preferencesManager.getRadiologPeriodicity() <= 5) preferencesManager.setRadiologPeriodicity(20);
        probeConfiguration.setRadiologsInterval(preferencesManager.getRadiologPeriodicity());

    }

    public ProbeConfiguration getProbeConfiguration() {
        return probeConfiguration;
    }
}
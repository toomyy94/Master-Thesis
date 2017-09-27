package pt.ptinovacao.arqospocket.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.core.alarms.AlarmsManager;
import pt.ptinovacao.arqospocket.core.cdrs.CDRsManager;
import pt.ptinovacao.arqospocket.core.messaging.SmsMessageManager;
import pt.ptinovacao.arqospocket.core.network.MobileNetworkManager;
import pt.ptinovacao.arqospocket.core.network.WifiNetworkManager;
import pt.ptinovacao.arqospocket.core.producers.TestProducer;
import pt.ptinovacao.arqospocket.core.settings.SharedPreferencesManager;

/**
 * Utility class the bootstraps the application test engine and initializes all the required consumers and producers.
 * <p>
 * Created by Emílio Simões on 06-04-2017.
 */
class TestEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestEngine.class);

    private static TestEngine instance;

    private final CoreApplication application;

    private TestEngine(CoreApplication application) {
        this.application = application;
    }

    synchronized static TestEngine getInstance(CoreApplication application) {
        if (instance == null) {
            instance = new TestEngine(application);
        }
        return instance;
    }

    void bootstrap() {
        LOGGER.debug("Application engine bootstrap started");

        TestProducer.startProducers(application);
        MobileNetworkManager.getInstance(application).init();
        WifiNetworkManager.getInstance(application).init();
        SmsMessageManager.getInstance(application).init();

        if (SharedPreferencesManager.getInstance(application).getConnectionWithMSManual()) {
            ResultsManager.getInstance(application).deliverPendingResults();
            AlarmsManager.getInstance(application).deliverPendingAlarms();
            CDRsManager.getInstance(application).deliverPendingCDRs();
        }
    }

    void cleanUp() {
        TestProducer.cleanUpProducers();
        MobileNetworkManager.getInstance(application).close();
        WifiNetworkManager.getInstance(application).close();
        SmsMessageManager.getInstance(application).close();
    }
}

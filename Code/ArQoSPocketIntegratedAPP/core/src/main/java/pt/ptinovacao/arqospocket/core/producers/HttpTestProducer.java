package pt.ptinovacao.arqospocket.core.producers;

import android.support.annotation.NonNull;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.TestConsumer;
import pt.ptinovacao.arqospocket.core.TestParser;
import pt.ptinovacao.arqospocket.core.alarms.AlarmType;
import pt.ptinovacao.arqospocket.core.alarms.AlarmUtils;
import pt.ptinovacao.arqospocket.core.alarms.AlarmsManager;
import pt.ptinovacao.arqospocket.core.http.client.HttpClient;
import pt.ptinovacao.arqospocket.core.http.client.RemoteServiceUrlManager;
import pt.ptinovacao.arqospocket.core.http.server.HttpRequestListener;
import pt.ptinovacao.arqospocket.core.http.server.HttpServer;
import pt.ptinovacao.arqospocket.core.keepalive.KeepAliveManager;
import pt.ptinovacao.arqospocket.core.maintenance.FileMaintenanceManager;
import pt.ptinovacao.arqospocket.core.network.MobileNetworkManager;
import pt.ptinovacao.arqospocket.core.network.NetworkUtils;
import pt.ptinovacao.arqospocket.core.serialization.entities.ManagementMessage;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.ResultFileData;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.type2.ConfigureModuleStatusRequest;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.type2.ModuleStatusRequest;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.type2.ResultFileDataAssocitedModuleStatusRequestResponse;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.type66.ConfigureProbeHardware;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.type66.ProbeConfiguration;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.type66.ProbeConfigurationManager;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.type66.ProbeHardware;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.type66.ResultFileDataAssocitedStatusRequestResponse;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.type71.ConfigureTestInfo;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.type71.ResultFileDataAssociatedLoadedTestStatusRequest;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.type71.TestInfo;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.type73.ConfigureOccupationReportRequest;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.type73.OccupationReportRequest;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.type73.ResultFileDataAssocitedOccupationReportRequestResponse;
import pt.ptinovacao.arqospocket.core.settings.SharedPreferencesManager;
import pt.ptinovacao.arqospocket.core.ssh.AttachmentsProcessManager;
import pt.ptinovacao.arqospocket.core.ssh.RadiologsAttachmentsProcessManager;
import pt.ptinovacao.arqospocket.core.ssh.ScanlogsAttachmentsProcessManager;
import pt.ptinovacao.arqospocket.core.tests.TestExecutionType;
import pt.ptinovacao.arqospocket.core.tests.data.TestData;
import pt.ptinovacao.arqospocket.core.utils.SystemUtils;

/**
 * Producer that listens for HTTP events and provides the consumer with the received tests.
 * <p>
 * Created by Emílio Simões on 06-04-2017.
 */

class HttpTestProducer extends TestProducer implements HttpRequestListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpTestProducer.class);

    private static final int TEMP_MINIMUM_ALARM_MANAGER = 30;

    private static final int MESSAGE_TYPE_RESET_REQUEST_1 = 1;

    private static final int MESSAGE_TYPE_MODULE_STATUS_REQUEST_2 = 2;

    private static final int MESSAGE_TYPE_MODULE_RESET_REQUEST_6 = 6;

    private static final int MESSAGE_TYPE_OPERATOR_SCAN_REQUEST_9 = 9;

    private static final int MESSAGE_TYPE_RESULT_64 = 64;

    private static final int MESSAGE_TYPE_CANCEL_TEST_REQUEST_65 = 65;

    private static final int MESSAGE_TYPE_STATUS_REQUEST_66 = 66;
/**/
    private static final int MESSAGE_TYPE_CONFIGURATION_REQUEST_67 = 67;

    private static final int MESSAGE_LOADED_TEST_STATUS_REQUEST_71 = 71;

    private static final int MESSAGE_TYPE_OCCUPATION_REPORT_REQUEST_73 = 73;

    private HttpServer server;

    private String deviceImei;

    /**
     * Constructor for the test producer.
     *
     * @param application the application to access the global context.
     * @param consumer the consumer to consume the tests.
     */
    HttpTestProducer(CoreApplication application, TestConsumer consumer) {
        super(application, consumer);
        deviceImei = MobileNetworkManager.getInstance(getApplication()).getDeviceImei();
    }

    @Override
    void start() {
        this.server = new HttpServer(getApplication(), this);
        try {
            server.start();
            LOGGER.debug("Server started and listening on '{}'", server.getAddress());
            LOGGER.debug("IMEI '{}'",
                    Strings.nullToEmpty(MobileNetworkManager.getInstance(getApplication()).getDeviceImei()));
            KeepAliveManager.getInstance(getApplication()).start();
            AttachmentsProcessManager.startSendAttachment(getApplication());
            RadiologsAttachmentsProcessManager.startSendAttachment(getApplication());
            ScanlogsAttachmentsProcessManager.startSendAttachment(getApplication());

        } catch (IOException e) {
            LOGGER.error("Could not start HTTP server, remote tests will not be processed", e);
        }
    }

    @Override
    void stop() {
        server.stop();
    }

    @Override
    public ProcessedResponse onRequestReceived(@NonNull String content) {
        LOGGER.debug("Processing received content: {}", content.length());

        TestParser parser = new TestParser();
        ManagementMessage message = parser.parseMessage(content);

        if (!imeiIsValid(message.getMacAddress())) {
            LOGGER.warn("Request was made with bad IMEI {}", message.getMacAddress());
            if (message.getMessageType() == MESSAGE_TYPE_RESULT_64) AlarmsManager.getInstance(getApplication()).generateAlarm(AlarmUtils.INICIO,AlarmType.A052.name(), AlarmType.A052.getAlarmContent(),
                    "expected: " + MobileNetworkManager.getInstance(getApplication()).getDeviceImei() + " received: " + message.getMacAddress());
            return new ProcessedResponse(2, "Invalid MAC");
        }

        if (message.getMessageType() == MESSAGE_TYPE_RESULT_64) {
            if (isValidTestInterval(message)) {
                getConsumer().consumeAll(message.getTests(), TestExecutionType.SCHEDULED);
            } else {
                return new ProcessedResponse(18, "Interval between tasks executions too short");
            }
        } else if (message.getMessageType() == MESSAGE_TYPE_RESET_REQUEST_1) {
            tryToRebootPhone();
        } else if (message.getMessageType() == MESSAGE_TYPE_MODULE_STATUS_REQUEST_2) {
            return new ProcessedResponse(responseModuleStatusRequest(parser));
        } else if (message.getMessageType() == MESSAGE_TYPE_MODULE_RESET_REQUEST_6) {
            tryToRebootRadio();
        }else if (message.getMessageType() == MESSAGE_TYPE_CANCEL_TEST_REQUEST_65) {
            getConsumer().cancelAll(message.getTests());
            if (!checkExistTest(message)) {
                new ProcessedResponse(15, "No such test");
            }
        } else if (message.getMessageType() == MESSAGE_TYPE_STATUS_REQUEST_66) {
            return new ProcessedResponse(responseStatusRequest(parser));
        } else if (message.getMessageType() == MESSAGE_TYPE_CONFIGURATION_REQUEST_67) {
            return new ProcessedResponse(responseConfigurationRequest(parser, message));
        } else if (message.getMessageType() == MESSAGE_LOADED_TEST_STATUS_REQUEST_71) {
            return new ProcessedResponse(responseLoadedTestStatusRequest(parser));
        } else if (message.getMessageType() == MESSAGE_TYPE_OCCUPATION_REPORT_REQUEST_73) {
            return new ProcessedResponse(responseOccupationReportRequest(parser, message));
        }

        return null;
    }

    private boolean checkExistTest(ManagementMessage message) {
        for (TestData testData : message.getTests()) {
            if (!getApplication().getDatabaseHelper().createExecutingEventDao().isExistTest(testData.getTestId())) {
                return false;
            }
            if (!getApplication().getDatabaseHelper().createScheduledEventDao().isExistTest(testData.getTestId())) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidTestInterval(ManagementMessage message) {

        for (TestData testData : message.getTests()) {
            if (testData.getRecursion() != null && testData.getRecursion().getParameters() != null &&
                    testData.getRecursion().getParameters().getInterval() != null &&
                    testData.getRecursion().getParameters().getInterval() < TEMP_MINIMUM_ALARM_MANAGER) {
                return false;
            }
        }
        return true;
    }

    private boolean imeiIsValid(String macAddress) {
        return Strings.nullToEmpty(deviceImei).trim().equals(Strings.nullToEmpty(macAddress).trim());
    }

    private String responseOperatorScanRequest(TestParser parser) {
        ArrayList<TestInfo> moduleStatusRequest = new ConfigureTestInfo(getApplication()).getTestInfo();

        ResultFileData data =
                new ResultFileDataAssociatedLoadedTestStatusRequest.Builder().appendTestInfo(moduleStatusRequest)
                        .equipmentType(HttpClient.EQUIPMENT_TYPE).serialNumber(deviceImei).macAddress(deviceImei)
                        .ipAddress(NetworkUtils.getIPAddress(true)).timestamp(Calendar.getInstance().getTime())
                        .success().build();

        return parser.stringify(data);
    }

    private String responseStatusRequest(TestParser parser) {
        ProbeHardware probeHardware = new ConfigureProbeHardware(getApplication()).getProbeHardware();
        ProbeConfiguration probeConfiguration = new ProbeConfigurationManager(getApplication()).getProbeConfiguration();

        ResultFileData data =
                new ResultFileDataAssocitedStatusRequestResponse.Builder().appendProbeConfiguration(probeConfiguration)
                        .appendProbeHardware(probeHardware).equipmentType(HttpClient.EQUIPMENT_TYPE)
                        .serialNumber(deviceImei).macAddress(deviceImei).ipAddress(NetworkUtils.getIPAddress(true))
                        .timestamp(Calendar.getInstance().getTime()).success().build();

        return parser.stringify(data);
    }

    private String responseModuleStatusRequest(TestParser parser) {
        ModuleStatusRequest moduleStatusRequest =
                new ConfigureModuleStatusRequest(getApplication()).getModuleStatusRequest();

        ResultFileData data = new ResultFileDataAssocitedModuleStatusRequestResponse.Builder()
                .appendModuleStatusRequest(moduleStatusRequest).equipmentType(HttpClient.EQUIPMENT_TYPE)
                .serialNumber(deviceImei).macAddress(deviceImei).ipAddress(NetworkUtils.getIPAddress(true))
                .timestamp(Calendar.getInstance().getTime()).success().build();

        return parser.stringify(data);
    }

    private String responseOccupationReportRequest(TestParser parser, ManagementMessage message) {
        OccupationReportRequest moduleStatusRequest =
                new ConfigureOccupationReportRequest(getApplication(), message).getOccupationReportRequest();

        ResultFileData data = new ResultFileDataAssocitedOccupationReportRequestResponse.Builder()
                .appendOccupationReportRequest(moduleStatusRequest).equipmentType(HttpClient.EQUIPMENT_TYPE)
                .serialNumber(deviceImei).macAddress(deviceImei).ipAddress(NetworkUtils.getIPAddress(true))
                .timestamp(Calendar.getInstance().getTime()).success().build();

        return parser.stringify(data);
    }

    private String responseLoadedTestStatusRequest(TestParser parser) {
        ArrayList<TestInfo> moduleStatusRequest = new ConfigureTestInfo(getApplication()).getTestInfo();

        ResultFileData data =
                new ResultFileDataAssociatedLoadedTestStatusRequest.Builder().appendTestInfo(moduleStatusRequest)
                        .equipmentType(HttpClient.EQUIPMENT_TYPE).serialNumber(deviceImei).macAddress(deviceImei)
                        .ipAddress(NetworkUtils.getIPAddress(true)).timestamp(Calendar.getInstance().getTime())
                        .success().build();

        return parser.stringify(data);
    }

    private String responseConfigurationRequest(TestParser parser, ManagementMessage message) {
        if (message.getProbeConfiguration() != null) {

            String managementServerAddress =
                    Strings.nullToEmpty(message.getProbeConfiguration().getManagementServerAddress()).trim();
            if (managementServerAddress.length() > 0) {
                RemoteServiceUrlManager.getInstance(getApplication()).updateBaseUrl(managementServerAddress);
            }

            Integer maxHistoryTime = message.getProbeConfiguration().getMaxHistoryTime();
            if (maxHistoryTime != null) {
                SharedPreferencesManager.getInstance(getApplication()).setDatabaseCleanupInterval(maxHistoryTime);
            }

            Integer percentageMemoryOccupied = message.getProbeConfiguration().getPercentageMaxMemoryOccupied();
            if (percentageMemoryOccupied != null) {
                SharedPreferencesManager.getInstance(getApplication())
                        .setPercentageMemoryOccupied(percentageMemoryOccupied);
            }

            Integer filesMaxHistoryTimeFiles = message.getProbeConfiguration().getMaxHistoryTimeFiles();
            if (filesMaxHistoryTimeFiles != null) {
                SharedPreferencesManager.getInstance(getApplication()).setFileCleanupInterval(filesMaxHistoryTimeFiles);
                FileMaintenanceManager.getInstance(getApplication()).cleanOldExecutingEntries();
            }

            Integer scanlogsEnable = message.getProbeConfiguration().getScanlogsEnable();
            if (scanlogsEnable != null) {
                SharedPreferencesManager.getInstance(getApplication()).setScanlogsEnable(scanlogsEnable);
            }

            Integer radiologsDedicated = message.getProbeConfiguration().getRadiologsDedicated();
            if (radiologsDedicated != null) {
                SharedPreferencesManager.getInstance(getApplication()).setRadiologsDedicatedMode(radiologsDedicated);
            }

            Integer radiologsIdle = message.getProbeConfiguration().getRadiologsIdle();
            if (radiologsIdle != null) {
                SharedPreferencesManager.getInstance(getApplication()).setRadiologsIdleMode(radiologsIdle);
            }

            Integer radiologsInterval = message.getProbeConfiguration().getRadiologsInterval();
            if (radiologsInterval != null) {
                SharedPreferencesManager.getInstance(getApplication()).setRadiologPeriodicity(radiologsInterval);
            }

            Integer radiologsMaxsize = message.getProbeConfiguration().getRadiologsMaxsize();
            if (radiologsMaxsize != null) {
                SharedPreferencesManager.getInstance(getApplication()).setRadiologsMaxsize(radiologsMaxsize);
            }

            Integer radiologsMultievent = message.getProbeConfiguration().getRadiologsMultievent();
            if (radiologsMultievent != null) {
                SharedPreferencesManager.getInstance(getApplication()).setRadiologsMultievent(radiologsMultievent);
            }

            Integer radiologsCellreselection = message.getProbeConfiguration().getRadiologsCellreselection();
            if (radiologsCellreselection != null) {
                SharedPreferencesManager.getInstance(getApplication()).setRadiologsCellreselection(radiologsCellreselection);
            }

            Integer maxTemperature = message.getProbeConfiguration().getMaxTemperature();
            if (maxTemperature != null) {
                SharedPreferencesManager.getInstance(getApplication()).setMaxTemperature(maxTemperature);
            }

            String baseDestinationSFTP = message.getProbeConfiguration().getBaseDestinationSFTP();
            if (baseDestinationSFTP != null) {
                SharedPreferencesManager.getInstance(getApplication()).setBaseDestinationSFTP(baseDestinationSFTP);
            }

            String sftpUsername = message.getProbeConfiguration().getSFTPUsername();
            if (sftpUsername != null) {
                SharedPreferencesManager.getInstance(getApplication()).setUsernameSFTP(sftpUsername);
                AttachmentsProcessManager.getInstance(getApplication()).configureServer();
                RadiologsAttachmentsProcessManager.startSendAttachment(getApplication());
                ScanlogsAttachmentsProcessManager.startSendAttachment(getApplication());
            }

            String sftpPassword = message.getProbeConfiguration().getSFTPPassword();
            if (sftpPassword != null) {
                SharedPreferencesManager.getInstance(getApplication()).setPasswordSFTP(sftpPassword);
                AttachmentsProcessManager.getInstance(getApplication()).configureServer();
                RadiologsAttachmentsProcessManager.startSendAttachment(getApplication());
                ScanlogsAttachmentsProcessManager.startSendAttachment(getApplication());
            }

            AlarmsManager.getInstance(getApplication()).generateAlarm(AlarmUtils.INICIO, AlarmType.A004.name(), AlarmType.A004.getAlarmContent());

        }

        ResultFileData data =
                new ResultFileData.Builder().equipmentType(HttpClient.EQUIPMENT_TYPE).serialNumber(deviceImei)
                        .macAddress(deviceImei).ipAddress(NetworkUtils.getIPAddress(true))
                        .timestamp(Calendar.getInstance().getTime()).success().build();

        return parser.stringify(data);
    }

    public void tryToRebootPhone(){
        try {
            SystemUtils.requestPermission();
            AlarmsManager.getInstance(getApplication()).generateAlarm(AlarmUtils.INICIO, AlarmType.A051.name(), AlarmType.A051.getAlarmContent());

            Runtime.getRuntime().exec("su -c reboot");
        } catch (Exception e) {
            LOGGER.error("Could not reboot: ",e);
        }
    }

    public void tryToRebootRadio(){
        MobileNetworkManager.getInstance(getApplication()).restartNetwork();
    }

}
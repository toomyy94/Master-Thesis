package pt.ptinovacao.arqospocket.core.serialization.entities.results.type2;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.TestParser;
import pt.ptinovacao.arqospocket.core.location.GeoLocationManager;
import pt.ptinovacao.arqospocket.core.network.MobileNetworkManager;
import pt.ptinovacao.arqospocket.core.network.MobileNetworkMode;
import pt.ptinovacao.arqospocket.core.network.NetworkUtils;
import pt.ptinovacao.arqospocket.core.network.WifiNetworkManager;
import pt.ptinovacao.arqospocket.core.network.data.mobile.MobileInfoData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.TestData;
import pt.ptinovacao.arqospocket.core.tests.data.TestResult;
import pt.ptinovacao.arqospocket.core.utils.BatteryUtils;
import pt.ptinovacao.arqospocket.core.utils.DiskUtils;
import pt.ptinovacao.arqospocket.core.utils.IntegerUtils;
import pt.ptinovacao.arqospocket.core.utils.TimeUtils;
import pt.ptinovacao.arqospocket.persistence.ExecutingEventDao;
import pt.ptinovacao.arqospocket.persistence.models.ExecutingEvent;

/**
 * Created by pedro on 22/05/2017.
 */
public class ConfigureModuleStatusRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigureModuleStatusRequest.class);

    private ModuleStatusRequest moduleStatusRequest;

    private CoreApplication coreApplication;

    private TelephonyStateManager telephonyManagerState = TelephonyStateManager.getInstance(coreApplication);

    public ConfigureModuleStatusRequest(CoreApplication coreApplication) {
        this.coreApplication = coreApplication;

        MobileNetworkManager mobileNetworkManager = MobileNetworkManager.getInstance(coreApplication);
        ExecutingEventDao executingEventDao = coreApplication.getDatabaseHelper().createExecutingEventDao();
        GeoLocationManager geoLocationManager = GeoLocationManager.getInstance(coreApplication);

        WifiNetworkManager wifiNetworkManager = WifiNetworkManager.getInstance(coreApplication);

        moduleStatusRequest = new ModuleStatusRequest();
        moduleStatusRequest.setTeste(executingEventDao.countAllExecutingEvents() > 0 ? 1 : 0);
        moduleStatusRequest.setTarefa(executingEventDao.countAllExecutingEvents() > 0 ? 1 : 0);

        TaskData taskData = getTaskData(executingEventDao);
        if (taskData != null) {
            moduleStatusRequest.setIdiTarefa(IntegerUtils.parseInt(taskData.getTaskId()));
        }

        ExecutingEvent executingEvent = getTestExecuting(executingEventDao);
        if (executingEvent != null) {
            TestParser testParser = new TestParser();
            TestData testData = testParser.parseSingleTest(executingEvent.getTestData());
            if (testData != null) {
                moduleStatusRequest.setIdTimeslot(testData.getMacroNumber());
            }
        }

        if (executingEvent != null) {
            String resultDataString = Strings.nullToEmpty(executingEvent.getResultData());
            final TestParser testParser = new TestParser();
            TestResult testResult = testParser.parseSingleResult(resultDataString);
            if (testResult != null && testResult.getTaskResults().length > 0) {
                moduleStatusRequest.setTarefaN(Long.valueOf(
                        testResult.getTaskResults()[testResult.getTaskResults().length - 1].getTaskNumber()));
            }
        }

        if (executingEvent != null) {
            TestParser testParser = new TestParser();
            TestData testData = testParser.parseSingleTest(executingEvent.getTestData());
            if (testData != null) {
                moduleStatusRequest.setNomeTeste(testData.getTestName());
            }
        }

        if (mobileNetworkManager != null) {
            moduleStatusRequest.setCountryCode(mobileNetworkManager.getCountryCode());
            moduleStatusRequest.setRsrp(mobileNetworkManager.getReferenceSignalReceivedPower());
            moduleStatusRequest.setRsrq(mobileNetworkManager.getReferenceSignalReceivedQuality());

            if (mobileNetworkManager.getMobileInfoData() != null) {
                moduleStatusRequest.setSignal(mobileNetworkManager.getMobileInfoData().getSignalLevel());
                moduleStatusRequest.setRede(getInformationGSM(mobileNetworkManager.getMobileInfoData()).toString());
                moduleStatusRequest.setCel(getCel(mobileNetworkManager.getMobileInfoData()));
                moduleStatusRequest.setIccid(mobileNetworkManager.getMobileInfoData().getSimIccid());
                moduleStatusRequest.setMcc(IntegerUtils.parseInt(mobileNetworkManager.getMobileInfoData().getMcc()));
                moduleStatusRequest.setMnc(IntegerUtils.parseInt(mobileNetworkManager.getMobileInfoData().getMccMnc()));

                moduleStatusRequest.setCall(insertValuesCallInformation(mobileNetworkManager, wifiNetworkManager));
                moduleStatusRequest.setgCallStatus(telephonyManagerState.getCallStatus() == null ?
                        TelephonyStateManager.CallStatus.IDLE.ordinal() :
                        telephonyManagerState.getCallStatus().ordinal());
            }
        }

        if (wifiNetworkManager != null && wifiNetworkManager.getWifiInfoData() != null) {

            moduleStatusRequest.setWifiGw(wifiNetworkManager.getWifiInfoData().getGateway());
            moduleStatusRequest.setWifiMask(wifiNetworkManager.getWifiInfoData().getNetMask());

            moduleStatusRequest.setWifiDns1(wifiNetworkManager.getWifiInfoData().getDns1());
            moduleStatusRequest.setWifiDns2(wifiNetworkManager.getWifiInfoData().getDns2());
            moduleStatusRequest.setWifiLease(wifiNetworkManager.getWifiInfoData().getLeaseDuration());

            moduleStatusRequest.setWifiBssid(wifiNetworkManager.getWifiInfoData().getBssid());
            moduleStatusRequest.setWifiSsid(wifiNetworkManager.getWifiInfoData().getSsid().replace("\"",""));
            moduleStatusRequest.setWifiQuality(wifiNetworkManager.getWifiInfoData().getSignal());
            moduleStatusRequest
                    .setWifiNoiseLevel(IntegerUtils.parseInt(wifiNetworkManager.getWifiInfoData().getRxLevel()));

            moduleStatusRequest.setWifiConnectionState(wifiNetworkManager.getWifiInfoData().getWifiState());
        }

        moduleStatusRequest.setGps(geoLocationManager.getLocationInfo().format());
        moduleStatusRequest.setTemperatura(BatteryUtils.batteryTemperature(coreApplication));
        moduleStatusRequest.setDf((int) DiskUtils.percentageFreeSpace());

        moduleStatusRequest.setSync(TimeUtils.isTimeAutomatic(coreApplication) ? "NETWORK" : "NO");

        Integer battery = BatteryUtils.getPercentage(coreApplication);
        if (battery != null) {
            moduleStatusRequest.setBatLevel(Float.valueOf(battery));
        }

        MobileNetworkManager networkManager = MobileNetworkManager.getInstance(coreApplication);
        if (networkManager != null) {
            moduleStatusRequest.setModem(networkManager.isMobileAvailable() ? 1 : 0);
            moduleStatusRequest.setGsmRoam(networkManager.isDataRoamingEnabled() ? 1 : 0);
        } else {
            moduleStatusRequest.setModem(0);
        }

        moduleStatusRequest.setBatTime(-1);
        moduleStatusRequest.setWifiAddr(NetworkUtils.getIPAddress(true));
        moduleStatusRequest.setSimType(0);
        moduleStatusRequest.setPwrStatus(BatteryUtils.getStateBatteryEnergy(coreApplication));
    }

    private String getCel(MobileInfoData mobileInfoData) {
        StringBuilder stringBuilder = new StringBuilder();

        if (mobileInfoData.getMobileNetworkMode() == MobileNetworkMode.NONE) {
            return null;
        } else {
            stringBuilder.append(mobileInfoData.getCellLocation()).append("-").append(mobileInfoData.getIdCell());
        }

        return stringBuilder.toString();
    }

    private String insertValuesCallInformation(MobileNetworkManager mobileNetworkManager, WifiNetworkManager wifiNetworkManager) {

        Integer call0, type, inOut, movel;
        call0 = telephonyManagerState.getCallStatus() == null ? TelephonyStateManager.CallStatus.IDLE.ordinal() :
                        telephonyManagerState.getCallStatus().ordinal();

        if(call0==0) type = null;
        else type = TelephonyStateManager.CallType.VOICE.ordinal();

        inOut = telephonyManagerState.getCallInOrOut() == null ? null :
                telephonyManagerState.getCallInOrOut().ordinal();
        movel = null;

        return getCallInformation(call0, type, inOut, movel, mobileNetworkManager, wifiNetworkManager);
    }

    private String getCallInformation(Integer call0, Integer type, Integer inOut, Integer movel, MobileNetworkManager mobileNetworkManager, WifiNetworkManager wifiNetworkManager) {
        StringBuilder value = new StringBuilder();

        if (call0 != null) {
            value.append("call0=").append(call0).append(" ");
        }
        if (type != null) {
            value.append("tipo=").append(type).append(" ");
        }
        if (inOut != null) {
            value.append("in_out=").append(inOut).append(" ");
        }
        if (movel != null) {
            value.append("telefone=").append(movel).append(" ");
        }

        if(!Strings.isNullOrEmpty(value.toString())) {
            value.setLength(value.length() - 1);
        }

        if(mobileNetworkManager == null && wifiNetworkManager == null) value.append(" - call1=0");
        else value.append(" - call1=3 tipo=1 in_out=1");

        return value.toString();
    }

    @NonNull
    private StringBuilder getInformationGSM(MobileInfoData mobileInfoData) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("rede=");

        switch (mobileInfoData.getMobileNetworkMode()) {
            case NONE:
                stringBuilder.append("gsm=0 gprs=0 umts=0 lte=0");
                break;
            case EDGE:
                stringBuilder.append("gsm=1 gprs=1 umts=0 lte=0");
                break;
            case GPRS:
                stringBuilder.append("gsm=1 gprs=1 umts=0 lte=0");
                break;
            case HSPA:
                stringBuilder.append("gsm=0 gprs=0 umts=1 lte=0");
                break;
            case UMTS:
                stringBuilder.append("gsm=0 gprs=0 umts=1 lte=0");
                break;
            case LTE:
                stringBuilder.append("gsm=0 gprs=0 umts=0 lte=1");
                break;
        }

        stringBuilder.append(" operadora=").append(mobileInfoData.getNetworkOperatorName());
        return stringBuilder;
    }

    private TaskData getTaskData(ExecutingEventDao executingEventDao) {
        ExecutingEvent executingEvent = getTestExecuting(executingEventDao);
        if (executingEvent == null) {
            return null;
        }

        String testDataString = Strings.nullToEmpty(executingEvent.getTestData());
        String resultDataString = Strings.nullToEmpty(executingEvent.getResultData());

        final TestParser testParser = new TestParser();
        TestResult testResult = testParser.parseSingleResult(resultDataString);
        TestData testData = testParser.parseSingleTest(testDataString);

        TaskResult[] taskResults;
        if (testResult == null) {
            taskResults = new TaskResult[] {};
        } else {
            taskResults = testResult.getTaskResults();
        }

        if (taskResults.length < testData.getTasksData().length) {
            return testData.getTasksData()[taskResults.length];
        }

        return null;
    }

    @Nullable
    private ExecutingEvent getTestExecuting(ExecutingEventDao executingEventDao) {
        ExecutingEvent executingEvent = executingEventDao.readExecutingEvent();

        if (executingEvent == null) {
            return null;
        }
        return executingEvent;
    }

    public ModuleStatusRequest getModuleStatusRequest() {
        return moduleStatusRequest;
    }
}
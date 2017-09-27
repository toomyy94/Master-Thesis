package pt.ptinovacao.arqospocket.core.cdrs;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.location.GeoLocationManager;
import pt.ptinovacao.arqospocket.core.network.MobileNetworkManager;
import pt.ptinovacao.arqospocket.core.network.WifiNetworkManager;
import pt.ptinovacao.arqospocket.core.network.data.mobile.MobileInfoData;
import pt.ptinovacao.arqospocket.core.network.data.wifi.WifiInfoData;
import pt.ptinovacao.arqospocket.core.tests.data.results.AnswerVoiceCallTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.MakeVoiceCallTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.ReceiveSmsTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.SendSmsTaskResult;
import pt.ptinovacao.arqospocket.core.tests.results.AnswerVoiceCallExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.results.MakeVoiceCallExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.results.ReceiveSmsExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.results.SendSmsExecutionResult;
import pt.ptinovacao.arqospocket.core.utils.DateUtils;
import pt.ptinovacao.arqospocket.persistence.models.CDR;

/**
 * Created by Tomás Rodrigues on 13-09-2017.
 */

public class CDRUtils  {
    private static final Logger LOGGER = LoggerFactory.getLogger(CDRUtils.class);

    //Instances
    private static CDRUtils instance;
    private final CoreApplication application;
    private MobileNetworkManager mobileNetworkManager;
    private WifiNetworkManager wifiNetworkManager;
    private GeoLocationManager geoLocationInfo;
    private MobileInfoData mobileInfoData;
    private WifiInfoData wifiInfoData;

    //Variables
    public CDR tempInitSmsCdr = new CDR();
    public CDR tempInitCallCdr = new CDR();

    public CDRUtils(CoreApplication application) {
        this.application = application;
        geoLocationInfo = GeoLocationManager.getInstance(application);
        mobileNetworkManager = MobileNetworkManager.getInstance(application);
        mobileInfoData = mobileNetworkManager.getMobileInfoData();
        wifiNetworkManager = WifiNetworkManager.getInstance(application);
    }

    public static CDRUtils getInstance(CoreApplication application) {
        if (instance == null) {
            instance = new CDRUtils(application);
        }
        return instance;
    }

    public void fillTempInitSmsCdr(){
        tempInitSmsCdr.setInitGpsLocation(geoLocationInfo.getLocationInfo().format());
        tempInitSmsCdr.setInitCell(mobileInfoData.getCellLocation() + "-" + mobileInfoData.getIdCell());
        tempInitSmsCdr.setInitSignalLevel(mobileInfoData.getSignalLevel());
    }

    public void fillTempInitCallCdr(){
        Date now = Calendar.getInstance().getTime();
        tempInitCallCdr.setInitGpsLocation(geoLocationInfo.getLocationInfo().format());
        tempInitCallCdr.setInitCell(mobileInfoData.getCellLocation() + "-" + mobileInfoData.getIdCell());
        tempInitCallCdr.setInitSignalLevel(mobileInfoData.getSignalLevel());
        tempInitCallCdr.setInitReportDate(DateUtils.convertDateToStringCDRs(now));
    }

    public void fillTempInitCallConversation(){
        Date now = Calendar.getInstance().getTime();
        tempInitCallCdr.setInitCallDate(DateUtils.convertDateToStringCDRs(now));
    }

    //Receive Call
    public CDR createCDRfromGeneralReceivedCall(String callerNumber, AnswerVoiceCallExecutionResult executionResult, AnswerVoiceCallTaskResult taskResult)
    {
        Date now = Calendar.getInstance().getTime();
        mobileInfoData = mobileNetworkManager.getMobileInfoData();
        wifiInfoData = wifiNetworkManager.getWifiInfoData();
        CDR cdr = new CDR();

        cdr.setCDReportDate(now);
        if (taskResult==null) cdr.setTaskDate(StringUtils.EMPTY);
        else cdr.setTaskDate(DateUtils.convertDateToStringCDRs(taskResult.getStartDate()));
        cdr.setInitReportDate(tempInitCallCdr.getInitReportDate());
        cdr.setInitCallDate(tempInitCallCdr.getInitCallDate());
        cdr.setFinalCallDate(DateUtils.convertDateToStringCDRs(now));
        cdr.setFinalReportDate(DateUtils.convertDateToStringCDRs(now));
        cdr.setInitGpsLocation(tempInitCallCdr.getInitGpsLocation());
        cdr.setFinalGpsLocation(geoLocationInfo.getLocationInfo().format());
        cdr.setCaller(0);
        cdr.setOperatorName(mobileInfoData.getNetworkOperatorName());
        cdr.setOriginNumber(callerNumber);
        cdr.setDestinationNumber(mobileInfoData.getMsisdn());
        cdr.setCallType(0);
        cdr.setNetworkType(mobileNetworkManager.getCDRsConnectionMode());
        if (executionResult != null && executionResult.getResult().getStatus().contains("NOK")) {
            ///Error
            if (executionResult.getResult().getStatus().contains("Service Probe:")) {
                cdr.setNetworkError(StringUtils.EMPTY);
                cdr.setNetworkErrorMessage(StringUtils.EMPTY);
                cdr.setServiceError(executionResult.getResult().getStatus().split(":")[1]);
                cdr.setServiceErrorMessage(executionResult.getResult().getStatus().split(":")[3].substring(1));
            } else if (executionResult.getResult().getStatus().contains("Network:")) {
                cdr.setNetworkError(executionResult.getResult().getStatus().split(":")[1]);
                cdr.setNetworkErrorMessage(executionResult.getResult().getStatus().split(":")[3].substring(1));
                cdr.setServiceError(StringUtils.EMPTY);
                cdr.setServiceErrorMessage(StringUtils.EMPTY);
            } else {
                cdr.setNetworkError(executionResult.getResult().getStatus().split(":")[1]);
                cdr.setNetworkErrorMessage(executionResult.getResult().getStatus().split(":")[3].substring(1));
                cdr.setServiceError(executionResult.getResult().getStatus().split(":")[1]);
                cdr.setServiceErrorMessage(executionResult.getResult().getStatus().split(":")[3].substring(1));
            }
            cdr.setOperationSuccess(0);
        }
        else{
            cdr.setNetworkError(StringUtils.EMPTY);
            cdr.setNetworkErrorMessage(StringUtils.EMPTY);
            cdr.setServiceError(StringUtils.EMPTY);
            cdr.setServiceErrorMessage(StringUtils.EMPTY);
            cdr.setOperationSuccess(1);
        }
        if (tempInitCallCdr.getSecondCall() != null) cdr.setSecondCall(tempInitCallCdr.getSecondCall());
        else cdr.setSecondCall(0);
        if (tempInitCallCdr.getRecordCall() != null) cdr.setRecordCall(tempInitCallCdr.getRecordCall());
        else cdr.setRecordCall(0);
        cdr.setInitCell(tempInitCallCdr.getInitCell());
        cdr.setFinalCell(mobileInfoData.getCellLocation()+"-"+mobileInfoData.getIdCell());
        cdr.setInitSignalLevel(tempInitCallCdr.getInitSignalLevel());
        cdr.setFinalSignalLevel(mobileInfoData.getSignalLevel());
        if (wifiNetworkManager.isWifiAvailable()){
            cdr.setIpAddress(wifiInfoData.getIpAddress());
            cdr.setMask(wifiInfoData.getNetMask());
            cdr.setGateway(wifiInfoData.getGateway());
            cdr.setDNS1(wifiInfoData.getDns1());
            cdr.setDNS2(wifiInfoData.getDns2());
        }
        cdr.setIccid(mobileInfoData.getSimIccid());

        tempInitCallCdr = new CDR();
        return cdr;
    }

    //Make Call
    public CDR createCDRfromGeneralMakeCall(String calleNumber, MakeVoiceCallExecutionResult executionResult, MakeVoiceCallTaskResult taskResult)
    {
        Date now = Calendar.getInstance().getTime();
        mobileInfoData = mobileNetworkManager.getMobileInfoData();
        wifiInfoData = wifiNetworkManager.getWifiInfoData();
        CDR cdr = new CDR();

        cdr.setCDReportDate(now);
        if (taskResult==null) cdr.setTaskDate(StringUtils.EMPTY);
        else cdr.setTaskDate(DateUtils.convertDateToStringCDRs(taskResult.getStartDate()));
        cdr.setInitReportDate(tempInitCallCdr.getInitReportDate());
        cdr.setFinalCallDate(DateUtils.convertDateToStringCDRs(now));
        cdr.setFinalReportDate(DateUtils.convertDateToStringCDRs(now));
        cdr.setInitGpsLocation(tempInitCallCdr.getInitGpsLocation());
        cdr.setFinalGpsLocation(geoLocationInfo.getLocationInfo().format());
        cdr.setCaller(1);
        cdr.setOperatorName(mobileInfoData.getNetworkOperatorName());
        cdr.setOriginNumber(mobileInfoData.getMsisdn());
        cdr.setDestinationNumber(calleNumber);
        cdr.setCallType(0);
        cdr.setNetworkType(mobileNetworkManager.getCDRsConnectionMode());
        if (executionResult != null && executionResult.getResult().getStatus().contains("NOK")) {
            ///Error
            if (executionResult.getResult().getStatus().contains("Service Probe:")) {
                cdr.setNetworkError(StringUtils.EMPTY);
                cdr.setNetworkErrorMessage(StringUtils.EMPTY);
                cdr.setServiceError(executionResult.getResult().getStatus().split(":")[1]);
                cdr.setServiceErrorMessage(executionResult.getResult().getStatus().split(":")[3].substring(1));
            } else if (executionResult.getResult().getStatus().contains("Network:")) {
                cdr.setNetworkError(executionResult.getResult().getStatus().split(":")[1]);
                cdr.setNetworkErrorMessage(executionResult.getResult().getStatus().split(":")[3].substring(1));
                cdr.setServiceError(StringUtils.EMPTY);
                cdr.setServiceErrorMessage(StringUtils.EMPTY);
            } else {
                cdr.setNetworkError(executionResult.getResult().getStatus().split(":")[1]);
                cdr.setNetworkErrorMessage(executionResult.getResult().getStatus().split(":")[3].substring(1));
                cdr.setServiceError(executionResult.getResult().getStatus().split(":")[1]);
                cdr.setServiceErrorMessage(executionResult.getResult().getStatus().split(":")[3].substring(1));
            }
            cdr.setOperationSuccess(0);
        }
        else{
            cdr.setNetworkError(StringUtils.EMPTY);
            cdr.setNetworkErrorMessage(StringUtils.EMPTY);
            cdr.setServiceError(StringUtils.EMPTY);
            cdr.setServiceErrorMessage(StringUtils.EMPTY);
            cdr.setOperationSuccess(1);
        }
        if (tempInitCallCdr.getSecondCall() != null) cdr.setSecondCall(tempInitCallCdr.getSecondCall());
        else cdr.setSecondCall(0);
        if (tempInitCallCdr.getRecordCall() != null) cdr.setRecordCall(tempInitCallCdr.getRecordCall());
        else cdr.setRecordCall(0);
        cdr.setInitCell(tempInitCallCdr.getInitCell());
        cdr.setFinalCell(mobileInfoData.getCellLocation()+"-"+mobileInfoData.getIdCell());
        cdr.setInitSignalLevel(tempInitCallCdr.getInitSignalLevel());
        cdr.setFinalSignalLevel(mobileInfoData.getSignalLevel());
        if (wifiNetworkManager.isWifiAvailable()){
            cdr.setIpAddress(wifiInfoData.getIpAddress());
            cdr.setMask(wifiInfoData.getNetMask());
            cdr.setGateway(wifiInfoData.getGateway());
            cdr.setDNS1(wifiInfoData.getDns1());
            cdr.setDNS2(wifiInfoData.getDns2());
        }
        cdr.setIccid(mobileInfoData.getSimIccid());

        tempInitCallCdr = new CDR();
        return cdr;
    }

    //General Send SMS
    //TODO can't listen sent messages in Android in newer devices.

    //General Receive SMS
    public CDR createCDRfromGeneralReceivedSMS(String sourceNumber, String messageBody)
    {
        Date now = Calendar.getInstance().getTime();
        mobileInfoData = mobileNetworkManager.getMobileInfoData();
        wifiInfoData = wifiNetworkManager.getWifiInfoData();
        CDR cdr = new CDR();

        cdr.setCDReportDate(now);
        cdr.setTaskDate(StringUtils.EMPTY);
        cdr.setInitReportDate(DateUtils.convertDateToStringCDRs(now));
        cdr.setInitCallDate(DateUtils.convertDateToStringCDRs(now));
        cdr.setFinalCallDate(DateUtils.convertDateToStringCDRs(now));
        cdr.setFinalReportDate(DateUtils.convertDateToStringCDRs(now));
        cdr.setInitGpsLocation(geoLocationInfo.getLocationInfo().format());
        cdr.setFinalGpsLocation(geoLocationInfo.getLocationInfo().format());
        cdr.setCaller(0);
        cdr.setOperatorName(mobileInfoData.getNetworkOperatorName());
        cdr.setOriginNumber(sourceNumber);
        cdr.setDestinationNumber(mobileInfoData.getMsisdn());
        cdr.setCallType(5);
        cdr.setNetworkType(mobileNetworkManager.getCDRsConnectionMode());
        cdr.setNetworkError(StringUtils.EMPTY);
        cdr.setNetworkErrorMessage(StringUtils.EMPTY);
        cdr.setServiceError(StringUtils.EMPTY);
        cdr.setServiceErrorMessage(StringUtils.EMPTY);
        cdr.setOperationSuccess(1);
        cdr.setSecondCall(0);
        cdr.setRecordCall(0);
        cdr.setInitCell(mobileInfoData.getCellLocation()+"-"+mobileInfoData.getIdCell());
        cdr.setFinalCell(mobileInfoData.getCellLocation()+"-"+mobileInfoData.getIdCell());
        cdr.setInitSignalLevel(mobileInfoData.getSignalLevel());
        cdr.setFinalSignalLevel(mobileInfoData.getSignalLevel());
        if (wifiNetworkManager.isWifiAvailable()){
            cdr.setIpAddress(wifiInfoData.getIpAddress());
            cdr.setMask(wifiInfoData.getNetMask());
            cdr.setGateway(wifiInfoData.getGateway());
            cdr.setDNS1(wifiInfoData.getDns1());
            cdr.setDNS2(wifiInfoData.getDns2());
        }
        cdr.setSMSTimestamp((int) (System.currentTimeMillis()/1000));
        cdr.setSMSText(messageBody);
        cdr.setSMSC(StringUtils.EMPTY);
        cdr.setIccid(mobileInfoData.getSimIccid());

        return cdr;
    }


    //Task Call


    //Task Send SMS
    public CDR createCDRfromTaskSendSMS(Long deliverTime, SendSmsExecutionResult executionResult, SendSmsTaskResult taskResult)
    {
        Date now = Calendar.getInstance().getTime();
        mobileInfoData = mobileNetworkManager.getMobileInfoData();
        wifiInfoData = wifiNetworkManager.getWifiInfoData();
        CDR cdr = new CDR();

        cdr.setCDReportDate(now);
        cdr.setTaskDate(DateUtils.convertDateToStringCDRs(taskResult.getStartDate()));
        cdr.setOriginNumber(mobileInfoData.getMsisdn());
        cdr.setCaller(1);
        cdr.setCallType(5);
        cdr.setOperatorName(mobileInfoData.getNetworkOperatorName());
        cdr.setNetworkType(mobileNetworkManager.getCDRsConnectionMode());
        cdr.setInitGpsLocation(executionResult.getResult().getGpsLocation());
        cdr.setInitCell(mobileInfoData.getCellLocation() + "-" + mobileInfoData.getIdCell());
        cdr.setInitSignalLevel(mobileInfoData.getSignalLevel());
        cdr.setFinalSignalLevel(mobileInfoData.getSignalLevel());
        cdr.setFinalGpsLocation(executionResult.getResult().getGpsLocation());
        cdr.setFinalCell(mobileInfoData.getCellLocation() + "-" + mobileInfoData.getIdCell());

        if (!executionResult.getResult().getStatus().contains("NOK")) {
            cdr.setDestinationNumber(taskResult.getDestinationNumber());
            cdr.setInitReportDate(DateUtils.convertDateToStringCDRs(new Date(deliverTime)));
            cdr.setInitCallDate(DateUtils.convertDateToStringCDRs(new Date(deliverTime)));
            cdr.setFinalCallDate(DateUtils.convertDateToStringCDRs(new Date(deliverTime)));
            cdr.setFinalReportDate(DateUtils.convertDateToStringCDRs(new Date(deliverTime)));
            cdr.setNetworkError(StringUtils.EMPTY);
            cdr.setNetworkErrorMessage(StringUtils.EMPTY);
            cdr.setServiceError(StringUtils.EMPTY);
            cdr.setServiceErrorMessage(StringUtils.EMPTY);
            cdr.setOperationSuccess(1);
            cdr.setSMSTimestamp((int) (System.currentTimeMillis() / 1000));
            cdr.setSMSText(taskResult.getMessageText());
        }
        else {
            cdr.setDestinationNumber(StringUtils.EMPTY);
            cdr.setInitReportDate(StringUtils.EMPTY);
            cdr.setInitCallDate(StringUtils.EMPTY);
            cdr.setFinalCallDate(StringUtils.EMPTY);
            cdr.setFinalReportDate(StringUtils.EMPTY);
            cdr.setOperationSuccess(0);
            cdr.setSMSTimestamp(0);
            cdr.setSMSText(StringUtils.EMPTY);

            ///Error
            if (executionResult.getResult().getStatus().contains("Service Probe:")) {
                cdr.setNetworkError(StringUtils.EMPTY);
                cdr.setNetworkErrorMessage(StringUtils.EMPTY);
                cdr.setServiceError(executionResult.getResult().getStatus().split(":")[1]);
                cdr.setServiceErrorMessage(executionResult.getResult().getStatus().split(":")[3].substring(1));
            } else if (executionResult.getResult().getStatus().contains("Network:")) {
                cdr.setNetworkError(executionResult.getResult().getStatus().split(":")[1]);
                cdr.setNetworkErrorMessage(executionResult.getResult().getStatus().split(":")[3].substring(1));
                cdr.setServiceError(StringUtils.EMPTY);
                cdr.setServiceErrorMessage(StringUtils.EMPTY);
            } else {
                cdr.setNetworkError(executionResult.getResult().getStatus().split(":")[1]);
                cdr.setNetworkErrorMessage(executionResult.getResult().getStatus().split(":")[3].substring(1));
                cdr.setServiceError(executionResult.getResult().getStatus().split(":")[1]);
                cdr.setServiceErrorMessage(executionResult.getResult().getStatus().split(":")[3].substring(1));
            }
        }

        cdr.setSecondCall(0);
        cdr.setRecordCall(0);
        cdr.setSMSC(StringUtils.EMPTY);
        cdr.setSMSMultipartParameters(StringUtils.EMPTY);
        if (wifiNetworkManager.isWifiAvailable()) {
            cdr.setIpAddress(wifiInfoData.getIpAddress());
            cdr.setMask(wifiInfoData.getNetMask());
            cdr.setGateway(wifiInfoData.getGateway());
            cdr.setDNS1(wifiInfoData.getDns1());
            cdr.setDNS2(wifiInfoData.getDns2());
        }
        cdr.setIccid(mobileInfoData.getSimIccid());

        return cdr;
    }

    //Task Receive SMS
    public CDR createCDRfromTaskReceivedSMS(Long receivedTime, ReceiveSmsExecutionResult executionResult, ReceiveSmsTaskResult taskResult)
    {
        Date now = Calendar.getInstance().getTime();
        mobileInfoData = mobileNetworkManager.getMobileInfoData();
        wifiInfoData = wifiNetworkManager.getWifiInfoData();
        CDR cdr = new CDR();

        cdr.setCDReportDate(now);
        cdr.setTaskDate(DateUtils.convertDateToStringCDRs(taskResult.getStartDate()));
        cdr.setDestinationNumber(mobileInfoData.getMsisdn());
        cdr.setCallType(5);
        cdr.setOperatorName(mobileInfoData.getNetworkOperatorName());
        cdr.setNetworkType(mobileNetworkManager.getCDRsConnectionMode());
        cdr.setInitGpsLocation(tempInitSmsCdr.getInitGpsLocation());
        cdr.setInitCell(tempInitSmsCdr.getInitCell());
        cdr.setInitSignalLevel(tempInitSmsCdr.getInitSignalLevel());
        cdr.setFinalSignalLevel(mobileInfoData.getSignalLevel());
        cdr.setFinalGpsLocation(executionResult.getResult().getGpsLocation());
        cdr.setFinalCell(mobileInfoData.getCellLocation() + "-" + mobileInfoData.getIdCell());

        if (!executionResult.getResult().getStatus().contains("NOK")) {
            cdr.setInitReportDate(DateUtils.convertDateToStringCDRs(new Date(receivedTime)));
            cdr.setInitCallDate(DateUtils.convertDateToStringCDRs(new Date(receivedTime)));
            cdr.setFinalCallDate(DateUtils.convertDateToStringCDRs(new Date(receivedTime)));
            cdr.setFinalReportDate(DateUtils.convertDateToStringCDRs(new Date(receivedTime)));
            cdr.setOriginNumber(taskResult.getSourceNumber());
            cdr.setNetworkError(StringUtils.EMPTY);
            cdr.setNetworkErrorMessage(StringUtils.EMPTY);
            cdr.setServiceError(StringUtils.EMPTY);
            cdr.setServiceErrorMessage(StringUtils.EMPTY);
            cdr.setOperationSuccess(1);
            cdr.setSMSTimestamp((int) (System.currentTimeMillis() / 1000));
            cdr.setSMSText(taskResult.getMessageText());

        }
        else {
            cdr.setInitReportDate(StringUtils.EMPTY);
            cdr.setInitCallDate(StringUtils.EMPTY);
            cdr.setFinalCallDate(StringUtils.EMPTY);
            cdr.setFinalReportDate(StringUtils.EMPTY);
            cdr.setOriginNumber(StringUtils.EMPTY);

            cdr.setOperationSuccess(0);
            cdr.setSMSTimestamp(0);
            cdr.setSMSText(StringUtils.EMPTY);

            ///Error
            if (executionResult.getResult().getStatus().contains("Service Probe:")) {
                cdr.setNetworkError(StringUtils.EMPTY);
                cdr.setNetworkErrorMessage(StringUtils.EMPTY);
                cdr.setServiceError(executionResult.getResult().getStatus().split(":")[1]);
                cdr.setServiceErrorMessage(executionResult.getResult().getStatus().split(":")[3].substring(1));
            } else if (executionResult.getResult().getStatus().contains("Network:")) {
                cdr.setNetworkError(executionResult.getResult().getStatus().split(":")[1]);
                cdr.setNetworkErrorMessage(executionResult.getResult().getStatus().split(":")[3].substring(1));
                cdr.setServiceError(StringUtils.EMPTY);
                cdr.setServiceErrorMessage(StringUtils.EMPTY);
            } else {
                cdr.setNetworkError(executionResult.getResult().getStatus().split(":")[1]);
                cdr.setNetworkErrorMessage(executionResult.getResult().getStatus().split(":")[3].substring(1));
                cdr.setServiceError(executionResult.getResult().getStatus().split(":")[1]);
                cdr.setServiceErrorMessage(executionResult.getResult().getStatus().split(":")[3].substring(1));

            }
        }
        cdr.setCaller(0);
        cdr.setSecondCall(0);
        cdr.setRecordCall(0);
        cdr.setSMSC(StringUtils.EMPTY);
        cdr.setSMSMultipartParameters(StringUtils.EMPTY);
        if (wifiNetworkManager.isWifiAvailable()) {
            cdr.setIpAddress(wifiInfoData.getIpAddress());
            cdr.setMask(wifiInfoData.getNetMask());
            cdr.setGateway(wifiInfoData.getGateway());
            cdr.setDNS1(wifiInfoData.getDns1());
            cdr.setDNS2(wifiInfoData.getDns2());
        }
        cdr.setIccid(mobileInfoData.getSimIccid());

        tempInitSmsCdr = new CDR();
        return cdr;
    }
}
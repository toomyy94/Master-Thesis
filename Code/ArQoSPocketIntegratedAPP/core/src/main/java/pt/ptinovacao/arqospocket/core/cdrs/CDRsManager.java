package pt.ptinovacao.arqospocket.core.cdrs;

import com.google.gson.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.http.client.HttpClient;
import pt.ptinovacao.arqospocket.core.http.client.RemoteServiceUrlManager;
import pt.ptinovacao.arqospocket.core.http.client.response.ProbeNotificationResponse;
import pt.ptinovacao.arqospocket.core.keepalive.KeepAliveManager;
import pt.ptinovacao.arqospocket.core.settings.SharedPreferencesManager;
import pt.ptinovacao.arqospocket.core.tests.data.results.AnswerVoiceCallTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.MakeVoiceCallTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.ReceiveSmsTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.RecordAudioTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.SendSmsTaskResult;
import pt.ptinovacao.arqospocket.core.tests.results.AnswerVoiceCallExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.results.MakeVoiceCallExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.results.ReceiveSmsExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.results.RecordAudioExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.results.SendSmsExecutionResult;
import pt.ptinovacao.arqospocket.persistence.CDRDao;
import pt.ptinovacao.arqospocket.persistence.ExecutingEventDao;
import pt.ptinovacao.arqospocket.persistence.models.CDR;

/**
 * Manager for the cdrs.
 * <p>
 * Created by Tomás Rodrigues on 13-09-2017.
 */
public class CDRsManager {
    private final static Logger LOGGER = LoggerFactory.getLogger(CDRsManager.class);
    public final CoreApplication application;

    // instances
    public AnswerVoiceCallExecutionResult answerExecutionResult;
    public AnswerVoiceCallTaskResult answerTaskResult;
    public MakeVoiceCallExecutionResult makeExecutionResult;
    public MakeVoiceCallTaskResult makeTaskResult;
    public RecordAudioExecutionResult recordExecutionResult;
    public RecordAudioTaskResult recordTaskResult;
    private static CDRsManager instance;
    private CDRUtils cdrUtils;

    //database
    private final CDRDao cdrDao;
    private CDRsParser cdrParser = new CDRsParser();


    public CDRsManager(CoreApplication application) {
        this.application = application;
        cdrDao = application.getDatabaseHelper().createCDRDao();
        cdrUtils = CDRUtils.getInstance(application);
    }

    public static CDRsManager getInstance(CoreApplication application) {
        if (instance == null) {
            instance = new CDRsManager(application);
        }
        return instance;
    }

    public void fillAnswerVoiceTaskResults(AnswerVoiceCallExecutionResult executionResult, AnswerVoiceCallTaskResult taskResult){
        this.answerExecutionResult = executionResult;
        this.answerTaskResult = taskResult;
    }

    public void fillMakeVoiceTaskResults(MakeVoiceCallExecutionResult executionResult, MakeVoiceCallTaskResult taskResult){
        this.makeExecutionResult = executionResult;
        this.makeTaskResult = taskResult;
    }


    public void fillRecordAudioTaskResults(RecordAudioExecutionResult executionResult, RecordAudioTaskResult taskResult){
        this.recordExecutionResult = executionResult;
        this.recordTaskResult = taskResult;
    }

    public void generateCDRReceivedSMS(String sourceNumber, String messageBody) {
        final String method = "generateCDRReceivedSMS";
        CDR cdr = cdrUtils.createCDRfromGeneralReceivedSMS(sourceNumber, messageBody);

        reportCDR(cdr);
    }

    public void generateCDRSendSMS(Long deliverTime, SendSmsExecutionResult executionResult, SendSmsTaskResult taskResult) {
        final String method = "generateCDRSendSMS";
        CDR cdr = cdrUtils.createCDRfromTaskSendSMS(deliverTime, executionResult, taskResult);

        reportCDR(cdr);
    }

    public void generateCDRReceivedSMS(Long receivedTime, ReceiveSmsExecutionResult executionResult, ReceiveSmsTaskResult taskResult) {
        final String method = "generateCDRReceivedSMS";
        CDR cdr = cdrUtils.createCDRfromTaskReceivedSMS(receivedTime, executionResult, taskResult);

        reportCDR(cdr);
    }

    public void generateCDRReceivedCall(String callerNumber) {
        final String method = "generateCDRReceivedCall";
        CDR cdr = cdrUtils.createCDRfromGeneralReceivedCall(callerNumber, answerExecutionResult, answerTaskResult);

        reportCDR(cdr);
    }

    public void generateCDRMakeCall(String calleNumber){
        final String method = "generateCDRMakeCall";
        CDR cdr = cdrUtils.createCDRfromGeneralMakeCall(calleNumber, makeExecutionResult, makeTaskResult);

        reportCDR(cdr);
    }


    public void reportCDR(CDR cdr) {
        cdr.setReported(false);
        cdr.setNotificationSent(false);

        LOGGER.debug(cdr.toString());
        cdrDao.saveCDR(cdr).subscribe(new Consumer<CDR>() {
            @Override
            public void accept(@NonNull CDR cdr) throws Exception {
                LOGGER.debug("Created cdr");
            }
        });

        startSendNotification();
    }

    public void startSendNotification(){
        if (SharedPreferencesManager.getInstance(application).getConnectionWithMSManual()) {
            ExecutingEventDao executingEventDao = application.getDatabaseHelper().createExecutingEventDao();
            final List<CDR> pendingCDRs = executingEventDao.readAllCDRsToSendNotification();

            deliverCDRs(pendingCDRs);
        }
    }

    void deliverCDRs(final List<CDR> pendingCDRs) {
        Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                processAndDeliverCDRs(pendingCDRs);
                return true;
            }
        }).subscribeOn(Schedulers.newThread()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean result) throws Exception {
                LOGGER.debug("Result sent to server");
            }
        });
    }

    private void processAndDeliverCDRs(List<CDR> pendingCDRs) {
        if (pendingCDRs.size() == 0) {
            return;
        }

        sendCDRs(pendingCDRs);
    }

    private void sendCDRs(List<CDR> pendingCDRs) {
        LOGGER.debug("Sending {} CDRs:", pendingCDRs.size());
        ExecutingEventDao executingEventDao = application.getDatabaseHelper().createExecutingEventDao();

        JsonObject data = cdrParser.CDRsToStrings(pendingCDRs);

        HttpClient client = new HttpClient(application);
        ProbeNotificationResponse response =
                client.postProbeNotificationResultCDRs(data, RemoteServiceUrlManager.getInstance(application).urlCDRsProcess());

        LOGGER.debug("Received response: [{}] {}", response.getCode(), response.getEntity());
        if (response.isSuccess()) {
            executingEventDao.updateAllCDRsThatPostNotification(getIds(pendingCDRs));
            KeepAliveManager.sendBroadcastReceiver(application);
        }
    }

    private ArrayList<Long> getIds(List<CDR> pendingCDRs) {
        ArrayList<Long> cdrsIds = new ArrayList<>();
        for (CDR cdr : pendingCDRs) {
            cdrsIds.add(cdr.getId());
        }
        return cdrsIds;
    }

    public void deliverPendingCDRs() {
        ExecutingEventDao executingEventDao = application.getDatabaseHelper().createExecutingEventDao();
        processAndDeliverCDRs(executingEventDao.readAllCDRsToSendNotification());
    }
}

package pt.ptinovacao.arqospocket.core;

import android.support.annotation.NonNull;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pt.ptinovacao.arqospocket.core.http.client.HttpClient;
import pt.ptinovacao.arqospocket.core.http.client.RemoteServiceUrlManager;
import pt.ptinovacao.arqospocket.core.http.client.response.ProbeNotificationResponse;
import pt.ptinovacao.arqospocket.core.keepalive.KeepAliveManager;
import pt.ptinovacao.arqospocket.core.tests.data.TestResult;
import pt.ptinovacao.arqospocket.persistence.ExecutingEventDao;
import pt.ptinovacao.arqospocket.persistence.models.ExecutingEvent;

/**
 * Manager to deliver results to the management service.
 * <p>
 * Created by Emílio Simões on 12-05-2017.
 */
public class ResultsManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResultsManager.class);

    private static ResultsManager instance;

    private CoreApplication application;

    private List<ExecutingEvent> pendingEvents;

    private ResultsManager(CoreApplication application) {
        this.application = application;
    }

    public synchronized static ResultsManager getInstance(CoreApplication application) {
        if (instance == null) {
            instance = new ResultsManager(application);
        }
        return instance;
    }

    void deliverSingleResult(final ExecutingEvent event) {
        Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                List<ExecutingEvent> events = new ArrayList<>();
                events.add(event);
                processAndDeliverResults(events);
                return true;
            }
        }).subscribeOn(Schedulers.newThread()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Boolean result) throws Exception {
                LOGGER.debug("Result sent to server");
            }
        });
    }

    public void deliverPendingResults() {
        pendingEvents = new ArrayList<>();
        ExecutingEventDao executingEventDao = application.getDatabaseHelper().createExecutingEventDao();
        executingEventDao.readPendingExecutedEvents().subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<ExecutingEvent>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull ExecutingEvent executingEvent)
                            throws Exception {
                        LOGGER.debug("Found not reported executing event: {}", executingEvent.getId());
                        pendingEvents.add(executingEvent);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        LOGGER.error("Error getting executing events", throwable);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        LOGGER.debug("All executing events have been read: {}", pendingEvents.size());
                        List<ExecutingEvent> events = new ArrayList<>(pendingEvents);
                        pendingEvents.clear();
                        pendingEvents = null;
                        processAndDeliverResults(events);
                    }
                });
    }

    private void processAndDeliverResults(List<ExecutingEvent> events) {
        if (events.size() == 0) {
            return;
        }
        List<TestResult> testResults = parseTestResults(events);
        sendResults(testResults, events);

        events.clear();
    }

    @NonNull
    private List<TestResult> parseTestResults(List<ExecutingEvent> events) {
        List<TestResult> testResults = new ArrayList<>();
        TestParser parser = new TestParser();

        for (ExecutingEvent event : events) {
            String resultData = Strings.nullToEmpty(event.getResultData());
            if (resultData.length() == 0) {
                LOGGER.debug("Test does not have a result, ignoring");
                continue;
            }

            TestResult result;
            try {
                result = parser.parseSingleResult(resultData);
            } catch (Exception e) {
                LOGGER.error("Error parsing result", e);
                continue;
            }

            if (result != null) {
                testResults.add(result);
            }
        }
        return testResults;
    }

    private void sendResults(List<TestResult> testResults, List<ExecutingEvent> events) {
        LOGGER.debug("Sending results: {}", testResults.size());

        HttpClient client = new HttpClient(application);
        ProbeNotificationResponse response =
                client.postProbeNotificationResultTests(testResults, RemoteServiceUrlManager.getInstance(application).urlResultProcess());

        LOGGER.debug("Received response: [{}] {}", response.getCode(), response.getEntity());
        if (response.isSuccess()) {
            final ExecutingEventDao executingEventDao = application.getDatabaseHelper().createExecutingEventDao();
            executingEventDao.flagTestsAsReported(events);
            KeepAliveManager.sendBroadcastReceiver(application);
        }
    }
}

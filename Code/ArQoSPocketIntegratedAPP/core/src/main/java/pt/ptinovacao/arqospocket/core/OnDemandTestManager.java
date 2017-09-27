package pt.ptinovacao.arqospocket.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import pt.ptinovacao.arqospocket.core.producers.TestProducer;
import pt.ptinovacao.arqospocket.core.tests.ExecutableTest;
import pt.ptinovacao.arqospocket.core.tests.data.TestData;
import pt.ptinovacao.arqospocket.persistence.OnDemandEventDao;
import pt.ptinovacao.arqospocket.persistence.models.OnDemandEvent;

/**
 * On demand test manager. Provides a common interface to access and execute test on user request.
 * <p>
 * Created by Emílio Simões on 13-04-2017.
 */
public class OnDemandTestManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(OnDemandTestManager.class);

    private static OnDemandTestManager instance;

    private final CoreApplication application;

    private OnDemandTestManager(CoreApplication application) {
        this.application = application;
    }

    synchronized static OnDemandTestManager getInstance(CoreApplication application) {
        if (instance == null) {
            instance = new OnDemandTestManager(application);
        }
        return instance;
    }

    /**
     * Executes a test on demand.
     *
     * @param testId the database test ID of the test to execute.
     * @param scheduled if is a scheduled test or a user defined test.
     */
    public void executeOnDemand(long testId, boolean scheduled) {
        TestProducer.getOnDemandInstance().executeOnDemand(testId, scheduled);
    }

    void finishOnDemandTestExecution(String testId) {
        OnDemandEventDao onDemandEventDao = application.getDatabaseHelper().createOnDemandEventDao();
        onDemandEventDao.endOnDemandEventExecuting(testId).subscribe(new Consumer<OnDemandEvent>() {
            @Override
            public void accept(@NonNull OnDemandEvent onDemandEvent) throws Exception {
                LOGGER.debug("On demand event executing state cancelled: {}",
                        onDemandEvent.getId() > 0 ? "done!" : "not on demand");
            }
        });
    }

    void saveUserExecutableTest(ExecutableTest executableTest) {
        OnDemandEventDao onDemandEventDao = application.getDatabaseHelper().createOnDemandEventDao();

        TestData data = executableTest.getData();
        String testData = getTestDataHasString(data);

        OnDemandEvent onDemandEvent = new OnDemandEvent();
        onDemandEvent.setTestData(testData);
        onDemandEvent.setTestId(data.getTestId());
        onDemandEventDao.saveOnDemandEvent(onDemandEvent).subscribe(new Consumer<OnDemandEvent>() {
            @Override
            public void accept(@NonNull OnDemandEvent onDemandEvent) throws Exception {
                LOGGER.debug("On demand test persisted on DB: {}", onDemandEvent.getId() > 0);
            }
        });
    }

    boolean clearOnDemandTest(TestData data) {
        OnDemandEventDao onDemandEventDao = application.getDatabaseHelper().createOnDemandEventDao();

        return onDemandEventDao.deleteTestByTestId(data.getTestId());
    }

    private String getTestDataHasString(TestData data) {
        TestParser parser = new TestParser();
        return parser.stringify(data, false);
    }
}

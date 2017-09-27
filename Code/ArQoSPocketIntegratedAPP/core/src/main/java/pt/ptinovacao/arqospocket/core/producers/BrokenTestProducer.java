package pt.ptinovacao.arqospocket.core.producers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.TestConsumer;
import pt.ptinovacao.arqospocket.core.TestExecutor;
import pt.ptinovacao.arqospocket.core.TestParser;
import pt.ptinovacao.arqospocket.core.tests.data.TestData;
import pt.ptinovacao.arqospocket.persistence.ExecutingEventDao;
import pt.ptinovacao.arqospocket.persistence.OnDemandEventDao;
import pt.ptinovacao.arqospocket.persistence.models.ExecutingEvent;
import pt.ptinovacao.arqospocket.persistence.models.OnDemandEvent;

/**
 * Validates broken tests (tests that didn't finish execution) and restores the recursive tests.
 * <p>
 * Created by Emílio Simões on 08-05-2017.
 */
class BrokenTestProducer extends TestProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BrokenTestProducer.class);

    /**
     * Constructor for the test producer.
     *
     * @param application the application to access the global context.
     * @param consumer the consumer to consume the tests.
     */
    BrokenTestProducer(CoreApplication application, TestConsumer consumer) {
        super(application, consumer);
    }

    @Override
    public void executeOneShot() {
        LOGGER.debug("Executing broken test producer");

        Single.just("Executing tests").subscribeOn(Schedulers.newThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull String name) throws Exception {
                processExecutingEvents();
            }
        });

        Single.just("On demand tests").subscribeOn(Schedulers.newThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull String name) throws Exception {
                cleanOnDemandEventFlags();
            }
        });
    }

    private void processExecutingEvents() {
        ExecutingEventDao executingEventDao = getApplication().getDatabaseHelper().createExecutingEventDao();
        List<ExecutingEvent> executingEvents = executingEventDao.listAllExecutingEvents();
        TestParser parser = new TestParser();

        for (ExecutingEvent event : executingEvents) {
            LOGGER.debug("Validating broken test {}", event.getId());
            if (TestExecutor.getInstance(getApplication()).isTestExecuting(event.getId())) {
                LOGGER.debug("Test is in executor, ignoring");
                continue;
            }

            TestData data = parser.parseSingleTest(event.getTestData());

            TestConsumer.getInstance(getApplication()).validateAfterExecution(data);

            executingEventDao.flagTestAsExecuted(event.getId(), event.getResultData(), false).blockingGet();
        }
    }

    private void cleanOnDemandEventFlags() {
        OnDemandEventDao onDemandEventDao = getApplication().getDatabaseHelper().createOnDemandEventDao();
        List<OnDemandEvent> onDemandEvents = onDemandEventDao.listAllExecutingOnDemandEvents();

        for (OnDemandEvent event : onDemandEvents) {
            LOGGER.debug("Validating broken on demand test {}", event.getId());
            onDemandEventDao.setOnDemandEventExecuting(event.getId(), false).blockingGet();
        }
    }

    @Override
    void start() {
    }

    @Override
    void stop() {
    }
}

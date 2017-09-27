package pt.ptinovacao.arqospocket.core.producers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.TestConsumer;
import pt.ptinovacao.arqospocket.core.TestParser;
import pt.ptinovacao.arqospocket.core.tests.TestExecutionType;
import pt.ptinovacao.arqospocket.core.tests.data.TestData;
import pt.ptinovacao.arqospocket.persistence.OnDemandEventDao;
import pt.ptinovacao.arqospocket.persistence.ScheduledEventDao;
import pt.ptinovacao.arqospocket.persistence.models.OnDemandEvent;
import pt.ptinovacao.arqospocket.persistence.models.ScheduledEvent;

/**
 * Test producer that will register for broadcasts and feed the received tests to the consumer.
 * <p>
 * Created by Emílio Simões on 06-04-2017.
 */

class OnDemandTestProducer extends TestProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OnDemandTestProducer.class);

    /**
     * Constructor for the test producer.
     *
     * @param application the application to access the global context.
     * @param consumer the consumer to consume the tests.
     */
    OnDemandTestProducer(CoreApplication application, TestConsumer consumer) {
        super(application, consumer);
    }

    @Override
    public void executeOnDemand(long testId, boolean scheduled) {
        LOGGER.debug("On demand test execution requested for test with ID {} [{}]", testId,
                scheduled ? "scheduler" : "user request");
        if (scheduled) {
            executeScheduledTest(testId);
        } else {
            executeUserTest(testId);
        }
    }

    private void executeUserTest(final long testId) {
        OnDemandEventDao onDemandEventDao = getApplication().getDatabaseHelper().createOnDemandEventDao();
        onDemandEventDao.readOnDemandEvent(testId).subscribe(new Consumer<OnDemandEvent>() {
            @Override
            public void accept(@NonNull OnDemandEvent onDemandEvent) throws Exception {
                TestParser parser = new TestParser();
                TestData data = parser.parseSingleTest(onDemandEvent.getTestData());
                data.setStartDate(Calendar.getInstance().getTime());
                getConsumer().consume(data, TestExecutionType.USER_REQUEST);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                LOGGER.warn("The test with ID = {} does not exist in the database", testId);
            }
        });
        onDemandEventDao.startOnDemandEventExecuting(testId).subscribe(new Consumer<OnDemandEvent>() {
            @Override
            public void accept(@NonNull OnDemandEvent onDemandEvent) throws Exception {
                LOGGER.debug("On demand event set has executing: {}", onDemandEvent.getId());
            }
        });
    }

    private void executeScheduledTest(final long testId) {
        final ScheduledEventDao scheduledEventDao = getApplication().getDatabaseHelper().createScheduledEventDao();
        scheduledEventDao.readScheduledEvent(testId).subscribe(new Consumer<ScheduledEvent>() {
            @Override
            public void accept(@NonNull ScheduledEvent scheduledEvent) throws Exception {
                TestParser parser = new TestParser();
                TestData data = parser.parseSingleTest(scheduledEvent.getTestData());
                getConsumer().consume(data, TestExecutionType.ALARM_REQUEST);
                scheduledEventDao.deleteExecutedEvent(testId).single().subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        LOGGER.debug("Deleted executed test: {}", integer);
                    }
                });
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                LOGGER.warn("The test with ID = {} does not exist in the database", testId);
            }
        });
    }

    @Override
    void start() {
    }

    @Override
    void stop() {
    }
}

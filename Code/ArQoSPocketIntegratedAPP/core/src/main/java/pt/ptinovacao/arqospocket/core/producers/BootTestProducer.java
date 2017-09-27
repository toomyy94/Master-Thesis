package pt.ptinovacao.arqospocket.core.producers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.TestConsumer;
import pt.ptinovacao.arqospocket.core.TestParser;
import pt.ptinovacao.arqospocket.core.tests.TestExecutionType;
import pt.ptinovacao.arqospocket.core.tests.data.TestData;
import pt.ptinovacao.arqospocket.persistence.ScheduledEventDao;
import pt.ptinovacao.arqospocket.persistence.models.ScheduledEvent;

/**
 * Producer that is executed on boot to restore scheduled alarms.
 * <p>
 * Created by Emílio Simões on 18-04-2017.
 */
class BootTestProducer extends TestProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BootTestProducer.class);

    /**
     * Constructor for the test producer.
     *
     * @param application the application to access the global context.
     * @param consumer the consumer to consume the tests.
     */
    BootTestProducer(CoreApplication application, TestConsumer consumer) {
        super(application, consumer);
    }

    @Override
    public void executeOneShot() {
        LOGGER.debug("Executing boot test producer");

        ScheduledEventDao scheduledEventDao = getApplication().getDatabaseHelper().createScheduledEventDao();
        List<ScheduledEvent> scheduledEvents = scheduledEventDao.readAllScheduledEvents().toList();

        if (scheduledEvents.size() == 0) {
            LOGGER.debug("No schedule events exist, aborting boot restore");
            return;
        }

        List<Long> expiredEvents = new ArrayList<>();
        long now = Calendar.getInstance().getTime().getTime();

        for (ScheduledEvent event : scheduledEvents) {
            if (event.getStartDate() <= now) {
                if (!hasNextIteration(event)) {
                    LOGGER.debug("Test has expired: {}", event.getId());
                    expiredEvents.add(event.getId());
                } else {
                    LOGGER.debug("Test has next iteration: {}", event.getId());
                }
            } else {
                LOGGER.debug("Scheduling test: {}", event.getId());
                TestParser parser = new TestParser();
                TestData data = parser.parseSingleTest(event.getTestData());
                getConsumer().consume(data, TestExecutionType.RESCHEDULE);
            }
        }

        if (expiredEvents.size() > 0) {
            Integer count = scheduledEventDao.deleteExpiredEvents(expiredEvents).single().blockingGet();
            LOGGER.debug("Deleted {} expired events", count);
        }
    }

    private boolean hasNextIteration(ScheduledEvent event) {
        TestParser parser = new TestParser();
        TestData testData = parser.parseSingleTest(event.getTestData());
        return getConsumer().validateAfterExecution(testData);
    }

    @Override
    void start() {
    }

    @Override
    void stop() {
    }
}

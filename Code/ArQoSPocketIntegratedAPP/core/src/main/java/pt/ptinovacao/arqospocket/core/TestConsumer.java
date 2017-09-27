package pt.ptinovacao.arqospocket.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import pt.ptinovacao.arqospocket.core.tests.TestExecutionType;
import pt.ptinovacao.arqospocket.core.tests.data.TestData;

/**
 * Consumes the tests produced by the test producer. In an optimal implementation we will only have a single consumer
 * for multiple producers. Ideally the consumer will be the test scheduler.
 * <p>
 * Created by Emílio Simões on 06-04-2017.
 */
public abstract class TestConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestConsumer.class);

    private static TestConsumer instance;

    private CoreApplication application;

    /**
     * Constructor for the test producer.
     *
     * @param application the application to access the global context.
     */
    TestConsumer(CoreApplication application) {
        this.application = application;
    }

    /**
     * Gets the instance of the test consumer.
     *
     * @param application the application to get the context.
     * @return the instance of the test consumer.
     */
    public synchronized static TestConsumer getInstance(CoreApplication application) {
        if (instance == null) {
            instance = new TestScheduler(application);
        }
        return instance;
    }

    /**
     * Accepts a single test and processes it.
     *
     * @param data the test data of the test to consume.
     * @param executionType the execution type.
     */
    public abstract void consume(TestData data, TestExecutionType executionType);

    /**
     * Cancel a single test and processes it.
     *
     * @param data the test data of the test to consume.
     */
    public abstract void cancel(TestData data);

    /**
     * Accepts multiple tests and processes them all.
     *
     * @param tests the tests to consume.
     * @param executionType the execution type.
     */
    public void consumeAll(Collection<TestData> tests, TestExecutionType executionType) {
        if (tests == null) {
            LOGGER.debug("Ignoring empty test batch");
            return;
        }

        LOGGER.debug("Consumer receiving test batch => {}", tests.size());
        for (TestData test : tests) {
            consume(test, executionType);
        }
    }

    /**
     * Cancel multiple tests and processes them all.
     *
     * @param tests the tests to consume.
     */
    public void cancelAll(Collection<TestData> tests) {
        if (tests == null) {
            LOGGER.debug("Ignoring empty test batch");
            return;
        }

        LOGGER.debug("Consumer receiving test batch => {}", tests.size());
        for (TestData test : tests) {
            cancel(test);
        }
    }

    /**
     * Validates a test after execution and checks if it needs to be .
     *
     * @param data the test data to validate.
     * @return {@code true} if the test is rescheduled, {@code false} if the test as expired and been cancelled.
     */
    public abstract boolean validateAfterExecution(TestData data);

    /**
     * Gets the application.
     *
     * @return the application.
     */
    protected CoreApplication getApplication() {
        return application;
    }
}

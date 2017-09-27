package pt.ptinovacao.arqospocket.core.producers;

import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.TestConsumer;

/**
 * Test producers supply the application with new tests to execute. A producer can feed tests from different sources
 * like a JSON file, a Web source or an Android Service started by a broadcast receiver.
 * <p>
 * Created by Emílio Simões on 06-04-2017.
 */
public abstract class TestProducer {

    private CoreApplication application;

    private TestConsumer consumer;

    private static TestProducer[] producers;

    /**
     * Constructor for the test producer.
     *
     * @param application the application to access the global context.
     * @param consumer the consumer to consume the tests.
     */
    TestProducer(CoreApplication application, TestConsumer consumer) {
        this.application = application;
        this.consumer = consumer;
    }

    /**
     * Starts all the available producers registering with then the default consumer.
     *
     * @param application the application to get the context.
     */
    public static void startProducers(CoreApplication application) {
        TestConsumer consumer = TestConsumer.getInstance(application);
        if (producers == null) {
            producers = new TestProducer[] {
                    new OnDemandTestProducer(application, consumer),
                    new BootTestProducer(application, consumer),
                    new BrokenTestProducer(application, consumer),
                    new HttpTestProducer(application, consumer),
                    new JsonTestProducer(application, consumer)
            };
        }
        for (TestProducer producer : producers) {
            producer.start();
        }
    }

    /**
     * Clean all the producers.
     */
    public static void cleanUpProducers() {
        if (producers != null) {
            for (TestProducer producer : producers) {
                producer.stop();
            }
        }
    }

    /**
     * Gets the default producer that can be used for on demand test executions.
     *
     * @return the default test producer.
     */
    public static TestProducer getOnDemandInstance() {
        return producers[0];
    }

    /**
     * Gets the producer that should be executed on boot to restore the alarms for scheduled tests.
     *
     * @return the boot producer.
     */
    public static TestProducer getBootInstance() {
        return producers[1];
    }

    /**
     * Gets the producer that should be executed on application start to restore recursive tests and clean broken tests.
     *
     * @return the boot producer.
     */
    public static TestProducer getBrokenTestInstance() {
        return producers[2];
    }

    /**
     * Executes a test on demand, if supported.
     *
     * @param testId the database test ID of the test to execute.
     * @param scheduled if is a scheduled test or a user defined test.
     */
    public void executeOnDemand(long testId, boolean scheduled) {
        throw new UnsupportedOperationException("This instance does not support on demand test execution");
    }

    /**
     * On shot execution method for producers that only need (or can) run once in the application lifecycle, like the
     * boot producer that can only run once the device boots. <strong>This method must only be overridden by one shot
     * producers.</strong>
     */
    public void executeOneShot() {
        throw new UnsupportedOperationException("This instance does not support one shot execution");
    }

    /**
     * Start the test producer. Once started the producer will start listening for tests and feed the consumer with
     * new tests.
     */
    abstract void start();

    /**
     * Stops the producer cleaning up dependencies.
     */
    abstract void stop();

    /**
     * Gets the application.
     *
     * @return the application.
     */
    protected CoreApplication getApplication() {
        return application;
    }

    /**
     * Gets the associated consumer.
     *
     * @return the associate consumer.
     */
    TestConsumer getConsumer() {
        return consumer;
    }
}

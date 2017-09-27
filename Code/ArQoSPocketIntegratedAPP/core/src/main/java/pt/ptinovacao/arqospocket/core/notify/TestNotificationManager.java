package pt.ptinovacao.arqospocket.core.notify;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

import pt.ptinovacao.arqospocket.core.CoreApplication;

/**
 * Helper class to manage notifications from the engine to the UI.
 * <p>
 * Created by Emílio Simões on 18-04-2017.
 */
public class TestNotificationManager {

    private static TestNotificationManager instance;

    private final CoreApplication application;

    static final String ACTION_TEST_STARTED = "pt.ptinovacao.arqospocket.core.notify.action.TEST_STARTED";

    static final String ACTION_TASK_STARTED = "pt.ptinovacao.arqospocket.core.notify.action.TASK_STARTED";

    static final String ACTION_TASK_FINISHED = "pt.ptinovacao.arqospocket.core.notify.action.TASK_FINISHED";

    static final String ACTION_TEST_FINISHED = "pt.ptinovacao.arqospocket.core.notify.action.TEST_FINISHED";

    static final String ACTION_TEST_CHANGED = "pt.ptinovacao.arqospocket.core.notify.action.TEST_CHANGED";

    static final String EXTRA_TEST_ID = "pt.ptinovacao.arqospocket.core.notify.action.TEST_ID";

    static final String EXTRA_TEST_DATA_ID = "pt.ptinovacao.arqospocket.core.notify.action.TEST_DATA_ID";

    static final String EXTRA_TASK_DATA_ID = "pt.ptinovacao.arqospocket.core.notify.action.TASK_DATA_ID";

    private TestNotificationManager(CoreApplication application) {
        this.application = application;
    }

    /**
     * Gets the instance of the {@link TestNotificationManager}.
     *
     * @param application the application context.
     * @return the {@link TestNotificationManager} instance.
     */
    public synchronized static TestNotificationManager getInstance(CoreApplication application) {
        if (instance == null) {
            instance = new TestNotificationManager(application);
        }
        return instance;
    }

    /**
     * Registers an instance of the {@link TestProgressNotifier} to be notified about test updates.
     *
     * @param context the context (activity) that wishes to be notified.
     * @param notifier the {@link TestProgressNotifier} instance.
     */
    public static void register(Context context, TestProgressNotifier notifier) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_TEST_STARTED);
        filter.addAction(ACTION_TASK_STARTED);
        filter.addAction(ACTION_TASK_FINISHED);
        filter.addAction(ACTION_TEST_FINISHED);
        filter.addAction(ACTION_TEST_CHANGED);

        context.registerReceiver(notifier, filter);
    }

    /**
     * Unregisters an instance of the {@link TestProgressNotifier} to cancel notifications.
     *
     * @param context the context (activity) that canceling the notifications.
     * @param notifier the {@link TestProgressNotifier} instance.
     */
    public static void unregister(Context context, TestProgressNotifier notifier) {
        context.unregisterReceiver(notifier);
    }

    /**
     * Sends a broadcast notifying that a test execution has started.
     *
     * @param testId the database ID of the executing test.
     * @param testDataId the test data ID of the executing test.
     */
    public void notifyTestStarted(long testId, String testDataId) {
        Intent intent = getIntent(ACTION_TEST_STARTED, testId, testDataId, null);
        application.sendBroadcast(intent);
    }

    /**
     * Sends a broadcast notifying that a task execution has started.
     *
     * @param testId the database ID of the executing test.
     * @param testDataId the test data ID of the parent executing test.
     * @param taskDataId the task data ID of the executing task.
     */
    public void notifyTaskStarted(long testId, String testDataId, String taskDataId) {
        Intent intent = getIntent(ACTION_TASK_STARTED, testId, testDataId, taskDataId);
        application.sendBroadcast(intent);
    }

    /**
     * Sends a broadcast notifying that a task execution has finished.
     *
     * @param testId the database ID of the executing test.
     * @param testDataId the test data ID of the parent executing test.
     * @param taskDataId the task data ID of the executing task.
     */
    public void notifyTaskFinished(long testId, String testDataId, String taskDataId) {
        Intent intent = getIntent(ACTION_TASK_FINISHED, testId, testDataId, taskDataId);
        application.sendBroadcast(intent);
    }

    /**
     * Sends a broadcast notifying that a test execution has finished.
     *
     * @param testId the database ID of the executing test.
     * @param testDataId the test data ID of the executing test.
     */
    public void notifyTestFinished(long testId, String testDataId) {
        Intent intent = getIntent(ACTION_TEST_FINISHED, testId, testDataId, null);
        application.sendBroadcast(intent);
    }

    /**
     * Sends a broadcast notifying that a test data has changed.
     *
     * @param testId the database ID of the executing test.
     * @param testDataId the test data ID of the executing test.
     */
    public void notifyTestChanged(long testId, String testDataId) {
        Intent intent = getIntent(ACTION_TEST_CHANGED, testId, testDataId, null);
        application.sendBroadcast(intent);
    }

    @NonNull
    private Intent getIntent(String action, long testId, String testDataId, String taskDataId) {
        Intent intent = new Intent(action);
        intent.putExtra(EXTRA_TEST_ID, testId);
        intent.putExtra(EXTRA_TEST_DATA_ID, testDataId);
        if (taskDataId != null) {
            intent.putExtra(EXTRA_TASK_DATA_ID, taskDataId);
        }
        return intent;
    }
}

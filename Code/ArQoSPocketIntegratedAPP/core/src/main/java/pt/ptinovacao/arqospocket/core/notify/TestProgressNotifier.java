package pt.ptinovacao.arqospocket.core.notify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper receiver to handle test progress notifications.
 * <p>
 * Created by Emílio Simões on 18-04-2017.
 */
public abstract class TestProgressNotifier extends BroadcastReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestProgressNotifier.class);

    @Override
    public final void onReceive(Context context, Intent intent) {
        if (TestNotificationManager.ACTION_TEST_STARTED.equals(intent.getAction())) {
            doTestExecutionStarted(intent);
        } else if (TestNotificationManager.ACTION_TASK_STARTED.equals(intent.getAction())) {
            doTaskExecutionStarted(intent);
        } else if (TestNotificationManager.ACTION_TASK_FINISHED.equals(intent.getAction())) {
            doTaskExecutionFinished(intent);
        } else if (TestNotificationManager.ACTION_TEST_FINISHED.equals(intent.getAction())) {
            doTestExecutionFinished(intent);
        } else if (TestNotificationManager.ACTION_TEST_CHANGED.equals(intent.getAction())) {
            doTestDataChanged(intent);
        } else {
            LOGGER.debug("Unknown action in test progress notifier: [{}]", intent.getAction());
        }
    }

    /**
     * Is raised when a test execution starts.
     *
     * @param testProgress contains information about the executing test.
     */
    public abstract void onTestExecutionStarted(TestProgress testProgress);

    /**
     * Is raised when a task execution starts.
     *
     * @param taskProgress contains information about the executing task.
     */
    public abstract void onTaskExecutionStarted(TaskProgress taskProgress);

    /**
     * Is raised when a task execution finishes.
     *
     * @param taskProgress contains information about the executing task.
     */
    public abstract void onTaskExecutionFinished(TaskProgress taskProgress);

    /**
     * Is raised when a test execution finishes.
     *
     * @param testProgress contains information about the executing test.
     */
    public abstract void onTestExecutionFinished(TestProgress testProgress);

    /**
     * Is raised when a test data changes.
     *
     * @param testProgress contains information about the changed test.
     */
    public abstract void onTestDataChanged(TestProgress testProgress);

    private void doTestExecutionStarted(Intent intent) {
        TestProgress progress = fillTestProgress(null, intent);
        onTestExecutionStarted(progress);
    }

    private void doTaskExecutionStarted(Intent intent) {
        TaskProgress progress = fillTaskProgress(intent);
        onTaskExecutionStarted(progress);
    }

    private void doTaskExecutionFinished(Intent intent) {
        TaskProgress progress = fillTaskProgress(intent);
        onTaskExecutionFinished(progress);
    }

    private void doTestExecutionFinished(Intent intent) {
        TestProgress progress = fillTestProgress(null, intent);
        onTestExecutionFinished(progress);
    }

    private void doTestDataChanged(Intent intent) {
        TestProgress progress = fillTestProgress(null, intent);
        onTestDataChanged(progress);
    }

    private TestProgress fillTestProgress(TestProgress progress, Intent intent) {
        if (progress == null) {
            progress = new TestProgress();
        }

        if (intent.hasExtra(TestNotificationManager.EXTRA_TEST_ID)) {
            progress.setTestId(intent.getLongExtra(TestNotificationManager.EXTRA_TEST_ID, 0L));
        }
        if (intent.hasExtra(TestNotificationManager.EXTRA_TEST_DATA_ID)) {
            progress.setTestDataId(intent.getStringExtra(TestNotificationManager.EXTRA_TEST_DATA_ID));
        }

        return progress;
    }

    private TaskProgress fillTaskProgress(Intent intent) {
        TaskProgress progress = new TaskProgress();
        fillTestProgress(progress, intent);

        if (intent.hasExtra(TestNotificationManager.EXTRA_TASK_DATA_ID)) {
            progress.setTaskDataId(intent.getStringExtra(TestNotificationManager.EXTRA_TASK_DATA_ID));
        }

        return progress;
    }
}

package pt.ptinovacao.arqospocket.core.tests;

import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pt.ptinovacao.arqospocket.core.TestExecutor;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.TestData;
import pt.ptinovacao.arqospocket.core.tests.data.TestResult;

/**
 * The result from a test execution. The result can be immediately returned by the
 * {@link ExecutableTest#execute(TestExecutor, long)} method or delivered asynchronously later. This behaviour is for
 * the test to define.
 * <p>
 * Created by Emílio Simões on 06-04-2017.
 */
public class TestExecutionResult {

    private final TestResult result;

    private long testId;

    private List<BaseTaskExecutionResult> tasks = new ArrayList<>();

    TestExecutionResult(long testId) {
        this.testId = testId;
        result = new TestResult();
        result.setStartDate(Calendar.getInstance().getTime());
        result.setModulo(0);
    }

    /**
     * Gets the test ID.
     *
     * @return the test ID.
     */
    public long getTestId() {
        return testId;
    }

    /**
     * Gets the test execution result.
     *
     * @return the test execution result.
     */
    public TestResult getResult() {
        List<TaskResult> results = new ArrayList<>();
        for (BaseTaskExecutionResult baseResult : tasks) {
            results.add(baseResult.getResult());
        }

        result.setTaskResults(Iterables.toArray(results, TaskResult.class));
        return result;
    }

    void addTaskResult(BaseTaskExecutionResult result) {
        tasks.add(result);
    }

    void finishExecution(TestData data) {
        if (data != null) {
            result.setTestId(data.getTestId());
        }
        result.setEndDate(Calendar.getInstance().getTime());
    }
}

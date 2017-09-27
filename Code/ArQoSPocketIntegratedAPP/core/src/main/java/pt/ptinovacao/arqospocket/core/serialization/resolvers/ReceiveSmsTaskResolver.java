package pt.ptinovacao.arqospocket.core.serialization.resolvers;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.serialization.TaskType;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.ReceiveSmsTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.ReceiveSmsTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.ReceiveSmsExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.tasks.ReceiveSmsExecutableTask;

/**
 * Receive Sms Task Resolver.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class ReceiveSmsTaskResolver implements InstanceResolver {

    private static final ResolverInfo RESOLVER_INFO = new ResolverInfo(TaskType.RECEIVE_SMS_TASK);

    private static final String NAME = "Receive SMS Message";

    @Override
    public TaskData task() {
        return new ReceiveSmsTaskData(RESOLVER_INFO, NAME);
    }

    @Override
    public TaskResult result() {
        return new ReceiveSmsTaskResult(RESOLVER_INFO, NAME);
    }

    @Override
    public BaseExecutableTask executableTask(TaskData data) {
        return new ReceiveSmsExecutableTask(data);
    }

    @Override
    public BaseTaskExecutionResult executionResult(TaskResult result) {
        return new ReceiveSmsExecutionResult(result);
    }
}

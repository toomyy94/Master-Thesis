package pt.ptinovacao.arqospocket.core.serialization.resolvers;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.serialization.TaskType;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.SendMmsTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.SendMmsTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.SendMmsExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.tasks.SendMmsExecutableTask;

/**
 * Send Mms Task Resolver.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class SendMmsTaskResolver implements InstanceResolver {

    private static final ResolverInfo RESOLVER_INFO = new ResolverInfo(TaskType.SEND_MMS_TASK);

    @Override
    public TaskData task() {
        return new SendMmsTaskData(RESOLVER_INFO);
    }

    @Override
    public TaskResult result() {
        return new SendMmsTaskResult(RESOLVER_INFO);
    }

    @Override
    public BaseExecutableTask executableTask(TaskData data) {
        return new SendMmsExecutableTask(data);
    }

    @Override
    public BaseTaskExecutionResult executionResult(TaskResult result) {
        return new SendMmsExecutionResult(result);
    }
}

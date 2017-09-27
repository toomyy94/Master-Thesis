package pt.ptinovacao.arqospocket.core.serialization.resolvers;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.serialization.TaskType;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.PingTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.PingTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.PingExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.tasks.PingExecutableTask;

/**
 * Ping Task Resolver.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class PingTaskResolver implements InstanceResolver {

    private static final ResolverInfo RESOLVER_INFO = new ResolverInfo(TaskType.PING_TASK);

    @Override
    public TaskData task() {
        return new PingTaskData(RESOLVER_INFO);
    }

    @Override
    public TaskResult result() {
        return new PingTaskResult(RESOLVER_INFO);
    }

    @Override
    public BaseExecutableTask executableTask(TaskData data) {
        return new PingExecutableTask(data);
    }

    @Override
    public BaseTaskExecutionResult executionResult(TaskResult result) {
        return new PingExecutionResult(result);
    }
}

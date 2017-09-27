package pt.ptinovacao.arqospocket.core.serialization.resolvers;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.serialization.TaskType;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.TurnOnMobileTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.TurnOnMobileTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.TurnOnMobileExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.tasks.TurnOnMobileExecutableTask;

/**
 * Turn On Mobile Task Resolver.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class TurnOnMobileTaskResolver implements InstanceResolver {

    private static final ResolverInfo RESOLVER_INFO = new ResolverInfo(TaskType.TURN_ON_MOBILE_TASK);

    @Override
    public TaskData task() {
        return new TurnOnMobileTaskData(RESOLVER_INFO);
    }

    @Override
    public TaskResult result() {
        return new TurnOnMobileTaskResult(RESOLVER_INFO);
    }

    @Override
    public BaseExecutableTask executableTask(TaskData data) {
        return new TurnOnMobileExecutableTask(data);
    }

    @Override
    public BaseTaskExecutionResult executionResult(TaskResult result) {
        return new TurnOnMobileExecutionResult(result);
    }
}

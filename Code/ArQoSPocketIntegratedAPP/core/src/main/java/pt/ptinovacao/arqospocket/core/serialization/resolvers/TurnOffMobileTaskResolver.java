package pt.ptinovacao.arqospocket.core.serialization.resolvers;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.serialization.TaskType;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.TurnOffMobileTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.TurnOffMobileTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.TurnOffMobileExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.tasks.TurnOffMobileExecutableTask;

/**
 * Turn Off Mobile Task Resolver.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class TurnOffMobileTaskResolver implements InstanceResolver {

    private static final ResolverInfo RESOLVER_INFO = new ResolverInfo(TaskType.TURN_OFF_MOBILE_TASK);

    @Override
    public TaskData task() {
        return new TurnOffMobileTaskData(RESOLVER_INFO);
    }

    @Override
    public TaskResult result() {
        return new TurnOffMobileTaskResult(RESOLVER_INFO);
    }

    @Override
    public BaseExecutableTask executableTask(TaskData data) {
        return new TurnOffMobileExecutableTask(data);
    }

    @Override
    public BaseTaskExecutionResult executionResult(TaskResult result) {
        return new TurnOffMobileExecutionResult(result);
    }
}

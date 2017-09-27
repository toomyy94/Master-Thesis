package pt.ptinovacao.arqospocket.core.serialization.resolvers;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.serialization.TaskType;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.TurnOnWifiTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.TurnOnWifiTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.TurnOnWifiExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.tasks.TurnOnWifiExecutableTask;

/**
 * Turn On Wifi Task Resolver.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class TurnOnWifiTaskResolver implements InstanceResolver {

    private static final ResolverInfo RESOLVER_INFO = new ResolverInfo(TaskType.TURN_ON_WIFI_TASK);

    @Override
    public TaskData task() {
        return new TurnOnWifiTaskData(RESOLVER_INFO);
    }

    @Override
    public TaskResult result() {
        return new TurnOnWifiTaskResult(RESOLVER_INFO);
    }

    @Override
    public BaseExecutableTask executableTask(TaskData data) {
        return new TurnOnWifiExecutableTask(data);
    }

    @Override
    public BaseTaskExecutionResult executionResult(TaskResult result) {
        return new TurnOnWifiExecutionResult(result);
    }
}

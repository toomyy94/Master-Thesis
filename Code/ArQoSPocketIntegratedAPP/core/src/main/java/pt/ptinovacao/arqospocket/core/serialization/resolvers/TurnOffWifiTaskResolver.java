package pt.ptinovacao.arqospocket.core.serialization.resolvers;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.serialization.TaskType;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.TurnOffWifiTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.TurnOffWifiTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.TurnOffWifiExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.tasks.TurnOffWifiExecutableTask;

/**
 * Turn Off Wifi Task Resolver.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class TurnOffWifiTaskResolver implements InstanceResolver {

    private static final ResolverInfo RESOLVER_INFO = new ResolverInfo(TaskType.TURN_OFF_WIFI_TASK);

    @Override
    public TaskData task() {
        return new TurnOffWifiTaskData(RESOLVER_INFO);
    }

    @Override
    public TaskResult result() {
        return new TurnOffWifiTaskResult(RESOLVER_INFO);
    }

    @Override
    public BaseExecutableTask executableTask(TaskData data) {
        return new TurnOffWifiExecutableTask(data);
    }

    @Override
    public BaseTaskExecutionResult executionResult(TaskResult result) {
        return new TurnOffWifiExecutionResult(result);
    }
}

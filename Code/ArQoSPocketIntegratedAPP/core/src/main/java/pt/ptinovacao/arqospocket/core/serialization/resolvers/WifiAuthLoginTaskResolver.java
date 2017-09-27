package pt.ptinovacao.arqospocket.core.serialization.resolvers;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.serialization.TaskType;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.WifiAuthLoginTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.WifiAuthLoginTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.WifiAuthLoginExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.tasks.WifiAuthLoginExecutableTask;

/**
 * Wifi Auth Login Task Resolver.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class WifiAuthLoginTaskResolver implements InstanceResolver {

    private static final ResolverInfo RESOLVER_INFO = new ResolverInfo(TaskType.WIFI_AUTH_LOGIN_TASK);

    @Override
    public TaskData task() {
        return new WifiAuthLoginTaskData(RESOLVER_INFO);
    }

    @Override
    public TaskResult result() {
        return new WifiAuthLoginTaskResult(RESOLVER_INFO);
    }

    @Override
    public BaseExecutableTask executableTask(TaskData data) {
        return new WifiAuthLoginExecutableTask(data);
    }

    @Override
    public BaseTaskExecutionResult executionResult(TaskResult result) {
        return new WifiAuthLoginExecutionResult(result);
    }
}

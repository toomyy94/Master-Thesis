package pt.ptinovacao.arqospocket.core.serialization.resolvers;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.serialization.TaskType;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.WifiAuthLogoutTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.WifiAuthLogoutTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.WifiAuthLogoutExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.tasks.WifiAuthLogoutExecutableTask;

/**
 * Wifi Auth Logout Task Resolver.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class WifiAuthLogoutTaskResolver implements InstanceResolver {

    private static final ResolverInfo RESOLVER_INFO = new ResolverInfo(TaskType.WIFI_AUTH_LOGOUT_TASK);

    @Override
    public TaskData task() {
        return new WifiAuthLogoutTaskData(RESOLVER_INFO);
    }

    @Override
    public TaskResult result() {
        return new WifiAuthLogoutTaskResult(RESOLVER_INFO);
    }

    @Override
    public BaseExecutableTask executableTask(TaskData data) {
        return new WifiAuthLogoutExecutableTask(data);
    }

    @Override
    public BaseTaskExecutionResult executionResult(TaskResult result) {
        return new WifiAuthLogoutExecutionResult(result);
    }
}

package pt.ptinovacao.arqospocket.core.serialization.resolvers;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.serialization.TaskType;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.ScanWifiNetworkTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.ScanWifiNetworkTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.ScanWifiNetworkExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.tasks.ScanWifiNetworkExecutableTask;

/**
 * Scan Wifi Network Task Resolver.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class ScanWifiNetworkTaskResolver implements InstanceResolver {

    private static final ResolverInfo RESOLVER_INFO = new ResolverInfo(TaskType.SCAN_WIFI_NETWORK_TASK);

    @Override
    public TaskData task() {
        return new ScanWifiNetworkTaskData(RESOLVER_INFO);
    }

    @Override
    public TaskResult result() {
        return new ScanWifiNetworkTaskResult(RESOLVER_INFO);
    }

    @Override
    public BaseExecutableTask executableTask(TaskData data) {
        return new ScanWifiNetworkExecutableTask(data);
    }

    @Override
    public BaseTaskExecutionResult executionResult(TaskResult result) {
        return new ScanWifiNetworkExecutionResult(result);
    }
}

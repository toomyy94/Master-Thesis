package pt.ptinovacao.arqospocket.core.serialization.resolvers;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.serialization.TaskType;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.DisassociateWiFiTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.DisassociateWiFiTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.DisassociateWiFiExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.tasks.DisassociateWiFiExecutableTask;

/**
 * Disassociate Wifi Task Resolver.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class DisassociateWifiTaskResolver implements InstanceResolver {

    private static final ResolverInfo RESOLVER_INFO = new ResolverInfo(TaskType.DISASSOCIATE_WIFI_TASK);

    @Override
    public TaskData task() {
        return new DisassociateWiFiTaskData(RESOLVER_INFO);
    }

    @Override
    public TaskResult result() {
        return new DisassociateWiFiTaskResult(RESOLVER_INFO);
    }

    @Override
    public BaseExecutableTask executableTask(TaskData data) {
        return new DisassociateWiFiExecutableTask(data);
    }

    @Override
    public BaseTaskExecutionResult executionResult(TaskResult result) {
        return new DisassociateWiFiExecutionResult(result);
    }
}

package pt.ptinovacao.arqospocket.core.serialization.resolvers;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.serialization.TaskType;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.AssociateWiFiTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.AssociateWiFiTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.AssociateWiFiExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.tasks.AssociateWiFiExecutableTask;

/**
 * Associate Wifi Task Resolver.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class AssociateWifiTaskResolver implements InstanceResolver {

    private static final ResolverInfo RESOLVER_INFO = new ResolverInfo(TaskType.ASSOCIATE_WIFI_TASK);

    @Override
    public TaskData task() {
        return new AssociateWiFiTaskData(RESOLVER_INFO);
    }

    @Override
    public TaskResult result() {
        return new AssociateWiFiTaskResult(RESOLVER_INFO);
    }

    @Override
    public BaseExecutableTask executableTask(TaskData data) {
        return new AssociateWiFiExecutableTask(data);
    }

    @Override
    public BaseTaskExecutionResult executionResult(TaskResult result) {
        return new AssociateWiFiExecutionResult(result);
    }
}

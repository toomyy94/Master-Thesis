package pt.ptinovacao.arqospocket.core.serialization.resolvers;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.serialization.TaskType;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.ReAssociateWifiTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.ReAssociateWifiTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.ReAssociateWifiExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.tasks.ReAssociateWifiExecutableTask;

/**
 * Re Associate Wifi Task Resolver.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class ReAssociateWifiTaskResolver implements InstanceResolver {

    private static final ResolverInfo RESOLVER_INFO = new ResolverInfo(TaskType.RE_ASSOCIATE_WIFI_TASK);

    @Override
    public TaskData task() {
        return new ReAssociateWifiTaskData(RESOLVER_INFO);
    }

    @Override
    public TaskResult result() {
        return new ReAssociateWifiTaskResult(RESOLVER_INFO);
    }

    @Override
    public BaseExecutableTask executableTask(TaskData data) {
        return new ReAssociateWifiExecutableTask(data);
    }

    @Override
    public BaseTaskExecutionResult executionResult(TaskResult result) {
        return new ReAssociateWifiExecutionResult(result);
    }
}

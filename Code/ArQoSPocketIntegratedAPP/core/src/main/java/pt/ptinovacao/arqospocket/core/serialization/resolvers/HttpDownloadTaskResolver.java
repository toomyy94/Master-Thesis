package pt.ptinovacao.arqospocket.core.serialization.resolvers;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.serialization.TaskType;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.HttpDownloadTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.HttpDownloadTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.HttpDownloadExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.tasks.HttpDownloadExecutableTask;

/**
 * Http Download Task Resolver.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class HttpDownloadTaskResolver implements InstanceResolver {

    private static final ResolverInfo RESOLVER_INFO = new ResolverInfo(TaskType.HTTP_DOWNLOAD_TASK);

    @Override
    public TaskData task() {
        return new HttpDownloadTaskData(RESOLVER_INFO);
    }

    @Override
    public TaskResult result() {
        return new HttpDownloadTaskResult(RESOLVER_INFO);
    }

    @Override
    public BaseExecutableTask executableTask(TaskData data) {
        return new HttpDownloadExecutableTask(data);
    }

    @Override
    public BaseTaskExecutionResult executionResult(TaskResult result) {
        return new HttpDownloadExecutionResult(result);
    }
}

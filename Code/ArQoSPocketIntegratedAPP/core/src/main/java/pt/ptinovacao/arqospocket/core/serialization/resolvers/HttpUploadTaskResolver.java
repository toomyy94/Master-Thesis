package pt.ptinovacao.arqospocket.core.serialization.resolvers;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.serialization.TaskType;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.HttpUploadTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.HttpUploadTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.HttpUploadExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.tasks.HttpUploadExecutableTask;

/**
 * Http Upload Task Resolver.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class HttpUploadTaskResolver implements InstanceResolver {

    private static final ResolverInfo RESOLVER_INFO = new ResolverInfo(TaskType.HTTP_UPLOAD_TASK);

    @Override
    public TaskData task() {
        return new HttpUploadTaskData(RESOLVER_INFO);
    }

    @Override
    public TaskResult result() {
        return new HttpUploadTaskResult(RESOLVER_INFO);
    }

    @Override
    public BaseExecutableTask executableTask(TaskData data) {
        return new HttpUploadExecutableTask(data);
    }

    @Override
    public BaseTaskExecutionResult executionResult(TaskResult result) {
        return new HttpUploadExecutionResult(result);
    }
}

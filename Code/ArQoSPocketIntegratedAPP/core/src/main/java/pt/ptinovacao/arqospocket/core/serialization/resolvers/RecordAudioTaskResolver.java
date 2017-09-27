package pt.ptinovacao.arqospocket.core.serialization.resolvers;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.RecordAudioTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.RecordAudioTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.RecordAudioExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.tasks.RecordAudioExecutableTask;

/**
 * Record Audio Task Resolver.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class RecordAudioTaskResolver implements InstanceResolver {

    private final String name;

    private final ResolverInfo resolverInfo;

    public RecordAudioTaskResolver(String type, String name) {
        this.name = name;
        resolverInfo = new ResolverInfo(type);
    }

    @Override
    public TaskData task() {
        return new RecordAudioTaskData(resolverInfo, name);
    }

    @Override
    public TaskResult result() {
        return new RecordAudioTaskResult(resolverInfo, name);
    }

    @Override
    public BaseExecutableTask executableTask(TaskData data) {
        return new RecordAudioExecutableTask(data);
    }

    @Override
    public BaseTaskExecutionResult executionResult(TaskResult result) {
        return new RecordAudioExecutionResult(result);
    }
}

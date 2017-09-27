package pt.ptinovacao.arqospocket.core.serialization.resolvers;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.serialization.TaskType;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.HangUpVoiceCallTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.HangUpVoiceCallTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.HangUpVoiceCallExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.tasks.HangUpVoiceCallExecutableTask;

/**
 * Hang Up Call Task Resolver.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class HangUpCallTaskResolver implements InstanceResolver {

    private static final String NAME = "Hang Up Voice Call";

    private ResolverInfo resolverInfo;

    @Override
    public TaskData task() {
        resolverInfo = new ResolverInfo(TaskType.HANG_UP_CALL_TASK);
        return new HangUpVoiceCallTaskData(resolverInfo, NAME);
    }

    @Override
    public TaskResult result() {
        return new HangUpVoiceCallTaskResult(resolverInfo, NAME);
    }

    @Override
    public BaseExecutableTask executableTask(TaskData data) {
        return new HangUpVoiceCallExecutableTask(data);
    }

    @Override
    public BaseTaskExecutionResult executionResult(TaskResult result) {
        return new HangUpVoiceCallExecutionResult(result);
    }
}
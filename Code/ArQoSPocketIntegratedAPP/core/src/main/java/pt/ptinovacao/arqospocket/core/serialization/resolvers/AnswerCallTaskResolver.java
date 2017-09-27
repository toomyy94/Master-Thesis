package pt.ptinovacao.arqospocket.core.serialization.resolvers;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.serialization.TaskType;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.AnswerVoiceCallTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.AnswerVoiceCallTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.AnswerVoiceCallExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.tasks.AnswerVoiceCallExecutableTask;

/**
 * Answer Call Task Resolver.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class AnswerCallTaskResolver implements InstanceResolver {

    private static final String NAME = "Answer Voice Call";

    private ResolverInfo resolverInfo;

    @Override
    public TaskData task() {
        resolverInfo = new ResolverInfo(TaskType.ANSWER_CALL_TASK);
        return new AnswerVoiceCallTaskData(resolverInfo, NAME);
    }

    @Override
    public TaskResult result() {
        return new AnswerVoiceCallTaskResult(resolverInfo, NAME);
    }

    @Override
    public BaseExecutableTask executableTask(TaskData data) {
        return new AnswerVoiceCallExecutableTask(data);
    }

    @Override
    public BaseTaskExecutionResult executionResult(TaskResult result) {
        return new AnswerVoiceCallExecutionResult(result);
    }
}


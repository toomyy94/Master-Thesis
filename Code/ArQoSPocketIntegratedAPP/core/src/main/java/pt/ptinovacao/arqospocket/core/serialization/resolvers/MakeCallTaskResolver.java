package pt.ptinovacao.arqospocket.core.serialization.resolvers;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.serialization.TaskType;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.MakeVoiceCallTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.MakeVoiceCallTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.MakeVoiceCallExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.tasks.MakeVoiceCallExecutableTask;

/**
 * Make Call Task Resolver.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class MakeCallTaskResolver extends SubInstanceResolver {

    @Override
    public TaskData task() {
        throw new UnsupportedOperationException("Task type " + TaskType.MAKE_CALL_TASK + " requires a sub task");
    }

    @Override
    public TaskResult result() {
        throw new UnsupportedOperationException("Task type " + TaskType.MAKE_CALL_TASK + " requires a sub task");
    }

    @Override
    public BaseExecutableTask executableTask(TaskData data) {
        throw new UnsupportedOperationException("Task type " + TaskType.MAKE_CALL_TASK + " requires a sub task");
    }

    @Override
    public BaseTaskExecutionResult executionResult(TaskResult result) {
        throw new UnsupportedOperationException("Task type " + TaskType.MAKE_CALL_TASK + " requires a sub task");
    }

    @Override
    public InstanceResolver resolver(String subType) {
        if (TaskType.VOICE_CALL_TYPE.equals(subType)) {
            return new MakeVoiceCallTaskResolver(subType);
        }
        return throwUnsupportedSubType(subType, TaskType.MAKE_CALL_TASK);
    }

    private static class MakeVoiceCallTaskResolver implements InstanceResolver {

        private static final String NAME = "Make Voice Call";

        private final ResolverInfo resolverInfo;

        MakeVoiceCallTaskResolver(String subType) {
            resolverInfo = new ResolverInfo(TaskType.MAKE_CALL_TASK, subType);
        }

        @Override
        public TaskData task() {
            return new MakeVoiceCallTaskData(resolverInfo, NAME);
        }

        @Override
        public TaskResult result() {
            return new MakeVoiceCallTaskResult(resolverInfo, NAME);
        }

        @Override
        public BaseExecutableTask executableTask(TaskData data) {
            return new MakeVoiceCallExecutableTask(data);
        }

        @Override
        public BaseTaskExecutionResult executionResult(TaskResult result) {
            return new MakeVoiceCallExecutionResult(result);
        }
    }
}

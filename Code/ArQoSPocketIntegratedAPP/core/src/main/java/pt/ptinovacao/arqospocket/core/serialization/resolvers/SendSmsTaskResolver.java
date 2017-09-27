package pt.ptinovacao.arqospocket.core.serialization.resolvers;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.serialization.TaskType;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.SendSmsTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.SendSmsTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.SendSmsExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.tasks.SendSmsExecutableTask;

/**
 * Send Sms Task Resolver.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class SendSmsTaskResolver implements InstanceResolver {

    private static final ResolverInfo RESOLVER_INFO = new ResolverInfo(TaskType.SEND_SMS_TASK);

    private static final String NAME = "Send SMS Message";

    @Override
    public TaskData task() {
        return new SendSmsTaskData(RESOLVER_INFO, NAME);
    }

    @Override
    public TaskResult result() {
        return new SendSmsTaskResult(RESOLVER_INFO, NAME);
    }

    @Override
    public BaseExecutableTask executableTask(TaskData data) {
        return new SendSmsExecutableTask(data);
    }

    @Override
    public BaseTaskExecutionResult executionResult(TaskResult result) {
        return new SendSmsExecutionResult(result);
    }
}

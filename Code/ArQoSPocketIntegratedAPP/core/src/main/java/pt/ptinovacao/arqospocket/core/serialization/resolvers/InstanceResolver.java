package pt.ptinovacao.arqospocket.core.serialization.resolvers;

import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;

/**
 * Interface that identifies a resolver. A resolver allows to obtain different implementations of an abstract class
 * based on an ID or other identity data.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public interface InstanceResolver {

    /**
     * Gets the {@link TaskData} for the provided identity.
     *
     * @return the {@link TaskData} for the provided identity.
     */
    TaskData task();

    /**
     * Gets the {@link TaskResult} for the provided identity.
     *
     * @return the {@link TaskResult} for the provided identity.
     */
    TaskResult result();

    /**
     * Gets the {@link BaseExecutableTask} for the provided identity.
     *
     * @return the {@link BaseExecutableTask} for the provided identity.
     */
    BaseExecutableTask executableTask(TaskData data);

    /**
     * Gets the {@link BaseTaskExecutionResult} for the provided identity.
     *
     * @return the {@link BaseTaskExecutionResult} for the provided identity.
     */
    BaseTaskExecutionResult executionResult(TaskResult result);
}

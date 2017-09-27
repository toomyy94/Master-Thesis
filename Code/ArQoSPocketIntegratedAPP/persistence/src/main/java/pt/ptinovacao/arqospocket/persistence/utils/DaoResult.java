package pt.ptinovacao.arqospocket.persistence.utils;

/**
 * Result that contains a database operation result with additional metadata.
 * <p>
 * Created by Emílio Simões on 10-05-2017.
 */
public abstract class DaoResult<T> {

    private final T entity;

    private final ExecutedOperation executedOperation;

    DaoResult(T entity, ExecutedOperation executedOperation) {
        this.entity = entity;
        this.executedOperation = executedOperation;
    }

    public T getEntity() {
        return entity;
    }

    public ExecutedOperation getExecutedOperation() {
        return executedOperation;
    }
}

package pt.ptinovacao.arqospocket.persistence.utils;

import pt.ptinovacao.arqospocket.persistence.models.ScheduledEvent;

/**
 * Implementation for the {@link ScheduledEvent}.
 * <p>
 * Created by Emílio Simões on 10-05-2017.
 */
public class ScheduledEventResult extends DaoResult<ScheduledEvent> {

    public ScheduledEventResult(ScheduledEvent entity, ExecutedOperation executedOperation) {
        super(entity, executedOperation);
    }
}

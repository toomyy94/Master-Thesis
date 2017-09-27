package pt.ptinovacao.arqospocket.core.mappers;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import pt.ptinovacao.arqospocket.core.TestParser;
import pt.ptinovacao.arqospocket.core.tests.ExecutableTest;
import pt.ptinovacao.arqospocket.core.tests.data.TestResult;
import pt.ptinovacao.arqospocket.persistence.models.ExecutingEvent;
import pt.ptinovacao.arqospocket.persistence.models.OnDemandEvent;

/**
 * Mapper utility to be used on the reactivex map operator to map a {@link OnDemandEvent} to a {@link ExecutableTest}.
 * <p>
 * Created by Emílio Simões on 20-04-2017.
 */
public class ExecutingTaskMapper implements Function<ExecutingEvent, TestResult> {

    @Override
    public TestResult apply(@NonNull ExecutingEvent executingEvent) throws Exception {
        TestParser parser = new TestParser();
        return parser.parseSingleResult(executingEvent.getResultData());
    }
}

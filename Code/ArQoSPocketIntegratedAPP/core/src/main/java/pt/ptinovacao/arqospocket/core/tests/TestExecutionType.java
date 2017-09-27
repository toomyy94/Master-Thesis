package pt.ptinovacao.arqospocket.core.tests;

/**
 * identifies the type of executions possible for a test.
 * <p>
 * Created by Emílio Simões on 11-04-2017.
 */
public enum TestExecutionType {
    SCHEDULED,
    USER_REQUEST,
    ALARM_REQUEST,
    RESCHEDULE
}

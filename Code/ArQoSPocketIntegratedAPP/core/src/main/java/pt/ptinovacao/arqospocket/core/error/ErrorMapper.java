package pt.ptinovacao.arqospocket.core.error;

/**
 * Created by pedro on 03/05/2017.
 */
public enum ErrorMapper {
    SMS_GENERIC_FAILURE(23500, "Network", "SMS: unknown error"),
    SMS_NO_SERVICE(3521, "Network", "Not registered in the Network"),
    SMS_NULL_PDU(23304, "Network", "SMS: invalid PDU mode parameter"),
    SMS_NO_RADIO(25297, "Network/Incorrect Usage", "No Radio"),

    //CAll
    CALL_TASK_TIMEOUT(3031, "Probe", "Timed call ended early due to task timeout"),

    SMS_RECEIVER_TIMEOUT(1840, "Service Probe", "Timeout before receiving expected message"),
    SMS_SEND_TIMEOUT(23501, "Network SMS", "SMS send timeout"),
    SMS_INVALID_TEXT(23305, "Network SMS", "invalid text mode parameter"),
    SMS_POSTPONED(23512, "Network SMS", "message delivery postponed"),
    SMS_SEND_INVALID_NUMBER_FORMAT(25184, "Probe", "Invalid number format"),

    TASK_EXECUTION_CANCELED(3530, "Service Probe", "Task execution canceled"),

    TASK_EXECUTION_EXCEEDED(3523, "Service Probe", "Task time tolerance exceeded"),

    TASK_NOT_EXECUTION_SNOOZING(7029, "Service Probe", "Not executed: snoozing"),

    PROBE_ERROR_ANSWER_THE_CALL(2010, "Probe", "Error answer the call"),

    THE_CALL_WAS_NOT_ACTIVE(3004, "Probe", "The call was not active"),

    MEMORY_FAILURE(512, "Probe", "Memory failure"),

    ERROR_PLACING_A_CALL(2009, "Probe", "Error placing a call"),

    CALL_WAITING_MESSAGE_NOT_DETECTED(3514, "Probe", "Call waiting message not detected"),

    THE_CALL_WAS_ALREADY_DISCONNECTED(3002, "Probe", "The call was already disconnected"),

    MOBILE_TERMINATION_ERROR_MEMORY_FULL(22020, "Module", "Mobile Termination error - memory full"),

    INCORRECT_USAGE(1816, "Probe", "invalid parameter");

    private int errorCode;

    private String type;

    private String text;

    ErrorMapper(int errorCode, String type, String text) {

        this.errorCode = errorCode;
        this.type = type;
        this.text = text;
    }

    @Override
    public String toString() {
        return "NOK:" + errorCode + ": " + type + ": " + text;
    }
}

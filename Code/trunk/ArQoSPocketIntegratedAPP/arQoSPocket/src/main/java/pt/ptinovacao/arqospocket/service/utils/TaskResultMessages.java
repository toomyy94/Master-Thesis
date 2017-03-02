package pt.ptinovacao.arqospocket.service.utils;

/**
 * Created by x00881 on 10-01-2017.
 */

public class TaskResultMessages {
    private TaskResultMessages(){
    }

    //TODO passar para as strings
    public static final String STATUS_OK = "OK";
    public static final String STATUS_NOK = "NOK";

    public static final String STATUS_SIM_NOT_INSERTED = ":22010: SIM not inserted";
    public static final String STATUS_SIM_PIN_REQUIRED = ":22011: SIM PIN required";
    public static final String STATUS_SIM_PUK_REQUIRED = ":22011: SIM PUK required";
    public static final String STATUS_SIM_BLOCKED = ":22262: SIM Blocked";
    public static final String STATUS_INVALID_PARAMETERS = ":2028: Probe: Invalid Parameters";
    public static final String STATUS_NOT_REGISTERED_IN_NETWORK = ":3521: Probe: Not registered in the Network";
    public static final String STATUS_INVALID_PHONE_NUMBER = ":3519: Probe: Invalid Number";
    public static final String STATUS_CALL_ALREADY_ACTIVE = ":3516: Probe: Call(s) already active";
    public static final String STATUS_CALL_NOT_ACTIVE = ":3004: Probe: The call was not active";
    public static final String STATUS_CALL_NOT_ANSWERED = ":33029: Probe: Destination didn't answer";
    public static final String STATUS_CALL_NOT_ANSWERED_OR_REJECTED = ":3017: Probe: Destination didn't answer or call was rejected";
    public static final String STATUS_CALL_DISCONNECTED_OR_REJECTED = ":3021: Probe: Disconnect or rejected call";
    public static final String STATUS_UNSUPPORTED_AUDIO_TYPE = ":7014: Unsupported audio type";


}

package pt.ptinovacao.arqospocket.core.serialization.entities.results.type2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.core.CoreApplication;

/**
 * Created by pedro on 07/06/2017.
 */
public class TelephonyStateManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(TelephonyStateManager.class);

    public CallStatus callStatus;

    public CallInOrOut callInOrOut;

    private CoreApplication coreApplication;

    private UpdateState updateState;

    private String incomingNumber;

    private static TelephonyStateManager telephonyStateManager;

    public static TelephonyStateManager getInstance(CoreApplication coreApplication) {

        if (telephonyStateManager == null) {
            telephonyStateManager = new TelephonyStateManager(coreApplication);
        }
        return telephonyStateManager;
    }

    public void registerStateCall(UpdateState updateState) {
        this.updateState = updateState;
    }

    public void unRegisterStateCall() {
        this.updateState = null;
    }

    public void notificationState() {
        if (updateState != null) {
            updateState.updateCallStatus();
        }
    }

    public TelephonyStateManager(CoreApplication coreApplication) {
        this.coreApplication = coreApplication;
    }

    public CallStatus getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(CallStatus callStatus) {
        this.callStatus = callStatus;
    }

    public CallInOrOut getCallInOrOut() {
        return callInOrOut;
    }

    public void setCallInOrOut(CallInOrOut callInOrOut) {
        this.callInOrOut = callInOrOut;
    }

    public String getIncomingNumber() {
        return incomingNumber;
    }

    public void setIncomingNumber(String incomingNumber) {
        this.incomingNumber = incomingNumber;
    }

    public enum CallStatus {
        IDLE,
        CONNECTING,
        ACTIVE,
        WAITING
    }

    public enum CallInOrOut {
        Callee,
        Caller
    }

    public enum CallType {
        VOICE,
        DATA,
        QNC,
        IP
    }

    public interface UpdateState {
        void updateCallStatus();
    }
}

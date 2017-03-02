package PTInov.IEX.ArQoSPocket.ServicesInstance;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class CallEventStateMachine {
	
	public final static String CALLSTATECHANGE_DIALING = "CALLSTATECHANGE_PHONE_EVENT:DIALING";
	public final static String CALLSTATECHANGE_ALERTING = "CALLSTATECHANGE_PHONE_EVENT:ALERTING";
	public final static String CALLSTATECHANGE_ACTIVE = "CALLSTATECHANGE_PHONE_EVENT:ACTIVE";
	public final static String CALLSTATECHANGE_DISCONNECTING = "CALLSTATECHANGE_PHONE_EVENT:DISCONNECTING";
	public final static String CALLSTATECHANGE_RINGING = "CALLSTATECHANGE_PHONE_EVENT:RINGING";
	public final static String CALLSTATECHANGE_IDLE = "CALLSTATECHANGE_PHONE_EVENT:IDLE";
	
	public final static String DISCONNECTPHONE_ERRORUNSPECIFIED = "DISCONNECT_PHONE_EVENT:ERROR_UNSPECIFIED";
	public final static String DISCONNECTPHONE_NORMAL = "DISCONNECT_PHONE_EVENT:NORMAL";
	public final static String DISCONNECTPHONE_LOCAL = "DISCONNECT_PHONE_EVENT:LOCAL";
	public final static String DISCONNECTPHONE_OUTOFSERVICE = "DISCONNECT_PHONE_EVENT:OUT_OF_SERVICE";
	public final static String DISCONNECTPHONE_INCOMINGMISSED = "DISCONNECT_PHONE_EVENT:INCOMING_MISSED";
	public final static String DISCONNECTPHONE_BUSY = "DISCONNECT_PHONE_EVENT:BUSY";
	
	public final static int BEGIN = 0;
	public final static int DIALING = 1;
	public final static int ALERTING = 2;
	public final static int OUT_OF_SERVICE = 3;
	public final static int ERROR_UNSPECIFIED = 4;
	public final static int ACTIVE = 5;
	public final static int NORMAL = 6;
	public final static int LOCAL = 7;
	
	
	private Semaphore callSemaphore = null;
	
	private int myInternalControlState = BEGIN;
	private String finalResult = "ERROR Machine State";
	
	
	public CallEventStateMachine(Semaphore s) {
		
		callSemaphore = s;	
		myInternalControlState = BEGIN;
	}
	
	public void resetStateMachine() {
		myInternalControlState = BEGIN;
		finalResult = "NOK: ERROR Machine State :";
	}
	
	public void addEvent(String receivedEvent) {
		switch(myInternalControlState) {
			case BEGIN:
				
				// apenas pode transitar para Dialing
				
				if (receivedEvent.equals(CALLSTATECHANGE_DIALING)) {
					
					myInternalControlState = DIALING;
					
					// Caso de timeout, tenho de dar o erro se ficar por aki
					finalResult = "NOK: TRY_DIALING";
					
				} else {
					// estado invalido
					finalResult = "NOK: ERROR_NOT_DEFINED :"+receivedEvent;
					callSemaphore.release();	
				}
				
				break;
			case DIALING:
				
				// apenas pode transitar para out_of_service ou alerting
				
				if (receivedEvent.equals(CALLSTATECHANGE_ALERTING)) {
					
					myInternalControlState = ALERTING;
					// Caso de timeout, tenho de dar o erro se ficar por aki
					finalResult = "NOK: RECEIVED_ALERTING";
					
				} else if (receivedEvent.equals(DISCONNECTPHONE_OUTOFSERVICE)) {
					
					// Sem serviço
					finalResult = "NOK: OUT_OF_SERVICE";
					callSemaphore.release();
					
				} else {
					// estado invalido
					finalResult = "NOK: ERROR_NOT_DEFINED :"+receivedEvent;
					callSemaphore.release();	
				}
				
				break;
			case ALERTING:
				
				// so pode transitar para ERROR_UNSPECIFIED ou para active
				
				if (receivedEvent.equals(CALLSTATECHANGE_ACTIVE)) {
					
					// chamada atendida
					myInternalControlState = ACTIVE;
					// Caso de timeout, tenho de dar o erro se ficar por aki
					finalResult = "NOK: RECEIVED_ACTIVE";
					
				} else if (receivedEvent.equals(DISCONNECTPHONE_ERRORUNSPECIFIED)) {
					
					// telefone desligado ou chamada nao atendida
					finalResult = "NOK: ERROR_UNSPECIFIED";
					callSemaphore.release();	
					
				} else {
					
					// estado invalido
					finalResult = "NOK: ERROR_NOT_DEFINED :"+receivedEvent;
					callSemaphore.release();	
				}
				
				break;
			case OUT_OF_SERVICE:
				break;
			case ERROR_UNSPECIFIED:
				break;
			case ACTIVE:
				
				// so pode ir para local ou normal
				if (receivedEvent.equals(DISCONNECTPHONE_NORMAL)) {
					
					// chamada atendida e desligada pelo outro lado
					finalResult = "OK: ANSWER CALL - Client hangup";
					callSemaphore.release();
					
				} else if (receivedEvent.equals(DISCONNECTPHONE_LOCAL)) {
					
					// chamada atendida e desligada pela APP
					finalResult = "OK: ANSWER CALL - APP hangup";
					callSemaphore.release();
					
				} else {
					
					// estado invalido
					finalResult = "NOK: ERROR_NOT_DEFINED :"+receivedEvent;
					callSemaphore.release();	
				}
				
				break;
			case NORMAL:
				break;
			case LOCAL:
				break;
		}
	}
	
	
	public String returnTestResult() {
		return finalResult;
	}

}

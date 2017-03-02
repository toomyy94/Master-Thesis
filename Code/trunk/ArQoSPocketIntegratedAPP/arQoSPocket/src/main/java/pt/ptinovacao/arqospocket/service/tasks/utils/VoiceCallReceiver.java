package pt.ptinovacao.arqospocket.service.tasks.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import pt.ptinovacao.arqospocket.service.enums.ECallState;
import pt.ptinovacao.arqospocket.service.enums.ECallType;

import static android.media.AudioManager.MODE_IN_CALL;
import static android.media.AudioManager.MODE_IN_COMMUNICATION;
import static android.telephony.TelephonyManager.CALL_STATE_IDLE;
import static pt.ptinovacao.arqospocket.service.enums.ECallState.IDLE;
import static pt.ptinovacao.arqospocket.service.enums.ECallState.INITIATED;
import static pt.ptinovacao.arqospocket.service.enums.ECallState.OFFHOOK;
import static pt.ptinovacao.arqospocket.service.enums.ECallState.RINGING;
import static pt.ptinovacao.arqospocket.service.enums.ECallType.INCOMING;
import static pt.ptinovacao.arqospocket.service.enums.ECallType.OUTGOING;

public class VoiceCallReceiver extends BroadcastReceiver {

    private static final String TAG = VoiceCallReceiver.class.getSimpleName();
    private static final int CALL_WATCHER_SLEEP_TIME_MS = 500;

    public static final String ACTION_PHONE_STATE = "android.intent.action.PHONE_STATE";
    public static final String ACTION_OUTGOING_CALL = "android.intent.action.NEW_OUTGOING_CALL";

    private Context context;
    private Bundle bundle;
    private String action, state, callNumber;
    private ECallType callType;
    private ECallState callState;
    private Thread outgoingCallAnswerWatchThread = null;
    private VoiceCallReceiverListener listener;
    private TelephonyManager telephonyManager;

    public VoiceCallReceiver(Context context, VoiceCallReceiverListener listener) {
        this.listener = listener;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context = context;

        try {
            if ((bundle = intent.getExtras()) == null) {
                Log.e(TAG, "Intent extras are null");
                return;
            }

            state = bundle.getString(TelephonyManager.EXTRA_STATE);
            action = intent.getAction();

            if (ACTION_PHONE_STATE.equals(action)) {
                Log.i(TAG, "android.intent.action.PHONE_STATE");

                if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
                    //incoming call, phone is ringing
                    callNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    Log.i(TAG, "New INCOMING CALL from: " + callNumber);
                    callType = INCOMING;
                    callState = RINGING;
                    if (listener != null){
                        listener.onCallRinging(callType, callNumber);
                    }
                } else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)){
                    if (callType == INCOMING){
                        Log.i(TAG, "INCOMING CALL answered!");
                        //incoming call was answered
                        callState = OFFHOOK;
                        if (listener != null){
                            listener.onCallAnswered(callType);
                        }
                    } else if (callType == OUTGOING) {
                        if (callState == INITIATED){
                            callState = RINGING;
                            final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                            if (outgoingCallAnswerWatchThread != null){
                                outgoingCallAnswerWatchThread.interrupt();
                                outgoingCallAnswerWatchThread = null;
                            }

                            outgoingCallAnswerWatchThread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    // by changing the audio mode to MODE_IN_COMMUNICATION
                                    // we can then detect when the call is answered
                                    // because android changes the audio mode back to MODE_IN_CALL
                                    // when that happens
                                    audioManager.setMode(MODE_IN_COMMUNICATION);
                                    int mode =  audioManager.getMode();
                                    Log.d(TAG, "outgoingCallAnswerWatchThread mode:" + mode);
                                    while(callState == RINGING && !Thread.interrupted()){
                                        try {
                                            Thread.sleep(CALL_WATCHER_SLEEP_TIME_MS);
                                        } catch (InterruptedException e) {
                                            Log.d(TAG, "outgoingCallAnswerWatchThread interrupted!");
                                            break;
                                        }

                                        int audioMode = audioManager.getMode();
                                        Log.d(TAG, "outgoingCallAnswerWatchThread audioMode:" + audioMode);
                                        //the call is connected
                                        if (audioMode == MODE_IN_CALL) {
                                            if (telephonyManager.getCallState() != CALL_STATE_IDLE){
                                                Log.i(TAG, "outgoingCallAnswerWatchThread OUTGOING CALL answered!");
                                                callState = OFFHOOK;
                                                listener.onCallAnswered(callType);
                                            }
                                        }

                                    }

                                }
                            });

                            outgoingCallAnswerWatchThread.start();
                            if (listener != null){
                                listener.onCallRinging(callType, callNumber);
                            }
                        }

                    }
                } else if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
                    //call was disconnected.
                    Log.i(TAG, "android.intent.action.EXTRA_STATE_IDLE");
                    Log.i(TAG, "Call was disconnected!");
                    callState = IDLE;
                    if (outgoingCallAnswerWatchThread != null){
                        outgoingCallAnswerWatchThread.interrupt();
                        outgoingCallAnswerWatchThread = null;
                    }


                    listener.onCallDisconnected(callType);
                }
            } else if (ACTION_OUTGOING_CALL.equals(action)) {
                Log.i(TAG, "android.intent.action.NEW_OUTGOING_CALL");
                callNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                Log.i(TAG, "New OUTGOING CALL to: " + callNumber);
                callType = OUTGOING;
                callState = INITIATED;
                listener.onCallInitiated(callType);
            }


        } catch (Exception e) {
            Log.d(TAG, "Exception:");
            e.printStackTrace();

        }


    }


    public static interface VoiceCallReceiverListener{
        void onCallInitiated(ECallType callType);
        void onCallRinging(ECallType callType, String number);
        void onCallAnswered(ECallType callType);
        void onCallDisconnected(ECallType callType);
    }

}

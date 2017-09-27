package pt.ptinovacao.arqospocket.core.messaging;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.util.LongSparseArray;
import android.telephony.SmsManager;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

import pt.ptinovacao.arqospocket.core.CoreApplication;

/**
 * Helper class to send SMS messages.
 * <p>
 * Created by Emílio Simões on 20-04-2017.
 */
public class SmsMessageManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsMessageManager.class);

    private static final String ACTION_SMS_SENT = "pt.ptinovacao.arqospocket.core.messaging.action.SMS_SENT";

    private static final String ACTION_SMS_RECEIVED = "pt.ptinovacao.arqospocket.core.messaging.action.SMS_RECEIVED";

    private static final String EXTRA_REQUEST_CODE = "pt.ptinovacao.arqospocket.core.messaging.extra.REQUEST_CODE";

    public static final int RESULT_OK = -1;

    private static final SendSmsListener EMPTY_SEND_LISTENER = new DefaultSendSmsListener();

    private static SmsMessageManager instance;

    private final CoreApplication application;

    private SentSmsBroadcastReceiver sentSmsBroadcastReceiver;

    private DeliveredSmsBroadcastReceiver deliveredSmsBroadcastReceiver;

    private LongSparseArray<SendSmsListener> waitingSendListeners = new LongSparseArray<>();

    private LongSparseArray<ReceiveSmsListener> waitingReceiveListeners = new LongSparseArray<>();

    private SmsMessageManager(CoreApplication application) {
        this.application = application;
    }

    /**
     * Gets the {@link SmsMessageManager} instance. This is a lazy singleton, the instance will be created the first
     * time the method is called.
     *
     * @param application the application object.
     * @return the {@link SmsMessageManager}.
     */
    public synchronized static SmsMessageManager getInstance(CoreApplication application) {
        if (instance == null) {
            instance = new SmsMessageManager(application);
        }
        return instance;
    }

    /**
     * Initializes the {@link SmsMessageManager} registering for SMS send broadcasts.
     */
    public void init() {
        sentSmsBroadcastReceiver = new SentSmsBroadcastReceiver();
        application.registerReceiver(sentSmsBroadcastReceiver, new IntentFilter(ACTION_SMS_SENT));

        deliveredSmsBroadcastReceiver = new DeliveredSmsBroadcastReceiver();
        application.registerReceiver(deliveredSmsBroadcastReceiver, new IntentFilter(ACTION_SMS_RECEIVED));
    }

    /**
     * Finalizes the {@link SmsMessageManager} un registering the SMS listeners.
     */
    public void close() {
        if (sentSmsBroadcastReceiver != null) {
            application.unregisterReceiver(sentSmsBroadcastReceiver);
        }
        if (deliveredSmsBroadcastReceiver != null) {
            application.unregisterReceiver(deliveredSmsBroadcastReceiver);
        }
    }

    /**
     * Sens an SMS message notifying a listener asynchronously if the message was sent and delivered.
     *
     * @param destination the destination number for the SMS.
     * @param text the SMS message text.
     * @param listener the listener to be notified if the message was delivered.
     */
    public void sendSms(String destination, String smscm, String text, SendSmsListener listener) {
        long requestId = Calendar.getInstance().getTimeInMillis();
        if (listener != null) {
            waitingSendListeners.append(requestId, listener);
        }

        PendingIntent sentPendingIntent = getPendingIntent(requestId, ACTION_SMS_SENT);
        PendingIntent deliveredPendingIntent = getPendingIntent(requestId, ACTION_SMS_RECEIVED);

        SmsManager smsManager = SmsManager.getDefault();

        //scAddress is the service center address or null to use the current default SMSC
        if (Strings.isNullOrEmpty(smscm)) {
            smscm = null;
        }
        LOGGER.debug("Sending SMS...");
        smsManager.sendTextMessage(destination, smscm, text, sentPendingIntent, deliveredPendingIntent);
    }

    /**
     * Processes a received SMS notifying any listener about the received message.
     *
     * @param receivedMessage the received message.
     */
    public synchronized void receiveSms(ReceivedMessage receivedMessage) {
        int listenersCount = waitingReceiveListeners.size();
        if (listenersCount > 0) {
            for (int i = 0; i < listenersCount; i++) {
                long key = waitingReceiveListeners.keyAt(i);
                waitingReceiveListeners.get(key).onSmsReceived(receivedMessage);
            }
        }
    }

    /**
     * Registers a listener to be notified about received SMS messages.
     *
     * @param listenerId a reference code for the listener to identify itself.
     * @param listener the listener instance.
     */
    public void registerSmsReceiver(long listenerId, ReceiveSmsListener listener) {
        waitingReceiveListeners.append(listenerId, listener);
    }

    /**
     * Unregisters a listener.
     *
     * @param listenerId the ID of the unregistering listener.
     */
    public void unregisterSmsReceiver(long listenerId) {
        waitingReceiveListeners.delete(listenerId);
    }

    private PendingIntent getPendingIntent(long requestId, String action) {
        Intent intent = getIntent(action, requestId);
        return PendingIntent.getBroadcast(application, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Intent getIntent(String action, long requestCode) {
        Intent intent = new Intent(action);
        intent.putExtra(EXTRA_REQUEST_CODE, requestCode);
        return intent;
    }

    private long getRequestCode(Intent intent) {
        return intent.getLongExtra(EXTRA_REQUEST_CODE, -1L);
    }

    private class SentSmsBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = getResultCode();
            switch (resultCode) {
                case RESULT_OK:
                    LOGGER.debug("SMS message send successfully");
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    LOGGER.debug("SMS message send failure: Generic failure cause: {}", resultCode);
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    LOGGER.debug("SMS message send failure: Service is currently unavailable: {}", resultCode);
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    LOGGER.debug("SMS message send failure: No PDU provided: {}", resultCode);
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    LOGGER.debug("SMS message send failure: Radio was explicitly turned off: {}", resultCode);
                    break;
            }

            long requestCode = getRequestCode(intent);
            SendSmsListener listener = waitingSendListeners.get(requestCode, EMPTY_SEND_LISTENER);
            listener.onSmsSent(resultCode);
        }
    }

    private class DeliveredSmsBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = getResultCode();
            switch (resultCode) {
                case RESULT_OK:
                    LOGGER.debug("SMS message delivered successfully");
                    break;
                case Activity.RESULT_CANCELED:
                    LOGGER.debug("SMS message delivery failed: {}", resultCode);
                    break;
            }

            long requestCode = getRequestCode(intent);
            SendSmsListener listener = waitingSendListeners.get(requestCode, EMPTY_SEND_LISTENER);
            try {
                listener.onSmsDelivered(resultCode);
            } catch (IllegalArgumentException e) {
                LOGGER.warn("Deliver notification has occurred after the task has timed out, ignoring notification", e);
            }
            waitingSendListeners.delete(requestCode);
        }
    }
}

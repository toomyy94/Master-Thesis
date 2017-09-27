package pt.ptinovacao.arqospocket.messaging;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.cdrs.CDRsManager;
import pt.ptinovacao.arqospocket.core.messaging.ReceivedMessage;
import pt.ptinovacao.arqospocket.core.messaging.SmsMessageManager;
import pt.ptinovacao.arqospocket.persistence.ExecutingEventDao;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in a service on a separate handler thread.
 */
public class SmsService extends IntentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsService.class);

    private static final String ACTION_PROCESS_SMS = "pt.ptinovacao.arqospocket.messaging.action.PROCESS_SMS";

    private static final String EXTRA_SOURCE_NUMBER = "pt.ptinovacao.arqospocket.messaging.extra.SOURCE_NUMBER";

    private static final String EXTRA_MESSAGE_BODY = "pt.ptinovacao.arqospocket.messaging.extra.MESSAGE_BODY";

    private static final String EXTRA_ENCODING = "pt.ptinovacao.arqospocket.messaging.extra.ENCODING";

    public SmsService() {
        super("SmsService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If the service is already performing a task
     * this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionProcessSms(Context context, String sourceNumber, String messageBody,
            String encoding) {
        Intent intent = new Intent(context, SmsService.class);
        intent.setAction(ACTION_PROCESS_SMS);
        intent.putExtra(EXTRA_SOURCE_NUMBER, sourceNumber);
        intent.putExtra(EXTRA_MESSAGE_BODY, messageBody);
        intent.putExtra(EXTRA_ENCODING, encoding);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PROCESS_SMS.equals(action)) {
                final String sourceNumber = intent.getStringExtra(EXTRA_SOURCE_NUMBER);
                final String messageBody = intent.getStringExtra(EXTRA_MESSAGE_BODY);
                final String encoding = intent.getStringExtra(EXTRA_ENCODING);
                handleActionProcessSms((CoreApplication) getApplication(), sourceNumber, messageBody, encoding);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided parameters.
     */
    private void handleActionProcessSms(CoreApplication application, String sourceNumber, String messageBody,
            String encoding) {
        LOGGER.debug("Starting received SMS processing");

        ExecutingEventDao executingEventDao = application.getDatabaseHelper().createExecutingEventDao();
        if (executingEventDao.countPendingExecutingEvents() == 0) CDRsManager.getInstance(application).generateCDRReceivedSMS(sourceNumber, messageBody);

        SmsMessageManager.getInstance(application).receiveSms(new ReceivedMessage(sourceNumber, messageBody, encoding));
    }
}

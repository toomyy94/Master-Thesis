package pt.ptinovacao.arqospocket.service.tasks;



import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;

import pt.ptinovacao.arqospocket.service.tasks.enums.ESMSState;
import pt.ptinovacao.arqospocket.service.tasks.interfaces.IMySMSManager;

/**
 * Created by 10057273 on 01-11-2013.
 */
public class MySMSManager {

    private SmsManager sms = null;
    private IMySMSManager callbackAPI = null;
    private Context app_context = null;
    
    //private int MAX_AVAILABLE = 1;
    //private final Semaphore semaphore = new Semaphore(MAX_AVAILABLE);
    
    private boolean resultState = false;


    public MySMSManager(Context app_context) {

        try {

            this.app_context = app_context;

            sms = SmsManager.getDefault();

            final Context localContext = app_context;

            app_context.registerReceiver(smsSentReceiver, new IntentFilter("SMS_SENT"));
            app_context.registerReceiver(smsDeliveredReceiver, new IntentFilter("SMS_DELIVERED"));

        } catch(Exception ex) {

        }
    }

    public void send_sms_async(String sms_text, String destNumber, String sourceNumber, IMySMSManager callbackAPI) {

        try {
            this.callbackAPI = callbackAPI;


            PendingIntent piSent = PendingIntent.getBroadcast(app_context, 0, new Intent("SMS_SENT"), 0);
            PendingIntent piDelivered = PendingIntent.getBroadcast(app_context, 0, new Intent("SMS_DELIVERED"), 0);
            sms.sendTextMessage(destNumber, sourceNumber, sms_text, piSent, piDelivered);

        } catch(Exception ex) {

        }

    }

    /*
    public boolean send_sms_sync(String sms_text, String destNumber, String sourceNumber) {

        resultState = false;
        this.callbackAPI = null;

        try {

            PendingIntent piSent = PendingIntent.getBroadcast(app_context, 0, new Intent("SMS_SENT"), 0);
            PendingIntent piDelivered = PendingIntent.getBroadcast(app_context, 0, new Intent("SMS_DELIVERED"), 0);
            sms.sendTextMessage(destNumber, sourceNumber, sms_text, piSent, piDelivered);
            semaphore.acquire();
            semaphore.acquire();


        } catch(Exception ex) {

        }

        return resultState;
    }
    */

    BroadcastReceiver smsSentReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {

            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    //Toast.makeText(app_context, "Enviada", Toast.LENGTH_SHORT).show();
                    if (callbackAPI != null) callbackAPI.report_sms_callback(ESMSState.SENT);
                    resultState = true;
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    //Toast.makeText(app_context, "Falha Generica", Toast.LENGTH_SHORT).show();
                    if (callbackAPI != null) callbackAPI.report_sms_callback(ESMSState.FAIL);
                    resultState = false;
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    //Toast.makeText(app_context, "Sem Serviço", Toast.LENGTH_SHORT).show();
                    if (callbackAPI != null) callbackAPI.report_sms_callback(ESMSState.NO_SERVICE);
                    resultState = false;
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    //Toast.makeText(app_context, "Null PDU", Toast.LENGTH_SHORT).show();
                    if (callbackAPI != null) callbackAPI.report_sms_callback(ESMSState.NULL_PDU);
                    resultState = false;
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    //Toast.makeText(app_context, "Radio Off", Toast.LENGTH_SHORT).show();
                    if (callbackAPI != null) callbackAPI.report_sms_callback(ESMSState.RADIO_OFF);
                    resultState = false;
                    break;
                default:
                    break;
            }

            //semaphore.release();
            //semaphore.release();
        }
    };

    BroadcastReceiver smsDeliveredReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            switch(getResultCode()) {
                case Activity.RESULT_OK:
                    //Toast.makeText(app_context, "Entregue ", Toast.LENGTH_SHORT).show();
                    if (callbackAPI != null) callbackAPI.report_sms_callback(ESMSState.DELIVERY);
                    break;
                case Activity.RESULT_CANCELED:
                    //Toast.makeText(app_context, "Não entregue", Toast.LENGTH_SHORT).show();
                    if (callbackAPI != null) callbackAPI.report_sms_callback(ESMSState.DELIVERY_FAIL);
                    break;
            }

            //semaphore.release();
            //semaphore.release();
        }
    };
}

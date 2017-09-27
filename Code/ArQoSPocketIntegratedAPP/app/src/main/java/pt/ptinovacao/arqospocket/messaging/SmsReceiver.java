package pt.ptinovacao.arqospocket.messaging;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SmsReceiver extends BroadcastReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsReceiver.class);

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus == null || pdus.length == 0) {
                return;
            }

            String sourceNumber = "";
            String format = "";
            List<String> contents = new ArrayList<>();

            for (int i = 0; i < pdus.length; i++) {
                SmsMessage message;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    format = bundle.getString("format");
                    message = getFromPdu(pdus, format, i);
                } else {
                    message = getFromPdu(pdus, i);
                }

                contents.add(Strings.emptyToNull(CharMatcher.whitespace().trimFrom(message.getMessageBody())));
                sourceNumber = message.getDisplayOriginatingAddress();
            }

            String messageBody = Joiner.on("").skipNulls().join(contents);
            int encode = calculateEncode(messageBody);
            SmsService.startActionProcessSms(context, sourceNumber, messageBody,
                    encode == -1 ? "" : String.valueOf(encode));
        }
    }

    private int calculateEncode(String messageBody) {
        /**
         * Sistema de Gestão
         * 0 – default (GSM 7-bit)
         * 1 – 8-bit
         * 2 – USC2 (16-bit)
         */
        int[] ints = SmsMessage.calculateLength(messageBody, false);
        switch (ints[3]) {
            case SmsMessage.ENCODING_7BIT:
                return 0;
            case SmsMessage.ENCODING_8BIT:
                return 1;
            case SmsMessage.ENCODING_16BIT:
                return 2;
            default:
                return -1;
        }
    }

    @SuppressWarnings("deprecation")
    private SmsMessage getFromPdu(Object[] pdus, int i) {
        return SmsMessage.createFromPdu((byte[]) pdus[i]);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private SmsMessage getFromPdu(Object[] pdus, String encoding, int i) {
        return SmsMessage.createFromPdu((byte[]) pdus[i], encoding);
    }

}

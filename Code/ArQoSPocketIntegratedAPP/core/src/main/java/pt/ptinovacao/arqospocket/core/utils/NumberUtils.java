package pt.ptinovacao.arqospocket.core.utils;

import android.net.Uri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pedro on 03/05/2017.
 */

public class NumberUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(NumberUtils.class);

    public static boolean isNumberToSendSms(String number) {

        number = number.replace("+", "00");

        String regex = "\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(number);

        return matcher.matches();
    }

    public static Uri isPhoneNumberValid(String phoneNumber) {
        final String phoneNumberPattern = "^(?:(?:00|\\+)\\d{1,3})?(?:\\d{5,9})$";
        boolean valid;
        try {
            Pattern pattern = Pattern.compile(phoneNumberPattern);
            Matcher matcher = pattern.matcher(phoneNumber);
            valid = matcher.matches();
            if (valid) {
                return Uri.parse("tel:" + phoneNumber);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }
}

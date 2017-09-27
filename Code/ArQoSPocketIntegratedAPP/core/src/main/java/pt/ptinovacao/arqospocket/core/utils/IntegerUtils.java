package pt.ptinovacao.arqospocket.core.utils;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by pedro on 05/06/2017.
 */

public class IntegerUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntegerUtils.class);

    public static Integer parseInt(String timeout) {
        if (Strings.isNullOrEmpty(timeout)) {
            return null;
        }

        try {
            return Integer.parseInt(timeout);
        } catch (NumberFormatException e) {
            LOGGER.error("The string does not contain a parsable integer.");
        }
        return null;
    }
}

package pt.ptinovacao.arqospocket.core.utils;

/**
 * Created by pedro on 26/04/2017.
 */
public class ParseNumberUtil {

    public static int parseNumber(String secondsString) {
        if (secondsString == null) {
            return 0;
        }

        int seconds;
        try {
            seconds = Integer.parseInt(secondsString);
        } catch (NumberFormatException e) {
            return 0;
        }
        return seconds;
    }
}

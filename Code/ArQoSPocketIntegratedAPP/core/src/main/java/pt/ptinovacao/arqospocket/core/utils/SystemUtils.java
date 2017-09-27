package pt.ptinovacao.arqospocket.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Tomás Rodrigues on 22/09/2017.
 */
public class SystemUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemUtils.class);

    public static Process requestPermission() {
        try {
            return Runtime.getRuntime().exec("su");
        } catch (IOException e) {
            LOGGER.error("The phone needs root: ", e.getMessage());
            return null;
        }
    }

    public static void runShellCommand(String command) throws Exception {
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
    }
}

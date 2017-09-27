package pt.ptinovacao.arqospocket.persistence.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hashing utils.
 * <p>
 * Created by Emílio Simões on 12-04-2017.
 */
public class Hashing {

    private static final Logger LOGGER = LoggerFactory.getLogger(Hashing.class);

    /**
     * Creates a MD5 hash of a string.
     *
     * @param source the source string.
     * @return the calculated hash.
     */
    public static String md5(final String source) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance(MD5);
            digest.update(source.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte digestByte : messageDigest) {
                String hexChar = Integer.toHexString(0xFF & digestByte);
                while (hexChar.length() < 2) {
                    hexChar = "0" + hexChar;
                }
                hexString.append(hexChar);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            LOGGER.debug("Error hashing the test contents", e);
        }
        return "";
    }
}

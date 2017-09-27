package pt.ptinovacao.arqospocket.core.messaging;

/**
 * Contains a received SMS data.
 * <p>
 * Created by Emílio Simões on 22-04-2017.
 */
public class ReceivedMessage {

    private String number;

    private String message;

    private String encoding;

    public ReceivedMessage(String number, String message, String encoding) {
        this.number = number;
        this.message = message;
        this.encoding = encoding;
    }

    public String getNumber() {
        return number;
    }

    public String getMessage() {
        return message;
    }

    public String getEncoding() {
        return encoding;
    }
}

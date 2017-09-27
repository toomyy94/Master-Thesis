package pt.ptinovacao.arqospocket.core.messaging;

/**
 * Interface to identify a class that listens for received SMS.
 * <p>
 * Created by Emílio Simões on 22-04-2017.
 */
public interface ReceiveSmsListener {

    /**
     * Is called when an SMS message is received.
     *
     * @param message the received SMS message.
     */
    void onSmsReceived(ReceivedMessage message);
}

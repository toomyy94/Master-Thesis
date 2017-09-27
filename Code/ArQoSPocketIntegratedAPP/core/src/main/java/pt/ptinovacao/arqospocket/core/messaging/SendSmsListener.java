package pt.ptinovacao.arqospocket.core.messaging;

/**
 * Identifies a class that receives notifications for an SMS event.
 * <p>
 * Created by Emílio Simões on 20-04-2017.
 */
public interface SendSmsListener {

    void onSmsSent(int resultCode);

    void onSmsDelivered(int resultCode);
}


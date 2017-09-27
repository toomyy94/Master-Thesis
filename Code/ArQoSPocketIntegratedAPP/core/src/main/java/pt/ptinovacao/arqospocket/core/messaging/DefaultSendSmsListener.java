package pt.ptinovacao.arqospocket.core.messaging;

/**
 * Empty implementation to avoid null references.
 * <p>
 * Created by Emílio Simões on 21-04-2017.
 */
class DefaultSendSmsListener implements SendSmsListener {

    @Override
    public void onSmsSent(int resultCode) {
    }

    @Override
    public void onSmsDelivered(int resultCode) {
    }
}

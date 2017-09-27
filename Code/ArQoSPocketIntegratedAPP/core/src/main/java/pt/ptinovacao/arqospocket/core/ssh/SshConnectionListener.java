package pt.ptinovacao.arqospocket.core.ssh;

/**
 * Interface to be implemented by any class that wishes to receive SSH connection events.
 * <p>
 * Created by Emílio Simões on 20-06-2017.
 */
public interface SshConnectionListener {

    /**
     * Is executed when the SSH connection is established with success.
     */
    void onSshSessionConnected();

    /**
     * Is executed when an error occurs while trying to establish an SSH connection.
     *
     * @param message the received error message.
     */
    void onSshConnectionFailed(String message);

    /**
     * Is executed when the SSH connection is terminated.
     */
    void onSshSessionDisconnected();
}


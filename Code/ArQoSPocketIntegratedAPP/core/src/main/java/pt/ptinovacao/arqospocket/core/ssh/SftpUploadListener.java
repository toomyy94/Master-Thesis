package pt.ptinovacao.arqospocket.core.ssh;

/**
 * Interface to be implemented by any class that wishes to receive SFTP upload events.
 * <p>
 * Created by Emílio Simões on 20-06-2017.
 */
public interface SftpUploadListener {

    /**
     * Is executed when a file upload starts.
     */
    void onUploadStarted();

    /**
     * Is executed when a file upload finishes with success.
     */
    void onUploadFinished();

    /**
     * Is executed when a file upload fails.
     *
     * @param message the received error message.
     */
    void onUploadFailed(String message);
}

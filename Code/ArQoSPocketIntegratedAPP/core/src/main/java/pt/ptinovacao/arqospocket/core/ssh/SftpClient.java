package pt.ptinovacao.arqospocket.core.ssh;

import java.io.File;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * SFTP client to upload files into an SFTP server.
 * <p>
 * Created by Emílio Simões on 26-06-2017.
 */
public class SftpClient {

    private final ConnectionInfo info;

    public SftpClient(ConnectionInfo info) {
        this.info = info;
    }

    public Single<Boolean> uploadFile(final File file, final String destination) {
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull final SingleEmitter<Boolean> emitter) throws Exception {
                final SshClient sshClient = new SshClient(info);
                sshClient.setListener(new ClientConnectionListener(sshClient, file, destination, emitter));
                sshClient.connect();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    private static class ClientUploadListener implements SftpUploadListener {

        private final SshClient sshClient;

        private final SingleEmitter<Boolean> emitter;

        ClientUploadListener(SshClient sshClient, SingleEmitter<Boolean> emitter) {
            this.sshClient = sshClient;
            this.emitter = emitter;
        }

        @Override
        public void onUploadStarted() {
        }

        @Override
        public void onUploadFinished() {
            sshClient.disconnect();
            emitter.onSuccess(true);
        }

        @Override
        public void onUploadFailed(String message) {
            emitter.onError(new RuntimeException(message));
        }
    }

    private static class ClientConnectionListener implements SshConnectionListener {

        private final SshClient sshClient;

        private final File file;

        private final String destination;

        private final SingleEmitter<Boolean> emitter;

        ClientConnectionListener(SshClient sshClient, File file, String destination, SingleEmitter<Boolean> emitter) {
            this.sshClient = sshClient;
            this.file = file;
            this.destination = destination;
            this.emitter = emitter;
        }

        @Override
        public void onSshSessionConnected() {
            sshClient.uploadFile(file, destination, new ClientUploadListener(sshClient, emitter));
        }

        @Override
        public void onSshConnectionFailed(String message) {
            emitter.onError(new RuntimeException(message));
        }

        @Override
        public void onSshSessionDisconnected() {
        }
    }
}

package pt.ptinovacao.arqospocket.core.ssh;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * SSH client to handle SSH connections and commands.
 * <p>
 * Created by Emílio Simões on 20-06-2017.
 */
public class SshClient implements SftpProgressMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SshClient.class);

    private final ConnectionInfo info;

    private SshConnectionListener listener;

    private JSch client;

    private Session session;

    private Disposable disposable = null;

    public SshClient(ConnectionInfo info, SshConnectionListener listener) {
        this.info = info;
        this.listener = listener;
    }

    SshClient(ConnectionInfo info) {
        this.info = info;
    }

    public void connect() {
        if (client != null) {
            throw new IllegalStateException("Client already connected or connecting");
        }

        client = new JSch();

        Single.create(new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<String> emitter) throws Exception {
                connectInternal(emitter);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String value) throws Exception {
                        LOGGER.debug("Connection result: {}", value);
                        notifyConnectionListener(value);
                    }
                });
    }

    public void disconnect() {
        if (session != null) {
            session.disconnect();
            session = null;
        }

        if (listener != null) {
            listener.onSshSessionDisconnected();
        }
    }

    public void uploadFile(final File file, final String destination, final SftpUploadListener uploadListener) {
        if (session == null || !session.isConnected()) {
            throw new IllegalStateException("Session is not established.");
        }

        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("File " + file + " is null or does not exist");
        }

        if (uploadListener != null) {
            uploadListener.onUploadStarted();
        }

        Single.create(new SingleOnSubscribe<UploadReport>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<UploadReport> emitter) throws Exception {
                uploadFileInternal(file, destination, emitter);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UploadReport>() {
                    @Override
                    public void accept(@NonNull UploadReport uploadReport) throws Exception {
                        notifyUploadListener(uploadReport, uploadListener);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        notifyUploadListener(UploadReport.fromThrowable(throwable, file), uploadListener);
                    }
                });
    }

    void setListener(SshConnectionListener listener) {
        this.listener = listener;
    }

    private void uploadFileInternal(File file, String destination, SingleEmitter<UploadReport> emitter) {
        try {
            ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
            channel.setInputStream(null);
            channel.connect();

            destination = Strings.nullToEmpty(destination);

            channel.put(file.getAbsolutePath(), destination, this, ChannelSftp.OVERWRITE);
            channel.disconnect();

            emitter.onSuccess(UploadReport.fromSuccess(file));
        } catch (Exception e) {
            emitter.onError(e);
        }
    }

    private void checkIfRemotePathExists(String destination, ChannelSftp channel) throws SftpException {
        if (destination.indexOf('/') > 0) {
            Iterable<String> pathsIterable = Splitter.on('/').omitEmptyStrings().split(destination);
            ArrayList<String> paths = Lists.newArrayList(pathsIterable);

            String current = channel.pwd();

            for (int i = 0; i < paths.size() - 1; i++) {
                String directoryName = paths.get(i);
                try {
                    channel.cd(directoryName);
                    LOGGER.debug("Directory {} is available", directoryName);
                } catch (SftpException e) {
                    LOGGER.debug("Directory {} does not exist, creating", directoryName);
                    channel.mkdir(directoryName);
                    channel.cd(directoryName);
                }
            }

            LOGGER.debug("Returning to {} directory", current);
            channel.cd(current);
        }
    }

    private void notifyUploadListener(UploadReport uploadReport, SftpUploadListener uploadListener) {
        if (uploadListener != null) {
            if (uploadReport.isSuccess()) {
                uploadListener.onUploadFinished();
            } else {
                uploadListener.onUploadFailed(uploadReport.getMessage());
            }
        }
    }

    private void connectInternal(final SingleEmitter<String> emitter) {
        try {
            session = client.getSession(info.getUsername(), info.getHost(), info.getPort());
            session.setUserInfo(info);

            Properties properties = new Properties();
            properties.setProperty("StrictHostKeyChecking", "no");
            session.setConfig(properties);

            session.connect();
        } catch (JSchException e) {
            emitter.onSuccess(e.getMessage());
            return;
        }

        if (session.isConnected()) {
            emitter.onSuccess("");
        } else {
            disposable = Flowable.interval(1, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                @Override
                public void accept(@NonNull Long seconds) throws Exception {
                    validateConnection(seconds, emitter);
                }
            });
        }
    }

    private void notifyConnectionListener(@NonNull String value) {
        if (listener != null) {
            if (Strings.nullToEmpty(value).length() > 0) {
                listener.onSshConnectionFailed(value);
            } else {
                listener.onSshSessionConnected();
            }
        }
    }

    private void validateConnection(@NonNull Long seconds, SingleEmitter<String> emitter) {
        if (session.isConnected()) {
            emitter.onSuccess("");
            if (disposable != null) {
                disposable.dispose();
            }
        } else if (seconds >= 30) {
            emitter.onSuccess("Connection has time out");
            if (disposable != null) {
                disposable.dispose();
            }
        }
    }

    @Override
    public void init(int op, String src, String dest, long max) {
        LOGGER.debug("SftpProgressMonitor::init --> {}, {}, {}, {}", op, src, dest, max);
    }

    @Override
    public boolean count(long count) {
        LOGGER.debug("SftpProgressMonitor::count --> {}", count);
        return true;
    }

    @Override
    public void end() {
        LOGGER.debug("SftpProgressMonitor::end");
    }

    private static class UploadReport {

        private final File file;

        private final String message;

        private final boolean success;

        UploadReport(File file, String message, boolean success) {
            this.file = file;
            this.message = message;
            this.success = success;
        }

        static UploadReport fromThrowable(Throwable throwable, File file) {
            return new UploadReport(file, throwable.getMessage(), false);
        }

        static UploadReport fromSuccess(File file) {
            return new UploadReport(file, null, true);
        }

        public File getFile() {
            return file;
        }

        String getMessage() {
            return message;
        }

        boolean isSuccess() {
            return success;
        }
    }
}

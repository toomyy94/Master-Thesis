package pt.ptinovacao.arqospocket.core.ssh;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.alarms.AlarmType;
import pt.ptinovacao.arqospocket.core.alarms.AlarmUtils;
import pt.ptinovacao.arqospocket.core.alarms.AlarmsManager;
import pt.ptinovacao.arqospocket.core.backoff.BackOffManager;
import pt.ptinovacao.arqospocket.core.http.client.HttpClient;
import pt.ptinovacao.arqospocket.core.http.client.RemoteServiceUrlManager;
import pt.ptinovacao.arqospocket.core.radiologs.RadiologsManager;
import pt.ptinovacao.arqospocket.core.settings.SharedPreferencesManager;
import pt.ptinovacao.arqospocket.core.utils.AlarmManagerUtils;
import pt.ptinovacao.arqospocket.core.utils.RandomNumberUtils;
import pt.ptinovacao.arqospocket.persistence.ExecutingEventDao;
import pt.ptinovacao.arqospocket.persistence.models.Radiolog;

/**
 * Created by Tomás on 27/06/2017.
 */

public class RadiologsAttachmentsProcessManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(RadiologsAttachmentsProcessManager.class);

    private static RadiologsAttachmentsProcessManager attachmentsProcessManager;

    private CoreApplication coreApplication;

    public static String baseDestinationSftp = "/opt/ptin/arqosng/data/probe/measureanalysis/";

    public static String username = "arqosng";

    public static String password = "desenarqos";

    public static String hostname = "arqos.ptinovacao.pt";

    private ConnectionInfo connectionInfo;

    private boolean running = false;

    private int timeDelay = BackOffManager.MIN_BACK_OFF;

    /**
     * false - mode send attachments
     * true - mode send notification post
     */
    private boolean mode = false;

    public static RadiologsAttachmentsProcessManager getInstance(CoreApplication coreApplication) {

        if (attachmentsProcessManager == null) {
            attachmentsProcessManager = new RadiologsAttachmentsProcessManager(coreApplication);
        }
        return attachmentsProcessManager;
    }

    public RadiologsAttachmentsProcessManager(CoreApplication coreApplication) {
        this.coreApplication = coreApplication;
    }

    public static void startSendAttachment(CoreApplication coreApplication) {
        RadiologsAttachmentsProcessManager instance = RadiologsAttachmentsProcessManager.getInstance(coreApplication);

        if (instance.isRunning()) {
            LOGGER.debug("In execution");
            return;
        }

        LOGGER.debug(""+SharedPreferencesManager.getInstance(coreApplication).getConnectionWithMSManual());

        if (!SharedPreferencesManager.getInstance(coreApplication).getConnectionWithMSManual()) {
            LOGGER.debug("Not started attachment process, it's blocked by user on the setting.");
            return;
        }

        instance.setRunning(true);
        instance.start();
        LOGGER.debug("Initialize");
    }

    public void send(File file, final Radiolog taskEvent) {
        LOGGER.debug("Sending File " + file.getName());

        new SftpClient(connectionInfo).uploadFile(file, baseDestinationSftp + file.getName())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean isSuccess) throws Exception {
                        if (isSuccess) {
                            LOGGER.debug("Sent file to the server");

                            ExecutingEventDao executingEventDao =
                                    coreApplication.getDatabaseHelper().createExecutingEventDao();

                            executingEventDao.updateAllRadiologsThatUploadFile(taskEvent.getId()).blockingGet();

                            resetTimeDelay();
                            mode = true;
                            start();

                        } else {
                            errorConnection();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        errorConnection();
                    }
                });
    }

    private void errorConnection() {
        createAlarm();
        addTimeOfSchedule();
    }

    public void notificationServer(final ArrayList<String> fileNames) {

        LOGGER.debug("Send File " + Arrays.toString(fileNames.toArray()));
        final HttpClient client = new HttpClient(coreApplication);
        Single.just(client).subscribeOn(Schedulers.newThread()).subscribe(new Consumer<HttpClient>() {
            @Override
            public void accept(@NonNull HttpClient httpClient) throws Exception {
                if (client.postProbeNotificationRadiologsAttachmentsProcess(fileNames).isSuccess()) {
                    ExecutingEventDao executingEventDao = coreApplication.getDatabaseHelper().createExecutingEventDao();
                    final List<Radiolog> taskEvents = executingEventDao.readAllRadiologsToSendNotification();

                    executingEventDao.updateAllRadiologsThatPostFileNotification(getIds(taskEvents)).blockingGet();

                    LOGGER.debug("Notification with success: {} with names {}", getIds(taskEvents),
                            getFileNames(taskEvents));
                    LOGGER.debug("Send notification to server");
                    resetTimeDelay();
                    mode = false;
                    start();

                } else {
                    errorConnection();
                }
            }
        });
    }

    public void stop() {
        setRunning(false);
        LOGGER.debug("stop");
    }

    public void start() {

        if (!isRunning()) {
            LOGGER.debug("Stopped");
            return;
        }

        configureServer();

        ExecutingEventDao executingEventDao = coreApplication.getDatabaseHelper().createExecutingEventDao();

        if (mode) {
            final List<Radiolog> taskEvents = executingEventDao.readAllRadiologsToSendNotification();

            if (taskEvents.size() > 0) {
                notificationServer(getFileNames(taskEvents));
            } else {
                if (checkPending()) {
                    mode = (executingEventDao.readAllRadiologsToSendNotification().size() > 0);
                    start();
                } else {
                    setRunning(false);
                }
            }
        } else {
            List<Radiolog> taskEvents = executingEventDao.readAllRadiologsToSendAttachments();
            if (taskEvents.size() > 0) {
                String nameFile = taskEvents.get(0).getNameFile();
                String path = RadiologsManager.completedPath(nameFile) + RadiologsManager.ZIP;
                LOGGER.debug(path);

                if (!Strings.isNullOrEmpty(nameFile) && validateFile(path)) {
                    send(new File(path), taskEvents.get(0));
                } else {
                    AlarmsManager.getInstance(coreApplication).generateAlarm(AlarmUtils.INICIO, AlarmType.A064.name(), AlarmType.A064.getAlarmContent(), Strings.nullToEmpty(nameFile));
                    LOGGER.debug("File not validate: {} {} ", Strings.nullToEmpty(nameFile), Strings.nullToEmpty(path));
                }
            } else {
                if (checkPending()) {
                    mode = true;
                    start();
                } else {
                    setRunning(false);
                }
            }
        }
    }

    private boolean checkPending() {
        ExecutingEventDao executingEventDao = coreApplication.getDatabaseHelper().createExecutingEventDao();

        if (executingEventDao.readAllRadiologsToSendAttachments().size() > 0 ||
                executingEventDao.readAllRadiologsToSendNotification().size() > 0) {
            return true;
        }
        return false;
    }

    private boolean validateFile(String path) {
        File file = new File(Strings.emptyToNull(path));
        if (!file.isDirectory()) {
            file = file.getParentFile();
        }
        if (file.exists()) {
            return true;
        }
        return false;
    }

    private void createAlarm() {
        LOGGER.debug("Creating alarm to attachment process");

        Intent intent = new Intent(coreApplication, AttachmentReceiver.class);
        intent.putExtra("", Boolean.FALSE);

        Calendar calendar = Calendar.getInstance();
        long alarmTime = calendar.getTimeInMillis() + (timeDelay * 1000);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(coreApplication, 0, intent, 0);

        AlarmManager manager = (AlarmManager) coreApplication.getSystemService(Context.ALARM_SERVICE);
        AlarmManagerUtils.setAlarm(pendingIntent, alarmTime, manager);
    }

    private ArrayList<String> getFileNames(List<Radiolog> taskEvents) {
        ArrayList<String> namesFiles = new ArrayList<>();
        for (Radiolog taskEvent : taskEvents) {
            String nameFile = taskEvent.getNameFile();
            if (!Strings.isNullOrEmpty(nameFile)) {
                namesFiles.add(taskEvent.getNameFile());
            }
        }
        return namesFiles;
    }

    private ArrayList<Long> getIds(List<Radiolog> taskEvents) {
        ArrayList<Long> namesFiles = new ArrayList<>();
        for (Radiolog taskEvent : taskEvents) {
            String nameFile = taskEvent.getNameFile();
            if (!Strings.isNullOrEmpty(nameFile)) {
                namesFiles.add(taskEvent.getId());
            }
        }
        return namesFiles;
    }

    public void resetTimeDelay() {
        this.timeDelay = BackOffManager.MIN_BACK_OFF;
    }

    private void addTimeOfSchedule() {
        if (timeDelay < BackOffManager.MAX_BACK_OFF) {
            LOGGER.debug(timeDelay + "");
            timeDelay += RandomNumberUtils.randInt0Until10();
            LOGGER.debug(timeDelay + "");
        }
    }

    public synchronized boolean isRunning() {
        return running;
    }

    public synchronized void setRunning(boolean running) {
        this.running = running;
    }

    public static class AttachmentReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            LOGGER.debug("onReceive");

            final CoreApplication applicationContext = ((CoreApplication) context.getApplicationContext());
            RadiologsAttachmentsProcessManager.getInstance(applicationContext).start();
        }
    }

    public void configureServer() {
        SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(coreApplication);

        URL u = null;
        try {

            String address = sharedPreferencesManager.getIpAddressManagement();
            if(Strings.isNullOrEmpty(address)) {
                address = RemoteServiceUrlManager.baseRequestUrl;
            }
            u = new URL(address);
        } catch (MalformedURLException e) {
            LOGGER.error("There is no address. It will use the default.");
        }

        if (u != null) {
            hostname = u.getHost();
        }

        String usernameSFTP = sharedPreferencesManager.getUsernameSFTP();
        String passwordSFTP = sharedPreferencesManager.getPasswordSFTP();
        if (!Strings.isNullOrEmpty(usernameSFTP) && !Strings.isNullOrEmpty(passwordSFTP)) {
            username = usernameSFTP;
            password = passwordSFTP;
        }

        connectionInfo = new ConnectionInfo(username, password, hostname);
    }
}
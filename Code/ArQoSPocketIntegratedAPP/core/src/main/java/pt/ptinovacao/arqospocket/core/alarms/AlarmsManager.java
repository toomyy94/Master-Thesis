package pt.ptinovacao.arqospocket.core.alarms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.alarms.data.AlarmParser;
import pt.ptinovacao.arqospocket.core.http.client.HttpClient;
import pt.ptinovacao.arqospocket.core.http.client.RemoteServiceUrlManager;
import pt.ptinovacao.arqospocket.core.http.client.response.ProbeNotificationResponse;
import pt.ptinovacao.arqospocket.core.keepalive.KeepAliveManager;
import pt.ptinovacao.arqospocket.core.location.GeoLocationManager;
import pt.ptinovacao.arqospocket.core.network.MobileNetworkManager;
import pt.ptinovacao.arqospocket.core.network.WifiNetworkManager;
import pt.ptinovacao.arqospocket.core.network.data.mobile.MobileInfoData;
import pt.ptinovacao.arqospocket.core.producers.JsonTestProducer;
import pt.ptinovacao.arqospocket.core.settings.SharedPreferencesManager;
import pt.ptinovacao.arqospocket.core.utils.DiskUtils;
import pt.ptinovacao.arqospocket.persistence.AlarmDao;
import pt.ptinovacao.arqospocket.persistence.ExecutingEventDao;
import pt.ptinovacao.arqospocket.persistence.models.Alarm;

import static android.content.Context.MODE_APPEND;

/**
 * Manager for the alarms.
 * <p>
 * Created by Tomás Rodrigues on 06-09-2017.
 */
public class AlarmsManager{
    private final static Logger LOGGER = LoggerFactory.getLogger(AlarmsManager.class);

    public static final String ALARMS_DIR = "resultados";
    public static final String INTERNAL_DIR = "data/user/0/pt.ptinovacao.arqospocket/files/";
    public static final String FILE_NAME = "TabAlarmes";
    public static final Integer ALARM_INTERVAL = 60;
    public static final Integer DEFAULT_MODULE = 0;
    private Boolean isOverheating = false;
    private Boolean isFull = false;

    // Output file path
    private String outputFilePath = null;
    public String outputFileName = null;

    // instances
    private static AlarmsManager instance;
    public final CoreApplication application;
    private MobileNetworkManager mobileNetworkManager;
    private WifiNetworkManager wifiManager;
    private MobileInfoData mobileInfoData;
    private GeoLocationManager geoLocationInfo;
    SharedPreferencesManager preferenceManager;
    BroadcastReceiver AlarmsReceiver;
    AlarmUtils alarmUtils = new AlarmUtils();

    //listeners

    //database
    private final AlarmDao alarmDao;
    private AlarmParser alarmParser = new AlarmParser();

    private AlarmsManager(CoreApplication application) {
        this.application = application;
        geoLocationInfo =  GeoLocationManager.getInstance(application);
        alarmDao = application.getDatabaseHelper().createAlarmDao();
    }

    public static AlarmsManager getInstance(CoreApplication application) {
        if (instance == null) {
            instance = new AlarmsManager(application);
        }
        return instance;
    }

    public void generateAlarm(String start_end, String alarmId, String alarm_content, String additional_info) {
        final String method = "generateAlarm";
        mobileNetworkManager = MobileNetworkManager.getInstance(application);
        mobileInfoData = mobileNetworkManager.getMobileInfoData();

        Alarm alarm = new Alarm();

        alarm.setOriginId(DEFAULT_MODULE);
        alarm.setGpsLocation(geoLocationInfo.getLocationInfo().format());
        alarm.setReportDate(Calendar.getInstance().getTime());
        alarm.setStartEnd(start_end);
        alarm.setAlarmId(alarmId);
        alarm.setAlarmContent(alarm_content);
        if (Strings.isNullOrEmpty(additional_info)) alarm.setAdditionalInfo(StringUtils.EMPTY);
        else alarm.setAdditionalInfo(additional_info);
        if (Strings.isNullOrEmpty(mobileInfoData.getIdCell())) alarm.setCellId(StringUtils.EMPTY);
        else alarm.setCellId(mobileInfoData.getIdCell());
        if (Strings.isNullOrEmpty(mobileInfoData.getSimIccid())) alarm.setIccid(StringUtils.EMPTY);
        else alarm.setIccid(mobileInfoData.getSimIccid());

        reportAlarm(alarm);
    }

    public void generateAlarm(String start_end, String alarmId, String alarm_content) {
        final String method = "generateAlarm";
        mobileNetworkManager = MobileNetworkManager.getInstance(application);
        mobileInfoData = mobileNetworkManager.getMobileInfoData();

        Alarm alarm = new Alarm();

        alarm.setOriginId(DEFAULT_MODULE);
        alarm.setGpsLocation(geoLocationInfo.getLocationInfo().format());
        alarm.setReportDate(Calendar.getInstance().getTime());
        alarm.setStartEnd(start_end);
        alarm.setAlarmId(alarmId);
        alarm.setAlarmContent(alarm_content);
        alarm.setAdditionalInfo(StringUtils.EMPTY);
        if (Strings.isNullOrEmpty(mobileInfoData.getIdCell())) alarm.setCellId(StringUtils.EMPTY);
        else alarm.setCellId(mobileInfoData.getIdCell());
        if (Strings.isNullOrEmpty(mobileInfoData.getSimIccid())) alarm.setIccid(StringUtils.EMPTY);
        else alarm.setIccid(mobileInfoData.getSimIccid());

        reportAlarm(alarm);
    }

    public void reportAlarm(Alarm alarm) {
        alarm.setReported(false);
        alarm.setNotificationSent(false);

        alarmToFile(alarm);

        alarm.setNameFile(outputFileName);
        LOGGER.debug(alarm.toString());

        alarmDao.saveAlarm(alarm).subscribe(new Consumer<Alarm>() {
            @Override
            public void accept(@NonNull Alarm alarm) throws Exception {
                LOGGER.debug("Created alarm");
            }
        });

        startSendNotification();
    }
    public void startSendNotification(){
        if (SharedPreferencesManager.getInstance(application).getConnectionWithMSManual()) {
            ExecutingEventDao executingEventDao = application.getDatabaseHelper().createExecutingEventDao();
            final List<Alarm> pendingAlarms = executingEventDao.readAllAlarmsToSendNotification();

            deliverAlarms(pendingAlarms);
        }
    }

    void deliverAlarms(final List<Alarm> pendingAlarms) {
        Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                processAndDeliverAlarms(pendingAlarms);
                return true;
            }
        }).subscribeOn(Schedulers.newThread()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Boolean result) throws Exception {
                LOGGER.debug("Result sent to server");
            }
        });
    }

    private void processAndDeliverAlarms(List<Alarm> pendingAlarms) {
        if (pendingAlarms.size() == 0) {
            return;
        }

        sendAlarms(pendingAlarms);
    }

    private void sendAlarms(List<Alarm> pendingAlarms) {
        LOGGER.debug("Sending {} alarms:", pendingAlarms.size());
        ExecutingEventDao executingEventDao = application.getDatabaseHelper().createExecutingEventDao();

        JsonObject data = alarmParser.alarmResultToStrings(pendingAlarms);

        HttpClient client = new HttpClient(application);
        ProbeNotificationResponse response =
                client.postProbeNotificationResultAlarms(data, RemoteServiceUrlManager.getInstance(application).urlAlarmsProcess());

        LOGGER.debug("Received response: [{}] {}", response.getCode(), response.getEntity());
        if (response.isSuccess()) {
            executingEventDao.updateAllAlarmsThatPostNotification(getIds(pendingAlarms));
            KeepAliveManager.sendBroadcastReceiver(application);
        }
    }

    private ArrayList<Long> getIds(List<Alarm> pendingAlarms) {
        ArrayList<Long> alarmIds = new ArrayList<>();
        for (Alarm alarm : pendingAlarms) {
            String nameFile = alarm.getNameFile();
            if (!Strings.isNullOrEmpty(nameFile)) {
                alarmIds.add(alarm.getId());
            }
        }
        return alarmIds;
    }

    public void deliverPendingAlarms() {
        ExecutingEventDao executingEventDao = application.getDatabaseHelper().createExecutingEventDao();
        processAndDeliverAlarms(executingEventDao.readAllAlarmsToSendNotification());
    }

    public void alarmToFile(Alarm alarm){

        setOutputFile(FILE_NAME);
        String pipedAlarm = alarmParser.databaseAlarmToPipes(alarm);

        try {
            FileOutputStream fou = application.openFileOutput(outputFileName, MODE_APPEND);
            OutputStreamWriter alarmStream = new OutputStreamWriter(fou);
            alarmStream.write(pipedAlarm);
            alarmStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        File file = new File(INTERNAL_DIR+outputFileName);
        String destFileName = INTERNAL_DIR+outputFileName;

        copyFile(destFileName, outputFilePath);

        //file.delete();
    }

    /**
     * Sets output file path, call directly after construction/reset.
     *
     * @param nameFile file path
     */
    public void setOutputFile(String nameFile) {
        try {
            nameFile += mobileNetworkManager.getDeviceImei();

            outputFileName = nameFile;
            outputFilePath = completedPath(nameFile);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    public static String completedPath(String nameFile) {
        return checkDirectory() + File.separator + nameFile;
    }

    @NonNull
    public static String checkDirectory() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
                JsonTestProducer.BASE_DIR;
        File alarmsDir = new File(path, ALARMS_DIR);
        if (!alarmsDir.exists()) {
            alarmsDir.mkdirs();
        }
        return path + File.separator + ALARMS_DIR;
    }

    private void copyFile(String inputPath, String outputPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(inputPath);
            out = new FileOutputStream(outputPath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

            LOGGER.debug("Copied file to "+outputPath);

        } catch (FileNotFoundException fnfe1) {
            LOGGER.error(fnfe1.getMessage());
        } catch (Exception e) {
            LOGGER.error("tag", e.getMessage());
        }
    }

    public void generateAlarms() {

        //A008 - Temperature
        int cpuTemperature = alarmUtils.getCpuTemp();
        if(cpuTemperature > preferenceManager.getMaxTemperature() && !isOverheating){
            generateAlarm(AlarmUtils.INICIO, AlarmType.A008.name(), AlarmType.A008.getAlarmContent(), "temp_act="+cpuTemperature + " temp_max="+preferenceManager.getMaxTemperature());
            isOverheating = true;
        }
        else if(cpuTemperature < preferenceManager.getMaxTemperature() && isOverheating){
            generateAlarm(AlarmUtils.FIM, AlarmType.A008.name(), AlarmType.A008.getAlarmContent(), "temp_act="+cpuTemperature + " temp_max="+preferenceManager.getMaxTemperature());
            isOverheating = false;
        }

        //A012 - DiskFree
        int diskOccupied = (int) (100 - DiskUtils.percentageFreeSpace());
        if(diskOccupied > preferenceManager.getPercentageMemoryOccupied() && !isFull){
            generateAlarm(AlarmUtils.INICIO, AlarmType.A012.name(), AlarmType.A012.getAlarmContent(), "df_act="+diskOccupied + " df="+preferenceManager.getPercentageMemoryOccupied());
            isFull = true;
        }
        else if(diskOccupied < preferenceManager.getPercentageMemoryOccupied() && isFull){
            generateAlarm(AlarmUtils.FIM, AlarmType.A012.name(), AlarmType.A012.getAlarmContent(), "df_act="+diskOccupied + " df="+preferenceManager.getPercentageMemoryOccupied());
            isFull = false;
        }
    }

    public static class AlarmsReceiver extends BroadcastReceiver {
        /**
         * This refers to com.android.internal.telehpony.IccCardConstants.INTENT_KEY_ICC_STATE.
         * It seems not possible to refer it through a builtin class like TelephonyManager, so we
         * define it here manually.
         */
        private static final String EXTRA_SIM_STATE = "ss";
        private Boolean isAbsent = true;


        @Override
        public void onReceive(Context context, Intent intent) {
            final CoreApplication applicationContext = ((CoreApplication) context.getApplicationContext());


            if (intent.getAction().equals("android.intent.action.SIM_STATE_CHANGED")) {
                String state = intent.getExtras().getString(EXTRA_SIM_STATE);
                if (state == null) {
                    return;
                }

                // Do stuff depending on state
                switch (state) {
                    case "ABSENT":
                        AlarmsManager.getInstance(applicationContext).generateAlarm(AlarmUtils.INICIO, AlarmType.A049.name(), AlarmType.A049.getAlarmContent(),
                                        "SIM REMOVED");
                        isAbsent = true;
                        break;
                    case "NETWORK_LOCKED":
                        AlarmsManager.getInstance(applicationContext).generateAlarm(AlarmUtils.INICIO, AlarmType.A055.name(), AlarmType.A055.getAlarmContent(),
                                "Phone locked, only emergency numbers are allowed.");
                        break;
                    case "PIN_REQUIRED":
                        AlarmsManager.getInstance(applicationContext).generateAlarm(AlarmUtils.INICIO, AlarmType.A055.name(), AlarmType.A055.getAlarmContent(),
                                "Phone locked, only emergency numbers are allowed.");
                        break;
                    case "PUK_REQUIRED":
                        AlarmsManager.getInstance(applicationContext).generateAlarm(AlarmUtils.INICIO, AlarmType.A055.name(), AlarmType.A055.getAlarmContent(),
                                "Phone locked, only emergency numbers are allowed.");
                        break;
                    case "READY":
                        AlarmsManager.getInstance(applicationContext)
                                .generateAlarm(AlarmUtils.FIM, AlarmType.A055.name(), AlarmType.A055.getAlarmContent());

//                        if (isAbsent == true) {
                            AlarmsManager.getInstance(applicationContext).generateAlarm(AlarmUtils.FIM, AlarmType.A049.name(), AlarmType.A049.getAlarmContent(),
                                    "SIM INSERTED");
                            isAbsent = false;
//                        }
                        break;
                    case "UNKNOWN":
                        break;
                }
            }

//            if (intent.getAction().equals("android.intent.action.SERVICE_STATE")) {
//                ServiceState serviceState = new ServiceState();
//                switch (serviceState.getState()) {
//                    case 0:
//                        AlarmsManager.getInstance(applicationContext).generateAlarm(AlarmUtils.FIM, AlarmType.A055.name(), AlarmType.A055.getAlarmContent());
//                        break;
//                    case 1:
//                        AlarmsManager.getInstance(applicationContext).generateAlarm(AlarmUtils.INICIO, AlarmType.A055.name(), AlarmType.A055.getAlarmContent());
//                        break;
//                    case 2:
//                        AlarmsManager.getInstance(applicationContext).generateAlarm(AlarmUtils.INICIO, AlarmType.A055.name(), AlarmType.A055.getAlarmContent(),
//                                "Phone locked, only emergency numbers are allowed.");
//                        break;
//                    case 3:
//                        AlarmsManager.getInstance(applicationContext).generateAlarm(AlarmUtils.INICIO, AlarmType.A040.name(), AlarmType.A040.getAlarmContent());
//                        break;
//
//                }
//            }
        }
    }

    public void generatePeriodicAlarms() {
        preferenceManager = SharedPreferencesManager.getInstance(application);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                //DO SOMETHING
                generateAlarms();
                handler.postDelayed(this, ALARM_INTERVAL * 1000);
            }
        }, ALARM_INTERVAL * 1000);

//        Timer timer = new Timer();
//        TimerTask doAsynchronousTask = new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    generateAlarms();
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        timer.schedule(doAsynchronousTask, 0,
//                ALARM_INTERVAL * 1000); //execute in every xxxxx ms


    }
}

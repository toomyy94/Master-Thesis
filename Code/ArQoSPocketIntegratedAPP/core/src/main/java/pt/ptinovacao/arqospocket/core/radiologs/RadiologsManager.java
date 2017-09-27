package pt.ptinovacao.arqospocket.core.radiologs;

import android.content.Context;
import android.media.AudioManager;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.zip.GZIPOutputStream;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.location.GeoLocationManager;
import pt.ptinovacao.arqospocket.core.network.MobileNetworkManager;
import pt.ptinovacao.arqospocket.core.network.MobileNetworkMode;
import pt.ptinovacao.arqospocket.core.network.WifiNetworkManager;
import pt.ptinovacao.arqospocket.core.network.data.mobile.MobileInfoData;
import pt.ptinovacao.arqospocket.core.network.data.wifi.WifiInfoData;
import pt.ptinovacao.arqospocket.core.producers.JsonTestProducer;
import pt.ptinovacao.arqospocket.core.settings.SharedPreferencesManager;
import pt.ptinovacao.arqospocket.core.ssh.RadiologsAttachmentsProcessManager;
import pt.ptinovacao.arqospocket.core.ssh.ScanlogsAttachmentsProcessManager;
import pt.ptinovacao.arqospocket.core.utils.DateUtils;
import pt.ptinovacao.arqospocket.core.utils.ParseNumberUtil;
import pt.ptinovacao.arqospocket.persistence.RadiologDao;
import pt.ptinovacao.arqospocket.persistence.models.Radiolog;

import static android.content.Context.MODE_APPEND;

/**
 * Manager for the radiologs.
 * <p>
 * Created by Tomás Rodrigues on 23-06-2017.
 */
public class RadiologsManager {
    private final static Logger LOGGER = LoggerFactory.getLogger(RadiologsManager.class);

    //final variables
    public static final String RADIOLOGS_DIR = "resultados/anexo";
    public static final String INTERNAL_DIR = "data/user/0/pt.ptinovacao.arqospocket/files/";
    public static final String ZIP = ".gz";
    private final static int WRONG_DEFAULT_VALUE = 2147483647;
    private final static int WRONG_DEFAULT_CELLID = 65535;
    private final static String RADIOLOG = "Radiolog";
    private final static String EVENT = "Event";
    private final static String SNAPSHOT = "Snapshot";
    private final static String SCANLOG = "Scanlog";

    //Variables
    private String outputFilePath = null;
    public String outputFileName = null;
    public String outputZippedFileName = null;
    public boolean isGenerating = false;
    public int cellId;

    //Instances
    private static RadiologsManager instance;
    public final CoreApplication application;
    private final RadiologDao radiologDao;
    private MobileNetworkManager mobileNetworkManager;
    private MobileInfoData mobileInfoData;
    private WifiNetworkManager wifiManager;
    private WifiInfoData wifiInfoData;

    private GeoLocationManager geoLocationInfo;
    SharedPreferencesManager preferenceManager;

    private RadiologsManager(CoreApplication application) {
        this.application = application;
        radiologDao = application.getDatabaseHelper().createRadiologDao();
        geoLocationInfo =  GeoLocationManager.getInstance(application);
    }

    public static RadiologsManager getInstance(CoreApplication application) {
        if (instance == null) {
            instance = new RadiologsManager(application);
        }
        return instance;
    }

    public String generateSnapshot(String user_feedback) {
        final String method = "generateSnapshot";
        mobileNetworkManager = MobileNetworkManager.getInstance(application);
        mobileInfoData = mobileNetworkManager.getMobileInfoData();
        JSONObject log;
        JSONObject radiologContent = new JSONObject();
        JSONArray radiolog_Array = new JSONArray();
        try {
            log = fillRadiologContent();

            //Snapshot
            log.put("feedback", user_feedback);

            radiolog_Array.put(log);
            radiologContent.put("radiolog", radiolog_Array);

        }catch (JSONException e){
            Log.e("Error","Error generating snapshot.");
            e.printStackTrace();
        }

        return radiologContent.toString();
    }

    public void generateRadiolog() {
        final String method = "generateRadiolog";
        JSONObject log;
        JSONObject radiologContent = new JSONObject();
        JSONArray radiolog_Array = new JSONArray();
        mobileNetworkManager = MobileNetworkManager.getInstance(application);
        mobileInfoData = mobileNetworkManager.getMobileInfoData();
        if (mobileNetworkManager != null && mobileNetworkManager.isMobileAvailable() && mobileNetworkManager.getConnectionMode() != 6 && mobileInfoData.getConnectionMode() != 6 &&
                mobileNetworkManager.getConnectionMode() != 7 &&  mobileInfoData.getMobileNetworkMode() != MobileNetworkMode.NONE  &&  mobileInfoData.getConnectionMode() != 7 ) {
            try {
                Radiolog radiolog = new Radiolog();
                radiolog.setReportType(RADIOLOG);
                log = fillRadiologContent();
                radiolog_Array.put(log);
                radiologContent.put("radiolog", radiolog_Array);

                radiolog.setRadiologContent(radiologContent.toString());
                reportRadiolog(radiolog);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else return;
    }

    public void generateRadiolog(EEvent type, String origin) {
        final String method = "generateRadiologEvent";
        JSONObject log;
        JSONObject radiologContent = new JSONObject();
        JSONArray radiolog_Array = new JSONArray();
        mobileNetworkManager = MobileNetworkManager.getInstance(application);
        mobileInfoData = mobileNetworkManager.getMobileInfoData();

        if (mobileNetworkManager != null && mobileNetworkManager.isMobileAvailable() && mobileNetworkManager.getConnectionMode() != 6 && mobileInfoData.getConnectionMode() != 6 &&
                mobileNetworkManager.getConnectionMode() != 7 &&  mobileInfoData.getMobileNetworkMode() != MobileNetworkMode.NONE  &&  mobileInfoData.getConnectionMode() != 7 ) {
            try {
                Radiolog radiolog = new Radiolog();
                radiolog.setReportType(EVENT);
                log = fillRadiologContent();

                //Event
                JSONObject eventlog = new JSONObject();
                eventlog.put("type", getEEventToInt(type));
                eventlog.put("origin", origin);
                log.put("event", eventlog);

                radiolog_Array.put(log);
                radiologContent.put("radiolog", radiolog_Array);

                radiolog.setRadiologContent(radiologContent.toString());
                reportRadiolog(radiolog);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else return;
    }

    public void generate_scanlog() {
        final String method = "generate_scanlog";
        JSONObject scanlogContent = new JSONObject();
        JSONObject log = new JSONObject();
        JSONArray scanlog_Array = new JSONArray();
        mobileNetworkManager = MobileNetworkManager.getInstance(application);
        mobileInfoData = MobileNetworkManager.getInstance(application).getMobileInfoData();
        wifiManager = WifiNetworkManager.getInstance(application);

        if (mobileNetworkManager != null && mobileNetworkManager.getConnectionMode() != 7 && mobileNetworkManager.getConnectionMode() != 6 &&
                mobileNetworkManager.isMobileAvailable() && wifiManager.isWifiAvailable() && wifiManager != null && mobileInfoData.getMobileNetworkMode() != MobileNetworkMode.NONE) {
            try {
                Radiolog scanlog = new Radiolog();
                scanlog.setReportType(SCANLOG);
                log = fillScanlogContent();

                scanlog_Array.put(log);
                scanlogContent.put("scanlog", scanlog_Array);

                scanlog.setRadiologContent(scanlogContent.toString());
                reportRadiolog(scanlog);
            }catch(JSONException jsone){
                Log.e("Error", "Error generating scanlog.");
                jsone.printStackTrace();
            }
        }
        else return;
    }

    public void reportRadiolog(Radiolog radiolog) {
        radiolog.setLatitude(geoLocationInfo.getLocation().getLatitude());
        radiolog.setLongitude(geoLocationInfo.getLocation().getLongitude());
        radiolog.setReportDate(Calendar.getInstance().getTime());
        radiolog.setReported(false);

        LOGGER.debug(radiolog.toString());

        radiolog.setNotificationSent(false);
        radiolog.setUpload(false);
        radiolog.setNameFile(outputFileName);

        radiologToFile(radiolog);

        radiologDao.saveRadiolog(radiolog).subscribe(new Consumer<Radiolog>() {
            @Override
            public void accept(@NonNull Radiolog radiolog) throws Exception {
                if(radiolog.getReportType() != "Scanlog") LOGGER.debug("Created radiolog");
                else LOGGER.debug("Created scanlog");
            }
        });

        if(radiolog.getReportType() != SCANLOG) RadiologsAttachmentsProcessManager.startSendAttachment(application);
        else ScanlogsAttachmentsProcessManager.startSendAttachment(application);
    }

    public int getEEventToInt(EEvent type){
        switch (type){
            case HANDOVER:
                return 1;
            case REGISTER_STATUS:
                return 2;
            case PLMN_CHANGE:
                return 3;
            case ROAMING_STATUS:
                return 4;
            case PAUSE_SCAN:
                return 5;
            case RESUME_SCAN:
                return 6;
            case NO_NETWORKS_DETECTED:
                return 7;
            case CELL_RESELECTION:
                return 8;
            case CALL_SETUP:
                return 9;
            case EMON_POCKET:
                return 10;
            case CALL_ESTABLISHED:
                return 11;
            case CALL_END:
                return 12;
            case CALL_DROP:
                return 13;
            case CALL_RELEASE:
                return 14;
        }
        return -1;
    }

    public String getIntToEEvent(Integer type){
        switch (type){
            case 1:
                return EEvent.HANDOVER.toString();
            case 2:
                return EEvent.REGISTER_STATUS.toString();
            case 3:
                return EEvent.PLMN_CHANGE.toString();
            case 4:
                return EEvent.ROAMING_STATUS.toString();
            case 5:
                return EEvent.PAUSE_SCAN.toString();
            case 6:
                return EEvent.RESUME_SCAN.toString();
            case 7:
                return EEvent.NO_NETWORKS_DETECTED.toString();
            case 8:
                return EEvent.CELL_RESELECTION.toString();
            case 9:
                return EEvent.CALL_SETUP.toString();
            case 10:
                return EEvent.EMON_POCKET.toString();
            case 11:
                return EEvent.CALL_ESTABLISHED.toString();
            case 12:
                return EEvent.CALL_END.toString();
            case 13:
                return EEvent.CALL_DROP.toString();
            case 14:
                return EEvent.CALL_RELEASE.toString();
        }
        return "No Event";
    }

    public String getIntToStatus(Integer status){
        switch (status){
            case -1:
                return "Unknown";
            case 0:
                return "Available";
            case 1:
                return "Limited";
            case 2:
                return "No Service";
            case 3:
                return "Offline";
        }
        return "Unknown";
    }

    public String getIntToRoaming(Integer roaming){
        switch (roaming){

            case 0:
                return "Off";
            case 1:
                return "Active";
        }
        return "Off";
    }

    public String getIntToMode(Integer mode){
        switch (mode){
            case 0:
                return "GPRS";
            case 1:
                return "EDGE";
            case 2:
                return "UMTS";
            case 3:
                return "HSDPA";
            case 4:
                return "HSUPA";
            case 5:
                return "HSPA";
            case 6:
                return "NONE";
            case 7:
                return "Unknown";
            case 8:
                return "HSPA+";
            case 9:
                return "DC-HSPA+";
            case 10:
                return "LTE";
        }
        return "Unknown";
    }

    public JSONObject fillRadiologContent(){
        JSONObject log = new JSONObject();

        try {
            log.put("module", 0);
            log.put("iccid", mobileInfoData.getSimIccid());
            log.put("imsi", mobileNetworkManager.getDeviceImsi());
            log.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000L));
            log.put("mac", mobileNetworkManager.getDeviceImei()); //IMEI
            log.put("gps", geoLocationInfo.getLocation().getLatitude()+","+geoLocationInfo.getLocation().getLongitude());

            //network
            JSONObject networklog = new JSONObject();

            if(mobileNetworkManager.isMobileAvailable()) networklog.put("status", 0);
            else networklog.put("status", mobileNetworkManager.getSimState());
            networklog.put("mode", mobileNetworkManager.getConnectionMode());
            networklog.put("cellid", ParseNumberUtil.parseNumber(mobileInfoData.getIdCell()));
            networklog.put("plmn", mobileInfoData.getNetworkOperatorName());
            if(!mobileInfoData.isRoaming()) networklog.put("roaming", 0);
            else networklog.put("roaming", 1);
            networklog.put("rssi", mobileInfoData.getSignalLevel());
            networklog.put("mcc", Integer.parseInt(mobileInfoData.getMcc()));
            networklog.put("mnc", Integer.parseInt(mobileInfoData.getMccMnc()));

            //For LTE
            if(mobileInfoData.getConnectionMode() == 10){
                networklog.put("pci", mobileNetworkManager.getPhysicalCellId());
                networklog.put("tac", ParseNumberUtil.parseNumber(mobileInfoData.getCellLocation()));
                networklog.put("rsrp", mobileNetworkManager.getReferenceSignalReceivedPower());
                networklog.put("rsrq", mobileNetworkManager.getReferenceSignalReceivedQuality());
                if(mobileNetworkManager.getRssnr() < WRONG_DEFAULT_VALUE ) networklog.put("rssnr", mobileNetworkManager.getRssnr());
                if(mobileNetworkManager.getChannelQualityIndicator() < WRONG_DEFAULT_VALUE) networklog.put("cqi", mobileNetworkManager.getChannelQualityIndicator());
                //                if(mobile_task.getLteRssi() != -1) networklog.put("rssi", mobile_task.getLteRssi());

            }
            else{
                networklog.put("ber", mobileNetworkManager.getBitErrorRate());
                networklog.put("lac", ParseNumberUtil.parseNumber(mobileInfoData.getCellLocation()));
                if(networklog.has("psc")) networklog.remove("psc");
                if(mobileInfoData.getConnectionMode() == 2){
                    networklog.put("psc", mobileNetworkManager.getPrimaryScramblingCode());
                }
            }

            log.put("network", networklog);

            //TODO DEBUG
            LOGGER.info(mobileInfoData.getAllCellInfoList().toString());

            //neighbours
            if(mobileInfoData.getAllCellInfoList() != null && mobileInfoData.getAllCellInfoList().size()> 1) {
                JSONArray neighbourslog = new JSONArray();

                for (int i = 1; i < mobileInfoData.getAllCellInfoList().size(); i++) {
                    JSONObject one_neighbour = new JSONObject();

                    if(mobileNetworkManager.getNeigCellId(mobileInfoData.getAllCellInfoList()).get(i) != WRONG_DEFAULT_CELLID) one_neighbour.put("cellid", mobileNetworkManager.getNeigCellId(mobileInfoData.getAllCellInfoList()).get(i));
                    else continue;

                    if (mobileInfoData.getConnectionMode() == 10) {
                        if(mobileNetworkManager.getNeigTac(mobileInfoData.getAllCellInfoList()).get(i) < WRONG_DEFAULT_VALUE ) one_neighbour.put("tac", mobileNetworkManager.getNeigTac(mobileInfoData.getAllCellInfoList()).get(i));
                        one_neighbour.put("pci", mobileNetworkManager.getNeigPci(mobileInfoData.getAllCellInfoList()).get(i));
                        one_neighbour.put("rsrp", mobileNetworkManager.getNeigRsrp(mobileInfoData.getAllCellInfoList()).get(i));
                        one_neighbour.put("rsrq", mobileNetworkManager.getNeigRsrq(mobileInfoData.getAllCellInfoList()).get(i));
                        if(mobileNetworkManager.getNeigRssnr(mobileInfoData.getAllCellInfoList()).get(i) < WRONG_DEFAULT_VALUE ) one_neighbour.put("rssnr", mobileNetworkManager.getNeigRssnr(mobileInfoData.getAllCellInfoList()).get(i));
                    } else if (mobileInfoData.getConnectionMode() == 2) {
                        //                        one_neighbour.put("lac", mobileNetworkManager.getNeigLac().get(i));
                        one_neighbour.put("psc", mobileNetworkManager.getNeigPsc(mobileInfoData.getAllCellInfoList()).get(i));
                    }
                    neighbourslog.put(one_neighbour);
                }
                log.put("neighbours", neighbourslog);
            }
        }catch (JSONException jsone){
            Log.e("Error","Error generating radiolog.");
            jsone.printStackTrace();
        }
        return log;
    }

    public JSONObject fillScanlogContent(){
        JSONObject log = new JSONObject();
        wifiInfoData = wifiManager.getWifiInfoData();

        try {
            log.put("module", 0);
            log.put("iccid", mobileInfoData.getSimIccid());
            log.put("imsi", mobileNetworkManager.getDeviceImsi());
            log.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000L));
            log.put("mac", mobileNetworkManager.getDeviceImei()); //IMEI
            log.put("gps", geoLocationInfo.getLocation().getLatitude() + "," + geoLocationInfo.getLocation().getLongitude());

            //network
            JSONObject networklog = new JSONObject();

            networklog.put("bssid", wifiInfoData.getBssid());
            //            networklog.put("hidden_ssid", w_advanced.get_hidden_ssid());
            networklog.put("mac_address", wifiInfoData.getMacAddress());
            networklog.put("ip_address", wifiInfoData.getIpAddress());
            networklog.put("dns1", wifiInfoData.getDns1());
            networklog.put("dns2", wifiInfoData.getDns2());
            networklog.put("gateway", wifiInfoData.getGateway());
            networklog.put("lease_duration", wifiInfoData.getLeaseDuration());
            networklog.put("netmask", wifiInfoData.getNetMask());
            networklog.put("server_address", wifiInfoData.getServerAddress());
            networklog.put("wireless_state", wifiInfoData.getWifiState());
            networklog.put("rxlevel", wifiInfoData.getRxLevel());
            networklog.put("essid", wifiInfoData.getSsid().replace("\"", ""));
            networklog.put("channel", ParseNumberUtil.parseNumber(wifiInfoData.getChannel()));
            networklog.put("link_speed", wifiInfoData.getLinkSpeed());
//            networklog.put("scan_Wifi_list", wifiInfoData.getScanWifiList());

            log.put("network", networklog);
        }catch (JSONException jsone){
                Log.e("Error","Error generating scanlog.");
                jsone.printStackTrace();
        }

        return log;
    }

    public void gzipFile(String outputZippedFile, String inputFile){

        byte[] buffer = new byte[1024];

        try{

            GZIPOutputStream gzos =
                    new GZIPOutputStream(new FileOutputStream(outputZippedFile));

            FileInputStream in =
                    new FileInputStream(inputFile);

            int len;
            while ((len = in.read(buffer)) > 0) {
                gzos.write(buffer, 0, len);
            }

            in.close();

            gzos.finish();
            gzos.close();

            System.out.println("Done");

        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public void radiologToFile(Radiolog radiolog){

        if(radiolog.getReportType() != SCANLOG) setOutputFile(RADIOLOG.toLowerCase());
        else setOutputFile(SCANLOG.toLowerCase());

        try {
            FileOutputStream fou = application.openFileOutput(outputFileName, MODE_APPEND);
            OutputStreamWriter radiologStream = new OutputStreamWriter(fou);
            radiologStream.write(radiolog.getRadiologContent());
            radiologStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        File internalFile = new File(INTERNAL_DIR+outputFileName);
        String destFileName = INTERNAL_DIR+outputFileName;

        copyFile(destFileName, outputFilePath);
        gzipFile(outputZippedFileName,outputFilePath);

        File unzippedFile = new File(outputFilePath);
        //Delete Files
        unzippedFile.delete();
        internalFile.delete();
    }

    /**
     * Sets output file path, call directly after construction/reset.
     *
     * @param nameFile file path
     */
    public void setOutputFile(String nameFile) {
        try {
            nameFile = nameFile+"_"+mobileNetworkManager.getDeviceImei()+"_"+DateUtils.GetUTCdatetimeAsString();

            outputFileName = nameFile;
            outputFilePath = completedPath(nameFile);
            outputZippedFileName = outputFilePath + ZIP;
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
        File radiologsDir = new File(path, RADIOLOGS_DIR);
        if (!radiologsDir.exists()) {
            radiologsDir.mkdirs();
        }
        return path + File.separator + RADIOLOGS_DIR;
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

    public boolean isCallActive(Context context){
        AudioManager manager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        if(manager.getMode()==AudioManager.MODE_IN_CALL){
            return true;
        }
        else{
            return false;
        }
    }

    public void generatePeriodicRadiologs() {
        preferenceManager = SharedPreferencesManager.getInstance(application);

        if(preferenceManager.getRadiologsIdleMode() == 1 || preferenceManager.getRadiologsDedicatedMode() == 1
                || preferenceManager.getScanlogsEnable() == 1) isGenerating = true;
        else return;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                //DO SOMETHING
                try {
                    if((preferenceManager.getRadiologsDedicatedMode() == 1 && isCallActive(application))
                            || (preferenceManager.getRadiologsIdleMode() == 1 && !isCallActive(application)))
                        generateRadiolog();

                    if(preferenceManager.getScanlogsEnable() == 1)
                        generate_scanlog();

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                handler.postDelayed(this, preferenceManager.getRadiologPeriodicity() * 1000);
            }
        }, preferenceManager.getRadiologPeriodicity() * 1000);

//        final Handler handler = new Handler();
//        Timer timer = new Timer();
//        TimerTask doAsynchronousTask = new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    public void run() {
//                        try {
//                            if((preferenceManager.getRadiologsDedicatedMode() == 1 && isCallActive(application))
//                                    || (preferenceManager.getRadiologsIdleMode() == 1 && !isCallActive(application)))
//                                generateRadiolog();
//
//                            if(preferenceManager.getScanlogsEnable() == 1)
//                                generate_scanlog();
//
//                            } catch (Exception e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        };
//
//        timer.schedule(doAsynchronousTask, 0,
//                preferenceManager.getRadiologPeriodicity() * 1000); //execute in every xxxxx ms
    }

}

package pt.ptinovacao.arqospocket.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.SparseArray;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;

import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.AnswerVoiceCallTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.HangUpVoiceCallTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.HttpDownloadTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.HttpUploadTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.PingTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.ReceiveSmsTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.RecordAudioTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.SendSmsTaskResult;

/**
 * FindResourceToDetailsTasks.
 * <p>
 * Created by pedro on 24/04/2017.
 */
public enum FindResourceToDetailsTasks {
    //geral
    TASK_ID(TaskResult.TASK_ID, R.string.tasks_details_task_id, SettingType.STRING),

    TASK_NAME(TaskResult.TASK_NAME, R.string.tasks_details_task_name, SettingType.STRING),

    MACRO_ID(TaskResult.MACRO_ID, R.string.tasks_details_macro_id, SettingType.STRING),

    TASK_NUMBER(TaskResult.TASK_NUMBER, R.string.tasks_details_task_number, SettingType.STRING),

    CELL_ID(TaskResult.CELL_ID, R.string.tasks_details_cell_id, SettingType.DETAILS_CELL),

    START_DATE(TaskResult.START_DATE, R.string.tasks_details_start_date, SettingType.DATA),

    END_DATE(TaskResult.END_DATE, R.string.tasks_details_end_date, SettingType.DATA),

    STATUS(TaskResult.STATUS, R.string.tasks_details_status, SettingType.STRING),

    LOC_GPS(TaskResult.GPS_LOCATION, R.string.tasks_details_gps_location, SettingType.STRING),

    //receiveSMS
    MESSAGE_TEXT(ReceiveSmsTaskResult.MESSAGE_TEXT, R.string.tasks_details_message_text, SettingType.STRING),

    SOURCE_NUMBER(ReceiveSmsTaskResult.SOURCE_NUMBER, R.string.tasks_details_source_number, SettingType.STRING),

    WAITING_TIME(ReceiveSmsTaskResult.WAITING_TIME, R.string.tasks_details_waiting_time, SettingType.MILLISECONDS),

    MESSAGE_DELIVERY_TIME(ReceiveSmsTaskResult.MESSAGE_DELIVERY_TIME, R.string.tasks_details_message_delivery_time,
            SettingType.MILLISECONDS),

    ENCODING(ReceiveSmsTaskResult.ENCODING, R.string.tasks_details_encoding, SettingType.STRING),

    //sendSMS
    DESTINATION_NUMBER(SendSmsTaskResult.DESTINATION_NUMBER, R.string.tasks_details_destination_number,
            SettingType.STRING),

    SENDING_TIME(SendSmsTaskResult.SENDING_TIME, R.string.tasks_details_sending_time, SettingType.MILLISECONDS),

    MESSAGE_TEXT_SEND(SendSmsTaskResult.MESSAGE_TEXT, R.string.tasks_details_message_text, SettingType.STRING),

    //ping
    MINIMUM(PingTaskResult.MINIMUM, R.string.tasks_details_minimum, SettingType.MILLISECONDS),

    MEDIUM(PingTaskResult.MEDIUM, R.string.tasks_details_medium, SettingType.MILLISECONDS),

    MAXIMUM(PingTaskResult.MAXIMUM, R.string.tasks_details_maximum, SettingType.MILLISECONDS),

    SENT_PACKETS(PingTaskResult.SENT_PACKETS, R.string.tasks_details_sent_packets, SettingType.STRING),

    RECEIVED_PACKETS(PingTaskResult.RECEIVED_PACKETS, R.string.tasks_details_received_packets, SettingType.STRING),

    LOST_PACKETS(PingTaskResult.LOST_PACKETS, R.string.tasks_details_lost_packets, SettingType.STRING),

    //down
    RECEIVED_DATA(HttpDownloadTaskResult.RECEIVED_DATA, R.string.tasks_details_received_data, SettingType.STRING),

    RECEIVED_DATA_SIZE(HttpDownloadTaskResult.RECEIVED_DATA_SIZE, R.string.tasks_details_received_data_size,
            SettingType.STRING),

    DOWNLOAD_TIME_IN_SECONDS(HttpDownloadTaskResult.DOWNLOAD_TIME_IN_SECONDS,
            R.string.tasks_details_download_time_seconds, SettingType.SECONDS),

    ACCESS_TIME_IN_SECONDS(HttpDownloadTaskResult.ACCESS_TIME_IN_SECONDS, R.string.tasks_details_access_time_seconds,
            SettingType.SECONDS),

    THROUGHPUT(HttpDownloadTaskResult.THROUGHPUT, R.string.tasks_details_throughput, SettingType.STRING),

    //up
    SENT_DATA(HttpUploadTaskResult.SENT_DATA, R.string.tasks_details_sent_data, SettingType.STRING),

    SENT_DATA_SIZE(HttpUploadTaskResult.SENT_DATA_SIZE, R.string.tasks_details_sent_data_size, SettingType.STRING),

    UPLOAD_TIME_IN_SECONDS(HttpUploadTaskResult.UPLOAD_TIME_IN_SECONDS, R.string.tasks_details_upload_time_seconds,
            SettingType.STRING),

    //voice
    CALL_DURATION(HangUpVoiceCallTaskResult.CALL_DURATION, R.string.tasks_details_call_duration, SettingType.SECONDS),
    CALLED_NUMBER(AnswerVoiceCallTaskResult.CALLED_NUMBER, R.string.tasks_details_called_number, SettingType.STRING),
    AUDIO_RECORDING_FILE_NAME(RecordAudioTaskResult.AUDIO_RECORD_FILE_NAME,
            R.string.tasks_details_audio_recordings_file_name, SettingType.STRING),
    TIME_WAITING_FOR_RINGING(AnswerVoiceCallTaskResult.TIME_WAITING_FOR_RINGING, R.string.tasks_details_time_waiting_for_ringing,
            SettingType.MILLISECONDS),

    UNKNOWN("UNKNOWN", R.string.tasks_details_unknown, SettingType.STRING);

    private String key;

    private int resource;

    private SettingType settingType;

    private final static Logger LOGGER = LoggerFactory.getLogger(FindResourceToDetailsTasks.class);

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    FindResourceToDetailsTasks(String key, int resource, SettingType settingType) {

        this.key = key;
        this.resource = resource;
        this.settingType = settingType;
    }

    public static FindResourceToDetailsTasks getFindResourceToDetailsTasks(String key) {

//        LOGGER.debug(key);

        for (FindResourceToDetailsTasks b : FindResourceToDetailsTasks.values()) {
            if (b.getKey().equalsIgnoreCase(key)) {
                return b;
            }
        }
        LOGGER.debug("UNKNOWN" + key);
        return UNKNOWN;
    }

    public String getKey() {
        return key;
    }

    public int getResource() {
        return resource;
    }

    public SettingType getSettingType() {
        return settingType;
    }

    enum SettingType {
        DATA,
        MILLISECONDS,
        SECONDS,
        STRING,
        INTEGER,
        DETAILS_CELL,
        MOBILE_NETWORK_MODE,
        STATE_WIFI,
        BER_VALUE,
        DBM
    }

    public static void validateRemoveLines(ArrayList<AbstractMap.SimpleEntry> simpleEntryArrayList) {

        for (int i = 0; i < simpleEntryArrayList.size(); i++) {
            if (Strings.isNullOrEmpty(simpleEntryArrayList.get(i).getValue().toString())) {
                simpleEntryArrayList.remove(simpleEntryArrayList.get(i));
                i--;
            }
        }
    }

    public static String getTextOfType(Context context, SettingType settingType, String value) {
        switch (settingType) {
            case DATA:
                try {
                    return formatter.parse(value).toString();
                } catch (ParseException e) {
                    LOGGER.error("Error ", e);
                }
                return value;
            case DBM:
                return value + " " + "dBm";
            case MILLISECONDS:
                return value + " " + "ms";
            case SECONDS:
                return value + " " + "s";
            case STATE_WIFI:
                try {
                    int parseInt = Integer.parseInt(value);
                    return LIST_CONNECTION_STATE[parseInt];
                } catch (Exception e) {
                    LOGGER.error("Error :", e);
                }
                return LIST_CONNECTION_STATE[12];
            case DETAILS_CELL:
                Iterable<String> split = Splitter.on(',').split(value);
                String[] strings = Iterables.toArray(split, String.class);

                SparseArray<Holder> arrayListGeneral = new SparseArray<>();
                SparseArray<Holder> arrayListWifi = new SparseArray<>();
                SparseArray<Holder> arrayListLTE = new SparseArray<>();

                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 0; i < strings.length; i++) {

                    if (!Strings.isNullOrEmpty(strings[i]) && !"-1".equals(strings[i])) {
                        RadioInformationParameter parameter = RadioInformationParameter.getRadioInformationParameter(i);
                        if (parameter.getGroup() == RadioInformationParameter.Group.MOBILE) {
                            arrayListGeneral.put(parameter.getResource(), new Holder(parameter, strings[i]));
                        } else if (parameter.getGroup() == RadioInformationParameter.Group.WIFI) {
                            arrayListWifi.put(parameter.getResource(), new Holder(parameter, strings[i]));
                        }
                    }
                }

                addValuesToList(context, arrayListGeneral, stringBuilder, true);
                addValuesToList(context, arrayListWifi, stringBuilder, false);
                addValuesToList(context, arrayListLTE, stringBuilder, false);

                return stringBuilder.toString();
            case BER_VALUE:
                return value + "%";
            case MOBILE_NETWORK_MODE:
                try {
                    int parseInt = Integer.parseInt(value);
                    return LIST_CONNECTION_MODE[parseInt];
                } catch (NumberFormatException e) {
                    LOGGER.error("Error :", e);
                }
            case STRING:
            case INTEGER:
            default:
                return value;
        }
    }

    private static void addValuesToList(Context context, SparseArray<Holder> arrayListGeneral,
            StringBuilder stringBuilder, boolean isFirst) {

        if (isFirst) {
            stringBuilder.append("<h5>").append(arrayListGeneral.valueAt(0).parameter.getGroup()).append("</h5>");
        } else if (arrayListGeneral.size() > 0) {
            stringBuilder.append("<br><br>").append("<h5>").append(arrayListGeneral.valueAt(0).parameter.getGroup())
                    .append("</h5>");
        }

        for (int i = 0; i < arrayListGeneral.size(); i++) {

            int key = arrayListGeneral.keyAt(i);
            stringBuilder.append("<br>").append(context.getString(key)).append(":").append(FindResourceToDetailsTasks
                    .getTextOfType(context, arrayListGeneral.get(key).parameter.getSettingType(),
                            arrayListGeneral.get(key).value));
        }
    }

    private static class Holder {

        final RadioInformationParameter parameter;

        final String value;

        Holder(RadioInformationParameter parameter, String value) {
            this.parameter = parameter;
            this.value = value;
        }
    }

    private static final String[] LIST_CONNECTION_MODE =
            { "GPRS", "EDGE", "UMTS", "HSDPA", "HSUPA", "HSPA", "NONE", "Unkown", "HSPA+", "DC-HSPA+", "LTE" };

    private static final String[] LIST_CONNECTION_STATE = {
            "ASSOCIATED",
            "ASSOCIATING",
            "AUTHENTICATING",
            "COMPLETED",
            "DISCONNECTED",
            "DORMANT",
            "FOUR_WAY_HANDSHAKE",
            "GROUP_HANDSHAKE",
            "INACTIVE",
            "INTERFACE_DISABLED",
            "INVALID",
            "SCANNING",
            "UNINITIALIZED"
    };

    @Override
    public String toString() {
        return "FindResourceToDetailsTasks{" + "key='" + key + '\'' + ", resource=" + resource + ", settingType=" +
                settingType + '}';
    }

    enum RadioInformationParameter {
        GSM_CEL_ID(1, R.string.radio_inf_parm_gsm_cel_id, SettingType.STRING, Group.MOBILE),
        GMS_SIG_LVL(2, R.string.radio_inf_parm_gsm_sig_lvl, SettingType.DBM, Group.MOBILE),
        GSM_BAND(3, R.string.radio_inf_parm_gsm_band, SettingType.STRING, Group.MOBILE),
        GSM_MODE(4, R.string.radio_inf_parm_gsm_more, SettingType.MOBILE_NETWORK_MODE, Group.MOBILE),
        GSM_OPERATOR(5, R.string.radio_inf_parm_gsm_operator, SettingType.STRING, Group.MOBILE),
        RSCP(32, R.string.radio_inf_parm_rscp, SettingType.DBM, Group.MOBILE),
        ECIO(33, R.string.radio_inf_parm_ecio, SettingType.STRING, Group.MOBILE),
        ARFCN(34, R.string.radio_inf_parm_arfcn, SettingType.STRING, Group.MOBILE),
        PSC(35, R.string.radio_inf_parm_gpsc, SettingType.STRING, Group.MOBILE),
        BSIC(36, R.string.radio_inf_parm_bsic, SettingType.STRING, Group.MOBILE),
        RAC(37, R.string.radio_inf_parm_rac, SettingType.STRING, Group.MOBILE),
        BER(38, R.string.radio_inf_parm_ber, SettingType.BER_VALUE, Group.MOBILE),
        IMSI(41, R.string.radio_inf_parm_imsi, SettingType.STRING, Group.MOBILE),
        CQI(42, R.string.radio_inf_parm_cqi, SettingType.STRING, Group.MOBILE),

        LTE_REG_STAT(10, R.string.lte_reg_stat, SettingType.STRING, Group.MOBILE),
        LTE_MODE(11, R.string.lte_mode, SettingType.MOBILE_NETWORK_MODE, Group.MOBILE),
        LTE_BAND(12, R.string.lte_band, SettingType.STRING, Group.MOBILE),
        LTE_BW(13, R.string.lte_bw, SettingType.STRING, Group.MOBILE),
        LTE_OPER(14, R.string.lte_oper, SettingType.STRING, Group.MOBILE),
        LTE_TXCH(15, R.string.lte_txch, SettingType.STRING, Group.MOBILE),
        LTE_RXCH(16, R.string.lte_rxch, SettingType.STRING, Group.MOBILE),
        LTE_RSSI(17, R.string.lte_rssi, SettingType.DBM, Group.MOBILE),
        LTE_RSSP(18, R.string.lte_rsrp, SettingType.DBM, Group.MOBILE),
        LTE_RSRQ(19, R.string.lte_rsrq, SettingType.DBM, Group.MOBILE),
        LTE_SINR(20, R.string.lte_cqi, SettingType.STRING, Group.MOBILE),
        LTE_CELLID(22, R.string.lte_cellid, SettingType.STRING, Group.MOBILE),

        WIFI_BSSID(23, R.string.wifi_inf_parm_bssid, SettingType.STRING, Group.WIFI),
        WIFI_SSID(27, R.string.wifi_inf_parm_ssid, SettingType.STRING, Group.WIFI),
        WIFI_RXLEVEL(26, R.string.wifi_inf_parm_rxLevel, SettingType.DBM, Group.WIFI),
        WIFI_WIFISTATE(28, R.string.wifi_inf_parm_wifiState, SettingType.STATE_WIFI, Group.WIFI),

        UNKNOWN(-1, R.string.tasks_details_unknown, SettingType.STRING, Group.MOBILE);

        enum Group {
            MOBILE,
            WIFI
        }

        private final int index;

        private final int resource;

        private final SettingType settingType;

        private final Group group;

        RadioInformationParameter(int index, int resource, SettingType settingType, Group group) {
            this.index = index;
            this.resource = resource;
            this.settingType = settingType;
            this.group = group;
        }

        public static RadioInformationParameter getRadioInformationParameter(int key) {
            for (RadioInformationParameter b : RadioInformationParameter.values()) {
                if ((b.getIndex() - 1) == key) {
                    return b;
                }
            }
            return UNKNOWN;
        }

        public Group getGroup() {
            return group;
        }

        public int getIndex() {
            return index;
        }

        public int getResource() {
            return resource;
        }

        public SettingType getSettingType() {
            return settingType;
        }

        @Override
        public String toString() {
            return "RadioInformationParameter{" + "index=" + index + ", resource=" + resource + ", settingType=" +
                    settingType + '}';
        }
    }

}

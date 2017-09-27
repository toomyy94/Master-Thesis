package pt.ptinovacao.arqospocket.core.alarms.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import pt.ptinovacao.arqospocket.core.utils.DateUtils;
import pt.ptinovacao.arqospocket.persistence.models.Alarm;

/**
 * This class is responsible to parse the alarms input in JSON format and return a list with the alarms to execute.
 * <p>
 * Created by Tomás Rodrigues on 06-04-2017.
 */
public class AlarmParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmParser.class);


    /**
     * Parses a database alarm with a single alarm on it.
     *
     * @param alarm the alarm with the alarm data.
     * @return the parsed String
     */
    public String databaseAlarmToPipes(Alarm alarm){
        Character PIPE = '|';

        String pipedAlarm = String.valueOf(alarm.getOriginId())+ PIPE + alarm.getGpsLocation() + PIPE +
                alarm.getCellId()+ PIPE + DateUtils.convertDateToStringAlarms(alarm.getReportDate()) + PIPE +
                alarm.getStartEnd() +" "+ alarm.getAlarmId()+": "+ alarm.getAlarmContent() + PIPE +
                alarm.getAdditionalInfo() + PIPE + alarm.getIccid() + PIPE + "\n";

        return pipedAlarm;
    }

    /**
     * Parses a list of alarms with a  alarm on it.
     *
     * @param pendingAlarm the alarms with the alarm data.
     * @return the parsed JsonObject
     */
    public JsonObject alarmResultToStrings(List<Alarm> pendingAlarm){
        Character PIPE = '|';
        JsonObject jsonObject = new JsonObject();
        JsonArray data = new JsonArray();

        for(Alarm alarm : pendingAlarm) {
            String pipedAlarm = String.valueOf(alarm.getOriginId()) + PIPE + alarm.getGpsLocation() + PIPE +
                alarm.getCellId() + PIPE + DateUtils.convertDateToStringAlarms(alarm.getReportDate()) + PIPE +
                alarm.getStartEnd() + " " + alarm.getAlarmId() + ": " + alarm.getAlarmContent() + PIPE +
                alarm.getAdditionalInfo() + PIPE + alarm.getIccid() + PIPE;

            data.add(pipedAlarm);
        }

        jsonObject.add("data", data);
        return jsonObject;
    }

//
//    /**
//     * Parses a string with a single test on it.
//     *
//     * @param alarmData the string with the test data.
//     * @return the parsed {@link AlarmData} instance.
//     */
//    public AlarmData parseSingleTest(String alarmData) {
//        Gson gson = JsonHelper.getGsonInstance();
//        return gson.fromJson(alarmData, AlarmData.class);
//    }
//
//    /**
//     * Parses a string with a single test on it.
//     *
//     * @param alarmResult the string with the test data.
//     * @return the parsed {@link AlarmResult} instance.
//     */
//    public AlarmResult parseSingleResult(String alarmResult) {
//        Gson gson = JsonHelper.getGsonInstance();
//        return gson.fromJson(alarmResult, AlarmResult.class);
//    }
//
//    /**
//     * Converts a {@link AlarmData} object into a JSON string.
//     *
//     * @param alarmData the {@link AlarmData} to convert.
//     * @return the converted object.
//     */
//    public String stringify(AlarmData alarmData) {
//        return stringify(alarmData, true);
//    }
//
//    /**
//     * Converts a {@link AlarmData} object into a JSON string.
//     *
//     * @param alarmData the {@link AlarmData} to convert.
//     * @param prettyPrint if the output JSON should be formatted.
//     * @return the converted object.
//     */
//    String stringify(AlarmData alarmData, boolean prettyPrint) {
//        Gson gson = JsonHelper.getGsonInstance(prettyPrint);
//        return gson.toJson(alarmData);
//    }
//
//    /**
//     * Converts a {@link AlarmResult} object into a JSON string.
//     *
//     * @param alarmData the {@link AlarmResult} to convert.
//     * @return the converted object.
//     */
//    String stringify(AlarmResult alarmData) {
//        return stringify(alarmData, false);
//    }
//
//    /**
//     * Converts a {@link AlarmResult} object into a JSON string.
//     *
//     * @param alarmData the {@link AlarmResult} to convert.
//     * @param prettyPrint if the output JSON should be formatted.
//     * @return the converted object.
//     */
//    String stringify(AlarmResult alarmData, boolean prettyPrint) {
//        Gson gson = JsonHelper.getGsonInstance(prettyPrint);
//        return gson.toJson(alarmData);
//    }
//
//    /**
//     * Converts a {@link ResultFileData} object into a JSON string.
//     *
//     * @param data the {@link ResultFileData} to convert.
//     * @return the converted object.
//     */
//    public String stringify(ResultFileData data) {
//        Gson gson = JsonHelper.getGsonInstance(false);
//        return gson.toJson(data);
//    }
//
//    private void close(Closeable inputStream) {
//        if (inputStream != null) {
//            try {
//                inputStream.close();
//            } catch (IOException e) {
//                LOGGER.error("Error closing input stream", e);
//            }
//        }
//    }
}
package pt.ptinovacao.arqospocket.core.serialization.composers;

import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import java.util.List;
import java.util.Map;

import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.AnswerVoiceCallTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.AssociateWiFiTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.DisassociateWiFiTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.HangUpVoiceCallTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.HttpDownloadTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.HttpUploadTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.MakeVoiceCallTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.PingTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.ReAssociateWifiTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.ReceiveSmsTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.RecordAudioTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.ScanWifiNetworkTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.SendMailTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.SendMmsTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.SendSmsTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.TurnOffMobileTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.TurnOffWifiTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.TurnOnMobileTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.TurnOnWifiTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.WifiAuthLoginTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.WifiAuthLogoutTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.AnswerVoiceCallTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.AssociateWiFiTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.DisassociateWiFiTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.HangUpVoiceCallTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.HttpDownloadTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.HttpUploadTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.MakeVoiceCallTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.PingTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.ReAssociateWifiTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.ReceiveSmsTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.RecordAudioTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.ScanWifiNetworkTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.SendMailTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.SendMmsTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.SendSmsTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.TurnOffMobileTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.TurnOffWifiTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.TurnOnMobileTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.TurnOnWifiTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.WifiAuthLoginTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.WifiAuthLogoutTaskData;

/**
 * Allows to create specific fillers for specific tasks.
 * <p>
 * Created by Emílio Simões on 13-04-2017.
 */
public abstract class ObjectComposer {

    private static final Map<Class<? extends TaskData>, Class<? extends ObjectComposer>> TASK_DATA_COMPOSERS =
            new ImmutableMap.Builder<Class<? extends TaskData>, Class<? extends ObjectComposer>>()
                    .put(AnswerVoiceCallTaskData.class, AnswerVoiceCallTaskObjectComposer.class)
                    .put(AssociateWiFiTaskData.class, AssociateWiFiTaskObjectComposer.class)
                    .put(DisassociateWiFiTaskData.class, EmptyObjectComposer.class)
                    .put(HangUpVoiceCallTaskData.class, HangUpVoiceCallTaskObjectComposer.class)
                    .put(HttpDownloadTaskData.class, HttpDownloadTaskObjectComposer.class)
                    .put(HttpUploadTaskData.class, HttpUploadTaskObjectComposer.class)
                    .put(MakeVoiceCallTaskData.class, MakeVoiceCallTaskObjectComposer.class)
                    .put(PingTaskData.class, PingTaskObjectComposer.class)
                    .put(ReAssociateWifiTaskData.class, EmptyObjectComposer.class)
                    .put(ReceiveSmsTaskData.class, ReceiveSmsTaskObjectComposer.class)
                    .put(RecordAudioTaskData.class, RecordAudioTaskObjectComposer.class)
                    .put(ScanWifiNetworkTaskData.class, ScanWifiNetworkTaskObjectComposer.class)
                    .put(SendSmsTaskData.class, SendSmsTaskObjectComposer.class)
                    .put(SendMmsTaskData.class, SendMmsTaskObjectComposer.class)
                    .put(SendMailTaskData.class, SendMailTaskObjectComposer.class)
                    .put(TurnOffMobileTaskData.class, EmptyObjectComposer.class)
                    .put(TurnOffWifiTaskData.class, EmptyObjectComposer.class)
                    .put(TurnOnMobileTaskData.class, EmptyObjectComposer.class)
                    .put(TurnOnWifiTaskData.class, EmptyObjectComposer.class)
                    .put(WifiAuthLoginTaskData.class, WifiAuthLoginTaskObjectComposer.class)
                    .put(WifiAuthLogoutTaskData.class, WifiAuthLogoutTaskObjectComposer.class).build();

    private static final Map<Class<? extends TaskResult>, Class<? extends ObjectComposer>> TASK_RESULT_COMPOSERS =
            new ImmutableMap.Builder<Class<? extends TaskResult>, Class<? extends ObjectComposer>>()
                    .put(AnswerVoiceCallTaskResult.class, AnswerVoiceCallTaskObjectComposer.class)
                    .put(AssociateWiFiTaskResult.class, AssociateWiFiTaskObjectComposer.class)
                    .put(DisassociateWiFiTaskResult.class, EmptyObjectComposer.class)
                    .put(HangUpVoiceCallTaskResult.class, HangUpVoiceCallTaskObjectComposer.class)
                    .put(HttpDownloadTaskResult.class, HttpDownloadTaskObjectComposer.class)
                    .put(HttpUploadTaskResult.class, HttpUploadTaskObjectComposer.class)
                    .put(MakeVoiceCallTaskResult.class, MakeVoiceCallTaskObjectComposer.class)
                    .put(PingTaskResult.class, PingTaskObjectComposer.class)
                    .put(ReAssociateWifiTaskResult.class, EmptyObjectComposer.class)
                    .put(ReceiveSmsTaskResult.class, ReceiveSmsTaskObjectComposer.class)
                    .put(RecordAudioTaskResult.class, RecordAudioTaskObjectComposer.class)
                    .put(ScanWifiNetworkTaskResult.class, ScanWifiNetworkTaskObjectComposer.class)
                    .put(SendSmsTaskResult.class, SendSmsTaskObjectComposer.class)
                    .put(SendMmsTaskResult.class, SendMmsTaskObjectComposer.class)
                    .put(SendMailTaskResult.class, SendMailTaskObjectComposer.class)
                    .put(TurnOffMobileTaskResult.class, EmptyObjectComposer.class)
                    .put(TurnOffWifiTaskResult.class, EmptyObjectComposer.class)
                    .put(TurnOnMobileTaskResult.class, EmptyObjectComposer.class)
                    .put(TurnOnWifiTaskResult.class, EmptyObjectComposer.class)
                    .put(WifiAuthLoginTaskResult.class, WifiAuthLoginTaskObjectComposer.class)
                    .put(WifiAuthLogoutTaskResult.class, WifiAuthLogoutTaskObjectComposer.class).build();

    public static ObjectComposer getTaskDataObjectComposer(Class<? extends TaskData> type) {
        Class<? extends ObjectComposer> clazz = TASK_DATA_COMPOSERS.get(type);
        if (clazz == null) {
            throw new UnsupportedOperationException("There is no matching filler for " + type);
        }

        if (EmptyObjectComposer.class.equals(clazz)) {
            return EmptyObjectComposer.INSTANCE;
        }

        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new UnsupportedOperationException("There is no matching filler for " + type, e);
        }
    }

    public static ObjectComposer getTaskResultObjectComposer(Class<? extends TaskResult> type) {
        Class<? extends ObjectComposer> clazz = TASK_RESULT_COMPOSERS.get(type);
        if (clazz == null) {
            throw new UnsupportedOperationException("There is no matching filler for " + type);
        }

        if (EmptyObjectComposer.class.equals(clazz)) {
            return EmptyObjectComposer.INSTANCE;
        }

        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new UnsupportedOperationException("There is no matching filler for " + type, e);
        }
    }

    public static int toInt(String value) {
        Integer result = Ints.tryParse(value);
        return result != null ? result : 0;
    }

    public static long toLong(String value) {
        Long result = Longs.tryParse(value);
        return result != null ? result : 0;
    }

    static Integer toInteger(String value) {
        return Ints.tryParse(value);
    }

    public static String toString(Integer value) {
        if (value == null) {
            return "";
        }
        return String.valueOf(value);
    }

    public static String toString(Long value) {
        if (value == null) {
            return "";
        }
        return String.valueOf(value);
    }

    /**
     * Fills the properties in a {@link TaskData} object from a property map.
     *
     * @param data the {@link TaskData} to fill.
     * @param propertyMap the property map with the data.
     */
    public abstract void fill(TaskData data, Map<String, String> propertyMap);

    /**
     * Fills the properties in a {@link TaskResult} object from a property map.
     *
     * @param data the {@link TaskResult} to fill.
     * @param propertyMap the property map with the data.
     */
    public abstract void fill(TaskResult data, Map<String, String> propertyMap);

    /**
     * Fills the properties in a {@link TaskData} object from a property list.
     *
     * @param data the {@link TaskData} to fill.
     * @param propertyList the property list with the data.
     */
    public abstract void fill(TaskData data, List<String> propertyList);

    /**
     * Fills the properties in a {@link TaskResult} object from a property list.
     *
     * @param data the {@link TaskResult} to fill.
     * @param propertyList the property list with the data.
     */
    public abstract void fill(TaskResult data, List<String> propertyList);

    /**
     * Transforms the object properties into a map of values.
     *
     * @param data the data value to process.
     * @param propertyMap the resulting properties map.
     */
    public abstract void collectToMap(TaskData data, Map<String, String> propertyMap);

    /**
     * Transforms the object properties into a map of values.
     *
     * @param data the data value to process.
     * @param propertyMap the resulting properties map.
     */
    public abstract void collectToMap(TaskResult data, Map<String, String> propertyMap);

    /**
     * Transforms the object properties into a list of values.
     *
     * @param data the data value to process.
     * @param propertyList the resulting properties lsi.
     */
    public abstract void collectToList(TaskData data, List<String> propertyList);

    /**
     * Transforms the object properties into a list of values.
     *
     * @param data the data value to process.
     * @param propertyList the resulting properties lsi.
     */
    public abstract void collectToList(TaskResult data, List<String> propertyList);

    /**
     * Empty object composer for classes that do not have any additional fields.
     */
    private static class EmptyObjectComposer extends ObjectComposer {

        private static final EmptyObjectComposer INSTANCE = new EmptyObjectComposer();

        @Override
        public void fill(TaskData data, Map<String, String> propertyMap) {
        }

        @Override
        public void fill(TaskResult data, Map<String, String> propertyMap) {
        }

        @Override
        public void fill(TaskData data, List<String> propertyList) {
        }

        @Override
        public void fill(TaskResult data, List<String> propertyList) {
        }

        @Override
        public void collectToMap(TaskData data, Map<String, String> propertyMap) {
        }

        @Override
        public void collectToMap(TaskResult data, Map<String, String> propertyMap) {
        }

        @Override
        public void collectToList(TaskData data, List<String> propertyList) {
        }

        @Override
        public void collectToList(TaskResult data, List<String> propertyList) {
        }
    }
}

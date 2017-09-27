package pt.ptinovacao.arqospocket.core.serialization;

import android.support.annotation.NonNull;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import org.json.JSONException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.AnswerVoiceCallTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.HangUpVoiceCallTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.MakeVoiceCallTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.PingTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.ReceiveSmsTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.RecordAudioTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.ScanWifiNetworkTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.SendSmsTaskResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TaskResultFactoryTest {

    private static final String BASE_MESSAGE_1 = "1|1|20170412170807|20170412170825|";

    private static final String BASE_MESSAGE_2 = "||2,,23,,,,,,,,2,|3443 N 2223 W 222 333|1|";

    @Test
    public void factory_parseAnswerCallTask() throws Exception {
        String source = BASE_MESSAGE_1 + "2" + BASE_MESSAGE_2 + "12345678|1|||||filename||3000";

        ArrayList<String> values = getPropertyList(source);
        assertEquals(18, values.size());

        TaskResult fromList = TaskResultFactory.createFromList(values);
        assertTrue(fromList instanceof AnswerVoiceCallTaskResult);

        AnswerVoiceCallTaskResult taskResult = (AnswerVoiceCallTaskResult) fromList;
        assertEquals(1, taskResult.getTaskNumber());
        assertEquals("12345678", taskResult.getCalledNumber());
        assertEquals(3000, taskResult.getTimeWaitingForRinging());
        assertEquals("filename", taskResult.getAudioRecordingFileName());

        List<String> decomposed = TaskResultFactory.decomposeObjectToList(taskResult);
        assertEquals(18, decomposed.size());
        for (int i = 0; i < 18; i++) {
            assertEquals(values.get(i), decomposed.get(i));
        }
        assertEquals(source, Joiner.on('|').join(decomposed));
    }

    @Test
    public void factory_parseHangUpCallTask() throws Exception {
        String source = BASE_MESSAGE_1 + "3" + BASE_MESSAGE_2 + "40|||||||||||||||||||||||||||||||||||||1";

        ArrayList<String> values = getPropertyList(source);
        assertEquals(47, values.size());

        TaskResult fromList = TaskResultFactory.createFromList(values);
        assertTrue(fromList instanceof HangUpVoiceCallTaskResult);

        HangUpVoiceCallTaskResult taskResult = (HangUpVoiceCallTaskResult) fromList;
        assertEquals(1, taskResult.getTaskNumber());
        assertEquals("40", taskResult.getCallDuration());

        List<String> decomposed = TaskResultFactory.decomposeObjectToList(taskResult);
        assertEquals(47, decomposed.size());
        for (int i = 0; i < 47; i++) {
            assertEquals(values.get(i), decomposed.get(i));
        }
        assertEquals(source, Joiner.on('|').join(decomposed));
    }

    @Test
    public void factory_parseMakeVoiceCallTask() throws Exception {
        String source = BASE_MESSAGE_1 + "1" + BASE_MESSAGE_2 + "1|987654321|60|||||||makecall|||||||||||||||||||";

        ArrayList<String> values = getPropertyList(source);
        assertEquals(38, values.size());

        TaskResult fromList = TaskResultFactory.createFromList(values);
        assertTrue(fromList instanceof MakeVoiceCallTaskResult);

        MakeVoiceCallTaskResult taskResult = (MakeVoiceCallTaskResult) fromList;
        assertEquals(1, taskResult.getTaskNumber());
        assertEquals("987654321", taskResult.getDestinationNumber());
        assertEquals("60", taskResult.getCallSetupTime());
        assertEquals("makecall", taskResult.getAudioRecordFileName());

        List<String> decomposed = TaskResultFactory.decomposeObjectToList(taskResult);
        assertEquals(38, decomposed.size());
        for (int i = 0; i < 38; i++) {
            assertEquals(values.get(i), decomposed.get(i));
        }
        assertEquals(source, Joiner.on('|').join(decomposed));
    }

    @Test
    public void factory_parsePingTask() throws Exception {
        String[] source = new String[] {
                "task_id|0001",
                "task_name|Ping",
                "macro_id|1",
                "task_number|6",
                "init_date|20170418145756",
                "end_date|20170418145832",
                "iccid|",
                "cell_id|2,,23,,,,,,,,2,",
                "loc_gps|3443 N 2223 W 222 333",
                "status|0",
                "min_in_msec|32",
                "med_in_msec|45",
                "max_in_msec|112",
                "sent_packets|8",
                "received_packets|8",
                "lost_packets|0"
        };

        Map<String, String> values = getPropertyMap(source);
        assertEquals(16, values.size());

        TaskResult fromMap = TaskResultFactory.createFromMap(values);
        assertTrue(fromMap instanceof PingTaskResult);

        PingTaskResult taskData = (PingTaskResult) fromMap;
        assertEquals("0001", taskData.getTaskId());
        assertEquals("32", taskData.getMinimum());
        assertEquals("45", taskData.getMedium());
        assertEquals("112", taskData.getMaximum());
        assertEquals("8", taskData.getSentPackets());
        assertEquals("8", taskData.getReceivedPackets());
        assertEquals("0", taskData.getLostPackets());

        Map<String, String> decomposed = TaskResultFactory.decomposeObjectToMap(taskData);
        assertEquals(16, decomposed.size());
        for (String key : decomposed.keySet()) {
            assertEquals(values.get(key), decomposed.get(key));
        }
    }

    @Test
    public void factory_parseReceiveSmsTask() throws Exception {
        String source = BASE_MESSAGE_1 + "36" + BASE_MESSAGE_2 + "|SMS test message||987654321||34|35|0";

        ArrayList<String> values = getPropertyList(source);
        assertEquals(17, values.size());

        TaskResult fromList = TaskResultFactory.createFromList(values);
        assertTrue(fromList instanceof ReceiveSmsTaskResult);

        ReceiveSmsTaskResult taskResult = (ReceiveSmsTaskResult) fromList;
        assertEquals(1, taskResult.getTaskNumber());
        assertEquals("987654321", taskResult.getSourceNumber());
        assertEquals("SMS test message", taskResult.getMessageText());
        assertEquals("34", taskResult.getMessageDeliveryTime());
        assertEquals("35", taskResult.getWaitingTime());
        assertEquals("0", taskResult.getEncoding());

        List<String> decomposed = TaskResultFactory.decomposeObjectToList(taskResult);
        assertEquals(17, decomposed.size());
        for (int i = 0; i < 17; i++) {
            assertEquals(values.get(i), decomposed.get(i));
        }
        assertEquals(source, Joiner.on('|').join(decomposed));
    }

    @Test
    public void factory_parseRecordAudioTask() throws Exception {
        String source = BASE_MESSAGE_1 + "42" + BASE_MESSAGE_2 + "recording";

        ArrayList<String> values = getPropertyList(source);
        assertEquals(10, values.size());

        TaskResult fromList = TaskResultFactory.createFromList(values);
        assertTrue(fromList instanceof RecordAudioTaskResult);

        RecordAudioTaskResult taskResult = (RecordAudioTaskResult) fromList;
        assertEquals(1, taskResult.getTaskNumber());
        assertEquals("recording", taskResult.getAudioRecordingFileName());

        List<String> decomposed = TaskResultFactory.decomposeObjectToList(taskResult);
        assertEquals(10, decomposed.size());
        for (int i = 0; i < 10; i++) {
            assertEquals(values.get(i), decomposed.get(i));
        }
        assertEquals(source, Joiner.on('|').join(decomposed));
    }

    @Test
    public void factory_parseScanWifiNetworksTask() throws Exception {
        String[] source = new String[] {
                "task_id|0012",
                "task_name|Scan WIFI Network",
                "macro_id|1",
                "task_number|1",
                "init_date|20170418123456",
                "end_date|20170418123512",
                "iccid|",
                "cell_id|2,,23,,,,,,,,2,",
                "loc_gps|39.332233 N 8.323445 W 1.22332 23.44322",
                "status|0",
                "wifi_networks_list|meo§wifi1§my-network§WF344343"
        };

        Map<String, String> values = getPropertyMap(source);
        assertEquals(11, values.size());

        TaskResult fromMap = TaskResultFactory.createFromMap(values);
        assertTrue(fromMap instanceof ScanWifiNetworkTaskResult);

        ScanWifiNetworkTaskResult taskData = (ScanWifiNetworkTaskResult) fromMap;
        assertEquals("0012", taskData.getTaskId());
        assertEquals(4, taskData.getWifiNetworkList().length);

        Map<String, String> decomposed = TaskResultFactory.decomposeObjectToMap(taskData);
        assertEquals(11, decomposed.size());
        for (String key : decomposed.keySet()) {
            assertEquals(values.get(key), decomposed.get(key));
        }
    }

    @Test
    public void factory_parseSendSmsTask() throws Exception {
        String source = BASE_MESSAGE_1 + "12" + BASE_MESSAGE_2 + "23|987654321|Sample message for text";

        ArrayList<String> values = getPropertyList(source);
        assertEquals(12, values.size());

        TaskResult fromList = TaskResultFactory.createFromList(values);
        assertTrue(fromList instanceof SendSmsTaskResult);

        SendSmsTaskResult taskResult = (SendSmsTaskResult) fromList;
        assertEquals(1, taskResult.getTaskNumber());
        assertEquals("23", taskResult.getSendingTime());
        assertEquals("987654321", taskResult.getDestinationNumber());
        assertEquals("Sample message for text", taskResult.getMessageText());

        List<String> decomposed = TaskResultFactory.decomposeObjectToList(taskResult);
        assertEquals(12, decomposed.size());
        for (int i = 0; i < 12; i++) {
            assertEquals(values.get(i), decomposed.get(i));
        }
        assertEquals(source, Joiner.on('|').join(decomposed));
    }

    @NonNull
    private ArrayList<String> getPropertyList(String source) {
        Iterable<String> iterable = Splitter.on('|').trimResults().split(source);
        return Lists.newArrayList(iterable);
    }

    @NonNull
    private Map<String, String> getPropertyMap(String[] source) throws JSONException {
        Map<String, String> values = new HashMap<>();
        for (String value : source) {
            ArrayList<String> pair = Lists.newArrayList(Splitter.on('|').trimResults().split(value));
            values.put(pair.get(0), pair.get(1).replace("§", "|"));
        }
        return values;
    }
}

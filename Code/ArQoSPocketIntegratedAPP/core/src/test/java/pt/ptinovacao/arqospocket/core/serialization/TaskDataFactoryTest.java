package pt.ptinovacao.arqospocket.core.serialization;

import android.support.annotation.NonNull;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import org.json.JSONException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.AnswerVoiceCallTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.AssociateWiFiTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.DisassociateWiFiTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.HangUpVoiceCallTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.HttpDownloadTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.HttpUploadTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.MakeVoiceCallTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.PingTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.ReceiveSmsTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.RecordAudioTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.ScanWifiNetworkTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.SendSmsTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.TurnOffMobileTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.TurnOffWifiTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.TurnOnMobileTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.TurnOnWifiTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.WifiAuthLoginTaskData;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.WifiAuthLogoutTaskData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TaskDataFactoryTest {

    @Test
    public void factory_parseAnswerCallTask() throws Exception {
        String source = "419232|1||2016-07-20T13:30:00.000Z|30|2|0|1|1|10||||answer|||5||||";

        ArrayList<String> values = getPropertyList(source);
        assertEquals(21, values.size());

        TaskData fromList = TaskDataFactory.createFromList(values);
        assertTrue(fromList instanceof AnswerVoiceCallTaskData);

        AnswerVoiceCallTaskData taskData = (AnswerVoiceCallTaskData) fromList;
        assertEquals(1, taskData.getTaskNumber());
        assertEquals("1", taskData.getCallType());
        assertEquals(1, taskData.getRingingTime());
        assertEquals(10, taskData.getCallDurationInSeconds());
        assertEquals("answer", taskData.getAudioRecordingFileName());
        assertEquals(Integer.valueOf(5), taskData.getAudioRecordingTime());
        assertEquals(null, taskData.getAudioType());

        List<String> decomposed = TaskDataFactory.decomposeObjectToList(taskData);
        assertEquals(21, decomposed.size());
        for (int i = 0; i < 21; i++) {
            assertEquals(values.get(i), decomposed.get(i));
        }
    }

    @Test
    public void factory_parseAssociateWifiTask() throws Exception {
        String[] source = new String[] {
                "task_id|0006",
                "task_name|Associate WiFi",
                "macro_id|1",
                "task_number|2",
                "iccid|",
                "instanteExec|30",
                "timeout|20",
                "immediate|0",
                "essid|MEO-WiFi-Premium",
                "password|",
                "bssid|"
        };

        Map<String, String> values = getPropertyMap(source);
        assertEquals(11, values.size());

        TaskData fromMap = TaskDataFactory.createFromMap(values);
        assertTrue(fromMap instanceof AssociateWiFiTaskData);

        AssociateWiFiTaskData taskData = (AssociateWiFiTaskData) fromMap;
        assertEquals("0006", taskData.getTaskId());
        assertEquals("MEO-WiFi-Premium", taskData.getEssid());

        Map<String, String> decomposed = TaskDataFactory.decomposeObjectToMap(taskData);
        assertEquals(11, decomposed.size());
        for (String key : decomposed.keySet()) {
            assertEquals(values.get(key), decomposed.get(key));
        }
    }

    @Test
    public void factory_parseDisassociateWifiTask() throws Exception {
        String[] source = new String[] {
                "task_id|0013",
                "task_name|Deassociate WiFi",
                "macro_id|1",
                "task_number|2",
                "iccid|",
                "instanteExec|30",
                "timeout|20",
                "immediate|0"
        };

        Map<String, String> values = getPropertyMap(source);
        assertEquals(8, values.size());

        TaskData fromMap = TaskDataFactory.createFromMap(values);
        assertTrue(fromMap instanceof DisassociateWiFiTaskData);

        DisassociateWiFiTaskData taskData = (DisassociateWiFiTaskData) fromMap;
        assertEquals("0013", taskData.getTaskId());

        Map<String, String> decomposed = TaskDataFactory.decomposeObjectToMap(taskData);
        assertEquals(8, decomposed.size());
        for (String key : decomposed.keySet()) {
            assertEquals(values.get(key), decomposed.get(key));
        }
    }

    @Test
    public void factory_parseHangUpCallTask() throws Exception {
        String source = "419232|1||2016-07-20T13:30:00.000Z|30|3|0|1";

        ArrayList<String> values = getPropertyList(source);
        assertEquals(8, values.size());

        TaskData fromList = TaskDataFactory.createFromList(values);
        assertTrue(fromList instanceof HangUpVoiceCallTaskData);

        HangUpVoiceCallTaskData taskData = (HangUpVoiceCallTaskData) fromList;
        assertEquals(1, taskData.getCallsToBeTerminated());

        List<String> decomposed = TaskDataFactory.decomposeObjectToList(taskData);
        assertEquals(8, decomposed.size());
        for (int i = 0; i < 8; i++) {
            assertEquals(values.get(i), decomposed.get(i));
        }
    }

    @Test
    public void factory_parseHttpDownloadTask() throws Exception {
        String[] source = new String[] {
                "task_id|0004",
                "task_name|Http Download",
                "macro_id|1",
                "task_number|1",
                "iccid|",
                "instanteExec|0",
                "timeout|20",
                "immediate|0",
                "url|https://meo.pt",
                "proxy|",
                "user_agent|Mozzila/5.0"
        };

        Map<String, String> values = getPropertyMap(source);
        assertEquals(11, values.size());

        TaskData fromMap = TaskDataFactory.createFromMap(values);
        assertTrue(fromMap instanceof HttpDownloadTaskData);

        HttpDownloadTaskData taskData = (HttpDownloadTaskData) fromMap;
        assertEquals("0004", taskData.getTaskId());
        assertEquals("Mozzila/5.0", taskData.getUserAgent());

        Map<String, String> decomposed = TaskDataFactory.decomposeObjectToMap(taskData);
        assertEquals(11, decomposed.size());
        for (String key : decomposed.keySet()) {
            assertEquals(values.get(key), decomposed.get(key));
        }
    }

    @Test
    public void factory_parseHttpUploadTask() throws Exception {
        String[] source = new String[] {
                "task_id|0005",
                "task_name|Http Upload",
                "macro_id|1",
                "task_number|5",
                "iccid|",
                "instanteExec|70",
                "timeout|60",
                "immediate|0",
                "url|http://www.speedtest.co.bw/speedtest/upload.php",
                "proxy|",
                "content|content0=1234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                        "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                        "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                        "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                        "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                        "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                        "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                        "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                        "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                        "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                        "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                        "0123456789012345678901234567890123456789012345678901234567890"
        };

        Map<String, String> values = getPropertyMap(source);
        assertEquals(11, values.size());

        TaskData fromMap = TaskDataFactory.createFromMap(values);
        assertTrue(fromMap instanceof HttpUploadTaskData);

        HttpUploadTaskData taskData = (HttpUploadTaskData) fromMap;
        assertEquals("0005", taskData.getTaskId());
        assertEquals("content0=1234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                "0123456789012345678901234567890123456789012345678901234567890", taskData.getContent());

        Map<String, String> decomposed = TaskDataFactory.decomposeObjectToMap(taskData);
        assertEquals(11, decomposed.size());
        for (String key : decomposed.keySet()) {
            assertEquals(values.get(key), decomposed.get(key));
        }
    }

    @Test
    public void factory_parseMakeVoiceCallTask() throws Exception {
        String source =
                "419232|1||2016-07-20T13:30:00.000Z|30|1||1|964389135||||0|||||||||||3|2||||||||||||1|||||||||makecall||";

        ArrayList<String> values = getPropertyList(source);
        assertEquals(48, values.size());

        TaskData fromList = TaskDataFactory.createFromList(values);
        assertTrue(fromList instanceof MakeVoiceCallTaskData);

        MakeVoiceCallTaskData taskData = (MakeVoiceCallTaskData) fromList;
        assertEquals("1", taskData.getCallType());
        assertEquals("964389135", taskData.getDestinationNumber());
        assertEquals(3, taskData.getCallDurationInSeconds());
        assertEquals("makecall", taskData.getAudioRecordingFileName());
        assertEquals(null, taskData.getAudioRecordingTime());
        assertEquals(null, taskData.getAudioType());

        List<String> decomposed = TaskDataFactory.decomposeObjectToList(taskData);
        assertEquals(48, decomposed.size());
        for (int i = 0; i < 48; i++) {
            assertEquals("Index: " + i, values.get(i), decomposed.get(i));
        }
    }

    @Test
    public void factory_parseMakeVoiceCallTaskV2() throws Exception {
        String source =
                "419232|1||2016-07-20T13:30:00.000Z|30|1||1|964389135||||0|||||||||||0|2||||||||||||1|||||||||||";

        ArrayList<String> values = getPropertyList(source);
        assertEquals(48, values.size());

        TaskData fromList = TaskDataFactory.createFromList(values);
        assertTrue(fromList instanceof MakeVoiceCallTaskData);

        MakeVoiceCallTaskData taskData = (MakeVoiceCallTaskData) fromList;
        assertEquals("1", taskData.getCallType());
        assertEquals("964389135", taskData.getDestinationNumber());
        assertEquals(0, taskData.getCallDurationInSeconds());
        assertEquals("", taskData.getAudioRecordingFileName());
        assertEquals(null, taskData.getAudioRecordingTime());
        assertEquals(null, taskData.getAudioType());

        List<String> decomposed = TaskDataFactory.decomposeObjectToList(taskData);
        assertEquals(48, decomposed.size());
        for (int i = 0; i < 48; i++) {
            assertEquals("Index: " + i, values.get(i), decomposed.get(i));
        }
    }

    @Test
    public void factory_parsePingTask() throws Exception {
        String[] source = new String[] {
                "task_id|0001",
                "task_name|Ping",
                "macro_id|1",
                "task_number|6",
                "iccid|",
                "instanteExec|0",
                "timeout|10",
                "immediate|0",
                "packet_size|16",
                "interval|1",
                "packet_number|8",
                "ip_address|8.8.8.8"
        };

        Map<String, String> values = getPropertyMap(source);
        assertEquals(12, values.size());

        TaskData fromMap = TaskDataFactory.createFromMap(values);
        assertTrue(fromMap instanceof PingTaskData);

        PingTaskData taskData = (PingTaskData) fromMap;
        assertEquals("0001", taskData.getTaskId());
        assertEquals("8.8.8.8", taskData.getIpAddress());

        Map<String, String> decomposed = TaskDataFactory.decomposeObjectToMap(taskData);
        assertEquals(12, decomposed.size());
        for (String key : decomposed.keySet()) {
            assertEquals(values.get(key), decomposed.get(key));
        }
    }

    @Test
    public void factory_parseReceiveSmsTask() throws Exception {
        String source = "1|1||0|60|36|0|1234|1234|";

        ArrayList<String> values = getPropertyList(source);
        assertEquals(10, values.size());

        TaskData fromList = TaskDataFactory.createFromList(values);
        assertTrue(fromList instanceof ReceiveSmsTaskData);

        ReceiveSmsTaskData taskData = (ReceiveSmsTaskData) fromList;
        assertEquals("1234", taskData.getExpectedTrailer());
        assertEquals("1234", taskData.getMessageToVerify());

        List<String> decomposed = TaskDataFactory.decomposeObjectToList(taskData);
        assertEquals(10, decomposed.size());
        for (int i = 0; i < 10; i++) {
            assertEquals("Index: " + i, values.get(i), decomposed.get(i));
        }
    }

    @Test
    public void factory_parseRecordAudioTask() throws Exception {
        String source = "419232|1||2016-07-20T13:30:00.000Z|30|42|0|record|20|1";

        ArrayList<String> values = getPropertyList(source);
        assertEquals(10, values.size());

        TaskData fromList = TaskDataFactory.createFromList(values);
        assertTrue(fromList instanceof RecordAudioTaskData);

        RecordAudioTaskData taskData = (RecordAudioTaskData) fromList;
        assertEquals("record", taskData.getAudioRecordingFileName());
        assertEquals(20, taskData.getAudioRecordingTime());
        assertEquals("1", taskData.getAudioType());

        List<String> decomposed = TaskDataFactory.decomposeObjectToList(taskData);
        assertEquals(10, decomposed.size());
        for (int i = 0; i < 10; i++) {
            assertEquals("Index: " + i, values.get(i), decomposed.get(i));
        }
    }

    @Test
    public void factory_parseScanWifiNetworkTask() throws Exception {
        String[] source = new String[] {
                "task_id|0012",
                "task_name|Scan WiFi Networks",
                "macro_id|1",
                "task_number|2",
                "iccid|",
                "instanteExec|30",
                "timeout|20",
                "immediate|0"
        };

        Map<String, String> values = getPropertyMap(source);
        assertEquals(8, values.size());

        TaskData fromMap = TaskDataFactory.createFromMap(values);
        assertTrue(fromMap instanceof ScanWifiNetworkTaskData);

        ScanWifiNetworkTaskData taskData = (ScanWifiNetworkTaskData) fromMap;
        assertEquals("0012", taskData.getTaskId());
        assertEquals("30", taskData.getExecutionDelay());

        Map<String, String> decomposed = TaskDataFactory.decomposeObjectToMap(taskData);
        assertEquals(8, decomposed.size());
        for (String key : decomposed.keySet()) {
            assertEquals(values.get(key), decomposed.get(key));
        }
    }

    @Test
    public void factory_parseSendSmsTask() throws Exception {
        String source = "1|1||0|60|12|0|5556|ArQoS Pocket SMS Test1234|1234|0||0|0|";

        ArrayList<String> values = getPropertyList(source);
        assertEquals(15, values.size());

        TaskData fromList = TaskDataFactory.createFromList(values);
        assertTrue(fromList instanceof SendSmsTaskData);

        SendSmsTaskData taskData = (SendSmsTaskData) fromList;
        assertEquals("5556", taskData.getDestinationNumber());
        assertEquals("ArQoS Pocket SMS Test1234", taskData.getTextMessage());
        assertEquals("1234", taskData.getTrailerText());

        List<String> decomposed = TaskDataFactory.decomposeObjectToList(taskData);
        assertEquals(15, decomposed.size());
        for (int i = 0; i < 15; i++) {
            assertEquals("Index: " + i, values.get(i), decomposed.get(i));
        }
    }

    @Test
    public void factory_parseTurnOffMobileTask() throws Exception {
        String[] source = new String[] {
                "task_id|0002",
                "task_name|Turn OFF Mobile",
                "macro_id|1",
                "task_number|4",
                "iccid|",
                "instanteExec|90",
                "timeout|20",
                "immediate|0"
        };

        Map<String, String> values = getPropertyMap(source);
        assertEquals(8, values.size());

        TaskData fromMap = TaskDataFactory.createFromMap(values);
        assertTrue(fromMap instanceof TurnOffMobileTaskData);

        TurnOffMobileTaskData taskData = (TurnOffMobileTaskData) fromMap;
        assertEquals("0002", taskData.getTaskId());

        Map<String, String> decomposed = TaskDataFactory.decomposeObjectToMap(taskData);
        assertEquals(8, decomposed.size());
        for (String key : decomposed.keySet()) {
            assertEquals(values.get(key), decomposed.get(key));
        }
    }

    @Test
    public void factory_parseTurnOffWifiTask() throws Exception {
        String[] source = new String[] {
                "task_id|0007",
                "task_name|Turn OFF WiFi",
                "macro_id|1",
                "task_number|4",
                "iccid|",
                "instanteExec|90",
                "timeout|20",
                "immediate|0"
        };

        Map<String, String> values = getPropertyMap(source);
        assertEquals(8, values.size());

        TaskData fromMap = TaskDataFactory.createFromMap(values);
        assertTrue(fromMap instanceof TurnOffWifiTaskData);

        TurnOffWifiTaskData taskData = (TurnOffWifiTaskData) fromMap;
        assertEquals("0007", taskData.getTaskId());

        Map<String, String> decomposed = TaskDataFactory.decomposeObjectToMap(taskData);
        assertEquals(8, decomposed.size());
        for (String key : decomposed.keySet()) {
            assertEquals(values.get(key), decomposed.get(key));
        }
    }

    @Test
    public void factory_parseTurnOnMobileTask() throws Exception {
        String[] source = new String[] {
                "task_id|0003",
                "task_name|Turn ON Mobile",
                "macro_id|1",
                "task_number|4",
                "iccid|",
                "instanteExec|90",
                "timeout|20",
                "immediate|0"
        };

        Map<String, String> values = getPropertyMap(source);
        assertEquals(8, values.size());

        TaskData fromMap = TaskDataFactory.createFromMap(values);
        assertTrue(fromMap instanceof TurnOnMobileTaskData);

        TurnOnMobileTaskData taskData = (TurnOnMobileTaskData) fromMap;
        assertEquals("0003", taskData.getTaskId());

        Map<String, String> decomposed = TaskDataFactory.decomposeObjectToMap(taskData);
        assertEquals(8, decomposed.size());
        for (String key : decomposed.keySet()) {
            assertEquals(values.get(key), decomposed.get(key));
        }
    }

    @Test
    public void factory_parseTurnOnWifiTask() throws Exception {
        String[] source = new String[] {
                "task_id|0008",
                "task_name|Turn ON WiFi",
                "macro_id|1",
                "task_number|1",
                "iccid|",
                "instanteExec|0",
                "timeout|20",
                "immediate|0"
        };

        Map<String, String> values = getPropertyMap(source);
        assertEquals(8, values.size());

        TaskData fromMap = TaskDataFactory.createFromMap(values);
        assertTrue(fromMap instanceof TurnOnWifiTaskData);

        TurnOnWifiTaskData taskData = (TurnOnWifiTaskData) fromMap;
        assertEquals("0008", taskData.getTaskId());

        Map<String, String> decomposed = TaskDataFactory.decomposeObjectToMap(taskData);
        assertEquals(8, decomposed.size());
        for (String key : decomposed.keySet()) {
            assertEquals(values.get(key), decomposed.get(key));
        }
    }

    @Test
    public void factory_parseWifiAuthLoginTask() throws Exception {
        String[] source = new String[] {
                "task_id|0010",
                "task_name|WiFi Authentication Login",
                "macro_id|1",
                "task_number|3",
                "iccid|",
                "instanteExec|60",
                "timeout|20",
                "immediate|0",
                "url|https://wifi.meo.pt/Httphandlers/login.aspx",
                "username|guest",
                "password|guest"
        };

        Map<String, String> values = getPropertyMap(source);
        assertEquals(11, values.size());

        TaskData fromMap = TaskDataFactory.createFromMap(values);
        assertTrue(fromMap instanceof WifiAuthLoginTaskData);

        WifiAuthLoginTaskData taskData = (WifiAuthLoginTaskData) fromMap;
        assertEquals("0010", taskData.getTaskId());
        assertEquals("https://wifi.meo.pt/Httphandlers/login.aspx", taskData.getUrl());
        assertEquals("guest", taskData.getUsername());
        assertEquals("guest", taskData.getPassword());

        Map<String, String> decomposed = TaskDataFactory.decomposeObjectToMap(taskData);
        assertEquals(11, decomposed.size());
        for (String key : decomposed.keySet()) {
            assertEquals(values.get(key), decomposed.get(key));
        }
    }

    @Test
    public void factory_parseWifiAuthLogoutTask() throws Exception {
        String[] source = new String[] {
                "task_id|0011",
                "task_name|WiFi Authentication Logout",
                "macro_id|1",
                "task_number|9",
                "iccid|",
                "instanteExec|180",
                "timeout|20",
                "immediate|0",
                "url|https://wifi.meo.pt/Httphandlers/logoff.aspx"
        };

        Map<String, String> values = getPropertyMap(source);
        assertEquals(9, values.size());

        TaskData fromMap = TaskDataFactory.createFromMap(values);
        assertTrue(fromMap instanceof WifiAuthLogoutTaskData);

        WifiAuthLogoutTaskData taskData = (WifiAuthLogoutTaskData) fromMap;
        assertEquals("0011", taskData.getTaskId());
        assertEquals("https://wifi.meo.pt/Httphandlers/logoff.aspx", taskData.getUrl());

        Map<String, String> decomposed = TaskDataFactory.decomposeObjectToMap(taskData);
        assertEquals(9, decomposed.size());
        for (String key : decomposed.keySet()) {
            assertEquals(values.get(key), decomposed.get(key));
        }
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
            values.put(pair.get(0), pair.get(1));
        }
        return values;
    }
}

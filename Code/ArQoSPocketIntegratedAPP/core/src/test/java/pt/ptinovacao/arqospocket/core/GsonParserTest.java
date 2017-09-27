package pt.ptinovacao.arqospocket.core;

import com.google.gson.Gson;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import pt.ptinovacao.arqospocket.core.tests.data.ParameterData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.ScanWifiNetworkTaskResult;
import pt.ptinovacao.arqospocket.core.utils.JsonHelper;
import pt.ptinovacao.arqospocket.core.utils.WeekDays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GsonParserTest {

    @Test
    public void gson_parsesParameterData() {
        String json = "{\"interval\":0,\"hour\":\"200000\",\"weekdays\":65}";
        Gson gson = JsonHelper.getGsonInstance();

        ParameterData data = gson.fromJson(json, ParameterData.class);
        WeekDays days = data.getWeekDays();

        assertTrue(days.sunday());
        assertFalse(days.monday());
        assertFalse(days.tuesday());
        assertFalse(days.wednesday());
        assertFalse(days.thursday());
        assertFalse(days.friday());
        assertTrue(days.saturday());

    }

    @Test
    public void gson_parsesParameterDataWeek() {
        String json = "{\"interval\":0,\"hour\":\"200000\"}";
        Gson gson = JsonHelper.getGsonInstance();

        ParameterData data = gson.fromJson(json, ParameterData.class);
        WeekDays days = data.getWeekDays();

        assertNull(days);

    }

    @Test
    public void gson_parsesParameterDataAndWrites() {
        String json = "{\"interval\":0,\"hour\":\"200000\",\"weekdays\":65}";
        Gson gson = JsonHelper.getGsonInstance(false);

        ParameterData data = gson.fromJson(json, ParameterData.class);
        String result = gson.toJson(data);

        assertEquals(json, result);
    }

    @Test
    public void gson_parsesParameterDataAndDate() {
        String json = "{\"interval\":0,\"hour\":\"200000\",\"weekdays\":65}";
        Gson gson = JsonHelper.getGsonInstance();

        ParameterData data = gson.fromJson(json, ParameterData.class);
        Date date = data.getHour();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 20);
        assertEquals(calendar.get(Calendar.MINUTE), 0);
        assertEquals(calendar.get(Calendar.SECOND), 0);
    }

    @Test
    public void testParser_parseScanWiFiNetworkScanTask() {
        String source = "{\"task_id\":\"0012\",\"task_name\":\"Scan WIFI Network\",\"macro_id\":\"1\"," +
                "\"task_number\":\"1\",\"init_date\":\"20170418123456\",\"end_date\":\"20170418123512\"," +
                "\"iccid\":\"\",\"cell_id\":\"2,,23,,,,,,,,2,\"," +
                "\"loc_gps\":\"39.332233 N 8.323445 W 1.22332 23.44322\",\"status\":\"0\"," +
                "\"wifi_networks_list\":[\"meo\",\"wifi1\",\"my-network\",\"WF344343\"]}";

        Gson gsonInstance = JsonHelper.getGsonInstance(false);
        TaskResult taskResult = gsonInstance.fromJson(source, TaskResult.class);

        assertTrue(taskResult instanceof ScanWifiNetworkTaskResult);

        ScanWifiNetworkTaskResult result = (ScanWifiNetworkTaskResult) taskResult;

        assertNotNull(result);
        assertNotNull(result.getWifiNetworkList());
        assertEquals(4, result.getWifiNetworkList().length);

        String output = gsonInstance.toJson(result);
        assertTrue(output.contains("\"wifi_networks_list\":[\"meo\",\"wifi1\",\"my-network\",\"WF344343\"]"));
    }
}
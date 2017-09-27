package pt.ptinovacao.arqospocket.core.serialization.composers;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

import java.util.Map;

import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.ScanWifiNetworkTaskResult;

/**
 * Scan WiFi networks task.
 * <p>
 * Created by Emílio Simões on 19-04-2017.
 */
class ScanWifiNetworkTaskObjectComposer extends MapObjectComposer {

    @Override
    public void fill(TaskData data, Map<String, String> propertyMap) {
        // No additional fields
    }

    @Override
    public void fill(TaskResult data, Map<String, String> propertyMap) {
        fillResultData((ScanWifiNetworkTaskResult) data, propertyMap);
    }

    @Override
    public void collectToMap(TaskData data, Map<String, String> propertyMap) {
        // No additional fields
    }

    @Override
    public void collectToMap(TaskResult data, Map<String, String> propertyMap) {
        collectResultData((ScanWifiNetworkTaskResult) data, propertyMap);
    }

    private void fillResultData(ScanWifiNetworkTaskResult data, Map<String, String> propertyMap) {
        data.setWifiNetworkList(parseArray(propertyMap.get(ScanWifiNetworkTaskResult.WIFI_NETWORKS_LIST)));
    }

    private void collectResultData(ScanWifiNetworkTaskResult data, Map<String, String> propertyMap) {
        propertyMap.put(ScanWifiNetworkTaskResult.WIFI_NETWORKS_LIST, formatArray(data.getWifiNetworkList()));
    }

    private String[] parseArray(String arrayString) {
        String source = Strings.nullToEmpty(arrayString).trim();
        if (source.length() == 0) {
            return new String[0];
        }

        Iterable<String> split = Splitter.on('|').trimResults().omitEmptyStrings().split(source);
        return Iterables.toArray(split, String.class);
    }

    private String formatArray(String[] array) {
        if (array == null) {
            return null;
        }
        return Joiner.on('|').useForNull("").join(array);
    }
}

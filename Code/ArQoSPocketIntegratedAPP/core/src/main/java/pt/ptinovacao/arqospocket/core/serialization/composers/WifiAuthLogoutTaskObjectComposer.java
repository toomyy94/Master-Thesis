package pt.ptinovacao.arqospocket.core.serialization.composers;

import java.util.Map;

import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.WifiAuthLogoutTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.WifiAuthLogoutTaskData;

/**
 * Wifi Auth logout task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
class WifiAuthLogoutTaskObjectComposer extends MapObjectComposer {

    @Override
    public void fill(TaskData data, Map<String, String> propertyMap) {
        fillTaskData((WifiAuthLogoutTaskData) data, propertyMap);
    }

    @Override
    public void fill(TaskResult data, Map<String, String> propertyMap) {
        fillResultData((WifiAuthLogoutTaskResult) data, propertyMap);
    }

    @Override
    public void collectToMap(TaskData data, Map<String, String> propertyMap) {
        collectTaskData((WifiAuthLogoutTaskData) data, propertyMap);
    }

    @Override
    public void collectToMap(TaskResult data, Map<String, String> propertyMap) {
        collectResultData((WifiAuthLogoutTaskResult) data, propertyMap);
    }

    private void fillTaskData(WifiAuthLogoutTaskData data, Map<String, String> propertyMap) {
        data.setUrl(propertyMap.get(WifiAuthLogoutTaskData.URL));
    }

    private void fillResultData(WifiAuthLogoutTaskResult data, Map<String, String> propertyMap) {
        data.setResponseCode(propertyMap.get(WifiAuthLogoutTaskResult.RESPONSE_CODE));
        data.setResponseTime(propertyMap.get(WifiAuthLogoutTaskResult.RESPONSE_TIME));
    }

    private void collectTaskData(WifiAuthLogoutTaskData data, Map<String, String> propertyMap) {
        propertyMap.put(WifiAuthLogoutTaskData.URL, data.getUrl());
    }

    private void collectResultData(WifiAuthLogoutTaskResult data, Map<String, String> propertyMap) {
        propertyMap.put(WifiAuthLogoutTaskResult.RESPONSE_CODE, data.getResponseCode());
        propertyMap.put(WifiAuthLogoutTaskResult.RESPONSE_TIME, data.getResponseTime());
    }
}

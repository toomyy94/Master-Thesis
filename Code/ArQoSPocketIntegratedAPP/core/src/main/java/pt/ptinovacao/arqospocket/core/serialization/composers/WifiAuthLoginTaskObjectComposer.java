package pt.ptinovacao.arqospocket.core.serialization.composers;

import java.util.Map;

import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.WifiAuthLoginTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.WifiAuthLoginTaskData;

/**
 * Wifi Auth login task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
class WifiAuthLoginTaskObjectComposer extends MapObjectComposer {

    @Override
    public void fill(TaskData data, Map<String, String> propertyMap) {
        fillTaskData((WifiAuthLoginTaskData) data, propertyMap);
    }

    @Override
    public void fill(TaskResult data, Map<String, String> propertyMap) {
        fillResultData((WifiAuthLoginTaskResult) data, propertyMap);
    }

    @Override
    public void collectToMap(TaskData data, Map<String, String> propertyMap) {
        collectTaskData((WifiAuthLoginTaskData) data, propertyMap);
    }

    @Override
    public void collectToMap(TaskResult data, Map<String, String> propertyMap) {
        collectResultData((WifiAuthLoginTaskResult) data, propertyMap);
    }

    private void fillTaskData(WifiAuthLoginTaskData data, Map<String, String> propertyMap) {
        data.setUrl(propertyMap.get(WifiAuthLoginTaskData.URL));
        data.setUsername(propertyMap.get(WifiAuthLoginTaskData.USERNAME));
        data.setPassword(propertyMap.get(WifiAuthLoginTaskData.PASSWORD));
    }

    private void fillResultData(WifiAuthLoginTaskResult data, Map<String, String> propertyMap) {
        data.setResponseCode(propertyMap.get(WifiAuthLoginTaskResult.RESPONSE_CODE));
        data.setResponseTime(propertyMap.get(WifiAuthLoginTaskResult.RESPONSE_TIME));
    }

    private void collectTaskData(WifiAuthLoginTaskData data, Map<String, String> propertyMap) {
        propertyMap.put(WifiAuthLoginTaskData.URL, data.getUrl());
        propertyMap.put(WifiAuthLoginTaskData.USERNAME, data.getUsername());
        propertyMap.put(WifiAuthLoginTaskData.PASSWORD, data.getPassword());
    }

    private void collectResultData(WifiAuthLoginTaskResult data, Map<String, String> propertyMap) {
        propertyMap.put(WifiAuthLoginTaskResult.RESPONSE_CODE, data.getResponseCode());
        propertyMap.put(WifiAuthLoginTaskResult.RESPONSE_TIME, data.getResponseTime());
    }
}

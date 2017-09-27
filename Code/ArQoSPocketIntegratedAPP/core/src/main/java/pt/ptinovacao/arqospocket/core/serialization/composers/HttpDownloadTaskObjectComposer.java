package pt.ptinovacao.arqospocket.core.serialization.composers;

import java.util.Map;

import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.HttpDownloadTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.HttpDownloadTaskData;

/**
 * HTTP download task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
class HttpDownloadTaskObjectComposer extends MapObjectComposer {

    @Override
    public void fill(TaskData data, Map<String, String> propertyMap) {
        fillTaskData((HttpDownloadTaskData) data, propertyMap);
    }

    @Override
    public void fill(TaskResult data, Map<String, String> propertyMap) {
        fillResultData((HttpDownloadTaskResult) data, propertyMap);
    }

    @Override
    public void collectToMap(TaskData data, Map<String, String> propertyMap) {
        collectTaskData((HttpDownloadTaskData) data, propertyMap);
    }

    @Override
    public void collectToMap(TaskResult data, Map<String, String> propertyMap) {
        collectResultData((HttpDownloadTaskResult) data, propertyMap);
    }

    private void fillTaskData(HttpDownloadTaskData data, Map<String, String> propertyMap) {
        data.setUrl(propertyMap.get(HttpDownloadTaskData.URL));
        data.setProxy(propertyMap.get(HttpDownloadTaskData.PROXY));
        data.setUserAgent(propertyMap.get(HttpDownloadTaskData.USER_AGENT));
    }

    private void fillResultData(HttpDownloadTaskResult data, Map<String, String> propertyMap) {
        data.setReceivedData(propertyMap.get(HttpDownloadTaskResult.RECEIVED_DATA));
        data.setReceivedDataSize(propertyMap.get(HttpDownloadTaskResult.RECEIVED_DATA_SIZE));
        data.setDownloadTimeInSeconds(propertyMap.get(HttpDownloadTaskResult.DOWNLOAD_TIME_IN_SECONDS));
        data.setAccessTimeInSeconds(propertyMap.get(HttpDownloadTaskResult.ACCESS_TIME_IN_SECONDS));
        data.setThroughput(propertyMap.get(HttpDownloadTaskResult.THROUGHPUT));
    }

    private void collectTaskData(HttpDownloadTaskData data, Map<String, String> propertyMap) {
        propertyMap.put(HttpDownloadTaskData.URL, data.getUrl());
        propertyMap.put(HttpDownloadTaskData.PROXY, data.getProxy());
        propertyMap.put(HttpDownloadTaskData.USER_AGENT, data.getUserAgent());
    }

    private void collectResultData(HttpDownloadTaskResult data, Map<String, String> propertyMap) {
        propertyMap.put(HttpDownloadTaskResult.RECEIVED_DATA, data.getReceivedData());
        propertyMap.put(HttpDownloadTaskResult.RECEIVED_DATA_SIZE, data.getReceivedDataSize());
        propertyMap.put(HttpDownloadTaskResult.DOWNLOAD_TIME_IN_SECONDS, data.getDownloadTimeInSeconds());
        propertyMap.put(HttpDownloadTaskResult.ACCESS_TIME_IN_SECONDS, data.getAccessTimeInSeconds());
        propertyMap.put(HttpDownloadTaskResult.THROUGHPUT, data.getThroughput());
    }
}


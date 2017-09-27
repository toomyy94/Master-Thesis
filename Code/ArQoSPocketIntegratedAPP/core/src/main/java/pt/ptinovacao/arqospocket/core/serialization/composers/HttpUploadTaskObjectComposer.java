package pt.ptinovacao.arqospocket.core.serialization.composers;

import java.util.Map;

import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.HttpUploadTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.HttpUploadTaskData;

/**
 * HTTP upload task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
class HttpUploadTaskObjectComposer extends MapObjectComposer {

    @Override
    public void fill(TaskData data, Map<String, String> propertyMap) {
        fillTaskData((HttpUploadTaskData) data, propertyMap);
    }

    @Override
    public void fill(TaskResult data, Map<String, String> propertyMap) {
        fillResultData((HttpUploadTaskResult) data, propertyMap);
    }

    @Override
    public void collectToMap(TaskData data, Map<String, String> propertyMap) {
        collectTaskData((HttpUploadTaskData) data, propertyMap);
    }

    @Override
    public void collectToMap(TaskResult data, Map<String, String> propertyMap) {
        collectResultData((HttpUploadTaskResult) data, propertyMap);
    }

    private void fillTaskData(HttpUploadTaskData data, Map<String, String> propertyMap) {
        data.setUrl(propertyMap.get(HttpUploadTaskData.URL));
        data.setProxy(propertyMap.get(HttpUploadTaskData.PROXY));
        data.setContent(propertyMap.get(HttpUploadTaskData.CONTENT));
    }

    private void fillResultData(HttpUploadTaskResult data, Map<String, String> propertyMap) {
        data.setSentData(propertyMap.get(HttpUploadTaskResult.SENT_DATA));
        data.setSentDataSize(propertyMap.get(HttpUploadTaskResult.SENT_DATA_SIZE));
        data.setUploadTimeInSeconds(propertyMap.get(HttpUploadTaskResult.UPLOAD_TIME_IN_SECONDS));
        data.setAccessTimeInSeconds(propertyMap.get(HttpUploadTaskResult.ACCESS_TIME_IN_SECONDS));
        data.setThroughput(propertyMap.get(HttpUploadTaskResult.THROUGHPUT));
    }

    private void collectTaskData(HttpUploadTaskData data, Map<String, String> propertyMap) {
        propertyMap.put(HttpUploadTaskData.URL, data.getUrl());
        propertyMap.put(HttpUploadTaskData.PROXY, data.getProxy());
        propertyMap.put(HttpUploadTaskData.CONTENT, data.getContent());
    }

    private void collectResultData(HttpUploadTaskResult data, Map<String, String> propertyMap) {
        propertyMap.put(HttpUploadTaskResult.SENT_DATA, data.getSentData());
        propertyMap.put(HttpUploadTaskResult.SENT_DATA_SIZE, data.getSentDataSize());
        propertyMap.put(HttpUploadTaskResult.UPLOAD_TIME_IN_SECONDS, data.getUploadTimeInSeconds());
        propertyMap.put(HttpUploadTaskResult.ACCESS_TIME_IN_SECONDS, data.getAccessTimeInSeconds());
        propertyMap.put(HttpUploadTaskResult.THROUGHPUT, data.getThroughput());
    }
}


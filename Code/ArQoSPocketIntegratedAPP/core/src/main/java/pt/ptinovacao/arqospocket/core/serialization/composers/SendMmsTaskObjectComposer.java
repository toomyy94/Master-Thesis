package pt.ptinovacao.arqospocket.core.serialization.composers;

import java.util.Map;

import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.SendMmsTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.SendMmsTaskData;

/**
 * Send MMS task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
class SendMmsTaskObjectComposer extends MapObjectComposer {

    @Override
    public void fill(TaskData data, Map<String, String> propertyMap) {
        fillTaskData((SendMmsTaskData) data, propertyMap);
    }

    @Override
    public void fill(TaskResult data, Map<String, String> propertyMap) {
        fillResultData((SendMmsTaskResult) data, propertyMap);
    }

    @Override
    public void collectToMap(TaskData data, Map<String, String> propertyMap) {
        collectTaskData((SendMmsTaskData) data, propertyMap);
    }

    @Override
    public void collectToMap(TaskResult data, Map<String, String> propertyMap) {
        collectResultData((SendMmsTaskResult) data, propertyMap);
    }

    private void fillTaskData(SendMmsTaskData data, Map<String, String> propertyMap) {
        data.setDestinationNumber(propertyMap.get(SendMmsTaskData.DESTINATION_NUMBER));
        data.setTextMessage(propertyMap.get(SendMmsTaskData.TEXT_MESSAGE));
        data.setImage(propertyMap.get(SendMmsTaskData.IMAGE));
        data.setResponseMessage(propertyMap.get(SendMmsTaskData.RESPONSE_MESSAGE));
        data.setEncoding(propertyMap.get(SendMmsTaskData.ENCODING));
    }

    private void fillResultData(SendMmsTaskResult data, Map<String, String> propertyMap) {
        data.setSentData(propertyMap.get(SendMmsTaskResult.SENT_DATA));
        data.setDataSize(propertyMap.get(SendMmsTaskResult.DATA_SIZE));
    }

    private void collectTaskData(SendMmsTaskData data, Map<String, String> propertyMap) {
        propertyMap.put(SendMmsTaskData.DESTINATION_NUMBER, data.getDestinationNumber());
        propertyMap.put(SendMmsTaskData.TEXT_MESSAGE, data.getTextMessage());
        propertyMap.put(SendMmsTaskData.IMAGE, data.getImage());
        propertyMap.put(SendMmsTaskData.RESPONSE_MESSAGE, data.getResponseMessage());
        propertyMap.put(SendMmsTaskData.ENCODING, data.getEncoding());
    }

    private void collectResultData(SendMmsTaskResult data, Map<String, String> propertyMap) {
        propertyMap.put(SendMmsTaskResult.SENT_DATA, data.getSentData());
        propertyMap.put(SendMmsTaskResult.DATA_SIZE, data.getDataSize());
    }
}


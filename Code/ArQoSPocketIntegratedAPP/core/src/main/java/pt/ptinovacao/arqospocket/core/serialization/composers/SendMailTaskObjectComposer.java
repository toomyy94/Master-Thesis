package pt.ptinovacao.arqospocket.core.serialization.composers;

import java.util.Map;

import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.SendMailTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.SendMailTaskData;

/**
 * Send Mail task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
class SendMailTaskObjectComposer extends MapObjectComposer {

    @Override
    public void fill(TaskData data, Map<String, String> propertyMap) {
        fillTaskData((SendMailTaskData) data, propertyMap);
    }

    @Override
    public void fill(TaskResult data, Map<String, String> propertyMap) {
        fillResultData((SendMailTaskResult) data, propertyMap);
    }

    @Override
    public void collectToMap(TaskData data, Map<String, String> propertyMap) {
        collectTaskData((SendMailTaskData) data, propertyMap);
    }

    @Override
    public void collectToMap(TaskResult data, Map<String, String> propertyMap) {
        collectResultData((SendMailTaskResult) data, propertyMap);
    }

    private void fillTaskData(SendMailTaskData data, Map<String, String> propertyMap) {
        data.setDestinationAddress(propertyMap.get(SendMailTaskData.DESTINATION_ADDRESS));
        data.setCarbonCopy(propertyMap.get(SendMailTaskData.CARBON_COPY));
        data.setBlackCarbonCopy(propertyMap.get(SendMailTaskData.BLACK_CARBON_COPY));
        data.setSubject(propertyMap.get(SendMailTaskData.SUBJECT));
        data.setBody(propertyMap.get(SendMailTaskData.BODY));
        data.setAttachedFile(propertyMap.get(SendMailTaskData.ATTACHED_FILE));
    }

    private void fillResultData(SendMailTaskResult data, Map<String, String> propertyMap) {
        data.setDestinationAddress(propertyMap.get(SendMailTaskResult.DESTINATION_ADDRESS));
        data.setMessageContent(propertyMap.get(SendMailTaskResult.MESSAGE_CONTENT));
        data.setMessageSize(propertyMap.get(SendMailTaskResult.MESSAGE_SIZE));
    }

    private void collectTaskData(SendMailTaskData data, Map<String, String> propertyMap) {
        propertyMap.put(SendMailTaskData.DESTINATION_ADDRESS, data.getDestinationAddress());
        propertyMap.put(SendMailTaskData.CARBON_COPY, data.getCarbonCopy());
        propertyMap.put(SendMailTaskData.BLACK_CARBON_COPY, data.getBlackCarbonCopy());
        propertyMap.put(SendMailTaskData.SUBJECT, data.getSubject());
        propertyMap.put(SendMailTaskData.BODY, data.getBody());
        propertyMap.put(SendMailTaskData.ATTACHED_FILE, data.getAttachedFile());
    }

    private void collectResultData(SendMailTaskResult data, Map<String, String> propertyMap) {
        propertyMap.put(SendMailTaskResult.DESTINATION_ADDRESS, data.getDestinationAddress());
        propertyMap.put(SendMailTaskResult.MESSAGE_CONTENT, data.getMessageContent());
        propertyMap.put(SendMailTaskResult.MESSAGE_SIZE, data.getMessageSize());
    }
}

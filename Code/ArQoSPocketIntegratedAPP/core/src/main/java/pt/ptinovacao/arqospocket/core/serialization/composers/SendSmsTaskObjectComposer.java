package pt.ptinovacao.arqospocket.core.serialization.composers;

import java.util.List;
import java.util.Map;

import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.SendSmsTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.SendSmsTaskData;

/**
 * Send SMS task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
class SendSmsTaskObjectComposer extends ObjectComposer {

    @Override
    public void fill(TaskData data, Map<String, String> propertyMap) {
        fillTaskData((SendSmsTaskData) data, propertyMap );
    }

    @Override
    public void fill(TaskResult data, Map<String, String> propertyMap) {
        fillResultData((SendSmsTaskResult) data, propertyMap);
    }

    @Override
    public void fill(TaskData data, List<String> propertyList) {
        fillTaskData((SendSmsTaskData) data, propertyList);
    }

    @Override
    public void fill(TaskResult data, List<String> propertyList) {
        fillResultData((SendSmsTaskResult) data, propertyList);
    }

    @Override
    public void collectToMap(TaskData data, Map<String, String> propertyMap) {
        collectTaskData((SendSmsTaskData) data, propertyMap);
    }

    @Override
    public void collectToMap(TaskResult data, Map<String, String> propertyMap) {
        collectResultData((SendSmsTaskResult) data, propertyMap);
    }

    @Override
    public void collectToList(TaskData data, List<String> propertyList) {
        collectTaskData((SendSmsTaskData) data, propertyList);
    }

    @Override
    public void collectToList(TaskResult data, List<String> propertyList) {
        collectResultData((SendSmsTaskResult) data, propertyList);
    }

    private void fillTaskData(SendSmsTaskData data, Map<String, String> propertyMap) {
        data.setDestinationNumber(propertyMap.get(SendSmsTaskData.DESTINATION_NUMBER));
        data.setTextMessage(propertyMap.get(SendSmsTaskData.TEXT_MESSAGE));
        data.setTrailerText(propertyMap.get(SendSmsTaskData.TRAILER_TEXT));
        data.setTrailerMetadata(toInteger(propertyMap.get(SendSmsTaskData.TRAILER_METADATA)));
        data.setSmsCenterNumber(propertyMap.get(SendSmsTaskData.SMS_CENTER_NUMBER));
        data.setReplySms(toInteger(propertyMap.get(SendSmsTaskData.REPLY_SMS)));
        data.setEncoding(propertyMap.get(SendSmsTaskData.ENCODING));
    }

    private void fillResultData(SendSmsTaskResult data, Map<String, String> propertyMap) {
        data.setSendingTime(propertyMap.get(SendSmsTaskResult.SENDING_TIME));
        data.setDestinationNumber(propertyMap.get(SendSmsTaskResult.DESTINATION_NUMBER));
        data.setMessageText(propertyMap.get(SendSmsTaskResult.MESSAGE_TEXT));
    }

    private void fillTaskData(SendSmsTaskData data, List<String> propertyList) {
        data.setDestinationNumber(propertyList.get(7));
        data.setTextMessage(propertyList.get(8));
        data.setTrailerText(propertyList.get(9));
        data.setTrailerMetadata(toInteger(propertyList.get(11)));
        data.setSmsCenterNumber(propertyList.get(12));
        data.setReplySms(toInteger(propertyList.get(13)));
        data.setEncoding(propertyList.get(14));
    }

    private void fillResultData(SendSmsTaskResult data, List<String> propertyList) {
        data.setSendingTime(propertyList.get(9));
        data.setDestinationNumber(propertyList.get(10));
        data.setMessageText(propertyList.get(11));
    }

    private void collectTaskData(SendSmsTaskData data, Map<String, String> propertyMap) {
        propertyMap.put(SendSmsTaskData.DESTINATION_NUMBER, data.getDestinationNumber());
        propertyMap.put(SendSmsTaskData.TEXT_MESSAGE, data.getTextMessage());
        propertyMap.put(SendSmsTaskData.TRAILER_TEXT, data.getTrailerText());
        propertyMap.put(SendSmsTaskData.TRAILER_METADATA, toString(data.getTrailerMetadata()));
        propertyMap.put(SendSmsTaskData.SMS_CENTER_NUMBER, data.getSmsCenterNumber());
        propertyMap.put(SendSmsTaskData.REPLY_SMS, toString(data.getReplySms()));
        propertyMap.put(SendSmsTaskData.ENCODING, data.getEncoding());
    }

    private void collectResultData(SendSmsTaskResult data, Map<String, String> propertyMap) {
        propertyMap.put(SendSmsTaskResult.SENDING_TIME, data.getSendingTime());
        propertyMap.put(SendSmsTaskResult.DESTINATION_NUMBER, data.getDestinationNumber());
        propertyMap.put(SendSmsTaskResult.MESSAGE_TEXT, data.getMessageText());
    }

    private void collectTaskData(SendSmsTaskData data, List<String> propertyList) {
        for (int i = 7; i < 15; i++) {
            propertyList.add("");
        }
        propertyList.set(7, data.getDestinationNumber());
        propertyList.set(8, data.getTextMessage());
        propertyList.set(9, data.getTrailerText());
        propertyList.set(11, toString(data.getTrailerMetadata()));
        propertyList.set(12, data.getSmsCenterNumber());
        propertyList.set(13, toString(data.getReplySms()));
        propertyList.set(14, data.getEncoding());
    }

    private void collectResultData(SendSmsTaskResult data, List<String> propertyList) {
        for (int i = 9; i < 12; i++) {
            propertyList.add("");
        }
        propertyList.set(9, data.getSendingTime());
        propertyList.set(10, data.getDestinationNumber());
        propertyList.set(11, data.getMessageText());
    }
}


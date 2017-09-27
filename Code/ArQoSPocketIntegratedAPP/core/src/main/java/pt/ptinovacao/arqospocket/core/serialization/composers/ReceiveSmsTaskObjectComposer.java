package pt.ptinovacao.arqospocket.core.serialization.composers;

import java.util.List;
import java.util.Map;

import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.ReceiveSmsTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.ReceiveSmsTaskData;

/**
 * Receive SMS task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
class ReceiveSmsTaskObjectComposer extends ObjectComposer {

    @Override
    public void fill(TaskData data, Map<String, String> propertyMap) {
        fillTaskData((ReceiveSmsTaskData) data, propertyMap);
    }

    @Override
    public void fill(TaskResult data, Map<String, String> propertyMap) {
        fillResultData((ReceiveSmsTaskResult) data, propertyMap);
    }

    @Override
    public void fill(TaskData data, List<String> propertyList) {
        fillTaskData((ReceiveSmsTaskData) data, propertyList);
    }

    @Override
    public void fill(TaskResult data, List<String> propertyList) {
        fillResultData((ReceiveSmsTaskResult) data, propertyList);
    }

    @Override
    public void collectToMap(TaskData data, Map<String, String> propertyMap) {
        collectTaskData((ReceiveSmsTaskData) data, propertyMap);
    }

    @Override
    public void collectToMap(TaskResult data, Map<String, String> propertyMap) {
        collectResultData((ReceiveSmsTaskResult) data, propertyMap);
    }

    @Override
    public void collectToList(TaskData data, List<String> propertyList) {
        collectTaskData((ReceiveSmsTaskData) data, propertyList);
    }

    @Override
    public void collectToList(TaskResult data, List<String> propertyList) {
        collectResultData((ReceiveSmsTaskResult) data, propertyList);
    }

    private void fillTaskData(ReceiveSmsTaskData data, Map<String, String> propertyMap) {
        data.setExpectedTrailer(propertyMap.get(ReceiveSmsTaskData.EXPECTED_TRAILER));
        data.setMessageToVerify(propertyMap.get(ReceiveSmsTaskData.MESSAGE_TO_VERIFY));
    }

    private void fillResultData(ReceiveSmsTaskResult data, Map<String, String> propertyMap) {
        data.setMessageText(propertyMap.get(ReceiveSmsTaskResult.MESSAGE_TEXT));
        data.setSourceNumber(propertyMap.get(ReceiveSmsTaskResult.SOURCE_NUMBER));
        data.setWaitingTime(propertyMap.get(ReceiveSmsTaskResult.WAITING_TIME));
        data.setMessageDeliveryTime(propertyMap.get(ReceiveSmsTaskResult.MESSAGE_DELIVERY_TIME));
        data.setEncoding(toInteger(propertyMap.get(ReceiveSmsTaskResult.ENCODING)));
    }

    private void fillTaskData(ReceiveSmsTaskData data, List<String> propertyList) {
        data.setExpectedTrailer(propertyList.get(7));
        data.setMessageToVerify(propertyList.get(8));
    }

    private void fillResultData(ReceiveSmsTaskResult data, List<String> propertyList) {
        data.setMessageText(propertyList.get(10));
        data.setSourceNumber(propertyList.get(12));
        data.setMessageDeliveryTime(propertyList.get(14));
        data.setWaitingTime(propertyList.get(15));
        data.setEncoding(toInteger(propertyList.get(16)));
    }

    private void collectTaskData(ReceiveSmsTaskData data, Map<String, String> propertyMap) {
        propertyMap.put(ReceiveSmsTaskData.EXPECTED_TRAILER, data.getExpectedTrailer());
        propertyMap.put(ReceiveSmsTaskData.MESSAGE_TO_VERIFY, data.getMessageToVerify());
    }

    private void collectResultData(ReceiveSmsTaskResult data, Map<String, String> propertyMap) {
        propertyMap.put(ReceiveSmsTaskResult.MESSAGE_TEXT, data.getMessageText());
        propertyMap.put(ReceiveSmsTaskResult.SOURCE_NUMBER, data.getSourceNumber());
        propertyMap.put(ReceiveSmsTaskResult.WAITING_TIME, data.getWaitingTime());
        propertyMap.put(ReceiveSmsTaskResult.MESSAGE_DELIVERY_TIME, data.getMessageDeliveryTime());
        propertyMap.put(ReceiveSmsTaskResult.ENCODING, toString(data.getEncoding()));
    }

    private void collectTaskData(ReceiveSmsTaskData data, List<String> propertyList) {
        for (int i = 7; i < 10; i++) {
            propertyList.add("");
        }
        propertyList.set(7, data.getExpectedTrailer());
        propertyList.set(8, data.getMessageToVerify());
    }

    private void collectResultData(ReceiveSmsTaskResult data, List<String> propertyList) {
        for (int i = 9; i < 17; i++) {
            propertyList.add("");
        }
        propertyList.set(10, data.getMessageText());
        propertyList.set(12, data.getSourceNumber());
        propertyList.set(14, data.getMessageDeliveryTime());
        propertyList.set(15, data.getWaitingTime());
        propertyList.set(16, toString(data.getEncoding()));
    }
}

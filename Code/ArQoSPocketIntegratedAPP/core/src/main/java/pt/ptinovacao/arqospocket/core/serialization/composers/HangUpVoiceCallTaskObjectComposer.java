package pt.ptinovacao.arqospocket.core.serialization.composers;

import java.util.List;
import java.util.Map;

import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.HangUpVoiceCallTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.HangUpVoiceCallTaskData;

/**
 * Hangup voice call task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
class HangUpVoiceCallTaskObjectComposer extends ObjectComposer {

    @Override
    public void fill(TaskData data, Map<String, String> propertyMap) {
        fillTaskData((HangUpVoiceCallTaskData) data, propertyMap);
    }

    @Override
    public void fill(TaskResult data, Map<String, String> propertyMap) {
        fillResultData((HangUpVoiceCallTaskResult) data, propertyMap);
    }

    @Override
    public void fill(TaskData data, List<String> propertyList) {
        fillTaskData((HangUpVoiceCallTaskData) data, propertyList);
    }

    @Override
    public void fill(TaskResult data, List<String> propertyList) {
        fillResultData((HangUpVoiceCallTaskResult) data, propertyList);
    }

    @Override
    public void collectToMap(TaskData data, Map<String, String> propertyMap) {
        collectTaskData((HangUpVoiceCallTaskData) data, propertyMap);
    }

    @Override
    public void collectToMap(TaskResult data, Map<String, String> propertyMap) {
        collectResultData((HangUpVoiceCallTaskResult) data, propertyMap);
    }

    @Override
    public void collectToList(TaskData data, List<String> propertyList) {
        collectTaskData((HangUpVoiceCallTaskData) data, propertyList);
    }

    @Override
    public void collectToList(TaskResult data, List<String> propertyList) {
        collectResultData((HangUpVoiceCallTaskResult) data, propertyList);
    }

    private static void fillTaskData(HangUpVoiceCallTaskData data, List<String> propertyList) {
        data.setCallsToBeTerminated(toInt(propertyList.get(7)));
    }

    private void fillResultData(HangUpVoiceCallTaskResult data, Map<String, String> propertyMap) {
        data.setCallDuration(propertyMap.get(HangUpVoiceCallTaskResult.CALL_DURATION));
    }

    private void fillResultData(HangUpVoiceCallTaskResult data, List<String> propertyList) {
        data.setCallDuration(propertyList.get(9));
        data.setFixedValue(toInt(propertyList.get(46)));
    }

    private void collectResultData(HangUpVoiceCallTaskResult data, Map<String, String> propertyMap) {
        propertyMap.put(HangUpVoiceCallTaskResult.CALL_DURATION, data.getCallDuration());
    }

    private void collectTaskData(HangUpVoiceCallTaskData data, List<String> propertyList) {
        propertyList.add(toString(data.getCallsToBeTerminated()));
    }

    private void collectTaskData(HangUpVoiceCallTaskData data, Map<String, String> propertyMap) {
        propertyMap.put(HangUpVoiceCallTaskData.CALLS_TO_BE_TERMINATED, toString(data.getCallsToBeTerminated()));
    }

    private void fillTaskData(HangUpVoiceCallTaskData data, Map<String, String> propertyMap) {
        data.setCallsToBeTerminated(toInt(propertyMap.get(HangUpVoiceCallTaskData.CALLS_TO_BE_TERMINATED)));
    }

    private void collectResultData(HangUpVoiceCallTaskResult data, List<String> propertyList) {
        for (int i = 9; i < 47; i++) {
            propertyList.add("");
        }
        propertyList.set(9, data.getCallDuration());
        propertyList.set(46, toString(data.getFixedValue()));
    }
}

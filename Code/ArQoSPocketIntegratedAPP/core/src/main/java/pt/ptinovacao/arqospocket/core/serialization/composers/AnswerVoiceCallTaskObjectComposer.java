package pt.ptinovacao.arqospocket.core.serialization.composers;

import java.util.List;
import java.util.Map;

import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.AnswerVoiceCallTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.AnswerVoiceCallTaskData;

/**
 * ObjectComposer for the {@link AnswerVoiceCallTaskData}.
 * <p>
 * Created by Emílio Simões on 13-04-2017.
 */
class AnswerVoiceCallTaskObjectComposer extends ObjectComposer {

    @Override
    public void fill(TaskData data, Map<String, String> propertyMap) {

    }

    @Override
    public void fill(TaskResult data, Map<String, String> propertyMap) {
        fillResultData((AnswerVoiceCallTaskResult) data, propertyMap);
    }

    @Override
    public void fill(TaskData data, List<String> propertyList) {
        fillTaskData((AnswerVoiceCallTaskData) data, propertyList);
    }

    @Override
    public void fill(TaskResult data, List<String> propertyList) {
        fillResultData((AnswerVoiceCallTaskResult) data, propertyList);
    }

    @Override
    public void collectToMap(TaskData data, Map<String, String> propertyMap) {

    }

    @Override
    public void collectToMap(TaskResult data, Map<String, String> propertyMap) {
        collectResultData((AnswerVoiceCallTaskResult) data, propertyMap);
    }

    @Override
    public void collectToList(TaskData data, List<String> propertyList) {
        collectTaskData((AnswerVoiceCallTaskData) data, propertyList);
    }

    @Override
    public void collectToList(TaskResult data, List<String> propertyList) {
        collectResultData((AnswerVoiceCallTaskResult) data, propertyList);
    }

    private static void fillTaskData(AnswerVoiceCallTaskData data, List<String> propertyList) {
        data.setCallType(propertyList.get(7));
        data.setRingingTime(toInt(propertyList.get(8)));
        data.setCallDurationInSeconds(toInt(propertyList.get(9)));
        data.setAudioRecordingFileName(propertyList.get(13));
        data.setAudioRecordingTime(toInteger(propertyList.get(16)));
        data.setAudioType(toInteger(propertyList.get(17)));
    }

    private void fillResultData(AnswerVoiceCallTaskResult data, Map<String, String> propertyMap) {
        data.setCalledNumber(propertyMap.get(AnswerVoiceCallTaskResult.CALLED_NUMBER));
        data.setTimeWaitingForRinging(toInt(propertyMap.get(AnswerVoiceCallTaskResult.TIME_WAITING_FOR_RINGING)));

    }

    private void collectResultData(AnswerVoiceCallTaskResult data, Map<String, String> propertyMap) {
        propertyMap.put(AnswerVoiceCallTaskResult.CALLED_NUMBER, data.getCalledNumber());
        propertyMap.put(AnswerVoiceCallTaskResult.TIME_WAITING_FOR_RINGING, toString(data.getTimeWaitingForRinging()));
    }

    private static void fillResultData(AnswerVoiceCallTaskResult data, List<String> propertyList) {
        data.setCalledNumber(propertyList.get(9));
        data.setFixedValue(toInt(propertyList.get(10)));
        data.setAudioRecordingFileName(propertyList.get(15));
        data.setTimeWaitingForRinging(toInt(propertyList.get(17)));
    }

    private void collectTaskData(AnswerVoiceCallTaskData data, List<String> propertyList) {
        for (int i = 7; i < 21; i++) {
            propertyList.add("");
        }
        propertyList.set(7, data.getCallType());
        propertyList.set(8, toString(data.getRingingTime()));
        propertyList.set(9, toString(data.getCallDurationInSeconds()));
        propertyList.set(13, data.getAudioRecordingFileName());
        propertyList.set(16, toString(data.getAudioRecordingTime()));
        propertyList.set(17, toString(data.getAudioType()));
    }

    private void collectResultData(AnswerVoiceCallTaskResult data, List<String> propertyList) {
        for (int i = 9; i < 18; i++) {
            propertyList.add("");
        }
        propertyList.set(9, data.getCalledNumber());
        propertyList.set(10, toString(data.getFixedValue()));
        propertyList.set(15, data.getAudioRecordingFileName());
        propertyList.set(17, toString(data.getTimeWaitingForRinging()));
    }
}

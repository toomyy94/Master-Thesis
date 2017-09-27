package pt.ptinovacao.arqospocket.core.serialization.composers;

import java.util.List;
import java.util.Map;

import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.MakeVoiceCallTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.SendSmsTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.MakeVoiceCallTaskData;

/**
 * Make voice call task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
class MakeVoiceCallTaskObjectComposer extends ObjectComposer {

    @Override
    public void fill(TaskData data, Map<String, String> propertyMap) {
        fillTaskData((MakeVoiceCallTaskData) data, propertyMap);
    }

    @Override
    public void fill(TaskResult data, Map<String, String> propertyMap) {
        fillResultData((MakeVoiceCallTaskResult) data, propertyMap);
    }

    @Override
    public void fill(TaskData data, List<String> propertyList) {
        fillTaskData((MakeVoiceCallTaskData) data, propertyList);
    }

    @Override
    public void fill(TaskResult data, List<String> propertyList) {
        fillResultData((MakeVoiceCallTaskResult) data, propertyList);
    }

    @Override
    public void collectToMap(TaskData data, Map<String, String> propertyMap) {
        collectTaskData((MakeVoiceCallTaskData) data, propertyMap);
    }

    @Override
    public void collectToMap(TaskResult data, Map<String, String> propertyMap) {
        collectResultData((MakeVoiceCallTaskResult) data, propertyMap);
    }

    @Override
    public void collectToList(TaskData data, List<String> propertyList) {
        collectTaskData((MakeVoiceCallTaskData) data, propertyList);
    }

    @Override
    public void collectToList(TaskResult data, List<String> propertyList) {
        collectResultData((MakeVoiceCallTaskResult) data, propertyList);
    }

    private static void fillTaskData(MakeVoiceCallTaskData data, List<String> propertyList) {
        data.setCallType(propertyList.get(7));
        data.setDestinationNumber(propertyList.get(8));
        data.setEnable3PartyConference(toInt(propertyList.get(12)));
        data.setAudioRecordingTime(toInteger(propertyList.get(15)));
        data.setCallDurationInSeconds(toInt(propertyList.get(23)));
        data.setEstablishedCallDetection(toInt(propertyList.get(24)));
        data.setExpectCallToBeRejected(toInt(propertyList.get(36)));
        data.setAudioType(toInteger(propertyList.get(39)));
        data.setAudioRecordingFileName(propertyList.get(45));
    }

    private void fillTaskData(MakeVoiceCallTaskData data, Map<String, String> propertyMap) {
        data.setDestinationNumber(propertyMap.get(MakeVoiceCallTaskData.DESTINATION_NUMBER));
    }

    private void fillResultData(MakeVoiceCallTaskResult data, List<String> propertyList) {
        data.setFixedValue(toInt(propertyList.get(9)));
        data.setDestinationNumber(propertyList.get(10));
        data.setCallSetupTime(toLong(propertyList.get(11)));
        data.setAudioRecordFileName(propertyList.get(18));
    }

    private void fillResultData(MakeVoiceCallTaskResult data, Map<String, String> propertyMap) {
        data.setDestinationNumber(propertyMap.get(MakeVoiceCallTaskResult.DESTINATION_NUMBER));
    }

    private void collectTaskData(MakeVoiceCallTaskData data, Map<String, String> propertyMap) {
        propertyMap.put(MakeVoiceCallTaskData.DESTINATION_NUMBER, data.getDestinationNumber());
    }

    private void collectResultData(MakeVoiceCallTaskResult data, Map<String, String> propertyMap) {
        propertyMap.put(SendSmsTaskResult.DESTINATION_NUMBER, data.getDestinationNumber());
    }

    private void collectTaskData(MakeVoiceCallTaskData data, List<String> propertyList) {
        for (int i = 7; i < 48; i++) {
            propertyList.add("");
        }
        propertyList.set(7, data.getCallType());
        propertyList.set(8, data.getDestinationNumber());
        propertyList.set(12, toString(data.getEnable3PartyConference()));
        propertyList.set(15, toString(data.getAudioRecordingTime()));
        propertyList.set(23, toString(data.getCallDurationInSeconds()));
        propertyList.set(24, toString(data.getEstablishedCallDetection()));
        propertyList.set(36, toString(data.getExpectCallToBeRejected()));
        propertyList.set(39, toString(data.getAudioType()));
        propertyList.set(45, data.getAudioRecordingFileName());
    }

    private void collectResultData(MakeVoiceCallTaskResult data, List<String> propertyList) {
        for (int i = 9; i < 38; i++) {
            propertyList.add("");
        }
        propertyList.set(9, toString(data.getFixedValue()));
        propertyList.set(10, data.getDestinationNumber());
        propertyList.set(11, toString(data.getCallSetupTime()));
        propertyList.set(18, data.getAudioRecordFileName());
    }
}

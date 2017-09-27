package pt.ptinovacao.arqospocket.core.serialization.composers;

import java.util.List;
import java.util.Map;

import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.RecordAudioTaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.RecordAudioTaskData;

/**
 * Record audio task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
class RecordAudioTaskObjectComposer extends ObjectComposer {

    @Override
    public void fill(TaskData data, Map<String, String> propertyMap) {
        fillTaskData((RecordAudioTaskData) data, propertyMap);
    }

    @Override
    public void fill(TaskResult data, Map<String, String> propertyMap) {
        fillResultData((RecordAudioTaskResult) data, propertyMap);
    }

    @Override
    public void fill(TaskData data, List<String> propertyList) {
        fillTaskData((RecordAudioTaskData) data, propertyList);
    }

    @Override
    public void fill(TaskResult data, List<String> propertyList) {
        fillResultData((RecordAudioTaskResult) data, propertyList);
    }

    @Override
    public void collectToMap(TaskData data, Map<String, String> propertyMap) {
        collectTaskData((RecordAudioTaskData) data, propertyMap);
    }

    @Override
    public void collectToMap(TaskResult data, Map<String, String> propertyMap) {
        collectResultData((RecordAudioTaskResult) data, propertyMap);
    }

    @Override
    public void collectToList(TaskData data, List<String> propertyList) {
        collectTaskData((RecordAudioTaskData) data, propertyList);
    }

    private void fillTaskData(RecordAudioTaskData data, Map<String, String> propertyMap) {
        data.setAudioRecordingFileName(propertyMap.get(RecordAudioTaskData.AUDIO_RECORDING_NAME));
        data.setAudioRecordingTime(toInt(propertyMap.get(RecordAudioTaskData.AUDIO_RECORDING_TIME)));
    }

    private void fillResultData(RecordAudioTaskResult data, Map<String, String> propertyMap) {
        data.setAudioRecordingFileName(propertyMap.get(RecordAudioTaskResult.AUDIO_RECORD_FILE_NAME));
    }

    private void collectTaskData(RecordAudioTaskData data, Map<String, String> propertyMap) {
        propertyMap.put(RecordAudioTaskData.AUDIO_RECORDING_TIME, toString(data.getAudioRecordingTime()));
        propertyMap.put(RecordAudioTaskData.AUDIO_RECORDING_NAME, data.getAudioRecordingFileName());
    }

    private void collectResultData(RecordAudioTaskResult data, Map<String, String> propertyMap) {
        propertyMap.put(RecordAudioTaskResult.AUDIO_RECORD_FILE_NAME, data.getAudioRecordingFileName());
    }

    @Override
    public void collectToList(TaskResult data, List<String> propertyList) {
        collectResultData((RecordAudioTaskResult) data, propertyList);
    }

    private void fillTaskData(RecordAudioTaskData data, List<String> propertyList) {
        data.setAudioRecordingFileName(propertyList.get(7));
        data.setAudioRecordingTime(toInt(propertyList.get(8)));
        data.setAudioType(propertyList.get(9));
    }

    private void fillResultData(RecordAudioTaskResult data, List<String> propertyList) {
        data.setAudioRecordingFileName(propertyList.get(9));
    }

    private void collectTaskData(RecordAudioTaskData data, List<String> propertyList) {
        for (int i = 7; i < 10; i++) {
            propertyList.add("");
        }
        propertyList.set(7, data.getAudioRecordingFileName());
        propertyList.set(8, toString(data.getAudioRecordingTime()));
        propertyList.set(9, data.getAudioType());
    }

    private void collectResultData(RecordAudioTaskResult data, List<String> propertyList) {
        for (int i = 9; i < 10; i++) {
            propertyList.add("");
        }
        propertyList.set(9, data.getAudioRecordingFileName());

    }
}

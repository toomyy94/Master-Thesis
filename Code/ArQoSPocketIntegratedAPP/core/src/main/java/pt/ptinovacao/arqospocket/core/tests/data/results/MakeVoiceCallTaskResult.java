package pt.ptinovacao.arqospocket.core.tests.data.results;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;

/**
 * Make voice call task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class MakeVoiceCallTaskResult extends TaskResult {

    public static final String DESTINATION_NUMBER = "destination_number";

    private static final String CALL_SETUP_TIME = "call_setup_time";

    private static final String AUDIO_RECORD_TIME = "audio_record_time";

    private static final String FIXED_VALUE = "fixed_value";

    @SerializedName(DESTINATION_NUMBER)
    private String destinationNumber;

    @SerializedName(CALL_SETUP_TIME)
    private Long callSetupTime;

    @SerializedName(AUDIO_RECORD_TIME)
    private String audioRecordFileName;

    @SerializedName(FIXED_VALUE)
    private int fixedValue;

    public MakeVoiceCallTaskResult(ResolverInfo resolverInfo, String name) {
        super(resolverInfo, name);
    }

    public String getDestinationNumber() {
        return destinationNumber;
    }

    public void setDestinationNumber(String destinationNumber) {
        this.destinationNumber = destinationNumber;
    }

    public Long getCallSetupTime() {
        return callSetupTime;
    }

    public void setCallSetupTime(Long callSetupTime) {
        this.callSetupTime = callSetupTime;
    }

    public String getAudioRecordFileName() {
        return audioRecordFileName;
    }

    public void setAudioRecordFileName(String audioRecordFileName) {
        this.audioRecordFileName = audioRecordFileName;
    }

    public int getFixedValue() {
        return fixedValue;
    }

    public void setFixedValue(int fixedValue) {
        this.fixedValue = fixedValue;
    }
}

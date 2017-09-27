package pt.ptinovacao.arqospocket.core.tests.data.tasks;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;

/**
 * Make voice call task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class MakeVoiceCallTaskData extends TaskData {

    private static final String CALL_TYPE = "call_type";

    public static final String DESTINATION_NUMBER = "destination_number";

    private static final String ENABLE_3PARTY_CONFERENCE = "enable_3party_conference";

    private static final String CALL_DURATION_SECONDS = "call_duration_seconds";

    private static final String AUDIO_RECORDING_FILENAME = "audio_recording_filename";

    private static final String AUDIO_RECORDING_TIME = "audio_recording_time";

    private static final String ESTABLISHED_CALL_DETECTION = "established_call_detection";

    private static final String EXPECT_CALL_TO_BE_REJECTED = "expect_call_to_be_rejected";

    private static final String AUDIO_TYPE = "audio_type";

    @SerializedName(CALL_TYPE)
    private String callType;

    @SerializedName(DESTINATION_NUMBER)
    private String destinationNumber;

    @SerializedName(ENABLE_3PARTY_CONFERENCE)
    private int enable3PartyConference;

    @SerializedName(AUDIO_RECORDING_TIME)
    private Integer audioRecordingTime;

    @SerializedName(AUDIO_RECORDING_FILENAME)
    private String audioRecordingFileName;

    @SerializedName(CALL_DURATION_SECONDS)
    private int callDurationInSeconds;

    @SerializedName(ESTABLISHED_CALL_DETECTION)
    private int establishedCallDetection;

    @SerializedName(EXPECT_CALL_TO_BE_REJECTED)
    private int expectCallToBeRejected;

    @SerializedName(AUDIO_TYPE)
    private Integer audioType;

    public MakeVoiceCallTaskData(ResolverInfo resolverInfo, String name) {
        super(resolverInfo, name);
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getDestinationNumber() {
        return destinationNumber;
    }

    public void setDestinationNumber(String destinationNumber) {
        this.destinationNumber = destinationNumber;
    }

    public int getEnable3PartyConference() {
        return enable3PartyConference;
    }

    public void setEnable3PartyConference(int enable3PartyConference) {
        this.enable3PartyConference = enable3PartyConference;
    }

    public Integer getAudioRecordingTime() {
        return audioRecordingTime;
    }

    public void setAudioRecordingTime(Integer audioRecordingTime) {
        this.audioRecordingTime = audioRecordingTime;
    }

    public String getAudioRecordingFileName() {
        return audioRecordingFileName;
    }

    public void setAudioRecordingFileName(String audioRecordingFileName) {
        this.audioRecordingFileName = audioRecordingFileName;
    }

    public int getCallDurationInSeconds() {
        return callDurationInSeconds;
    }

    public void setCallDurationInSeconds(int callDurationInSeconds) {
        this.callDurationInSeconds = callDurationInSeconds;
    }

    public int getEstablishedCallDetection() {
        return establishedCallDetection;
    }

    public void setEstablishedCallDetection(int establishedCallDetection) {
        this.establishedCallDetection = establishedCallDetection;
    }

    public int getExpectCallToBeRejected() {
        return expectCallToBeRejected;
    }

    public void setExpectCallToBeRejected(int expectCallToBeRejected) {
        this.expectCallToBeRejected = expectCallToBeRejected;
    }

    public Integer getAudioType() {
        return audioType;
    }

    public void setAudioType(Integer audioType) {
        this.audioType = audioType;
    }
}

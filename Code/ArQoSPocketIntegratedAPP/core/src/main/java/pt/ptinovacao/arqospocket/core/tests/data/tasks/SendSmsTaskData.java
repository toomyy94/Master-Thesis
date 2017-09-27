package pt.ptinovacao.arqospocket.core.tests.data.tasks;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;

/**
 * Send SMS task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class SendSmsTaskData extends TaskData {

    public static final String DESTINATION_NUMBER = "destination_number";

    public static final String TEXT_MESSAGE = "text_msg";

    public static final String TRAILER_TEXT = "trailer_text";

    public static final String TRAILER_METADATA = "trailer_metadata";

    public static final String SMS_CENTER_NUMBER = "sms_center_number";

    public static final String REPLY_SMS = "reply_sms";

    public static final String ENCODING = "encoding";

    @SerializedName(DESTINATION_NUMBER)
    private String destinationNumber;

    @SerializedName(TEXT_MESSAGE)
    private String textMessage;

    @SerializedName(TRAILER_TEXT)
    private String trailerText;

    @SerializedName(TRAILER_METADATA)
    private Integer trailerMetadata;

    @SerializedName(SMS_CENTER_NUMBER)
    private String smsCenterNumber;

    @SerializedName(REPLY_SMS)
    private Integer replySms;

    @SerializedName(ENCODING)
    private String encoding;

    public SendSmsTaskData(ResolverInfo resolverInfo, String name) {
        super(resolverInfo, name);
    }

    public String getDestinationNumber() {
        return destinationNumber;
    }

    public void setDestinationNumber(String destinationNumber) {
        this.destinationNumber = destinationNumber;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getTrailerText() {
        return trailerText;
    }

    public void setTrailerText(String trailerText) {
        this.trailerText = trailerText;
    }

    public Integer getTrailerMetadata() {
        return trailerMetadata;
    }

    public void setTrailerMetadata(Integer trailerMetadata) {
        this.trailerMetadata = trailerMetadata;
    }

    public String getSmsCenterNumber() {
        return smsCenterNumber;
    }

    public void setSmsCenterNumber(String smsCenterNumber) {
        this.smsCenterNumber = smsCenterNumber;
    }

    public Integer getReplySms() {
        return replySms;
    }

    public void setReplySms(Integer replySms) {
        this.replySms = replySms;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}

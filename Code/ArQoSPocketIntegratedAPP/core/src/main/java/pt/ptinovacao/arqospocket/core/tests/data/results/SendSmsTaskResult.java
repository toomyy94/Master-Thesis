package pt.ptinovacao.arqospocket.core.tests.data.results;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;

/**
 * Send SMS task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class SendSmsTaskResult extends TaskResult {

    public static final String SENDING_TIME = "sending_time";

    public static final String DESTINATION_NUMBER = "destination_number";

    public static final String MESSAGE_TEXT = "message_text";

    @SerializedName(SENDING_TIME)
    private String sendingTime;

    @SerializedName(DESTINATION_NUMBER)
    private String destinationNumber;

    @SerializedName(MESSAGE_TEXT)
    private String messageText;

    public SendSmsTaskResult(ResolverInfo resolverInfo, String name) {
        super(resolverInfo, name);
    }

    public String getSendingTime() {
        return sendingTime;
    }

    public void setSendingTime(String sendingTime) {
        this.sendingTime = sendingTime;
    }

    public String getDestinationNumber() {
        return destinationNumber;
    }

    public void setDestinationNumber(String destinationNumber) {
        this.destinationNumber = destinationNumber;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}

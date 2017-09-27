package pt.ptinovacao.arqospocket.core.tests.data.results;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;

/**
 * Receive SMS task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class ReceiveSmsTaskResult extends TaskResult {

    public static final String MESSAGE_TEXT = "sms_text";

    public static final String SOURCE_NUMBER = "source_number";

    public static final String WAITING_TIME = "time_wait";

    public static final String MESSAGE_DELIVERY_TIME = "end_time_wait";

    public static final String ENCODING = "encoding";

    @SerializedName(MESSAGE_TEXT)
    private String messageText;

    @SerializedName(SOURCE_NUMBER)
    private String sourceNumber;

    @SerializedName(WAITING_TIME)
    private String waitingTime;

    @SerializedName(MESSAGE_DELIVERY_TIME)
    private String messageDeliveryTime;

    @SerializedName(ENCODING)
    private Integer encoding;

    public ReceiveSmsTaskResult(ResolverInfo resolverInfo, String name) {
        super(resolverInfo, name);
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSourceNumber() {
        return sourceNumber;
    }

    public void setSourceNumber(String sourceNumber) {
        this.sourceNumber = sourceNumber;
    }

    public String getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(String waitingTime) {
        this.waitingTime = waitingTime;
    }

    public String getMessageDeliveryTime() {
        return messageDeliveryTime;
    }

    public void setMessageDeliveryTime(String messageDeliveryTime) {
        this.messageDeliveryTime = messageDeliveryTime;
    }

    public Integer getEncoding() {
        return encoding;
    }

    public void setEncoding(Integer encoding) {
        this.encoding = encoding;
    }
}

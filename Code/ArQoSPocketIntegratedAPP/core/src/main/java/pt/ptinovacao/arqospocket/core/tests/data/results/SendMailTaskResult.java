package pt.ptinovacao.arqospocket.core.tests.data.results;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;

/**
 * Send mail task data.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class SendMailTaskResult extends TaskResult {

    public static final String MESSAGE_CONTENT = "mail_data";

    public static final String MESSAGE_SIZE = "data_size";

    public static final String DESTINATION_ADDRESS = "destination_address";

    @SerializedName(MESSAGE_CONTENT)
    private String messageContent;

    @SerializedName(MESSAGE_SIZE)
    private String messageSize;

    @SerializedName(DESTINATION_ADDRESS)
    private String destinationAddress;

    public SendMailTaskResult(ResolverInfo resolverInfo) {
        super(resolverInfo);
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageSize() {
        return messageSize;
    }

    public void setMessageSize(String messageSize) {
        this.messageSize = messageSize;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }
}

package pt.ptinovacao.arqospocket.core.tests.data.tasks;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;

/**
 * Send MMS task data.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class SendMmsTaskData extends TaskData {

    public static final String DESTINATION_NUMBER = "destination_number";

    public static final String TEXT_MESSAGE = "text_msg";

    public static final String IMAGE = "image";

    public static final String RESPONSE_MESSAGE = "response_mms";

    public static final String ENCODING = "encoding";

    @SerializedName(DESTINATION_NUMBER)
    private String destinationNumber;

    @SerializedName(TEXT_MESSAGE)
    private String textMessage;

    @SerializedName(IMAGE)
    private String image;

    @SerializedName(RESPONSE_MESSAGE)
    private String responseMessage;

    @SerializedName(ENCODING)
    private String encoding;

    public SendMmsTaskData(ResolverInfo resolverInfo) {
        super(resolverInfo);
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}

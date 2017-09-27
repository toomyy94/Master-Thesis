package pt.ptinovacao.arqospocket.core.tests.data.tasks;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;

/**
 * Send mail task data.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class SendMailTaskData extends TaskData {

    public static final String DESTINATION_ADDRESS = "destination_number";

    public static final String CARBON_COPY = "cc";

    public static final String BLACK_CARBON_COPY = "bcc";

    public static final String SUBJECT = "subject";

    public static final String BODY = "mail_text";

    public static final String ATTACHED_FILE = "attach_file";

    @SerializedName(DESTINATION_ADDRESS)
    private String destinationAddress;

    @SerializedName(CARBON_COPY)
    private String carbonCopy;

    @SerializedName(BLACK_CARBON_COPY)
    private String blackCarbonCopy;

    @SerializedName(SUBJECT)
    private String subject;

    @SerializedName(BODY)
    private String body;

    @SerializedName(ATTACHED_FILE)
    private String attachedFile;

    public SendMailTaskData(ResolverInfo resolverInfo) {
        super(resolverInfo);
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getCarbonCopy() {
        return carbonCopy;
    }

    public void setCarbonCopy(String carbonCopy) {
        this.carbonCopy = carbonCopy;
    }

    public String getBlackCarbonCopy() {
        return blackCarbonCopy;
    }

    public void setBlackCarbonCopy(String blackCarbonCopy) {
        this.blackCarbonCopy = blackCarbonCopy;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAttachedFile() {
        return attachedFile;
    }

    public void setAttachedFile(String attachedFile) {
        this.attachedFile = attachedFile;
    }
}

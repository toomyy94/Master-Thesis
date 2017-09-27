package pt.ptinovacao.arqospocket.core.tests.data.tasks;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;

/**
 * Receive SMS task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class ReceiveSmsTaskData extends TaskData {

    public static final String EXPECTED_TRAILER = "destination_number";

    public static final String MESSAGE_TO_VERIFY = "text_msg";

    @SerializedName(EXPECTED_TRAILER)
    private String expectedTrailer;

    @SerializedName(MESSAGE_TO_VERIFY)
    private String messageToVerify;

    public ReceiveSmsTaskData(ResolverInfo resolverInfo, String name) {
        super(resolverInfo, name);
    }

    public String getExpectedTrailer() {
        return expectedTrailer;
    }

    public void setExpectedTrailer(String expectedTrailer) {
        this.expectedTrailer = expectedTrailer;
    }

    public String getMessageToVerify() {
        return messageToVerify;
    }

    public void setMessageToVerify(String messageToVerify) {
        this.messageToVerify = messageToVerify;
    }
}

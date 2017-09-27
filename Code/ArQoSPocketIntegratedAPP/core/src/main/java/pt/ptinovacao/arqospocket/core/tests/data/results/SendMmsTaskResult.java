package pt.ptinovacao.arqospocket.core.tests.data.results;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;

/**
 * Send MMS task data.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class SendMmsTaskResult extends TaskResult {

    public static final String SENT_DATA = "sent_data";

    public static final String DATA_SIZE = "data_size";

    @SerializedName(SENT_DATA)
    private String sentData;

    @SerializedName(DATA_SIZE)
    private String dataSize;

    public SendMmsTaskResult(ResolverInfo resolverInfo) {
        super(resolverInfo);
    }

    public String getSentData() {
        return sentData;
    }

    public void setSentData(String sentData) {
        this.sentData = sentData;
    }

    public String getDataSize() {
        return dataSize;
    }

    public void setDataSize(String dataSize) {
        this.dataSize = dataSize;
    }
}

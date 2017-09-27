package pt.ptinovacao.arqospocket.core.tests.data.results;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;

/**
 * HTTP upload task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class HttpUploadTaskResult extends TaskResult {

    public static final String SENT_DATA = "sent_data";

    public static final String SENT_DATA_SIZE = "sent_data_size";

    public static final String UPLOAD_TIME_IN_SECONDS = "upload_time_in_sec";

    public static final String ACCESS_TIME_IN_SECONDS = "access_time_in_sec";

    public static final String THROUGHPUT = "throughput";

    @SerializedName(SENT_DATA)
    private String sentData;

    @SerializedName(SENT_DATA_SIZE)
    private String sentDataSize;

    @SerializedName(UPLOAD_TIME_IN_SECONDS)
    private String uploadTimeInSeconds;

    @SerializedName(ACCESS_TIME_IN_SECONDS)
    private String accessTimeInSeconds;

    @SerializedName(THROUGHPUT)
    private String throughput;

    public HttpUploadTaskResult(ResolverInfo resolverInfo) {
        super(resolverInfo);
    }

    public String getSentData() {
        return sentData;
    }

    public void setSentData(String sentData) {
        this.sentData = sentData;
    }

    public String getSentDataSize() {
        return sentDataSize;
    }

    public void setSentDataSize(String sentDataSize) {
        this.sentDataSize = sentDataSize;
    }

    public String getUploadTimeInSeconds() {
        return uploadTimeInSeconds;
    }

    public void setUploadTimeInSeconds(String uploadTimeInSeconds) {
        this.uploadTimeInSeconds = uploadTimeInSeconds;
    }

    public String getAccessTimeInSeconds() {
        return accessTimeInSeconds;
    }

    public void setAccessTimeInSeconds(String accessTimeInSeconds) {
        this.accessTimeInSeconds = accessTimeInSeconds;
    }

    public String getThroughput() {
        return throughput;
    }

    public void setThroughput(String throughput) {
        this.throughput = throughput;
    }
}


package pt.ptinovacao.arqospocket.core.tests.data.results;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;

/**
 * HTTP download task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class HttpDownloadTaskResult extends TaskResult {

    public static final String RECEIVED_DATA = "received_data";

    public static final String RECEIVED_DATA_SIZE = "received_data_size";

    public static final String DOWNLOAD_TIME_IN_SECONDS = "download_time_in_sec";

    public static final String ACCESS_TIME_IN_SECONDS = "access_time_in_sec";

    public static final String THROUGHPUT = "throughput";

    @SerializedName(RECEIVED_DATA)
    private String receivedData;

    @SerializedName(RECEIVED_DATA_SIZE)
    private String receivedDataSize;

    @SerializedName(DOWNLOAD_TIME_IN_SECONDS)
    private String downloadTimeInSeconds;

    @SerializedName(ACCESS_TIME_IN_SECONDS)
    private String accessTimeInSeconds;

    @SerializedName(THROUGHPUT)
    private String throughput;

    public HttpDownloadTaskResult(ResolverInfo resolverInfo) {
        super(resolverInfo);
    }

    public String getReceivedData() {
        return receivedData;
    }

    public void setReceivedData(String receivedData) {
        this.receivedData = receivedData;
    }

    public String getReceivedDataSize() {
        return receivedDataSize;
    }

    public void setReceivedDataSize(String receivedDataSize) {
        this.receivedDataSize = receivedDataSize;
    }

    public String getDownloadTimeInSeconds() {
        return downloadTimeInSeconds;
    }

    public void setDownloadTimeInSeconds(String downloadTimeInSeconds) {
        this.downloadTimeInSeconds = downloadTimeInSeconds;
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


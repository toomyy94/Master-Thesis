package pt.ptinovacao.arqospocket.core.http;

/**
 * Wrapper for the download and upload responses.
 * <p>
 * Created by Emílio Simões on 26-04-2017.
 */
public class TestHttpResponse {

    private final TestHttpResponseStatus status;

    private long accessTime;

    private long totalTime;

    private int totalBytes;

    private String receivedData;

    private long throughput;

    private String url;

    TestHttpResponse(TestHttpResponseStatus status) {
        this.status = status;
    }

    public TestHttpResponseStatus getStatus() {
        return status;
    }

    public long getAccessTime() {
        return accessTime;
    }

    void setAccessTime(long accessTime) {
        this.accessTime = accessTime;
    }

    public long getTotalTime() {
        return totalTime;
    }

    void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public int getTotalBytes() {
        return totalBytes;
    }

    void setTotalBytes(int totalBytes) {
        this.totalBytes = totalBytes;
    }

    public String getReceivedData() {
        return receivedData;
    }

    void setReceivedData(String receivedData) {
        this.receivedData = receivedData;
    }

    public long getThroughput() {
        return throughput;
    }

    void setThroughput(long throughput) {
        this.throughput = throughput;
    }

    public String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }
}

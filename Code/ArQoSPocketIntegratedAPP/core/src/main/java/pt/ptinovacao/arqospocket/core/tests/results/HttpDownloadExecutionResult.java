package pt.ptinovacao.arqospocket.core.tests.results;

import pt.ptinovacao.arqospocket.core.http.TestHttpResponse;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.HttpDownloadTaskResult;

/**
 * HTTP download execution result.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class HttpDownloadExecutionResult extends BaseTaskExecutionResult {

    private final HttpDownloadTaskResult taskResult;

    public HttpDownloadExecutionResult(TaskResult result) {
        super(result);
        taskResult = (HttpDownloadTaskResult) result;
    }

    public void updateDownloadData(TestHttpResponse response, String status) {
        taskResult.setThroughput(String.valueOf(response.getThroughput()));
        taskResult.setAccessTimeInSeconds(String.valueOf(response.getAccessTime()));
        taskResult.setDownloadTimeInSeconds(String.valueOf(response.getTotalTime()));
        taskResult.setReceivedDataSize(String.valueOf(response.getTotalBytes()));
        taskResult.setReceivedData(encode(response.getReceivedData()));
        taskResult.setStatus(status);
    }
}

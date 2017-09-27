package pt.ptinovacao.arqospocket.core.tests.results;

import pt.ptinovacao.arqospocket.core.http.TestHttpResponse;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.HttpUploadTaskResult;

/**
 * HTTP download execution result.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class HttpUploadExecutionResult extends BaseTaskExecutionResult {

    private final HttpUploadTaskResult taskResult;

    public HttpUploadExecutionResult(TaskResult result) {
        super(result);
        taskResult = (HttpUploadTaskResult) result;
    }

    public void updateUploadData(TestHttpResponse response, String status) {
        taskResult.setThroughput(String.valueOf(response.getThroughput()));
        taskResult.setAccessTimeInSeconds(String.valueOf(response.getAccessTime()));
        taskResult.setUploadTimeInSeconds(String.valueOf(response.getTotalTime()));
        taskResult.setSentDataSize(String.valueOf(response.getTotalBytes()));
        taskResult.setSentData(encode(response.getReceivedData()));
        taskResult.setStatus(status);
    }

}

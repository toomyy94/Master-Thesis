package pt.ptinovacao.arqospocket.core.tests;

import com.google.common.base.Strings;
import com.google.common.io.BaseEncoding;

import java.util.Calendar;
import java.util.Date;

import pt.ptinovacao.arqospocket.core.location.LocationInfo;
import pt.ptinovacao.arqospocket.core.network.ConnectionTechnology;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;

/**
 * The result from a task execution. The result can be immediately returned by the
 * {@link BaseExecutableTask#execute(ExecutableTest, int)} method or delivered asynchronously later. This behaviour is
 * for the test to define.
 * <p>
 * Created by Emílio Simões on 06-04-2017.
 */
public abstract class BaseTaskExecutionResult {

    private final TaskResult result;

    private int executionId;

    private String nameFileAttachments;

    private ConnectionTechnology connectionTechnology;

    private LocationInfo locationInfo;

    public LocationInfo getLocationInfo() {
        return locationInfo;
    }

    protected BaseTaskExecutionResult(TaskResult result) {
        this.result = result;
    }

    /**
     * Gets the task execution result.
     *
     * @return the task execution result.
     */
    public TaskResult getResult() {
        return result;
    }

    public int getExecutionId() {
        return executionId;
    }

    public void setExecutionId(int executionId) {
        this.executionId = executionId;
    }

    public void updateStartDate(Date startDate) {
        result.setStartDate(startDate);
    }

    void updateLocation(LocationInfo locationInfo) {
        this.locationInfo = locationInfo;
        result.setGpsLocation(locationInfo.format());
    }

    public ConnectionTechnology getConnectionTechnology() {
        return connectionTechnology;
    }

    public void setConnectionTechnology(ConnectionTechnology connectionTechnology) {
        this.connectionTechnology = connectionTechnology;
    }

    public void updateStatus(String status) {
        result.setStatus(status);
    }

    void updateTaskData(BaseExecutableTask executableTask) {
        result.setEndDate(Calendar.getInstance().getTime());
        result.setTaskId(executableTask.getData().getTaskId());
        result.setTaskName(executableTask.getData().getTaskName());
        result.setTaskNumber(executableTask.getData().getTaskNumber());
        result.setMacroId(executableTask.getData().getMacroId());
        result.setPipedString(executableTask.getData().isPipedString());
        result.setIccid(executableTask.getData().getIccid());
    }

    void updateCellId(String cellId) {
        result.setCellId(cellId);
    }

    protected String encode(String data) {
        String value = Strings.nullToEmpty(data);
        if (value.length() == 0) {
            return null;
        }
        byte[] bytes = value.getBytes();
        return BaseEncoding.base64().encode(bytes);
    }

    public String getNameFileAttachments() {
        return nameFileAttachments;
    }

    public void setNameFileAttachments(String nameFile) {
        this.nameFileAttachments = nameFile;
    }
}

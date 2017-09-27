package pt.ptinovacao.arqospocket.core.tests.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.serialization.TaskResultJsonAdapter;
import pt.ptinovacao.arqospocket.core.utils.JsonHelper;

/**
 * Contains a task parsed result.
 * <p>
 * Created by Emílio Simões on 06-04-2017.
 */
@JsonAdapter(TaskResultJsonAdapter.class)
public abstract class TaskResult {

    public static final String TASK_ID = "task_id";

    public static final String TASK_NAME = "task_name";

    public static final String MACRO_ID = "macro_id";

    public static final String TASK_NUMBER = "task_number";

    public static final String ICCID = "iccid";

    public static final String CELL_ID = "cell_id";

    public static final String GPS_LOCATION = "loc_gps";

    public static final String STATUS = "status";

    public static final String START_DATE = "init_date";

    public static final String END_DATE = "end_date";

    @SerializedName(TASK_ID)
    private String taskId;

    @SerializedName(TASK_NAME)
    private String taskName;

    @SerializedName(MACRO_ID)
    private String macroId;

    @SerializedName(TASK_NUMBER)
    private int taskNumber;

    @SerializedName(ICCID)
    private String iccid;

    @SerializedName(CELL_ID)
    private String cellId;

    @SerializedName(GPS_LOCATION)
    private String gpsLocation;

    @SerializedName(STATUS)
    private String status;

    @SerializedName(START_DATE)
    private Date startDate;

    @SerializedName(END_DATE)
    private Date endDate;

    @Expose(serialize = false, deserialize = false)
    private boolean pipedString;

    @Expose(serialize = false, deserialize = false)
    private final ResolverInfo resolverInfo;

    /**
     * Default constructor for the task data.
     *
     * @param resolverInfo internal reference to resolve the task type from the task resolver.
     */
    protected TaskResult(ResolverInfo resolverInfo) {
        this.resolverInfo = resolverInfo;
    }

    protected TaskResult(ResolverInfo resolverInfo, String name) {
        this(resolverInfo);
        taskName = name;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getMacroId() {
        return macroId;
    }

    public void setMacroId(String macroId) {
        this.macroId = macroId;
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getCellId() {
        return cellId;
    }

    public void setCellId(String cellId) {
        this.cellId = cellId;
    }

    public String getGpsLocation() {
        return gpsLocation;
    }

    public void setGpsLocation(String gpsLocation) {
        this.gpsLocation = gpsLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isPipedString() {
        return pipedString;
    }

    public void setPipedString(boolean pipedString) {
        this.pipedString = pipedString;
    }

    public ResolverInfo getResolverInfo() {
        return resolverInfo;
    }

    @Override
    public String toString() {
        return JsonHelper.getGsonInstance().toJson(this);
    }
}

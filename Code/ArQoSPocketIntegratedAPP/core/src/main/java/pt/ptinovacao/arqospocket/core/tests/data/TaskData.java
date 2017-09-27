package pt.ptinovacao.arqospocket.core.tests.data;

import com.google.common.base.Strings;
import com.google.common.primitives.Ints;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.serialization.TaskDataJsonAdapter;
import pt.ptinovacao.arqospocket.core.utils.JsonHelper;

/**
 * Contains the task parsed data.
 * <p>
 * Created by Emílio Simões on 06-04-2017.
 */
@JsonAdapter(TaskDataJsonAdapter.class)
public abstract class TaskData {

    public static final String TASK_ID = "task_id";

    public static final String TASK_NAME = "task_name";

    public static final String MACRO_ID = "macro_id";

    public static final String TASK_NUMBER = "task_number";

    public static final String ICCID = "iccid";

    public static final String EXECUTION_DELAY = "instanteExec";

    public static final String TIMEOUT = "timeout";

    public static final String IMMEDIATE = "immediate";

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

    @SerializedName(EXECUTION_DELAY)
    private String executionDelay;

    @SerializedName(TIMEOUT)
    private String timeout;

    @SerializedName(IMMEDIATE)
    private String immediate;

    @Expose(serialize = false, deserialize = false)
    private boolean pipedString;

    @Expose(serialize = false, deserialize = false)
    private final ResolverInfo resolverInfo;

    /**
     * Default constructor for the task data.
     *
     * @param resolverInfo internal reference to resolve the task type from the task resolver.
     */
    protected TaskData(ResolverInfo resolverInfo) {
        this.resolverInfo = resolverInfo;
    }

    protected TaskData(ResolverInfo resolverInfo, String name) {
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

    public String getExecutionDelay() {
        return executionDelay;
    }

    public void setExecutionDelay(String executionDelay) {
        this.executionDelay = executionDelay;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getImmediate() {
        return immediate;
    }

    public void setImmediate(String immediate) {
        this.immediate = immediate;
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

    public boolean executeImmediately() {
        return Strings.nullToEmpty(immediate).equals("1");
    }

    public int delay() {
        Integer result = Ints.tryParse(executionDelay);
        return result != null ? result : -1;
    }

    @Override
    public String toString() {
        return JsonHelper.getGsonInstance().toJson(this);
    }
}

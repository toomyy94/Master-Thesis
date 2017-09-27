package pt.ptinovacao.arqospocket.core.serialization.entities.results.type73;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by pedro on 26/05/2017.
 */

public class IterationInfo {

    private static final String MODULO = "modulo";

    private static final String TESTNAME = "testname";

    private static final String TESTEID = "testeid";

    private static final String EXECUTION_TYPE = "executionType";

    private static final String DATAINI = "dataini";

    private static final String DATAFIM = "datafim";

    private static final String REALDATAINI = "realdataini";

    private static final String REALDATAFIM = "realdatafim";

    private static final String EXCUTION_WINDOW_START = "excutionWindowStart";

    private static final String EXCUTION_WINDOW_END = "excutionWindowEnd";

    private static final String TASKS = "tasks";

    @SerializedName(MODULO)
    private String modulo;

    @SerializedName(TESTNAME)
    private String testName;

    @SerializedName(TESTEID)
    private String testeid;

    @SerializedName(EXECUTION_TYPE)
    private String executionType;

    @SerializedName(DATAINI)
    private String dataIni;

    @SerializedName(DATAFIM)
    private String dataFim;

    @SerializedName(REALDATAINI)
    private String realDataIni;

    @SerializedName(REALDATAFIM)
    private String realDataFim;

    @SerializedName(EXCUTION_WINDOW_START)
    private String executionWindowsStart;

    @SerializedName(EXCUTION_WINDOW_END)
    private String executionWindowsEnd;

    @SerializedName(TASKS)
    private List<TaskInfo> taskInfo;

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTesteid() {
        return testeid;
    }

    public void setTesteid(String testeid) {
        this.testeid = testeid;
    }

    public String getExecutionType() {
        return executionType;
    }

    public void setExecutionType(String executionType) {
        this.executionType = executionType;
    }

    public String getDataIni() {
        return dataIni;
    }

    public void setDataIni(String dataIni) {
        this.dataIni = dataIni;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public String getRealDataIni() {
        return realDataIni;
    }

    public void setRealDataIni(String realDataIni) {
        this.realDataIni = realDataIni;
    }

    public String getRealDataFim() {
        return realDataFim;
    }

    public void setRealDataFim(String realDataFim) {
        this.realDataFim = realDataFim;
    }

    public String getExecutionWindowsStart() {
        return executionWindowsStart;
    }

    public void setExecutionWindowsStart(String executionWindowsStart) {
        this.executionWindowsStart = executionWindowsStart;
    }

    public String getExecutionWindowsEnd() {
        return executionWindowsEnd;
    }

    public void setExecutionWindowsEnd(String executionWindowsEnd) {
        this.executionWindowsEnd = executionWindowsEnd;
    }

    public List<TaskInfo> getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(List<TaskInfo> taskInfo) {
        this.taskInfo = taskInfo;
    }
}

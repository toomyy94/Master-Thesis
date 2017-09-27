package pt.ptinovacao.arqospocket.core.serialization.entities.results.type73;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pedro on 29/05/2017.
 */

public class TaskInfo {

    private static final String MODULO = "modulo";

    private static final String TESTNAME = "testname";

    private static final String TESTEID = "testeid";

    private static final String MACROID = "macroid";

    private static final String TASKNUMBER = "tasknumber";

    private static final String TASKID = "taskid";

    private static final String STATE = "state";

    private static final String DATAINI = "dataini";

    private static final String DATAFIM = "datafim";

    private static final String REALDATAINI = "realdataini";

    private static final String REALDATAFIM = "realdatafim";

    private static final String EXCUTION_WINDOW_START = "excutionWindowStart";

    private static final String EXCUTION_WINDOW_END = "excutionWindowEnd";

    @SerializedName(MODULO)
    private String modulo;

    @SerializedName(TESTNAME)
    private String testName;

    @SerializedName(TESTEID)
    private String testeid;

    @SerializedName(MACROID)
    private String macroid;

    @SerializedName(TASKNUMBER)
    private String taskNumber;

    @SerializedName(STATE)
    private String state;

    @SerializedName(TASKID)
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

    public String getMacroid() {
        return macroid;
    }

    public void setMacroid(String macroid) {
        this.macroid = macroid;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

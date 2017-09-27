package pt.ptinovacao.arqospocket.core.serialization.entities.results.type71;

import com.google.gson.annotations.SerializedName;

/**
 * Test info.
 * <p>
 * Created by pedro on 28/06/2017.
 */
public class TestInfo {

    private static final String MODULO = "modulo";

    private static final String TESTNAME = "testname";

    private static final String TESTEID = "testeid";

    private static final String DATAINI = "dataini";

    private static final String DATAFIM = "datafim";

    private static final String STARTEVENT = "startevent";

    private static final String STARTPARAM = "startparam";

    private static final String ENDEVENT = "endevent";

    private static final String ENDPARAM = "endparam";

    private static final String RECURSION = "recursion";

    private static final String EXECUTED_ITERATIONS = "executedIterations";

    private static final String BROKEN_ITERATIONS = "brokenIterations";

    private static final String SKIPPED_ITERATIONS = "skippedIterations";

    private static final String ITERATION_MAX_DURATION = "iterationMaxDuration";

    private static final String TASKS_PER_ITERATION = "tasksPerIteration";

    private static final String MACROS_PER_ITERATION = "macrosPerIteration";

    private static final String STATE = "state";

    private static final String INTERNAL_STATE = "internalState";

    @SerializedName(MODULO)
    private Integer modulo;

    @SerializedName(TESTNAME)
    private String testName;

    @SerializedName(TESTEID)
    private String testId;

    @SerializedName(DATAINI)
    private String dataIni;

    @SerializedName(DATAFIM)
    private String dataFim;

    @SerializedName(STARTEVENT)
    private Integer startEvent;

    @SerializedName(STARTPARAM)
    private Object startParam;

    @SerializedName(ENDEVENT)
    private Integer endEvent;

    @SerializedName(ENDPARAM)
    private Object endParam;

    @SerializedName(RECURSION)
    private Object recursion;

    @SerializedName(EXECUTED_ITERATIONS)
    private Integer executedIterations;

    @SerializedName(BROKEN_ITERATIONS)
    private Integer brokenIterations;

    @SerializedName(SKIPPED_ITERATIONS)
    private Integer skippedIterations;

    @SerializedName(ITERATION_MAX_DURATION)
    private Integer iterationMaxDuration;

    @SerializedName(TASKS_PER_ITERATION)
    private Integer tasksPerIteration;

    @SerializedName(MACROS_PER_ITERATION)
    private Integer macroPerIteration;

    @SerializedName(STATE)
    private Integer state;

    @SerializedName(INTERNAL_STATE)
    private Integer internalState;

    public Integer getModulo() {
        return modulo;
    }

    public void setModulo(Integer modulo) {
        this.modulo = modulo;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
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

    public Integer getStartEvent() {
        return startEvent;
    }

    public void setStartEvent(Integer startEvent) {
        this.startEvent = startEvent;
    }

    public Object getStartParam() {
        return startParam;
    }

    public void setStartParam(Object startParam) {
        this.startParam = startParam;
    }

    public Integer getEndEvent() {
        return endEvent;
    }

    public void setEndEvent(Integer endEvent) {
        this.endEvent = endEvent;
    }

    public Object getEndParam() {
        return endParam;
    }

    public void setEndParam(Object endParam) {
        this.endParam = endParam;
    }

    public Object getRecursion() {
        return recursion;
    }

    public void setRecursion(Object recursion) {
        this.recursion = recursion;
    }

    public Integer getExecutedIterations() {
        return executedIterations;
    }

    public void setExecutedIterations(Integer executedIterations) {
        this.executedIterations = executedIterations;
    }

    public Integer getBrokenIterations() {
        return brokenIterations;
    }

    public void setBrokenIterations(Integer brokenIterations) {
        this.brokenIterations = brokenIterations;
    }

    public Integer getSkippedIterations() {
        return skippedIterations;
    }

    public void setSkippedIterations(Integer skippedIterations) {
        this.skippedIterations = skippedIterations;
    }

    public Integer getIterationMaxDuration() {
        return iterationMaxDuration;
    }

    public void setIterationMaxDuration(Integer iterationMaxDuration) {
        this.iterationMaxDuration = iterationMaxDuration;
    }

    public Integer getTasksPerIteration() {
        return tasksPerIteration;
    }

    public void setTasksPerIteration(Integer tasksPerIteration) {
        this.tasksPerIteration = tasksPerIteration;
    }

    public Integer getMacroPerIteration() {
        return macroPerIteration;
    }

    public void setMacroPerIteration(Integer macroPerIteration) {
        this.macroPerIteration = macroPerIteration;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getInternalState() {
        return internalState;
    }

    public void setInternalState(Integer internalState) {
        this.internalState = internalState;
    }
}
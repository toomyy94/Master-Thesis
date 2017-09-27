package pt.ptinovacao.arqospocket.core.tests.data;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import pt.ptinovacao.arqospocket.core.TestParser;
import pt.ptinovacao.arqospocket.core.utils.DateUtils;

/**
 * Contains the test parsed data.
 * <p>
 * Created by Emílio Simões on 06-04-2017.
 */
public class TestData {

    private static final String TESTE_ID = "testeid";

    private static final String START_DATE = "dataini";

    private static final String END_DATE = "datafim";

    private static final String MODULO = "modulo";

    private static final String TEST_NAME = "testname";

    private static final String MACRO_NUMBER = "macronr";

    private static final String TEST_TYPE = "testtype";

    private static final String RECURSION = "recursion";

    private static final String END_EVENT = "endevent";

    private static final String START_PARAMETER = "startparam";

    private static final String END_PARAMETER = "endparam";

    private static final String STATE = "state";

    private static final String PRIORITY = "priority";

    private static final String DATA = "data";

    @SerializedName(TESTE_ID)
    private String testId;

    @SerializedName(START_DATE)
    private Date startDate;

    @SerializedName(END_DATE)
    private Date endDate;

    @SerializedName(MODULO)
    private int modulo;

    @SerializedName(TEST_NAME)
    private String testName;

    @SerializedName(MACRO_NUMBER)
    private int macroNumber;

    @SerializedName(TEST_TYPE)
    private int testType;

    @SerializedName(RECURSION)
    private RecursionData recursion;

    @SerializedName(END_EVENT)
    private int endEvent;

    @SerializedName(START_PARAMETER)
    private ParameterData startParameter;

    @SerializedName(END_PARAMETER)
    private ParameterData endParameter;

    @SerializedName(STATE)
    private int state;

    @SerializedName(PRIORITY)
    private int priority;

    @SerializedName(DATA)
    private TaskData[] tasksData;

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {

        if (endDate == null) {
            endDate = DateUtils.maxDate();
        }

        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getModulo() {
        return modulo;
    }

    public void setModulo(int modulo) {
        this.modulo = modulo;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public int getMacroNumber() {
        return macroNumber;
    }

    public void setMacroNumber(int macroNumber) {
        this.macroNumber = macroNumber;
    }

    public int getTestType() {
        return testType;
    }

    public void setTestType(int testType) {
        this.testType = testType;
    }

    public RecursionData getRecursion() {
        return recursion;
    }

    public void setRecursion(RecursionData recursion) {
        this.recursion = recursion;
    }

    public int getEndEvent() {
        return endEvent;
    }

    public void setEndEvent(int endEvent) {
        this.endEvent = endEvent;
    }

    public ParameterData getStartParameter() {
        return startParameter;
    }

    public void setStartParameter(ParameterData startParameter) {
        this.startParameter = startParameter;
    }

    public ParameterData getEndParameter() {
        return endParameter;
    }

    public void setEndParameter(ParameterData endParameter) {
        this.endParameter = endParameter;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public TaskData[] getTasksData() {
        return tasksData;
    }

    public void setTasksData(TaskData[] tasksData) {
        this.tasksData = tasksData;
    }

    @Override
    public String toString() {
        return new TestParser().stringify(this);
    }

}

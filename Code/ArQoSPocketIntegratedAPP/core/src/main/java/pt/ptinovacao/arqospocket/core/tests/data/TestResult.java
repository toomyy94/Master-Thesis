package pt.ptinovacao.arqospocket.core.tests.data;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import pt.ptinovacao.arqospocket.core.utils.JsonHelper;

/**
 * Contains a test parsed result.
 * <p>
 * Created by Emílio Simões on 06-04-2017.
 */
public class TestResult {

    private static final String START_DATE = "dataini";

    private static final String END_DATE = "datafim";

    private static final String MODULO = "modulo";

    private static final String TEST_ID = "testeid";

    private static final String DATA = "data";

    @SerializedName(START_DATE)
    private Date startDate;

    @SerializedName(END_DATE)
    private Date endDate;

    @SerializedName(MODULO)
    private int modulo;

    @SerializedName(TEST_ID)
    private String testId;

    @SerializedName(DATA)
    private TaskResult[] taskResults;

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

    public int getModulo() {
        return modulo;
    }

    public void setModulo(int modulo) {
        this.modulo = modulo;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public TaskResult[] getTaskResults() {
        return taskResults;
    }

    public void setTaskResults(TaskResult[] taskResults) {
        this.taskResults = taskResults;
    }

    @Override
    public String toString() {
        return JsonHelper.getGsonInstance().toJson(this);
    }
}

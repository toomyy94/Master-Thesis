package pt.ptinovacao.arqospocket.core.notify;

/**
 * Wrapper class to provide information about test execution progress.
 * <p>
 * Created by Emílio Simões on 18-04-2017.
 */
public class TestProgress {

    private long testId;

    private String testDataId;

    public long getTestId() {
        return testId;
    }

    void setTestId(long testId) {
        this.testId = testId;
    }

    public String getTestDataId() {
        return testDataId;
    }

    void setTestDataId(String testDataId) {
        this.testDataId = testDataId;
    }
}

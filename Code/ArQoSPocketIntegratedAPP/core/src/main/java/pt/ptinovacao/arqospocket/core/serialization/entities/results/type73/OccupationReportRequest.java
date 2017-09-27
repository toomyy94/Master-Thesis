package pt.ptinovacao.arqospocket.core.serialization.entities.results.type73;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by pedro on 26/05/2017.
 */

public class OccupationReportRequest {

    private static final String DATAINI = "dataini";

    private static final String DATAFIM = "datafim";

    private static final String TEST_COUNT = "testCount";

    private static final String ITERATION_COUNT = "iterationCount";

    private static final String ITERATIONS = "iterations";

    @SerializedName(DATAINI)
    private String dataini;

    @SerializedName(DATAFIM)
    private String dataFim;

    @SerializedName(TEST_COUNT)
    private Integer testCount;

    @SerializedName(ITERATION_COUNT)
    private Integer iterationCount;

    @SerializedName(ITERATIONS)
    private List<IterationInfo> iteration;

    public String getDataini() {
        return dataini;
    }

    public void setDataini(String dataini) {
        this.dataini = dataini;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public Integer getTestCount() {
        return testCount;
    }

    public void setTestCount(Integer testCount) {
        this.testCount = testCount;
    }

    public Integer getIterationCount() {
        return iterationCount;
    }

    public void setIterationCount(Integer iterationCount) {
        this.iterationCount = iterationCount;
    }

    public List<IterationInfo> getIteration() {
        return iteration;
    }

    public void setIteration(List<IterationInfo> iteration) {
        this.iteration = iteration;
    }
}

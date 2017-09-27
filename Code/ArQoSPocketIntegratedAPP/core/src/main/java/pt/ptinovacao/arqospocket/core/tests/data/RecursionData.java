package pt.ptinovacao.arqospocket.core.tests.data;

import com.google.gson.annotations.SerializedName;

/**
 * Contains the recursion data associated with a test.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class RecursionData {

    @SerializedName("event")
    private int event;

    @SerializedName("param")
    private ParameterData parameters;

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public ParameterData getParameters() {
        return parameters;
    }

    public void setParameters(ParameterData parameters) {
        this.parameters = parameters;
    }
}


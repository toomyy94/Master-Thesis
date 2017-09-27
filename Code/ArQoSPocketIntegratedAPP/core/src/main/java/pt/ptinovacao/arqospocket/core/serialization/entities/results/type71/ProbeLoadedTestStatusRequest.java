package pt.ptinovacao.arqospocket.core.serialization.entities.results.type71;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import pt.ptinovacao.arqospocket.core.serialization.entities.results.proberesponse.BaseProbeResponse;

/**
 * Created by Pedro Simões on 17-04-2017.
 */
public class ProbeLoadedTestStatusRequest extends BaseProbeResponse {

    private static final String DATA = "data";

    @SerializedName(DATA)
    private ArrayList<TestInfo> data;

    public ArrayList<TestInfo> getData() {
        return data;
    }

    public void setData(ArrayList<TestInfo> data) {
        this.data = data;
    }
}





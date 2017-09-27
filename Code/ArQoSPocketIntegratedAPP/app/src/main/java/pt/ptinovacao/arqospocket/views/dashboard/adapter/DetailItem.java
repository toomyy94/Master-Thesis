package pt.ptinovacao.arqospocket.views.dashboard.adapter;

/**
 * Data container to display data in the dashboard network details.
 * <p>
 * Created by Emílio Simões on 03-05-2017.
 */
public class DetailItem {

    private String name;

    private String value;

    public DetailItem(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

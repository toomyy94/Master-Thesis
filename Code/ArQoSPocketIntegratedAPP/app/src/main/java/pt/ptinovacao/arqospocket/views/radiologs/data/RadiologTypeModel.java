package pt.ptinovacao.arqospocket.views.radiologs.data;

import android.support.annotation.StringRes;

/**
 * Contains data required to display and select an anomaly type model.
 * <p>
 * Created by Tomás Rodrigues on 28-04-2017.
 */
public class RadiologTypeModel {

    private int id;

    @StringRes
    private int value;

    public RadiologTypeModel(int id, @StringRes int value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RadiologTypeModel that = (RadiologTypeModel) o;

        if (id != that.id) {
            return false;
        }
        return value == that.value;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + value;
        return result;
    }

    @Override
    public String toString() {
        return "RadiologTypeModel{" + "id=" + id + ", value=" + value + '}';
    }
}


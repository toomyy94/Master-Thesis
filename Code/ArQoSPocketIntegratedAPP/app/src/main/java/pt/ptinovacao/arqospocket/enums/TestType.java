package pt.ptinovacao.arqospocket.enums;

/**
 * Created by pedro on 23/04/2017.
 */

public enum  TestType {
    ONDEMAND(1);

    private int type;

    TestType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}

package pt.ptinovacao.arqospocket.core.network;

/**
 * Identifies the status of the device connection.
 * <p>
 * Created by Emílio Simões on 21-04-2017.
 */
public enum ConnectionTechnology {
    WIFI(1),
    MOBILE(2),
    NOT_CONNECTED(3),
    MIXED(4),
    NA(0);

    private final int value;

    private static final ConnectionTechnology[] VALUES = {
            WIFI,
            MOBILE,
            NOT_CONNECTED,
            MIXED,
            NA
    };

    ConnectionTechnology(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ConnectionTechnology fromConnectionTechnology(int value) {
        for (ConnectionTechnology state : VALUES) {
            if (state.getValue() == value) {
                return state;
            }
        }
        return NA;
    }
}

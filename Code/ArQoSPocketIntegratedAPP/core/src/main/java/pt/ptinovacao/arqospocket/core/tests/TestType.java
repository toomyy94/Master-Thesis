package pt.ptinovacao.arqospocket.core.tests;

/**
 * Identifies the possible test types.
 * <p>
 * Created by Emílio Simões on 11-04-2017.
 */
public enum TestType {
    DATE(0),
    BOOT(1),
    ITERATIONS(2),
    TIME_INTERVAL(3),
    WEEK(4),
    DATE_BEST_EFFORT(5),
    TIME_INTERVAL_AT_LEAST(6),
    USER_REQUEST(7),
    NONE(255);

    private final int type;

    private static final TestType[] VALUES = {
            DATE, BOOT, ITERATIONS, TIME_INTERVAL, WEEK,DATE_BEST_EFFORT,
            TIME_INTERVAL_AT_LEAST, USER_REQUEST
    };

    TestType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static TestType fromInt(int value) {
        if (value >= 0 && value <= 7) {
            return VALUES[value];
        } else if (value == 255) {
            return NONE;
        }

        throw new IndexOutOfBoundsException("There is no matching value for " + value);
    }

    public static TestType fromInt(int value, TestType defaultValue) {
        try {
            return fromInt(value);
        } catch (IndexOutOfBoundsException e) {
            return defaultValue;
        }
    }
}

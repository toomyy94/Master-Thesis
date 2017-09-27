package pt.ptinovacao.arqospocket.core.utils;

/**
 * Helper utility to check on which week days a test should be run.
 * <p>
 * Created by Emílio Simões on 12-04-2017.
 */
public class WeekDays {

    private final boolean[] days;

    public WeekDays(boolean[] days) {
        this.days = days;
    }

    public boolean sunday() {
        return dayAt(6);
    }

    public boolean monday() {
        return dayAt(5);
    }

    public boolean tuesday() {
        return dayAt(4);
    }

    public boolean wednesday() {
        return dayAt(3);
    }

    public boolean thursday() {
        return dayAt(2);
    }

    public boolean friday() {
        return dayAt(1);
    }

    public boolean saturday() {
        return dayAt(0);
    }

    boolean isActive(int day) {
        return dayAt(day);
    }

    private boolean dayAt(int index) {
        return days != null && days[index];
    }
}

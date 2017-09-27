package pt.ptinovacao.arqospocket.core.utils;

import java.util.Calendar;
import java.util.Date;

import pt.ptinovacao.arqospocket.core.tests.data.ParameterData;

/**
 * Helper class to calculate dates for the weekly events.
 * <p>
 * Created by Emílio Simões on 12-04-2017.
 */
public class WeekEventUtil {

    public static Date getNextExecutionDate(ParameterData data) {
        long now = Calendar.getInstance().getTimeInMillis();
        return getNextExecutionDate(now, data);
    }

    public static Date getNextExecutionDate(long now, ParameterData data) {
        Calendar calendar = now(eventHour(data.getHour()));
        long eventTime = calendar.getTimeInMillis();
        int currentDay = 7 - calendar.get(Calendar.DAY_OF_WEEK);

        WeekDays days = data.getWeekDays();
        if (days.isActive(currentDay) && now < eventTime) {
            return calendar.getTime();
        } else {
            int addDays = 0;
            for (int i = 7; i > 1; i--) {
                if (days.isActive((currentDay + i) % 7)) {
                    calendar.add(Calendar.DAY_OF_YEAR, addDays);
                    if (data.getInterval() > 0) {
                        calendar.add(Calendar.WEEK_OF_YEAR, data.getInterval());
                    }
                    return calendar.getTime();
                }
                addDays++;
            }
        }

        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }

    private static Calendar eventHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    private static Calendar now(Calendar eventHour) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, eventHour.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, eventHour.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, eventHour.get(Calendar.SECOND));
        return calendar;
    }
}

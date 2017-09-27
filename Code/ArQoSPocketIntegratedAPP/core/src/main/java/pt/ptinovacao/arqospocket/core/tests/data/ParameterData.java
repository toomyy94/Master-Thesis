package pt.ptinovacao.arqospocket.core.tests.data;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import pt.ptinovacao.arqospocket.core.serialization.HourJsonAdapter;
import pt.ptinovacao.arqospocket.core.serialization.WeekDaysJsonAdapter;
import pt.ptinovacao.arqospocket.core.utils.WeekDays;

/**
 * Contains the recursion parameters data associated with a test event.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class ParameterData {

    @SerializedName("count")
    private Integer count;

    @SerializedName("interval")
    private Integer interval;

    @SerializedName("hour")
    @JsonAdapter(HourJsonAdapter.class)
    private Date hour;

    @SerializedName("weekdays")
    @JsonAdapter(WeekDaysJsonAdapter.class)
    private WeekDays weekDays;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Date getHour() {
        return hour;
    }

    public void setHour(Date hour) {
        this.hour = hour;
    }

    public WeekDays getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(WeekDays weekDays) {
        this.weekDays = weekDays;
    }
}

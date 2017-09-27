package pt.ptinovacao.arqospocket.core.alarms.data;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.utils.JsonHelper;

/**
 * Contains a alarm parsed result.
 * <p>
 * Created by Tomás Rodrigues on 06-09-2017.
 */
public class AlarmResult {

    private static final String DATA = "data";

    @SerializedName(DATA)
    private AlarmData alarmData[];

    public AlarmData[] getAlarmData() {
        return alarmData;
    }

    public void setAlarmData(AlarmData[] alarmData) {
        this.alarmData = alarmData;
    }

    @Override
    public String toString() {
        return JsonHelper.getGsonInstance().toJson(this);
    }
}

package pt.ptinovacao.arqospocket.core.alarms.data;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.utils.JsonHelper;

//import pt.ptinovacao.arqospocket.core.serialization.AlarmDataJsonAdapter;

/**
 * Contains a alarm parsed result.
 * <p>
 * Created by Tomás Rodrigues on 06-04-2017.
 */
public class AlarmData {

    private static final String ORIGIN_ID = "originId";

    private static final String GPS = "gps";

    private static final String CELL_ID = "cellId";

    private static final String DATE = "date";

    private static final String START_END = "startEnd";

    private static final String ALARM_ID = "alarmId";

    private static final String ALARM_CONTENT = "alarmContent";

    private static final String ADDITIONAL_INFO = "additional_info";

    private static final String ICCID = "iccid";

    @SerializedName(ORIGIN_ID)
    private Integer originId;

    @SerializedName(GPS)
    private String gps;

    @SerializedName(CELL_ID)
    private String cellId;

    @SerializedName(DATE)
    private String date;

    @SerializedName(START_END)
    private String startEnd;

    @SerializedName(ALARM_ID)
    private String alarmId;

    @SerializedName(ALARM_CONTENT)
    private String alarmContent;

    @SerializedName(ADDITIONAL_INFO)
    private String additional_info;

    @SerializedName(ICCID)
    private String iccid;

    public Integer getOriginId() {
        return originId;
    }

    public void setOriginId(Integer originId) {
        this.originId = originId;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getCellId() {
        return cellId;
    }

    public void setCellId(String cellId) {
        this.cellId = cellId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartEnd() {
        return startEnd;
    }

    public void setStartEnd(String startEnd) {
        this.startEnd = startEnd;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public String getAlarmContent() {
        return alarmContent;
    }

    public void setAlarmContent(String alarmContent) {
        this.alarmContent = alarmContent;
    }

    public String getAdditionalInfo() {
        return additional_info;
    }

    public void setAdditionalInfo(String additional_info) {
        this.additional_info = additional_info;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    @Override
    public String toString() {
        return JsonHelper.getGsonInstance().toJson(this);
    }
}

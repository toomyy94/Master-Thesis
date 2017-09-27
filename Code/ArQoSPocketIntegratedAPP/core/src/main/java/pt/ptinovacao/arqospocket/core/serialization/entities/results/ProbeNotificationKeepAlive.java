package pt.ptinovacao.arqospocket.core.serialization.entities.results;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pedro on 16/05/2017.
 */
public class ProbeNotificationKeepAlive extends ProbeNotification {

    private static final String TIME_TO_REFRESH = "timeToRefresh";

    @SerializedName(TIME_TO_REFRESH)
    private int timeToRefresh;

    public int getTimeToRefresh() {
        return timeToRefresh;
    }

    public void setTimeToRefresh(int timeToRefresh) {
        this.timeToRefresh = timeToRefresh;
    }
}


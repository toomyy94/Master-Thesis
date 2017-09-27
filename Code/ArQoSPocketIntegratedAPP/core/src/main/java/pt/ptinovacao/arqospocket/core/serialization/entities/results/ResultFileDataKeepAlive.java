package pt.ptinovacao.arqospocket.core.serialization.entities.results;

/**
 * Created by pedro on 16/05/2017.
 */
public class ResultFileDataKeepAlive extends ResultFileData {

    public static class Builder extends ResultFileData.Builder {

        private int timeToRefresh;

        public int getTimeToRefresh() {
            return timeToRefresh;
        }

        public Builder timeToRefresh(int timeToRefresh) {
            this.timeToRefresh = timeToRefresh;
            return this;
        }

        public ResultFileDataKeepAlive build() {

            ResultFileDataKeepAlive data = new ResultFileDataKeepAlive();

            ProbeNotificationKeepAlive probeNotification = new ProbeNotificationKeepAlive();
            data.setNotification(probeNotification);

            setValues(probeNotification);

            probeNotification.setTimeToRefresh(timeToRefresh);

            return data;
        }
    }
}
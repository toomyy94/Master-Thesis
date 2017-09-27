package pt.ptinovacao.arqospocket.core.tests.data.results;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;

/**
 * Ping task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class PingTaskResult extends TaskResult {

    public static final String MINIMUM = "min_in_msec";

    public static final String MEDIUM = "med_in_msec";

    public static final String MAXIMUM = "max_in_msec";

    public static final String SENT_PACKETS = "sent_packets";

    public static final String RECEIVED_PACKETS = "received_packets";

    public static final String LOST_PACKETS = "lost_packets";

    @SerializedName(MINIMUM)
    private String minimum;

    @SerializedName(MEDIUM)
    private String medium;

    @SerializedName(MAXIMUM)
    private String maximum;

    @SerializedName(SENT_PACKETS)
    private String sentPackets;

    @SerializedName(RECEIVED_PACKETS)
    private String receivedPackets;

    @SerializedName(LOST_PACKETS)
    private String lostPackets;

    public PingTaskResult(ResolverInfo resolverInfo) {
        super(resolverInfo);
    }

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getMaximum() {
        return maximum;
    }

    public void setMaximum(String maximum) {
        this.maximum = maximum;
    }

    public String getSentPackets() {
        return sentPackets;
    }

    public void setSentPackets(String sentPackets) {
        this.sentPackets = sentPackets;
    }

    public String getReceivedPackets() {
        return receivedPackets;
    }

    public void setReceivedPackets(String receivedPackets) {
        this.receivedPackets = receivedPackets;
    }

    public String getLostPackets() {
        return lostPackets;
    }

    public void setLostPackets(String lostPackets) {
        this.lostPackets = lostPackets;
    }
}


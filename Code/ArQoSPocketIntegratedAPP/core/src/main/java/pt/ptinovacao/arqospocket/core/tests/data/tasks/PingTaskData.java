package pt.ptinovacao.arqospocket.core.tests.data.tasks;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;

/**
 * Ping task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class PingTaskData extends TaskData {

    public static final String PACKET_SIZE = "packet_size";

    public static final String INTERVAL = "interval";

    public static final String PACKET_NUMBER = "packet_number";

    public static final String PING_TIMEOUT = "ping_timeout";

    public static final String IP_ADDRESS = "ip_address";

    @SerializedName(PACKET_SIZE)
    private String packetSize;

    @SerializedName(INTERVAL)
    private String interval;

    @SerializedName(PACKET_NUMBER)
    private String packetNumber;

    @SerializedName(PING_TIMEOUT)
    private String pingTimeout;

    @SerializedName(IP_ADDRESS)
    private String ipAddress;

    public PingTaskData(ResolverInfo resolverInfo) {
        super(resolverInfo);
    }

    public String getPacketSize() {
        return packetSize;
    }

    public void setPacketSize(String packetSize) {
        this.packetSize = packetSize;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getPacketNumber() {
        return packetNumber;
    }

    public void setPacketNumber(String packetNumber) {
        this.packetNumber = packetNumber;
    }

    public String getPingTimeout() {
        return pingTimeout;
    }

    public void setPingTimeout(String pingTimeout) {
        this.pingTimeout = pingTimeout;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}


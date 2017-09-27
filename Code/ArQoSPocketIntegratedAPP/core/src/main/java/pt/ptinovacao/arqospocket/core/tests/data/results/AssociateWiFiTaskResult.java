package pt.ptinovacao.arqospocket.core.tests.data.results;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;

/**
 * Associate WiFi task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class AssociateWiFiTaskResult extends TaskResult {

    public static final String SSID = "ssid";

    public static final String MAC_ADDRESS = "mac";

    public static final String FREQUENCY = "frequency";

    public static final String CHANNEL = "channel";

    public static final String MODE = "mode";

    public static final String PROTOCOL = "protocol";

    public static final String BITRATE = "bitrate";

    public static final String ENCRYPTION = "encryption";

    public static final String ASSOCIATION_TIME_IN_SECONDS = "association_time_in_sec";

    public static final String SIGNAL_LEVEL = "signal_level";

    public static final String NOISE_LEVEL = "noise_level";

    public static final String RATIO_SIGNAL_NOISE = "ratio_signal_noise";

    public static final String ADDRESS = "address";

    public static final String MASK = "mask";

    public static final String GATEWAY = "gateway";

    public static final String DNS = "dns";

    public static final String DOMAIN = "domain";

    public static final String LEASE_IN_SECONDS = "lease_in_sec";

    @SerializedName(SSID)
    private String ssid;

    @SerializedName(MAC_ADDRESS)
    private String macAddress;

    @SerializedName(FREQUENCY)
    private String frequency;

    @SerializedName(CHANNEL)
    private String channel;

    @SerializedName(MODE)
    private String mode;

    @SerializedName(PROTOCOL)
    private String protocol;

    @SerializedName(BITRATE)
    private String bitrate;

    @SerializedName(ENCRYPTION)
    private String encryption;

    @SerializedName(ASSOCIATION_TIME_IN_SECONDS)
    private String associationTimeInSeconds;

    @SerializedName(SIGNAL_LEVEL)
    private String signalLevel;

    @SerializedName(NOISE_LEVEL)
    private String noiseLevel;

    @SerializedName(RATIO_SIGNAL_NOISE)
    private String ratioSignalNoise;

    @SerializedName(ADDRESS)
    private String address;

    @SerializedName(MASK)
    private String mask;

    @SerializedName(GATEWAY)
    private String gateway;

    @SerializedName(DNS)
    private String dns;

    @SerializedName(DOMAIN)
    private String domain;

    @SerializedName(LEASE_IN_SECONDS)
    private String leaseInSeconds;

    public AssociateWiFiTaskResult(ResolverInfo resolverInfo) {
        super(resolverInfo);
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getBitrate() {
        return bitrate;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    public String getAssociationTimeInSeconds() {
        return associationTimeInSeconds;
    }

    public void setAssociationTimeInSeconds(String associationTimeInSeconds) {
        this.associationTimeInSeconds = associationTimeInSeconds;
    }

    public String getSignalLevel() {
        return signalLevel;
    }

    public void setSignalLevel(String signalLevel) {
        this.signalLevel = signalLevel;
    }

    public String getNoiseLevel() {
        return noiseLevel;
    }

    public void setNoiseLevel(String noiseLevel) {
        this.noiseLevel = noiseLevel;
    }

    public String getRatioSignalNoise() {
        return ratioSignalNoise;
    }

    public void setRatioSignalNoise(String ratioSignalNoise) {
        this.ratioSignalNoise = ratioSignalNoise;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getLeaseInSeconds() {
        return leaseInSeconds;
    }

    public void setLeaseInSeconds(String leaseInSeconds) {
        this.leaseInSeconds = leaseInSeconds;
    }
}
